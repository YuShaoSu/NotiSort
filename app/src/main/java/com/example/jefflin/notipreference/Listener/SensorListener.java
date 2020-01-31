package com.example.jefflin.notipreference.Listener;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import com.example.jefflin.notipreference.manager.ContextManager;

public class SensorListener implements SensorEventListener {

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
//        Log.d("sensor lis", sensorEvent.sensor.getName() + " " + sensorEvent.values.toString());
        /**Motion Sensor**/
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            ContextManager.getInstance().accelerometer = sensorEvent.values;
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            ContextManager.getInstance().gyroscope= sensorEvent.values;
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_GRAVITY) {
//            Log.d("sensor lis", String.valueOf(sensorEvent.values[0]));
            ContextManager.getInstance().gravity = sensorEvent.values;
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            ContextManager.getInstance().linearAcceleration = sensorEvent.values;
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            ContextManager.getInstance().rotationVector = sensorEvent.values;
        }

        /**Position Sensor**/
        else if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            //Log.d(LOG_TAG, "in [onSensorChange] Proximity: " +  event.values[0] );
            ContextManager.getInstance().proximity = sensorEvent.values[0];
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            //Log.d(LOG_TAG, "in [onSensorChange] Proximity: " +  event.values[0] );
            ContextManager.getInstance().magneticField = sensorEvent.values;
        }

        /**Environment Sensor**/
        else if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            ContextManager.getInstance().light = sensorEvent.values[0];
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            ContextManager.getInstance().ambientTemperature = sensorEvent.values[0];
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE) {
            ContextManager.getInstance().pressure = sensorEvent.values[0];
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            ContextManager.getInstance().relativeHumidity = sensorEvent.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
