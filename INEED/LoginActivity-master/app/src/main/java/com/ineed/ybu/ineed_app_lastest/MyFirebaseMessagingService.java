package com.ineed.ybu.ineed_app_lastest;

/**
 * Created by SERPİL on 16.05.2017.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) { // Data mesajı içeriyor mu
            //Uygulama arkaplanda veya ön planda olması farketmez. Her zaman çağırılacaktır.
            //Gelen içerik json formatındadır.
            Log.d(TAG, "Mesaj data içeriği: " + remoteMessage.getData());

            //Json formatındaki datayı parse edip kullanabiliriz. Biz direk datayı Push Notification olarak bastırıyoruz

            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());


        }

        if (remoteMessage.getNotification() != null) { //Notification mesajı içeriyor mu
            //Uygulama arkaplanda ise burası çağrılmaz.Ön planda ise notification mesajı geldiğinde çağırılır
            //getBody() ile mesaj içeriği
            //getTitle() ile mesaj başlığı
            Log.d(TAG, "Mesaj Notification Başlığı: " + remoteMessage.getNotification().getTitle() + " " + "Mesaj Notification İçeriği: " + remoteMessage.getNotification().getBody());

            //Gelen içeriğe göre notifikasyon bildiriminde bulunma
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());

        }

    }

    public void sendNotification(String messageTitle, String messageBody) {

        Intent intent = new Intent(this, SearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        long[] pattern = {500, 500, 500, 500};

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.need_logo)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        try {

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notificationBuilder.setSound(alarmSound);

        } catch (Exception e) {
            e.printStackTrace();
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


}