package com.example.notify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private NotificationManager notificationManager;
    private final static String CHANNEL_ID = "primary-channel";
    private int NOTIFICATION_ID = 0;
    private final static String ACTION_UPDATE_NOTIF = "action-update-notif";
    private NotificationReceiver notificationReceiver = new NotificationReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "App Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        findViewById(R.id.btn_notify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });

        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNotification();
            }
        });

        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationManager.cancel(NOTIFICATION_ID);
            }
        });

        registerReceiver(notificationReceiver, new IntentFilter(ACTION_UPDATE_NOTIF));
    }

    private NotificationCompat.Builder getNotificationBuilder() {
        Intent notificationIntent = new Intent(this, SecondActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.
                getActivity(this, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("PERINGATAN TSUNAMI TROFI!!!")
                .setContentText("Manchester United akan mendapatkan banyak trophy musim ini")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(notificationPendingIntent);
        return notifyBuilder;
    }

    private void sendNotification() {
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIF);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID,
                updateIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        notifyBuilder.addAction(R.drawable.ic_android, "Update", updatePendingIntent);
        notificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

    private void updateNotification() {
        Bitmap androidImage = BitmapFactory.decodeResource(getResources(), R.drawable.roket);
        NotificationCompat.Builder notifiyBuilder = getNotificationBuilder();
        notifiyBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(androidImage)
                .setBigContentTitle("GGMU!"));
        notificationManager.notify(NOTIFICATION_ID, notifiyBuilder.build());
    }

    public class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_UPDATE_NOTIF)) {
                updateNotification();
            }
        }
    }

}