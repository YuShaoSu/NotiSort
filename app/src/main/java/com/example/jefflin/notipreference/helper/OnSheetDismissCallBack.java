package com.example.jefflin.notipreference.helper;

public interface OnSheetDismissCallBack {
    void setNotAttend(boolean need_no_info, boolean no_use, boolean other);
    void setNotDisplay(boolean duplicate, boolean not_relate, boolean other);
}
