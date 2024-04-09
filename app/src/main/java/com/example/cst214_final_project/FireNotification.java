package com.example.cst214_final_project;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

/**
 * Jared Striemer CST214
 * COSC195 Final Project - King Wheels Mania
 * Fire Notification File - Handles all the scheduled notification code
 */
public class FireNotification extends Service
{
    public FireNotification()
    {
        //Empty Constructor
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate()
    {
        NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Create a Notification
        Notification notify;

        //Create a Pending Intent to open/ re open the Main Activity (Home Screen)
        Intent intent = new Intent(this, MainActivity.class);

        //Wrap "intent" in Pending Intend
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        //Build Configuration for different versions on devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            //Configure channel to be used/ then add notifications to it
            String channel = "2222";
            CharSequence name = "channel2222";

            int importance = NotificationManager.IMPORTANCE_LOW; //Make the importance low since its just a game app

            //Create the Channel
            NotificationChannel nChannel = new NotificationChannel(channel, name, importance);

            //Make the Manager aware of the created Channel - And pass information to it
            notifyManager.createNotificationChannel(nChannel);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setChannelId(channel)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setTicker("Ticker Notify")
                    .setContentTitle("King Wheels Slot Mania")
                    .setContentText("We miss you come back!")
                    .setAutoCancel(false)
                    .setContentIntent(pendingIntent);

            notify = builder.build(); //Build the notification
        }
        else
        {
            Notification.Builder builder = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker("Ticker Notify")
                    .setContentTitle("King Wheels Slot Mania")
                    .setContentText("We miss you come back!")
                    .setAutoCancel(false)
                    .setContentIntent(pendingIntent);

            notify = builder.build(); //Build the notification
        }

        //Use the Notification Manager to create the Notification and make it appear
        notifyManager.notify(1, notify);

    }

}
