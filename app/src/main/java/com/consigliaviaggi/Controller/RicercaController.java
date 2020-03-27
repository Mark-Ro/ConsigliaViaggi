package com.consigliaviaggi.Controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.consigliaviaggi.DAO.StrutturaDAO;
import com.consigliaviaggi.Entity.Struttura;
import com.consigliaviaggi.GUI.ListaStrutturePage;

import java.util.ArrayList;


public class RicercaController {

    private Context contextRicercaPage;
    private StrutturaDAO strutturaDAO;

    public RicercaController(Context contextRicercaPage) {
        this.contextRicercaPage = contextRicercaPage;
        this.strutturaDAO = new StrutturaDAO(contextRicercaPage);
    }

    public void effettuaRicercaStrutture(String nomeStruttura, String citta, float prezzoMassimo, float voto) {
        ArrayList<Struttura> listaStrutture = strutturaDAO.getListaStruttureCittaFromDatabase(nomeStruttura,citta,prezzoMassimo,voto);
        if (listaStrutture!=null) {
            Log.i("RICERCA_CONTROLLER","Lista size: " + String.valueOf(listaStrutture.size()));
            Intent intent = new Intent(contextRicercaPage, ListaStrutturePage.class);
            intent.putExtra("ListaStrutture",listaStrutture);
            contextRicercaPage.startActivity(intent);
        }

        else {
            Log.i("RICERCA_CONTROLLER", "Lista vuota!");
            Toast.makeText(contextRicercaPage, "Nessun risultato trovato!", Toast.LENGTH_SHORT).show();
        }
    }

}
