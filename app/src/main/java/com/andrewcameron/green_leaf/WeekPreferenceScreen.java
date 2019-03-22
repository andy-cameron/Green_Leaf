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
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WeekPreferenceScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String uID;
    private ToggleButton mondayChecked;
    private ToggleButton tuesdayChecked;
    private ToggleButton wednesdayChecked;
    private ToggleButton thursdayChecked;
    private ToggleButton fridayChecked;

    //Prompt
    Dialog leavesConfirmedPrompt;
    Dialog daysConfirmPrompt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_week_preference_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        leavesConfirmedPrompt = new Dialog(this);
        daysConfirmPrompt = new Dialog(this);

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
                ShowLeavesPrompt(v);
            }
        });
    }

    public void ShowLeavesPrompt(View v) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uID = user.getUid();
        }
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference profileRef = database.getReference();

        profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String weekIndex = (String) dataSnapshot.child("Configuration").child("current_week_index").getValue();
                String userWeekIndex = (String) dataSnapshot.child("Profiles").child(uID).child("weekPreferences").child("recentWeekIndexSubmitted").getValue();

                if (weekIndex.equals(userWeekIndex)) {
                    daysConfirmPrompt.setContentView(R.layout.confirm_days_prompt);

                    Button returnBtn;
                    returnBtn = (Button) daysConfirmPrompt.findViewById(R.id.button_return_prompt);
                    returnBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            daysConfirmPrompt.dismiss();
                        }
                    });
                    daysConfirmPrompt.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    daysConfirmPrompt.show();
                } else {
                    leavesConfirmedPrompt.setContentView(R.layout.confirm_leaves_prompt);

                    Button returnBtn;
                    returnBtn = (Button) leavesConfirmedPrompt.findViewById(R.id.button_return_prompt);
                    returnBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            leavesConfirmedPrompt.dismiss();
                        }
                    });
                    leavesConfirmedPrompt.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    leavesConfirmedPrompt.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void rewardingLeaves () {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setPreferences () {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uID = user.getUid();
        }
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference configRef = database.getReference();

        configRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Obtaining the configuration of the current week
                String weekIndex = (String) dataSnapshot.child("Configuration").child("current_week_index").getValue();
                String weekDate = (String) dataSnapshot.child("Configuration").child("current_week").getValue();
                //Obtaining the value of the user's current week
                String userWeekIndex = (String) dataSnapshot.child("Profiles").child(uID).child("weekPreferences").child("recentWeekIndexSubmitted").getValue();
                //Obtaining user's number of Leaves (Total and current)
                long currentNumberOfLeaves = (long) dataSnapshot.child("Profiles").child(uID).child("currentNumberOfLeaves").getValue();
                long totalNumberOfLeaves = (long) dataSnapshot.child("Profiles").child(uID).child("totalNumberOfLeaves").getValue();
                long numberOfPlants = (long) dataSnapshot.child("Profiles").child(uID).child("plants").getValue();

                if (weekIndex.equals(userWeekIndex)) {
                    DatabaseReference ref = database.getReference("Profiles").child(uID);
                    DatabaseReference newPreferencesField = ref.child("weekPreferences");
                    newPreferencesField.setValue(new UserPreferences(mondayChecked.isChecked(),tuesdayChecked.isChecked(),wednesdayChecked.isChecked(),thursdayChecked.isChecked(),fridayChecked.isChecked(), weekDate, weekIndex));
                } else {
                    DatabaseReference ref = database.getReference("Profiles").child(uID);
                    DatabaseReference newPreferencesField = ref.child("weekPreferences");
                    DatabaseReference updateCurrentLeaves = ref.child("currentNumberOfLeaves");
                    DatabaseReference updateTotalLeaves = ref.child("totalNumberOfLeaves");
                    DatabaseReference updatePlants = ref.child("plants");

                    if (currentNumberOfLeaves == 20) {
                        newPreferencesField.setValue(new UserPreferences(mondayChecked.isChecked(),tuesdayChecked.isChecked(),wednesdayChecked.isChecked(),thursdayChecked.isChecked(),fridayChecked.isChecked(), weekDate, weekIndex));
                        updateCurrentLeaves.setValue(1);
                        updateTotalLeaves.setValue(totalNumberOfLeaves + 1);
                        updatePlants.setValue(numberOfPlants + 1);
                    } else {
                        newPreferencesField.setValue(new UserPreferences(mondayChecked.isChecked(),tuesdayChecked.isChecked(),wednesdayChecked.isChecked(),thursdayChecked.isChecked(),fridayChecked.isChecked(), weekDate, weekIndex));
                        updateCurrentLeaves.setValue(currentNumberOfLeaves + 1);
                        updateTotalLeaves.setValue(totalNumberOfLeaves + 1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
