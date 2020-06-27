package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.consigliaviaggi.DAO.CittaDAO;
import com.consigliaviaggi.DAO.StrutturaDAO;
import com.consigliaviaggi.Entity.Struttura;
import com.consigliaviaggi.Entity.Utente;
import com.consigliaviaggi.GUI.LoginPage;
import com.consigliaviaggi.GUI.MainActivity;
import com.consigliaviaggi.GUI.MappaPage;
import com.consigliaviaggi.GUI.OverviewStrutturaPage;
import com.consigliaviaggi.GUI.ProfiloPage;
import com.consigliaviaggi.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MappaController {
    private Context contextMappaPage;

    private ArrayList<Struttura> listaStrutture;
    private ArrayList<Marker> listaOldLocations;
    private Activity activityMappaPage;

    public MappaController(Activity activityMappaPage,Context contextMappaPage) {
        this.activityMappaPage = activityMappaPage;
        this.contextMappaPage = contextMappaPage;
        this.listaOldLocations = new ArrayList<>();
    }

    public ArrayList<Struttura> getStrutture() {
        if (isNetworkAvailable()) {
            StrutturaDAO strutturaDAO = new StrutturaDAO(contextMappaPage);
            listaStrutture = strutturaDAO.getListaStruttureMappaFromDatabase();
        }
        else
            Toast.makeText(contextMappaPage, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
        return listaStrutture;
    }

    public ArrayList<String> getSuggerimenti() {
        int i=0;
        ArrayList<String> risultatoSuggerimenti = new ArrayList<>();
        ArrayList<String> listaCitta;
        CittaDAO cittaDAO = new CittaDAO(contextMappaPage);
        listaCitta=cittaDAO.getArrayCittaFromDatabase();
        for (i=0;i<listaCitta.size();i++) {
            if (!risultatoSuggerimenti.contains(listaCitta.get(i)))
                risultatoSuggerimenti.add(listaCitta.get(i));
        }
        for (i=0; i<listaStrutture.size(); i++) {
            if (!risultatoSuggerimenti.contains(listaStrutture.get(i).getNomeStruttura()))
                risultatoSuggerimenti.add(listaStrutture.get(i).getNomeStruttura());
        }
        return  risultatoSuggerimenti;
    }


    public void ricercaStruttureMappa(String inputRicerca, GoogleMap googleMap) {
        boolean ricercaEffettuataConSuccesso;
        if (isNetworkAvailable()) {
            if (inputRicerca==null || inputRicerca.isEmpty())
                Toast.makeText(contextMappaPage, "Ricerca vuota!", Toast.LENGTH_SHORT).show();
            else {
                ricercaEffettuataConSuccesso = ricercaLocazione(inputRicerca,googleMap);
                if (ricercaEffettuataConSuccesso == false)
                    Toast.makeText(contextMappaPage, "Nessun risultato trovato!", Toast.LENGTH_SHORT).show();
            }
        }
        else
            Toast.makeText(contextMappaPage, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    private boolean ricercaLocazione(String inputRicerca, GoogleMap googleMap) {
        boolean ricercaEffettuataConSuccesso;
        inputRicerca = inputRicerca.toLowerCase();
        ricercaEffettuataConSuccesso = ricercaStrutturaNelDatabase(inputRicerca, googleMap);
        if (ricercaEffettuataConSuccesso == false)
            ricercaEffettuataConSuccesso = ricercaStrutturaInGoogle(inputRicerca,googleMap);
        return ricercaEffettuataConSuccesso;
    }

    private boolean ricercaStrutturaInGoogle(String inputRicerca, GoogleMap googleMap) {
        boolean risultato=false;
        List<Address> addressList = null;
        Geocoder geocoder = new Geocoder(contextMappaPage);

        removeOldPositions();
        try {
            addressList = geocoder.getFromLocationName(inputRicerca, 1);
        } catch (IOException e) {
            Toast.makeText(contextMappaPage, "Errore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if (addressList!=null && !addressList.isEmpty()) {
            risultato=true;
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(inputRicerca);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            listaOldLocations.add(googleMap.addMarker(markerOptions));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        }
        return risultato;
    }

    private boolean ricercaStrutturaNelDatabase(String inputRicerca, GoogleMap googleMap) {
        boolean risultato=false;
        removeOldPositions();
        if (listaStrutture!=null) {
            for (int i=0;i<listaStrutture.size();i++) {
                if (listaStrutture.get(i).getNomeStruttura().toLowerCase().equals(inputRicerca.toLowerCase())) {
                    risultato=true;
                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng latLng = new LatLng(listaStrutture.get(i).getLatitudine(), listaStrutture.get(i).getLongitudine());
                    markerOptions.position(latLng);
                    markerOptions.title(inputRicerca);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    listaOldLocations.add(googleMap.addMarker(markerOptions));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(listaStrutture.get(i).getLatitudine(),listaStrutture.get(i).getLongitudine()), 20f));
                }
            }
        }
        return risultato;
    }

    public void removeOldPositions() {
        for (int i=0;i<listaOldLocations.size();i++)
            listaOldLocations.get(i).remove();
        listaOldLocations.clear();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) contextMappaPage.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public Struttura trovaStruttura(double latitudine, double longitudine) {
        Struttura risultato=null;
        for (int i = 0; i < listaStrutture.size(); i++) {
            if (listaStrutture.get(i).getLatitudine() == latitudine && listaStrutture.get(i).getLongitudine() == longitudine) {
                risultato=listaStrutture.get(i);
            }
        }
        return risultato;
    }

    public void apriDialogStruttura(final Struttura struttura) {
        Dialog dialogStrutturaMappa;
        dialogStrutturaMappa = new Dialog(contextMappaPage);
        dialogStrutturaMappa.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogStrutturaMappa.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogStrutturaMappa.setContentView(R.layout.dialog_struttura_mappa);
        ImageView imageView = dialogStrutturaMappa.findViewById(R.id.imageView);
        ImageView imageViewStelle = dialogStrutturaMappa.findViewById(R.id.imageViewStelle);
        TextView textViewNomeStrutturaMappa = dialogStrutturaMappa.findViewById(R.id.textViewNomeStrutturaMappa);
        Button buttonStrutturaMappa = dialogStrutturaMappa.findViewById(R.id.buttonStrutturaMappa);
        Picasso.get().load(struttura.getFotoStruttura()).noFade().fit().centerCrop().into(imageView);
        textViewNomeStrutturaMappa.setText(struttura.getNomeStruttura());
        buttonStrutturaMappa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activityMappaPage, OverviewStrutturaPage.class);
                intent.putExtra("Struttura", struttura);
                activityMappaPage.startActivity(intent);
            }
        });
        switch (arrotondaValutazione(struttura.getVoto())) {

            case "1":
                imageViewStelle.setImageResource(R.drawable.ic_1stelle);
                break;
            case "1.5":
                imageViewStelle.setImageResource(R.drawable.ic_1_2stelle);
                break;
            case "2":
                imageViewStelle.setImageResource(R.drawable.ic_2stelle);
                break;
            case "2.5":
                imageViewStelle.setImageResource(R.drawable.ic_2_2stelle);
                break;
            case "3":
                imageViewStelle.setImageResource(R.drawable.ic_3stelle);
                break;
            case "3.5":
                imageViewStelle.setImageResource(R.drawable.ic_3_2stelle);
                break;
            case "4":
                imageViewStelle.setImageResource(R.drawable.ic_4stelle);
                break;
            case "4.5":
                imageViewStelle.setImageResource(R.drawable.ic_4_2stelle);
                break;
            case "5":
                imageViewStelle.setImageResource(R.drawable.ic_5stelle);
                break;
        }
        dialogStrutturaMappa.show();
    }

    private String arrotondaValutazione(float valutazione) {

        if (valutazione < 0)
            throw new IllegalArgumentException("Valutazione deve essere >0");
        if (valutazione > 5)
            throw new IllegalArgumentException("Valutazione deve essere <5");

        int primaCifraDecimale= Integer.parseInt(String.valueOf(valutazione).substring(2,3));

        if (primaCifraDecimale < 5)
            return String.valueOf(Math.floor(valutazione));
        if (primaCifraDecimale > 5)
            return  String.valueOf(Math.ceil(valutazione));
        return String.valueOf(valutazione).substring(0,3);
    }

    public void openHomePage() {
        Log.i("LISTA_STRUTTURE_PAGE_CONTROLLER","Entrato in openHomePage");
        Intent intent = new Intent(contextMappaPage.getApplicationContext(), MainActivity.class);
        contextMappaPage.startActivity(intent);
    }

    public void openProfiloPage() {
        Log.i("LISTA_STRUTTURE_PAGE_CONTROLLER","Entrato in profilo");
        Intent intent;
        Utente utente = Utente.getIstance();
        if (utente.isUtenteAutenticato())
            intent = new Intent(contextMappaPage, ProfiloPage.class);
        else
            intent = new Intent(contextMappaPage, LoginPage.class);

        contextMappaPage.startActivity(intent);
    }
}
