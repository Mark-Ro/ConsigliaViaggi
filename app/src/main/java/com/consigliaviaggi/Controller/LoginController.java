package com.consigliaviaggi.Controller;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.consigliaviaggi.DAO.LoginCognito;
import com.consigliaviaggi.GUI.LoginPage;

public class LoginController {

    private Context contextLoginPage;
    private LoginCognito loginCognito;

    public Context getContextLoginPage() {
        return contextLoginPage;
    }

    public LoginController(Context context) {
        this.contextLoginPage = context;
    }

    public void effettuaLogin(String username, String password) {
        loginCognito = new LoginCognito(LoginController.this,username,password);
        loginCognito.effettuaLoginCognito();
    }

    public void loginEffettuatoConSuccesso(String username) {
        Intent intent = new Intent(contextLoginPage, LoginPage.class);
        intent.putExtra("Username",username);
        contextLoginPage.startActivity(intent);
    }

    //public void loginFallito()

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) contextLoginPage.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
