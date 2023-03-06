package com.example.compassproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// operations for a entry in our database
@Dao
public interface FriendEntryDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(FriendEntry friendEntry);

    @Query("SELECT * FROM `friend_entries` WHERE `uid`=:id")
    FriendEntry get(String id);

    @Query("SELECT * FROM `friend_entries`")
    List<FriendEntry> getAll();

    @Delete
    int delete(FriendEntry friendEntry);
}
