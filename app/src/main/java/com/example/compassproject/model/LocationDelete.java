package com.example.compassproject.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class LocationDelete {
    /** The private code of the person. */
    @SerializedName("private_code")
    public String private_code;

    public LocationDelete(Location loc){
        this.private_code = loc.private_code;
    }
    public static LocationDelete fromJSON(String json) {
        return new Gson().fromJson(json, LocationDelete.class);
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }
}
