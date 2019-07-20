package com.example.jefflin.notipreference;

import android.graphics.drawable.Drawable;
import java.util.Date;

/*
    A class that store notification object's attributes
 */
public class NotiItem {
    public Drawable icon;
    public String appName;
    public String title;
    public String content;
    public Date postTime;
    public String category;

    public NotiItem(Drawable icon, String appName, String title, String content, Date postTime, String category) {
        this.icon = icon;
        this.appName = appName;
        this.title = title;
        this.content = content;
        this.postTime = postTime;
        this.category = category;
    }
}
