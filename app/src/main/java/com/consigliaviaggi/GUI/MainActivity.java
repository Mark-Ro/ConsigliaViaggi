package com.consigliaviaggi.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.consigliaviaggi.Controller.MainActivityController;
import com.consigliaviaggi.R;

public class MainActivity extends AppCompatActivity {
    private Button bottoneRicerca,bottoneProfilo;
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

        mainActivityController = new MainActivityController(MainActivity.this);
        mainActivityController.inizializzaLambda();

        Log.i("MAIN_ACTIVITY","Username: " + username);
        Log.i("MAIN_ACTIVITY","Logout = " + String.valueOf(logout));
        if (logout==false)
            gestisciSessioneUtente(username);

        bottoneRicerca = (Button) findViewById(R.id.bottoneRicerca);
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
