package com.consigliaviaggi.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.consigliaviaggi.Controller.RecuperaPasswordController;
import com.consigliaviaggi.R;

public class RecuperaPasswordPage extends AppCompatActivity {

    private EditText editTextUsername,editTextCodice,editTextNuovaPassword;
    private Button bottoneRiceviCodice,bottoneResetPassword;

    private RecuperaPasswordController recuperaPasswordController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recupera_password_page);

        Intent intent = getIntent();

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextCodice = findViewById(R.id.editTextCodice);
        editTextNuovaPassword = findViewById(R.id.editTextNuovaPassword);
        bottoneRiceviCodice = findViewById(R.id.bottoneRiceviCodice);
        bottoneResetPassword = findViewById(R.id.bottoneResetPassword);

        recuperaPasswordController = new RecuperaPasswordController(this,this);

        bottoneRiceviCodice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextUsername.getText().toString().isEmpty())
                    recuperaPasswordController.riceviCodice(editTextUsername.getText().toString());
                else
                    Toast.makeText(RecuperaPasswordPage.this, "Riempire i campi!", Toast.LENGTH_SHORT).show();
            }
        });

        bottoneResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextCodice.getText().toString().isEmpty() || editTextNuovaPassword.getText().toString().isEmpty())
                    Toast.makeText(RecuperaPasswordPage.this, "Riempire i campi!", Toast.LENGTH_SHORT).show();
                else if(editTextNuovaPassword.getText().toString().length()<8)
                    Toast.makeText(RecuperaPasswordPage.this, "Password troppo corta!", Toast.LENGTH_SHORT).show();
                else {
                    recuperaPasswordController.openLoadingDialog(RecuperaPasswordPage.this);
                    recuperaPasswordController.resettaPassword(editTextCodice.getText().toString(), editTextNuovaPassword.getText().toString());
                }
            }
        });
    }

    public void activityPrecedente() {
        super.onBackPressed();
    }
}
