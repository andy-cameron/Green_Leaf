package com.andrewcameron.green_leaf;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardScreenActivity extends AppCompatActivity {

    private List<UserProfile> mUserProfileList;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_screen);

        mRecyclerView = findViewById(R.id.recycler_view);

        Button returnToMenuButton = (Button) findViewById(R.id.return_to_profile);
        returnToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(LeaderboardScreenActivity.this, ProfileScreenActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void prepareUserProfileData() {
        new GetDataFromFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        //Read From Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Profiles");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserProfileList = new ArrayList<>();

                for (DataSnapshot profileSnapshot: dataSnapshot.getChildren()) {
                    String firstName = (String) profileSnapshot.child("firstName").getValue();
                    String lastName = (String) profileSnapshot.child("lastName").getValue();
                    String email = (String) profileSnapshot.child("email").getValue();
                    Long numberOfLeaves = (Long) profileSnapshot.child("numberOfLeaves").getValue();
                    String organisation = (String) profileSnapshot.child("organisation").getValue();

                    UserProfile userProfile = new UserProfile(email, firstName, lastName, organisation, numberOfLeaves);
                    mUserProfileList.add(userProfile);
                }
                mRecyclerView.setAdapter(new LeaderboardAdapter(mUserProfileList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Failed to read value." + databaseError.toException());
            }
        });
    }

    private class GetDataFromFirebase extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }
}
