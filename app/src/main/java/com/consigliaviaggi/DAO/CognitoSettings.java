package com.consigliaviaggi.DAO;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Regions;

public class CognitoSettings {
    private String userPoolId="us-west-2_7m7HHJHGA";
    private String clientId="74mnv97hrarqoi0fmgfr19aip6";
    private String clientSecret="uifiqtaqvibo401hkpvmmlfh8dqfsks9rm3otll4vpn3c0dbn6a";
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
