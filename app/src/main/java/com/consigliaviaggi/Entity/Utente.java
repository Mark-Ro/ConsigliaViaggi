package com.consigliaviaggi.Entity;

public class Utente {
    private static Utente istance = null;

    private int numeroRecensioni;
    private String nickname,email,nome,cognome,statoAccount,nomePubblico;
    private float media;
    private boolean utenteAutenticato,caricamentoUtente;

    private Utente() {
        this.utenteAutenticato = false;
        this.caricamentoUtente = false;
    }

    public static Utente getIstance() {
        if(istance==null)
            istance = new Utente();
        return istance;
    }

    public int getNumeroRecensioni() {
        return numeroRecensioni;
    }

    public void setNumeroRecensioni(int numeroRecensioni) {
        this.numeroRecensioni = numeroRecensioni;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getStatoAccount() {
        return statoAccount;
    }

    public void setStatoAccount(String statoAccount) {
        this.statoAccount = statoAccount;
    }

    public String getNomePubblico() {
        return nomePubblico;
    }

    public void setNomePubblico(String nomePubblico) {
        this.nomePubblico = nomePubblico;
    }

    public float getMedia() {
        return media;
    }

    public void setMedia(float media) {
        this.media = media;
    }

    public void setUtenteAutenticato(boolean utenteAutenticato) {
        this.utenteAutenticato = utenteAutenticato;
    }

    public boolean isUtenteAutenticato() {
        return utenteAutenticato;
    }

    public boolean isCaricamentoUtente() {
        return caricamentoUtente;
    }

    public void setCaricamentoUtente(boolean caricamentoUtente) {
        this.caricamentoUtente = caricamentoUtente;
    }
}
