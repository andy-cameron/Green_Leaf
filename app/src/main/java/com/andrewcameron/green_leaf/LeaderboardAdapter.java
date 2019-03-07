package com.andrewcameron.green_leaf;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.MyViewHolder> {

    private List<UserProfile> mUserProfileList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, numberOfLeaves, ranking;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.users_name_display);
            numberOfLeaves = (TextView) view.findViewById(R.id.number_leaves_display_card);
        }
    }

    public LeaderboardAdapter(List<UserProfile> userProfileList) {
        mUserProfileList = userProfileList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        UserProfile userProfileList = mUserProfileList.get(position);
        holder.name.setText(userProfileList.getFirstName() + " " + userProfileList.getLastName());
        holder.numberOfLeaves.setText(userProfileList.getCurrentNumberOfLeaves().toString());
    }

    @Override
    public int getItemCount() {
        return mUserProfileList.size();
    }
}