package com.example.nirvansharma.ttsecure;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by Nirvan Sharma on 10/20/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "FirebaseMessagingService";

    public FirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        String urlKey = null , urlValue = null ;
        if(remoteMessage.getData().size() > 0){

            for(Map.Entry<String,String> entry: remoteMessage.getData().entrySet()){
                urlKey = entry.getKey();
                urlValue = entry.getValue();
            }
        }
        Log.d(TAG,"Url = " + urlKey);

        Map<String,String> data = remoteMessage.getData();
        String urlText = data.get("URL");
        Log.d(TAG,"UrlText = " + urlText);
        //Log.d(TAG, "onMessageReceived: Message Receive: \n" + "Title:" + title + "\n" + "Message: " + message);
    }



    @Override
    public void onDeletedMessages() {

    }

   /* private void sendNotification(String title,String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 *//* ID of notification *//*, notificationBuilder.build());
    }*/
}
