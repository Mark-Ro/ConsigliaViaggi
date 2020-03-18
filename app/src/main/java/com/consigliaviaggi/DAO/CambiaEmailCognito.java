package com.consigliaviaggi.DAO;

import android.content.Context;
import android.util.Log;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.UpdateAttributesHandler;
import com.consigliaviaggi.Controller.CambiaEmailController;

import java.util.List;

public class CambiaEmailCognito {

    private CognitoSettings cognitoSettings;
    private CambiaEmailController cambiaEmailController;

    public CambiaEmailCognito(CambiaEmailController cambiaEmailController, Context context) {
        this.cognitoSettings = new CognitoSettings(context);
        this.cambiaEmailController = cambiaEmailController;

    }

    public void modificaEmailCognito(String username, final String email) {

        CognitoUserAttributes attributes = new CognitoUserAttributes();
        attributes.getAttributes().put("email",email);
        cognitoSettings.getUserPool().getUser(username).updateAttributesInBackground(attributes, new UpdateAttributesHandler() {
            @Override
            public void onSuccess(List<CognitoUserCodeDeliveryDetails> attributesVerificationList) {
                Log.i("Cognito","Email updated!");
                cambiaEmailController.cambiaEmailEffettuatoConSuccesso(email);
            }

            @Override
            public void onFailure(Exception exception) {
                Log.i("Cognito","Errore cambia email: " + exception.getLocalizedMessage());
                cambiaEmailController.cambiaEmailFallito(exception);
            }
        });
    }
}
