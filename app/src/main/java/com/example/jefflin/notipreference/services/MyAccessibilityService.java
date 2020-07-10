package com.example.jefflin.notipreference.services;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.example.jefflin.notipreference.database.AccessibilityDao;
import com.example.jefflin.notipreference.database.NotiDatabase;
import com.example.jefflin.notipreference.manager.ContextManager;
import com.example.jefflin.notipreference.model.Accessibility;

import java.util.Calendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MyAccessibilityService extends AccessibilityService {

    private final static String TAG = "AccessibilityService";
    private Executor mExecutor = Executors.newSingleThreadExecutor();
    AccessibilityDao accessibilityDao = NotiDatabase.getInstance(this).accessibilityDao();

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        Log.d("AccessibilityService", "bind");
        return super.bindService(service, conn, flags);
    }


    @Override
    public void onInterrupt() {
        Log.d(TAG, "Interrupt");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        final int eventType = accessibilityEvent.getEventType();

        if (eventType == 2048) return;

        final String pack;
        final String text;
        final String type;
        final String extra;
        long time = -1;

        Log.d("onAccessibilityEvent", String.valueOf(eventType));

        if (accessibilityEvent.getPackageName() != null) {
            pack = accessibilityEvent.getPackageName().toString();
            Log.d(TAG, "pack : " + pack);
        } else {
            pack = "";
        }

        if (accessibilityEvent.getClassName() != null) {
            text = accessibilityEvent.getClassName().toString();
        } else {
            text = "";
        }

        if (accessibilityEvent.getText() != null) {
//            text += ":" + accessibilityEvent.getText().toString();
            Log.d(TAG, "text : " + text);
        }

        if (accessibilityEvent.getContentDescription() != null) {
            extra = accessibilityEvent.getContentDescription().toString();
            Log.d(TAG, "extra : " + extra);
        } else {
            extra = "";
        }
        time = Calendar.getInstance().getTimeInMillis();

        ContextManager.getInstance().setCurrentAccessibility(eventType, pack, text, extra);

        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                accessibilityDao.insert(
                        new Accessibility(eventType, pack, text, extra));
            }
        });


    }
}
