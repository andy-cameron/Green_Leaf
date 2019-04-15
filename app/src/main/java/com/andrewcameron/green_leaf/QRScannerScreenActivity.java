package com.andrewcameron.green_leaf;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QRScannerScreenActivity extends AppCompatActivity {

    private String uID;
    private EditText mCode1, mCode2, mCode3, mCode4, mCode5, mCode6;
    private long codeEnteredLong;


    //Prompts
    Dialog codeSuccessPrompt;
    Dialog codeFailPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner_screen);
        codeSuccessPrompt = new Dialog(this);
        codeFailPrompt = new Dialog(this);

        final Button returnToProfile = (Button) findViewById(R.id.return_to_profile);
        final Button verifyCode = (Button) findViewById(R.id.verify_code);
        mCode1 = (EditText) findViewById(R.id.code_1);
        mCode2 = (EditText) findViewById(R.id.code_2);
        mCode3 = (EditText) findViewById(R.id.code_3);
        mCode4 = (EditText) findViewById(R.id.code_4);
        mCode5 = (EditText) findViewById(R.id.code_5);
        mCode6 = (EditText) findViewById(R.id.code_6);

        returnToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(QRScannerScreenActivity.this, ProfileScreenActivity.class);
                startActivity(myIntent);
            }
        });

        verifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCodePrompt(v);
            }
        });
    }

    public void ShowCodePrompt(View v) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uID = user.getUid();
        }
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference fbDatabase = database.getReference();

        fbDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String codeEntered = mCode1.getText().toString() + mCode2.getText().toString() + mCode3.getText().toString() + mCode4.getText().toString() + mCode5.getText().toString() + mCode6.getText().toString();
                try {
                    codeEnteredLong = Long.parseLong(codeEntered);
                } catch (Exception e) {
                    codeFailPrompt.setContentView(R.layout.bonus_leaf_prompt_fail);

                    Button returnBtn;
                    returnBtn = (Button) codeFailPrompt.findViewById(R.id.button_return_prompt);
                    returnBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            codeFailPrompt.dismiss();
                        }
                    });
                    codeFailPrompt.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    codeFailPrompt.show();
                }

                long bonusCode = (long) dataSnapshot.child("Configuration").child("bonus_code").getValue();
                long userBonusCode = (long) dataSnapshot.child("Profiles").child(uID).child("weekPreferences").child("bonusCode").getValue();

                // User has entered correct code
                if (codeEnteredLong == bonusCode) {
                    // User Already Submitted Code
                    if (bonusCode == userBonusCode) {
                        codeFailPrompt.setContentView(R.layout.bonus_leaf_prompt_fail);

                        Button returnBtn;
                        returnBtn = (Button) codeFailPrompt.findViewById(R.id.button_return_prompt);
                        returnBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                codeFailPrompt.dismiss();
                            }
                        });
                        codeFailPrompt.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        codeFailPrompt.show();
                    } else {
                        codeSuccessPrompt.setContentView(R.layout.bonus_leaf_prompt_success);

                        fbDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //Obtaining user's number of Leaves (Total and current)
                                long bonusCode = (long) dataSnapshot.child("Configuration").child("bonus_code").getValue();
                                long currentNumberOfLeaves = (long) dataSnapshot.child("Profiles").child(uID).child("currentNumberOfLeaves").getValue();
                                long totalNumberOfLeaves = (long) dataSnapshot.child("Profiles").child(uID).child("totalNumberOfLeaves").getValue();
                                long numberOfPlants = (long) dataSnapshot.child("Profiles").child(uID).child("plants").getValue();

                                DatabaseReference ref = database.getReference("Profiles").child(uID);
                                DatabaseReference updateCurrentLeaves = ref.child("currentNumberOfLeaves");
                                DatabaseReference updateTotalLeaves = ref.child("totalNumberOfLeaves");
                                DatabaseReference updatePlants = ref.child("plants");
                                DatabaseReference updateBonusCode = ref.child("weekPreferences").child("bonusCode");
                                updateBonusCode.setValue(bonusCode);

                                if (currentNumberOfLeaves == 20) {
                                    updateCurrentLeaves.setValue(1);
                                    updateTotalLeaves.setValue(totalNumberOfLeaves + 1);
                                    updatePlants.setValue(numberOfPlants + 1);
                                } else {
                                    updateCurrentLeaves.setValue(currentNumberOfLeaves + 1);
                                    updateTotalLeaves.setValue(totalNumberOfLeaves + 1);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Button returnBtn;
                        returnBtn = (Button) codeSuccessPrompt.findViewById(R.id.button_return_prompt);
                        returnBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                codeSuccessPrompt.dismiss();
                            }
                        });
                        codeSuccessPrompt.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        codeSuccessPrompt.show();
                    }
                } else {
                    codeFailPrompt.setContentView(R.layout.bonus_leaf_prompt_fail);

                    Button returnBtn;
                    returnBtn = (Button) codeFailPrompt.findViewById(R.id.button_return_prompt);
                    returnBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            codeFailPrompt.dismiss();
                        }
                    });
                    codeFailPrompt.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    codeFailPrompt.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
