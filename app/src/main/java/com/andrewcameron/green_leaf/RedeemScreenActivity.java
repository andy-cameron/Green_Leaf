package com.andrewcameron.green_leaf;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class RedeemScreenActivity extends AppCompatActivity {

    private String uID;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private Long userPlantLeaves;
//    private Button btnReturn;

    //Prompt
    Dialog redeemLeavesPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        redeemLeavesPrompt = new Dialog(this);
//
//        mUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (mUser != null) {
//            uID = mUser.getUid();
//        }

//        mDatabase = FirebaseDatabase.getInstance().getReference("Profiles");
//        //Getting basic User Info
//        mDatabase.child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                userPlantLeaves = (Long) dataSnapshot.child("plants").getValue();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        Button redeemItem1 = (Button) findViewById(R.id.redeem_item_1);
        Button redeemItem2 = (Button) findViewById(R.id.redeem_item_2);
        Button redeemItem3 = (Button) findViewById(R.id.redeem_item_3);
        Button returnToProfile = (Button) findViewById(R.id.return_to_profile);

        returnToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(RedeemScreenActivity.this, ProfileScreenActivity.class);
                startActivity(myIntent);
            }
        });

        redeemItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPrompt(v);
            }
        });

        redeemItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView redeemValuePrompt = (TextView) findViewById(R.id.redeem_reward_value_prompt);
                long redeemValue = 5;

                redeemValuePrompt.setText(redeemValue + " Plants");
            }
        });
    }

    public void ShowPrompt (View v) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uID = user.getUid();
        }

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Profiles").child(uID);
        DatabaseReference updatePlants = ref.child("plants");

        updatePlants.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long numberOfPlants = (long) dataSnapshot.getValue();
                long redeemValue = 3;

                if (numberOfPlants >= redeemValue) {
                    redeemLeavesPrompt.setContentView(R.layout.redeem_confirm_prompt);
                    Button btnReturn, btnConfirmPurchase;
                    btnReturn = (Button) redeemLeavesPrompt.findViewById(R.id.button_return_prompt_redeem);
                    btnConfirmPurchase = (Button) redeemLeavesPrompt.findViewById(R.id.confirm_purchase);

                    redeemLeavesPrompt.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    redeemLeavesPrompt.show();

                    btnReturn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            redeemLeavesPrompt.dismiss();
                        }
                    });

                    btnConfirmPurchase.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RedeemTransaction();
                            redeemLeavesPrompt.dismiss();
                        }
                    });

                } else {
                    redeemLeavesPrompt.setContentView(R.layout.redeem_invalid_prompt);
                    redeemLeavesPrompt.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Button btnReturn;
                    btnReturn = (Button) redeemLeavesPrompt.findViewById(R.id.button_return_prompt);
                    redeemLeavesPrompt.show();

                    btnReturn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            redeemLeavesPrompt.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void RedeemTransaction () {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uID = user.getUid();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Profiles").child(uID);
        final DatabaseReference updatePlants = ref.child("plants");

        updatePlants.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long numberOfPlants = (long) dataSnapshot.getValue();
                long redeemValue = 3;

                updatePlants.setValue(numberOfPlants - redeemValue);
                redeemLeavesPrompt.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void ConfirmRedeemPrompt () {
//        redeemLeavesPrompt.setContentView(R.layout.redeem_confirm_prompt);
//        redeemLeavesPrompt.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        redeemLeavesPrompt.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uID = user.getUid();
        }

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Profiles").child(uID);
        final DatabaseReference updatePlants = ref.child("plants");

        updatePlants.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final long numberOfPlants = (long) dataSnapshot.getValue();
                //Button Return
                Button btnReturn = (Button) findViewById(R.id.button_return_prompt_redeem);
                btnReturn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        redeemLeavesPrompt.dismiss();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}