package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.consigliaviaggi.DAO.RecensioneDAO;
import com.consigliaviaggi.DAO.UtenteDAO;
import com.consigliaviaggi.Entity.Recensione;
import com.consigliaviaggi.Entity.Utente;
import com.consigliaviaggi.GUI.CambiaEmailPage;
import com.consigliaviaggi.GUI.CambiaPasswordPage;
import com.consigliaviaggi.GUI.LoadingDialog;
import com.consigliaviaggi.GUI.MainActivity;
import com.consigliaviaggi.GUI.MappaPage;
import com.consigliaviaggi.GUI.MieRecensioniPage;
import com.consigliaviaggi.GUI.ProfiloPage;

import java.util.ArrayList;

public class ProfiloController {
    private Context contextProfiloPage;
    private Activity activityProfiloPage;
    private Utente utente;

    private LoadingDialog loadingDialog;

    public ProfiloController(Context contextProfiloPage, Activity activityProfiloPage) {
        this.contextProfiloPage = contextProfiloPage;
        this.activityProfiloPage = activityProfiloPage;
        this.utente = Utente.getIstance();
    }

    public void setTextViewsProfiloPage(TextView textViewNomeProfilo, TextView textViewCognomeProfilo, TextView textViewEmailProfilo, TextView textViewNicknameProfilo, TextView textViewNumeroRecensioniProfilo, Button bottoneLogout) {

        Log.i("UTENTE",utente.getNome() + " " + utente.getCognome() + " " + utente.getEmail() + " " + utente.getNickname() + " " + String.valueOf(utente.getNumeroRecensioni()));

        if (utente.getNome()!=null) {
            textViewNomeProfilo.setText(utente.getNome());
            textViewCognomeProfilo.setText(utente.getCognome());
            textViewEmailProfilo.setText(utente.getEmail());
            textViewNicknameProfilo.setText(utente.getNickname());
            textViewNumeroRecensioniProfilo.setText(String.valueOf(utente.getNumeroRecensioni()));
            bottoneLogout.setEnabled(true);
        }
    }

    public void updateTextViewsProfiloPage(ProfiloPage profiloPage) {
        utente = Utente.getIstance();
        if (utente.isCaricamentoUtente()==false) {
            openLoadingDialog(activityProfiloPage);
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
                Log.i("PROFILO_CICLO", "Sono nel ciclo...");
            Log.i("THREAD_PROFILO","Uscito dal while");
            cancelLoadingDialog();
            profiloPage.finish();
            profiloPage.overridePendingTransition(0, 0);
            profiloPage.startActivity(profiloPage.getIntent());
            profiloPage.overridePendingTransition(0, 0);
        }
    }

    public void logout() {
        UtenteDAO utenteDAO = new UtenteDAO(contextProfiloPage);
        utenteDAO.logout();
        utente.resettaUtente();
        MainActivityController mainActivityController = new MainActivityController(contextProfiloPage);
        mainActivityController.saveUsername(null);
        Log.i("UTENTE","Autenticato: " + String.valueOf(utente.isUtenteAutenticato()));
        Log.i("UTENTE","Caricamento: " + String.valueOf(utente.isCaricamentoUtente()));
        Intent intent = new Intent(contextProfiloPage, MainActivity.class);
        intent.putExtra("Logout",true);
        contextProfiloPage.startActivity(intent);
    }

    private ArrayList<Recensione> getMieRecensioni() {
        RecensioneDAO recensioneDAO = new RecensioneDAO(contextProfiloPage);
        Utente utente = Utente.getIstance();
        ArrayList<Recensione> listaMieRecensioni = recensioneDAO.getMieRecensioniFromDatabase(utente.getNickname());
        return listaMieRecensioni;
    }

    public void openCambiaPasswordPage() {
        Intent intent = new Intent(contextProfiloPage, CambiaPasswordPage.class);
        contextProfiloPage.startActivity(intent);
    }

    public void openHomePage() {
        Intent intent = new Intent(contextProfiloPage,MainActivity.class);
        contextProfiloPage.startActivity(intent);
    }

    public void openCambiaEmailPage() {
        Intent intent = new Intent(contextProfiloPage, CambiaEmailPage.class);
        contextProfiloPage.startActivity(intent);
    }
    public void openMappaPage() {
        if (isNetworkAvailable()) {
            Intent intent = new Intent(contextProfiloPage, MappaPage.class);
            contextProfiloPage.startActivity(intent);
        }
        else
            Toast.makeText(contextProfiloPage, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    public void openMieRecensioniPage() {
        if (isNetworkAvailable()) {
            new AsyncTask<Void,Void,Void>() {

                private ArrayList<Recensione> listaMieRecensioni;

                @Override
                protected Void doInBackground(Void... voids) {
                    listaMieRecensioni = getMieRecensioni();
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    utente.setNumeroRecensioni(listaMieRecensioni.size());
                    cancelLoadingDialog();
                    Intent intent = new Intent(contextProfiloPage, MieRecensioniPage.class);
                    intent.putExtra("ListaMieRecensioni",listaMieRecensioni);
                    contextProfiloPage.startActivity(intent);
                }
            }.execute();
        }
        else
            Toast.makeText(contextProfiloPage, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
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
        ConnectivityManager connectivityManager = (ConnectivityManager) contextProfiloPage.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
