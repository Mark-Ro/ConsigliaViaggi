package com.consigliaviaggi.Controller;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.widget.Toast;

import com.consigliaviaggi.DAO.LoginCognito;
import com.consigliaviaggi.GUI.MainActivity;
import com.consigliaviaggi.GUI.VerificationCodePage;

public class LoginController {

    private Context contextLoginPage;
    private LoginCognito loginCognito;
    String username,password;

    public Context getContextLoginPage() {
        return contextLoginPage;
    }

    public LoginController(Context context, String username, String password) {
        this.contextLoginPage = context;
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
        Intent intent = new Intent(contextLoginPage, MainActivity.class);
        intent.putExtra("Username",username);
        contextLoginPage.startActivity(intent);
    }

    public void loginFallito(Exception exception) {
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) contextLoginPage.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
