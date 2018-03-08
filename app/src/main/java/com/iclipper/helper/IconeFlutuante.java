package com.iclipper.helper;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.iclipper.Janela_Flutuante;
import com.iclipper.R;

import java.util.concurrent.ExecutionException;

/**
 * Created by Erico on 02/11/2017.
 */

public class IconeFlutuante extends Service{

    private WindowManager wm;
    private LinearLayout ll;
    private Button stop;
    private ImageView imagem;
    private static final int MAX_CLICK_DURATION = 200;
    private long startClickTime;
    private static final String ARQUIVO_PREFERENCIA = "icone";
    String texto;
    private int tamanhoTelaOrizontal;
    private int tamanhoIcone;
    CountDownT timer;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {return null;}

    //Recebe o texto a ser traduzido
    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        texto = intent.getStringExtra("texto");
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();





        //Salvar posicao do icone
        SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA,0);
        final SharedPreferences.Editor editor = sharedPreferences.edit();



        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        ll = new LinearLayout(this);
        stop = new Button(this);
        imagem = new ImageView(this);
        tamanhoTelaOrizontal = wm.getDefaultDisplay().getWidth();

        ViewGroup.LayoutParams telaParameters = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        stop.setText("X");

        stop.setLayoutParams(telaParameters);

        imagem.setBackgroundResource(R.drawable.icone_balao);
       // imagem.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);



        imagem.setLayoutParams(telaParameters);

        LinearLayout.LayoutParams llParameters = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.VERTICAL);
        ll.setLayoutParams(llParameters);


        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());

        final WindowManager.LayoutParams parameters = new WindowManager.LayoutParams(50,50,WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

        Display display = wm.getDefaultDisplay();
        parameters.width = (int) px;
        parameters.height = (int) px;

        parameters.x = 0;
        parameters.y = Integer.parseInt(sharedPreferences.getString("posicaoIcone","0"));
        tamanhoIcone = parameters.width;
        parameters.gravity = Gravity.LEFT| Gravity.CENTER;



        ll.addView(imagem);
        wm.addView(ll,parameters);

        ObjectAnimator animacao = ObjectAnimator.ofFloat(ll,"translationX",-px,0);
        animacao.setDuration(400);
        animacao.start();

        timer = new CountDownT(10000,10000);
        timer.start();

        ll.setOnTouchListener(new View.OnTouchListener() {
            private WindowManager.LayoutParams updateParameters = parameters;
            int x,y;
            float touchedX, touchedY;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startClickTime = System.currentTimeMillis();
                        //x = updateParameters.x;
                        y = updateParameters.y;

                        touchedX = motionEvent.getRawX();
                        touchedY = motionEvent.getRawY();

                        break;

                    case MotionEvent.ACTION_UP: {
                        long clickDuration = System.currentTimeMillis() - startClickTime;
                        //Verifica se o usuario clicou ou arrastou o icone
                        if(clickDuration < MAX_CLICK_DURATION) {

                            //Executa ao clicar no icone flutuante
                            updateParameters.gravity = Gravity.CENTER | Gravity.CENTER;

                            wm.updateViewLayout(ll,updateParameters);

                            wm.removeView(ll);
                            stopSelf();
                            timer.cancel();



                            try {
                                Intent window = new Intent(IconeFlutuante.this, Janela_Flutuante.class);
                                 window.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                               window.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                window.putExtra("texto",texto);
                                startActivity(window);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    }


                    case MotionEvent.ACTION_MOVE:
                        updateParameters.y = (int)(y + (motionEvent.getRawY() - touchedY));


                        //Salva a posição do icone definida pelo usuario
                        editor.putString("posicaoIcone",String.valueOf(updateParameters.y));
                        editor.commit();

                        wm.updateViewLayout(ll,updateParameters);
                }

                return false;
            }
        });


    }





    public class CountDownT extends CountDownTimer {
        public CountDownT(long inMillisSeconds, long TimeGap){
            super(inMillisSeconds,TimeGap);
        }

        @Override
        public void onTick(long l) {
        }

        @Override
        public void onFinish() {
            wm.removeView(ll);
            stopSelf();
        }
    }

}
