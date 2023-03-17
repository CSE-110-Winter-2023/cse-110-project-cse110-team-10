package com.example.compassproject.model;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

// This class is for the structure needed for the GET HTTP request
public class LocationGet {
    /** The public code of the person. Used as the primary key for locations */
    @PrimaryKey
    @SerializedName("public_code")
    @NonNull
    public String public_code;

    @SerializedName("label")
    @NonNull
    public String label;

    @SerializedName("latitude")
    @NonNull
    public double latitude;

    @SerializedName("longitude")
    @NonNull
    public double longitude;

    @SerializedName("is_listed_publicly")
    public boolean is_listed_publicly;

    @SerializedName("created_at")
    public String created_at;

    @SerializedName("updated_at")
    public String updated_at;

    // Generate objects from JSON
    public static LocationGet fromJSON(String json) {
        return new Gson().fromJson(json, LocationGet.class);
    }

    // Generate JSON for object
    public String toJSON() {
        return new Gson().toJson(this);
    }
}
