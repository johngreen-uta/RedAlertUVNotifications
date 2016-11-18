package com.team11.cse4322.redalertuvnotifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Cyril on 11/13/2016.
 */

public class NotificationStarter extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        AlertReceiver.setupAlarm(context);
    }
}
