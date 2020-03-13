package com.consigliaviaggi.DAO;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Regions;

public class CognitoSettings {
    private String userPoolId="us-west-2_WQtiblUbT";
    private String clientId="qa2scn5afnaa8kve9esp6q71v";
    private String clientSecret="1ontrltih1ircm8o0086i86vavusfvck34plu01sbftad99ddrrv";
    private Regions cognitoRegion=Regions.US_WEST_2;
    private String identityPoolId="us-west-2:8e7fd219-55e9-445a-8cd0-652460ef9ae0";

    private Context context;

    public CognitoSettings(Context context) {
        this.context=context;
    }

    public String getUserPoolId() {
        return userPoolId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public Regions getCognitoRegion() {
        return cognitoRegion;
    }

    public CognitoUserPool getUserPool() {
        return new CognitoUserPool(context,userPoolId,clientId,clientSecret,cognitoRegion);
    }

    public CognitoCachingCredentialsProvider getCredentialsProvider() {
        return new CognitoCachingCredentialsProvider(context.getApplicationContext(),identityPoolId,cognitoRegion);
    }
}
