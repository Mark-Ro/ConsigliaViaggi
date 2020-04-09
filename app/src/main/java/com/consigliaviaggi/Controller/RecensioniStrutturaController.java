package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Window;
import android.widget.TextView;

import com.consigliaviaggi.DAO.RecensioneDAO;
import com.consigliaviaggi.Entity.Recensione;
import com.consigliaviaggi.Entity.Struttura;
import com.consigliaviaggi.GUI.GalleryStrutturaPage;
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
        Intent intent = new Intent(activityRecensioniStrutturaPage, OverviewStrutturaPage.class);
        intent.putExtra("Struttura", struttura);
        activityRecensioniStrutturaPage.finish();
        activityRecensioniStrutturaPage.overridePendingTransition(0, 0);
        activityRecensioniStrutturaPage.startActivity(intent);
        activityRecensioniStrutturaPage.overridePendingTransition(0, 0);
    }

    public void openGallery(Struttura struttura) {
        Intent intent = new Intent(activityRecensioniStrutturaPage, GalleryStrutturaPage.class);
        intent.putExtra("Struttura", struttura);
        activityRecensioniStrutturaPage.finish();
        activityRecensioniStrutturaPage.overridePendingTransition(0, 0);
        activityRecensioniStrutturaPage.startActivity(intent);
        activityRecensioniStrutturaPage.overridePendingTransition(0, 0);
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
}
