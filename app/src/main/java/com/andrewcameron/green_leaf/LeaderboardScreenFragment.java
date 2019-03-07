package com.andrewcameron.green_leaf;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardScreenFragment extends Fragment {

    private List<UserProfile> mUserProfileList;
    private RecyclerView mRecyclerView;
    private OnFinishedCLickListener mOnFinishedClickListener;
    private String uID;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    private TextView numberOfUserLeaves;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_leaderboard_screen, parent, false);

        return view;
    }

    public static LeaderboardScreenFragment newInstance(Bundle extra) {
        LeaderboardScreenFragment fragment = new LeaderboardScreenFragment();
        fragment.setArguments(extra);
        return fragment;
    }

    public void setOnFinishedClickListener(OnFinishedCLickListener onFinishedClickListener) {
        mOnFinishedClickListener = onFinishedClickListener;
    }

    public interface OnFinishedCLickListener {
        void OnFinishedClicked();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonReturn = view.findViewById(R.id.return_to_profile);
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFinishedClickListener.OnFinishedClicked();
            }
        });

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) mLayoutManager).setReverseLayout(true);
        ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        prepareLeaderboardProfileData();

        //---------------------------------------------------------
        numberOfUserLeaves = view.findViewById(R.id.number_leaves_plant);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            uID = mUser.getUid();
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("Profiles");
        mDatabase.orderByChild("numberOfLeaves");

        mDatabase.child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userProfileName = (String) dataSnapshot.child("firstName").getValue();
                String userProfileLastName = (String) dataSnapshot.child("lastName").getValue();
                String userProfileEmail = (String) dataSnapshot.child("email").getValue();
                String userProfileOrganisation = (String) dataSnapshot.child("organisation").getValue();
                Long userProfileLeaves = (Long) dataSnapshot.child("numberOfLeaves").getValue();
                numberOfUserLeaves.setText(userProfileLeaves.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void prepareLeaderboardProfileData() {
        new GetDataFromFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        
        //Read From Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Profiles");
        //Read & Order from Database
        Query mostLeavesOrdered = myRef.orderByChild("numberOfLeaves");

        mostLeavesOrdered.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserProfileList = new ArrayList<>();

                for (DataSnapshot profileSnapshot: dataSnapshot.getChildren()) {
                    String email = (String) profileSnapshot.child("email").getValue();
                    String firstName = (String) profileSnapshot.child("firstName").getValue();
                    String lastName = (String) profileSnapshot.child("lastName").getValue();
                    String organisation = (String) profileSnapshot.child("organisation").getValue();
                    Long currentNumberOfLeaves = (Long) profileSnapshot.child("currentNumberOfLeaves").getValue();
                    Long totalNumberOfLeaves = (Long) profileSnapshot.child("totalNumberOfLeaves").getValue();
                    Long rewards = (Long) profileSnapshot.child("rewards").getValue();

                    UserProfile userProfile = new UserProfile(email, firstName, lastName, organisation, currentNumberOfLeaves, totalNumberOfLeaves, rewards);
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
