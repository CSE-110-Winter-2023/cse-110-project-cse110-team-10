package com.example.compassproject;

import static com.example.compassproject.Utilities.showPopup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NewLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_location);
    }

    public void onSubmitLocationClicked(View view) {
        if(isInputValid()){
            saveLocation();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else{
            showPopup(this, "Error", getErrorMessage());
        }
    }

    private void saveLocation(){
        SavedLocations sl = new SavedLocations(getSharedPreferences(getString(R.string.saveLocation), MODE_PRIVATE));
        sl.saveLocation(getCoordinates(), getLabel());
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