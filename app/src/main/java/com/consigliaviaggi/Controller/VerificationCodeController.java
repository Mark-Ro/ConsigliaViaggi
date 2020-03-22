package com.consigliaviaggi.Controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.widget.Toast;

import com.consigliaviaggi.DAO.VerificationCodeCognito;

public class VerificationCodeController {

    private Context contextVerificationCode;
    private VerificationCodeCognito verificationCodeCognito;
    private ProgressDialog progressDialog;
    private String username,password;

    public VerificationCodeController (Context contextVerificationCode, String username, String password) {
        this.contextVerificationCode = contextVerificationCode;
        verificationCodeCognito = new VerificationCodeCognito(VerificationCodeController.this,contextVerificationCode);
        this.username = username;
        this.password = password;
    }

    public void verificaCodice(String codice) {

        if (!isNetworkAvailable())
            Toast.makeText(contextVerificationCode, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
        else {
            progressDialog = ProgressDialog.show(contextVerificationCode, "","Caricamento...", true);
            verificationCodeCognito.verificaCodiceCognito(username,codice);
        }
    }

    public void verificaEffettuataConSuccesso() {
        progressDialog.cancel();
        LoginController loginController = new LoginController(contextVerificationCode,username,password);
        loginController.effettuaLogin();
    }

    public void verificaFallita(Exception exception) {
        progressDialog.cancel();
        Toast.makeText(contextVerificationCode, "Errore: " + exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    public void effettuaResend() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        verificationCodeCognito.effettuaResendCognito(username);
    }

    public void resendEffettuatoConSuccesso(String email) {
        Toast.makeText(contextVerificationCode, "Email inviata con successo a: " + email, Toast.LENGTH_SHORT).show();
    }

    public void resendFallito(Exception exception) {
        Toast.makeText(contextVerificationCode, "Errore: " + exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    public void verificaCodiceEmail(String codice) {
        if (isNetworkAvailable()) {
            progressDialog = ProgressDialog.show(contextVerificationCode, "","Caricamento...", true);
            verificationCodeCognito.verificaCodiceEmailCognito(username,codice);
        }
        else
            Toast.makeText(contextVerificationCode, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    public void effettuaResendCambiaEmail() {
        verificationCodeCognito.effettuaResendCambiaEmailCognito(username);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) contextVerificationCode.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
