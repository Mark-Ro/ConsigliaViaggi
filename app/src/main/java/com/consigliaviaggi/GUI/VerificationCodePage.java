package com.consigliaviaggi.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.consigliaviaggi.Controller.VerificationCodeController;
import com.consigliaviaggi.R;

public class VerificationCodePage extends AppCompatActivity {

    private EditText editTextCodice;
    private Button bottoneVerificaCodice,bottoneReinviaCodice;

    private VerificationCodeController verificationCodeController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code_page);

        Intent intent = getIntent();
        final String username = intent.getStringExtra("Username");
        final String password = intent.getStringExtra("Password");
        final String activityChiamante=intent.getStringExtra("ActivityChiamante");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        editTextCodice = findViewById(R.id.editTextCodice);
        bottoneVerificaCodice = findViewById(R.id.bottoneVerificaCodice);
        bottoneReinviaCodice = findViewById(R.id.bottoneReinviaCodice);

        verificationCodeController = new VerificationCodeController(VerificationCodePage.this,username,password);

        bottoneVerificaCodice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextCodice.getText().toString().isEmpty())
                    Toast.makeText(VerificationCodePage.this, "Riempire i campi!", Toast.LENGTH_SHORT).show();
                else if (editTextCodice.getText().toString().length()!=6)
                    Toast.makeText(VerificationCodePage.this, "Verifica non riuscita: codice non corretto!", Toast.LENGTH_SHORT).show();
                else if (!activityChiamante.equals("CambiaEmail")) {
                    verificationCodeController.openLoadingDialog(VerificationCodePage.this);
                    verificationCodeController.verificaCodice(editTextCodice.getText().toString());
                }
                else {
                    verificationCodeController.openLoadingDialog(VerificationCodePage.this);
                    verificationCodeController.verificaCodiceEmail(editTextCodice.getText().toString());
                }
            }
        });

        bottoneReinviaCodice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activityChiamante.equals("CambiaEmail"))
                    verificationCodeController.effettuaResend();
                else
                    verificationCodeController.effettuaResendCambiaEmail();
            }
        });

        if (activityChiamante.equals("Login"))
            bottoneReinviaCodice.performClick();
    }
}
