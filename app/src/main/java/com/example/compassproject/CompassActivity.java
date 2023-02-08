package com.example.compassproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CompassActivity extends AppCompatActivity {

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