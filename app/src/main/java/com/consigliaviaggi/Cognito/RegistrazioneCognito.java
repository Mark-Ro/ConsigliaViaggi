package com.consigliaviaggi.Cognito;

import android.content.Context;
import android.util.Log;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult;
import com.consigliaviaggi.Controller.RegistrazioneController;

public class RegistrazioneCognito {

    private CognitoSettings cognitoSettings;
    private RegistrazioneController registrazioneController;
    private String nickname,password,email;

    private final CognitoUserAttributes userAttributes=new CognitoUserAttributes();

    public RegistrazioneCognito(RegistrazioneController registrazioneController, Context context, String nickname, String password, String email) {
        this.registrazioneController = registrazioneController;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.cognitoSettings = new CognitoSettings(context);
    }

    final SignUpHandler signupCallback = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser user, SignUpResult signUpResult) {
            Log.i("Cognito","sign up success ... is confirmed " + signUpResult.isUserConfirmed());

            registrazioneController.registrazioneEffettuataConSuccesso(nickname,password,signUpResult.getCodeDeliveryDetails().getDestination());
        }

        @Override
        public void onFailure(Exception exception) {

            registrazioneController.registrazioneFallita(exception);
        }
    };

    public void effettuaRegistrazioneCognito() {

        userAttributes.addAttribute("given_name", nickname);
        userAttributes.addAttribute("email", email);

        cognitoSettings.getUserPool().signUpInBackground(nickname, password, userAttributes, null, signupCallback);
    }

}
