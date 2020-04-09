package com.consigliaviaggi.GUI;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.consigliaviaggi.Controller.RecensioniStrutturaController;
import com.consigliaviaggi.Entity.Recensione;
import com.consigliaviaggi.Entity.Struttura;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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

public class RecensioniStrutturaPage extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private View contentView;
    private ImageView imageViewRecensioni;
    private TextView textViewNomeStrutturaRecensioni,textViewVotoRecensioni;
    private ToggleButton toggleButtonMenu,toggleButtonOverview,toggleButtonGallery;

    private Struttura struttura;

    private ListView listViewRecensioni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recensioni_struttura_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        struttura = (Struttura) intent.getSerializableExtra("Struttura");

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        final AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();

        navigationView = findViewById(R.id.menuLaterale);
        drawerLayout = findViewById(R.id.drawerLayout);
        toggleButtonMenu = findViewById(R.id.toggleButtonMenu);
        toggleButtonOverview = findViewById(R.id.toggleButtonOverview);
        toggleButtonGallery = findViewById(R.id.toggleButtonGallery);
        listViewRecensioni = findViewById(R.id.listViewRecensioni);
        imageViewRecensioni = findViewById(R.id.imageViewRecensioni);
        textViewNomeStrutturaRecensioni = findViewById(R.id.textViewNomeStrutturaRecensioni);
        textViewVotoRecensioni = findViewById(R.id.textViewVotoRecensioni);

        final RecensioniStrutturaController recensioniStrutturaController = new RecensioniStrutturaController(this,this);
        final ArrayList<Recensione> listaRecensioni = recensioniStrutturaController.getListaRecensioni(struttura);

        CustomAdapterRecensioniPage customAdapterRecensioniPage = new CustomAdapterRecensioniPage(this,listaRecensioni);
        listViewRecensioni.setAdapter(customAdapterRecensioniPage);
        listViewRecensioni.setNestedScrollingEnabled(true);
        if (listaRecensioni.isEmpty() || listaRecensioni.size() == 1)
            params.setScrollFlags(0);
        else
            params.setScrollFlags(3);

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
                if (isChecked)
                    recensioniStrutturaController.openOverview(struttura);
            }
        });

        toggleButtonGallery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    recensioniStrutturaController.openGallery(struttura);
            }
        });

        listViewRecensioni.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                recensioniStrutturaController.openDialogRecensione(listaRecensioni.get(position).getTesto());
            }
        });

        inizalizzaRecensioniStrutturaPage();
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

    private void inizalizzaRecensioniStrutturaPage() {
        Picasso.get().load(struttura.getFotoStruttura()).noFade().fit().centerCrop().into(imageViewRecensioni);
        textViewNomeStrutturaRecensioni.setText(struttura.getNomeStruttura());
        textViewVotoRecensioni.setText(String.valueOf(struttura.getVoto()));
    }

}
