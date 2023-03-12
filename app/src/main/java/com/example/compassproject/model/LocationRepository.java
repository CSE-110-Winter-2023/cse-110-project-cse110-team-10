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

    // Synced Methods
    // ==============

    /**
     * This is where the magic happens. This method will return a LiveData object that will be
     * updated when the note is updated either locally or remotely on the server. Our activities
     * however will only need to observe this one LiveData object, and don't need to care where
     * it comes from!
     *
     * This method will always prefer the newest version of the note.
     *
     * @param public_code the public code of the location
     * @return a LiveData object that will be updated when the note is updated locally or remotely.
     */
    public LiveData<Location> getSynced(String public_code) {
        var location = new MediatorLiveData<Location>();

        Observer<Location> updateFromRemote = theirLocation -> {
            var ourLocation = location.getValue();
            if(theirLocation == null){
                return;
            }
            if (ourLocation == null || ourLocation.updated_at.compareTo(theirLocation.updated_at) < 0) {
                upsertLocal(theirLocation);
            }
        };

        Observer<Location> postValueToLiveData = ourLocation -> {
            if(ourLocation != null) {
                location.postValue(ourLocation);
            }
        };

        // get value from server for the 1st time.
        var updatedLoc = getRemote(public_code);
        location.setValue(updatedLoc.getValue());

        // If we get a local update, pass it on.
        location.addSource(getLocal(public_code), postValueToLiveData);
        // If we get a remote update, update the local version (triggering the above observer)
        location.addSource(updatedLoc, updateFromRemote);

        return location;
    }

    public void upsertSynced(Location location) {
        upsertLocal(location);
        upsertRemote(location);
    }

    // Local Methods
    // =============

    public LiveData<Location> getLocal(String public_code) {
        return dao.get(public_code);
    }

    public LiveData<List<Location>> getAllLocal() {
        return dao.getAll();
    }

    public void upsertLocal(Location location) {
        location.updated_at = String.valueOf(System.currentTimeMillis());
        if(existsLocal(location.public_code)){
            dao.update(location);
        }
        else{
            dao.insert(location);
        }
    }

    public void deleteLocal(Location location) {
        dao.delete(location);
    }

    public boolean existsLocal(String title) {
        return dao.exists(title);
    }

    // Remote Methods
    // ==============
    public LiveData<Location> getRemote(String public_code) {
        // making live data to postValue
        MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
        // Calling getLocationAsync() to get the 1st location data

        // Get data from server
        Location currLoc = LocationAPI.provide().getLocationAsync(public_code);
        //Log.i("GET REMOTE", currLoc.toJSON());
        // Save it in local database
        //upsertLocal(currLoc);
        locationMutableLiveData.setValue(currLoc);

        // getting the most updated live data from server every 3s
        var executor = Executors.newSingleThreadScheduledExecutor();
        var future = executor.scheduleAtFixedRate(() -> {
            Location updatedLoc = LocationAPI.provide().getLocation(public_code);
            locationMutableLiveData.postValue(updatedLoc);
        }, 0, 3000, TimeUnit.MILLISECONDS);

        return locationMutableLiveData;

        // Start by fetching the note from the server _once_ and feeding it into MutableLiveData.
        // Then, set up a background thread that will poll the server every 3 seconds.

        // You may (but don't have to) want to cache the LiveData's for each title, so that
        // you don't create a new polling thread every time you call getRemote with the same title.
        // You don't need to worry about killing background threads.
    }

    public void upsertRemote(Location location) {
        LocationAPI.provide().putLocationAsync(location);
        Log.i("UPSERT REMOTE", location.label);
    }
}
