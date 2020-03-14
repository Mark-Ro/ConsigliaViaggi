package com.consigliaviaggi.DAO;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler;
import com.consigliaviaggi.Controller.VerificationCodeController;

public class VerificationCodeDAO {

    private VerificationCodeController verificationCodeController;

    public VerificationCodeDAO(VerificationCodeController verificationCodeController) {
        this.verificationCodeController = verificationCodeController;
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
}
