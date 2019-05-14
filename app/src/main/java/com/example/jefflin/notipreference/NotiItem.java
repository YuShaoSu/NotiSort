package com.example.jefflin.notipreference;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

public class NotiItem {
    public Drawable icon;
    public String appname;
    public String title;
    public String content;

    public NotiItem(Drawable icon, String appname, String title, String content) {
        this.appname = appname;
        this.title = title;
        this.content = content;
        this.icon = icon;
    }
}
