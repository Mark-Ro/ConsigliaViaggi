package com.consigliaviaggi.Cognito;

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

    private CognitoSettings cognitoSettings;
    private LoginController loginController;
    private Context context;

    private int resultLogin = -1;

    public LoginCognito(Context context) {
        this.context = context;
        cognitoSettings = new CognitoSettings(context);
    }

    public LoginCognito(LoginController loginController) {
        this.loginController = loginController;
        this.cognitoSettings = new CognitoSettings(loginController.getContextLoginPage());
    }

    public void effettuaLoginCognito(String username, final String password) {
        CognitoUser thisUser = cognitoSettings.getUserPool().getUser(username);
        thisUser.getSessionInBackground(new AuthenticationHandler() {
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
        });
    }

    public boolean isUserLoggable(String username, final String password) {
        boolean result = false;
        CognitoUser thisUser = cognitoSettings.getUserPool().getUser(username);
        thisUser.getSession(new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                resultLogin = 1;
                Log.i("LOGIN_COGNITO","IS_USER_LOGGABLE: " + resultLogin);
            }

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
                AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId,password,null);
                authenticationContinuation.setAuthenticationDetails(authenticationDetails);
                authenticationContinuation.continueTask();
            }

            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation continuation) {

            }

            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {
                continuation.continueTask();
            }

            @Override
            public void onFailure(Exception exception) {
                resultLogin = 0;
                Log.i("LOGIN_COGNITO","IS_USER_LOGGABLE: " + resultLogin);
            }
        });

        if (resultLogin == 1)
            result = true;
        Log.i("LOGIN_COGNITO","IS_USER_LOGGABLE: " + resultLogin);
        resultLogin = -1;
        return result;
    }

}
