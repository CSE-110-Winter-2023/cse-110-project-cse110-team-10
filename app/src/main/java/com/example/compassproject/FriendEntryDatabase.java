package com.example.compassproject;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

//Database class
@Database(entities = {FriendEntry.class}, version = 1)
public abstract class FriendEntryDatabase extends RoomDatabase
{
    private static FriendEntryDatabase singleton = null;

    public abstract FriendEntryDao friendEntryDao();

    // Ensure that there only ever exists one database per context
    public synchronized static FriendEntryDatabase getSingleton(Context context)
    {
        if(singleton == null)
        {
            singleton = FriendEntryDatabase.makeDatabase(context);
        }

        return singleton;
    }

    //Create Database
    private static FriendEntryDatabase makeDatabase(Context context)
    {
        return Room.databaseBuilder(context, FriendEntryDatabase.class, "Friend_Entry.db")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db)
                    {
                        super.onCreate(db);

                    }

                })
                .build();
    }

    @VisibleForTesting
    public static void injectTestDatabase(FriendEntryDatabase testDatabase)
    {
        if(singleton != null)
        {
            singleton.close();
        }

        singleton = testDatabase;
    }
}
