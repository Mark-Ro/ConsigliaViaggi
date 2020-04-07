package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


import com.consigliaviaggi.Entity.Struttura;
import com.consigliaviaggi.Entity.Utente;
import com.consigliaviaggi.GUI.LoginPage;
import com.consigliaviaggi.GUI.MainActivity;
import com.consigliaviaggi.GUI.OverviewStrutturaPage;
import com.consigliaviaggi.GUI.ProfiloPage;

import java.util.ArrayList;

public class ListaStrutturePageController {

    private Activity activityListaStrutturePage;
    private ArrayList<Struttura> listaStrutture,listaStruttureIntent;

    public ListaStrutturePageController(Activity activityListaStrutturePage, ArrayList<Struttura> listaStrutture) {
        this.activityListaStrutturePage = activityListaStrutturePage;
        this.listaStrutture = listaStrutture;
    }

    public void openHomePage() {
        Log.i("LISTA_STRUTTURE_PAGE_CONTROLLER","Entrato in openHomePage");
        Intent intent = new Intent(activityListaStrutturePage.getApplicationContext(),MainActivity.class);
        activityListaStrutturePage.startActivity(intent);
    }

    public void openProfiloPage() {
        Log.i("LISTA_STRUTTURE_PAGE_CONTROLLER","Entrato in profilo");
        Intent intent;
        Utente utente = Utente.getIstance();
        if (utente.isUtenteAutenticato())
            intent = new Intent(activityListaStrutturePage, ProfiloPage.class);
        else
            intent = new Intent(activityListaStrutturePage, LoginPage.class);

        activityListaStrutturePage.startActivity(intent);
    }

    public void openMappa() {
        //Da implementare
    }

    public ArrayList<Struttura> selezionaStruttureHotel() {
        ArrayList<Struttura> listaOutput = new ArrayList<>();

        for (int i = 0; i<listaStrutture.size();i++) {
            if (listaStrutture.get(i).getTipoStruttura().equals("hotel"))
                listaOutput.add(listaStrutture.get(i));
        }
        listaStruttureIntent = listaOutput;
        return listaOutput;
    }

    public ArrayList<Struttura> selezionaStruttureRistoranti() {
        ArrayList<Struttura> listaOutput = new ArrayList<>();

        for (int i = 0; i<listaStrutture.size();i++) {
            if (listaStrutture.get(i).getTipoStruttura().equals("ristorante"))
                listaOutput.add(listaStrutture.get(i));
        }
        listaStruttureIntent = listaOutput;
        return listaOutput;
    }

    public ArrayList<Struttura> selezionaStruttureAltro() {
        ArrayList<Struttura> listaOutput = new ArrayList<>();

        for (int i = 0; i<listaStrutture.size();i++) {
            if (listaStrutture.get(i).getTipoStruttura().equals("altro"))
                listaOutput.add(listaStrutture.get(i));
        }
        listaStruttureIntent = listaOutput;
        return listaOutput;
    }

    public void clickStruttura(int posizione) {
        Intent intent = new Intent(activityListaStrutturePage, OverviewStrutturaPage.class);
        intent.putExtra("Struttura",listaStruttureIntent.get(posizione));
        activityListaStrutturePage.startActivity(intent);
    }
}
