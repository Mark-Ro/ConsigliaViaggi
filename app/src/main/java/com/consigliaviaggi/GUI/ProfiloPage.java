package com.consigliaviaggi.GUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.consigliaviaggi.Controller.ProfiloController;
import com.consigliaviaggi.R;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.internal.$Gson$Preconditions;

public class ProfiloPage extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

    private TextView textViewNomeProfilo,textViewCognomeProfilo,textViewEmailProfilo,textViewNicknameProfilo,textViewNumeroRecensioniProfilo;
    private Button bottoneHome,bottoneLogout,bottoneCambiaPassword,bottoneCambiaEmail,bottoneMieRecensioni,bottoneMappa;
    private ProfiloController profiloController;
    private ToggleButton toggleButtonMenu;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo_page);

        Intent intent = getIntent();

        textViewNomeProfilo = findViewById(R.id.textViewNomeProfilo);
        textViewCognomeProfilo =  findViewById(R.id.textViewCognomeProfilo);
        textViewEmailProfilo = findViewById(R.id.textViewEmailProfilo);
        textViewNicknameProfilo = findViewById(R.id.textViewNicknameProfilo);
        textViewNumeroRecensioniProfilo = findViewById(R.id.textViewNumeroRecensioniProfilo);
        bottoneHome = findViewById(R.id.bottoneHome);
        bottoneLogout = findViewById(R.id.bottoneLogout);
        bottoneCambiaPassword = findViewById(R.id.bottoneCambiaPassword);
        bottoneCambiaEmail = findViewById(R.id.bottoneCambiaEmail);
        bottoneMieRecensioni = findViewById(R.id.bottoneMieRecensioni);
        bottoneMappa=findViewById(R.id.bottoneMappa);
        toggleButtonMenu = findViewById(R.id.toggleButtonMenu);
        navigationView = findViewById(R.id.menuLaterale);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView.setNavigationItemSelectedListener(this);

        profiloController = new ProfiloController(ProfiloPage.this,ProfiloPage.this);
        setTextViews();

        profiloController.updateTextViewsProfiloPage(ProfiloPage.this);

        bottoneHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profiloController.openHomePage();
            }
        });

        bottoneLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profiloController.logout();
            }
        });

        bottoneCambiaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profiloController.openCambiaPasswordPage();
            }
        });

        bottoneCambiaEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profiloController.openCambiaEmailPage();
            }
        });

        bottoneMieRecensioni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profiloController.openLoadingDialog(ProfiloPage.this);
                profiloController.openMieRecensioniPage();
            }
        });
        bottoneMappa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profiloController.openMappaPage();
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
        animazioneNavigationDrawer();
    }

    public void setTextViews () {
        profiloController.setTextViewsProfiloPage(textViewNomeProfilo,textViewCognomeProfilo,textViewEmailProfilo,textViewNicknameProfilo,textViewNumeroRecensioniProfilo,bottoneLogout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTextViews();
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
            case R.id.home: {
                profiloController.openHomePage();
                break;
            }
            case R.id.mappa: {
                profiloController.openMappaPage();
                break;
            }
        }
        return false;
    }
}
