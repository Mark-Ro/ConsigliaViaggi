package com.consigliaviaggi.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.consigliaviaggi.DAO.CognitoSettings;
import com.consigliaviaggi.DAO.InterfacciaLambda;
import com.consigliaviaggi.DAO.RequestDetailsUtenteQuery;
import com.consigliaviaggi.DAO.ResponseDetailsQuery;
import com.consigliaviaggi.DAO.UtenteDAO;
import com.consigliaviaggi.Entity.Utente;
import com.consigliaviaggi.GUI.LoginPage;
import com.consigliaviaggi.GUI.ProfiloPage;
import com.consigliaviaggi.GUI.RicercaPage;
import com.consigliaviaggi.GUI.VerificationCodePage;

import static android.content.Context.MODE_PRIVATE;

public class MainActivityController {

    private Context contextMainActivity;
    private Utente utente;
    private UtenteDAO utenteDAO;

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String usernameSalvato = "username";

    public MainActivityController(Context contextMainActivity) {
        this.contextMainActivity = contextMainActivity;
        this.utente = Utente.getIstance();
        this.utenteDAO = new UtenteDAO(contextMainActivity,MainActivityController.this);
    }

    public void openProfiloPage(){

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
        if (!utente.isUtenteAutenticato() && username!=null) {
            utenteDAO.verificaEmailStatus(username);
            loadInformazioniUtente(username);
        }
        else
            inizializzaLambda();
    }

    public void loadInformazioniUtente(String username) {
        if (isNetworkAvailable()) {
            ThreadGetInformazioniUtente threadGetInformazioniUtente = new ThreadGetInformazioniUtente(username);
            threadGetInformazioniUtente.start();
        }
        else
            Toast.makeText(contextMainActivity, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    private class ThreadGetInformazioniUtente extends Thread {

        private String username;

        public ThreadGetInformazioniUtente(String username) {
            this.username = username;
        }

        @Override
        public void run() {
            super.run();
            utenteDAO.getInformazioniUtente(username);
        }
    }

    public void inizializzaLambda() {
        if (isNetworkAvailable()) {
            if (utente.getNome() == null || utente.getNome().equals("")) {
                ThreadInizializzaLambda threadInizializzaLambda = new ThreadInizializzaLambda();
                threadInizializzaLambda.start();
            }
        }
        else
            Toast.makeText(contextMainActivity, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    private class ThreadInizializzaLambda extends Thread {
        @Override
        public void run() {
            super.run();
            doQuery("null");
        }
    }

    private void doQuery(String inizializza) {
        CognitoSettings cognitoSettings = new CognitoSettings(contextMainActivity);
        CognitoCachingCredentialsProvider cognitoProvider = cognitoSettings.getCredentialsProvider();
        LambdaInvokerFactory lambdaInvokerFactory = new LambdaInvokerFactory(contextMainActivity, Regions.EU_CENTRAL_1, cognitoProvider);
        InterfacciaLambda interfacciaLambda = lambdaInvokerFactory.build(InterfacciaLambda.class);
        RequestDetailsUtenteQuery request = new RequestDetailsUtenteQuery();
        request.setNickname(inizializza);
        ResponseDetailsQuery responseDetails = interfacciaLambda.funzioneLambdaQueryUtente(request);
        Log.i("MAIN_ACTIVITY",responseDetails.getResultQuery());
    }

    public void verificaEmailNonVerificata() {
        SharedPreferences sharedPreferences = contextMainActivity.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String username = sharedPreferences.getString(usernameSalvato, null);
        Intent intent = new Intent(contextMainActivity, VerificationCodePage.class);
        intent.putExtra("Username",username);
        intent.putExtra("Password","token");
        intent.putExtra("ActivityChiamante","CambiaEmail");
        contextMainActivity.startActivity(intent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) contextMainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
