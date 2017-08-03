package com.aapkatrade.buyer.smsreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.SharedPreferenceConstants;
import com.aapkatrade.buyer.general.Validation;

/**
 * Created by PPC16 on 6/23/2017.
 */

public class SmsReceiver extends BroadcastReceiver
{

    private AppSharedPreference appSharedpreference;


    final SmsManager sms = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {

        appSharedpreference = new AppSharedPreference(context);


      /*  final Bundle bundle = intent.getExtras();



            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                Log.e("SmsReceiver", "Exception smsReceiver length" +pdusObj.length);
                SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[0]);
                String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                String senderNum = phoneNumber;
                String message = currentMessage.getDisplayMessageBody().split(":")[0];

                message = message.substring(0, message.length()-1);
                Log.e("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                *//*for (int i = 0; i < pdusObj.length; i++) {


                    Log.e("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);


                    // Show Alert

                } // end for loop*//*
            } // bundle is null


    }*/


        Bundle data = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        System.out.println("pdus------------" + pdus + "pdus.length--" + pdus.length);

        for (int i = 0; i < pdus.length; i++) {
            SmsMessage smsMessage;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i], "3gpp");
                if (Validation.isNonEmptyStr(smsMessage.toString())) {
                    String messageBody = smsMessage.getMessageBody();
                    System.out.println("------------" + messageBody);
                    //Pass on the text to our listenmessageBodyer.
                    // mListener.messageReceived(messageBody);
                    appSharedpreference.setSharedPref(SharedPreferenceConstants.LASTEST_OTP.toString(), messageBody.replace("Your otp is ",""));
                }

            } else {


                smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                if (Validation.isNonEmptyStr(smsMessage.toString())) {
                    String messageBody = smsMessage.getMessageBody();

                    appSharedpreference.setSharedPref(SharedPreferenceConstants.LASTEST_OTP.toString(), messageBody.replace("Your otp is ",""));
                    System.out.println("messageBody2------------" + messageBody);
                    //Pass on the text to our listener.
//                    if(mListener != null) {
//                        mListener.messageReceived(messageBody);
//                    }
                }
            }
        }
    }



}

