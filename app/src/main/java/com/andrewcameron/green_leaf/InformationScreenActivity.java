package com.andrewcameron.green_leaf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class InformationScreenActivity extends AppCompatActivity {

    private static final String TAG = "";

    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private String uID;

    private TextView profileName;
    private TextView profileEmail;
    private TextView profileOrganisation;
    private TextView profileLeaves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_screen);

        Button returnToProfileScreen = (Button) findViewById(R.id.return_to_profile);

        returnToProfileScreen.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(InformationScreenActivity.this, ProfileScreenActivity.class);
                startActivity(myIntent);
            }
        }));
    }
}
