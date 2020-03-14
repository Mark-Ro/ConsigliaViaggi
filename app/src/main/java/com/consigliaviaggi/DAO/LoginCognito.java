package com.consigliaviaggi.DAO;

import android.content.Context;
import android.util.Log;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.consigliaviaggi.Controller.LoginController;

public class LoginCognito {

    private LoginController loginController;
    private String username,password;

    public LoginCognito(LoginController loginController, String username, String password) {
        this.loginController = loginController;
        this.username = username;
        this.password = password;
    }

    final AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
            Log.i("Cognito","Login avvenuto con successo, puoi prendere il token!");
            loginController.loginEffettuatoConSuccesso();
        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
            Log.i("Cognito","in getAuthenticationDetails().....");
            AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId,password,null);
            authenticationContinuation.setAuthenticationDetails(authenticationDetails);
            authenticationContinuation.continueTask();
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
            Log.i("Cognito","in getMFACode()...."); //Necessario per l'autenticazione multifattore
        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {
            Log.i("Cognito","in authenticationChallenge...");
            continuation.continueTask();
        }

        @Override
        public void onFailure(Exception exception) {
            Log.i("Cognito","Login failed" + exception.getLocalizedMessage());
            loginController.loginFallito(exception);
        }
    };

    public void effettuaLoginCognito() {
        CognitoSettings cognitoSettings = new CognitoSettings(loginController.getContextLoginPage());
        CognitoUser thisUser = cognitoSettings.getUserPool().getUser(username);
        thisUser.getSessionInBackground(authenticationHandler);
    }

}
