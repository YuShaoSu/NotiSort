package com.example.jefflin.notipreference.manager;

public class ContextManager {

    private static volatile ContextManager uniqueInstance;

    public static ContextManager getInstance() {
        if (uniqueInstance == null) {
            synchronized (ContextManager.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new ContextManager();
                }
            }
        }
        return uniqueInstance;
    }


    // Accessibility data
    public int accessibilityType;
    public String accessibilityPack;
    public String accessibilityText;
    public String accessibilityExtra;
    public void setCurrentAccessibility(int type, String pack, String text, String extra){
        this.accessibilityType = type;
        this.accessibilityPack = pack;
        this.accessibilityText = text;
        this.accessibilityExtra = extra;
    }

    // Location data
    public double locatoinLongtitude;
    public double locatoinLatitude;
    public float locatoinAccuracy;
    public void setCurrentLocation(double mLatitude, double mLongtitude, float mAccuracy) {
        this.locatoinLongtitude = mLongtitude;
        this.locatoinLatitude = mLatitude;
        this.locatoinAccuracy = mAccuracy;
    }

    public String phoneSignalType;
    public int phoneSignalDbm;
    public void setPhoneState(String type, int dbm) {
        // type:
        // 0: LTE; 1: GSM 3G; 2: CDMA
        this.phoneSignalType = type;
        this.phoneSignalDbm = dbm;
    }

    // Sensor data
    public float light = -9999;
}
