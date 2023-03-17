package com.example.compassproject.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LocationRepository {
    private final LocationDao dao;

    public LocationRepository(LocationDao dao) {
        this.dao = dao;
    }

    // Get latest available data
    // Pull data from the server and upsert to the database
    public LiveData<Location> getSynced(String public_code) {
        var location = new MediatorLiveData<Location>();

        // Update database with latest info from server
        Observer<Location> updateFromRemote = theirLocation -> {
            var ourLocation = location.getValue();
            if(theirLocation == null){
                return;
            }
            if (ourLocation == null || ourLocation.updated_at.compareTo(theirLocation.updated_at) < 0) {
                upsertLocal(theirLocation);
            }
        };

        // Update live data if value isn't null
        Observer<Location> postValueToLiveData = ourLocation -> {
            if(ourLocation != null) {
                location.postValue(ourLocation);
            }
        };

        // Get value from server the 1st time
        var updatedLoc = getRemote(public_code);
        location.setValue(updatedLoc.getValue());

        // If we get a local update, pass it on
        location.addSource(getLocal(public_code), postValueToLiveData);
        // If we get a remote update, update the local version (triggering the above observer)
        location.addSource(updatedLoc, updateFromRemote);

        return location;
    }

    // Upsert value to both database and server
    public void upsertSynced(Location location) {
        upsertLocal(location);
        upsertRemote(location);
    }

    // Local Methods
    // =============

    // Get location value from the database
    public LiveData<Location> getLocal(String public_code) {
        return dao.get(public_code);
    }

    // Get list of all locations in the database
    public LiveData<List<Location>> getAllLocal() {
        return dao.getAll();
    }

    // Upsert location data to database
    public void upsertLocal(Location location) {
        location.updated_at = String.valueOf(System.currentTimeMillis());
        if(existsLocal(location.public_code)){
            dao.update(location);
        }
        else{
            dao.insert(location);
        }
    }

    // Delete location from database
    public void deleteLocal(Location location) {
        dao.delete(location);
    }

    // Check if location exists in database
    public boolean existsLocal(String title) {
        return dao.exists(title);
    }

    // Remote Methods
    // ==============

    // Get location from server
    public LiveData<Location> getRemote(String public_code) {
        // making live data to postValue
        MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();

        // Get data from server
        Location currLoc = LocationAPI.provide().getLocationAsync(public_code);
        locationMutableLiveData.setValue(currLoc);

        // Get the most updated live data from server every 3s
        var executor = Executors.newSingleThreadScheduledExecutor();
        var future = executor.scheduleAtFixedRate(() -> {
            Location updatedLoc = LocationAPI.provide().getLocation(public_code);
            locationMutableLiveData.postValue(updatedLoc);
        }, 0, 3000, TimeUnit.MILLISECONDS);

        return locationMutableLiveData;
    }

    // Upsert location on server
    public void upsertRemote(Location location) {
        LocationAPI.provide().putLocationAsync(location);
        Log.i("UPSERT REMOTE", location.label);
    }
}
