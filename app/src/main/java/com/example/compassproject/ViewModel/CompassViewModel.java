package com.example.compassproject.ViewModel;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.core.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.compassproject.CompassActivity;
import com.example.compassproject.FriendEntryDao;
import com.example.compassproject.FriendEntryDatabase;
import com.example.compassproject.UserInfo;
import com.example.compassproject.model.Location;
import com.example.compassproject.model.LocationDatabase;
import com.example.compassproject.model.LocationRepository;

import java.util.ArrayList;
import java.util.List;

public class CompassViewModel extends AndroidViewModel {
    private LiveData<Location> location;
    private final LocationRepository locRepo;
    private final FriendEntryDao friendEntryDao;

    public CompassViewModel(@NonNull Application application) {
        super(application);
        var context = application.getApplicationContext();
        var locDb = LocationDatabase.provide(context);
        var locDao = locDb.getDao();
        this.locRepo = new LocationRepository(locDao);
        var friendDb = FriendEntryDatabase.getSingleton(context);
        this.friendEntryDao = friendDb.friendEntryDao();
    }
    public void updateCoordinatesRemote(Pair<Double, Double> location, UserInfo u1){
        //TODO: Use real public_code, private_code, and label

        Location loc1 = new Location(u1.getUID(), u1.getPrivateCode(), u1.getName(), location.first, location.second, true, "2023-02-18T12:00:00Z", "2023-02-18T18:30:00Z");
        locRepo.upsertRemote(loc1);
    }
    public List<String> getAllFriendUIDs(){
        var friendEntryList = friendEntryDao.getAll();
        var list = new ArrayList<String>();

        for(int i = 0; i < friendEntryList.size(); i++){
            list.add(friendEntryList.get(i).uid);
        }

        return list;
    }

    public LiveData<Location> getLiveLocation(String public_code){
        return locRepo.getSynced(public_code);
        //return locRepo.getRemote(public_code);
    }
}

