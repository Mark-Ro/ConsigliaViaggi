package com.consigliaviaggi.Controller;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.consigliaviaggi.Entity.Utente;
import com.consigliaviaggi.GUI.ProfiloPage;

public class ProfiloController {
    private Context contextProfiloPage;
    private Utente utente;

    public ProfiloController(Context contextProfiloPage) {
        this.contextProfiloPage = contextProfiloPage;
        this.utente = Utente.getIstance();
    }

    public void setTextViewsProfiloPage(TextView textViewNomeProfilo, TextView textViewCognomeProfilo, TextView textViewEmailProfilo, TextView textViewNicknameProfilo, TextView textViewNumeroRecensioniProfilo) {

        Log.i("UTENTE",utente.getNome() + " " + utente.getCognome() + " " + utente.getEmail() + " " + utente.getNickname() + " " + String.valueOf(utente.getNumeroRecensioni()));

        if (utente.getNome()!=null) {
            textViewNomeProfilo.setText(utente.getNome());
            textViewCognomeProfilo.setText(utente.getCognome());
            textViewEmailProfilo.setText(utente.getEmail());
            textViewNicknameProfilo.setText(utente.getNickname());
            textViewNumeroRecensioniProfilo.setText(String.valueOf(utente.getNumeroRecensioni()));
        }
    }

    public void updateTextViewsProfiloPage(ProfiloPage profiloPage) {
        utente = Utente.getIstance();
        if (utente.isCaricamentoUtente()==false) {
            RiaggiornaProfiloPage riaggiornaProfiloPage = new RiaggiornaProfiloPage(profiloPage);
            riaggiornaProfiloPage.start();
        }
    }

    private class RiaggiornaProfiloPage extends Thread {

        private ProfiloPage profiloPage;

        public RiaggiornaProfiloPage(ProfiloPage profiloPage) {
            this.profiloPage = profiloPage;
        }

        @Override
        public void run() {
            super.run();
            Utente utente = Utente.getIstance();
            while (utente.isCaricamentoUtente()==false)
                Log.i("PROFILO_CICLO","Sono nel ciclo...");
            Log.i("THREAD_PROFILO","Uscito dal while");
            profiloPage.finish();
            profiloPage.overridePendingTransition(0, 0);
            profiloPage.startActivity(profiloPage.getIntent());
            profiloPage.overridePendingTransition(0, 0);
        }
    }
}
