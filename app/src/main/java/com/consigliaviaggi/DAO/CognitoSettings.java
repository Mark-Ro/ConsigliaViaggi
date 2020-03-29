package com.consigliaviaggi.DAO;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Regions;

public class CognitoSettings {
    private String userPoolId="eu-central-1_zOS8labm0";
    private String clientId="43ghmtmpdkb2e9o9u8014od99m";
    private String clientSecret="f7bjm500km2necs8ifivce49kdsehpitum5vtg80krf42374498";
    private Regions cognitoRegion=Regions.EU_CENTRAL_1;
    private String identityPoolId="eu-central-1:4376a734-cebe-49c9-8d54-4d77b0e7e335";

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
