package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.consigliaviaggi.Entity.Struttura;
import com.consigliaviaggi.GUI.GalleryStrutturaPage;

public class OverviewStrutturaController {

    private Activity activityOverviewController;
    private Context contextOverviewController;

    public OverviewStrutturaController(Activity activityOverviewController, Context contextOverviewController) {
        this.activityOverviewController = activityOverviewController;
        this.contextOverviewController = contextOverviewController;
    }

    public void openGallery(Struttura struttura) {
        Intent intent = new Intent(activityOverviewController, GalleryStrutturaPage.class);
        intent.putExtra("Struttura", struttura);
        activityOverviewController.startActivity(intent);
    }

}
