package com.iclipper.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.andexert.library.RippleView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.iclipper.Home;
import com.iclipper.R;
import com.iclipper.adapter.TraducoesListAdapter;
import com.iclipper.entities.Traducoes;
import com.iclipper.helper.Firebase;
import com.iclipper.helper.Preferences;
import com.iclipper.helper.Speak;
import com.iclipper.listener.OnListClickInteractionListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.R.attr.id;


/**
 * Responsável por fazer as manipulações de elementos de interface
 */
public class TraducoesViewHolder extends RecyclerView.ViewHolder {

    // Elemento de interface
    private TextView txtOrigem;
    private TextView txtDestino;
    private TextView lgOrigem;
    private TextView lgDestino;
    private TextView data;
    private LinearLayout linearLayout;
    TextView dicionario1;
     ImageButton mTextViewDetails;
    RippleView deleteButton;
    RippleView btExibeAlerta;
    ToggleButton iconeExibeAlerta;
    boolean exibeAlerta;
    Preferences preferences;

    Context context;
    Speak speak;
    TextToSpeech tts;





    /**
     * Construtor
     */
    public TraducoesViewHolder(View itemView) {
        super(itemView);
        this.txtDestino = (TextView) itemView.findViewById(R.id.txtDestino);
        this.txtOrigem = (TextView) itemView.findViewById(R.id.txtOrigem);
        this.lgOrigem = (TextView) itemView.findViewById(R.id.lgOrigem);
        this.lgDestino = (TextView) itemView.findViewById(R.id.lgDestino);
        this.data = (TextView) itemView.findViewById(R.id.data);
        this.linearLayout = (LinearLayout) itemView.findViewById(R.id.layoutDicionario);
        this.dicionario1 = (TextView) itemView.findViewById(R.id.dicionario1);
        this.deleteButton = (RippleView) itemView.findViewById(R.id.delete);
        this.btExibeAlerta = (RippleView) itemView.findViewById(R.id.notification);
        this.iconeExibeAlerta = (ToggleButton) itemView.findViewById(R.id.notificationIcon);
        this.context = itemView.getContext();
        this.preferences = new Preferences(context);



        this.mTextViewDetails = (ImageButton) itemView.findViewById(R.id.text_view_details);




        }

    /**
     * Atribui valores aos elementos
     */
    public void bindData(final Traducoes car, final OnListClickInteractionListener listener, final TextToSpeech tts) {

        // Altera valor
        this.lgOrigem.setText(car.lgOrigem+ ":");
        this.lgDestino.setText(car.lgDestino+ ":");
        this.txtOrigem.setText(car.palavraOrigem);
        this.txtDestino.setText(car.palavraTraduzida);
        //this.data.setText(String.valueOf(car.data));
        if(car.exibeAlerta.equals("1")){
            this.iconeExibeAlerta.setChecked(true);
            exibeAlerta = true;
        }else{
            this.iconeExibeAlerta.setChecked(false);
            exibeAlerta = false;
        }

        //Validação da data
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar dataAtual = Calendar.getInstance();
        String dataAtualFormatada = formatter.format(dataAtual.getTimeInMillis());
        String dataPalavraFormatada = formatter.format(car.data);

        if(dataPalavraFormatada.equals(dataAtualFormatada)){
            SimpleDateFormat formatterHora = new SimpleDateFormat("HH:mm:ss");
            this.data.setText(formatterHora.format(car.data));
        }else{
            SimpleDateFormat formatterHora = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            this.data.setText(formatterHora.format(car.data));
        }



        this.tts = tts;
        //TextView dicionario1 = new TextView(itemView.getContext().getApplicationContext());
        //car.dicionario.replaceAll(";","     ");


        this.dicionario1.setText(car.dicionario.replaceAll(";","    "));
       // TraducoesListAdapter listAdapter = new TraducoesListAdapter(car);

        /*this.btExibeAlerta.setOnClickListener(new View.OnClickListener() {
            @Override
            Preferences preferences = new Preferences(context);
            public void onClick(View v) {
               // Firebase.getFirebase().child(preferences.getUserId()).child)
            }
        });*/
        // Adciona evento de click
        this.btExibeAlerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!exibeAlerta) {
                    Firebase.getFirebase().child(preferences.getUserId()).child(car.key).child("exibeAlerta").setValue("1");
                    exibeAlerta = true;
                }else{
                    Firebase.getFirebase().child(preferences.getUserId()).child(car.key).child("exibeAlerta").setValue("0");
                    exibeAlerta = false;
                }
            }
        });
       this.mTextViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak = new Speak();
                speak.reproduzirAudio(car.palavraOrigem,tts);

               // speak.ConvertTextToSpeech(car.palavraOrigem);
                //Speak speak = new Speak(context,car.lgOrigem.toUpperCase(),car.palavraOrigem);
                //Toast.makeText(context,car.lgOrigem.toUpperCase(),Toast.LENGTH_LONG).show();
               // speak.reproduzir(context,car.lgOrigem.toUpperCase(),car.palavraOrigem);

            }
        });
        this.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(car.id);
                Firebase.getFirebase().child(preferences.getUserId()).child(car.key).removeValue();

            }
        });
    }



}
