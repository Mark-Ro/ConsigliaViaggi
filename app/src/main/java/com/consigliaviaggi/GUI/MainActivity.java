package com.consigliaviaggi.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.consigliaviaggi.Controller.MainActivityController;
import com.consigliaviaggi.R;

public class MainActivity extends AppCompatActivity {
    private Button bottoneRicerca,bottoneProfilo;
    private MainActivityController mainActivityController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityController = new MainActivityController(MainActivity.this);

        bottoneRicerca = (Button) findViewById(R.id.bottoneRicerca);

        bottoneProfilo = findViewById(R.id.bottoneProfilo);
        bottoneProfilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivityController.openProfiloPage();
            }
        });
    }


}
