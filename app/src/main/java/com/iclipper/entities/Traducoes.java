package com.iclipper.entities;

/**
 * Entidade de carros
 */
public class Traducoes {

    public int id;
    public String key;
    public long data;
    public String palavraOrigem;
    public String palavraTraduzida;
    public String lgOrigem;
    public String lgDestino;
    public String dicionario;
    public String exibeAlerta;


    public Traducoes(String key, int id, long data, String palavraOrigem, String palavraTraduzida, String lgOrigem, String lgDestino, String dicionario, String exibeAlerta ) {
        this.id = id;
        this.palavraOrigem = palavraOrigem;
        this.palavraTraduzida = palavraTraduzida;
        this.lgOrigem = lgOrigem;
        this.lgDestino = lgDestino;
        this.data = data;
        this.dicionario = dicionario;
        this.exibeAlerta = exibeAlerta;
        this.key = key;

    }

}