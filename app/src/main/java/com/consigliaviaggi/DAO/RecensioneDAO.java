package com.consigliaviaggi.DAO;

import android.content.Context;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.consigliaviaggi.Cognito.CognitoSettings;
import com.consigliaviaggi.Entity.Recensione;
import com.consigliaviaggi.Entity.Utente;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecensioneDAO {

    private Context context;
    private CognitoSettings cognitoSettings;

    public RecensioneDAO(Context context) {
        this.context = context;
        cognitoSettings = new CognitoSettings(context);
    }

    public ArrayList<Recensione> getRecensioniStrutturaFromDatabase(int idStruttura) {
        ArrayList<Recensione> listaRecensioni;
        String resultQuery = doQueryRecensioni(idStruttura);
        listaRecensioni = creazioneListaRecensioni(resultQuery);
        return listaRecensioni;
    }

    private String doQueryRecensioni(int idStruttura) {

        String resultQuery;

        CognitoCachingCredentialsProvider cognitoProvider = cognitoSettings.getCredentialsProvider();
        LambdaInvokerFactory lambdaInvokerFactory = new LambdaInvokerFactory(context, Regions.EU_CENTRAL_1, cognitoProvider);
        InterfacciaLambda interfacciaLambda = lambdaInvokerFactory.build(InterfacciaLambda.class);

        RequestDetailsTable request = new RequestDetailsTable();
        request.setTable(String.valueOf(idStruttura));
        ResponseDetailsQuery responseDetails = interfacciaLambda.funzioneLambdaQueryRecensioni(request);
        if (responseDetails != null)
            resultQuery = responseDetails.getResultQuery();
        else
            resultQuery = "Errore query";

        Log.i("RECENSIONI_DAO_QUERY",resultQuery);
        return resultQuery;
    }

    private ArrayList<Recensione> creazioneListaRecensioni(String query) {
        ArrayList<Recensione> listaRecensioni = new ArrayList<>();
        String idRecensione=null,testo=null,voto=null,nomeUtente=null;

        JSONObject jsonQuery = null;

        if (!query.equals("\n}")) {

            try {
                jsonQuery = new JSONObject(query);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            boolean flag = false;
            int i = 1;

            while (flag == false) {

                try {
                    idRecensione = (String) jsonQuery.get("IdRecensione" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                try {
                    testo = (String) jsonQuery.get("Testo" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                try {
                    voto = (String) jsonQuery.get("Voto" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                try {
                    nomeUtente = (String) jsonQuery.get("NomeUtente" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                if (flag==false)
                    listaRecensioni.add(new Recensione(Integer.parseInt(idRecensione), testo, Integer.parseInt(voto),nomeUtente));
                i++;
            }
        }
        return listaRecensioni;
    }

    public ArrayList<Recensione> getMieRecensioniFromDatabase(String nickname) {
        ArrayList<Recensione> listaMieRecensioni;
        String resultQuery = doQueryMieRecensioni(nickname);
        listaMieRecensioni = creazioneListaMieRecensioni(resultQuery);
        return listaMieRecensioni;
    }

    private String doQueryMieRecensioni(String nickname) {

        String resultQuery;

        CognitoCachingCredentialsProvider cognitoProvider = cognitoSettings.getCredentialsProvider();
        LambdaInvokerFactory lambdaInvokerFactory = new LambdaInvokerFactory(context, Regions.EU_CENTRAL_1, cognitoProvider);
        InterfacciaLambda interfacciaLambda = lambdaInvokerFactory.build(InterfacciaLambda.class);

        RequestDetailsTable request = new RequestDetailsTable();
        request.setTable(nickname);
        ResponseDetailsQuery responseDetails = interfacciaLambda.funzioneLambdaQueryMieRecensioni(request);
        if (responseDetails != null)
            resultQuery = responseDetails.getResultQuery();
        else
            resultQuery = "Errore query";

        Log.i("RECENSIONI_DAO_QUERY",resultQuery);
        return resultQuery;
    }

    private ArrayList<Recensione> creazioneListaMieRecensioni(String query) {
        ArrayList<Recensione> listaRecensioni = new ArrayList<>();
        String idRecensione=null,testo=null,voto=null,nomeStruttura=null;

        JSONObject jsonQuery = null;

        if (!query.equals("\n}")) {

            try {
                jsonQuery = new JSONObject(query);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            boolean flag = false;
            int i = 1;

            while (flag == false) {

                try {
                    idRecensione = (String) jsonQuery.get("IdRecensione" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                try {
                    testo = (String) jsonQuery.get("Testo" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                try {
                    voto = (String) jsonQuery.get("Voto" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                try {
                    nomeStruttura = (String) jsonQuery.get("NomeStruttura" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                if (flag==false)
                    listaRecensioni.add(new Recensione(Integer.parseInt(idRecensione),testo,nomeStruttura,Integer.parseInt(voto)));
                i++;
            }
        }
        return listaRecensioni;
    }

    public String inserimentoRecensione(int idStruttura, String testo, int voto) {
        String resultMessage = null;

        CognitoCachingCredentialsProvider cognitoProvider = cognitoSettings.getCredentialsProvider();
        LambdaInvokerFactory lambdaInvokerFactory = new LambdaInvokerFactory(context, Regions.EU_CENTRAL_1, cognitoProvider);
        InterfacciaLambda interfacciaLambda = lambdaInvokerFactory.build(InterfacciaLambda.class);

        Utente utente = Utente.getIstance();
        RequestDetailsRecensione request = new RequestDetailsRecensione();
        request.setTesto(testo);
        request.setIdStruttura(String.valueOf(idStruttura));
        request.setVoto(String.valueOf(voto));
        request.setNomeUtente(utente.getNomePubblico());
        request.setIdUtente(utente.getNickname());
        ResponseDetailsUpdate responseDetails = interfacciaLambda.funzioneLambdaInsertRecensione(request);
        if (responseDetails != null)
            resultMessage = responseDetails.getMessageReason();
        else
            resultMessage = "Errore insert";

        Log.i("RECENSIONE_DAO",resultMessage);

        return resultMessage;
    }

    public String updateRecensioneFromDatabase(int idRecensione, String testo, int voto) {
        String resultMessage = null;

        CognitoCachingCredentialsProvider cognitoProvider = cognitoSettings.getCredentialsProvider();
        LambdaInvokerFactory lambdaInvokerFactory = new LambdaInvokerFactory(context, Regions.EU_CENTRAL_1, cognitoProvider);
        InterfacciaLambda interfacciaLambda = lambdaInvokerFactory.build(InterfacciaLambda.class);

        Utente utente = Utente.getIstance();
        RequestDetailsUpdateRecensione request = new RequestDetailsUpdateRecensione();
        request.setIdRecensione(String.valueOf(idRecensione));
        request.setTesto(testo);
        request.setVoto(String.valueOf(voto));
        ResponseDetailsUpdate responseDetails = interfacciaLambda.funzioneLambdaUpdateRecensione(request);
        if (responseDetails != null)
            resultMessage = responseDetails.getMessageReason();
        else
            resultMessage = "Errore update";

        Log.i("RECENSIONE_DAO",resultMessage);

        return resultMessage;
    }

    public String deleteRecensioneFromDatabase(int idRecensione) {
        String resultMessage = null;

        CognitoCachingCredentialsProvider cognitoProvider = cognitoSettings.getCredentialsProvider();
        LambdaInvokerFactory lambdaInvokerFactory = new LambdaInvokerFactory(context, Regions.EU_CENTRAL_1, cognitoProvider);
        InterfacciaLambda interfacciaLambda = lambdaInvokerFactory.build(InterfacciaLambda.class);

        RequestDetailsTable request = new RequestDetailsTable();
        request.setTable(String.valueOf(idRecensione));
        ResponseDetailsUpdate responseDetails = interfacciaLambda.funzioneLambdaDeleteRecensione(request);
        if (responseDetails != null)
            resultMessage = responseDetails.getMessageReason();
        else
            resultMessage = "Errore update";

        if (resultMessage!=null)
            Log.i("RECENSIONE_DAO",resultMessage);

        return resultMessage;
    }
}
