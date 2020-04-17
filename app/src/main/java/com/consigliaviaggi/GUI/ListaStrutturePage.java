package com.consigliaviaggi.GUI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.consigliaviaggi.Controller.ListaStrutturePageController;
import com.consigliaviaggi.Entity.Struttura;
import com.google.android.gms.maps.MapView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.consigliaviaggi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListaStrutturePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listView;
    private TextView textViewNomeCitta;
    private ArrayList<Struttura> listaStrutture;
    private ImageView imageViewCitta,imageViewBarraCitta;
    private AppBarLayout appBarLayout;
    private String nomeCitta,tipoStruttura;
    private Button buttonLenteRicerca;
    private ToggleButton toggleButtonMenu;
    private ToggleButton toggleButtonHotel,toggleButtonRistorante,toggleButtonAltro;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private View contentView;
    private MapView mapView;

    private ListaStrutturePageController listaStrutturePageController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_strutture_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        listaStrutture = (ArrayList<Struttura>) intent.getSerializableExtra("ListaStrutture");
        nomeCitta = intent.getStringExtra("Citta");
        tipoStruttura = intent.getStringExtra("TipoStruttura");

        listView = findViewById(R.id.listView);
        textViewNomeCitta = findViewById(R.id.textViewNomeCitta);
        imageViewCitta = findViewById(R.id.imageViewCitta);
        imageViewBarraCitta = findViewById(R.id.imageViewBarraCitta);
        appBarLayout=findViewById(R.id.app_bar);
        toggleButtonMenu = findViewById(R.id.toggleButtonMenu);
        navigationView = findViewById(R.id.menuLaterale);
        toggleButtonHotel = findViewById(R.id.toggleButtonHotel);
        toggleButtonRistorante = findViewById(R.id.toggleButtonRistorante);
        toggleButtonAltro = findViewById(R.id.toggleButtonAltro);
        drawerLayout = findViewById(R.id.drawerlayout);
        buttonLenteRicerca = findViewById(R.id.buttonLenteRicerca);

        navigationView.setNavigationItemSelectedListener(this);
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        final AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();


        listaStrutturePageController = new ListaStrutturePageController(ListaStrutturePage.this,listaStrutture);

        toggleButtonHotel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @SuppressLint("WrongConstant")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ArrayList<Struttura> listaHotel;
                if (isChecked){
                    toggleButtonRistorante.setChecked(false);
                    toggleButtonAltro.setChecked(false);
                    listView.setAdapter(null);
                    appBarLayout.setExpanded(true);
                    listaHotel = listaStrutturePageController.selezionaStruttureHotel();
                    Log.i("TOGGLE_BUTTON_HOTEL","Lista hotel size: " + String.valueOf(listaHotel.size()));
                    CustomAdapterListaStrutture customAdapterListaHotel = new CustomAdapterListaStrutture(ListaStrutturePage.this,listaHotel);
                    listView.setAdapter(customAdapterListaHotel);
                    listView.setNestedScrollingEnabled(true);
                    if (listaHotel.isEmpty() || listaHotel.size() == 1)
                        params.setScrollFlags(0);
                    else
                        params.setScrollFlags(3);
                }
            }
        });

        toggleButtonRistorante.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @SuppressLint("WrongConstant")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ArrayList<Struttura> listaRistoranti;
                if (isChecked){
                    toggleButtonHotel.setChecked(false);
                    toggleButtonAltro.setChecked(false);
                    listView.setAdapter(null);
                    appBarLayout.setExpanded(true);
                    listaRistoranti = listaStrutturePageController.selezionaStruttureRistoranti();
                    CustomAdapterListaStrutture customAdapterListaRistoranti = new CustomAdapterListaStrutture(ListaStrutturePage.this,listaRistoranti);
                    listView.setAdapter(customAdapterListaRistoranti);
                    listView.setNestedScrollingEnabled(true);
                    if (listaRistoranti.isEmpty() || listaRistoranti.size() == 1)
                        params.setScrollFlags(0);
                    else
                        params.setScrollFlags(3);
                }
            }
        });

        toggleButtonAltro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @SuppressLint("WrongConstant")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ArrayList<Struttura> listaAltro;
                if (isChecked){
                    toggleButtonHotel.setChecked(false);
                    toggleButtonRistorante.setChecked(false);
                    listView.setAdapter(null);
                    appBarLayout.setExpanded(true);
                    listaAltro = listaStrutturePageController.selezionaStruttureAltro();
                    CustomAdapterListaStrutture customAdapterListaAltro = new CustomAdapterListaStrutture(ListaStrutturePage.this,listaAltro);
                    listView.setAdapter(customAdapterListaAltro);
                    listView.setNestedScrollingEnabled(true);
                    if (listaAltro.isEmpty() || listaAltro.size() == 1)
                        params.setScrollFlags(0);
                    else
                        params.setScrollFlags(3);
                }
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

        buttonLenteRicerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaStrutturePageController.openRicercaPage();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listaStrutturePageController.clickStruttura(position);
            }
        });

        setCitta();
        inizializzaBottoni();
        animazioneNavigationDrawer();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home: {
                listaStrutturePageController.openHomePage();
                break;
            }
            case R.id.profilo: {
                listaStrutturePageController.openProfiloPage();
                break;
            }
            case R.id.mappa: {
                listaStrutturePageController.openMappaPage();
                break;
            }
        }
        return  false;
    }

    private void setCitta() {
        Log.i("LISTA_STRUTTURA_PAGE",nomeCitta);
        if (!nomeCitta.equals("null")) {
            textViewNomeCitta.setText(listaStrutture.get(0).getCitta().getNome());
            Picasso.get().load(listaStrutture.get(0).getCitta().getFotoCitta()).noFade().fit().centerCrop().into(imageViewCitta);
        }
        else {
            Picasso.get().load(R.drawable.background_citta).noFade().fit().centerCrop(Gravity.BOTTOM).into(imageViewCitta);
            imageViewBarraCitta.setAlpha(0f);
            textViewNomeCitta.setText("");
        }
    }

    private void inizializzaBottoni() {
        if (tipoStruttura.equals("Hotel"))
            toggleButtonHotel.performClick();
        else if (tipoStruttura.equals("Ristorante"))
            toggleButtonRistorante.performClick();
        else
            toggleButtonAltro.performClick();
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
}
