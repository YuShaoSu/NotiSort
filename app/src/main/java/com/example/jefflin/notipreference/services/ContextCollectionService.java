package com.example.jefflin.notipreference.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.location.LocationServices;

public class ContextCollectionService extends Service {

    private static final String TAG = "ContextCollectionService";
    IBinder mBinder;

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"bind");

        return mBinder;
    }

}
