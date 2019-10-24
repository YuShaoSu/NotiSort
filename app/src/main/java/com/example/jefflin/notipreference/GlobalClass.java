package com.example.jefflin.notipreference;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.File;

public class GlobalClass extends Application {
    private static File dirPath;

    public static File getDirPath() {
        return dirPath;
    }

    public static void setDirPath(Context context, String dirName) {
        dirPath = context.getDir(dirName, Context.MODE_PRIVATE);

        Log.d("pathName: ", String.valueOf(dirPath));

    }

}
