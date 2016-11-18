package com.team11.cse4322.redalertuvnotifications;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by Cyril on 11/13/2016.
 */
public class NotificationIntentService extends IntentService implements AsyncResponse {

    private static final int notifyID = 1;
    private static final String actionStart = "Action Start";
    private static final String actionStop = "Action Stop";

    public NotificationIntentService(){
        super(NotificationIntentService.class.getSimpleName());
    }

    public static Intent createIntentStartNotificationService(Context context) {
        Intent intent = new  Intent(context, NotificationIntentService.class);
        intent.setAction(actionStart);
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(actionStop);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(getClass().getSimpleName(), "onHandleIntent, started handling a notification event");
        try {
            String action = intent.getAction();
            if (actionStart.equals(action)) {
                processStartNotification();
            }
            if (actionStop.equals(action)) {
                processDeleteNotification(intent);
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }

    }

    private void processDeleteNotification(Intent intent) {
        Log.d(getClass().getSimpleName(), "stopped");
    }

    private void processStartNotification() {
        // Do something here
        // data is stored it to a file

        final NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this);
        nBuilder.setContentTitle("UV_Alert: 2")// hardcoded for now
                .setAutoCancel(true)
                .setContentText("low risk")// hardcorded for now
                .setSmallIcon(R.drawable.pushpin);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, notifyID, new Intent(this, UV_Lookup.class), PendingIntent.FLAG_UPDATE_CURRENT);

        nBuilder.setContentIntent(pendingIntent);

        nBuilder.setDeleteIntent(AlertReceiver.getDeleteIntent(this));

        final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notifyID, nBuilder.build());
    }

    @Override
    public void processFinish(String output) {

    }
}
