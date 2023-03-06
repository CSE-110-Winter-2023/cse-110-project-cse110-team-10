package com.example.compassproject.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import java.util.List;

@Dao
public abstract class LocationDao {
    // Currently does not work - use LocationRepository's upsert instead
    @Upsert
    public abstract long upsert(Location location);

    @Update
    public abstract int update(Location location);

    @Insert
    public abstract long insert(Location location);

    @Query("SELECT EXISTS(SELECT 1 FROM `locations` WHERE `public_code` = :public_code)")
    public abstract boolean exists(String public_code);

    @Query("SELECT * FROM `locations` WHERE `public_code` = :public_code")
    public abstract LiveData<Location> get(String public_code);

    @Query("SELECT * FROM `locations` WHERE `rowId` = :id")
    public abstract LiveData<Location> get(long id);

    @Query("SELECT * FROM `locations` ORDER BY `public_code`")
    public abstract LiveData<List<Location>> getAll();

    @Delete
    public abstract int delete(Location location);
}

