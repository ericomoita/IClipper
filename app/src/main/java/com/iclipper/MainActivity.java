package com.iclipper;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.iclipper.helper.IconeFlutuante;
import com.iclipper.helper.MonitorarClipboard;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lista;
    ArrayList <String> palavrasOriginal = new ArrayList<String>();
    ArrayList <String> palavrasTraduzido = new ArrayList<String>();
    //AlertDialog alerta;
    Switch swExibeAlerta;
    SeekBar skIntervalo;
    TextView txtIntervalo;
    AlertDialog alertaExluir;
    Bundle extra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        SQLiteDatabase bancoDados = openOrCreateDatabase("app", MODE_PRIVATE, null);
        final SharedPreferences sharedPreferences = getSharedPreferences("ARQUIVO_PREFERENCIA",0);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        lista = (ListView)findViewById(R.id.listaPalavras);
        
        swExibeAlerta = (Switch)findViewById(R.id.switchExibirAlerta);
        swExibeAlerta.setChecked(sharedPreferences.getBoolean("exibirAlerta",false));
        skIntervalo = (SeekBar) findViewById(R.id.skIntervalo);
        skIntervalo.setProgress(sharedPreferences.getInt("skIntervalo",0));
        txtIntervalo = (TextView) findViewById(R.id.txtIntervalo);
        txtIntervalo.setText(sharedPreferences.getString("txtIntervalo",""));




        //Abre tela para liberar permissão de janela flutuante
        if(Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(MainActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 123);
            }
        }

        //Inicia a tarefa que monitora o clipboad
       startService(new Intent(getApplicationContext(),MonitorarClipboard.class));





        try {
            //Criar cursor para percorrer registros da tabela
           Cursor cursor = bancoDados.rawQuery("SELECT original,traduzido FROM traducoes", null);

            int indiceColunaOriginal = cursor.getColumnIndex("original");
            int indiceColunaTraduzido = cursor.getColumnIndex("traduzido");

            cursor.moveToFirst();
            int contador = 0;
            while (cursor != null) {
                palavrasOriginal.add(contador,cursor.getString(indiceColunaOriginal).toUpperCase());
                palavrasTraduzido.add(contador,cursor.getString(indiceColunaTraduzido).toUpperCase());
                cursor.moveToNext();
                contador++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        final ArrayAdapter<String> adapter =
               new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, palavrasOriginal);

        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),palavrasTraduzido.get(i),Toast.LENGTH_SHORT).show();
            }
        });


        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long arg3) {

                //Toast.makeText(getApplicationContext(),"Clicou longo",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.MyDialogTheme);;
                    builder.setTitle("Excluir");
                builder.setMessage("Deseja realmente excluir?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"Exluiu",Toast.LENGTH_SHORT).show();

                    }
                });
                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                //AlertDialog.Builder builder = new AlertDialog.Builder(this);

               // builder.setView(tela);

                        alertaExluir = builder.create();
                alertaExluir.show();

                return false;
            }

        });



        swExibeAlerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //editor.putString("exibirAlerta", String.valueOf(swExibeAlerta.isChecked()));
                editor.putBoolean("exibirAlerta",swExibeAlerta.isChecked());
                editor.commit();

            }
        });

        skIntervalo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int segundos,minutos,horas,dias;
                int valor = progress;
                segundos = ( valor / 1000 ) % 60;
                minutos  = ( valor / 60000 ) % 60;     // 60000    = 60 * 1000
                horas    = ( valor / 3600000 ) % 24;   // 3600000  = 60 * 60 * 1000
                dias     = valor / 86400000;            // 86400000 = 24 * 60 * 60 * 1000
                txtIntervalo.setText(horas+"h:"+minutos+"m:"+segundos+"s");
                editor.putString("txtIntervalo",txtIntervalo.getText().toString());
                editor.putInt("skIntervalo",progress);
                editor.commit();


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    @Override
    public void onResume(){
        super.onResume();
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
    }
    @Override
    public void onPause(){
        super.onPause();
        //finish();
    }



}
