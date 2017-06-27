package com.aapkatrade.buyer.smsreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Validation;

/**
 * Created by PPC16 on 6/23/2017.
 */

public class SmsReceiver extends BroadcastReceiver
{

    private static SmsListener mListener;


    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        System.out.println("pdus------------"+pdus+"pdus.length--"+pdus.length);

        for(int i=0;i<pdus.length;i++)
        {
            SmsMessage smsMessage;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            {
                smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i],"3gpp");
                if (Validation.isNonEmptyStr(smsMessage.toString()))
                {
                    String messageBody = smsMessage.getMessageBody();
                    System.out.println("------------"+messageBody);
                    //Pass on the text to our listenmessageBodyer.
                    mListener.messageReceived(messageBody);
                }

            }
            else
            {
                smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                if (Validation.isNonEmptyStr(smsMessage.toString()))
                {
                    String messageBody = smsMessage.getMessageBody();
                    System.out.println("messageBody2------------"+messageBody);
                    //Pass on the text to our listener.
                    mListener.messageReceived(messageBody);
                }
            }
        }

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}

