package com.iclipper;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.iclipper.helper.IconeFlutuante;
import com.iclipper.helper.ScaleImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class TelaBloqueio extends AppCompatActivity {
    private AdView mAdView;
    public static boolean aberta = false;
    ImageView fundo;
    TextView relogio;
    TextView diaMes;
    TextView alarm;
    ContadorRelogio timer;
    TextView alternativa1;
    TextView alternativa2;
    TextView alternativa3;
    ImageButton btDesativarAlerta;
    ImageButton btDeletarCard;
    boolean cardOpcoes ;
    ImageButton btMenuCard;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_bloqueio);
        fundo = findViewById(R.id.fundo);
        relogio = findViewById(R.id.relogio);
        diaMes = findViewById(R.id.diaMes);
        alarm = findViewById(R.id.alarm);
        cardOpcoes = false;
        alternativa1 =  findViewById(R.id.alternativa1);
        alternativa2 =  findViewById(R.id.alternativa2);
        alternativa3 =  findViewById(R.id.alternativa3);

        btDesativarAlerta = findViewById(R.id.desativar_alerta);
        btDesativarAlerta.setVisibility(View.INVISIBLE);
        btDeletarCard = findViewById(R.id.card_delete);
        btDeletarCard.setVisibility(View.INVISIBLE);
        btMenuCard =  findViewById(R.id.card_opcoes);

        //Define o fundo igual ao wallpaper da home
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        final Drawable wallpaperDrawable = wallpaperManager.getDrawable();
       // final Bitmap imagemFundo = new Bitmap(wallpaperDrawable);

        fundo.setImageDrawable(wallpaperDrawable);




        //Exibe o anuncio
        MobileAds.initialize(this, "ca-app-pub-3066317516488763~6619236447");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        criaRelogio();

        //Inicia o relogio
        timer = new ContadorRelogio(1000,1000);
        timer.start();

        


       alternativa1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               //Efeito
               YoYo.with(Techniques.Shake)
                       .duration(800)
                       .repeat(0)
                       .playOn(findViewById(R.id.alternativa1));


               alternativa1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.arredondar_tela_bloqueio_red));
               alternativa1.setTextColor(Color.WHITE);

           }
       });
        alternativa2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* //Efeito
                YoYo.with(Techniques.Shake)
                        .duration(800)
                        .repeat(0)
                        .playOn(findViewById(R.id.alternativa1));*/


                alternativa2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.arredondar_tela_bloqueio_green));

               YoYo.with(Techniques.TakingOff)
                        .duration(800)
                        .playOn(findViewById(R.id.alternativa2));



                //alternativa1.setTextColor(Color.WHITE);

            }
        });

        btMenuCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!cardOpcoes) {
                    btMenuCard.setImageDrawable(getResources().getDrawable(R.drawable.ic_more_horiz_black_24dp));
                    //Animação icone desativar
                    btDesativarAlerta.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeInRight)
                            .duration(200)
                            .playOn(btDesativarAlerta);
                    //Animação icone deletar
                    btDeletarCard.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeInRight)
                            .duration(200)
                            .playOn(btDeletarCard);
                    cardOpcoes = true;
                }else{
                    btMenuCard.setImageDrawable(getResources().getDrawable(R.drawable.ic_more_vert_black_24dp));
                    //Animação icone desativar
                    YoYo.with(Techniques.FadeOutRight)
                            .duration(200)
                            .playOn(btDesativarAlerta);
                    //Animação icone deletar
                    YoYo.with(Techniques.FadeOutRight)
                            .duration(200)
                            .playOn(btDeletarCard);
                    cardOpcoes = false;
                }

            }
        });




    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void criaRelogio(){
        //Formata Hora
        SimpleDateFormat formatRelogio = new SimpleDateFormat("HH:mm");
        Date hora = Calendar.getInstance().getTime(); // Ou qualquer outra forma que tem
        String dataFormatada = formatRelogio.format(hora);
        relogio.setText(dataFormatada);

        //Formata dia e mes
        SimpleDateFormat formatDiaMes = new SimpleDateFormat("E, MMM d");
        String diaMesFormatado = formatDiaMes.format(hora).toUpperCase();
        diaMes.setText(diaMesFormatado);

        //Pega o horario do alarm
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        String nextAlarm = Settings.System.getString(getContentResolver(),
                Settings.System.NEXT_ALARM_FORMATTED);
        alarm.setText(nextAlarm);
    }
    @Override
    public void onStart() {
        super.onStart();
        aberta = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        aberta = false;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        aberta = false;
    }

    public class ContadorRelogio extends CountDownTimer {
        public ContadorRelogio(long inMillisSeconds, long TimeGap){
            super(inMillisSeconds,TimeGap);

        }

        @Override
        public void onTick(long l) {

        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onFinish() {
            //Formata Hora
            criaRelogio();

            timer.start();


        }
    }





}
