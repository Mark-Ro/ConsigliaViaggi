package com.consigliaviaggi.DAO;

import android.content.Context;
import android.location.Location;
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

    private String doQueryStruttureCitta(String nomeStruttura, String citta, String nazione, float prezzoMassimo, float voto) {
        String resultQuery;

        CognitoCachingCredentialsProvider cognitoProvider = cognitoSettings.getCredentialsProvider();
        LambdaInvokerFactory lambdaInvokerFactory = new LambdaInvokerFactory(context, Regions.EU_CENTRAL_1, cognitoProvider);
        InterfacciaLambda interfacciaLambda = lambdaInvokerFactory.build(InterfacciaLambda.class);

        RequestDetailsStrutturaCitta request = new RequestDetailsStrutturaCitta();
        request.setNomeStruttura(nomeStruttura);
        request.setCitta(citta);
        request.setNazione(nazione);
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

    private String doQueryStruttureMappa() {
        String resultQuery;

        CognitoCachingCredentialsProvider cognitoProvider = cognitoSettings.getCredentialsProvider();
        LambdaInvokerFactory lambdaInvokerFactory = new LambdaInvokerFactory(context, Regions.EU_CENTRAL_1, cognitoProvider);
        InterfacciaLambda interfacciaLambda = lambdaInvokerFactory.build(InterfacciaLambda.class);

        RequestDetailsTable request = new RequestDetailsTable();
        request.setTable("strutture");

        ResponseDetailsQuery responseDetails = interfacciaLambda.funzioneLambdaQueryStruttura(request);

        if (responseDetails != null)
            resultQuery = responseDetails.getResultQuery();
        else
            resultQuery = "Errore query";

        Log.i("STRUTTURA_DAO_QUERY",resultQuery);
        return resultQuery;
    }

    private String doQueryStruttureGPS(String nomeStruttura, Location miaPosizione, float prezzoMassimo, float voto) {
        String resultQuery;

        CognitoCachingCredentialsProvider cognitoProvider = cognitoSettings.getCredentialsProvider();
        LambdaInvokerFactory lambdaInvokerFactory = new LambdaInvokerFactory(context, Regions.EU_CENTRAL_1, cognitoProvider);
        InterfacciaLambda interfacciaLambda = lambdaInvokerFactory.build(InterfacciaLambda.class);

        RequestDetailsStrutturaGPS request = new RequestDetailsStrutturaGPS();
        request.setNomeStruttura(nomeStruttura);
        request.setLatitudine(String.valueOf(miaPosizione.getLatitude()));
        request.setLongitudine(String.valueOf(miaPosizione.getLongitude()));
        request.setMassimoPrezzo(String.valueOf(prezzoMassimo));
        request.setVoto(String.valueOf(voto));

        ResponseDetailsQuery responseDetails = interfacciaLambda.funzioneLambdaQueryStrutturaGPS(request);

        if (responseDetails != null)
            resultQuery = responseDetails.getResultQuery();
        else
            resultQuery = "Errore query";

        Log.i("STRUTTURA_DAO_QUERY",resultQuery);
        return resultQuery;
    }

    private ArrayList<Struttura> creazioneListaStruttureMappaFromQuery(String query) {
        ArrayList<Struttura> listaStrutture = new ArrayList<>();
        Citta citta=null;
        String idStruttura=null,nome=null,prezzo=null,descrizione=null,latitudine=null,longitudine=null,tipoStruttura=null,voto=null,fotoStruttura=null,numeroRecensioni=null,indirizzo=null;
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
                    indirizzo = (String) jsonQuery.get("Indirizzo" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                if (flag==false) {

                    listaStrutture.add(new Struttura(Integer.parseInt(idStruttura), nome, Float.parseFloat(prezzo), descrizione, Double.parseDouble(latitudine), Double.parseDouble(longitudine), tipoStruttura, Float.parseFloat(voto), fotoStruttura, Integer.parseInt(numeroRecensioni), null, -1, indirizzo));
                    Log.i("STRUTTURA_DAO","Nome: " + listaStrutture.get(i-1).getNomeStruttura());
                }
                i++;
            }
        }
        return listaStrutture;
    }

    private ArrayList<Struttura> creazioneListaStruttureCittaFromQuery(String query) {
        ArrayList<Struttura> listaStrutture = new ArrayList<>();
        Citta citta=null;
        String idStruttura=null,nome=null,prezzo=null,descrizione=null,latitudine=null,longitudine=null,tipoStruttura=null,voto=null,fotoStruttura=null,numeroRecensioni=null,idCitta=null,nomeCitta=null,fotoCitta=null,nazione=null,indirizzo=null;
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

                try {
                    nazione = (String) jsonQuery.get("Nazione" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                try {
                    indirizzo = (String) jsonQuery.get("Indirizzo" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                if (i == 1)
                    citta = new Citta(Integer.parseInt(idCitta), nomeCitta, fotoCitta,nazione);
                if (flag==false) {

                    listaStrutture.add(new Struttura(Integer.parseInt(idStruttura), nome, Float.parseFloat(prezzo), descrizione, Double.parseDouble(latitudine), Double.parseDouble(longitudine), tipoStruttura, Float.parseFloat(voto), fotoStruttura, Integer.parseInt(numeroRecensioni), citta, -1,indirizzo));
                    Log.i("STRUTTURA_DAO","Nome: " + listaStrutture.get(i-1).getNomeStruttura());
                }
                i++;
            }
        }
        return listaStrutture;
    }

    private ArrayList<Struttura> creazioneListaStruttureGPSFromQuery(String query, Location miaPosizione) {
        ArrayList<Struttura> listaStrutture = new ArrayList<>();
        Citta citta=null;
        String idStruttura=null,nome=null,prezzo=null,descrizione=null,latitudine=null,longitudine=null,tipoStruttura=null,voto=null,fotoStruttura=null,numeroRecensioni=null,idCitta=null,nomeCitta=null,fotoCitta=null,nazione=null,indirizzo=null;
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

                try {
                    nazione = (String) jsonQuery.get("Nazione" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                try {
                    indirizzo = (String) jsonQuery.get("Indirizzo" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                if (i == 1)
                    citta = new Citta(Integer.parseInt(idCitta), nomeCitta, fotoCitta,nazione);
                if (flag==false) {
                    Location locationStruttura = new Location("dummyprovider");
                    locationStruttura.setLatitude(Double.parseDouble(latitudine));
                    locationStruttura.setLongitude(Double.parseDouble(longitudine));
                    double distanza = miaPosizione.distanceTo(locationStruttura);
                    if (distanza<=500)
                        listaStrutture.add(new Struttura(Integer.parseInt(idStruttura), nome, Float.parseFloat(prezzo), descrizione, Double.parseDouble(latitudine), Double.parseDouble(longitudine), tipoStruttura, Float.parseFloat(voto), fotoStruttura, Integer.parseInt(numeroRecensioni), citta, Math.round(distanza),indirizzo));
                    Log.i("STRUTTURA_DAO","Nome: " + nome + ", distanza: " + distanza);
                }
                i++;
            }
        }
        return listaStrutture;
    }

    public ArrayList<Struttura> getListaStruttureMappaFromDatabase() {
        String resultQuery = doQueryStruttureMappa();
        ArrayList<Struttura> listaStrutture = creazioneListaStruttureMappaFromQuery(resultQuery);
        return listaStrutture;
    }

    public ArrayList<Struttura> getListaStruttureCittaFromDatabase(String nomeStruttura, String citta, String nazione, float prezzoMassimo, float voto) {

        String resultQuery = doQueryStruttureCitta(nomeStruttura,citta,nazione,prezzoMassimo,voto);
        ArrayList<Struttura> listaStrutture = creazioneListaStruttureCittaFromQuery(resultQuery);
        return listaStrutture;
    }

    public ArrayList<Struttura> getListaStruttureGPSFromDatabase(String nomeStruttura, Location miaPosizione, float prezzoMassimo, float voto) {
        String resultQuery = doQueryStruttureGPS(nomeStruttura,miaPosizione,prezzoMassimo,voto);
        ArrayList<Struttura> listaStrutture = creazioneListaStruttureGPSFromQuery(resultQuery,miaPosizione);
        return listaStrutture;
    }

}
