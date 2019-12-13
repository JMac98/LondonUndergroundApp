package com.example.londonundergroundapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton jubileeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jubileeButton = findViewById(R.id.jubileeButton);

        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.jubileeButton:
                        Intent intentJubileeActivity = new Intent(MainActivity.this, JubileeLineActivity.class);
                        startActivity(intentJubileeActivity);
                        break;
                    }
                }
            };

        jubileeButton.setOnClickListener(listener);



    }
}










