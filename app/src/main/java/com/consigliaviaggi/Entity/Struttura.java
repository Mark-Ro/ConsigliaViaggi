package com.consigliaviaggi.Entity;

import java.io.Serializable;

public class Struttura implements Serializable {

    private int idStruttura,numeroRecensioni;
    private String nome,descrizione,tipoStruttura,fotoStruttura;
    private float prezzo,voto;
    private double latitudine,longitudine;

    private Citta citta;

    public Struttura(int idStruttura, String nomeStruttura, float prezzo, String descrizione, double latitudine, double longitudine, String tipoStruttura, float voto, String fotoStruttura, int numeroRecensioni, Citta citta) {
        this.idStruttura = idStruttura;
        this.numeroRecensioni = numeroRecensioni;
        this.nome = nome;
        this.descrizione = descrizione;
        this.tipoStruttura = tipoStruttura;
        this.fotoStruttura = fotoStruttura;
        this.prezzo = prezzo;
        this.voto = voto;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
        this.citta = citta;
    }

    public int getIdStruttura() {
        return idStruttura;
    }

    public void setIdStruttura(int idStruttura) {
        this.idStruttura = idStruttura;
    }

    public int getNumeroRecensioni() {
        return numeroRecensioni;
    }

    public void setNumeroRecensioni(int numeroRecensioni) {
        this.numeroRecensioni = numeroRecensioni;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getTipoStruttura() {
        return tipoStruttura;
    }

    public void setTipoStruttura(String tipoStruttura) {
        this.tipoStruttura = tipoStruttura;
    }

    public String getFotoStruttura() {
        return fotoStruttura;
    }

    public void setFotoStruttura(String fotoStruttura) {
        this.fotoStruttura = fotoStruttura;
    }

    public float getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(float prezzo) {
        this.prezzo = prezzo;
    }

    public float getVoto() {
        return voto;
    }

    public void setVoto(float voto) {
        this.voto = voto;
    }

    public double getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(double latitudine) {
        this.latitudine = latitudine;
    }

    public double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(double longitudine) {
        this.longitudine = longitudine;
    }

    public Citta getCitta() {
        return citta;
    }

    public void setCitta(Citta citta) {
        this.citta = citta;
    }
}
