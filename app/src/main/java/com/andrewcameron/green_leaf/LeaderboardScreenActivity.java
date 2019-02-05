package com.andrewcameron.green_leaf;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class LeaderboardScreenActivity extends AppCompatActivity implements LeaderboardScreenFragment.OnFinishedCLickListener{

    private static final String LEADERBOARD_FRAGMENT_TAG = "LEADERBOARD_FRAGMENT_TAG";
    public static final String ARG_EXTRA_BUNDLE = "ARG_EXTRA_BUNDLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0,0);
        setContentView(R.layout.activity_leaderboard_screen);

        if (savedInstanceState == null && !isFragmentShown()){
            showFragment();
        }
    }

    private void showFragment() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        LeaderboardScreenFragment fragment = LeaderboardScreenFragment.newInstance(getIntent().getBundleExtra(ARG_EXTRA_BUNDLE));
        fragment.setOnFinishedClickListener(this);

        ft.replace(R.id.fragment_container, fragment, LEADERBOARD_FRAGMENT_TAG);
        ft.commit();
    }

    private boolean isFragmentShown() {
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentByTag(LEADERBOARD_FRAGMENT_TAG);

        return fragment != null;
    }

    public void OnFinishedClicked() {
        Intent myIntent = new Intent(LeaderboardScreenActivity.this, ProfileScreenActivity.class);
        startActivity(myIntent);
    }

}
