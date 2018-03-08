package com.iclipper.adapter;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.iclipper.R;
import com.iclipper.entities.Traducoes;
import com.iclipper.listener.OnListClickInteractionListener;
import com.iclipper.viewholder.TraducoesViewHolder;

import java.util.List;

/**
 * Classe Adapter que extende a classe RecyclerView.Adapter
 * Necessária para implementação da RecyclerView
 */
public class TraducoesListAdapter extends RecyclerView.Adapter<TraducoesViewHolder> {

    // Lista de carros
    private List<Traducoes> mListCars;



    // Interface que define as ações
    private OnListClickInteractionListener mOnListClickInteractionListener;

    TextToSpeech tts;

    /**
     * Construtor
     */
    public TraducoesListAdapter(List<Traducoes> cars, OnListClickInteractionListener listener, TextToSpeech tts) {
        this.mListCars = cars;
        this.mOnListClickInteractionListener = listener;
        this.tts = tts;



    }

    /**
     * Responsável pela criação de linha
     */
    @Override
    public TraducoesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Obtém o contexto
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Instancia o layout para manipulação dos elementos
        View carView = inflater.inflate(R.layout.row_car_list, parent, false);


        // Passa a ViewHolder
        return new TraducoesViewHolder(carView);
    }

    /**
     * Responsável por atribuir valores após linha criada
     */
    @Override
    public void onBindViewHolder(TraducoesViewHolder holder, int position) {
        Traducoes car = this.mListCars.get(position);
        car.id = holder.getAdapterPosition();

        //Animacao


        holder.bindData(car,this.mOnListClickInteractionListener,tts);
        try {
            YoYo.with(Techniques.FlipInX)
                    .duration(700)
                    .repeat(0)
                    .playOn(holder.itemView);
        }catch (Exception e){
            e.printStackTrace();
        }







    }
    public void removerItem(int id){
        mListCars.remove(id);
        notifyItemRemoved(id);
        notifyItemRangeChanged(id,mListCars.size());

    }

    @Override
    public int getItemCount() {
        return this.mListCars.size();
    }

}
