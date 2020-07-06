package com.consigliaviaggi.GUI;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.consigliaviaggi.Controller.RecensioniStrutturaController;
import com.consigliaviaggi.Entity.Recensione;
import com.consigliaviaggi.Entity.Struttura;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.consigliaviaggi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecensioniStrutturaPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private View contentView;
    private ImageView imageViewRecensioni;
    private TextView textViewNomeStrutturaRecensioni,textViewVotoRecensioni;
    private ToggleButton toggleButtonMenu,toggleButtonOverview,toggleButtonGallery;
    private FloatingActionButton floatingActionButtonNuovaRecensione,floatingActionButtonFiltroRecensioni;
    private RecensioniStrutturaController recensioniStrutturaController;
    private ArrayList<Recensione> listaRecensioni, listaTmpRecensioni;
    private Struttura struttura;

    private ListView listViewRecensioni;

    @SuppressLint("WrongConstant")
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
        floatingActionButtonNuovaRecensione = findViewById(R.id.floatingActionButtonNuovaRecensione);
        floatingActionButtonFiltroRecensioni = findViewById(R.id.floatingActionButtonFilter);
        navigationView.setNavigationItemSelectedListener(this);

        recensioniStrutturaController = new RecensioniStrutturaController(this,this);
        listaRecensioni = recensioniStrutturaController.getListaRecensioni(struttura);
        listaTmpRecensioni = listaRecensioni;
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
                recensioniStrutturaController.openDialogRecensione(listaTmpRecensioni.get(position).getTesto());
            }
        });

        floatingActionButtonNuovaRecensione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recensioniStrutturaController.openInserimentoRecensionePage(struttura);
            }
        });

        floatingActionButtonFiltroRecensioni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogFiltriRecensione();
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
        textViewVotoRecensioni.setText(String.valueOf(struttura.getVoto()).substring(0,3));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home: {
                recensioniStrutturaController.openHomePage();
                break;
            }
            case R.id.profilo: {
                recensioniStrutturaController.openProfiloPage();
                break;
            }
            case R.id.mappa: {
                recensioniStrutturaController.openMappaPage();
                break;
            }
        }
        return false;
    }

    public void openDialogFiltriRecensione() {
        final Dialog dialogRecensione = new Dialog(this);
        final ArrayList<Recensione> listaRecensioniFiltrata = new ArrayList<>();
        dialogRecensione.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogRecensione.setContentView(R.layout.layout_filtri_recensioni);
        dialogRecensione.setTitle("Titolo Dialog");
        dialogRecensione.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final RatingBar mratingBar = dialogRecensione.findViewById(R.id.ratingBar);
        Button buttonApplicaFiltro = dialogRecensione.findViewById(R.id.buttonApplicaFiltri);
        Button buttonAnnullaFiltro = dialogRecensione.findViewById(R.id.buttonAnnullaFiltri);
        mratingBar.setRating(1f);
        mratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating<1) {
                    mratingBar.setRating(1);
                }
            }
            });
        buttonApplicaFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (Recensione elem : listaRecensioni) {
                    if (elem.getVoto() == mratingBar.getRating()) {
                        listaRecensioniFiltrata.add(elem);
                    }
                }
                listaTmpRecensioni=listaRecensioniFiltrata;
                CustomAdapterRecensioniPage customAdapterRecensioniPage = new CustomAdapterRecensioniPage(RecensioniStrutturaPage.this,listaRecensioniFiltrata);
                listViewRecensioni.setAdapter(customAdapterRecensioniPage);
                dialogRecensione.dismiss();
            }
        });
        buttonAnnullaFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fix recensioni listview
                listaTmpRecensioni=listaRecensioni;
                CustomAdapterRecensioniPage customAdapterRecensioniPage = new CustomAdapterRecensioniPage(RecensioniStrutturaPage.this,listaRecensioni);
                listViewRecensioni.setAdapter(customAdapterRecensioniPage);
                dialogRecensione.dismiss();
            }
        });
        dialogRecensione.show();
    }
}
