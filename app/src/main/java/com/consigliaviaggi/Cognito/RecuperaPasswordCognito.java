package com.consigliaviaggi.Cognito;

import android.content.Context;
import android.util.Log;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;
import com.consigliaviaggi.Controller.RecuperaPasswordController;

public class RecuperaPasswordCognito {

    private CognitoSettings cognitoSettings;
    private RecuperaPasswordController recuperaPasswordController;
    private ForgotPasswordContinuation resultContinuation;

    public RecuperaPasswordCognito(RecuperaPasswordController recuperaPasswordController, Context context) {
        this.recuperaPasswordController = recuperaPasswordController;
        this.cognitoSettings = new CognitoSettings(context);
    }

    final ForgotPasswordHandler callback = new ForgotPasswordHandler() {
        @Override
        public void onSuccess() {
            Log.i("Cognito","Password changed successfully!");
            recuperaPasswordController.operazioneCompletataConSuccesso();
        }

        @Override
        public void getResetCode(ForgotPasswordContinuation continuation) {
            Log.i("Cognito","in getResetCode...");
            CognitoUserCodeDeliveryDetails codeSentHere = continuation.getParameters();
            Log.i("Cognito","Code sent here: " + codeSentHere.getDestination());
            recuperaPasswordController.codiceRicevuto(codeSentHere.getDestination());
            resultContinuation = continuation;
        }

        @Override
        public void onFailure(Exception exception) {
            recuperaPasswordController.operazioneFallita(exception);
        }
    };

    public void riceviCodiceCognito(String username) {
        CognitoUser thisUser = cognitoSettings.getUserPool().getUser(username);
        Log.i("Cognito", "calling forgot password to get confirmation code....");
        thisUser.forgotPasswordInBackground(callback);
    }

    public void resettaPasswordCognito(String codice, String password) {
        Log.i("Cognito", "Got code and password setting, continuation object....");
        resultContinuation.setPassword(password);
        resultContinuation.setVerificationCode(codice);
        Log.i("Cognito", "Got code and password, calling continueTask()....");
        resultContinuation.continueTask();
    }
}
