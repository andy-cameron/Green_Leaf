package com.andrewcameron.green_leaf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterScreenActivity extends AppCompatActivity {

    private EditText firstName;
    private EditText surName;
    private EditText organisation;
    private EditText emailInput;
    private EditText passwordInput;
    private String uID;
    private String email;


    private FirebaseAuth mAuth;

    private View.OnClickListener confirm_regsiter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        emailInput = findViewById(R.id.email_input);
        firstName = findViewById(R.id.first_name_input);
        surName = findViewById(R.id.last_name_input);
        organisation = findViewById(R.id.organisation_input);
        passwordInput = findViewById(R.id.password_input);

        mAuth = FirebaseAuth.getInstance();

        final Button registerConfirm = findViewById(R.id.register_confirm);
        Button registerCancel = (Button) findViewById(R.id.cancel_registration);

        registerCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(RegisterScreenActivity.this, LoginScreenActivity.class);
                startActivity(myIntent);
            }
        });

        registerConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                register_user(v);
            }
        });

        //Create intent for cancel button
    }

    public void register_user(View v) {
        final ProgressDialog progressDialog = ProgressDialog.show(RegisterScreenActivity.this, "Please wait...", "Processing...", true);
        mAuth.createUserWithEmailAndPassword(emailInput.getText().toString(), passwordInput.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()) {
                    Toast.makeText(RegisterScreenActivity.this, "Registration successful", Toast.LENGTH_LONG).show();
                    //[BEGIN] NEW DB INFORMATION
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        uID = user.getUid();
                        email = user.getEmail();
                    }

                    EditText firstName = (EditText) findViewById(R.id.first_name_input);
                    EditText lastName = (EditText) findViewById(R.id.last_name_input);
                    String strFirstName = firstName.getText().toString();
                    String strLastName = lastName.getText().toString();
                    String fullName = strFirstName + " " + strLastName;

                    EditText organisation = (EditText) findViewById(R.id.organisation_input);
                    String strOrganisation = organisation.getText().toString();
                    long leaves = 1;

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("users");

                    DatabaseReference newUserProfileRef = ref.child(uID);
                    newUserProfileRef.setValue(new UserProfile(email, fullName, strOrganisation, leaves));

                    Intent i = new Intent(RegisterScreenActivity.this, LoginScreenActivity.class);
                    startActivity(i);
                } else {
                    Log.e("ERROR", task.getException().toString());
                    Toast.makeText(RegisterScreenActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
