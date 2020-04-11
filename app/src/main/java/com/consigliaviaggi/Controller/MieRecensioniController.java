package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.consigliaviaggi.DAO.RecensioneDAO;
import com.consigliaviaggi.Entity.Recensione;
import com.consigliaviaggi.Entity.Utente;
import com.consigliaviaggi.GUI.GestioneMiaRecensionePage;

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

    public void openGestioneRecensionePage(Recensione recensione) {
        Intent intent = new Intent(contextMieRecensioniPage, GestioneMiaRecensionePage.class);
        intent.putExtra("Recensione",recensione);
        contextMieRecensioniPage.startActivity(intent);
    }
}
