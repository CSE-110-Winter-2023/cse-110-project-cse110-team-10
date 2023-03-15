package com.example.compassproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ZoomServiceTester {
    ZoomService zoomService;

    @Before
    public void setUpActivity()
    {
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences preferences = context.getSharedPreferences("ZoomData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("zoom_radius", 10);
        editor.apply();
        zoomService = new ZoomService(context);
    }

    @Test
    public void testGetZoomLevel() {
        int zoomLevel = zoomService.getZoomLevel();
        assertEquals(2, zoomLevel);
    }

    @Test
    public void testZoomIn() {
        ZoomService.zoomIn();
        int zoomLevel = zoomService.getZoomLevel();
        assertEquals(1, zoomLevel);
    }

    @Test
    public void testZoomInMax() {
        ZoomService.zoomIn();
        ZoomService.zoomIn();
        int zoomLevel = zoomService.getZoomLevel();
        assertEquals(1, zoomLevel);
    }

    @Test
    public void testZoomOutMax() {
        ZoomService.zoomOut();
        ZoomService.zoomOut();
        int zoomLevel = zoomService.getZoomLevel();
        assertEquals(3, zoomLevel);
    }
}

