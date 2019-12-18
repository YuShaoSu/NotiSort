package com.example.jefflin.notipreference.services;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.example.jefflin.notipreference.manager.ContextManager;

import java.util.Calendar;

public class MyAccessibilityService extends AccessibilityService {

    private final static String TAG = "AccessibilityService";

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        Log.d("AccessibilityService", "bind");
        return super.bindService(service, conn, flags);
    }


    //    @Override
//    protected void onServiceConnected() {
//        super.onServiceConnected();
//        Log.d("AccessibilityService", "Connected");
//    }
//
//    @Override
//    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
//        return super.bindService(service, conn, flags);
//    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "Interrupt");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        int eventType = accessibilityEvent.getEventType();

        if(eventType == 2048)   return;

        String pack = "";
        String text = "";
        String type = "";
        String extra = "";
        long time = -1;

        Log.d("onAccessibilityEvent", String.valueOf(eventType));

        if(accessibilityEvent.getPackageName()!=null){
            pack=accessibilityEvent.getPackageName().toString();
            Log.d(TAG,"pack : "+ pack);
        }

        if (accessibilityEvent.getClassName()!=null ) {
            text = accessibilityEvent.getClassName().toString();
        }

        if(accessibilityEvent.getText()!=null){
            text += ":" + accessibilityEvent.getText().toString();
            //TODO testing the attribute.
            Log.d(TAG,"text : "+ text);
        }

        if (accessibilityEvent.getContentDescription()!=null) {
            extra = accessibilityEvent.getContentDescription().toString();
            Log.d(TAG,"extra : "+ extra);
        }
        time = Calendar.getInstance().getTimeInMillis();

        ContextManager.getInstance().setCurrentAccessibility(eventType, pack, text, extra);

    }
}
