package com.iclipper.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Erico on 06/11/2017.
 */

public class IniciaBoot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent i) {
        Intent intent = new Intent(context, MonitorarClipboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(intent);
    }
}
