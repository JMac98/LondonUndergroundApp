package com.example.londonundergroundapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class JubileeLineActivity extends AppCompatActivity {

    Button canningtownButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jubilee_line);

        canningtownButton = findViewById(R.id.canningtownButton);

        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.canningtownButton:
                        Intent intentJubileeActivity = new Intent(JubileeLineActivity.this, CanningTownActivity.class);
                        startActivity(intentJubileeActivity);
                        break;
                }
            }
        };

        canningtownButton.setOnClickListener(listener);
    }
}
