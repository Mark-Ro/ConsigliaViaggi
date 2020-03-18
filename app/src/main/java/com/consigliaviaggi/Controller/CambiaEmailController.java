package com.consigliaviaggi.Controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.consigliaviaggi.DAO.CambiaEmailCognito;
import com.consigliaviaggi.DAO.UtenteDAO;
import com.consigliaviaggi.Entity.Utente;
import com.consigliaviaggi.GUI.CambiaEmailPage;

public class CambiaEmailController {

    private CambiaEmailPage cambiaEmailPage;
    private Context contextCambiaEmail;
    private Utente utente;

    public CambiaEmailController(CambiaEmailPage cambiaEmailPage, Context contextCambiaEmail) {
        this.cambiaEmailPage = cambiaEmailPage;
        this.contextCambiaEmail = contextCambiaEmail;
        utente = Utente.getIstance();
    }

    public void cambiaEmail(String email) {
        if (isNetworkAvailable()) {
            UtenteDAO utenteDAO = new UtenteDAO(contextCambiaEmail);
            if (utenteDAO.updateEmailUtente(email)) {
                CambiaEmailCognito cambiaEmailCognito = new CambiaEmailCognito(CambiaEmailController.this,contextCambiaEmail);
                cambiaEmailCognito.modificaEmailCognito(utente.getNickname(),email);
            }
            else
                Toast.makeText(cambiaEmailPage, "Email già presente!", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(cambiaEmailPage, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    public void cambiaEmailEffettuatoConSuccesso(String email) {
        Toast.makeText(cambiaEmailPage, "Email cambiata con successo!", Toast.LENGTH_SHORT).show();
        utente.setEmail(email);
        cambiaEmailPage.activityPrecedente();
    }

    public void cambiaEmailFallito(Exception exception) {
        Toast.makeText(cambiaEmailPage, "Operazione fallita: " + exception.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) contextCambiaEmail.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
