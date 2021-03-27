package com.openclassrooms.realestatemanager.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.openclassrooms.realestatemanager.R;

public class NotificationHelper {

    public static void sendNotifications(Context context, String title, String content){

        //Call the method to construct channel, required for newer android versions
        createNotificationChannel(context);

        //Setup notification content
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "EstateChannelID")
                .setSmallIcon(R.drawable.ic_baseline_house_24)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        //Show Notif
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(42, builder.build());
    }

    public static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "EstateChannelName";
            String description = "EstateChannelDesc";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("EstateChannelID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
