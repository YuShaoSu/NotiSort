package com.example.jefflin.notipreference.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.example.jefflin.notipreference.database.NotiDatabase;
import com.example.jefflin.notipreference.manager.ContextManager;
import com.example.jefflin.notipreference.manager.SurveyManager;
import com.example.jefflin.notipreference.model.LocationUpdateModel;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

public class LocationUpdateReceiver extends BroadcastReceiver {

    public LocationUpdateReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {

        if(LocationResult.hasResult(intent)) {
            Location result = LocationResult.extractResult(intent).getLastLocation();
            ContextManager.getInstance().setCurrentLocation(
                    result.getLongitude(), result.getLatitude(), result.getAccuracy());
//            Log.d("location update receive", String.valueOf(result.getLongitude()));
        }
    }
}
