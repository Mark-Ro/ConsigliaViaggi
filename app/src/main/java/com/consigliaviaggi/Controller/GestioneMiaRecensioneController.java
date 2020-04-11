package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import com.consigliaviaggi.DAO.RecensioneDAO;
import com.consigliaviaggi.GUI.LoadingDialog;

public class GestioneMiaRecensioneController {

    private Context contextGestioneMiaRecensionePage;
    private LoadingDialog loadingDialog;

    public GestioneMiaRecensioneController(Context contextGestioneMiaRecensionePage) {
        this.contextGestioneMiaRecensionePage = contextGestioneMiaRecensionePage;
    }

    public void updateRecensione(final int idRecensione, final String testo, final float voto) {
        if (isNetworkAvailable()) {
            new AsyncTask<Void,Void,Void>() {

                private String resultUpdate;

                @Override
                protected Void doInBackground(Void... voids) {
                    RecensioneDAO recensioneDAO = new RecensioneDAO(contextGestioneMiaRecensionePage);
                    resultUpdate = recensioneDAO.updateRecensione(idRecensione, testo, (int) voto);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    cancelLoadingDialog();
                    Toast.makeText(contextGestioneMiaRecensionePage, resultUpdate, Toast.LENGTH_SHORT).show();
                }
            }.execute();
        }
        else {
            cancelLoadingDialog();
            Toast.makeText(contextGestioneMiaRecensionePage, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
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
        ConnectivityManager connectivityManager = (ConnectivityManager) contextGestioneMiaRecensionePage.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
