package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.consigliaviaggi.DAO.CittaDAO;
import com.consigliaviaggi.DAO.StrutturaDAO;
import com.consigliaviaggi.Entity.Struttura;
import com.consigliaviaggi.GUI.ListaStrutturePage;
import com.consigliaviaggi.GUI.LoadingDialog;
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

    private LoadingDialog loadingDialog;

    private Location miaPosizione;

    private ArrayList<Struttura> listaStrutture;


    public RicercaController(Context contextRicercaPage, Activity activityRicercaPage) {
        this.contextRicercaPage = contextRicercaPage;
        this.activityRicercaPage = activityRicercaPage;
        this.strutturaDAO = new StrutturaDAO(contextRicercaPage);
    }

    public void effettuaRicercaStrutture(final String nomeStruttura, final String citta, final String nazione, final float prezzoMassimo, final float voto, final String tipoStruttura) {

        if (isNetworkAvailable()) {

             new AsyncTask<Void,Void,Void>() {

                 @Override
                 protected Void doInBackground(Void... voids) {
                     listaStrutture = strutturaDAO.getListaStruttureCittaFromDatabase(nomeStruttura, citta, nazione, prezzoMassimo, voto);
                     return null;
                 }

                 @Override
                 protected void onPostExecute(Void aVoid) {
                     super.onPostExecute(aVoid);
                     if (listaStrutture != null) {
                         Log.i("RICERCA_CONTROLLER", "Lista size: " + String.valueOf(listaStrutture.size()));
                         cancelLoadingDialog();
                         Intent intent = new Intent(contextRicercaPage, ListaStrutturePage.class);
                         intent.putExtra("ListaStrutture", listaStrutture);
                         intent.putExtra("Citta", citta);
                         intent.putExtra("TipoStruttura", tipoStruttura);
                         contextRicercaPage.startActivity(intent);
                     } else {
                         cancelLoadingDialog();
                         Log.i("RICERCA_CONTROLLER", "Lista vuota!");
                         Toast.makeText(contextRicercaPage, "Nessun risultato trovato!", Toast.LENGTH_SHORT).show();
                     }
                 }
             }.execute();
        }
        else {
            cancelLoadingDialog();
            Toast.makeText(contextRicercaPage, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
        }
    }

    public void effettuaRicercaStruttureConPosizione(final String nomeStruttura, final float prezzoMassimo, final float voto, final String tipoStruttura) {

        if (isNetworkAvailable()) {
            if (miaPosizione != null) {
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... voids) {

                        listaStrutture = strutturaDAO.getListaStruttureGPSFromDatabase(nomeStruttura, miaPosizione, prezzoMassimo, voto);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        if (listaStrutture != null) {
                            Log.i("RICERCA_CONTROLLER", "Lista size: " + String.valueOf(listaStrutture.size()));
                            cancelLoadingDialog();
                            Intent intent = new Intent(contextRicercaPage, ListaStrutturePage.class);
                            intent.putExtra("ListaStrutture", listaStrutture);
                            intent.putExtra("Citta", listaStrutture.get(0).getCitta().getNome());
                            intent.putExtra("TipoStruttura", tipoStruttura);
                            contextRicercaPage.startActivity(intent);
                        } else {
                            cancelLoadingDialog();
                            Log.i("RICERCA_CONTROLLER", "Lista vuota!");
                            Toast.makeText(contextRicercaPage, "Nessun risultato trovato!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
            } else {
                cancelLoadingDialog();
                Toast.makeText(contextRicercaPage, "Posizione GPS non trovata! Riprovare!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            cancelLoadingDialog();
            Toast.makeText(contextRicercaPage, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
        }
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

    public void openLoadingDialog(Activity activity) {
        loadingDialog = new LoadingDialog(activity);
        loadingDialog.startLoadingDialog();
    }

    public void cancelLoadingDialog() {
        if (loadingDialog!=null)
            loadingDialog.dismissDialog();
    }

    public String[] getArrayStringaCitta() {
        ArrayList<String> listaStringheCitta;
        String[] risultato=null;
        if (isNetworkAvailable()) {

            CittaDAO cittaDAO = new CittaDAO(contextRicercaPage);
            listaStringheCitta = cittaDAO.getArrayCittaFromDatabase();
            risultato = new String[listaStringheCitta.size()];
            for (int i=0;i<listaStringheCitta.size();i++)
                risultato[i] = listaStringheCitta.get(i);
        }
        else
            Toast.makeText(contextRicercaPage, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
        return risultato;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) contextRicercaPage.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

}
