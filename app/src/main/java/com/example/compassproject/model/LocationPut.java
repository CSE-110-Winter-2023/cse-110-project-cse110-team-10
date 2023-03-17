package com.example.compassproject.model;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

// This class is for the structure needed for the PUT HTTP request
public class LocationPut {

    /** The private code of the person. */
    @SerializedName("private_code")
    public String private_code;

    @SerializedName("label")
    @NonNull
    public String label;

    @SerializedName("latitude")
    @NonNull
    public double latitude;

    @SerializedName("longitude")
    @NonNull
    public double longitude;

    public LocationPut(Location loc){
        this.private_code = loc.private_code;
        this.label = loc.label;
        this.latitude = loc.latitude;
        this.longitude = loc.longitude;
    }

    // Generate object from JSON
    public static LocationPut fromJSON(String json) {
        return new Gson().fromJson(json, LocationPut.class);
    }

    // Generate JSON from object
    public String toJSON() {
        return new Gson().toJson(this);
    }
}
