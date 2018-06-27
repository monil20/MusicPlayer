package com.example.monilandharia.musicplayer.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.example.monilandharia.musicplayer.MainActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by monil20 on 6/23/18.
 */

public class SmsReceiver extends BroadcastReceiver {

    //interface
    private static SmsListener mListener;

    public static final String REGEX = "PLAY .+?";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context.getSharedPreferences("com.example.monilandharia.musicplayer", Context.MODE_PRIVATE).getBoolean("isSms", false)) {
            Bundle myBundle = intent.getExtras();
            SmsMessage[] messages = null;
            String strMessage = "";

            if (myBundle != null) {
                Object[] pdus = (Object[]) myBundle.get("pdus");

                messages = new SmsMessage[pdus.length];

                for (int i = 0; i < messages.length; i++) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String format = myBundle.getString("format");
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                    } else {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
//                strMessage += "SMS From: " + messages[i].getOriginatingAddress();
//                strMessage += " : ";
                    strMessage += messages[i].getMessageBody();
//                strMessage += "\n";
                }

                Pattern pattern = Pattern.compile(REGEX);
                Matcher matcher = pattern.matcher(strMessage);
                if (matcher.matches()) {
                    while (matcher.find()) {
                        strMessage = matcher.group();
                    }
                    Log.e("SMS", strMessage);
                    Toast.makeText(context, strMessage, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}