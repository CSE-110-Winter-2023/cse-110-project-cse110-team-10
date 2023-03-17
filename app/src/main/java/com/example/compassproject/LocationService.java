package com.example.compassproject;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.SystemClock;

import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Arrays;

/**
    This class is as a Location Service implement for
    Location Listener which handles functions as location
    listener, GPS status.
    functions   LocationService
                RegisterLocationListener
                WithLocationPermission
                onLocationChanged
                satelliteStatusChanged
                unregisterLocationListener
                getLocation
                getGPSFix
                setMockLocationSource
                setMockGPSFixSource
                setMockElapsedTime
                getElapsedTime
 */
public class LocationService implements LocationListener {

    private static final long GPS_UPDATE_INTERVAL = 1000;
    GnssStatus.Callback mGnssStatusCallback;

    private MutableLiveData<Boolean> isGPSFix;
    private Location mLastLocation;
    private long mLastLocationMillis;
    final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };


    // This needs to be more specific than just Activity for location permissions requesting.
    private final AppCompatActivity activity;

    private static LocationService instance;

    private MutableLiveData<Pair<Double, Double>> locationValue;

    private final LocationManager locationManager;

    long elapsedTime;
    boolean mocking = false;

    /**
     * This method is locationService constructor
     * @param activity
     * @return instance
     */
    public static LocationService singleton(AppCompatActivity activity) {
        if (instance == null) {
            instance = new LocationService(activity);
        }
        return instance;
    }


    /**
     * Constructor for LocationService
     *
     * @param activity Context needed to initiate LocationManager
     */
    protected LocationService(AppCompatActivity activity) {
        this.locationValue = new MutableLiveData<>();
        this.isGPSFix = new MutableLiveData<>();
        this.activity = activity;
        this.locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        mGnssStatusCallback = new GnssStatus.Callback() {
            @Override
            public void onSatelliteStatusChanged(GnssStatus status) {
                satelliteStatusChanged();
            }
        };
        // Register sensor listeners
        withLocationPermissions(this::registerLocationListener);

    }

    /**  This will only be called when we for sure have permissions. */
    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    private void registerLocationListener() {
        locationManager.registerGnssStatusCallback(mGnssStatusCallback);
        this.locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0,
                LocationService.this);
    }

    /** Utility method for doing something with location permissions if we have them, and
     *  after asking for them if we don't already.
     * @param action the thing to do that needs permissions.
     */
    private void withLocationPermissions(Runnable action) {
        if (Arrays.stream(REQUIRED_PERMISSIONS).allMatch(perm -> activity.checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED)) {
            // We already have at least one of the location permissions, go ahead!
            action.run();
        } else {
            // We need to ask for permission first.
            // This is the call that requires AppCompatActivity and not just Activity!
            var launcher = activity.registerForActivityResult(new RequestMultiplePermissions(), grants -> {
                // At least one of the values in the Map<String, Boolean> grants needs to be true.
                if (grants.values().stream().noneMatch(isGranted -> isGranted)) {
                    // If you've landed here by denying it, you should grant it manually in settings or wipe data.
                    throw new IllegalStateException("App needs you to grant at least one location permission!");
                }
                // We have permission now, carry on!
                action.run();
            });
            launcher.launch(REQUIRED_PERMISSIONS);
        }
    }

    /**
     * This method is used to set postValue of locationValue
     * to latitude and longitude of location
     * @param location the updated location
     */
    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (location == null) return;

        mLastLocationMillis = SystemClock.elapsedRealtime();

        mLastLocation = location;
        this.locationValue.postValue(new Pair<>(location.getLatitude(), location.getLongitude()));

    }

    /**
     * This method is used to decide when the satellite status change
     */
    public void satelliteStatusChanged() {
        // if mock is true
        if (!mocking) {
            elapsedTime = SystemClock.elapsedRealtime() - mLastLocationMillis;
            if (mLastLocation != null)
                isGPSFix.postValue(elapsedTime < 60000);
        }
        else{
            isGPSFix.setValue(elapsedTime < 60000);
        }
    }

    /**
     * This method is used to unregister location listener
     */
    private void unregisterLocationListener() {
        locationManager.removeUpdates(this);
    }

    /**
     * This method is used to get location
     * @return locationValue
     */
    public LiveData<Pair<Double, Double>> getLocation() {
        return this.locationValue;
    }

    /**
     * This method is used to get GPSFix
     * @return isGPSFix
     */
    public LiveData<Boolean> getGPSFix(){ return this.isGPSFix;}

    /**
     * This method is used to set mocking for location source
     * @param mockData
     */
    public void setMockLocationSource(MutableLiveData<Pair<Double, Double>> mockData) {
        unregisterLocationListener();
        this.locationValue = mockData;
    }

    /**
     * This method is used to set mocking for GPSFix source
     * @param mockGPSFixSource
     */
    public void setMockGPSFixSource(MutableLiveData<Boolean> mockGPSFixSource) {
        unregisterLocationListener();
        this.isGPSFix = mockGPSFixSource;
    }

    /**
     * This method is used to set mocking for elapsed time
     * @param time
     */
    public void setMockElapsedTime(long time){
        elapsedTime = time;
        mocking = true;
    }

    /**
     * This method is used to get elapsed time
     * @return elapsedTime
     */
    public long getElapsedTime(){
        return elapsedTime;
    }

}