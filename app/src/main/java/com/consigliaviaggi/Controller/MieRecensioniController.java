package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.consigliaviaggi.DAO.RecensioneDAO;
import com.consigliaviaggi.Entity.Recensione;
import com.consigliaviaggi.Entity.Utente;
import com.consigliaviaggi.GUI.GestioneMiaRecensionePage;

import java.util.ArrayList;
import java.util.List;

public class MieRecensioniController {

    private Activity activityMieRecensioniPage;
    private Context contextMieRecensioniPage;

    public MieRecensioniController(Activity activityMieRecensioniPage, Context contextMieRecensioniPage) {
        this.activityMieRecensioniPage = activityMieRecensioniPage;
        this.contextMieRecensioniPage = contextMieRecensioniPage;
    }

    public void openGestioneRecensionePage(Recensione recensione) {
        Intent intent = new Intent(contextMieRecensioniPage, GestioneMiaRecensionePage.class);
        intent.putExtra("Recensione",recensione);
        contextMieRecensioniPage.startActivity(intent);
    }

    public ArrayList<String> inizializzaSuggerimenti(ArrayList<Recensione> listaRecensioni) {
        ArrayList<String> risultatoSuggerimenti = new ArrayList<>();
        for(int i=0;i<listaRecensioni.size();i++) {
            if (!risultatoSuggerimenti.contains(listaRecensioni.get(i).getNomeStruttura()))
                risultatoSuggerimenti.add(listaRecensioni.get(i).getNomeStruttura());
        }
        return  risultatoSuggerimenti;
    }

    public ArrayList<Recensione> ricercaRecensione(String parolaChiave, ArrayList<Recensione> listaRecensioni) {
        ArrayList<Recensione> resultSearch=new ArrayList<>();
        for (int i=0; i<listaRecensioni.size(); i++) {
            if(listaRecensioni.get(i).getNomeStruttura().contains(parolaChiave))
                resultSearch.add(listaRecensioni.get(i));
        }
        return resultSearch;
    }

    public ArrayList<Recensione> getMieRecensioni() {
        RecensioneDAO recensioneDAO = new RecensioneDAO(contextMieRecensioniPage);
        Utente utente = Utente.getIstance();
        ArrayList<Recensione> listaMieRecensioni = recensioneDAO.getMieRecensioniFromDatabase(utente.getNickname());
        return listaMieRecensioni;
    }
}
