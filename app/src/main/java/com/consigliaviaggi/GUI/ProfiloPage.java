package com.consigliaviaggi.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.consigliaviaggi.Controller.ProfiloController;
import com.consigliaviaggi.Entity.Utente;
import com.consigliaviaggi.R;

public class ProfiloPage extends AppCompatActivity {

    private TextView textViewNomeProfilo,textViewCognomeProfilo,textViewEmailProfilo,textViewNicknameProfilo,textViewNumeroRecensioniProfilo;
    private Button bottoneHome;
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
        bottoneHome = findViewById(R.id.bottoneHome);

        profiloController = new ProfiloController(ProfiloPage.this);
        setTextViews();

        profiloController.updateTextViewsProfiloPage(ProfiloPage.this);

        bottoneHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfiloPage.this,MainActivity.class);
                startActivity(intent);
            }
        });



    }

    public void setTextViews () {
        profiloController.setTextViewsProfiloPage(textViewNomeProfilo,textViewCognomeProfilo,textViewEmailProfilo,textViewNicknameProfilo,textViewNumeroRecensioniProfilo);
    }


}
