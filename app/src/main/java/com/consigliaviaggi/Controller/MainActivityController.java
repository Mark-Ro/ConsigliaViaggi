package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.consigliaviaggi.Entity.Utente;
import com.consigliaviaggi.GUI.LoginPage;
import com.consigliaviaggi.GUI.MainActivity;
import com.consigliaviaggi.GUI.ProfiloPage;

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
}
