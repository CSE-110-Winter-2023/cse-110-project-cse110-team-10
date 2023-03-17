package com.example.compassproject;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * This class is used to implement for SensorEventListener
 * Functions    OrientationService
 *              registerSensorListeners
 *              singleton
 *              onSensorChanged
 *              onAccuracyChanged
 *              onBothSensorDataAvailable
 *              unregisterSensorListeners
 *              getOrientation
 *              setMockOrientationSource
 */
public class OrientationService implements SensorEventListener {
    private static OrientationService instance;
    private final SensorManager sensorManager;
    private float[] accelerometerReading;
    private float[] magnetometerReading;
    private MutableLiveData<Float> azimuth;

    /**
     * OrientationService constructor
     * @param activity
     */
    protected OrientationService(Activity activity){
        this.azimuth = new MutableLiveData<>();
        this.sensorManager = (SensorManager)activity.getSystemService(Context.SENSOR_SERVICE);
        this.registerSensorListeners();

    }

    /**
     * SensorListeners constructor
     */
    private void registerSensorListeners(){
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Create new OrientationService
     * @param activity
     * @return instance
     */
    public static OrientationService singleton(Activity activity){
        if(instance == null){
            instance = new OrientationService(activity);
        }
        return instance;
    }

    /**
     * This method is watch events change use the sensor
     * @param event the {@link android.hardware.SensorEvent SensorEvent}.
     */
    public void onSensorChanged(SensorEvent event){
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            accelerometerReading = event.values;
        }
        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            magnetometerReading = event.values;
        }
        if(accelerometerReading != null && magnetometerReading != null){
            onBothSensorDataAvailable();
        }
    }

    /**
     * This method is used to check the accuracy of sensor
     * @param sensor
     * @param accuracy The new accuracy of this sensor, one of
     *         {@code SensorManager.SENSOR_STATUS_*}
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * This method is check if sensor is available to compute orientation
     * and compute the orientation.
     */
    private void onBothSensorDataAvailable(){
        if(accelerometerReading == null || magnetometerReading == null){
            throw new IllegalStateException("Both sensors must be available to compute orientation.");
        }

        float[] r = new float[9];
        float[] i = new float[9];

        boolean success = SensorManager.getRotationMatrix(r, i, accelerometerReading, magnetometerReading);

        if(success){
            float[] orientation = new float[3];
            SensorManager.getOrientation(r, orientation);

            this.azimuth.postValue(orientation[0]);
        }
    }

    /**
     * This method is used to un-register the sensor listeners
     */
    public void unregisterSensorListeners(){
        sensorManager.unregisterListener(this);
    }

    /**
     * This method is used to get Orientation
     * @return orientation
     */
    public LiveData<Float> getOrientation(){
        return this.azimuth;
    }

    /**
     * This method is used to get most recent orientation
     * @param activity
     * @return the most recent orientation
     */
    public static Float getMostRecentOrientation(Activity activity) {
        return singleton(activity).getOrientation().getValue();
    }

    /**
     * This method is used to set mocking for orientation source
     * @param mockDataSource
     */
    public void setMockOrientationSource(MutableLiveData<Float> mockDataSource){
        unregisterSensorListeners();
        this.azimuth = mockDataSource;
    }
}