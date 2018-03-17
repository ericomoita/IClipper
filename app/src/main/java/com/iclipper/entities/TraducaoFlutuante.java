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
    public long nivel;


    public TraducaoFlutuante(String key,long data,long order, String txtOrigem, String txtDestino, String lgOrigem, String lgDestino, String sinonimo, String exibeAlerta, long nivel) {
        this.key = key;
        this.data = data;
        this.order = order;
        this.txtOrigem = txtOrigem.toLowerCase();
        this.txtDestino = txtDestino.toLowerCase();
        this.lgOrigem = lgOrigem.toLowerCase();
        this.lgDestino = lgDestino.toLowerCase();
        this.sinonimo = sinonimo.toLowerCase();
        this.exibeAlerta = exibeAlerta;
        this.nivel = nivel;

    }
}
