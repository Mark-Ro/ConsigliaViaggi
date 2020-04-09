package com.consigliaviaggi.Entity;

public class Recensione {

    private String testo,nomeUtente;
    private int voto;

    public Recensione(String testo, String nomeUtente, int voto) {
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

    public int getVoto() {
        return voto;
    }

    public void setVoto(int voto) {
        this.voto = voto;
    }
}
