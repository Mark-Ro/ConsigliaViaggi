package com.consigliaviaggi.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.consigliaviaggi.Controller.RicercaController;
import com.consigliaviaggi.Entity.Struttura;
import com.consigliaviaggi.R;

import java.util.ArrayList;

public class RicercaPage extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextCitta,autoCompleteTextNomeStruttura;
    private Switch switchGPS,switchHotel,switchRistoranti,switchAltro;
    private Spinner spinnerRangeVoto,spinnerRangePrezzo;
    private Button bottoneAnnulla,bottoneRicerca;

    private RicercaController ricercaController;
    private ArrayList<Struttura> listaStrutture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricerca_page);

        autoCompleteTextCitta = findViewById(R.id.autoCompleteTextCitta);
        autoCompleteTextNomeStruttura = findViewById(R.id.autoCompleteTextNomeStruttura);
        switchGPS = findViewById(R.id.switchGPS);
        switchHotel = findViewById(R.id.switchHotel);
        switchRistoranti = findViewById(R.id.switchRistoranti);
        switchAltro = findViewById(R.id.switchAltro);
        spinnerRangeVoto = findViewById(R.id.spinnerRangeVoto);
        spinnerRangePrezzo = findViewById(R.id.spinnerRangePrezzo);
        bottoneAnnulla = findViewById(R.id.bottoneAnnulla);
        bottoneRicerca = findViewById(R.id.bottoneRicerca);

        ricercaController = new RicercaController(RicercaPage.this);

        switchGPS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    autoCompleteTextCitta.setEnabled(false);
                else
                    autoCompleteTextCitta.setEnabled(true);
            }
        });

        switchHotel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchRistoranti.setChecked(false);
                    switchAltro.setChecked(false);
                    spinnerRangePrezzo.setEnabled(true);
                }
            }
        });

        switchRistoranti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchHotel.setChecked(false);
                    switchAltro.setChecked(false);
                    spinnerRangePrezzo.setEnabled(false);
                }
            }
        });

        switchAltro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchHotel.setChecked(false);
                    switchRistoranti.setChecked(false);
                    spinnerRangePrezzo.setEnabled(false);
                }
            }
        });

        bottoneAnnulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextCitta.setText("");
                autoCompleteTextNomeStruttura.setText("");
                switchGPS.setChecked(false);
                switchHotel.setChecked(false);
                switchRistoranti.setChecked(false);
                switchAltro.setChecked(false);
                spinnerRangePrezzo.setEnabled(true);
            }
        });

        bottoneRicerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaStrutture = ricercaController.getListaStrutture(autoCompleteTextNomeStruttura.getText().toString(),autoCompleteTextCitta.getText().toString(),0.0f,100.0f,Float.valueOf(spinnerRangeVoto.getSelectedItem().toString()));
            }
        });
    }
}
