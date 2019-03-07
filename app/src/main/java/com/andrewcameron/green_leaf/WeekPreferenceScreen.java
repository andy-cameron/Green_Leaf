package com.andrewcameron.green_leaf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WeekPreferenceScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String uID;
    private ToggleButton mondayChecked;
    private ToggleButton tuesdayChecked;
    private ToggleButton wednesdayChecked;
    private ToggleButton thursdayChecked;
    private ToggleButton fridayChecked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_week_preference_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button returnToProfile = (Button) findViewById(R.id.return_to_profile);
        final Button confirmPreferences = (Button) findViewById(R.id.confirm_preferences);
        mondayChecked = (ToggleButton) findViewById(R.id.day_1_monday);
        tuesdayChecked = (ToggleButton) findViewById(R.id.day_2_tuesday);
        wednesdayChecked = (ToggleButton) findViewById(R.id.day_3_wednesday);
        thursdayChecked = (ToggleButton) findViewById(R.id.day_4_thursday);
        fridayChecked = (ToggleButton) findViewById(R.id.day_5_friday);

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

                setPreferences();
                Intent myIntent = new Intent(WeekPreferenceScreen.this, ProfileScreenActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void setPreferences () {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uID = user.getUid();
        }

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Profiles").child(uID);

        DatabaseReference newPreferencesField = ref.child("weekPreferences");
        newPreferencesField.setValue(new UserPreferences(mondayChecked.isChecked(),tuesdayChecked.isChecked(),wednesdayChecked.isChecked(),thursdayChecked.isChecked(),fridayChecked.isChecked()));
    }

}
