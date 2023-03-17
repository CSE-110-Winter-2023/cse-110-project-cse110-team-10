package com.example.compassproject;

import static java.util.UUID.randomUUID;

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

            generatePrivateCode();

            generateUID(name_input);

            Intent intent = new Intent(this,CompassActivity.class);
            startActivity(intent);
        }
        else
        {
            Utilities.showPopup(this, "Error", "No Name Entered");
        }



    }

    //checks if user inputted a valid name
    private boolean checkNonEmptyName(String name_input)
    {
        return !name_input.equals("");
    }

    //stores the name the user entered
    private void storeName(String name)
    {
        UserInfo u1 = new UserInfo(getSharedPreferences(getString(R.string.saveUserInfo), MODE_PRIVATE));
        u1.setName(name);
    }

    //generates private code for user
    private void generatePrivateCode()
    {
        String privateCode = randomUUID().toString();
        UserInfo u1 = new UserInfo(getSharedPreferences(getString(R.string.saveUserInfo), MODE_PRIVATE));
        u1.setPrivateCode(privateCode);
    }

    //generates UID for user
    private void generateUID(String name_input)
    {
        String UID = GenerateUID.generateUID(name_input);
        UserInfo u1 = new UserInfo(getSharedPreferences(getString(R.string.saveUserInfo), MODE_PRIVATE));
        u1.setUID(UID);
    }


}