package com.consigliaviaggi.Controller;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.consigliaviaggi.DAO.RecuperaPasswordCognito;
import com.consigliaviaggi.GUI.LoginPage;

public class RecuperaPasswordController {

    private Context contextRecuperaPassword;
    private RecuperaPasswordCognito recuperaPasswordCognito;
    public RecuperaPasswordController(Context contextRecuperaPassword) {
        this.contextRecuperaPassword = contextRecuperaPassword;
        this.recuperaPasswordCognito = new RecuperaPasswordCognito(RecuperaPasswordController.this,contextRecuperaPassword);
    }

    public void riceviCodice(String username) {
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
        Toast.makeText(contextRecuperaPassword, "Operazione riuscita!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(contextRecuperaPassword, LoginPage.class);
        contextRecuperaPassword.startActivity(intent);
    }

    public void operazioneFallita(Exception exception) {
        Toast.makeText(contextRecuperaPassword, "Operazione fallita: " + exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    public void resettaPassword(String codice, String password) {
        if (isNetworkAvailable()) {
            recuperaPasswordCognito.resettaPasswordCognito(codice,password);
        }
        else
            Toast.makeText(contextRecuperaPassword, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) contextRecuperaPassword.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
