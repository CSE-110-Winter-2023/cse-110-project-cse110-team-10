package com.example.compassproject.model;

import android.util.Log;

import androidx.annotation.AnyThread;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
public class LocationAPI {
    private volatile static LocationAPI instance = null;

    private OkHttpClient client;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    private static String endpoint = "https://socialcompass.goto.ucsd.edu/location/";

    public LocationAPI() {
        this.client = new OkHttpClient();
    }

    // Singleton method
    public static LocationAPI provide() {
        if (instance == null) {
            instance = new LocationAPI();
        }
        return instance;
    }

    // Method to change API endpoint for mocking purposes
    public static void changeEndpoint(String newURL){
        endpoint = newURL + "/";
        Log.i("URL", endpoint);
    }

    // Get location from server
    public Location getLocation(String public_code) {
        // URLs cannot contain spaces, so we replace them with %20.
        String encodedMsg = public_code.replace(" ", "%20");

        var request = new Request.Builder()
                .url(endpoint + encodedMsg)
                .method("GET", null)
                .build();

        try (var response = client.newCall(request).execute()) {
            Log.i("GET", public_code);
            var body = response.body().string();
            Log.i("GET", body);
            return response.code() == 200 ? new Location(LocationGet.fromJSON(body)) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get location asynchronously
    @AnyThread
    public Location getLocationAsync(String public_code) {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> getLocation(public_code));

        // We can use future.get(1, SECONDS) to wait for the result.
        try {
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Put location on the server
    public void putLocation(Location location) {
        // URLs cannot contain spaces, so we replace them with %20.
        String encodedMsg = location.public_code.replace(" ", "%20");

        var locationContents = RequestBody.create(new LocationPut(location).toJSON(), JSON);
        var request = new Request.Builder()
                .url(endpoint + encodedMsg)
                .method("PUT", locationContents)
                .build();

        try (var response = client.newCall(request).execute()) {
            Log.i("PUT", location.public_code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Put location asynchronously
    @AnyThread
    public void putLocationAsync(Location location) {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> putLocation(location));

        try {
            future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Delete location from Server
    public boolean deleteLocation(Location location) {
        // URLs cannot contain spaces, so we replace them with %20.
        String encodedMsg = location.public_code.replace(" ", "%20");

        var locationContents = RequestBody.create(new LocationDelete(location).toJSON(), JSON);
        var request = new Request.Builder()
                .url(endpoint + encodedMsg)
                .method("DELETE", locationContents)
                .build();

        try (var response = client.newCall(request).execute()) {
            Log.i("DELETE", location.public_code);
            var body = response.body().string();
            Log.i("DELETE", body);
            return response.code() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete Location Asynchronously
    @AnyThread
    public void deleteLocationAsync(Location location) {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> deleteLocation(location));
    }
}
