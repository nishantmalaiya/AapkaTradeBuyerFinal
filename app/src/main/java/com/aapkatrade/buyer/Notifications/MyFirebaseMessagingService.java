package com.aapkatrade.buyer.Notifications;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.aapkatrade.buyer.MainActivity;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.chat.ChatActivity;
import com.aapkatrade.buyer.chat.ChatDatas;
import com.aapkatrade.buyer.general.AppSharedPreference;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Utils.ParseUtils;
import com.aapkatrade.buyer.home.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 * Created by PPC16 on 6/3/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;
    Intent resultIntent;
    ArrayList<ChatDatas> chatDatasNotification = new ArrayList<>();
    String chat_id, name;
    TimerTask timerTask;
    Context mcontext;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        mcontext = getApplicationContext();
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            // RemoteMessage.Notification notification = remoteMessage.getNotification();
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());


        }


    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            try {
                JSONObject jsonObject = new JSONObject(message.toString());
                handleDataMessage(jsonObject);
                Log.e(TAG, "push json Background  " + jsonObject.toString());
            } catch (Exception e) {
                Log.e("json Exception", "Exception1: " + e.toString());
            }
            // app is in foreground, broadcast the push message

        } else {


            try {

                JSONObject jsonObject = new JSONObject(message.toString());
               /* JsonParser parser = new JsonParser();

                JsonObject json = parser.parse(message).getAsJsonObject();*/
                handleDataMessage(jsonObject);
                Log.e(TAG, "push json UI  " + jsonObject.toString());
            } catch (Exception e) {
                Log.e("json Exception", "Exception2: " + e.toString());
            }

            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        try {

            String NotificationType = json.get("type").toString();


            JSONObject NotificationBody = json.getJSONObject("body");
            String last_message = NotificationBody.get("last_message").toString();
            String last_id = NotificationBody.get("last_id").toString();

            JSONArray list = NotificationBody.getJSONArray("list");


            for (int k = 0; k < list.length(); k++) {


                JSONObject jsonObject = list.getJSONObject(k);

                String message = jsonObject.get("msg").toString();
                name = jsonObject.get("name_support").toString();
                String user_id = jsonObject.get("user_id").toString();
                chat_id = jsonObject.get("chat_id").toString();

                boolean you;

                if (user_id.contains("1")) {

                    you = true;


                } else {

                    you = false;

                }
                long time = Long.parseLong(jsonObject.get("time").toString());

                chatDatasNotification.add(new ChatDatas(message, name, time, you));


            }


            resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
            resultIntent.putExtra("message", last_message);
            resultIntent.putExtra("chatid", chat_id);
            resultIntent.putExtra("name", name);
            resultIntent.putExtra("jsonarray_string", list.toString());
            resultIntent.putExtra("className", "NotifyChat");
            Log.e("json Exception", "Exception2: " + "NotRunning");


            if (isRunning()) {



                notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound(mcontext);




                chatDatasNotification.clear();
                chatDatasNotification.add(new ChatDatas(last_message, name, System.currentTimeMillis(), false));

                ChatActivity.commonInterface.getData(chatDatasNotification);
            } else {

                showNotificationMessage(getApplicationContext(), "AapkaTrade", last_message, System.currentTimeMillis() + "", resultIntent);

            }

        } catch (Exception e) {
            Log.e("json Exception", "Exception2: " + e.toString());
        }

    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(getApplicationContext(), title, message, timeStamp, intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, "");


        Intent pushNotification = new Intent(AppSharedPreference.PUSH_NOTIFICATION);
        pushNotification.putExtra("message", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);


        notificationUtils.playNotificationSound(context);

    }


    public boolean isRunning() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(getApplicationContext().ACTIVITY_SERVICE);


        List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1);

        Log.e("topActivity", "CURRENT Activity ::"
                + taskInfo.get(0).topActivity.getClassName() + "*****");

        ComponentName componentInfo = taskInfo.get(0).topActivity;
        //--- To get currently active activity in foreground. We can use getShortClassName() & getClassName()
        String classname = componentInfo.getClassName();
        //---get package name of currently running application in foreground. We can use getPackageName()
        String packagename = componentInfo.getPackageName();


        if (taskInfo.get(0).topActivity.getClassName().equalsIgnoreCase("com.aapkatrade.buyer.chat.ChatActivity"))
            return true;


        return false;
    }

}
