package com.iclipper.data;

import android.app.Activity;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;


import com.iclipper.entities.Traducoes;

import java.util.ArrayList;
import java.util.List;
import android.database.sqlite.SQLiteDatabase;

import static android.content.Context.MODE_PRIVATE;


/**
 * Classe mock implementada para simular um repositório de dados
 * Ex: Banco de dados, chamada a serviço API
 */
public class TraducoesMock {
    // Lista de carros







    private List<Traducoes> mListCars;

    /**
     * Construtor - Inicia Mock
     */


    /**
     * Retorna lista de carros
     */

    /**
     * Retorna carro de acordo com o Id
     */
    public Traducoes get(int id) {
        return this.mListCars.get(id);
    }

    public List<Traducoes> getCarMockList() {
        List<Traducoes> mMockList = new ArrayList<>();

        int id = 0;
        Traducoes audiR8 = new Traducoes("",id,43 ,"Audi R8", "Audi", "610", "1165338.00","","1");
        mMockList.add(audiR8);
        Traducoes bugattiChiron = new Traducoes("",++id,43, "Bugatti Chiron", "Bugatti", "1520", "1000000.00","","1");
        mMockList.add(bugattiChiron);

        return mMockList;
    }
    public List<Traducoes> setCarMockList(Traducoes traducoes) {
        List<Traducoes> mMockList = new ArrayList<>();
        int id = 0;
        //Traducoes audiR8 = new Traducoes(id, "Audi R8", "Audi", "610", "1165338.00");
        mMockList.add(traducoes);
       // Traducoes bugattiChiron = new Traducoes(++id, "Bugatti Chiron", "Bugatti", "1520", "1000000.00");
       // mMockList.add(bugattiChiron);

        return mMockList;
    }

}
