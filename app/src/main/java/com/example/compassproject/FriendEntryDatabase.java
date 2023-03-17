package com.example.compassproject;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
This class is use as Friend Entry Database
function: makeDataBase - make database for friend entry
          injectTestDataBase - testDataBase
 */
@Database(entities = {FriendEntry.class}, version = 1)
public abstract class FriendEntryDatabase extends RoomDatabase
{
    private static FriendEntryDatabase singleton = null;

    public abstract FriendEntryDao friendEntryDao();

    /** Ensure that there only ever exists one database per context
     * @param context
     * @return singleton
     */
    public synchronized static FriendEntryDatabase getSingleton(Context context)
    {
        if(singleton == null)
        {
            singleton = FriendEntryDatabase.makeDatabase(context);
        }

        return singleton;
    }

    /**
        This method is used to make the data base for friend entry
        @param context - the context to make database
        @return a database which contains friend entries.
     */
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

    /**
        This method is used to inject test database
         @param testDatabase - the database to test
     */
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
