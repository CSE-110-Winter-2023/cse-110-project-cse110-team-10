package com.example.compassproject;

import static java.util.UUID.randomUUID;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This class is used to setName for activities
 * Functions    onNameSubmitButtonClicked
 *              checkNonEmptyName
 *              storeName
 *              generatePrivateCode
 *              generateUID
 */
public class SetNameActivity extends AppCompatActivity {

    /**
     * This method is used to set content view
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_name);
    }

    /**
     * This method is used to direct to compass activity
     * checking if view is not empty
     * @param view
     */
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

    /**
     * This method is used to check if name entered is non empty
     * @param name_input
     * @return true if name_input is non-empty
     *          false if name_input is empty
     */
    private boolean checkNonEmptyName(String name_input)
    {
        return !name_input.equals("");
    }

    /**
     * This method is used to store the name that
     * user entered
     * @param name
     */
    private void storeName(String name)
    {
        UserInfo u1 = new UserInfo(getSharedPreferences(getString(R.string.saveUserInfo), MODE_PRIVATE));
        u1.setName(name);
    }

    /**
     * This method is used to generate public code for name user enter.
     */
    private void generatePrivateCode()
    {
        String privateCode = randomUUID().toString();
        UserInfo u1 = new UserInfo(getSharedPreferences(getString(R.string.saveUserInfo), MODE_PRIVATE));
        u1.setPrivateCode(privateCode);
    }

    /**
     * This method is used to generate UID for name user entered.
     * @param name_input
     */
    private void generateUID(String name_input)
    {
        String UID = GenerateUID.generateUID(name_input);
        UserInfo u1 = new UserInfo(getSharedPreferences(getString(R.string.saveUserInfo), MODE_PRIVATE));
        u1.setUID(UID);
    }
}