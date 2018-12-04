package com.andrewcameron.green_leaf;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class GreenLeafIntroductionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_leaf_introduction);

        Button buttonLetsGo = (Button) findViewById(R.id.button_lets_go);

        buttonLetsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(GreenLeafIntroductionActivity.this, LoginScreenActivity.class);
                startActivity(myIntent);
            }
        });
    }

}
