package com.example.compassproject;

import static com.example.compassproject.Utilities.showPopup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

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

    public void onOrientationSubmitClicked(View view) {
        TextView orientation = findViewById(R.id.orientationInput);
        SavedLocations sl = new SavedLocations(getSharedPreferences(getString(R.string.saveLocation), MODE_PRIVATE));

        if(orientation.getText().toString().equals("")){
            Utilities.showPopup(this, "Error", "Please enter an orientation");
        }
        else if(sl.getNumLocations() == 0){
            Utilities.showPopup(this, "Error", "Please enter a location before setting the orientation");
        }
        else{
            OrientationService os = OrientationService.singleton(this);
            MutableLiveData<Float> newOrientation = new MutableLiveData<>();
            newOrientation.setValue((float) Math.toRadians(Float.parseFloat(orientation.getText().toString())));
            os.setMockOrientationSource(newOrientation);

            Intent intent = new Intent(this, CompassActivity.class);
            startActivity(intent);
        }
    }
}