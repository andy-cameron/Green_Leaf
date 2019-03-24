package com.andrewcameron.green_leaf;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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
    private EditText currentPassword;
    private EditText newPassword;
    private EditText newPasswordConfirm;
    private TextView profileLeaves;

    private ImageButton showCurrentPassword;
    private ImageButton showNewPassword;
    private ImageButton showNewPasswordConfirm;

    //Prompt
    Dialog emailPrompt;
    Dialog passwordPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_screen);
        emailPrompt = new Dialog(this);
        passwordPrompt = new Dialog(this);

        Button returnToProfileScreen = (Button) findViewById(R.id.return_to_profile);
        Button editEmail = (Button) findViewById(R.id.edit_email_button);
        Button editPassword = (Button) findViewById(R.id.edit_password);
        profileName = findViewById(R.id.profile_name);
        profileEmail = findViewById(R.id.profile_email);
        currentPassword = findViewById(R.id.current_password);
        newPassword = findViewById(R.id.new_password);
        newPasswordConfirm = findViewById(R.id.confirm_password);
        showCurrentPassword = findViewById(R.id.show_current_password);
        showNewPassword = findViewById(R.id.show_new_password);
        showNewPasswordConfirm = findViewById(R.id.show_confirm_new_password);

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
            public void onClick(final View v) {
                mUser.updateEmail(profileEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                            mDatabase.child("email").setValue(profileEmail.getText().toString());
                            String newEmail = profileEmail.getText().toString();
                            profileEmail.setHint(newEmail);
                            profileEmail.setText(null);
                            profileEmail.clearFocus();
                            UpdateEmailPrompt(v);
                        }
                    }
                });
            }
        });

        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (newPassword.getText().toString().equals(newPasswordConfirm.getText().toString())) {
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String userEmail = (String) dataSnapshot.child("email").getValue();
                            AuthCredential credential = EmailAuthProvider.getCredential(userEmail, currentPassword.getText().toString());
                            mUser.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            mUser.updatePassword(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "Password updated");
                                                        currentPassword.setText(null);
                                                        currentPassword.clearFocus();
                                                        newPassword.setText(null);
                                                        newPassword.clearFocus();
                                                        newPasswordConfirm.setText(null);
                                                        newPasswordConfirm.clearFocus();
                                                        UpdatePasswordPrompt(v);
                                                    } else {
                                                        Log.d(TAG, "Error password not updated");
                                                    }
                                                }
                                            });
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    System.out.println("Unsuccessful!!!");
                }
            }
        });

        showCurrentPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });

        showNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });

        showNewPasswordConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPasswordConfirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });
    }

    public void UpdateEmailPrompt(View v) {
        emailPrompt.setContentView(R.layout.email_changed_prompt);

        Button returnBtn;
        returnBtn = (Button) emailPrompt.findViewById(R.id.button_return_prompt);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailPrompt.dismiss();
            }
        });
        emailPrompt.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        emailPrompt.show();
    }

    public void UpdatePasswordPrompt(View v) {
        passwordPrompt.setContentView(R.layout.password_changed_prompt);

        Button returnBtn;
        returnBtn = (Button) passwordPrompt.findViewById(R.id.button_return_prompt);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordPrompt.dismiss();
            }
        });
        passwordPrompt.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        passwordPrompt.show();
    }
}
