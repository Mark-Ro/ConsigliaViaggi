package com.consigliaviaggi.Entity;

public class Utente {
    private static Utente istance = null;

    private boolean utenteAutenticato;

    private Utente() {
        this.utenteAutenticato=false;
    }

    public static Utente getIstance() {
        if(istance==null)
            istance = new Utente();
        return istance;
    }

    public boolean isUtenteAutenticato() {
        return utenteAutenticato;
    }
}
