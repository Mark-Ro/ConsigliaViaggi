package com.consigliaviaggi.GUI;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.consigliaviaggi.Controller.LoginController;
import com.consigliaviaggi.R;

public class LoginPage extends AppCompatActivity {

    private EditText editTextUsername,editTextPassword;
    private Button bottoneAccedi,bottoneRecuperaPassword,bottoneRegistrati,bottoneHome,bottoneMappa;

    private LoginController loginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        Intent intent = getIntent();

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        bottoneAccedi = findViewById(R.id.bottoneAccedi);
        bottoneRecuperaPassword = findViewById(R.id.bottoneRecuperaPassword);
        bottoneRegistrati = findViewById(R.id.bottoneRegistrati);
        bottoneHome = findViewById(R.id.bottoneHome);
        bottoneMappa = findViewById(R.id.bottoneMappa);

        bottoneAccedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextUsername.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty())
                    Toast.makeText(LoginPage.this, "Riempire i campi!", Toast.LENGTH_SHORT).show();
                else {
                    loginController = new LoginController(LoginPage.this, editTextUsername.getText().toString(), editTextPassword.getText().toString());
                    loginController.openLoadingDialog(LoginPage.this);
                    loginController.effettuaLogin();
                }
            }
        });

        bottoneHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginController.openHomePage();
            }
        });

        bottoneRegistrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginController = new LoginController(LoginPage.this);
                loginController.openRegistrazionePage();
            }
        });

        bottoneRecuperaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginController = new LoginController(LoginPage.this);
                loginController.openRecuperaPasswordPage();
            }
        });
    }
}
