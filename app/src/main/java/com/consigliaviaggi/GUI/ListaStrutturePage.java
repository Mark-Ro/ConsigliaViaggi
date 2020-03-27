package com.consigliaviaggi.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.consigliaviaggi.Entity.Struttura;
import com.consigliaviaggi.R;

import java.util.ArrayList;

public class ListaStrutturePage extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_strutture_page);

        Intent intent = getIntent();
        ArrayList<Struttura> listaStrutture = (ArrayList<Struttura>) intent.getSerializableExtra("ListaStrutture");

        listView = findViewById(R.id.listView);
        CustomAdapterListaStrutture customAdapterListaStrutture = new CustomAdapterListaStrutture(this,listaStrutture);
        listView.setAdapter(customAdapterListaStrutture);
    }
}
