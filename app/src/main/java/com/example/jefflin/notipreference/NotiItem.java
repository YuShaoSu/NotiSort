package com.example.jefflin.notipreference;

import android.graphics.drawable.Drawable;

public class NotiItem {
    public Drawable icon;
    public String appname;
    public String title;
    public String content;

    public NotiItem(Drawable icon, String appname, String title, String content) {
        this.icon = icon;
        this.appname = appname;
        this.title = title;
        this.content = content;
    }
}
