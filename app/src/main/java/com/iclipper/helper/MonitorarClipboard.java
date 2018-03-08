package com.iclipper.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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
    CountDownT timer;
    SharedPreferences sharedPreferences;
    SQLiteDatabase bancoDados;

    //@Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {


         bancoDados = openOrCreateDatabase("app", MODE_PRIVATE, null);
        sharedPreferences = getSharedPreferences("ARQUIVO_PREFERENCIA", 0);




        //SharedPreferences.Editor editor = sharedPreferences.edit();

        clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            public void onPrimaryClipChanged() {
                a = clipboard.getText().toString();


                    /*Intent window = new Intent(MonitorarClipboard.this, IconeFlutuante.class);
                    window.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(window);*/

               // String regex = "[\d]+";
                //String str = "12a3";
                if (a != null) {
                    //Abre o icone de notificação passando o texto a ser traduzido
                    Intent intent = new Intent(MonitorarClipboard.this, IconeFlutuante.class);
                    intent.putExtra("texto", a);
                    startService(intent);
                }


            }
        });

        timer = new MonitorarClipboard.CountDownT(10000, 10000);
        timer.start();
        //Toast.makeText(getApplicationContext(),"Teste",Toast.LENGTH_LONG).show();


    }
    public static boolean soContemNumeros(String a) {
        if(a == null)
            return false;
        for (char letra : a.toCharArray())
            if(letra < '0' || letra > '9')
                return false;
        return true;

    }

    public String palavraAleatoria() {
        String palavra = null;
        Random random = new Random();
        try {
            //Criar cursor para percorrer registros da tabela
            Cursor cursor = bancoDados.rawQuery("SELECT original FROM traducoes", null);

            int indiceColunaOriginal = cursor.getColumnIndex("original");
            //int indiceColunaTraduzido = cursor.getColumnIndex("traduzido");
            int posicao = random.nextInt(cursor.getCount());
            cursor.moveToFirst();

            //Toast.makeText(getApplicationContext(),cursor.getColumnCount(),Toast.LENGTH_SHORT).show();



                cursor.moveToPosition(posicao);
                    palavra = cursor.getString(indiceColunaOriginal);


                cursor.moveToNext();


        }catch (Exception e){
            e.printStackTrace();
        }

        return palavra;
    }







    public class CountDownT extends CountDownTimer {

        public CountDownT(long inMillisSeconds, long TimeGap){
            super(inMillisSeconds,TimeGap);
        }

        @Override
        public void onTick(long l) {
            //Toast.makeText(getApplicationContext(),"Teste",Toast.LENGTH_LONG).show();

        }

        @Override
        public void onFinish() {

           // Toast.makeText(getApplicationContext(),"Teste",Toast.LENGTH_LONG).show();
            //

           // Toast.makeText(getApplicationContext(),sharedPreferences.getString("exibirAlerta","Valor não encontrado"),Toast.LENGTH_SHORT).show();
            if(sharedPreferences.getBoolean("exibirAlerta",false)){
               // Intent intent = new Intent(MonitorarClipboard.this, Janela_Alerta.class);
               // intent.putExtra("texto","teste");
               // startActivity(intent);


                //Define funcao do botao
                Intent entrarIntent = new Intent(MonitorarClipboard.this, Home.class);
               // entrarIntent.putExtra("notificacaoAberta",true);
                entrarIntent.setAction( "action-entrar" );
                PendingIntent entrarPI = PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        entrarIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                        );
                //Cria botão
                NotificationCompat.Action btSim = new NotificationCompat.Action(
                        R.drawable.ic_sentiment_satisfied,
                        "Sim",
                        entrarPI);

                NotificationCompat.Action btNao = new NotificationCompat.Action(
                        R.drawable.ic_sentiment_dissatisfied,
                        "Não",
                        entrarPI);


                //Cria notificacao
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(MonitorarClipboard.this)
                                .setSmallIcon(R.drawable.icone_balao)
                                .setContentTitle("Lembrete")
                                //.setOngoing(true)
                                .setContentText("Você lembra da tradução da palavra: "+palavraAleatoria()+"?")
                                .setStyle( new NotificationCompat.BigTextStyle().bigText( "Você lembra da tradução da palavra: "+palavraAleatoria()+"?" ) )
                                //.setStyle( new NotificationCompat.BigTextStyle() )
                                .addAction( btSim )
                                .addAction( btNao )

                                .setAutoCancel(true);

                    fecharNotificação();
                //mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;



                int mNotificationId = 001;

                NotificationManager  mNotifyMgr =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                mNotifyMgr.notify(mNotificationId, mBuilder.build());


                //NotificationManagerCompat.from(inContext).cancel(notificationId, 0));
            }

            timer.start();

        }
        public void fecharNotificação(){

        }
    }


}

