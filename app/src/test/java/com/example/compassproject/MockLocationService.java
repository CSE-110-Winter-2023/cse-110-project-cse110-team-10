package com.example.compassproject;

import android.app.Activity;
import android.util.Pair;

import androidx.lifecycle.LiveData;

public class MockLocationService extends LocationService
{
    private LiveData<Pair<Double,Double>> coordinates;

    /**
     * constructor for location service
     *
     * @param activity context needed to initiate location manager
     */
    protected MockLocationService(Activity activity) {
        super(activity);
    }

    public LiveData<Pair<Double,Double>> getLocation()
    {
        return coordinates;
    }

    public void setLocation(LiveData<Pair<Double,Double>> coord)
    {
        coordinates = coord;
    }
}
