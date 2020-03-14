package com.consigliaviaggi.Controller;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.consigliaviaggi.Entity.Utente;

public class ProfiloController {
    private Context contextProfiloPage;
    private Utente utente;

    public ProfiloController(Context contextProfiloPage) {
        this.contextProfiloPage = contextProfiloPage;
        this.utente = Utente.getIstance();
    }

    public void setTextViewsProfiloPage(TextView textViewNomeProfilo, TextView textViewCognomeProfilo, TextView textViewEmailProfilo, TextView textViewNicknameProfilo, TextView textViewNumeroRecensioniProfilo) {

        Log.i("UTENTE",utente.getNome() + " " + utente.getCognome() + " " + utente.getEmail() + " " + utente.getNickname() + " " + String.valueOf(utente.getNumeroRecensioni()));

        textViewNomeProfilo.setText(utente.getNome());
        textViewCognomeProfilo.setText(utente.getCognome());
        textViewEmailProfilo.setText(utente.getEmail());
        textViewNicknameProfilo.setText(utente.getNickname());
        textViewNumeroRecensioniProfilo.setText(String.valueOf(utente.getNumeroRecensioni()));
    }
}
