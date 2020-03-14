package com.consigliaviaggi.DAO;

import android.os.StrictMode;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.consigliaviaggi.Controller.LoginController;
import com.consigliaviaggi.Entity.Utente;

import org.json.JSONException;
import org.json.JSONObject;

public class UtenteDAO {
    private Utente utente;
    private CognitoSettings cognitoSettings;
    private LoginController loginController;

    public UtenteDAO(LoginController loginController) {
        this.utente = Utente.getIstance();
        this.loginController = loginController;
        this.cognitoSettings = new CognitoSettings(loginController.getContextLoginPage());
    }

    public CognitoSettings getCognitoSettings() {
        return cognitoSettings;
    }

    public void getInformazioniUtente(String username) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        cognitoSettings = getCognitoSettings();
        CognitoCachingCredentialsProvider cognitoProvider = cognitoSettings.getCredentialsProvider();
        LambdaInvokerFactory lambdaInvokerFactory = new LambdaInvokerFactory(loginController.getContextLoginPage(), Regions.US_WEST_2, cognitoProvider);
        InterfacciaLambda interfacciaLambda = lambdaInvokerFactory.build(InterfacciaLambda.class);
        String query = doQuery(interfacciaLambda,username);
        setInformazioniUtente(query);
    }

    private String doQuery(InterfacciaLambda interfacciaLambda, String username) {
        String query;
        RequestDetailsUtente request = new RequestDetailsUtente();
        request.setNickname(username);
        ResponseDetails responseDetails = interfacciaLambda.funzioneLambdaQueryUtente(request);
        if (responseDetails!=null)
            query = responseDetails.getResultQuery();
        else
            query="Errore query";
        return query;
    }

    private void setInformazioniUtente(String query) {
        String nickname,email,nome,cognome,stato,media,numeroRecensioni,nomePubblico;
        JSONObject jsonQuery= null;

        utente.setUtenteAutenticato(true);

        try {
            jsonQuery = new JSONObject(query);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        boolean flag=false;
        int i=1;

        while (flag==false) {
            try {
                nickname = (String) jsonQuery.get("Nickname" + String.valueOf(i));
                utente.setNickname(nickname);
            } catch (JSONException e) {
                flag=true;
            }

            try {
                email = (String) jsonQuery.get("Email" + String.valueOf(i));
                utente.setEmail(email);
            } catch (JSONException e) {
                flag=true;
            }

            try {
                nome = (String) jsonQuery.get("Nome" + String.valueOf(i));
                utente.setNome(nome);
            } catch (JSONException e) {
                Log.e("Errore","Errore: " + e.getLocalizedMessage());
                flag=true;
            }

            try {
                cognome = (String) jsonQuery.get("Cognome" + String.valueOf(i));
                utente.setCognome(cognome);
            } catch (JSONException e) {
                flag=true;
            }

            try {
                stato = (String) jsonQuery.get("Stato" + String.valueOf(i));
                utente.setStatoAccount(stato);
            } catch (JSONException e) {
                flag=true;
            }

            try {
                media = (String) jsonQuery.get("Media" + String.valueOf(i));
                utente.setMedia(Float.parseFloat(media));
            } catch (JSONException e) {
                flag=true;
            }

            try {
                numeroRecensioni = (String) jsonQuery.get("NumeroRecensioni" + String.valueOf(i));
                utente.setNumeroRecensioni(Integer.parseInt(numeroRecensioni));
            } catch (JSONException e) {
                flag=true;
            }

            try {
                nomePubblico = (String) jsonQuery.get("NomePubblico" + String.valueOf(i));
                utente.setNomePubblico(nomePubblico);
            } catch (JSONException e) {
                flag=true;
            }
            i++;
        }
    }
}
