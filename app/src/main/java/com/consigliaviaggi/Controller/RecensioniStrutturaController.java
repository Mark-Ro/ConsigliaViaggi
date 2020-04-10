package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.consigliaviaggi.DAO.RecensioneDAO;
import com.consigliaviaggi.Entity.Recensione;
import com.consigliaviaggi.Entity.Struttura;
import com.consigliaviaggi.Entity.Utente;
import com.consigliaviaggi.GUI.GalleryStrutturaPage;
import com.consigliaviaggi.GUI.InserimentoRecensionePage;
import com.consigliaviaggi.GUI.OverviewStrutturaPage;
import com.consigliaviaggi.R;

import java.util.ArrayList;

public class RecensioniStrutturaController {

    private Activity activityRecensioniStrutturaPage;
    private Context contextRecensioniStrutturaPage;
    private Dialog dialogRecensione;

    public RecensioniStrutturaController(Activity activityRecensioniStrutturaPage, Context contextRecensioniStrutturaPage) {
        this.activityRecensioniStrutturaPage = activityRecensioniStrutturaPage;
        this.contextRecensioniStrutturaPage = contextRecensioniStrutturaPage;
    }

    public ArrayList<Recensione> getListaRecensioni(Struttura struttura) {
        RecensioneDAO recensioneDAO = new RecensioneDAO(contextRecensioniStrutturaPage);
        ArrayList<Recensione> listaRecensioni = recensioneDAO.getRecensioniStrutturaFromDatabase(struttura.getIdStruttura());
        return listaRecensioni;
    }

    public void openOverview(Struttura struttura) {
        if (isNetworkAvailable()) {
            Intent intent = new Intent(activityRecensioniStrutturaPage, OverviewStrutturaPage.class);
            intent.putExtra("Struttura", struttura);
            activityRecensioniStrutturaPage.finish();
            activityRecensioniStrutturaPage.overridePendingTransition(0, 0);
            activityRecensioniStrutturaPage.startActivity(intent);
            activityRecensioniStrutturaPage.overridePendingTransition(0, 0);
        }
        else
            Toast.makeText(activityRecensioniStrutturaPage, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    public void openGallery(Struttura struttura) {
        if (isNetworkAvailable()) {
            Intent intent = new Intent(activityRecensioniStrutturaPage, GalleryStrutturaPage.class);
            intent.putExtra("Struttura", struttura);
            activityRecensioniStrutturaPage.finish();
            activityRecensioniStrutturaPage.overridePendingTransition(0, 0);
            activityRecensioniStrutturaPage.startActivity(intent);
            activityRecensioniStrutturaPage.overridePendingTransition(0, 0);
        }
        else
            Toast.makeText(activityRecensioniStrutturaPage, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    public void openDialogRecensione(String testoRecensione) {
        dialogRecensione = new Dialog(contextRecensioniStrutturaPage);
        dialogRecensione.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogRecensione.setContentView(R.layout.layout_visione_recensione);
        dialogRecensione.setTitle("Titolo Dialog");
        TextView testo = dialogRecensione.findViewById(R.id.textViewVisione_Recensione);
        testo.setText(testoRecensione);
        dialogRecensione.show();
    }

    public void openInserimentoRecensionePage(Struttura struttura) {
        if (isNetworkAvailable()) {
            Utente utente = Utente.getIstance();
            if (utente.isUtenteAutenticato()) {
                Intent intent = new Intent(activityRecensioniStrutturaPage, InserimentoRecensionePage.class);
                intent.putExtra("Struttura", struttura);
                activityRecensioniStrutturaPage.startActivity(intent);
            }
            else
                Toast.makeText(activityRecensioniStrutturaPage, "È necessario autenticarsi per inserire una recensione!", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(activityRecensioniStrutturaPage, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) contextRecensioniStrutturaPage.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
