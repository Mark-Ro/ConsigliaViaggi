package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.consigliaviaggi.Cognito.RecuperaPasswordCognito;
import com.consigliaviaggi.GUI.LoadingDialog;
import com.consigliaviaggi.GUI.RecuperaPasswordPage;

public class RecuperaPasswordController {

    private RecuperaPasswordPage recuperaPasswordPage;
    private Context contextRecuperaPassword;
    private RecuperaPasswordCognito recuperaPasswordCognito;

    private LoadingDialog loadingDialog;

    public RecuperaPasswordController(RecuperaPasswordPage recuperaPasswordPage, Context contextRecuperaPassword) {
        this.recuperaPasswordPage = recuperaPasswordPage;
        this.contextRecuperaPassword = contextRecuperaPassword;
        this.recuperaPasswordCognito = new RecuperaPasswordCognito(RecuperaPasswordController.this,contextRecuperaPassword);
    }

    public void riceviCodiceViaEmail(String username) {
        if (isNetworkAvailable()) {
            recuperaPasswordCognito.riceviCodiceCognito(username);
        }
        else
            Toast.makeText(contextRecuperaPassword, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    public void codiceRicevuto(String email) {
        Toast.makeText(contextRecuperaPassword, "Codice inviato a: " + email, Toast.LENGTH_SHORT).show();
    }

    public void operazioneCompletataConSuccesso() {
        cancelLoadingDialog();
        Toast.makeText(contextRecuperaPassword, "Operazione riuscita!", Toast.LENGTH_SHORT).show();
        recuperaPasswordPage.activityPrecedente();
    }

    public void operazioneFallita(Exception exception) {
        cancelLoadingDialog();
        if(exception.getLocalizedMessage().contains("Invalid verification code"))
            Toast.makeText(contextRecuperaPassword, "Codice errato!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(contextRecuperaPassword, "Operazione fallita: " + exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    public void resettaPassword(String codice, String password) {
        if (isNetworkAvailable()) {
            recuperaPasswordCognito.resettaPasswordCognito(codice,password);
        }
        else {
            cancelLoadingDialog();
            Toast.makeText(contextRecuperaPassword, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
        }
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
        ConnectivityManager connectivityManager = (ConnectivityManager) contextRecuperaPassword.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
