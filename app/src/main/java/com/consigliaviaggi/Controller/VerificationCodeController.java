package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.widget.Toast;

import com.consigliaviaggi.DAO.VerificationCodeCognito;
import com.consigliaviaggi.GUI.LoadingDialog;

public class VerificationCodeController {

    private Context contextVerificationCode;
    private VerificationCodeCognito verificationCodeCognito;
    private String username,password;

    private LoadingDialog loadingDialog;

    public VerificationCodeController (Context contextVerificationCode, String username, String password) {
        this.contextVerificationCode = contextVerificationCode;
        verificationCodeCognito = new VerificationCodeCognito(VerificationCodeController.this,contextVerificationCode);
        this.username = username;
        this.password = password;
    }

    public void verificaCodice(String codice) {

        if (!isNetworkAvailable()) {
            cancelLoadingDialog();
            Toast.makeText(contextVerificationCode, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
        }
        else
            verificationCodeCognito.verificaCodiceCognito(username,codice);
    }

    public void verificaEffettuataConSuccesso() {
        cancelLoadingDialog();
        LoginController loginController = new LoginController(contextVerificationCode,username,password);
        loginController.effettuaLogin();
    }

    public void verificaFallita(Exception exception) {
        cancelLoadingDialog();
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
        if (isNetworkAvailable())
            verificationCodeCognito.verificaCodiceEmailCognito(username,codice);
        else {
            cancelLoadingDialog();
            Toast.makeText(contextVerificationCode, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
        }
    }

    public void effettuaResendCambiaEmail() {
        verificationCodeCognito.effettuaResendCambiaEmailCognito(username);
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
        ConnectivityManager connectivityManager = (ConnectivityManager) contextVerificationCode.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
