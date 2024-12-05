package com.example.phoneregistrator

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.phoneregistrator.ui.theme.PhoneRegistratorTheme
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

public var MESSAGES = ArrayList<String>()
public var COUNTER = 0

public fun incrementCounter(){
    COUNTER = COUNTER+1;
}

public fun sendMessage(message:String){
    Log.i("MainActivity", "Sending message: $message");
    val smsManager = SmsManager.getDefault()
    smsManager.sendTextMessage("+995577111942", null, message, null, null)
}

public fun sendNext(number:Int){
    sendMessage(MESSAGES[number]);
}

class MainActivity : ComponentActivity() {

    private val REQUEST_CODE_PERMISSIONS = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        val readFileButton = findViewById<Button>(R.id.ReadFile)
        val readLinesText = findViewById<TextView>(R.id.readLines)
        var sendSmsButton = findViewById<Button>(R.id.SendSms)

        readFileButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_SMS,
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    REQUEST_CODE_PERMISSIONS
                )
            } else {
                readMessagesFromFile()

                var i = 0;
                while (i < MESSAGES.count()) {
                    readLinesText.setText(readLinesText.text.toString() + "\n" + MESSAGES[i]);

                    i++
                }

                readLinesText.refreshDrawableState()
            }
        }

        sendSmsButton.setOnClickListener {
            sendNext(0);
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if
                    (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED
//                && grantResults[3] == PackageManager.PERMISSION_GRANTED
            ) {
                readMessagesFromFile()
            } else {
                Toast.makeText(
                    this,
                    "SMS and Storage permissions are required.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun readMessagesFromFile() {
        try {
            val reader = BufferedReader(InputStreamReader(assets.open("messages.txt")))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                MESSAGES.add(line!!)
            }
            reader.close()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error reading file.", Toast.LENGTH_SHORT).show()
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PhoneRegistratorTheme {
        Greeting("Android")
    }
}