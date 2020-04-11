package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.content.Context;

import com.consigliaviaggi.DAO.RecensioneDAO;
import com.consigliaviaggi.Entity.Recensione;
import com.consigliaviaggi.Entity.Utente;

import java.util.ArrayList;

public class MieRecensioniController {

    private Activity activityMieRecensioniPage;
    private Context contextMieRecensioniPage;

    public MieRecensioniController(Activity activityMieRecensioniPage, Context contextMieRecensioniPage) {
        this.activityMieRecensioniPage = activityMieRecensioniPage;
        this.contextMieRecensioniPage = contextMieRecensioniPage;
    }

    public ArrayList<Recensione> getMieRecensioni() {
        RecensioneDAO recensioneDAO = new RecensioneDAO(contextMieRecensioniPage);
        Utente utente = Utente.getIstance();
        ArrayList<Recensione> listaMieRecensioni = recensioneDAO.getMieRecensioniFromDatabase(utente.getNickname());
        return listaMieRecensioni;
    }
}
