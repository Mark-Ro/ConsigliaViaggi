package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.consigliaviaggi.DAO.RecensioneDAO;
import com.consigliaviaggi.Entity.Struttura;
import com.consigliaviaggi.GUI.LoadingDialog;
import com.consigliaviaggi.R;

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
                    risultatoInserimento = recensioneDAO.inserimentoRecensioneDatabase(struttura.getIdStruttura(),testo,(int)voto);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    cancelLoadingDialog();
                    mostraDialogResponso(risultatoInserimento);
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

    private void mostraDialogResponso(final String resultUpdate) {
        final Dialog responseDialog = new Dialog(contextInserimentoRecensionePage);
        responseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        responseDialog.setContentView(R.layout.layout_conferma_recensione_eliminata);
        responseDialog.setTitle("Responso eliminazione recensione");
        TextView textViewResponso = responseDialog.findViewById(R.id.textViewResponso);
        Log.i("INSERIMENTO_RECENSIONE",resultUpdate);
        if(resultUpdate.contains("Successfully"))
            textViewResponso.setText("Recensione Inserita con successo!");
        else if (resultUpdate.contains("bannato"))
            textViewResponso.setText("Utente bannato");
        else
            textViewResponso.setText("Struttura già recensita!");
        Button bottoneOk = responseDialog.findViewById(R.id.bottoneOk);
        bottoneOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resultUpdate!=null && resultUpdate.contains("Successfully")) {
                    responseDialog.dismiss();
                    activityInserimentoRecensionePage.overridePendingTransition(0, 0);
                    activityInserimentoRecensionePage.finish();
                    activityInserimentoRecensionePage.overridePendingTransition(0, 0);
                }
                else
                    responseDialog.dismiss();
            }
        });
        responseDialog.show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) contextInserimentoRecensionePage.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
