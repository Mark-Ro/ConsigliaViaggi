package com.consigliaviaggi.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.consigliaviaggi.Controller.CambiaPasswordController;
import com.consigliaviaggi.R;

public class CambiaPasswordPage extends AppCompatActivity {

    private EditText editTextVecchiaPassword,editTextNuovaPassword,editTextConfermaPassword;
    private Button bottoneCambiaPassword;

    private CambiaPasswordController cambiaPasswordController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambia_password_page);

        Intent intent = getIntent();

        editTextVecchiaPassword = findViewById(R.id.editTextVecchiaPassword);
        editTextNuovaPassword = findViewById(R.id.editTextNuovaPassword);
        editTextConfermaPassword = findViewById(R.id.editTextConfermaPassword);
        bottoneCambiaPassword = findViewById(R.id.bottoneCambiaPassword);

        cambiaPasswordController = new CambiaPasswordController(this,this);

        bottoneCambiaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextVecchiaPassword.getText().toString().isEmpty() || editTextNuovaPassword.getText().toString().isEmpty() || editTextConfermaPassword.getText().toString().isEmpty())
                    Toast.makeText(CambiaPasswordPage.this, "Riempire i campi!", Toast.LENGTH_SHORT).show();
                else if (editTextVecchiaPassword.getText().toString().length()<8 || editTextNuovaPassword.getText().toString().length()<8 || editTextConfermaPassword.getText().toString().length()<8)
                    Toast.makeText(CambiaPasswordPage.this, "Password troppo corta!", Toast.LENGTH_SHORT).show();
                else if (!editTextNuovaPassword.getText().toString().equals(editTextConfermaPassword.getText().toString()))
                    Toast.makeText(CambiaPasswordPage.this, "Le password non coincidono!", Toast.LENGTH_SHORT).show();
                else
                    cambiaPasswordController.cambiaPassword(editTextVecchiaPassword.getText().toString(),editTextNuovaPassword.getText().toString());
            }
        });
    }

    public void activityPrecedente() {
        super.onBackPressed();
    }
}
