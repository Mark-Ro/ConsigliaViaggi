package com.consigliaviaggi.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.consigliaviaggi.Controller.CambiaEmailController;
import com.consigliaviaggi.R;

public class CambiaEmailPage extends AppCompatActivity {

    private EditText editTextNuovaEmail;
    private Button bottoneCambiaEmail;

    private CambiaEmailController cambiaEmailController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambia_email_page);

        editTextNuovaEmail = findViewById(R.id.editTextNuovaEmail);
        bottoneCambiaEmail = findViewById(R.id.bottoneCambiaEmail);

        cambiaEmailController = new CambiaEmailController(this,this);

        bottoneCambiaEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextNuovaEmail.getText().toString().isEmpty())
                    Toast.makeText(CambiaEmailPage.this, "Riempire i campi!", Toast.LENGTH_SHORT).show();
                else if (!editTextNuovaEmail.getText().toString().contains("@") || !editTextNuovaEmail.getText().toString().contains("."))
                    Toast.makeText(CambiaEmailPage.this, "Inserire una mail valida!", Toast.LENGTH_SHORT).show();
                else
                    cambiaEmailController.cambiaEmail(editTextNuovaEmail.getText().toString());
            }
        });
    }

    public void activityPrecedente() {
        super.onBackPressed();
    }
}
