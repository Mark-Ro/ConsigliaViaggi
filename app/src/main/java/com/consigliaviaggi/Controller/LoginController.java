package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.widget.Toast;

import com.consigliaviaggi.DAO.LoginCognito;
import com.consigliaviaggi.GUI.LoadingDialog;
import com.consigliaviaggi.GUI.MainActivity;
import com.consigliaviaggi.GUI.RecuperaPasswordPage;
import com.consigliaviaggi.GUI.RegistrazionePage;
import com.consigliaviaggi.GUI.VerificationCodePage;

public class LoginController {

    private Context contextLoginPage;
    private LoginCognito loginCognito;
    private Activity activityVerificationCodePage;
    private LoadingDialog loadingDialog;
    private String username,password;

    public Context getContextLoginPage() {
        return contextLoginPage;
    }

    public LoginController(Context context, String username, String password) {
        this.contextLoginPage = context;
        this.username = username;
        this.password = password;
    }

    public LoginController(Context contextLoginPage) {
        this.contextLoginPage = contextLoginPage;
    }

    public LoginController(Context context, Activity activityVerificationCodePage, String username, String password) {
        this.contextLoginPage = context;
        this.activityVerificationCodePage = activityVerificationCodePage;
        this.username = username;
        this.password = password;
    }

    public void effettuaLogin() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (isNetworkAvailable()) {
            loginCognito = new LoginCognito(LoginController.this, username, password);
            loginCognito.effettuaLoginCognito();
        }
        else
            Toast.makeText(contextLoginPage, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    public void loginEffettuatoConSuccesso() {
        cancelLoadingDialog();
        Intent intent = new Intent(contextLoginPage, MainActivity.class);
        intent.putExtra("Username",username);
        if (activityVerificationCodePage!=null)
            activityVerificationCodePage.finish();
        contextLoginPage.startActivity(intent);
    }

    public void loginFallito(Exception exception) {
        cancelLoadingDialog();
        if (exception.getLocalizedMessage().contains("not confirmed") || exception.getLocalizedMessage().contains("Failed to authenticate user")) {
            Intent intent = new Intent(contextLoginPage, VerificationCodePage.class);
            intent.putExtra("Username",username);
            intent.putExtra("Password",password);
            intent.putExtra("ActivityChiamante","Login");
            contextLoginPage.startActivity(intent);
        }
        else
            Toast.makeText(contextLoginPage, "Login fallito: " + exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

    }

    public void openHomePage() {
        if (isNetworkAvailable()) {
            Intent intent = new Intent(contextLoginPage,MainActivity.class);
            contextLoginPage.startActivity(intent);
        }
        else
            Toast.makeText(contextLoginPage, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    public void openRegistrazionePage() {
        if (isNetworkAvailable()) {
            Intent intent = new Intent(contextLoginPage, RegistrazionePage.class);
            contextLoginPage.startActivity(intent);
        }
        else
            Toast.makeText(contextLoginPage, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    public void openRecuperaPasswordPage() {
        if (isNetworkAvailable()) {
            Intent intent = new Intent(contextLoginPage, RecuperaPasswordPage.class);
            contextLoginPage.startActivity(intent);
        }
        else
            Toast.makeText(contextLoginPage, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
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
        ConnectivityManager connectivityManager = (ConnectivityManager) contextLoginPage.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
