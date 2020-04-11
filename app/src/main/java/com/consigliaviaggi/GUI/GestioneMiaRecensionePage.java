package com.consigliaviaggi.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.consigliaviaggi.Entity.Recensione;
import com.consigliaviaggi.R;

public class GestioneMiaRecensionePage extends AppCompatActivity {

    private Recensione recensione;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_mia_recensione_page);

        Intent intent = getIntent();
        recensione = (Recensione) intent.getSerializableExtra("Recensione");

        Toast.makeText(this, recensione.getTesto(), Toast.LENGTH_SHORT).show();
    }
}
