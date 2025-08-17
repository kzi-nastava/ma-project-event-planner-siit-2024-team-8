package com.example.myapplication.services;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.IBinder;
import android.view.ContextThemeWrapper;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.R;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.domain.Notification;
import com.example.myapplication.domain.dto.NotificationResponse;
import com.example.myapplication.utilities.NotificationsUtils;

public class NotificationForegroundService extends Service {
    private NotificationService notificationService;

    private static final String CHANNEL_ID = "notif_channel";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String token = intent.getStringExtra("token");

        notificationService = new NotificationService();
        notificationService.connect(token, new NotificationService.NotificationListener() {
            @Override
            public void onNotificationReceived(Notification notification) {
                /*
                    ContextThemeWrapper contextWrapper = new ContextThemeWrapper(
                        getApplicationContext(),
                        R.style.primaryTheme
                );
                NotificationsUtils.getInstance().showServerNotification(contextWrapper, notification);
                 */
                sendSystemNotification(notification);
            }

            @Override
            public void onNotificationClicked(NotificationResponse notification) {

            }

        });

        // Create foreground notification so Android lets it run
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notif_channel")
                .setContentTitle("Notification Service Running")
                .setSmallIcon(R.drawable.outline_notifications_none_24)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        startForeground(1, builder.build(), ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC);

        return START_STICKY;
    }

    private void sendSystemNotification(Notification notification) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.planner_no_bg)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setDefaults(android.app.Notification.DEFAULT_ALL); // sound + vibration

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        int notificationId = (int) System.currentTimeMillis(); // unique ID
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(notificationId, builder.build());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (notificationService != null) {
            notificationService.disconnect();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // Not binding in this example
    }
}