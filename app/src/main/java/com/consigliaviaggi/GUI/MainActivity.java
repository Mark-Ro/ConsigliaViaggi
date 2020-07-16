package com.consigliaviaggi.GUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.consigliaviaggi.Controller.MainActivityController;
import com.consigliaviaggi.Controller.RicercaController;
import com.consigliaviaggi.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    private Button bottoneRicerca,bottoneProfilo,bottoneHotel,bottoneRistoranti,bottoneAltro,bottoneMappa;
    private MainActivityController mainActivityController;
    private ToggleButton toggleButtonMenu;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private View contentView;

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

        mainActivityController = new MainActivityController(this,MainActivity.this,MainActivity.this);


        Log.i("MAIN_ACTIVITY","Username: " + username);
        Log.i("MAIN_ACTIVITY","Logout = " + String.valueOf(logout));
        if (logout==false)
            gestisciSessioneUtente(username);

        bottoneRicerca = findViewById(R.id.bottoneRicerca);
        bottoneHotel = findViewById(R.id.bottoneHotel);
        bottoneRistoranti = findViewById(R.id.bottoneRistoranti);
        bottoneAltro = findViewById(R.id.bottoneAltro);
        bottoneMappa = findViewById(R.id.bottoneMappa);
        toggleButtonMenu = findViewById(R.id.toggleButtonMenu);
        navigationView = findViewById(R.id.menuLaterale);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView.setNavigationItemSelectedListener(this);

        bottoneRistoranti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottoneRistoranti.setEnabled(false);
                bottoneHotel.setEnabled(false);
                bottoneAltro.setEnabled(false);
                mainActivityController.openStruttureVicine("Ristorante");
            }
        });

        bottoneHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottoneRistoranti.setEnabled(false);
                bottoneHotel.setEnabled(false);
                bottoneAltro.setEnabled(false);
                mainActivityController.openStruttureVicine("Hotel");
            }
        });

        bottoneAltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottoneRistoranti.setEnabled(false);
                bottoneHotel.setEnabled(false);
                bottoneAltro.setEnabled(false);
                mainActivityController.openStruttureVicine("Altro");
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

        bottoneMappa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivityController.openMappaPage();
            }
        });

        toggleButtonMenu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!drawerLayout.isDrawerOpen(GravityCompat.START))
                        drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        new AsyncTask<Void,Void,Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                if (mainActivityController.verificaCondizioniGPS() == 1)
                    mainActivityController.getCurrentLocation();
                return null;
            }
        }.execute();
        animazioneNavigationDrawer();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        new AsyncTask<Void,Void,Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                if (mainActivityController.verificaCondizioniGPS() == 1)
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
    private void  animazioneNavigationDrawer(){
        final float END_SCALE = 0.7f;
        contentView  = findViewById(R.id.coordinator_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public  void onDrawerOpened(View drawerView){
                toggleButtonMenu.setChecked(true);
            }
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                toggleButtonMenu.setChecked(false);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profilo: {
                mainActivityController.openProfiloPage();
                break;
            }
            case R.id.mappa: {
                mainActivityController.openMappaPage();
                break;
            }
        }
        return false;
    }

    public void resetGuiButtons() {
        bottoneRistoranti.setEnabled(true);
        bottoneHotel.setEnabled(true);
        bottoneAltro.setEnabled(true);
    }
}
