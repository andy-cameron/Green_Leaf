package com.andrewcameron.green_leaf;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InformationScreenActivity extends AppCompatActivity {

    private static final String TAG = "";

    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private String uID;

    private TextView profileName;
    private EditText profileEmail;
    private EditText profileOrganisation;
    private TextView profileLeaves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_screen);


        Button returnToProfileScreen = (Button) findViewById(R.id.return_to_profile);
        Button editEmail = (Button) findViewById(R.id.edit_email_button);
        profileName = findViewById(R.id.profile_name);
        profileEmail = findViewById(R.id.profile_email);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            uID = mUser.getUid();
        }

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Profiles").child(uID);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Setting name field
                String firstName = (String) dataSnapshot.child("firstName").getValue();
                String lastName = (String) dataSnapshot.child("lastName").getValue();
                profileName.setText(firstName + " " + lastName);

                //Setting Email field
                String email = (String) dataSnapshot.child("email").getValue();
                profileEmail.setHint(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        returnToProfileScreen.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(InformationScreenActivity.this, ProfileScreenActivity.class);
                startActivity(myIntent);
            }
        }));

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.updateEmail(profileEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                            mDatabase.child("email").setValue(profileEmail.getText().toString());
                        }
                    }
                });
            }
        });
    }
}
