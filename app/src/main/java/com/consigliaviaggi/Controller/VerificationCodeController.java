package com.consigliaviaggi.Controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.consigliaviaggi.DAO.CognitoSettings;
import com.consigliaviaggi.DAO.VerificationCodeCognito;

public class VerificationCodeController {

    private Context contextVerificationCode;
    private VerificationCodeCognito verificationCodeDAO;
    private String username,password;

    public VerificationCodeController (Context contextVerificationCode, String username, String password) {
        this.contextVerificationCode = contextVerificationCode;
        this.username = username;
        this.password = password;
    }

    public void verificaCodice(String codice) {
        verificationCodeDAO = new VerificationCodeCognito(VerificationCodeController.this);
        if (!isNetworkAvailable())
            Toast.makeText(contextVerificationCode, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
        else {
            CognitoSettings cognitoSettings = new CognitoSettings(contextVerificationCode);
            CognitoUser thisUser = cognitoSettings.getUserPool().getUser(username);
            thisUser.confirmSignUp(codice,false,verificationCodeDAO.confirmationCallback);
        }
    }

    public void verificaEffettuataConSuccesso() {
        LoginController loginController = new LoginController(contextVerificationCode,username,password);
        loginController.effettuaLogin();
    }

    public void verificaFallita(Exception exception) {
        Toast.makeText(contextVerificationCode, "Errore: " + exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    public void effettuaResend() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        verificationCodeDAO = new VerificationCodeCognito(VerificationCodeController.this);
        CognitoSettings cognitoSettings = new CognitoSettings(contextVerificationCode);
        CognitoUser thisUser = cognitoSettings.getUserPool().getUser(username);
        thisUser.resendConfirmationCode(verificationCodeDAO.resendConfCodeHandler);
    }

    public void resendEffettuatoConSuccesso(String email) {
        Toast.makeText(contextVerificationCode, "Email inviata con successo a: " + email, Toast.LENGTH_SHORT).show();
    }

    public void resendFallito(Exception exception) {
        Toast.makeText(contextVerificationCode, "Errore: " + exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) contextVerificationCode.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
