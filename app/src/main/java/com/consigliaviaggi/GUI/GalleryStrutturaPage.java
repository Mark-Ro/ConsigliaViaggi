package com.consigliaviaggi.GUI;

import android.content.Intent;
import android.os.Bundle;

import com.consigliaviaggi.Entity.Struttura;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.consigliaviaggi.R;

public class GalleryStrutturaPage extends AppCompatActivity {

    private Struttura struttura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_struttura_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        struttura = (Struttura) intent.getSerializableExtra("Struttura");
    }
}
