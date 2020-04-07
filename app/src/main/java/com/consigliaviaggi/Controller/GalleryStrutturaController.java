package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Window;

import com.consigliaviaggi.DAO.GalleryDAO;
import com.consigliaviaggi.Entity.Gallery;
import com.consigliaviaggi.Entity.Struttura;
import com.consigliaviaggi.GUI.OverviewStrutturaPage;
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
        PhotoView photoView = zoomDialog.findViewById(R.id.photoView);
        Picasso.get().load(immagine).noFade().into(photoView);
        zoomDialog.show();
    }

}
