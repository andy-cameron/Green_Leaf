package com.andrewcameron.green_leaf;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileScreenActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_profile_screen);

        profileName = (TextView) findViewById(R.id.users_name_display);
        profileOrganisation = (TextView) findViewById(R.id.user_organisation_display);
        profileLeaves = (TextView) findViewById(R.id.user_leaves_display);

        Button goToPreferences = (Button) findViewById(R.id.go_to_preferences);
        Button goToLeaderboard = (Button) findViewById(R.id.go_to_leaderboard);
        Button goToInformation = (Button) findViewById(R.id.go_to_user_information);
        Button goToScanQR = (Button) findViewById(R.id.go_to_scan_qr_code);
        Button returnToLogin = (Button) findViewById(R.id.return_to_login);

        goToPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent( ProfileScreenActivity.this, WeekPreferenceScreen.class);
                startActivity(myIntent);
            }
        });

        goToScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ProfileScreenActivity.this, QRScannerScreenActivity.class);
                startActivity(myIntent);
            }
        });

        goToLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent( ProfileScreenActivity.this, LeaderboardScreenActivity.class);
                startActivity(myIntent);
            }
        });

        goToInformation.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ProfileScreenActivity.this, InformationScreenActivity.class);
                startActivity(myIntent);
            }
        }));

        returnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ProfileScreenActivity.this, LoginScreenActivity.class);
                startActivity(myIntent);
            }
        });

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            uID = mUser.getUid();
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("Profiles");

        mDatabase.child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userProfileName = (String) dataSnapshot.child("firstName").getValue();
                String userProfileLastName = (String) dataSnapshot.child("lastName").getValue();
                String userProfileEmail = (String) dataSnapshot.child("email").getValue();
                String userProfileOrganisation = (String) dataSnapshot.child("organisation").getValue();
                Long userProfileLeaves = (Long) dataSnapshot.child("numberOfLeaves").getValue();
                profileName.setText("/ " + userProfileName + " " + userProfileLastName);
//                profileEmail.setText("/ Email: " + userProfileEmail);
                profileOrganisation.setText("/ " +userProfileOrganisation);
                profileLeaves.setText(userProfileLeaves.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
