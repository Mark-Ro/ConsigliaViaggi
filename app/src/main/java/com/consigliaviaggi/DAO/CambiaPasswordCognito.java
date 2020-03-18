package com.consigliaviaggi.DAO;

import android.content.Context;
import android.util.Log;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.consigliaviaggi.Controller.CambiaPasswordController;

public class CambiaPasswordCognito {

    CognitoSettings cognitoSettings;
    CambiaPasswordController cambiaPasswordController;

    public CambiaPasswordCognito(CambiaPasswordController cambiaPasswordController, Context context) {
        this.cambiaPasswordController = cambiaPasswordController;
        this.cognitoSettings = new CognitoSettings(context);
    }

    final GenericHandler handler = new GenericHandler() {
        @Override
        public void onSuccess() {
            Log.i("Cognito","Password changed!");
            cambiaPasswordController.cambiaPasswordEffettuatoConSuccesso();
        }

        @Override
        public void onFailure(Exception exception) {
            Log.i("Cognito","Operazione fallita: " + exception.getLocalizedMessage());
            cambiaPasswordController.cambiaPasswordFallito(exception);
        }
    };

    public void modificaPasswordCognito(String username, String vecchiaPassword, String nuovaPassword) {
        cognitoSettings.getUserPool().getUser(username).changePasswordInBackground(vecchiaPassword, nuovaPassword, handler);
    }

}
