package com.aapkatrade.buyer.Notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.aapkatrade.buyer.MainActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.home.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;

/**
 * Created by PPC16 on 6/3/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;
    Intent resultIntent;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        RemoteMessage.Notification notification = remoteMessage.getNotification();



        resultIntent = new Intent(getApplicationContext(), MainActivity.class);
        resultIntent.putExtra("message", notification.getBody());

//


        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());

            try {
                JSONObject json = new JSONObject(notification.getBody());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e("json Exception", "Exception1: " + e.getMessage());
            }


        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e("json Exception", "Exception2: " + e.getMessage());
            }
        }




    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(AppSharedPreference.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {


            String NotificationType = json.getString("type");
            JSONObject NotificationBody = json.getJSONObject("body");
            String last_message = NotificationBody.getString("last_message");
            String last_id = NotificationBody.getString("last_id");


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

                resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("message", last_message);

                showNotificationMessage(getApplicationContext(), "AapkaTrade", last_message, System.currentTimeMillis() + "", resultIntent);
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(AppSharedPreference.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", last_message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
                handleNotification(last_message);
            } else {

                handleNotification(last_message);

                resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("message", last_message);

                showNotificationMessage(getApplicationContext(), "AapkaTrade", last_message, System.currentTimeMillis() + "", resultIntent);

                handleNotification(last_message);
                showNotificationMessage(getApplicationContext(), getString(R.string.app_name), last_message, "", resultIntent);
                // app is in background, show the notification in notification tray

                // check for image attachment
                if (TextUtils.isEmpty("")) {
                    showNotificationMessage(getApplicationContext(), getString(R.string.app_name), last_message, "", resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), getString(R.string.app_name), last_message, "", resultIntent, "");
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.toString());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.toString());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(getApplicationContext(), title, message, timeStamp, intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, "");
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(getApplicationContext(), title, message, timeStamp, intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }


}
