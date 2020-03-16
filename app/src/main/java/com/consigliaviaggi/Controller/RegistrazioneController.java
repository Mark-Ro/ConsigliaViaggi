package com.consigliaviaggi.Controller;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.consigliaviaggi.DAO.RegistrazioneCognito;
import com.consigliaviaggi.DAO.UtenteDAO;
import com.consigliaviaggi.GUI.VerificationCodePage;

public class RegistrazioneController {

    private Context context;
    private UtenteDAO utenteDAO;

    public RegistrazioneController(Context context) {
        this.context = context;
        this.utenteDAO = new UtenteDAO(context);
    }

    public void effettuaRegistrazione(String nome, String cognome, String email, String nickname, String password, boolean nomePubblico) {

        if (isNetworkAvailable()) {
            String resultInsert = utenteDAO.inserimentoUtente(nome, cognome, email, nickname, password, nomePubblico);
            Log.i("REGISTRAZIONE_CONTROLLER",resultInsert);
            if (resultInsert.contains("PRIMARY"))
                Toast.makeText(context, "Nickname già esistente!", Toast.LENGTH_SHORT).show();
            else if (resultInsert.contains("uniqueMail"))
                Toast.makeText(context, "Mail già esistente!", Toast.LENGTH_SHORT).show();
            else {
                RegistrazioneCognito registrazioneCognito = new RegistrazioneCognito(RegistrazioneController.this,context,nickname,password,email);
                registrazioneCognito.effettuaRegistrazioneCognito();
            }
        }
            else
            Toast.makeText(context, "Connessione Internet non disponibile!", Toast.LENGTH_SHORT).show();

    }
    
    public void registrazioneEffettuataConSuccesso(String nickname, String password, String email) {
        Toast.makeText(context, "Registrazione effettuata! Codice di verifica inviato a: " + email, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, VerificationCodePage.class);
        intent.putExtra("Username",nickname);
        intent.putExtra("Password",password);
        intent.putExtra("ActivityChiamante","Registrazione");
        context.startActivity(intent);
    }

    public void registrazioneFallita(Exception exception) {
        Toast.makeText(context, "Registrazione fallita: " + exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
    
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
