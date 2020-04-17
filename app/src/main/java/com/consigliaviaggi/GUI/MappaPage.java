package com.consigliaviaggi.GUI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

import com.consigliaviaggi.Controller.MappaController;
import com.consigliaviaggi.Entity.Struttura;
import com.consigliaviaggi.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MappaPage extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    private SearchView searchView;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code = 99;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private View contentView;

    private MappaController mappaController;

    private ArrayList<Struttura> listaStrutture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mappa_page);

        String languageToLoad = "it";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

        searchView = findViewById(R.id.searchView);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        navigationView = findViewById(R.id.menuLaterale);
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView.setNavigationItemSelectedListener(this);

        mappaController = new MappaController(this,this);

        mapFragment.getMapAsync(this);

        animazioneNavigationDrawer();


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            buildGoogleApiClient();
            googleMap.setMyLocationEnabled(true);
        }

        listaStrutture = mappaController.getStrutture();

        for (int i=0; i<listaStrutture.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(listaStrutture.get(i).getLatitudine(),listaStrutture.get(i).getLongitudine()));
            markerOptions.title(listaStrutture.get(i).getNomeStruttura());
            if (listaStrutture.get(i).getTipoStruttura().equals("hotel"))
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_hotel));
            else if (listaStrutture.get(i).getTipoStruttura().equals("ristorante"))
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_ristorante));
            else
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_altro));
            currentUserLocationMarker = googleMap.addMarker(markerOptions);

            addSuggestion(mappaController.getSuggerimenti(),searchView);
        }

        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng latLng = new LatLng(marker.getPosition().latitude,marker.getPosition().longitude);
                mappaController.apriDialogStruttura(mappaController.trovaStruttura(latLng.latitude,latLng.longitude));
                return false;
            }
        });
    }

    protected synchronized void buildGoogleApiClient (){
        googleApiClient=new GoogleApiClient.Builder( this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        googleApiClient.connect();
    }

    public void addSuggestion(final List<String> suggestions, final SearchView searchView) {

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        final CursorAdapter suggestionAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                new String[]{SearchManager.SUGGEST_COLUMN_TEXT_1},
                new int[]{android.R.id.text1},
                0);

        searchView.setSuggestionsAdapter(suggestionAdapter);

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }
            public boolean onSuggestionClick(int position) {
                Cursor cursor= searchView.getSuggestionsAdapter().getCursor();
                cursor.moveToPosition(position);
                String suggestion =cursor.getString(2);
                searchView.setQuery(suggestion,true);
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mappaController.ricercaStruttureMappa(searchView.getQuery().toString(),googleMap);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                String[] columns = { BaseColumns._ID,
                        SearchManager.SUGGEST_COLUMN_TEXT_1,
                        SearchManager.SUGGEST_COLUMN_INTENT_DATA,
                };
                MatrixCursor cursor = new MatrixCursor(columns);
                for (int i = 0; i < suggestions.size(); i++) {
                    if(suggestions.get(i).toLowerCase().contains(searchView.getQuery().toString().toLowerCase())) {
                        String[] tmp = {Integer.toString(i), suggestions.get(i), suggestions.get(i)};
                        cursor.addRow(tmp);
                    }
                }
                suggestionAdapter.swapCursor(cursor);
                return true;
            }
        });
    }

    private void  animazioneNavigationDrawer(){
        final float END_SCALE = 0.7f;
        contentView  = findViewById(R.id.coordinator_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public  void onDrawerOpened(View drawerView){

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
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home: {
                mappaController.openHomePage();
                break;
            }
            case R.id.profilo: {
                mappaController.openProfiloPage();
                break;
            }
            case R.id.mappa: {
                mappaController.openMappaPage();
                break;
            }
        }
        return false;
    }
}
