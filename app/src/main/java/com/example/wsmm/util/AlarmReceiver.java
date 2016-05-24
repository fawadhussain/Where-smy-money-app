package com.example.wsmm.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.example.wsmm.R;

/**
 * Created by abubaker on 5/24/16.
 */
public class AlarmReceiver extends BroadcastReceiver {

    public static final int MY_NOTIFICATION_ID = 1;


    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);


        Intent in = new Intent(context, AlarmReceiver.class);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent Sender = PendingIntent.getActivity(context, 0, in, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Alarm")
                        .setContentText("Alarm is Ringing!")
                        .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                        .setLights(Color.GREEN, 3000, 3000);


        try {
            Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/"
                    + com.example.wsmm.R.raw.best);
            mBuilder.setSound(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mBuilder.setContentIntent(Sender);
        mNotificationManager.notify(MY_NOTIFICATION_ID, mBuilder.build());
    }

}
