package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.consigliaviaggi.Cognito.RegistrazioneCognito;
import com.consigliaviaggi.DAO.UtenteDAO;
import com.consigliaviaggi.GUI.LoadingDialog;
import com.consigliaviaggi.GUI.VerificationCodePage;

public class RegistrazioneController {

    private Context context;
    private UtenteDAO utenteDAO;
    private LoadingDialog loadingDialog;

    public RegistrazioneController(Context context) {
        this.context = context;
        this.utenteDAO = new UtenteDAO(context);
    }

    public void effettuaRegistrazione(final String nome, final String cognome, final String email, final String nickname, final String password, final boolean nomePubblico) {

        if (isNetworkAvailable()) {
            new AsyncTask<Void,Void,Void>() {

                private String resultInsert;

                @Override
                protected Void doInBackground(Void... voids) {

                    resultInsert = utenteDAO.inserimentoUtente(nome, cognome, email, nickname, password, nomePubblico);
                    Log.i("REGISTRAZIONE_CONTROLLER",resultInsert);

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    if (resultInsert.contains("uniqueMail")) {
                        cancelLoadingDialog();
                        Toast.makeText(context, "Mail già esistente!", Toast.LENGTH_SHORT).show();
                    }
                    else if (resultInsert.contains("PRIMARY")) {
                        cancelLoadingDialog();
                        Toast.makeText(context, "Nickname già esistente!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        RegistrazioneCognito registrazioneCognito = new RegistrazioneCognito(RegistrazioneController.this,context,nickname,password,email);
                        registrazioneCognito.effettuaRegistrazioneCognito();
                    }
                }
            }.execute();
        }
        else {
            cancelLoadingDialog();
            Toast.makeText(context, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
        }
    }
    
    public void registrazioneEffettuataConSuccesso(String nickname, String password, String email) {
        cancelLoadingDialog();
        Toast.makeText(context, "Registrazione effettuata! Codice di verifica inviato a: " + email, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, VerificationCodePage.class);
        intent.putExtra("Username",nickname);
        intent.putExtra("Password",password);
        intent.putExtra("ActivityChiamante","Registrazione");
        context.startActivity(intent);
    }

    public void registrazioneFallita(Exception exception) {
        cancelLoadingDialog();
        Toast.makeText(context, "Registrazione fallita: " + exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
