package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.consigliaviaggi.Cognito.CognitoSettings;
import com.consigliaviaggi.DAO.InterfacciaLambda;
import com.consigliaviaggi.Cognito.LoginCognito;
import com.consigliaviaggi.DAO.RequestDetailsUtenteQuery;
import com.consigliaviaggi.DAO.ResponseDetailsQuery;
import com.consigliaviaggi.DAO.StrutturaDAO;
import com.consigliaviaggi.DAO.UtenteDAO;
import com.consigliaviaggi.Entity.Struttura;
import com.consigliaviaggi.Entity.Utente;
import com.consigliaviaggi.GUI.ListaStrutturePage;
import com.consigliaviaggi.GUI.LoadingDialog;
import com.consigliaviaggi.GUI.LoginPage;
import com.consigliaviaggi.GUI.MappaPage;
import com.consigliaviaggi.GUI.ProfiloPage;
import com.consigliaviaggi.GUI.RicercaPage;
import com.consigliaviaggi.GUI.VerificationCodePage;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.MODE_PRIVATE;

public class MainActivityController {

    private Activity activityMainActivity;
    private Context contextMainActivity;
    private Utente utente;
    private UtenteDAO utenteDAO;

    private Location miaPosizione;

    private LoadingDialog loadingDialog;

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String usernameSalvato = "username";

    public MainActivityController(Activity activityMainActivity, Context contextMainActivity) {
        this.activityMainActivity = activityMainActivity;
        this.contextMainActivity = contextMainActivity;
        this.utente = Utente.getIstance();
        this.utenteDAO = new UtenteDAO(contextMainActivity,MainActivityController.this);
    }

    public MainActivityController(Context contextMainActivity) {
        this.contextMainActivity = contextMainActivity;
        this.utente = Utente.getIstance();
        this.utenteDAO = new UtenteDAO(contextMainActivity,MainActivityController.this);
    }

    public Location getMiaPosizione() {
        return miaPosizione;
    }

    public void openProfiloPage(){

        Intent intent;

        if (isNetworkAvailable()) {
            if (utente.isUtenteAutenticato()) {
                intent = new Intent(contextMainActivity, ProfiloPage.class);
                Log.i("MAIN_ACTIVITY", "AUTENTICATO: TRUE");
            } else {
                intent = new Intent(contextMainActivity, LoginPage.class);
                Log.i("MAIN_ACTIVITY", "AUTENTICATO: FALSE");
            }
            contextMainActivity.startActivity(intent);
        }
        else
            Toast.makeText(contextMainActivity, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    public void openRicercaPage() {

        if (isNetworkAvailable()) {
            Intent intent = new Intent(contextMainActivity, RicercaPage.class);
            contextMainActivity.startActivity(intent);
        }
        else
            Toast.makeText(contextMainActivity, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    public void openMappaPage() {
        if (isNetworkAvailable()) {
            Intent intent = new Intent(contextMainActivity, MappaPage.class);
            contextMainActivity.startActivity(intent);
        }
        else
            Toast.makeText(contextMainActivity, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    public void saveUsername(String username) {
        SharedPreferences sharedPreferences = contextMainActivity.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(usernameSalvato, username);
        editor.apply();
        Log.i("SALVATAGGIO","Username " + username + " salvato!");
    }

    public void loadUsername() {
        SharedPreferences sharedPreferences = contextMainActivity.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String username = sharedPreferences.getString(usernameSalvato, null);
        Log.i("LOAD_USERNAME","Username: " + username);
        utente = Utente.getIstance();
        if (!utente.isUtenteAutenticato() && username!=null) {
            LoginCognito loginCognito = new LoginCognito(contextMainActivity);
            if (loginCognito.isUserLoggable(username,"token")) {
                utenteDAO.verificaEmailStatus(username);
                loadInformazioniUtente(username);
            }
        }
        else
            inizializzaLambda();
    }

    public void loadInformazioniUtente(String username) {
        if (isNetworkAvailable()) {
            ThreadGetInformazioniUtente threadGetInformazioniUtente = new ThreadGetInformazioniUtente(username);
            threadGetInformazioniUtente.start();
        }
        else
            Toast.makeText(contextMainActivity, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    private class ThreadGetInformazioniUtente extends Thread {

        private String username;

        public ThreadGetInformazioniUtente(String username) {
            this.username = username;
        }

        @Override
        public void run() {
            super.run();
            utenteDAO.getInformazioniUtente(username);
        }
    }

    public void inizializzaLambda() {
        if (isNetworkAvailable()) {
            if (utente.getNome() == null || utente.getNome().equals("")) {
                ThreadInizializzaLambda threadInizializzaLambda = new ThreadInizializzaLambda();
                threadInizializzaLambda.start();
            }
        }
        else
            Toast.makeText(contextMainActivity, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    private class ThreadInizializzaLambda extends Thread {
        @Override
        public void run() {
            super.run();
            doQuery("null");
        }
    }

    private void doQuery(String inizializza) {
        CognitoSettings cognitoSettings = new CognitoSettings(contextMainActivity);
        CognitoCachingCredentialsProvider cognitoProvider = cognitoSettings.getCredentialsProvider();
        LambdaInvokerFactory lambdaInvokerFactory = new LambdaInvokerFactory(contextMainActivity, Regions.EU_CENTRAL_1, cognitoProvider);
        InterfacciaLambda interfacciaLambda = lambdaInvokerFactory.build(InterfacciaLambda.class);
        RequestDetailsUtenteQuery request = new RequestDetailsUtenteQuery();
        request.setNickname(inizializza);
        ResponseDetailsQuery responseDetails = interfacciaLambda.funzioneLambdaQueryUtente(request);
        Log.i("MAIN_ACTIVITY",responseDetails.getResultQuery());
    }

    public void verificaEmailNonVerificata() {
        SharedPreferences sharedPreferences = contextMainActivity.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String username = sharedPreferences.getString(usernameSalvato, null);
        Intent intent = new Intent(contextMainActivity, VerificationCodePage.class);
        intent.putExtra("Username",username);
        intent.putExtra("Password","token");
        intent.putExtra("ActivityChiamante","CambiaEmail");
        contextMainActivity.startActivity(intent);
    }

    public void effettuaRicercaStruttureConPosizione(final String nomeStruttura, final float prezzoMassimo, final float voto, final String tipoStruttura) {

        if (isNetworkAvailable()) {
            if (miaPosizione != null) {
                new AsyncTask<Void, Void, Void>() {

                    private ArrayList<Struttura> listaStrutture;

                    @Override
                    protected Void doInBackground(Void... voids) {
                        StrutturaDAO strutturaDAO = new StrutturaDAO(contextMainActivity);
                        listaStrutture = strutturaDAO.getListaStruttureGPSFromDatabase(nomeStruttura, miaPosizione, prezzoMassimo, voto);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        if (!listaStrutture.isEmpty()) {
                            Log.i("RICERCA_CONTROLLER", "Lista size: " + String.valueOf(listaStrutture.size()));
                            cancelLoadingDialog();
                            Intent intent = new Intent(contextMainActivity, ListaStrutturePage.class);
                            intent.putExtra("ListaStrutture", listaStrutture);
                            intent.putExtra("Citta", listaStrutture.get(0).getCitta().getNome());
                            intent.putExtra("TipoStruttura", tipoStruttura);
                            contextMainActivity.startActivity(intent);
                        } else {
                            cancelLoadingDialog();
                            Log.i("RICERCA_CONTROLLER", "Lista vuota!");
                            Toast.makeText(contextMainActivity, "Nessun risultato trovato!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
            } else {
                cancelLoadingDialog();
                Toast.makeText(contextMainActivity, "Posizione GPS non trovata! Riprovare!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            cancelLoadingDialog();
            Toast.makeText(contextMainActivity, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getCurrentLocation() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(contextMainActivity).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(contextMainActivity).removeLocationUpdates(this);
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

    public int verificaCondizioniGPS() {     //Permessi e GPS abilitato
        int risultato = -1;
        LocationManager locationManager = (LocationManager) contextMainActivity.getSystemService( Context.LOCATION_SERVICE );;
        if (ContextCompat.checkSelfPermission(contextMainActivity, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activityMainActivity, new String[]{ACCESS_FINE_LOCATION}, 1);
            if (ContextCompat.checkSelfPermission(contextMainActivity, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                risultato = 1;
        }
        else if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            risultato = 0;
            Log.i("MAIN_ACTIVITY_CONTROLLER", "GPS DISABILITATO");
        }
        else
            risultato = 1;
        return risultato;
    }

    public void openStruttureVicine(String tipoStruttura) {
        if (verificaCondizioniGPS() == 1) {
            openLoadingDialog(activityMainActivity);
            getCurrentLocation();
            effettuaRicercaStruttureConPosizione("null",3000,0,tipoStruttura);
        }
        else if (verificaCondizioniGPS() == 0)
            Toast.makeText(activityMainActivity, "Devi abilitare il GPS!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(activityMainActivity, "Sono necessari i permessi di geolocalizzazione!", Toast.LENGTH_SHORT).show();
    }


    public void openLoadingDialog(Activity activity) {
        loadingDialog = new LoadingDialog(activity);
        loadingDialog.startLoadingDialog();
    }

    public void cancelLoadingDialog() {
        if (loadingDialog!=null)
            loadingDialog.dismissDialog();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) contextMainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
