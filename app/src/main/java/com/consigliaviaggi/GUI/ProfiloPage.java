package com.consigliaviaggi.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.consigliaviaggi.Controller.ProfiloController;
import com.consigliaviaggi.R;

public class ProfiloPage extends AppCompatActivity {

    private TextView textViewNomeProfilo,textViewCognomeProfilo,textViewEmailProfilo,textViewNicknameProfilo,textViewNumeroRecensioniProfilo;

    private ProfiloController profiloController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo_page);

        Intent intent = getIntent();

        textViewNomeProfilo = findViewById(R.id.textViewNomeProfilo);
        textViewCognomeProfilo =  findViewById(R.id.textViewCognomeProfilo);
        textViewEmailProfilo = findViewById(R.id.textViewEmailProfilo);
        textViewNicknameProfilo = findViewById(R.id.textViewNicknameProfilo);
        textViewNumeroRecensioniProfilo = findViewById(R.id.textViewNumeroRecensioniProfilo);

        profiloController = new ProfiloController(ProfiloPage.this);
        profiloController.setTextViewsProfiloPage(textViewNomeProfilo,textViewCognomeProfilo,textViewEmailProfilo,textViewNicknameProfilo,textViewNumeroRecensioniProfilo);

    }
}
