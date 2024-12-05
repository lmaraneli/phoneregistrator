package com.example.phoneregistrator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
        {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String
            str = "";

            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        if( msgs[i].getOriginatingAddress().contains("577111942")) {
                            str += msgs[i].getMessageBody().toString();

                            if(str.toLowerCase().equals("success")){
                                // TODO: Success code
                                Log.i("Success", "onReceive: success");

                                MainActivityKt.setCOUNTER(MainActivityKt.getCOUNTER() + 1);

                                int counter = MainActivityKt.getCOUNTER();
                                if(counter < MainActivityKt.getMESSAGES().size()) {
                                    MainActivityKt.sendNext(MainActivityKt.getCOUNTER());
                                }
                            }
                            else {
                                // TODO: Error code
                                Log.i("error", "onReceive: error");

                                MainActivityKt.sendNext(MainActivityKt.getCOUNTER());
                            }
                        }
                    }

                } catch (Exception e) {
                    Log.e("SmsReceiver", "Exception in SmsReceiver", e);
                }
            }
        }
    }
}

//SMS from +995577111942 :Okay
