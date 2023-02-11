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
        else{
            showPopup(this, "Error", getErrorMessage());
        }
    }

    private boolean isInputValid(){
        return InputHandling.isInputValid(getCoordinates(), getLabel());
    }

    private String getErrorMessage(){
        return InputHandling.getErrorMessage(getInputError());
    }

    private int getInputError() {
        return InputHandling.getInputError(getCoordinates(), getLabel());
    }

    private String getCoordinates(){
        TextView coords_input = findViewById(R.id.coordinateInput);
        String coords = coords_input.getText().toString();
        return coords;
    }

    private String getLabel(){
        TextView label_input = findViewById(R.id.labelInput);
        String label = label_input.getText().toString();
        return label;
    }
}