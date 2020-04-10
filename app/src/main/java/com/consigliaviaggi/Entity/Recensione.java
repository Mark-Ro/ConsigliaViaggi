package com.consigliaviaggi.Entity;

public class Recensione {

    private String testo,nomeUtente;
    private int idStruttura,voto;

    public Recensione(int idStruttura, String testo, String nomeUtente, int voto) {
        this.idStruttura = idStruttura;
        this.testo = testo;
        this.nomeUtente = nomeUtente;
        this.voto = voto;
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

    public int getIdStruttura() {
        return idStruttura;
    }

    public void setIdStruttura(int idStruttura) {
        this.idStruttura = idStruttura;
    }

    public int getVoto() {
        return voto;
    }

    public void setVoto(int voto) {
        this.voto = voto;
    }
}
