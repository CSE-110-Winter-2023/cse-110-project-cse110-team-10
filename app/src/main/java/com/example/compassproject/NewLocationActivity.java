package com.example.compassproject;

import static com.example.compassproject.Utilities.showPopup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class NewLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_location);
    }

    public void onSubmitLocationClicked(View view) {

        if(isInputValid()){
            /*
            Save info
            */
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public boolean isInputValid(){
        TextView coordsInput = findViewById(R.id.coordinateInput);
        String coords = coordsInput.getText().toString();

        TextView labelInput = findViewById(R.id.labelInput);
        String label = labelInput.getText().toString();

        return isInputValid(coords, label);
    }

    // Checks if all inputted data is valid
    public boolean isInputValid(String coords, String label){

        if(InputHandling.isCoordinatesEmpty(coords)){
            showPopup(this, "Error", "Please make sure the coordinates are not empty.");
            return false;
        }
        else if(!InputHandling.isCoordinatesValid(coords)){
            showPopup(this, "Error", "Please make sure the coordinates are valid.");
            return false;
        }
        else if(InputHandling.isLabelEmpty(label)){
            showPopup(this,"Error", "Please make sure the label is not empty.");
            return false;
        }

        return true;
    }
}