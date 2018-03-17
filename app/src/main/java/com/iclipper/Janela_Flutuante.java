package com.iclipper;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iclipper.entities.TraducaoFlutuante;
import com.iclipper.helper.Firebase;
import com.iclipper.helper.Preferences;
import com.iclipper.helper.VerificaConexao;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Janela_Flutuante extends Activity {

    public Context context;
    Display display;
    WindowManager.LayoutParams params;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    TextToSpeech tts;
    String txtParaTraduzir;
    private AudioManager volume;
     Locale portugues;
     Locale espanhol;
    Bundle extra;
  SQLiteDatabase bancoDados;


    //Elementos da tela
    String[] linguagens = {"Português", "Inglês", "Espanhol", "Francês"};
    MaterialSpinner listaLingOrigem;
    MaterialSpinner listaLingDestino;
    RelativeLayout layoutPrincipal;
    MaterialRippleLayout microfone;
    MaterialRippleLayout ouvir;
    MaterialRippleLayout ouvirTraduzido;
    EditText textoParaTraduzir;
    MaterialRippleLayout fechar;
    Button traduzir;
    EditText textoTraduzido;
    ProgressBar loading;
    ImageView imgTrocaLinguagem;
    Button apagar;
    TextView sinonimo0, sinonimo1,sinonimo2;






    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setVolumeControlStream(AudioManager.STREAM_MUSIC);//Habilita botao de diminuir ou aumentar volume
        carregarAudio();

        setContentView(R.layout.activity_janela__flutuante);
        listaLingOrigem = (MaterialSpinner) findViewById(R.id.spinnerLingOrigem);
        listaLingDestino = (MaterialSpinner) findViewById(R.id.spinnerLingDestino);
        layoutPrincipal = (RelativeLayout) findViewById(R.id.layoutPrincipal);
        microfone = (MaterialRippleLayout) findViewById(R.id.btFalar);
        textoParaTraduzir = (EditText) findViewById(R.id.textoParaTraduzir);
        ouvir = (MaterialRippleLayout ) findViewById(R.id.antigo);
        ouvirTraduzido = (MaterialRippleLayout ) findViewById(R.id.btOuvirTraduzido);
        fechar = (MaterialRippleLayout) findViewById(R.id.btFechar);
        txtParaTraduzir = textoParaTraduzir.getText().toString();
        volume = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        traduzir = (Button) findViewById(R.id.btTraduzir);
        loading = (ProgressBar) findViewById(R.id.loading);
        imgTrocaLinguagem = (ImageView) findViewById(R.id.imgTrocaLinguagem);
        apagar = (Button) findViewById(R.id.apagar);
       try {
           extra = getIntent().getExtras();
       }catch (Exception e){
           e.printStackTrace();
       }


        textoTraduzido = (EditText) findViewById(R.id.textoTraduzido);
        sinonimo0 = (TextView)findViewById(R.id.sin1);
        sinonimo1 = (TextView)findViewById(R.id.sin2);
        sinonimo2 = (TextView)findViewById(R.id.sin3);



        //Criar Banco
        // bancoDados = openOrCreateDatabase("app", MODE_PRIVATE, null);

        //Criar tabela
       // bancoDados.execSQL("CREATE TABLE IF NOT EXISTS traducoes(original VARCHAR, traduzido VARCHAR, lgorigem VARCHAR, lgdestino VARCHAR)");



        //Cria as linguagens
        portugues = new Locale("pt","BR");
        tts.setLanguage(portugues);

        espanhol =  new Locale("es_ES");
        tts.setLanguage(espanhol);

        //preencherSinonimos();


       ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, linguagens);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        listaLingOrigem.setAdapter(adapter);
        listaLingDestino.setAdapter(adapter);




        // Params for the window.
        // You can easily set the alpha and the dim behind the window from here
        params = getWindow().getAttributes();
        //params.alpha = 1.0f;    // lower than one makes it more transparent
        params.dimAmount = 0f;  // set it higher if you want to dim behind the window
        params.gravity = Gravity.LEFT | Gravity.CENTER;
        getWindow().setAttributes(params);


        // Gets the display size so that you can set the window to a percent of that
        display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        getWindow().setLayout(width, height);

        //Seta linguagem padrao original
        listaLingOrigem.setSelectedIndex(1);

        listaLingDestino.setSelectedIndex(0);





        //Ativa o microfone
        microfone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startVoiceInput();
            }
        });

        //Fecha tela ao clicar fora
        layoutPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animacaoFechar();
            }
        });
        fechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animacaoFechar();
            }
        });

        //Ouvir texto original
        ouvir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConvertTextToSpeech(converteLingagemOuvir(),textoParaTraduzir.getText().toString());
                Toast.makeText(getApplicationContext(),"Falando:"+linguagens[listaLingOrigem.getSelectedIndex()],Toast.LENGTH_SHORT).show();
            }
        });
        ouvirTraduzido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConvertTextToSpeech(converteLingagemDestinoOuvir(), textoTraduzido.getText().toString());
                Toast.makeText(getApplicationContext(),"Falando:"+linguagens[listaLingDestino.getSelectedIndex()],Toast.LENGTH_SHORT).show();

                //loadingOuvir.setVisibility(View.INVISIBLE);
            }
        });

        traduzir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!textoParaTraduzir.getText().toString().isEmpty()) {
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textoParaTraduzir.getWindowToken(), 0);
                    try {
                        sinonimo0.setText("");
                        sinonimo1.setText("");
                        sinonimo2.setText("");
                        traduzir(textoParaTraduzir.getText().toString(), converteLinguagemOrigem(), converteLinguagemDestino());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Digite algo para traduzir.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgTrocaLinguagem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // touch down code
                        girarImagem();
                        int linguagemOrigem = listaLingOrigem.getSelectedIndex();
                        int linguagemDestino = listaLingDestino.getSelectedIndex();
                        listaLingOrigem.setSelectedIndex(linguagemDestino);
                        listaLingDestino.setSelectedIndex(linguagemOrigem);
                        break;
                }
                try {
                    traduzir(textoParaTraduzir.getText().toString(),converteLinguagemOrigem(), converteLinguagemDestino());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return false;


            }
        });

    apagar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            textoParaTraduzir.setText("");
            textoTraduzido.setText("");
            sinonimo0.setText("");
            sinonimo1.setText("");
            sinonimo2.setText("");

        }
    });
        try {
            textoParaTraduzir.setText(extra.getString("texto"));
        }catch (Exception e){
            e.printStackTrace();
        }
        animacaoAbrir();



    }
    public void girarImagem(){
        RotateAnimation rotate = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF,
                0.5f,  Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(500);
        imgTrocaLinguagem.startAnimation(rotate);

    }


    //Converte a linguagem origem selecionada para sigla
    public String converteLinguagemOrigem(){
        String sigla = "us";
        switch (listaLingOrigem.getSelectedIndex()){
            case 0 : sigla = "pt"; break;
            case 1 : sigla = "en"; break;
            case 2 : sigla = "es"; break;
            case 3 : sigla = "fr"; break;

        }

        return sigla;

    }
    //Converte a linguagem destino selecionada para sigla
    public String converteLinguagemDestino(){
        String sigla = "us";
        switch (listaLingDestino.getSelectedIndex()){
            case 0 : sigla = "pt"; break;
            case 1 : sigla = "en"; break;
            case 2 : sigla = "es"; break;
            case 3 : sigla = "fr"; break;

        }

        return sigla;

    }
    //Pega a voz e converte para Sigla da linguagem (Falar)
    public String converteLingagemFalar(){

        String sigla = "en-US";
        switch (listaLingOrigem.getSelectedIndex()){
            case 0 : sigla = "pt-BR"; break;
            case 1 : sigla = "en-US"; break;
            case 2 : sigla = "es-ES"; break;
            case 3 : sigla = "fr-FR"; break;

        }

        return sigla;
    }
    //Pega a voz e converte para Sigla da linguagem (Ouvir)
    public Locale converteLingagemOuvir(){

        Locale sigla = Locale.US;
        switch (listaLingOrigem.getSelectedIndex()){
            case 0 : sigla = portugues; break;
            case 1 : sigla = Locale.US; break;
            case 2 : sigla = espanhol; break;
            case 3 : sigla = Locale.FRENCH; break;

        }

        return sigla;
    }
    public Locale converteLingagemDestinoOuvir(){

        Locale sigla = Locale.US;
        switch (listaLingDestino.getSelectedIndex()){

            case 0 : sigla = portugues; break;
            case 1 : sigla = Locale.US; break;
            case 2 : sigla = espanhol; break;
            case 3 : sigla = Locale.FRENCH; break;

        }

        return sigla;
    }

    private void carregarAudio() {
        tts = new TextToSpeech(Janela_Flutuante.this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {


            }
        });
    }

    private void ConvertTextToSpeech(Locale linguagem, String texto) {

        int result = tts.setLanguage(linguagem);

        if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Toast.makeText(getApplicationContext(), "Linguagem não suportada!", Toast.LENGTH_SHORT).show();

        } else {

            //Exibe mensagem caso o volume esteja baixo
            if (volume.getStreamVolume(AudioManager.STREAM_MUSIC) == 0)
                Toast.makeText(getApplicationContext(), "Aumente o Volume!", Toast.LENGTH_SHORT).show();

            try {

                tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null);
                tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null);




            } catch (Exception e) {
                e.printStackTrace();
                ConvertTextToSpeech(linguagem, texto);
            }
        }




    }



    //Captura voz
    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);


        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, converteLingagemFalar()); //Linguem que será lida
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, converteLingagemFalar());// Liguagem q será lida


        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    textoParaTraduzir.setText(result.get(0));
                }
                break;
            }

        }
    }

    public void animacaoAbrir(){
        ObjectAnimator animacao = ObjectAnimator.ofFloat(layoutPrincipal,"translationX",2000f,0f);
        animacao.setDuration(600);
        animacao.start();
        animacao.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                try {
                    if(!textoParaTraduzir.getText().toString().isEmpty()) {
                        traduzir(textoParaTraduzir.getText().toString(), converteLinguagemOrigem(), converteLinguagemDestino());
                    }else{
                        loading.setVisibility(View.INVISIBLE);
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


    }

    public void animacaoFechar(){

        ObjectAnimator animacao = ObjectAnimator.ofFloat(layoutPrincipal,"translationX",0,2000f);
        animacao.setDuration(300);
        animacao.start();
        animacao.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }


    public void traduzir(String texto, String lgOrigem, String lgDestino) throws ExecutionException, InterruptedException {
        textoTraduzido.setText("");
        loading.setVisibility(View.VISIBLE);
        Translate translate = new Translate();
        VerificaConexao verificaConexao = new VerificaConexao();
       if (verificaConexao.verifica(getApplicationContext())){
           translate.execute(texto, lgOrigem, lgDestino);
           //textoTraduzido.setText(textTraduzido);





           if (textoTraduzido == null)   Toast.makeText(getApplicationContext(),"Erro ao Traduzir, tente novamente!",Toast.LENGTH_LONG).show();

       }else{
           textoTraduzido.setText("Verifique sua conexão com a internet");
           loading.setVisibility(View.INVISIBLE);
           //Toast.makeText(getApplication(),"Verifique sua Conexão!",Toast.LENGTH_LONG).show();

       }



    }


    public class Translate extends AsyncTask<String, Void, String> {
        String response,sinonimo, texto,lgOrigem,lgDestino;



        /** progress dialog to show user that the backup is processing. */

        /** application context. */

        @Override
        protected void onPreExecute() {
           // Toast.makeText(Janela_Flutuante.this,"Começou a traduzir",Toast.LENGTH_SHORT).show();

        }


        @Override
        protected String doInBackground(String... params) {
            texto = params[0];
            lgOrigem = params[1];
            lgDestino = params[2];

            try {
                texto = URLEncoder.encode(texto, "utf-8");
                response = request("https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20171103T015639Z.08fa7eaad1dd9594.60151607df10f1f07e72204e94522587fe8b3b8a&text=" + texto + "&lang="+ lgOrigem +"-"+ lgDestino);
                response = response.substring(response.indexOf("text")+8, response.length()-3);



            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            }
            //chama metodo para pegar sinonimos
            try {
                getSinonimo("https://dictionary.yandex.net/api/v1/dicservice/lookup?key=dict.1.1.20171227T222410Z.2d2452e0187861ef.15df76caa79661559df59c2482ff549013d9ad54&lang="+ lgOrigem +"-"+ lgDestino+"&text="+texto,response);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }


            return response;
        }
        @Override
        protected void onPostExecute(String trad) {
            super.onPostExecute(trad);
            loading.setVisibility(View.INVISIBLE);
        textoTraduzido.setText(trad.replace("\\n"," \n"));

            Preferences preferences = new Preferences(getApplicationContext());
            //Inserir dados
             Calendar data = Calendar.getInstance();
           long order =  Long.parseLong("999999999999999");
           order = order - data.getTimeInMillis();
            String key = Firebase.getFirebase().child(preferences.getUserId()).push().getKey();
            TraducaoFlutuante traducaoFlutuante = new TraducaoFlutuante(key,data.getTimeInMillis(),order,textoParaTraduzir.getText().toString(),trad.replace("\\n"," "),lgOrigem,lgDestino,sinonimo,"1",1);
            Firebase.getFirebase().child(preferences.getUserId()).child(key).setValue(traducaoFlutuante).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //Setar sinonimos no textview
                    try {
                        String[] listaSinonimos = sinonimo.split(";");
                        sinonimo0.setText(listaSinonimos[0]);
                        sinonimo1.setText(listaSinonimos[1]);
                        sinonimo2.setText(listaSinonimos[2]);
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            });
            //Se der erro ele tenta traduzir novamente
            if(textoTraduzido.getText().toString().isEmpty()){
                try {
                    traduzir(extra.getString("texto"),converteLinguagemOrigem(), converteLinguagemDestino());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }




        }

        private  void getSinonimo(String URL,String txtTraduzido) throws IOException, ParserConfigurationException, SAXException {
            java.net.URL url = new URL(URL);
            URLConnection urlConn = url.openConnection();
            urlConn.addRequestProperty("User-Agent", "Mozilla");

            InputStream inStream = urlConn.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
            while(in.readLine() != null){

                String xml = in.readLine();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();

                Document xmlDocument = builder.parse(new InputSource(new StringReader(xml)));

                // Pega os elementos em que o nome da tag seja "c":
                NodeList nodes = xmlDocument.getElementsByTagName("text");

              //  sinonimo = textoParaTraduzir.getText().toString().toLowerCase();
                for(int i = 0; i < nodes.getLength(); i++) {
                   // sinonimo = nodes.item(i).getTextContent();
                    //txtTraduzido = "fogo\\n";
                   // txtTraduzido = txtTraduzido.replaceAll("\\n", "");
                    System.out.println(txtTraduzido);
                   // if(!nodes.item(i).getTextContent().equals(textoParaTraduzir.getText().toString().toLowerCase().replaceAll(" ","").replaceAll("\\n","")) && !nodes.item(i).getTextContent().equals(txtTraduzido.toString().toLowerCase().replaceAll(" ","").replaceAll("\\n","")))  {
                    if(validaDicionario(nodes.item(i).getTextContent(),textoParaTraduzir.getText().toString(),txtTraduzido)){

                        if(sinonimo == null){
                            sinonimo =  nodes.item(i).getTextContent().toLowerCase();
                        }else {
                            sinonimo = sinonimo + ";" + nodes.item(i).getTextContent().toLowerCase();
                        }
                    }
                }
            }
            if(sinonimo == null)  sinonimo = "";





            inStream.close();

        }
        public boolean validaDicionario(String dicionario, String palavraOriginal,String palavraTraduzida){
            //valida se a palavra do dicionario é igual a palavra original
            if(dicionario.toLowerCase().equals(palavraOriginal.toLowerCase().replaceAll(" ","").replaceAll("\\n",""))) {
            return false;
            }
            //valida se a palavra do dicionario é igual a palavra traduzida
            if(dicionario.toLowerCase().equals(palavraTraduzida.toLowerCase().replaceAll(" ","").replaceAll("\\n",""))){
                return false;
            }


                return true;
        }


        private  String request(String URL) throws IOException {
            java.net.URL url = new URL(URL);
            URLConnection urlConn = url.openConnection();
            urlConn.addRequestProperty("User-Agent", "Mozilla");

            InputStream inStream = urlConn.getInputStream();

            String recieved = new BufferedReader(new InputStreamReader(inStream)).readLine();

            inStream.close();
            return recieved;
        }
    }
}
