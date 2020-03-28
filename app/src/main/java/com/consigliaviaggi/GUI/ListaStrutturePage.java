package com.consigliaviaggi.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.consigliaviaggi.Entity.Struttura;
import com.consigliaviaggi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListaStrutturePage extends AppCompatActivity {

    private ListView listView;
    private TextView textViewNomeCitta;
    private ArrayList<Struttura> listaStrutture;
    private ImageView imageViewCitta,imageViewBarraCitta;


    private String nomeCitta,tipoStruttura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_strutture_page);

        Intent intent = getIntent();
        listaStrutture = (ArrayList<Struttura>) intent.getSerializableExtra("ListaStrutture");
        nomeCitta = intent.getStringExtra("Citta");
        tipoStruttura = intent.getStringExtra("TipoStruttura");

        listView = findViewById(R.id.listView);
        textViewNomeCitta = findViewById(R.id.textViewNomeCitta);
        imageViewCitta = findViewById(R.id.imageViewCitta);
        imageViewBarraCitta = findViewById(R.id.imageViewBarraCitta);

        CustomAdapterListaStrutture customAdapterListaStrutture = new CustomAdapterListaStrutture(this,listaStrutture);
        listView.setAdapter(customAdapterListaStrutture);

        setCitta();
    }

    private void setCitta() {
        Log.i("LISTA_STRUTTURA_PAGE",nomeCitta);
        if (!nomeCitta.equals("null")) {
            textViewNomeCitta.setText(listaStrutture.get(0).getCitta().getNome());
            Picasso.get().load(listaStrutture.get(0).getCitta().getFotoCitta()).noFade().fit().centerCrop().into(imageViewCitta);
        }
        else {
            Picasso.get().load(R.drawable.background_citta).noFade().fit().centerCrop(Gravity.BOTTOM).into(imageViewCitta);
            imageViewBarraCitta.setAlpha(0f);
            textViewNomeCitta.setText("");
        }
    }
}
