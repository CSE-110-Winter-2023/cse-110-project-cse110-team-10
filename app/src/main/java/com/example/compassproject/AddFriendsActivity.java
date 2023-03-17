package com.example.compassproject;

import static com.example.compassproject.CheckValidFriendUID.checkValidFriendUID;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.compassproject.model.Location;
import com.example.compassproject.model.LocationAPI;

import org.w3c.dom.Text;

public class AddFriendsActivity extends AppCompatActivity {
    private FriendEntryDatabase db;
    private FriendEntryDao friendEntryDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        //set UID display
        TextView textView = this.findViewById(R.id.your_uid);
        SharedPreferences preferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        UserInfo u1 = new UserInfo(preferences);
        textView.setText(u1.getUID());

        // Instantiate database and DAO
        db = FriendEntryDatabase.getSingleton(this);
        friendEntryDao = db.friendEntryDao();
    }

    // Return to compass view when back button is clicked
    public void onAddFriendsBackButtonClicked(View view)
    {
        finish();
    }

    // Add friend if valid UID
    public void onAddFriendsSubmitButtonClicked(View view)
    {
        // Gets inputted UID from the edit text view
        EditText friend_uid_view = findViewById(R.id.friend_uid_input);
        String friend_uid = friend_uid_view.getText().toString();
        FriendEntry friend = new FriendEntry(friend_uid);

        boolean isValidUID = checkValidFriend(friend);

        // If valid UID, add friend to database
        if(isValidUID)
        {
            friendEntryDao.insert(friend);
            //TODO: Change to finish() when CompassActivity is refactored to not only run properly onCreate()
            Intent intent = new Intent(this, CompassActivity.class);
            startActivity(intent);
        }
        // Else, show error
        else
        {
            Utilities.showPopup(this, "Error", "Invalid UID Entered");
        }

    }

    // Check if friend has valid UID on server
    private boolean checkValidFriend(FriendEntry friend)
    {
        return checkValidFriendUID(friend);
    }

    // Update app with new server endpoint
    public void onUrlSubmitButtonClicked(View view) {
        TextView urlInput = findViewById(R.id.urlInput);
        LocationAPI.changeEndpoint(urlInput.getText().toString());
        finish();
    }
}