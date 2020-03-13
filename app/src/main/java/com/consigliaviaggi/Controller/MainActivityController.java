package com.consigliaviaggi.Controller;

import android.content.Context;
import android.content.Intent;

import com.consigliaviaggi.Entity.Utente;
import com.consigliaviaggi.GUI.LoginPage;
import com.consigliaviaggi.GUI.ProfiloPage;
import com.consigliaviaggi.GUI.RicercaPage;

public class MainActivityController {

    private Context contextMainActivity;
    private Utente utente;

    public MainActivityController(Context contextMainActivity) {
        this.contextMainActivity = contextMainActivity;
    }

    public void openProfiloPage(){

        utente = Utente.getIstance();

        Intent intent;

        if (utente.isUtenteAutenticato())
            intent = new Intent(contextMainActivity, ProfiloPage.class);
        else
            intent = new Intent(contextMainActivity,LoginPage.class);

        contextMainActivity.startActivity(intent);
    }

    public void openRicercaPage() {

        Intent intent = new Intent(contextMainActivity, RicercaPage.class);

        contextMainActivity.startActivity(intent);
    }
}
