package com.consigliaviaggi.DAO;

import android.content.Context;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.UpdateAttributesHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler;
import com.consigliaviaggi.Controller.VerificationCodeController;

public class VerificationCodeCognito {

    private VerificationCodeController verificationCodeController;
    private CognitoSettings cognitoSettings;

    public VerificationCodeCognito(VerificationCodeController verificationCodeController, Context context) {
        this.verificationCodeController = verificationCodeController;
        cognitoSettings = new CognitoSettings(context);
    }

    public final GenericHandler confirmationCallback = new GenericHandler() {
        @Override
        public void onSuccess() {
            verificationCodeController.verificaEffettuataConSuccesso();
        }

        @Override
        public void onFailure(Exception exception) {
            verificationCodeController.verificaFallita(exception);
        }
    };

    public final VerificationHandler resendConfCodeHandler = new VerificationHandler() {

        @Override
        public void onSuccess(CognitoUserCodeDeliveryDetails verificationCodeDeliveryMedium) {
            verificationCodeController.resendEffettuatoConSuccesso(verificationCodeDeliveryMedium.getDestination());
        }

        @Override
        public void onFailure(Exception exception) {
            verificationCodeController.resendFallito(exception);
        }
    };

    public void verificaCodiceCognito(String username, String codice) {
        CognitoUser thisUser = cognitoSettings.getUserPool().getUser(username);
        thisUser.confirmSignUp(codice,false,confirmationCallback);
    }

    public void effettuaResendCognito(String username) {
        CognitoUser thisUser = cognitoSettings.getUserPool().getUser(username);
        thisUser.resendConfirmationCode(resendConfCodeHandler);
    }

    public void verificaCodiceEmailCognito(String username, String codice) {
        cognitoSettings.getUserPool().getUser(username).verifyAttributeInBackground("email",codice,confirmationCallback);
    }

    public void effettuaResendCambiaEmailCognito(String username) {
        cognitoSettings.getUserPool().getUser(username).getAttributeVerificationCodeInBackground("email",resendConfCodeHandler);
    }
}
