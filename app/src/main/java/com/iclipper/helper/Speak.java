package com.iclipper.helper;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import com.iclipper.Janela_Flutuante;

import java.util.Locale;



/**
 * Created by Erico on 27/02/2018.
 */

public class Speak {
    Locale portugues, espanhol;
   TextToSpeech tts;
    private AudioManager volume;
    Locale sigla;
    String textoFalar;
    int linguagemSuportada;
    Context context;


    public TextToSpeech  carregaAudio(Context context, final String lgOrigem) {
        volume = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);

        this.context = context;

       tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                portugues = new Locale("pt", "BR");
                tts.setLanguage(portugues);

                espanhol = new Locale("es_ES");
                tts.setLanguage(espanhol);

                 sigla = Locale.US;
        if (lgOrigem.equals("PT")) {
            sigla = portugues;
        } else if (lgOrigem.equals("EN")) {
            sigla = Locale.US;
        } else if (lgOrigem.equals("ES")) {
            sigla = espanhol;
        } else if (lgOrigem.equals("FR")) {
            sigla = Locale.FRENCH;
        }

                tts.setLanguage(sigla);
               // linguagemSuportada = tts.setLanguage(sigla);



            }
        });
       return tts;
    }

      /*  //Cria as linguagens
        portugues = new Locale("pt", "BR");
        tts.setLanguage(portugues);

        espanhol = new Locale("es_ES");
        tts.setLanguage(espanhol);

        //////
        sigla = Locale.US;
        if (lgOrigem.equals("PT")) {
            sigla = portugues;
        } else if (lgOrigem.equals("EN")) {
            sigla = Locale.US;
        } else if (lgOrigem.equals("ES")) {
            sigla = espanhol;
        } else if (lgOrigem.equals("FR")) {
            sigla = Locale.FRENCH;
        }
       // ConvertTextToSpeech(context, sigla, texto);
        // Toast.makeText(getApplicationContext(),"Falando:"+linguagens[listaLingOrigem.getSelectedIndex()],Toast.LENGTH_SHORT).show();
        tts.setLanguage(sigla);
        int result = tts.setLanguage(sigla);

        if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Toast.makeText(context, "Linguagem não suportada!", Toast.LENGTH_SHORT).show();

        } else {

            //Exibe mensagem caso o volume esteja baixo
            if (volume.getStreamVolume(AudioManager.STREAM_MUSIC) == 0)
                Toast.makeText(context, "Aumente o Volume!", Toast.LENGTH_SHORT).show();

            try {

                tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null);


            } catch (Exception e) {
                e.printStackTrace();
                // ConvertTextToSpeech(linguagem, texto);
            }
        }
    }*/



/*
    public void reproduzir(Context context, String lgOrigem, String texto) {
        sigla = Locale.US;
        if (lgOrigem.equals("PT")) {
            sigla = portugues;
        } else if (lgOrigem.equals("EN")) {
            sigla = Locale.US;
        } else if (lgOrigem.equals("ES")) {
            sigla = espanhol;
        } else if (lgOrigem.equals("FR")) {
            sigla = Locale.FRENCH;
        }
        ConvertTextToSpeech(context, sigla, texto);
        // Toast.makeText(getApplicationContext(),"Falando:"+linguagens[listaLingOrigem.getSelectedIndex()],Toast.LENGTH_SHORT).show();
    }*/

    public void reproduzirAudio(String texto,TextToSpeech tts) {

       /* sigla = Locale.US;
        if (lgOrigem.equals("PT")) {
            sigla = portugues;
        } else if (lgOrigem.equals("EN")) {
            sigla = Locale.US;
        } else if (lgOrigem.equals("ES")) {
            sigla = espanhol;
        } else if (lgOrigem.equals("FR")) {
            sigla = Locale.FRENCH;
        }*/
        if (linguagemSuportada == TextToSpeech.LANG_MISSING_DATA ||
                linguagemSuportada == TextToSpeech.LANG_NOT_SUPPORTED) {
            Toast.makeText(context, "Linguagem não suportada!", Toast.LENGTH_SHORT).show();

        } else {

            //Exibe mensagem caso o volume esteja baixo


                 tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null);
            }






    }
}

