package com.example.compassproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class CompassActivity extends AppCompatActivity {


    /*
     * TODO: Update locations and angles for compass_N, compass_E, compass_S, compass_W
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

    }

    public void onAddLocationClicked(View view) {
        Intent intent = new Intent(this, NewLocationActivity.class);
        startActivity(intent);
    }
}