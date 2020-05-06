package com.consigliaviaggi.DAO;

import android.content.Context;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.consigliaviaggi.Cognito.CognitoSettings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CittaDAO {

    private Context context;
    private CognitoSettings cognitoSettings;

    public CittaDAO(Context context) {
        this.context = context;
        cognitoSettings = new CognitoSettings(context);
    }

    public ArrayList<String> getArrayCittaFromDatabase() {
        ArrayList<String> listaStringheCitta;
        String resultQuery = doQueryCitta();
        listaStringheCitta = creazioneListaStringheCitta(resultQuery);
        return listaStringheCitta;
    }

    private String doQueryCitta() {

        String resultQuery;

        CognitoCachingCredentialsProvider cognitoProvider = cognitoSettings.getCredentialsProvider();
        LambdaInvokerFactory lambdaInvokerFactory = new LambdaInvokerFactory(context, Regions.EU_CENTRAL_1, cognitoProvider);
        InterfacciaLambda interfacciaLambda = lambdaInvokerFactory.build(InterfacciaLambda.class);

        RequestDetailsTable request = new RequestDetailsTable();
        request.setTable("citta");
        ResponseDetailsQuery responseDetails = interfacciaLambda.funzioneLambdaQueryCitta(request);
        if (responseDetails != null)
            resultQuery = responseDetails.getResultQuery();
        else
            resultQuery = "Errore query";

        Log.i("CITTA_DAO_QUERY",resultQuery);
        return resultQuery;
    }

    private ArrayList<String> creazioneListaStringheCitta(String query) {
        ArrayList<String> listaStringheCitta=null;
        String nome=null,nazione=null;

        JSONObject jsonQuery = null;

        if (!query.equals("\n}")) {

            listaStringheCitta = new ArrayList<>();

            try {
                jsonQuery = new JSONObject(query);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            boolean flag = false;
            int i = 1;

            while (flag == false) {

                try {
                    nome = (String) jsonQuery.get("Nome" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                try {
                    nazione = (String) jsonQuery.get("Nazione" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                if (flag==false)
                    listaStringheCitta.add(nome + "," + nazione);
                i++;
            }
        }
        return listaStringheCitta;
    }
}
