package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.consigliaviaggi.DAO.RecensioneDAO;
import com.consigliaviaggi.GUI.LoadingDialog;
import com.consigliaviaggi.R;

public class GestioneMiaRecensioneController {

    private Activity activityGestioneMiaRecensionePage;
    private Context contextGestioneMiaRecensionePage;
    private LoadingDialog loadingDialog;

    private RecensioneDAO recensioneDAO;

    public GestioneMiaRecensioneController(Activity activityGestioneMiaRecensionePage, Context contextGestioneMiaRecensionePage) {
        this.activityGestioneMiaRecensionePage = activityGestioneMiaRecensionePage;
        this.contextGestioneMiaRecensionePage = contextGestioneMiaRecensionePage;
        this.recensioneDAO = new RecensioneDAO(contextGestioneMiaRecensionePage);
    }

    public void updateRecensione(final int idRecensione, final String testo, final float voto) {
        if (isNetworkAvailable()) {
            new AsyncTask<Void,Void,Void>() {

                private String resultUpdate;

                @Override
                protected Void doInBackground(Void... voids) {
                    resultUpdate = recensioneDAO.updateRecensioneFromDatabase(idRecensione, testo, (int) voto);
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

    private void deleteRecensioneTask(final int idRecensione) {
        if (isNetworkAvailable()) {
            new AsyncTask<Void,Void,Void>() {

                private String resultUpdate;

                @Override
                protected Void doInBackground(Void... voids) {
                    resultUpdate = recensioneDAO.deleteRecensioneFromDatabase(idRecensione);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    cancelLoadingDialog();
                    mostraDialogResponso(resultUpdate);
                }
            }.execute();
        }
        else {
            cancelLoadingDialog();
            Toast.makeText(contextGestioneMiaRecensionePage, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteRecensione(final int idRecensione) {
        final Dialog deleteRecensioniDialog = new Dialog(contextGestioneMiaRecensionePage);
        deleteRecensioniDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        deleteRecensioniDialog.setContentView(R.layout.layout_dialog_elimina);
        deleteRecensioniDialog.setTitle("Conferma eliminazione recensione");
        Button bottoneAnnulla,bottoneConferma;
        bottoneAnnulla = deleteRecensioniDialog.findViewById(R.id.buttonAnnulla);
        bottoneConferma = deleteRecensioniDialog.findViewById(R.id.buttonConferma);
        bottoneAnnulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecensioniDialog.dismiss();
            }
        });

        bottoneConferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecensioniDialog.dismiss();
                openLoadingDialog(activityGestioneMiaRecensionePage);
                deleteRecensioneTask(idRecensione);
            }
        });

        deleteRecensioniDialog.show();
    }

    public void openLoadingDialog(Activity activity) {
        loadingDialog = new LoadingDialog(activity);
        loadingDialog.startLoadingDialog();
    }

    public void cancelLoadingDialog() {
        if (loadingDialog!=null)
            loadingDialog.dismissDialog();
    }

    private void mostraDialogResponso(final String resultUpdate) {
        final Dialog  responseDialog = new Dialog(contextGestioneMiaRecensionePage);
        responseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        responseDialog.setContentView(R.layout.layout_conferma_recensione_eliminata);
        responseDialog.setTitle("Responso eliminazione recensione");
        TextView textViewResponso = responseDialog.findViewById(R.id.textViewResponso);
        textViewResponso.setText(resultUpdate);
        Button bottoneOk = responseDialog.findViewById(R.id.bottoneOk);
        bottoneOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resultUpdate!=null && resultUpdate.contains("Successfully")) {
                    responseDialog.dismiss();
                    activityGestioneMiaRecensionePage.overridePendingTransition(0, 0);
                    activityGestioneMiaRecensionePage.finish();
                    activityGestioneMiaRecensionePage.overridePendingTransition(0, 0);
                }
                else
                    responseDialog.dismiss();
            }
        });
        responseDialog.show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) contextGestioneMiaRecensionePage.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
