package com.consigliaviaggi.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.consigliaviaggi.Controller.GestioneMiaRecensioneController;
import com.consigliaviaggi.Entity.Recensione;
import com.consigliaviaggi.R;
import com.google.android.material.textfield.TextInputEditText;

public class GestioneMiaRecensionePage extends AppCompatActivity {

    private TextInputEditText textInputEditTextGestisciRecensione;
    private Button buttonGestisciRecensioneOk,buttonGestisciRecensioniElimina;
    private RatingBar ratingBar;

    private Recensione recensione;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_mia_recensione_page);

        Intent intent = getIntent();
        recensione = (Recensione) intent.getSerializableExtra("Recensione");

        textInputEditTextGestisciRecensione = findViewById(R.id.textInputEditTextGestisciRecensione);
        buttonGestisciRecensioneOk = findViewById(R.id.buttonGestisciRecensioneOk);
        buttonGestisciRecensioniElimina = findViewById(R.id.buttonGestisciRecensioneElimina);
        ratingBar = findViewById(R.id.ratingBar);

        textInputEditTextGestisciRecensione.setText(recensione.getTesto());
        ratingBar.setRating(recensione.getVoto());

        final GestioneMiaRecensioneController gestioneMiaRecensioneController = new GestioneMiaRecensioneController(this,this);

        buttonGestisciRecensioneOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textInputEditTextGestisciRecensione.getText().toString().isEmpty())
                    Toast.makeText(GestioneMiaRecensionePage.this, "Riempire i campi!", Toast.LENGTH_SHORT).show();
                else if (ratingBar.getRating()<1)
                    Toast.makeText(GestioneMiaRecensionePage.this, "Il voto deve essere almeno uno", Toast.LENGTH_SHORT).show();
                else {
                    gestioneMiaRecensioneController.openLoadingDialog(GestioneMiaRecensionePage.this);
                    gestioneMiaRecensioneController.updateRecensione(recensione.getIdRecensione(), textInputEditTextGestisciRecensione.getText().toString(), ratingBar.getRating());
                }
            }
        });

        buttonGestisciRecensioniElimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gestioneMiaRecensioneController.deleteRecensione(recensione.getIdRecensione());
            }
        });
    }
}
