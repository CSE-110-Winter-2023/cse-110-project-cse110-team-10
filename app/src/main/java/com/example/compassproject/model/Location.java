package com.example.compassproject.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "locations")
public class Location {

    /** The public code of the person. Used as the primary key for locations */
    @PrimaryKey
    @SerializedName("public_code")
    @NonNull
    public String public_code;

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

    @SerializedName("is_listed_publicly")
    public boolean is_listed_publicly;

    @SerializedName("created_at")
    public String created_at;

    @SerializedName("updated_at")
    public String updated_at;

    public Location(@NonNull String public_code, String private_code, @NonNull String label, double latitude, double longitude, boolean is_listed_publicly, String created_at, String updated_at) {
        this.public_code = public_code;
        this.private_code = private_code;
        this.label = label;
        this.latitude = latitude;
        this.longitude = longitude;
        this.is_listed_publicly = is_listed_publicly;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
    public Location(LocationGet locationGet){
        this.public_code = locationGet.public_code;
        this.private_code = null;
        this.label = locationGet.label;
        this.latitude = locationGet.latitude;
        this.longitude = locationGet.longitude;
        this.is_listed_publicly = locationGet.is_listed_publicly;
        this.created_at = locationGet.created_at;
        this.updated_at = locationGet.updated_at;
    }

    public static Location fromJSON(String json) {
        return new Gson().fromJson(json, Location.class);
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }


    // This equals method is helping for testing location is matched or not
    public boolean equals(Location location){
        return  this.public_code.equals(location.public_code) &&
                this.private_code.equals(location.private_code) &&
                this.label.equals(location.label) &&
                this.latitude == location.latitude &&
                this.longitude == location.longitude &&
                this.is_listed_publicly == location.is_listed_publicly &&
                this.created_at.equals(location.created_at) &&
                this.updated_at.equals(location.updated_at);
    }
}
