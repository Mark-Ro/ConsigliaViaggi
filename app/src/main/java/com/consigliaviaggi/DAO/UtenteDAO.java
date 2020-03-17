package com.consigliaviaggi.DAO;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.consigliaviaggi.Entity.Utente;


import org.json.JSONException;
import org.json.JSONObject;

public class UtenteDAO {
    private Utente utente;
    private CognitoSettings cognitoSettings;
    private Context context;

    public UtenteDAO(Context context) {
        this.utente = Utente.getIstance();
        this.context = context;
        this.cognitoSettings = new CognitoSettings(context);
    }

    private CognitoSettings getCognitoSettings() {
        return cognitoSettings;
    }

    public void getInformazioniUtente(String username) {
        utente.setUtenteAutenticato(true);
        Log.i("UTENTE_DAO","Nickname: " + username);
        new getInformazioniUtenteTask().doInBackground(username);
    }

    private class getInformazioniUtenteTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            cognitoSettings = getCognitoSettings();
            CognitoCachingCredentialsProvider cognitoProvider = cognitoSettings.getCredentialsProvider();
            LambdaInvokerFactory lambdaInvokerFactory = new LambdaInvokerFactory(context, Regions.US_WEST_2, cognitoProvider);
            InterfacciaLambda interfacciaLambda = lambdaInvokerFactory.build(InterfacciaLambda.class);
            String query = doQuery(interfacciaLambda, strings[0]);
            Log.i("UTENTE_DAO","Query: " + query);
            if (!query.equals("\n}"))
                setInformazioniUtente(query);
            else {
                utente.setUtenteAutenticato(false);
                utente.setCaricamentoUtente(true);
            }
            return null;
        }
    }


    private String doQuery(InterfacciaLambda interfacciaLambda, String username) {
        String query;
        RequestDetailsUtenteQuery request = new RequestDetailsUtenteQuery();
        request.setNickname(username);
        ResponseDetailsQuery responseDetails = interfacciaLambda.funzioneLambdaQueryUtente(request);
        if (responseDetails != null)
            query = responseDetails.getResultQuery();
        else
            query = "Errore query";
        return query;
    }

    private void setInformazioniUtente(String query) {
        String nickname, email, nome, cognome, stato, media, numeroRecensioni, nomePubblico;
        JSONObject jsonQuery = null;

        try {
            jsonQuery = new JSONObject(query);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        boolean flag = false;
        int i = 1;

        while (flag == false) {
            try {
                nickname = (String) jsonQuery.get("Nickname" + String.valueOf(i));
                utente.setNickname(nickname);
            } catch (JSONException e) {
                flag = true;
            }

            try {
                email = (String) jsonQuery.get("Email" + String.valueOf(i));
                utente.setEmail(email);
            } catch (JSONException e) {
                flag = true;
            }

            try {
                nome = (String) jsonQuery.get("Nome" + String.valueOf(i));
                utente.setNome(nome);
            } catch (JSONException e) {
                Log.e("Errore", "Errore: " + e.getLocalizedMessage());
                flag = true;
            }

            try {
                cognome = (String) jsonQuery.get("Cognome" + String.valueOf(i));
                utente.setCognome(cognome);
            } catch (JSONException e) {
                flag = true;
            }

            try {
                stato = (String) jsonQuery.get("Stato" + String.valueOf(i));
                utente.setStatoAccount(stato);
            } catch (JSONException e) {
                flag = true;
            }

            try {
                media = (String) jsonQuery.get("Media" + String.valueOf(i));
                utente.setMedia(Float.parseFloat(media));
            } catch (JSONException e) {
                flag = true;
            }

            try {
                numeroRecensioni = (String) jsonQuery.get("NumeroRecensioni" + String.valueOf(i));
                utente.setNumeroRecensioni(Integer.parseInt(numeroRecensioni));
            } catch (JSONException e) {
                flag = true;
            }

            try {
                nomePubblico = (String) jsonQuery.get("NomePubblico" + String.valueOf(i));
                utente.setNomePubblico(nomePubblico);
            } catch (JSONException e) {
                flag = true;
            }
            i++;
        }

        utente.setCaricamentoUtente(true);
    }

    public String inserimentoUtente(String nome, String cognome, String email, String nickname, String password, boolean nomePubblico) {

        String inserimento = null, resultMessage = null;

        cognitoSettings = getCognitoSettings();
        CognitoCachingCredentialsProvider cognitoProvider = cognitoSettings.getCredentialsProvider();
        LambdaInvokerFactory lambdaInvokerFactory = new LambdaInvokerFactory(context, Regions.US_WEST_2, cognitoProvider);
        InterfacciaLambda interfacciaLambda = lambdaInvokerFactory.build(InterfacciaLambda.class);

        if (nomePubblico)
            inserimento = "'" + nickname + "'," + "'" + email + "'," + "'" + nome + "'," + "'" + cognome + "'," + "'unbanned','0.0','0','" + nickname + "'";
        else
            inserimento = "'" + nickname + "'," + "'" + email + "'," + "'" + nome + "'," + "'" + cognome + "'," + "'unbanned','0.0','0','" + nome + " " + cognome + "'";

        Log.i("UTENTE_DAO", "Insert: " + inserimento);

        RequestDetailsUtenteInsert request = new RequestDetailsUtenteInsert();
        request.setInserimento(inserimento);
        ResponseDetailsUpdate responseDetails = interfacciaLambda.funzioneLambdaInserimentoUtente(request);
        if (responseDetails != null)
            resultMessage = responseDetails.getMessageReason();
        else
            resultMessage = "Errore insert";

        return resultMessage;
    }
}

