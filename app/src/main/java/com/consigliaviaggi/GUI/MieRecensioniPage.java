package com.consigliaviaggi.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

        final MieRecensioniController mieRecensioniController = new MieRecensioniController(this,this);

        final ArrayList<Recensione> listaMieRecensioni = mieRecensioniController.getMieRecensioni();

        CustomAdapterMieRecensioniPage customAdapterMieRecensioniPage = new CustomAdapterMieRecensioniPage(this,listaMieRecensioni);
        listViewMieRecensioni.setAdapter(customAdapterMieRecensioniPage);

        listViewMieRecensioni.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mieRecensioniController.openGestioneRecensionePage(listaMieRecensioni.get(position));
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        listViewMieRecensioni = findViewById(R.id.listView);

        final MieRecensioniController mieRecensioniController = new MieRecensioniController(this,this);

        final ArrayList<Recensione> listaMieRecensioni = mieRecensioniController.getMieRecensioni();

        CustomAdapterMieRecensioniPage customAdapterMieRecensioniPage = new CustomAdapterMieRecensioniPage(this,listaMieRecensioni);
        listViewMieRecensioni.setAdapter(customAdapterMieRecensioniPage);

    }

}
