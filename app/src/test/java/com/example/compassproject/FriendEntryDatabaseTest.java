package com.example.compassproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class FriendEntryDatabaseTest
{
    private FriendEntryDao dao;
    private FriendEntryDatabase db;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void createDb()
    {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, FriendEntryDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.friendEntryDao();
    }

    @Test
    public void testInsert()
    {
        FriendEntry friend1 = new FriendEntry("TestUID1");
        FriendEntry friend2 = new FriendEntry("TestUID2");

        long id1 = dao.insert(friend1);
        long id2 = dao.insert(friend2);

        assertNotEquals(id1, id2);
    }

    @Test
    public void testGet()
    {
        FriendEntry friend1 = new FriendEntry("TestUID1");
        dao.insert(friend1);
        FriendEntry friendReturn = dao.get(friend1.uid);

        assertEquals(friendReturn.uid, friend1.uid);

    }

    @Test
    public void testDelete()
    {
        FriendEntry friend1 = new FriendEntry("TestUID1");
        dao.insert(friend1);

        friend1 = dao.get("TestUID1");
        int itemsDeleted = dao.delete(friend1);
        assertEquals(1, itemsDeleted);
        assertNull(dao.get(friend1.uid));
    }

    @Test
    public void testDuplicate()
    {
        FriendEntry friend1 = new FriendEntry("TestUID1");
        dao.insert(friend1);

        FriendEntry friend2 = new FriendEntry("TestUID1");
        dao.insert(friend2);

        List<FriendEntry> entries = dao.getAll();
        assertEquals(1, entries.size());
    }

    @After
    public void closeDb() throws IOException
    {
        db.close();
    }


}
