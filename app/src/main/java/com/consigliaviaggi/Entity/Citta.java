package com.consigliaviaggi.Entity;

import java.io.Serializable;

public class Citta implements Serializable {

    private int idCitta;
    private String nome,fotoCitta,nazione;

    public int getIdCitta() {
        return idCitta;
    }

    public Citta(int idCitta, String nome, String fotoCitta, String nazione) {
        this.idCitta = idCitta;
        this.nome = nome;
        this.fotoCitta = fotoCitta;
        this.nazione = nazione;
    }

    public void setIdCitta(int idCitta) {
        this.idCitta = idCitta;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFotoCitta() {
        return fotoCitta;
    }

    public void setFotoCitta(String fotoCitta) {
        this.fotoCitta = fotoCitta;
    }

    public String getNazione() {
        return nazione;
    }

    public void setNazione(String nazione) {
        this.nazione = nazione;
    }
}
