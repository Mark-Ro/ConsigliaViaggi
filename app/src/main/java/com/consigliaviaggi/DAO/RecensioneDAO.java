package com.consigliaviaggi.DAO;

import android.content.Context;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.consigliaviaggi.Entity.Recensione;

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
        String testo=null,voto=null,nomeUtente=null;

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
                    listaRecensioni.add(new Recensione(testo,nomeUtente,Integer.parseInt(voto)));
                i++;
            }
        }
        return listaRecensioni;
    }
}
