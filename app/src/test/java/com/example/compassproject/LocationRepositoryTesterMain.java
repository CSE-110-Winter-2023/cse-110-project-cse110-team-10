package com.example.compassproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.compassproject.model.Location;
import com.example.compassproject.model.LocationAPI;
import com.example.compassproject.model.LocationDao;
import com.example.compassproject.model.LocationDatabase;
import com.example.compassproject.model.LocationRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LocationRepositoryTesterMain {

    private LocationDao dao;
    private LocationDatabase db;
    private Observer<Location> observer;

    private LocationRepository locRepo;
    LocationAPI singleTon;
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Before
    public void setUp(){
        singleTon = LocationAPI.provide();
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, LocationDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.getDao();
        locRepo = new LocationRepository(dao);

        observer = new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
            }
        };
    }

    @Test
    public void testUpsertLocal(){
        String code = "kgupta";
        Location item = new Location(code, "password", "Kanishk", 32.12, 74.12, true, "day1", "day1");
        locRepo.upsertLocal(item);

        LiveData<Location> itemLive = locRepo.getLocal(code);
        itemLive.observeForever(observer);
        item = itemLive.getValue();

        item.latitude = -5.123;
        locRepo.upsertLocal(item);

        itemLive = locRepo.getLocal(code);
        itemLive.observeForever(observer);
        item = itemLive.getValue();

        assertNotNull(item);
        assertEquals(-5.123, item.latitude, 0.01);
    }

    @After
    public void closeDb(){
        db.close();
    }
}
