package com.consigliaviaggi.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.consigliaviaggi.Controller.RegistrazioneController;
import com.consigliaviaggi.R;

public class RegistrazionePage extends AppCompatActivity {

    private EditText editTextNome,editTextCognome,editTextEmail,editTextNickname,editTextPassword,editTextConfermaPassword;
    private CheckBox checkBox;
    private Button bottoneConferma,bottoneAnnulla;

    private RegistrazioneController registrazioneController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione_page);

        Intent intent = getIntent();

        editTextNome = findViewById(R.id.editTextNome);
        editTextCognome = findViewById(R.id.editTextCognome);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextNickname = findViewById(R.id.editTextNickname);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfermaPassword = findViewById(R.id.editTextConfermaPassword);
        checkBox = findViewById(R.id.checkBox);
        bottoneConferma = findViewById(R.id.bottoneConferma);
        bottoneAnnulla = findViewById(R.id.bottoneAnnulla);

        registrazioneController = new RegistrazioneController(this,RegistrazionePage.this);

        bottoneConferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextNome.getText().toString().isEmpty() || isBlank(editTextNome.getText().toString()) || editTextCognome.getText().toString().isEmpty() || isBlank(editTextCognome.getText().toString()) || editTextEmail.getText().toString().isEmpty() || editTextNickname.getText().toString().isEmpty() || isBlank(editTextNickname.getText().toString()) || editTextPassword.getText().toString().isEmpty() || isBlank(editTextPassword.getText().toString()) || editTextConfermaPassword.getText().toString().isEmpty() || isBlank(editTextConfermaPassword.getText().toString()) )
                    Toast.makeText(RegistrazionePage.this, "Riempire i campi!", Toast.LENGTH_SHORT).show();
                else if (!editTextEmail.getText().toString().contains("@") || !editTextEmail.getText().toString().contains("."))
                    Toast.makeText(RegistrazionePage.this, "Inserire una email valida!", Toast.LENGTH_SHORT).show();
                else if (!editTextPassword.getText().toString().equals(editTextConfermaPassword.getText().toString()))
                    Toast.makeText(RegistrazionePage.this, "Le password non coincidono!", Toast.LENGTH_SHORT).show();
                else if (editTextPassword.getText().toString().length() < 8)
                    Toast.makeText(RegistrazionePage.this, "Password troppo corta!", Toast.LENGTH_SHORT).show();
                else {
                    bottoneConferma.setEnabled(false);
                    registrazioneController.openLoadingDialog(RegistrazionePage.this);
                    registrazioneController.effettuaRegistrazione(editTextNome.getText().toString(), editTextCognome.getText().toString(), editTextEmail.getText().toString(), editTextNickname.getText().toString(), editTextPassword.getText().toString(), checkBox.isChecked());
                }
            }
        });

        bottoneAnnulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextNome.setText("");
                editTextCognome.setText("");
                editTextEmail.setText("");
                editTextNickname.setText("");
                editTextPassword.setText("");
                editTextConfermaPassword.setText("");
                checkBox.setChecked(false);
            }
        });

    }

    private boolean isBlank(String string) {
        return string == null || string.trim().length() == 0;
    }

    public void resetGuiButtons() {
        bottoneConferma.setEnabled(true);
    }

}
