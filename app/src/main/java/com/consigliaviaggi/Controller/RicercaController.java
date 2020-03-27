package com.consigliaviaggi.Controller;

import android.content.Context;

import com.consigliaviaggi.DAO.StrutturaDAO;
import com.consigliaviaggi.Entity.Struttura;

import java.util.ArrayList;


public class RicercaController {

    private Context contextRicercaPage;
    private StrutturaDAO strutturaDAO;

    public RicercaController(Context contextRicercaPage) {
        this.contextRicercaPage = contextRicercaPage;
        this.strutturaDAO = new StrutturaDAO(contextRicercaPage);
    }

    public ArrayList<Struttura> getListaStrutture(String nomeStruttura, String citta, float prezzoMinimo, float prezzoMassimo, float voto) {
        ArrayList<Struttura> listaStrutture = strutturaDAO.getListaStruttureCittaFromDatabase(nomeStruttura,citta,prezzoMinimo,prezzoMassimo,voto);
        return listaStrutture;
    }

}
