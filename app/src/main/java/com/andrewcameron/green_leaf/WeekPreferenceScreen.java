package com.andrewcameron.green_leaf;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class WeekPreferenceScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_week_preference_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button returnToProfile = (Button) findViewById(R.id.return_to_profile);
        final Button confirmPreferences = (Button) findViewById(R.id.confirm_preferences);

        returnToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(WeekPreferenceScreen.this, ProfileScreenActivity.class);
                startActivity(myIntent);
            }
        });

        confirmPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(WeekPreferenceScreen.this, ProfileScreenActivity.class);
                startActivity(myIntent);
            }
        });
    }

}
