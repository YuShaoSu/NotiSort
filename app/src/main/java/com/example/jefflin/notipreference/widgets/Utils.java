package com.example.jefflin.notipreference.widgets;

import com.example.jefflin.notipreference.model.NotiItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Utils {
    public List<String> getTwoAppList(ArrayList<NotiItem> notiItems) {
        // create drawer map: appName -> ArrayList<NotiItem>
        HashMap<String, List<NotiItem>> drawerAppNameMap = new HashMap<>();
        for (NotiItem drawerNotiItem: notiItems) {
            if (!drawerAppNameMap.containsKey(drawerNotiItem.appName)) {
                drawerAppNameMap.put(drawerNotiItem.appName, new ArrayList<NotiItem>());
            }
            drawerAppNameMap.get(drawerNotiItem.appName).add(drawerNotiItem);
        }

        // create drawer list<appName> with alphabet order
        List<String> drawerAppNameList = new ArrayList<>(drawerAppNameMap.keySet());
        Collections.sort(drawerAppNameList);

        // form 2-app list: app1;app2
        List<String> twoAppList = new ArrayList<>();
        for (int i=0; i<drawerAppNameList.size()-1; i++) {
            for (int j=i+1; j<drawerAppNameList.size(); j++) {
                String twoApp = drawerAppNameList.get(i) + ";" + drawerAppNameList.get(j);
                twoAppList.add(twoApp);
            }
        }

        //return
        return twoAppList;
    }
}
