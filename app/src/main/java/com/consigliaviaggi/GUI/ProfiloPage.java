package com.consigliaviaggi.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.consigliaviaggi.Controller.ProfiloController;
import com.consigliaviaggi.R;

public class ProfiloPage extends AppCompatActivity {

    private TextView textViewNomeProfilo,textViewCognomeProfilo,textViewEmailProfilo,textViewNicknameProfilo,textViewNumeroRecensioniProfilo;
    private Button bottoneHome,bottoneLogout,bottoneCambiaPassword,bottoneCambiaEmail;
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
        bottoneLogout = findViewById(R.id.bottoneLogout);
        bottoneCambiaPassword = findViewById(R.id.bottoneCambiaPassword);
        bottoneCambiaEmail = findViewById(R.id.bottoneCambiaEmail);

        profiloController = new ProfiloController(ProfiloPage.this,ProfiloPage.this);
        setTextViews();

        profiloController.updateTextViewsProfiloPage(ProfiloPage.this);

        bottoneHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profiloController.openHomePage();
            }
        });

        bottoneLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profiloController.logout();
            }
        });

        bottoneCambiaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profiloController.openCambiaPasswordPage();
            }
        });

        bottoneCambiaEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profiloController.openCambiaEmailPage();
            }
        });
    }

    public void setTextViews () {
        profiloController.setTextViewsProfiloPage(textViewNomeProfilo,textViewCognomeProfilo,textViewEmailProfilo,textViewNicknameProfilo,textViewNumeroRecensioniProfilo,bottoneLogout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTextViews();
    }


}
