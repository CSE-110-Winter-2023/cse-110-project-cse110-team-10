package com.example.compassproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.compassproject.model.Location;
import com.example.compassproject.model.LocationDao;
import com.example.compassproject.model.LocationDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LocationDatabaseTesterMain {
    private LocationDao dao;
    private LocationDatabase db;
    private Observer<Location> observer;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, LocationDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.getDao();

        observer = new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
            }
        };
    }

    @Test
    public void testInsert(){
        Location loc1 = new Location("kgupta", "password", "Kanishk", 32.12, 74.12, true, "day1", "day1");
        Location loc2 = new Location("vqnuyen", "password", "Van", 12.34, -41.96, false, "day0", "day3");

        long code1 = dao.insert(loc1);
        long code2 = dao.insert(loc2);

        assertNotEquals(code1, code2);
    }

    @Test
    public void testGet(){
        String code = "kgupta";
        Location insertedItem = new Location(code, "password", "Kanishk", 32.12, 74.12, true, "day1", "day1");;
        long id = dao.insert(insertedItem);

        LiveData<Location> itemLive = dao.get(code);
        itemLive.observeForever(observer);
        Location item = itemLive.getValue();

        assertEquals(code, item.public_code);
        assertEquals(insertedItem.private_code, item.private_code);
        assertEquals(insertedItem.label, item.label);
        assertEquals(insertedItem.latitude, item.latitude, 0.01);
        assertEquals(insertedItem.longitude, item.longitude, 0.01);
        assertEquals(insertedItem.is_listed_publicly, item.is_listed_publicly);
        assertEquals(insertedItem.created_at, item.created_at);
        assertEquals(insertedItem.updated_at, item.updated_at);
    }

    @Test
    public void testUpdate(){
        String code = "kgupta";
        Location item = new Location(code, "password", "Kanishk", 32.12, 74.12, true, "day1", "day1");;
        long id = dao.insert(item);

        LiveData<Location> itemLive = dao.get(code);
        itemLive.observeForever(observer);
        item = itemLive.getValue();

        item.latitude = -5.123;
        long idUpdated = dao.update(item);
        assertEquals(1, idUpdated);

        itemLive = dao.get(code);
        itemLive.observeForever(observer);
        item = itemLive.getValue();

        assertNotNull(item);
        assertEquals(-5.123, item.latitude, 0.01);
    }

    @Test
    public void testDelete(){
        String code = "kgupta";
        Location item = new Location(code, "password", "Kanishk", 32.12, 74.12, true, "day1", "day1");;
        long id = dao.insert(item);

        LiveData<Location> itemLive = dao.get(code);
        itemLive.observeForever(observer);
        item = itemLive.getValue();

        int itemDeleted = dao.delete(item);
        assertEquals(1, itemDeleted);
        assertNull(dao.get(code).getValue());
    }

    @After
    public void closeDb(){
        db.close();
    }
}