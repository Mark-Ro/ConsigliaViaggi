package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.consigliaviaggi.DAO.CambiaPasswordCognito;
import com.consigliaviaggi.Entity.Utente;
import com.consigliaviaggi.GUI.CambiaPasswordPage;
import com.consigliaviaggi.GUI.LoadingDialog;

public class CambiaPasswordController {

    private CambiaPasswordPage cambiaPasswordPage;
    private Context contextCambiaPassword;
    private Utente utente;

    private LoadingDialog loadingDialog;

    public CambiaPasswordController(CambiaPasswordPage cambiaPasswordPage, Context contextCambiaPassword) {
        this.cambiaPasswordPage = cambiaPasswordPage;
        this.contextCambiaPassword = contextCambiaPassword;
        this.utente = Utente.getIstance();
    }

    public void cambiaPassword(String vecchiaPassword, String nuovaPassword) {
        if (isNetworkAvailable()) {
            CambiaPasswordCognito cambiaPasswordCognito = new CambiaPasswordCognito(CambiaPasswordController.this,contextCambiaPassword);
            cambiaPasswordCognito.modificaPasswordCognito(utente.getNickname(),vecchiaPassword,nuovaPassword);
        }
        else {
            cancelLoadingDialog();
            Toast.makeText(contextCambiaPassword, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
        }
    }

    public void cambiaPasswordEffettuatoConSuccesso() {
        cancelLoadingDialog();
        Toast.makeText(contextCambiaPassword, "Password cambiata con successo!", Toast.LENGTH_SHORT).show();
        cambiaPasswordPage.activityPrecedente();
    }

    public void cambiaPasswordFallito(Exception exception) {
        cancelLoadingDialog();
        Toast.makeText(contextCambiaPassword, "Operazione fallita: " + exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    public void openLoadingDialog(Activity activity) {
        loadingDialog = new LoadingDialog(activity);
        loadingDialog.startLoadingDialog();
    }

    public void cancelLoadingDialog() {
        if (loadingDialog!=null)
            loadingDialog.dismissDialog();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) contextCambiaPassword.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
