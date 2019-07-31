package com.android.quyentraining.sevices;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.quyentraining.R;
import com.android.quyentraining.activities.splashscreen.SplashScreenActivity;
import com.android.quyentraining.ultis.AppConstain;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("message", "From: " + remoteMessage.getFrom());
        if (remoteMessage.getNotification() != null) {
            Log.d("message", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            if (remoteMessage.getNotification().getTitle() != null && remoteMessage.getNotification().getBody() != null) {
                Log.d("message", "Message Notification Title: " + remoteMessage.getNotification().getTitle());
                sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
            }
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

    private void sendNotification(String message, String title) {
        Intent intent = new Intent(this, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, AppConstain.CHANNEL_ID)
                .setContentTitle(title).setContentText(message).setAutoCancel(true).setSound(defaultSoundUri).setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(AppConstain.CHANNEL_ID, title, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationBuilder.setSmallIcon(R.drawable.launcher);
        Notification notification = new Notification();
        notification.number += notification.number;
        notificationBuilder.setNumber(notification.number);
        notificationManager.notify(0, notificationBuilder.build());
    }
}
