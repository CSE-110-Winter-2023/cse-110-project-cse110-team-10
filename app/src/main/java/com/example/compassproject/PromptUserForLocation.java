package com.example.compassproject;

import static com.example.compassproject.Utilities.showPopup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PromptUserForLocation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt_user_for_location);
    }

    // Saves data if all input is valid
    public void submitUserInput(View view) {
        if(isInputValid()){
            /*
            Save info
            */
            showPopup(this, "Success", "All inputted data is valid!");
        }
    }

    // Checks if all inputted data is valid
    private boolean isInputValid(){
        TextView coordsInput = findViewById(R.id.coordinateInput);
        String coords = coordsInput.getText().toString();

        TextView labelInput = findViewById(R.id.labelInput);
        String label = labelInput.getText().toString();

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