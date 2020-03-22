package com.consigliaviaggi.Controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.consigliaviaggi.DAO.CambiaEmailCognito;
import com.consigliaviaggi.DAO.UtenteDAO;
import com.consigliaviaggi.Entity.Utente;
import com.consigliaviaggi.GUI.CambiaEmailPage;
import com.consigliaviaggi.GUI.VerificationCodePage;

public class CambiaEmailController {

    private CambiaEmailPage cambiaEmailPage;
    private Context contextCambiaEmail;
    private ProgressDialog progressDialog;
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
        progressDialog.cancel();
        utente.setEmail(email);
        Intent intent = new Intent(contextCambiaEmail,VerificationCodePage.class);
        intent.putExtra("Username",utente.getNickname());
        intent.putExtra("Password","token");
        intent.putExtra("ActivityChiamante","CambiaEmail");
        contextCambiaEmail.startActivity(intent);
    }

    public void cambiaEmailFallito(Exception exception) {
        progressDialog.cancel();
        Toast.makeText(cambiaEmailPage, "Operazione fallita: " + exception.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
    }

    public void openProgressDialog() {
        progressDialog = ProgressDialog.show(contextCambiaEmail, "","Caricamento...", true);
    }

    public void cancelProgressDialog() {
        progressDialog.cancel();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) contextCambiaEmail.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
