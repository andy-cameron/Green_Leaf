package com.andrewcameron.green_leaf;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mOrganisation;
    private EditText mFirstName;
    private EditText mLastName;
    private String uID;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        //  Views
        mEmailField = findViewById(R.id.email_input);
        mPasswordField = findViewById(R.id.password_input);
        mOrganisation = findViewById(R.id.organisation_input);
        mFirstName = findViewById(R.id.first_name_input);
        mLastName = findViewById(R.id.last_name_input);

        //  Buttons
        findViewById(R.id.register_confirm).setOnClickListener(this);
        findViewById(R.id.cancel_registration).setOnClickListener(this);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
    // [END on_start_check_user]

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);


        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterScreenActivity.this, "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();

                            createDBProfile();

                            Intent myIntent = new Intent(RegisterScreenActivity.this, LoginScreenActivity.class);
                            startActivity(myIntent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterScreenActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void cancelRegistration () {
        Intent myIntent = new Intent(RegisterScreenActivity.this, LoginScreenActivity.class);
        startActivity(myIntent);
    }

    private void createDBProfile () {

        long starterLeaves = 1;
        long starterPlants = 0;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uID = user.getUid();
        }

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Profiles");

        DatabaseReference newProfile = ref.child(uID);
        newProfile.setValue(new UserProfile(mEmailField.getText().toString(), mFirstName.getText().toString(), mLastName.getText().toString(), mOrganisation.getText().toString(), starterLeaves, starterLeaves, starterPlants));
        newProfile.child("weekPreferences").setValue(new UserPreferences(false, false, false, false, false, null, null, 1));
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.register_confirm) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.cancel_registration) {
            cancelRegistration();
        }
    }
}
