package com.consigliaviaggi.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.consigliaviaggi.DAO.UtenteDAO;
import com.consigliaviaggi.Entity.Utente;
import com.consigliaviaggi.GUI.LoginPage;
import com.consigliaviaggi.GUI.ProfiloPage;
import com.consigliaviaggi.GUI.RicercaPage;

import static android.content.Context.MODE_PRIVATE;

public class MainActivityController {

    private Context contextMainActivity;
    private Utente utente;
    private UtenteDAO utenteDAO;

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String usernameSalvato = "username";

    public MainActivityController(Context contextMainActivity) {
        this.contextMainActivity = contextMainActivity;
    }

    public void openProfiloPage(){

        utente = Utente.getIstance();

        Intent intent;

        if (isNetworkAvailable()) {
            if (utente.isUtenteAutenticato()) {
                intent = new Intent(contextMainActivity, ProfiloPage.class);
                Log.i("MAIN_ACTIVITY", "AUTENTICATO: TRUE");
            } else {
                intent = new Intent(contextMainActivity, LoginPage.class);
                Log.i("MAIN_ACTIVITY", "AUTENTICATO: FALSE");
            }
            contextMainActivity.startActivity(intent);
        }
        else
            Toast.makeText(contextMainActivity, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    public void openRicercaPage() {

        if (isNetworkAvailable()) {
            Intent intent = new Intent(contextMainActivity, RicercaPage.class);
            contextMainActivity.startActivity(intent);
        }
        else
            Toast.makeText(contextMainActivity, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    public void saveUsername(String username) {
        SharedPreferences sharedPreferences = contextMainActivity.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(usernameSalvato, username);
        editor.apply();
        Log.i("SALVATAGGIO","Username " + username + " salvato!");
    }

    public void loadUsername() {
        SharedPreferences sharedPreferences = contextMainActivity.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String username = sharedPreferences.getString(usernameSalvato, null);
        Log.i("LOAD_USERNAME","Username: " + username);
        utente = Utente.getIstance();
        if (!utente.isUtenteAutenticato() && username!=null)
            loadInformazioniUtente(username);
    }

    public void loadInformazioniUtente(String username) {
        if (isNetworkAvailable()) {
            ThreadGetInformazioniUtente threadGetInformazioniUtente = new ThreadGetInformazioniUtente(username);
            threadGetInformazioniUtente.start();
        }
        else
            Toast.makeText(contextMainActivity, "Connessione internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    public class ThreadGetInformazioniUtente extends Thread {

        private String username;

        public ThreadGetInformazioniUtente(String username) {
            this.username = username;
        }

        @Override
        public void run() {
            super.run();
            utenteDAO = new UtenteDAO(contextMainActivity);
            utenteDAO.getInformazioniUtente(username);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) contextMainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
