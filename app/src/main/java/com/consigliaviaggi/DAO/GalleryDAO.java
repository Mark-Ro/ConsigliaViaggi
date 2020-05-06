package com.consigliaviaggi.DAO;

import android.content.Context;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.consigliaviaggi.Cognito.CognitoSettings;
import com.consigliaviaggi.Entity.Gallery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GalleryDAO {

    private Context context;
    private CognitoSettings cognitoSettings;

    public GalleryDAO(Context context) {
        this.context = context;
        cognitoSettings = new CognitoSettings(context);
    }

    public ArrayList<Gallery> getGalleryStrutturaFromDatabase(int idStruttura) {
        ArrayList<Gallery> listaGallery;
        String resultQuery = doQueryGallery(idStruttura);
        listaGallery = creazioneListaGallery(resultQuery);
        return listaGallery;
    }

    private String doQueryGallery(int idStruttura) {

        String resultQuery;

        CognitoCachingCredentialsProvider cognitoProvider = cognitoSettings.getCredentialsProvider();
        LambdaInvokerFactory lambdaInvokerFactory = new LambdaInvokerFactory(context, Regions.EU_CENTRAL_1, cognitoProvider);
        InterfacciaLambda interfacciaLambda = lambdaInvokerFactory.build(InterfacciaLambda.class);

        RequestDetailsTable request = new RequestDetailsTable();
        request.setTable(String.valueOf(idStruttura));
        ResponseDetailsQuery responseDetails = interfacciaLambda.funzioneLambdaQueryGallery(request);
        if (responseDetails != null)
            resultQuery = responseDetails.getResultQuery();
        else
            resultQuery = "Errore query";

        Log.i("GALLERY_DAO_QUERY",resultQuery);
        return resultQuery;
    }

    private ArrayList<Gallery> creazioneListaGallery(String query) {
        ArrayList<Gallery> listaGallery = new ArrayList<>();
        String idGallery=null,immagine=null;

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
                    idGallery = (String) jsonQuery.get("IdGallery" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                try {
                    immagine = (String) jsonQuery.get("Immagine" + String.valueOf(i));
                } catch (JSONException e) {
                    flag = true;
                }

                if (flag==false)
                    listaGallery.add(new Gallery(Integer.valueOf(idGallery),immagine));
                i++;
            }
        }
        return listaGallery;
    }
}
