package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import com.consigliaviaggi.DAO.RecensioneDAO;
import com.consigliaviaggi.Entity.Struttura;
import com.consigliaviaggi.GUI.LoadingDialog;

public class InserimentoRecensioneController {

    private Activity activityInserimentoRecensionePage;
    private Context contextInserimentoRecensionePage;

    private LoadingDialog loadingDialog;

    public InserimentoRecensioneController(Activity activityInserimentoRecensionePage, Context contextInserimentoRecensionePage) {
        this.activityInserimentoRecensionePage = activityInserimentoRecensionePage;
        this.contextInserimentoRecensionePage = contextInserimentoRecensionePage;
    }

    public void inserimentoRecensione(final Struttura struttura, final String testo, final float voto) {
        if (isNetworkAvailable()) {
            new AsyncTask<Void,Void,Void>() {

                private String risultatoInserimento;

                @Override
                protected Void doInBackground(Void... voids) {
                    RecensioneDAO recensioneDAO = new RecensioneDAO(contextInserimentoRecensionePage);
                    risultatoInserimento = recensioneDAO.inserimentoRecensione(struttura.getIdStruttura(),testo,(int)voto);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    cancelLoadingDialog();
                    activityInserimentoRecensionePage.overridePendingTransition(0, 0);
                    activityInserimentoRecensionePage.finish();
                    activityInserimentoRecensionePage.overridePendingTransition(0, 0);
                    Toast.makeText(activityInserimentoRecensionePage, risultatoInserimento, Toast.LENGTH_SHORT).show();
                }
            }.execute();
        }
        else {
            cancelLoadingDialog();
            activityInserimentoRecensionePage.overridePendingTransition(0, 0);
            activityInserimentoRecensionePage.finish();
            activityInserimentoRecensionePage.overridePendingTransition(0, 0);
            Toast.makeText(activityInserimentoRecensionePage, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
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
        ConnectivityManager connectivityManager = (ConnectivityManager) contextInserimentoRecensionePage.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
