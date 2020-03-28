package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.consigliaviaggi.DAO.StrutturaDAO;
import com.consigliaviaggi.Entity.Struttura;
import com.consigliaviaggi.GUI.ListaStrutturePage;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class RicercaController {

    private Context contextRicercaPage;
    private Activity activityRicercaPage;
    private StrutturaDAO strutturaDAO;

    private Location miaPosizione;


    public RicercaController(Context contextRicercaPage, Activity activityRicercaPage) {
        this.contextRicercaPage = contextRicercaPage;
        this.activityRicercaPage = activityRicercaPage;
        this.strutturaDAO = new StrutturaDAO(contextRicercaPage);
    }

    public void effettuaRicercaStrutture(String nomeStruttura, String citta, float prezzoMassimo, float voto, String tipoStruttura) {
        if (isNetworkAvailable()) {
            ArrayList<Struttura> listaStrutture = strutturaDAO.getListaStruttureCittaFromDatabase(nomeStruttura, citta, prezzoMassimo, voto);
            if (listaStrutture != null) {
                Log.i("RICERCA_CONTROLLER", "Lista size: " + String.valueOf(listaStrutture.size()));
                Intent intent = new Intent(contextRicercaPage, ListaStrutturePage.class);
                intent.putExtra("ListaStrutture", listaStrutture);
                intent.putExtra("Citta", citta);
                intent.putExtra("TipoStruttura", tipoStruttura);
                contextRicercaPage.startActivity(intent);
            } else {
                Log.i("RICERCA_CONTROLLER", "Lista vuota!");
                Toast.makeText(contextRicercaPage, "Nessun risultato trovato!", Toast.LENGTH_SHORT).show();
            }
        }
        else
            Toast.makeText(contextRicercaPage, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    public void effettuaRicercaStruttureConPosizione(String nomeStruttura, float prezzoMassimo, float voto, String tipoStruttura) {
        if (isNetworkAvailable()) {
            if (miaPosizione!=null) {
                ArrayList<Struttura> listaStrutture = strutturaDAO.getListaStruttureGPSFromDatabase(nomeStruttura, miaPosizione, prezzoMassimo, voto);
                if (listaStrutture != null) {
                    Log.i("RICERCA_CONTROLLER", "Lista size: " + String.valueOf(listaStrutture.size()));
                    Intent intent = new Intent(contextRicercaPage, ListaStrutturePage.class);
                    intent.putExtra("ListaStrutture", listaStrutture);
                    intent.putExtra("Citta", listaStrutture.get(0).getCitta().getNome());
                    intent.putExtra("TipoStruttura", tipoStruttura);
                    contextRicercaPage.startActivity(intent);
                } else {
                    Log.i("RICERCA_CONTROLLER", "Lista vuota!");
                    Toast.makeText(contextRicercaPage, "Nessun risultato trovato!", Toast.LENGTH_SHORT).show();
                }
            }
            else
                Toast.makeText(contextRicercaPage, "Posizione GPS non trovata! Riprovare!", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(contextRicercaPage, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    public void getCurrentLocation() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(contextRicercaPage).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(contextRicercaPage).removeLocationUpdates(this);
                if (locationResult!=null && locationResult.getLocations().size() > 0) {
                    int latestLocationsIndex = locationResult.getLocations().size() - 1;
                    double latitudine = locationResult.getLocations().get(latestLocationsIndex).getLatitude();
                    double longitudine = locationResult.getLocations().get(latestLocationsIndex).getLongitude();

                    miaPosizione = new Location("dummyprovider");
                    miaPosizione.setLatitude(latitudine);
                    miaPosizione.setLongitude(longitudine);

                }
            }
        }, Looper.getMainLooper());
    }

    public boolean verificaCondizioniGPS() {      //Permessi e GPS abilitato
        boolean risultato=false;
        LocationManager locationManager = (LocationManager) contextRicercaPage.getSystemService( Context.LOCATION_SERVICE );;
        if (ContextCompat.checkSelfPermission(contextRicercaPage, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activityRicercaPage, new String[]{ACCESS_FINE_LOCATION}, 1);
            if (ContextCompat.checkSelfPermission(contextRicercaPage, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                risultato=true;
        }
        else if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            Toast.makeText(contextRicercaPage, "Devi abilitare il GPS!", Toast.LENGTH_SHORT).show();
        else
            risultato=true;
        return risultato;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) contextRicercaPage.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

}
