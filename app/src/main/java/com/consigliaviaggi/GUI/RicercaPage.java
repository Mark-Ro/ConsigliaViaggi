package com.consigliaviaggi.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.widget.CompoundButton;
import android.widget.RatingBar;
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
    private RatingBar ratingBar;
    private Button bottoneAnnulla,bottoneRicerca;
    private SeekBar seekBarPrezzoMassimo;
    private ArrayAdapter<String> adapter;

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
        ratingBar = findViewById(R.id.ratingBar);
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
                        ricercaController.setCurrentLocation();
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
                    seekBarPrezzoMassimo.setProgress(3000);
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
                    seekBarPrezzoMassimo.setProgress(3000);
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
                           ricercaController.effettuaRicercaStrutture(autoCompleteTextNomeStruttura.getText().toString(), "null", "null", getPrezzoMassimoFromSpinner(), ratingBar.getRating(), getTipoStruttura());
                       }
                       else if (autoCompleteTextNomeStruttura.getText().toString().isEmpty()) {
                           String[] cittaNazione = getCittaAndNazioneFromAutoCompleteTextView();
                           ricercaController.openLoadingDialog(RicercaPage.this);
                           ricercaController.effettuaRicercaStrutture("null", cittaNazione[0], cittaNazione[1], getPrezzoMassimoFromSpinner(),ratingBar.getRating() , getTipoStruttura());

                       }
                       else {
                           String[] cittaNazione = getCittaAndNazioneFromAutoCompleteTextView();
                           ricercaController.openLoadingDialog(RicercaPage.this);
                           ricercaController.effettuaRicercaStrutture(autoCompleteTextNomeStruttura.getText().toString(),cittaNazione[0],cittaNazione[1],getPrezzoMassimoFromSpinner(),ratingBar.getRating(),getTipoStruttura());
                       }
                   }
               }
               else {
                    if (autoCompleteTextNomeStruttura.getText().toString().isEmpty()) {
                        ricercaController.openLoadingDialog(RicercaPage.this);
                        ricercaController.effettuaRicercaStruttureConPosizione("null", getPrezzoMassimoFromSpinner(), ratingBar.getRating(), getTipoStruttura());
                    }
                    else {
                        ricercaController.openLoadingDialog(RicercaPage.this);
                        ricercaController.effettuaRicercaStruttureConPosizione(autoCompleteTextNomeStruttura.getText().toString(), getPrezzoMassimoFromSpinner(), ratingBar.getRating(), getTipoStruttura());
                    }
               }
            }
        });

        new AsyncTask<Void,Void,Void>() {

            private String[] arrayCitta;

            @Override
            protected Void doInBackground(Void... voids) {
                arrayCitta = ricercaController.getArrayStringaCitta();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (arrayCitta!=null) {
                    adapter = new ArrayAdapter<String>(RicercaPage.this, R.layout.autocompletetextview_item, R.id.text_view_list_item, arrayCitta);
                    autoCompleteTextCitta.setAdapter(adapter);
                }
            }
        }.execute();

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

    private String[] getCittaAndNazioneFromAutoCompleteTextView() {
        String[] risultato = new String[2];
        if (adapter!=null && !adapter.isEmpty()) {
            String ricercaInput = adapter.getItem(0).toString();
            if (ricercaInput != null) {
                int indiceVirgola = ricercaInput.indexOf(",");
                risultato[0] = ricercaInput.substring(0, indiceVirgola);
                risultato[1] = ricercaInput.substring(indiceVirgola + 1);
            }
            else {
                risultato[0] = "null";
                risultato[1] = "null";
            }
        }
        else {
            risultato[0] = "null";
            risultato[1] = "null";
        }

        Log.i("RICERCA_PAGE_CITTA_NAZIONE", risultato[0] + " " + risultato[1]);

        return risultato;
    }
}
