package com.example.compassproject;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

// Friend object stored in database

@Entity(tableName = "friend_entries")
public class FriendEntry
{
    // public fields

    @NonNull
    @PrimaryKey(autoGenerate = false)
    public String uid;


    // constructor
    public FriendEntry(@NonNull String uid)
    {
        this.uid = uid;
    }

    // toString method for debugging purposes
    @Override
    public String toString() {
        return "FriendEntry{" +
                "uid='" + uid + '\'' +
                '}';
    }
}
