package com.example.compassproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * This class is main activity where it handle request permission
 * from user at runtime to use location, and request internet
 * permission. It will direct to SetNameActivity or CompassActivity
 * depends on user decision perform on UI.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * This method is used to request permission to use location,
     * and permission for internet.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.
     *                              <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       //request permission from user at runtime to use location
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

        //request internet permission
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.INTERNET}, 200);
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.ACCESS_NETWORK_STATE}, 200);
        }

        UserInfo u1 = new UserInfo(getSharedPreferences(getString(R.string.saveUserInfo), MODE_PRIVATE));
        if(u1.hasName())
        {
            Intent intent = new Intent(this,CompassActivity.class);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(this,SetNameActivity.class);
            startActivity(intent);
        }
    }
}