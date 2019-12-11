package com.example.jefflin.notipreference.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.example.jefflin.notipreference.database.NotiDatabase;
import com.example.jefflin.notipreference.model.LocationUpdateModel;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

public class LocationUpdateReceiver extends BroadcastReceiver {

    public LocationUpdateReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,
                "Receive Location Update",
                Toast.LENGTH_SHORT)
                .show();

        if(LocationResult.hasResult(intent)) {
            Location result = LocationResult.extractResult(intent).getLastLocation();
//            NotiDatabase.getInstance(context).locationUpdateDao().insert(
//                    new LocationUpdateModel(result.getLongitude(), result.getLatitude(),
//                            result.getAccuracy(), result.getTime())
//            );
            Log.d("location update receive", String.valueOf(result.getLongitude()));
        }
    }
}
