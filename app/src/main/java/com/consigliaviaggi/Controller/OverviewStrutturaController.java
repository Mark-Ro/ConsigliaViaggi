package com.consigliaviaggi.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.consigliaviaggi.Entity.Struttura;
import com.consigliaviaggi.GUI.GalleryStrutturaPage;
import com.consigliaviaggi.GUI.RecensioniStrutturaPage;

public class OverviewStrutturaController {

    private Activity activityOverviewController;
    private Context contextOverviewController;

    public OverviewStrutturaController(Activity activityOverviewController, Context contextOverviewController) {
        this.activityOverviewController = activityOverviewController;
        this.contextOverviewController = contextOverviewController;
    }

    public void openGallery(Struttura struttura) {
        if (isNetworkAvailable()) {
            Intent intent = new Intent(activityOverviewController, GalleryStrutturaPage.class);
            intent.putExtra("Struttura", struttura);
            activityOverviewController.finish();
            activityOverviewController.overridePendingTransition(0, 0);
            activityOverviewController.startActivity(intent);
            activityOverviewController.overridePendingTransition(0, 0);
        }
        else
            Toast.makeText(activityOverviewController, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    public void openRecensioni(Struttura struttura) {
        if (isNetworkAvailable()) {
            Intent intent = new Intent(activityOverviewController, RecensioniStrutturaPage.class);
            intent.putExtra("Struttura", struttura);
            activityOverviewController.finish();
            activityOverviewController.overridePendingTransition(0, 0);
            activityOverviewController.startActivity(intent);
            activityOverviewController.overridePendingTransition(0, 0);
        }
        else
            Toast.makeText(activityOverviewController, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) contextOverviewController.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

}
