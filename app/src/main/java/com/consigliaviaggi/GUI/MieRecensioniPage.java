package com.consigliaviaggi.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.consigliaviaggi.Controller.MieRecensioniController;
import com.consigliaviaggi.Entity.Recensione;
import com.consigliaviaggi.R;

import java.util.ArrayList;

public class MieRecensioniPage extends AppCompatActivity {

    private ListView listViewMieRecensioni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mie_recensioni_page);

        listViewMieRecensioni = findViewById(R.id.listView);

        MieRecensioniController mieRecensioniController = new MieRecensioniController(this,this);

        ArrayList<Recensione> listaMieRecensioni = mieRecensioniController.getMieRecensioni();

        CustomAdapterMieRecensioniPage customAdapterMieRecensioniPage = new CustomAdapterMieRecensioniPage(this,listaMieRecensioni);
        listViewMieRecensioni.setAdapter(customAdapterMieRecensioniPage);
    }
}
