package com.example.compassproject.model;

import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Location.class}, version = 1, exportSchema = false)
public abstract class LocationDatabase extends RoomDatabase{
    private volatile static LocationDatabase instance = null;

    public abstract LocationDao getDao();

    public synchronized static LocationDatabase provide(Context context) {
        if (instance == null) {
            instance = LocationDatabase.make(context);
        }
        return instance;
    }

    private static LocationDatabase make(Context context) {
        return Room.databaseBuilder(context, LocationDatabase.class, "compass_app.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    @VisibleForTesting
    public static void inject(LocationDatabase testDatabase) {
        if (instance != null ) {
            instance.close();
        }
        instance = testDatabase;
    }
}