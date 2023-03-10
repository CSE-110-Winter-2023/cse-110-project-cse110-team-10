package com.example.compassproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SetNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_name);
    }

    public void onNameSubmitButtonClicked(View view)
    {
        //gets inputted UID from the edit text view
        EditText name_input_view = findViewById(R.id.name_input);
        String name_input = name_input_view.getText().toString();

        if(checkNonEmptyName(name_input))
        {
            storeName(name_input);
            Intent intent = new Intent(this,CompassActivity.class);
            startActivity(intent);
        }
        else
        {
            Utilities.showPopup(this, "Error", "No Name Entered");
        }



    }

    private boolean checkNonEmptyName(String name_input)
    {
        return !name_input.equals("");

    }

    private void storeName(String name)
    {
        UserInfo u1 = new UserInfo(getSharedPreferences(getString(R.string.saveUserInfo), MODE_PRIVATE));
        u1.setName(name);
    }


}