package com.iclipper.entities;

/**
 * Created by Erico on 02/02/2018.
 */

public class TraducaoFlutuante {
    public String key;
    public long data;
    public long order;
    public String txtOrigem;
    public String txtDestino;
    public String lgOrigem;
    public String lgDestino;
    public String sinonimo;
    public String exibeAlerta;


    public TraducaoFlutuante(String key,long data,long order, String txtOrigem, String txtDestino, String lgOrigem, String lgDestino, String sinonimo, String exibeAlerta) {
        this.key = key;
        this.data = data;
        this.order = order;
        this.txtOrigem = txtOrigem;
        this.txtDestino = txtDestino;
        this.lgOrigem = lgOrigem;
        this.lgDestino = lgDestino;
        this.sinonimo = sinonimo;
        this.exibeAlerta = exibeAlerta;

    }
}
