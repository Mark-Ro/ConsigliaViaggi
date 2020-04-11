package com.consigliaviaggi.Entity;

public class Recensione {

    private String testo,nomeUtente,nomeStruttura;
    private int idRecensione,voto;

    public Recensione(int idRecensione, String testo, int voto, String nomeUtente) {
        this.idRecensione = idRecensione;
        this.testo = testo;
        this.nomeUtente = nomeUtente;
        this.voto = voto;
    }

    public Recensione(int idRecensione, String testo, String nomeStruttura, int voto) {
        this.idRecensione = idRecensione;
        this.testo = testo;
        this.nomeStruttura = nomeStruttura;
        this.voto = voto;
    }

    public String getNomeStruttura() {
        return nomeStruttura;
    }

    public void setNomeStruttura(String nomeStruttura) {
        this.nomeStruttura = nomeStruttura;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public String getNomeUtente() {
        return nomeUtente;
    }

    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }

    public int getIdRecensione() {
        return idRecensione;
    }

    public void setIdRecensione(int idRecensione) {
        this.idRecensione = idRecensione;
    }

    public int getVoto() {
        return voto;
    }

    public void setVoto(int voto) {
        this.voto = voto;
    }
}
