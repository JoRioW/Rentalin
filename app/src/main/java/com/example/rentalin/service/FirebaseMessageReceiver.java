package com.example.rentalin.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.rentalin.R;
import com.example.rentalin.ui.HomeActivity;
import com.example.rentalin.ui.PaymentActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FirebaseMessageReceiver extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        if (message.getNotification() != null){
            String title = message.getNotification().getTitle();
            String body = message.getNotification().getBody();

            if (!message.getData().isEmpty()){
                Map<String, String> data = message.getData();
                handleDataPayload(data, title, body);
            }else{
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("body", body);

                sendNotification(title, body, intent);
            }
        }
    }

    private void handleDataPayload(Map<String, String> data, String title, String body){
        String actionType = data.get("action_type");
        Intent intent;

        switch (actionType){
            case "payment_success":
                intent = new Intent(this, HomeActivity.class);
                break;
            default:
                intent = new Intent(this, HomeActivity.class);
                break;
        }

        for (Map.Entry<String, String> entry : data.entrySet()) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }

        intent.putExtra("title", title);
        intent.putExtra("body", body);

        sendNotification(title, body, intent);
    }

    private void sendNotification(String title, String body, Intent intent){
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "NotifChannel";

        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(
                    channelId,
                    "Push Notification Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            manager.createNotificationChannel(channel);
        }


        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        manager.notify(0, notifBuilder.build());
    }
}