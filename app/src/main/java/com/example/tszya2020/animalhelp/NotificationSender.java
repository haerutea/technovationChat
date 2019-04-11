package com.example.tszya2020.animalhelp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.tszya2020.animalhelp.object_classes.Constants;

public class NotificationSender
{
    //https://developer.android.com/training/notify-user/build-notification#java
    public static void setNotif(Context inContext, Class destinationClass,
                                String notifTitle, String notifContent, boolean startSpecialActivity)
    {

        Intent intent = new Intent(inContext, destinationClass);
        PendingIntent notifyPendingIntent;

        if(startSpecialActivity)
        {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            notifyPendingIntent = PendingIntent.getActivity(
                    inContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        else
        {
            notifyPendingIntent = PendingIntent.getActivity(inContext, 0, intent, 0);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(inContext, Constants.CHANNEL_ID)
                .setSmallIcon(R.drawable.chat_notif_icon)
                .setContentTitle(notifTitle)
                .setContentText(notifContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(notifyPendingIntent);

        setChannel(inContext);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(inContext);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(Constants.CHAT_NOTIF_ID, builder.build());

    }

    private static void setChannel(Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = Constants.CHANNEL_NAME;
            String description = Constants.CHANNEL_DES;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

