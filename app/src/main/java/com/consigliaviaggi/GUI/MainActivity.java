package com.consigliaviaggi.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.consigliaviaggi.Controller.MainActivityController;
import com.consigliaviaggi.Controller.RicercaController;
import com.consigliaviaggi.R;

public class MainActivity extends AppCompatActivity {
    private Button bottoneRicerca,bottoneProfilo,bottoneHotel,bottoneRistoranti,bottoneAltro;
    private MainActivityController mainActivityController;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
        boolean logout = intent.getBooleanExtra("Logout",false);

        mainActivityController = new MainActivityController(MainActivity.this,MainActivity.this);


        Log.i("MAIN_ACTIVITY","Username: " + username);
        Log.i("MAIN_ACTIVITY","Logout = " + String.valueOf(logout));
        if (logout==false)
            gestisciSessioneUtente(username);

        bottoneRicerca = findViewById(R.id.bottoneRicerca);
        bottoneHotel = findViewById(R.id.bottoneHotel);
        bottoneRistoranti = findViewById(R.id.bottoneRistoranti);
        bottoneAltro = findViewById(R.id.bottoneAltro);

        bottoneRistoranti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivityController.verificaCondizioniGPS()) {
                    mainActivityController.openLoadingDialog(MainActivity.this);
                    mainActivityController.getCurrentLocation();
                    mainActivityController.effettuaRicercaStruttureConPosizione("null",3000,0,"Ristorante");
                }
            }
        });

        bottoneHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivityController.verificaCondizioniGPS()) {
                    mainActivityController.openLoadingDialog(MainActivity.this);
                    mainActivityController.getCurrentLocation();
                    mainActivityController.effettuaRicercaStruttureConPosizione("null",300,0,"Hotel");
                }
            }
        });

        bottoneAltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivityController.verificaCondizioniGPS()) {
                    mainActivityController.openLoadingDialog(MainActivity.this);
                    mainActivityController.getCurrentLocation();
                    mainActivityController.effettuaRicercaStruttureConPosizione("null",300,0,"Altro");
                }
            }
        });

        bottoneRicerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivityController.openRicercaPage();
            }
        });

        bottoneProfilo = findViewById(R.id.bottoneProfilo);
        bottoneProfilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivityController.openProfiloPage();
            }
        });

        new AsyncTask<Void,Void,Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                if (mainActivityController.verificaCondizioniGPS())
                    mainActivityController.getCurrentLocation();
                return null;
            }
        }.execute();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        new AsyncTask<Void,Void,Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                if (mainActivityController.verificaCondizioniGPS())
                    mainActivityController.getCurrentLocation();
                return null;
            }
        }.execute();
    }

    private void gestisciSessioneUtente(String username) {
        if (username!=null) {
            mainActivityController.saveUsername(username);
            mainActivityController.loadInformazioniUtente(username);
        }
        else
            mainActivityController.loadUsername();
    }

}
