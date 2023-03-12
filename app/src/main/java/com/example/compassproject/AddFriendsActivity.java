package com.example.compassproject;

import static com.example.compassproject.CheckValidFriendUID.checkValidFriendUID;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.compassproject.model.Location;
import com.example.compassproject.model.LocationAPI;

public class AddFriendsActivity extends AppCompatActivity {

    private FriendEntryDatabase db;
    private FriendEntryDao friendEntryDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);


        db = FriendEntryDatabase.getSingleton(this);
        friendEntryDao = db.friendEntryDao();
    }

    //return to compass view when back button is clicked
    public void onAddFriendsBackButtonClicked(View view)
    {
        finish();
    }

    //clicking submit button
    public void onAddFriendsSubmitButtonClicked(View view)
    {
        //gets inputted UID from the edit text view
        EditText friend_uid_view = findViewById(R.id.friend_uid_input);
        String friend_uid = friend_uid_view.getText().toString();
        FriendEntry friend = new FriendEntry(friend_uid);

        boolean isValidUID = checkValidFriend(friend);

        if(isValidUID)
        {
            friendEntryDao.insert(friend);
            //TODO: Change to finish() when CompassActivity is refactored to not only run properly onCreate()
            Intent intent = new Intent(this, CompassActivity.class);
            startActivity(intent);
        }
        else
        {
            Utilities.showPopup(this, "Error", "Invalid UID Entered");
        }

    }

    private boolean checkValidFriend(FriendEntry friend)
    {
        return checkValidFriendUID(friend);
    }

}