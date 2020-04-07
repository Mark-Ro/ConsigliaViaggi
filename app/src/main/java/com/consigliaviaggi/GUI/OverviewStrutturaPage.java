package com.consigliaviaggi.GUI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.consigliaviaggi.Controller.OverviewStrutturaController;
import com.consigliaviaggi.Entity.Struttura;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.consigliaviaggi.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class OverviewStrutturaPage extends AppCompatActivity implements OnMapReadyCallback {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private View contentView;
    private ToggleButton toggleButtonMenu,toggleButtonGalleryOverview,toggleButtonRecensioniOverview;
    private ImageView imageViewOverview;
    private TextView textViewNomeStrutturaOverview,textViewVotoOverview,textViewDescrizioneOverview,textViewTitoloPrezzo,textViewPrezzoOverview;

    private OverviewStrutturaController overviewStrutturaController;

    private Struttura struttura;

    private CustomMapView mapView;
    private static final String MAP_VIEW_BUNDLE_KEY = "AIzaSyCO7MGfrOOjQolJ7za04xdwcaEAIagBSv0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_struttura_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        struttura = (Struttura) intent.getSerializableExtra("Struttura");

        navigationView = findViewById(R.id.menuLaterale);
        drawerLayout = findViewById(R.id.drawerLayoutOverview);
        toggleButtonMenu = findViewById(R.id.toggleButtonMenu);
        toggleButtonGalleryOverview = findViewById(R.id.toggleButtonGallery_Overview);
        toggleButtonRecensioniOverview = findViewById(R.id.toggleButtonRecensioni_Overview);
        imageViewOverview = findViewById(R.id.imageViewOverview);
        textViewNomeStrutturaOverview = findViewById(R.id.textViewNomeStrutturaOverview);
        textViewVotoOverview = findViewById(R.id.textViewVotoOverview);
        textViewDescrizioneOverview = findViewById(R.id.textViewDescrizione_Overview);
        textViewTitoloPrezzo = findViewById(R.id.textViewTitoloPrezzo);
        textViewPrezzoOverview = findViewById(R.id.textViewPrezzo_Overview);
        mapView = findViewById(R.id.mapView);

        overviewStrutturaController = new OverviewStrutturaController(this,this);

        toggleButtonMenu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!drawerLayout.isDrawerOpen(GravityCompat.START))
                        drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        toggleButtonGalleryOverview.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleButtonGalleryOverview.setChecked(false);
                    overviewStrutturaController.openGallery(struttura);
                }
            }
        });

        inizializzaViewOverviewStrutturaPage();
        animazioneNavigationDrawer();
        Bundle mapViewBundle = null;
        if (savedInstanceState != null)
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        checkUserLocationPermission();
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
        mapView.setNestedScrollingEnabled(true);
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

    private void inizializzaViewOverviewStrutturaPage() {
        Picasso.get().load(struttura.getFotoStruttura()).noFade().fit().centerCrop().into(imageViewOverview);
        textViewNomeStrutturaOverview.setText(struttura.getNomeStruttura());
        textViewVotoOverview.setText(String.valueOf(struttura.getVoto()));
        textViewDescrizioneOverview.setText(struttura.getDescrizione());
        if (struttura.getPrezzo()>0) {
            textViewTitoloPrezzo.setText("Prezzo per notte");
            textViewPrezzoOverview.setText(String.valueOf(struttura.getPrezzo()) + "€");
        }
    }

    public boolean checkUserLocationPermission() {
        if (ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
            else
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
            return false;
        }
        else
            return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (checkUserLocationPermission())
            googleMap.setMyLocationEnabled(true);
        googleMap.setMinZoomPreference(12);
        LatLng latLng = new LatLng(struttura.getLatitudine(), struttura.getLongitudine());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(struttura.getNomeStruttura());
        markerOptions.position(latLng);
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(OverviewStrutturaPage.this, "Hai cliccato: " + marker.getTitle() + " " + marker.getPosition().toString(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
