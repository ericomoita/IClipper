package com.iclipper.helper;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;


import com.iclipper.Home;
import com.iclipper.MainActivity;
import com.iclipper.R;

import java.sql.SQLOutput;
import java.util.Random;

/**
 * Created by Erico on 06/11/2017.
 */

public class MonitorarClipboard extends Service {
    String a;
    ClipboardManager clipboard;

    SharedPreferences sharedPreferences;
    SQLiteDatabase bancoDados;
    private BroadcastReceiver mReceiver;

    //@Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {


         bancoDados = openOrCreateDatabase("app", MODE_PRIVATE, null);
        sharedPreferences = getSharedPreferences("ARQUIVO_PREFERENCIA", 0);

        //Monitora o clipborad
        clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            public void onPrimaryClipChanged() {
                a = clipboard.getText().toString();

                if (a != null) {
                    //Abre o icone de notificação passando o texto a ser traduzido
                    Intent intent = new Intent(MonitorarClipboard.this, IconeFlutuante.class);
                    intent.putExtra("texto", a);
                    startService(intent);
                }


            }
        });

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);

        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);

        //Recebe o evento que a tela foi bloqueada
        mReceiver = new PhoneUnlockedReceiver();
        registerReceiver(mReceiver, filter);




    }






}

