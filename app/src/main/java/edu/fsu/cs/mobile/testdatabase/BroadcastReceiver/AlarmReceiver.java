package edu.fsu.cs.mobile.testdatabase.BroadcastReceiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import edu.fsu.cs.mobile.testdatabase.MainActivity;
import edu.fsu.cs.mobile.testdatabase.R;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long when = System.currentTimeMillis();
        NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent mNotificationIntent = new Intent(context, MainActivity.class);
        mNotificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, mNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.flashcardio_launcher)
                .setContentTitle("FlashCard.io")
                .setContentText("Reminder to study your cards!")
                .setAutoCancel(true)
                .setWhen(when)
                .setContentIntent(mPendingIntent);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
