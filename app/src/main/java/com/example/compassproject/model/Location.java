package com.example.compassproject.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.time.Instant;
import androidx.annotation.NonNull;

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

    public static Location fromJSON(String json) {
        return new Gson().fromJson(json, Location.class);
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }
}