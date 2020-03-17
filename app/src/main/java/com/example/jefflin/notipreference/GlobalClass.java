package com.example.jefflin.notipreference;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class GlobalClass extends Application {
    private static File dirPath;
    private static String deviceID = "";
    private static int intervalTime[] = {8, 12, 15, 18, 21};
    private static int intervalMinute = 00;

    public static File getDirPath() {
        return dirPath;
    }
    public static void setDirPath(Context context, String dirName) {
        dirPath = context.getDir(dirName, Context.MODE_PRIVATE);
    }

    public static String getDeviceID() {
        if (deviceID.equals("")) setUUID();
        return deviceID;
    }

    public static void setDeviceID(String id) {
        if (!deviceID.equals("")) return;
        deviceID = id;
    }

    public static int[] getIntervalTime() { return intervalTime; }
    public static int getIntervalMinute() { return intervalMinute; }

    public static void setUUID() {
        String m_szDevIDShort = "35" +
                (Build.BOARD.length() % 10)
                + (Build.BRAND.length() % 10)
                + (Build.HARDWARE.length() % 10)
                + (Build.DEVICE.length() % 10)
                + (Build.MANUFACTURER.length() % 10)
                + (Build.MODEL.length() % 10)
                + (Build.PRODUCT.length() % 10);
        String serial = null;
        try
        {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            deviceID = new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        }
        catch (Exception e)
        {
            // String needs to be initialized
            serial = "serial"; // some value
        }

        deviceID =  new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    public static String Epoch2DateString(long epochSeconds, String formatString) {
        Date updatedate = new Date(epochSeconds);
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        return format.format(updatedate);
    }

}
