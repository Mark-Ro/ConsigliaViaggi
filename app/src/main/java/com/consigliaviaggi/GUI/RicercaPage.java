package com.consigliaviaggi.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.consigliaviaggi.Controller.RicercaController;
import com.consigliaviaggi.R;



public class RicercaPage extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextCitta,autoCompleteTextNomeStruttura;
    private TextView textViewRangePrezzo;
    private Switch switchGPS;
    private ToggleButton toggleButtonHotel,toggleButtonRistorante,toggleButtonAltro;
    private Spinner spinnerRangeVoto;
    private Button bottoneAnnulla,bottoneRicerca;
    private SeekBar seekBarPrezzoMassimo;

    private RicercaController ricercaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricerca_page);

        autoCompleteTextCitta = findViewById(R.id.autoCompleteTextCitta);
        autoCompleteTextNomeStruttura = findViewById(R.id.autoCompleteTextNomeStruttura);
        switchGPS = findViewById(R.id.switchGPS);
        toggleButtonHotel = findViewById(R.id.toggleButtonHotel);
        toggleButtonRistorante = findViewById(R.id.toggleButtonRistorante);
        toggleButtonAltro = findViewById(R.id.toggleButtonAltro);
        spinnerRangeVoto = findViewById(R.id.spinnerRangeVoto);
        textViewRangePrezzo=findViewById(R.id.textViewRangePrezzo);
        seekBarPrezzoMassimo=findViewById(R.id.seekBarPrezzoMassimo);
        bottoneAnnulla = findViewById(R.id.bottoneAnnulla);
        bottoneRicerca = findViewById(R.id.bottoneRicerca);
        toggleButtonHotel.performClick();

        ricercaController = new RicercaController(RicercaPage.this,RicercaPage.this);

        switchGPS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    autoCompleteTextCitta.setEnabled(false);
                    if (ricercaController.verificaCondizioniGPS())
                        ricercaController.getCurrentLocation();
                    else {
                        switchGPS.setChecked(false);
                        autoCompleteTextCitta.setEnabled(true);
                        }
                    }
                else
                    autoCompleteTextCitta.setEnabled(true);
            }
        });

        toggleButtonHotel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleButtonRistorante.setChecked(false);
                    toggleButtonAltro.setChecked(false);
                    seekBarPrezzoMassimo.setEnabled(true);
                }
            }
        });

       toggleButtonRistorante.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (isChecked) {
                   toggleButtonHotel.setChecked(false);
                   toggleButtonAltro.setChecked(false);
                   seekBarPrezzoMassimo.setEnabled(false);
               }
           }
       });

        toggleButtonAltro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleButtonHotel.setChecked(false);
                    toggleButtonRistorante.setChecked(false);
                    seekBarPrezzoMassimo.setEnabled(false);
                }
            }
        });

        bottoneAnnulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextCitta.setText("");
                autoCompleteTextNomeStruttura.setText("");
                switchGPS.setChecked(false);
                toggleButtonHotel.setChecked(false);
                toggleButtonRistorante.setChecked(false);
                toggleButtonAltro.setChecked(false);
                seekBarPrezzoMassimo.setEnabled(true);
            }
        });

        seekBarPrezzoMassimo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewRangePrezzo.setText(String.valueOf(progress)+"€");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        bottoneRicerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (!switchGPS.isChecked()) {
                   if (autoCompleteTextCitta.getText().toString().isEmpty() && autoCompleteTextNomeStruttura.getText().toString().isEmpty())
                       Toast.makeText(RicercaPage.this, "Riempire i campi!", Toast.LENGTH_SHORT).show();
                   else {
                       if (autoCompleteTextCitta.getText().toString().isEmpty()) {
                           ricercaController.openLoadingDialog(RicercaPage.this);
                           ricercaController.effettuaRicercaStrutture(autoCompleteTextNomeStruttura.getText().toString(), "null", "null", getPrezzoMassimoFromSpinner(), Float.parseFloat(spinnerRangeVoto.getSelectedItem().toString()), getTipoStruttura());
                       }
                       else if (autoCompleteTextNomeStruttura.getText().toString().isEmpty()) {
                           ricercaController.openLoadingDialog(RicercaPage.this);
                           ricercaController.effettuaRicercaStrutture("null", autoCompleteTextCitta.getText().toString(), "Italia", getPrezzoMassimoFromSpinner(), Float.parseFloat(spinnerRangeVoto.getSelectedItem().toString()), getTipoStruttura());
                       }
                       else {
                           ricercaController.openLoadingDialog(RicercaPage.this);
                           ricercaController.effettuaRicercaStrutture(autoCompleteTextNomeStruttura.getText().toString(),autoCompleteTextCitta.getText().toString(),"Italia",getPrezzoMassimoFromSpinner(),Float.parseFloat(spinnerRangeVoto.getSelectedItem().toString()),getTipoStruttura());
                       }
                   }
               }
               else {
                    if (autoCompleteTextNomeStruttura.getText().toString().isEmpty()) {
                        ricercaController.openLoadingDialog(RicercaPage.this);
                        ricercaController.effettuaRicercaStruttureConPosizione("null", getPrezzoMassimoFromSpinner(), Float.parseFloat(spinnerRangeVoto.getSelectedItem().toString()), getTipoStruttura());
                    }
                    else {
                        ricercaController.openLoadingDialog(RicercaPage.this);
                        ricercaController.effettuaRicercaStruttureConPosizione(autoCompleteTextNomeStruttura.getText().toString(), getPrezzoMassimoFromSpinner(), Float.parseFloat(spinnerRangeVoto.getSelectedItem().toString()), getTipoStruttura());
                    }
               }
            }
        });
    }


    private float getPrezzoMassimoFromSpinner() {
        String prezzoInput =textViewRangePrezzo.getText().toString();
        float risultato;
        if (prezzoInput.equals("oltre"))
            risultato = 10000f;
        else {
            prezzoInput = prezzoInput.substring(0, prezzoInput.length() - 1);
            Log.i("RICERCA_PAGE", prezzoInput);
            risultato = Float.parseFloat(prezzoInput);
        }
        return risultato;
    }

    private String getTipoStruttura() {

        String risultato=null;

        if (toggleButtonHotel.isChecked())
            risultato="Hotel";
        else if (toggleButtonAltro.isChecked())
            risultato="Altro";
        else
            risultato="Ristorante";
        return risultato;
    }


}
