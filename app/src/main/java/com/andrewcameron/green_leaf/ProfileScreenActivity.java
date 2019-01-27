package com.andrewcameron.green_leaf;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
//        profileEmail = (TextView) findViewById(R.id.user_email_display);
        profileOrganisation = (TextView) findViewById(R.id.user_organisation_display);
        profileLeaves = (TextView) findViewById(R.id.user_leaves_display);

        Button returnToLogin = (Button) findViewById(R.id.return_to_login);

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
