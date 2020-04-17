package com.consigliaviaggi.GUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.consigliaviaggi.Controller.LoginController;
import com.consigliaviaggi.R;
import com.google.android.material.navigation.NavigationView;

public class LoginPage extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

    private EditText editTextUsername,editTextPassword;
    private Button bottoneAccedi,bottoneRecuperaPassword,bottoneRegistrati,bottoneHome,bottoneMappa;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private View contentView;
    private ToggleButton toggleButtonMenu;
    private LoginController loginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        Intent intent = getIntent();

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        bottoneAccedi = findViewById(R.id.bottoneAccedi);
        bottoneRecuperaPassword = findViewById(R.id.bottoneRecuperaPassword);
        bottoneRegistrati = findViewById(R.id.bottoneRegistrati);
        bottoneHome = findViewById(R.id.bottoneHome);
        bottoneMappa = findViewById(R.id.bottoneMappa);
        toggleButtonMenu = findViewById(R.id.toggleButtonMenu);
        navigationView = findViewById(R.id.menuLaterale);
        drawerLayout = findViewById(R.id.drawerLayout);
        loginController = new LoginController(LoginPage.this);
        navigationView.setNavigationItemSelectedListener(this);

        bottoneAccedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextUsername.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty())
                    Toast.makeText(LoginPage.this, "Riempire i campi!", Toast.LENGTH_SHORT).show();
                else {
                    loginController = new LoginController(LoginPage.this, editTextUsername.getText().toString(), editTextPassword.getText().toString());
                    loginController.openLoadingDialog(LoginPage.this);
                    loginController.effettuaLogin();
                }
            }
        });

        bottoneHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginController.openHomePage();
            }
        });

        bottoneRegistrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginController.openRegistrazionePage();
            }
        });

        bottoneRecuperaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginController.openRecuperaPasswordPage();
            }
        });
        bottoneMappa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginController.openMappaPage();
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
                loginController.openHomePage();
                break;
            }
            case R.id.mappa: {
                loginController.openMappaPage();
                break;
            }
        }
        return false;
    }
}
