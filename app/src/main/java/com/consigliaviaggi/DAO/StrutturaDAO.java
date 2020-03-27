package com.consigliaviaggi.DAO;

import android.content.Context;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.consigliaviaggi.Entity.Citta;
import com.consigliaviaggi.Entity.Struttura;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StrutturaDAO {

    private Context context;
    private CognitoSettings cognitoSettings;

    public StrutturaDAO(Context context) {
        this.context = context;
        cognitoSettings = new CognitoSettings(context);
    }

    private String doQueryStruttureCitta(String nomeStruttura, String citta, float prezzoMassimo, float voto) {
        String resultQuery;

        CognitoCachingCredentialsProvider cognitoProvider = cognitoSettings.getCredentialsProvider();
        LambdaInvokerFactory lambdaInvokerFactory = new LambdaInvokerFactory(context, Regions.US_WEST_2, cognitoProvider);
        InterfacciaLambda interfacciaLambda = lambdaInvokerFactory.build(InterfacciaLambda.class);

        RequestDetailsStrutturaCitta request = new RequestDetailsStrutturaCitta();
        request.setNomeStruttura(nomeStruttura);
        request.setCitta(citta);
        request.setMassimoPrezzo(String.valueOf(prezzoMassimo));
        request.setVoto(String.valueOf(voto));
        ResponseDetailsQuery responseDetails = interfacciaLambda.funzioneLambdaQueryStrutturaCitta(request);
        if (responseDetails != null)
            resultQuery = responseDetails.getResultQuery();
        else
            resultQuery = "Errore query";

        Log.i("STRUTTURA_DAO_QUERY",resultQuery);
        return resultQuery;
    }

    private ArrayList<Struttura> creazioneListaStruttureCittaFromQuery(String query) {
        ArrayList<Struttura> listaStrutture=null;
        Citta citta=null;
        String idStruttura=null,nome=null,prezzo=null,descrizione=null,latitudine=null,longitudine=null,tipoStruttura=null,voto=null,fotoStruttura=null,numeroRecensioni=null,idCitta=null,nomeCitta=null,fotoCitta=null;
        JSONObject jsonQuery = null;

        if (!query.equals("\n}")) {

            listaStrutture = new ArrayList<>();

            try {
                jsonQuery = new JSONObject(query);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            boolean flag = false;
            int i = 1;

            while (flag == false) {

                try {
                    idStruttura = (String) jsonQuery.get("IdStruttura" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                try {
                    nome = (String) jsonQuery.get("Nome" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                try {
                    prezzo = (String) jsonQuery.get("Prezzo" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                try {
                    descrizione = (String) jsonQuery.get("Descrizione" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                try {
                    latitudine = (String) jsonQuery.get("Latitudine" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                try {
                    longitudine = (String) jsonQuery.get("Longitudine" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                try {
                    tipoStruttura = (String) jsonQuery.get("Struttura" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                try {
                    voto = (String) jsonQuery.get("Voto" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                try {
                    fotoStruttura = (String) jsonQuery.get("FotoStruttura" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                try {
                    numeroRecensioni = (String) jsonQuery.get("NumeroRecensioni" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                try {
                    idCitta = (String) jsonQuery.get("IdCitta" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                try {
                    nomeCitta = (String) jsonQuery.get("NomeCitta" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                try {
                    fotoCitta = (String) jsonQuery.get("ImmagineCitta" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                if (i == 1)
                    citta = new Citta(Integer.parseInt(idCitta), nomeCitta, fotoCitta);
                if (flag==false) {

                    listaStrutture.add(new Struttura(Integer.parseInt(idStruttura), nome, Float.parseFloat(prezzo), descrizione, Double.parseDouble(latitudine), Double.parseDouble(longitudine), tipoStruttura, Float.parseFloat(voto), fotoStruttura, Integer.parseInt(numeroRecensioni), citta, -1));
                    Log.i("STRUTTURA_DAO","Nome: " + listaStrutture.get(i-1).getNomeStruttura());
                }
                i++;
            }
        }
        return listaStrutture;
    }

    public ArrayList<Struttura> getListaStruttureCittaFromDatabase(String nomeStruttura, String citta, float prezzoMassimo, float voto) {

        String resultQuery = doQueryStruttureCitta(nomeStruttura,citta,prezzoMassimo,voto);
        ArrayList<Struttura> listaStrutture = creazioneListaStruttureCittaFromQuery(resultQuery);
        return listaStrutture;
    }



}
