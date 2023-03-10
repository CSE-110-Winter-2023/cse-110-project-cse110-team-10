package com.example.compassproject.ViewModel;


import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.compassproject.model.Location;
import com.example.compassproject.model.LocationDatabase;
import com.example.compassproject.model.LocationRepository;

import java.util.ArrayList;
import java.util.List;

public class CompassViewModel extends AndroidViewModel {
    private LiveData<Location> location;
    private final LocationRepository locRepo;

    public CompassViewModel(@NonNull Application application) {
        super(application);
        var context = application.getApplicationContext();
        var locDb = LocationDatabase.provide(context);
        var locDao = locDb.getDao();
        this.locRepo = new LocationRepository(locDao);
        //TODO: Add friend's database
    }
    public void updateCoordinatesRemote(Pair<Double, Double> location){
        //TODO: Use real public_code, private_code, and label
        Location loc1 = new Location("kgupta", "Team10TestInput", "Kanishk", location.first, location.second, true, "2023-02-18T12:00:00Z", "2023-02-18T18:30:00Z");
        locRepo.upsertRemote(loc1);
    }
    public List<String> getAllFriendUIDs(){
        //TODO: GETTING OUR FRIEND's UID FROM DATABASE
        var list = new ArrayList<String>();
        list.add("van");
        list.add("Kanishk");
        return list;
    }

    public LiveData<Location> getLiveLocation(String public_code){
        return locRepo.getSynced(public_code);
        //return locRepo.getRemote(public_code);
    }
}

