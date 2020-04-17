package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.consigliaviaggi.DAO.GalleryDAO;
import com.consigliaviaggi.Entity.Gallery;
import com.consigliaviaggi.Entity.Struttura;
import com.consigliaviaggi.Entity.Utente;
import com.consigliaviaggi.GUI.LoginPage;
import com.consigliaviaggi.GUI.MainActivity;
import com.consigliaviaggi.GUI.MappaPage;
import com.consigliaviaggi.GUI.OverviewStrutturaPage;
import com.consigliaviaggi.GUI.ProfiloPage;
import com.consigliaviaggi.GUI.RecensioniStrutturaPage;
import com.consigliaviaggi.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GalleryStrutturaController {

    private Activity activityGalleryStrutturaPage;
    private Context contextGalleryStrutturaPage;
    private Dialog zoomDialog;

    public GalleryStrutturaController(Activity activityGalleryStrutturaPage, Context contextGalleryStrutturaPage) {
        this.activityGalleryStrutturaPage = activityGalleryStrutturaPage;
        this.contextGalleryStrutturaPage = contextGalleryStrutturaPage;
    }

    public void openOverview(Struttura struttura) {
        Intent intent = new Intent(activityGalleryStrutturaPage, OverviewStrutturaPage.class);
        intent.putExtra("Struttura", struttura);
        activityGalleryStrutturaPage.finish();
        activityGalleryStrutturaPage.overridePendingTransition(0, 0);
        activityGalleryStrutturaPage.startActivity(intent);
        activityGalleryStrutturaPage.overridePendingTransition(0, 0);
    }

    public void openRecensioni(Struttura struttura) {
        if (isNetworkAvailable()) {
            Intent intent = new Intent(activityGalleryStrutturaPage, RecensioniStrutturaPage.class);
            intent.putExtra("Struttura",struttura);
            activityGalleryStrutturaPage.finish();
            activityGalleryStrutturaPage.overridePendingTransition(0, 0);
            activityGalleryStrutturaPage.startActivity(intent);
            activityGalleryStrutturaPage.overridePendingTransition(0, 0);
        }
        else
            Toast.makeText(activityGalleryStrutturaPage, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }
    public void openHomePage() {
        Log.i("LISTA_STRUTTURE_PAGE_CONTROLLER","Entrato in openHomePage");
        Intent intent = new Intent(activityGalleryStrutturaPage.getApplicationContext(), MainActivity.class);
        activityGalleryStrutturaPage.startActivity(intent);
    }

    public void openProfiloPage() {
        Log.i("LISTA_STRUTTURE_PAGE_CONTROLLER","Entrato in profilo");
        Intent intent;
        Utente utente = Utente.getIstance();
        if (utente.isUtenteAutenticato())
            intent = new Intent(activityGalleryStrutturaPage, ProfiloPage.class);
        else
            intent = new Intent(activityGalleryStrutturaPage, LoginPage.class);

        activityGalleryStrutturaPage.startActivity(intent);
    }

    public void openMappaPage() {
        if (isNetworkAvailable()) {
            Intent intent = new Intent(activityGalleryStrutturaPage, MappaPage.class);
            activityGalleryStrutturaPage.startActivity(intent);
        }
        else
            Toast.makeText(activityGalleryStrutturaPage, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();

    }

    public ArrayList<Gallery> getGalleryStruttura(Struttura struttura) {
        GalleryDAO galleryDAO = new GalleryDAO(contextGalleryStrutturaPage);
        ArrayList<Gallery> listaGallery = galleryDAO.getGalleryStrutturaFromDatabase(struttura.getIdStruttura());
        return listaGallery;
    }

    public void zoomImageDialog(String immagine) {
        zoomDialog = new Dialog(contextGalleryStrutturaPage);
        zoomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        zoomDialog.setContentView(R.layout.zoom_image_dialog);
        zoomDialog.setTitle("Titolo Dialog");
        Button bottoneChiudiDialog = zoomDialog.findViewById(R.id.buttonChiudiDialog);
        PhotoView photoView = zoomDialog.findViewById(R.id.photoView);
        bottoneChiudiDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomDialog.dismiss();
            }
        });
        Picasso.get().load(immagine).noFade().into(photoView);
        zoomDialog.show();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) activityGalleryStrutturaPage.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
