package com.consigliaviaggi.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.consigliaviaggi.Controller.InserimentoRecensioneController;
import com.consigliaviaggi.Entity.Struttura;
import com.consigliaviaggi.R;
import com.google.android.material.textfield.TextInputEditText;

public class InserimentoRecensionePage extends AppCompatActivity {

    private TextInputEditText textInputEditTextRecensione;
    private Button bottoneNuovaRecensione;
    private RatingBar ratingBar;

    private Struttura struttura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserimento_recensioni_page);

        Intent intent = getIntent();
        struttura = (Struttura) intent.getSerializableExtra("Struttura");

        textInputEditTextRecensione = findViewById(R.id.textInputEditTextRecensione);
        bottoneNuovaRecensione = findViewById(R.id.bottoneNuovaRecensione);
        ratingBar = findViewById(R.id.ratingBar);

        final InserimentoRecensioneController inserimentoRecensioneController = new InserimentoRecensioneController(this,this);

        bottoneNuovaRecensione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textInputEditTextRecensione.setText(textInputEditTextRecensione.getText().toString().trim());
                if (textInputEditTextRecensione.getText().toString().isEmpty())
                    Toast.makeText(InserimentoRecensionePage.this, "Riempire i campi!", Toast.LENGTH_SHORT).show();
                else if (ratingBar.getRating()<1)
                    Toast.makeText(InserimentoRecensionePage.this, "Il voto deve essere almeno di una stella!", Toast.LENGTH_SHORT).show();
                else {
                    inserimentoRecensioneController.openLoadingDialog(InserimentoRecensionePage.this);
                    inserimentoRecensioneController.inserimentoRecensione(struttura,textInputEditTextRecensione.getText().toString(),ratingBar.getRating());
                }
            }
        });
    }
}
