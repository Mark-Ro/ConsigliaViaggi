package com.consigliaviaggi.GUI;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.consigliaviaggi.Controller.GalleryStrutturaController;
import com.consigliaviaggi.Entity.Gallery;
import com.consigliaviaggi.Entity.Struttura;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.consigliaviaggi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GalleryStrutturaPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private View contentView;
    private ToggleButton toggleButtonMenu,toggleButtonOverview,toggleButtonRecensioni;
    private ImageView imageViewGallery;
    private TextView textViewNomeStrutturaGallery,textViewVotoGallery;

    private ListView listViewGallery;
    private GalleryStrutturaController galleryStrutturaController;

    private Struttura struttura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_struttura_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        struttura = (Struttura) intent.getSerializableExtra("Struttura");

        navigationView = findViewById(R.id.menuLaterale);
        drawerLayout = findViewById(R.id.drawerLayout);
        toggleButtonMenu = findViewById(R.id.toggleButtonMenu);
        toggleButtonOverview = findViewById(R.id.toggleButtonOverview);
        toggleButtonRecensioni = findViewById(R.id.toggleButtonRecensioni);
        imageViewGallery = findViewById(R.id.imageViewGallery);
        textViewNomeStrutturaGallery = findViewById(R.id.textViewNomeStrutturaGallery);
        textViewVotoGallery = findViewById(R.id.textViewVotoGallery);
        listViewGallery = findViewById(R.id.listViewGallery);
        navigationView.setNavigationItemSelectedListener(this);

        galleryStrutturaController = new GalleryStrutturaController(this,this);

        final ArrayList<Gallery> listaGallery = galleryStrutturaController.getGalleryStruttura(struttura);
        CustomAdapterGalleryPage customAdapterGalleryPage = new CustomAdapterGalleryPage(this,listaGallery);
        listViewGallery.setAdapter(customAdapterGalleryPage);

        toggleButtonMenu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!drawerLayout.isDrawerOpen(GravityCompat.START))
                        drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        toggleButtonOverview.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleButtonOverview.setChecked(false);
                    galleryStrutturaController.openOverview(struttura);
                }
            }
        });

        listViewGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                galleryStrutturaController.zoomImageDialog(listaGallery.get(position).getImmagine());
            }
        });

        toggleButtonRecensioni.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleButtonRecensioni.setChecked(false);
                    galleryStrutturaController.openRecensioni(struttura);
                }
            }
        });

        listViewGallery.setNestedScrollingEnabled(true);
        inizalizzaGalleryStrutturaPage();
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

    private void inizalizzaGalleryStrutturaPage() {
        Picasso.get().load(struttura.getFotoStruttura()).noFade().fit().centerCrop().into(imageViewGallery);
        textViewNomeStrutturaGallery.setText(struttura.getNomeStruttura());
        textViewVotoGallery.setText(String.valueOf(struttura.getVoto()).substring(0,3));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home: {
                galleryStrutturaController.openHomePage();
                break;
            }
            case R.id.profilo: {
                galleryStrutturaController.openProfiloPage();
                break;
            }
            case R.id.mappa: {
                galleryStrutturaController.openMappaPage();
                break;
            }
        }
        return false;
    }
}
