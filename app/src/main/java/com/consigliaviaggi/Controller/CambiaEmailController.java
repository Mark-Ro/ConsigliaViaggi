package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import com.consigliaviaggi.DAO.CambiaEmailCognito;
import com.consigliaviaggi.DAO.UtenteDAO;
import com.consigliaviaggi.Entity.Utente;
import com.consigliaviaggi.GUI.CambiaEmailPage;
import com.consigliaviaggi.GUI.LoadingDialog;
import com.consigliaviaggi.GUI.VerificationCodePage;

public class CambiaEmailController {

    private CambiaEmailPage cambiaEmailPage;
    private Context contextCambiaEmail;
    private Utente utente;

    private LoadingDialog loadingDialog;

    public CambiaEmailController(CambiaEmailPage cambiaEmailPage, Context contextCambiaEmail) {
        this.cambiaEmailPage = cambiaEmailPage;
        this.contextCambiaEmail = contextCambiaEmail;
        utente = Utente.getIstance();
    }

    public void cambiaEmail(final String email) {

        if (!utente.getEmail().equals(email)) {
            if (isNetworkAvailable()) {
                new AsyncTask<Void,Void,Void>() {

                    private boolean emailPresente;

                    @Override
                    protected Void doInBackground(Void... voids) {
                        UtenteDAO utenteDAO = new UtenteDAO(contextCambiaEmail);
                        emailPresente = utenteDAO.updateEmailUtente(email);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        if (emailPresente) {
                            CambiaEmailCognito cambiaEmailCognito = new CambiaEmailCognito(CambiaEmailController.this, contextCambiaEmail);
                            cambiaEmailCognito.modificaEmailCognito(utente.getNickname(), email);
                        }
                        else {
                            cancelLoadingDialog();
                            Toast.makeText(cambiaEmailPage, "Email già presente!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
            }
            else {
                cancelLoadingDialog();
                Toast.makeText(cambiaEmailPage, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            cancelLoadingDialog();
            Toast.makeText(cambiaEmailPage, "Email uguale a quella attuale!", Toast.LENGTH_SHORT).show();
        }
    }

    public void cambiaEmailEffettuatoConSuccesso(String email) {
        cancelLoadingDialog();
        utente.setEmail(email);
        Intent intent = new Intent(contextCambiaEmail,VerificationCodePage.class);
        intent.putExtra("Username",utente.getNickname());
        intent.putExtra("Password","token");
        intent.putExtra("ActivityChiamante","CambiaEmail");
        contextCambiaEmail.startActivity(intent);
    }

    public void cambiaEmailFallito(Exception exception) {
        cancelLoadingDialog();
        Toast.makeText(cambiaEmailPage, "Operazione fallita: " + exception.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
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
        ConnectivityManager connectivityManager = (ConnectivityManager) contextCambiaEmail.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
