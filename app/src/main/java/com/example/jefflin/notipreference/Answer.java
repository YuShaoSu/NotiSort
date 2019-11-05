package com.example.jefflin.notipreference;

import java.util.ArrayList;
import java.util.Collections;

public class Answer {

    private String id;
    private long time;
    private int interval;
    private ArrayList<NotiItem> notifications;
    private String esm_q1;
    private String esm_q2;
    private String esm_q3;
    private String esm_q4;
    private String esm_q5;
    private String esm_q6;

    public Answer(String id, long time, int interval) {
        this.id = id;
        this.time = time;
        this.interval = interval;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setNotifications(ArrayList<NotiItem> notifications) {
        this.notifications = notifications;
    }

    public void setEsmQ1(String esm_q1) {
        this.esm_q1 = esm_q1;
    }
    public void setEsmQ2(String esm_q2) { this.esm_q2 = esm_q2; }
    public void setEsmQ3(String esm_q3) { this.esm_q3 = esm_q3; }
    public void setEsmQ4(String esm_q4) { this.esm_q4 = esm_q4; }
    public void setEsmQ5(String esm_q5) { this.esm_q5 = esm_q5; }
    public void setEsmQ6(String esm_q6) { this.esm_q6 = esm_q6; }

    // false for scale check fails
    public boolean answerHandler(ArrayList<NotiItem> notiItems, ArrayList<NotiItem> notiItemsD) {

        // scale check and set click_order / display_order
        for (int i = 0; i < notiItems.size(); i++) {
            NotiItem item = notiItems.get(i);
            NotiItem itemD = notiItemsD.get(i);
            if(item.checkScale()) {
                item.setClickOrder(i);
                itemD.setDisplayOrder(i);
            }
            else return false;
        }

        Collections.sort(notiItems);
        Collections.sort(notiItemsD);

        // fill display_order to first arraylist of notis
        for (int i = 0; i < notiItems.size(); i++) {
            NotiItem item = notiItems.get(i);
            NotiItem itemD = notiItemsD.get(i);
            item.setDisplayOrder(itemD.getDisplayOrder());
        }

        this.notifications = notiItems;

        return true;
    }


}
