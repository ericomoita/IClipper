package com.iclipper.helper;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.iclipper.TelaBloqueio;

import static android.content.Context.POWER_SERVICE;

/**
 * Created by ANDROID on 19/03/2018.
 */

public class PhoneUnlockedReceiver extends BroadcastReceiver {


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {


        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            if (!TelaBloqueio.aberta) {
                //start activity
                Intent i = new Intent();
                i.setClassName("com.iclipper", "com.iclipper.TelaBloqueio");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
                // onPause() will be called.
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                System.out.println("In Method:  ACTION_SCREEN_ON");
//onResume() will be called.

//Better check for whether the screen was already locked
// if locked, do not take any resuming action in onResume()

                //Suggest you, not to take any resuming action here.
            } else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
                System.out.println("In Method:  ACTION_USER_PRESENT");
//Handle resuming events
            }
       /* KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
        if (keyguardManager.isKeyguardSecure()) {

            if (!TelaBloqueio.aberta) {
                //start activity
                Intent i = new Intent();
                i.setClassName("com.iclipper", "com.iclipper.TelaBloqueio");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }

        }*/
        }
    }