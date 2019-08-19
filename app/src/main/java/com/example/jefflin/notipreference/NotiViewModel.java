package com.example.jefflin.notipreference;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NotiViewModel extends AndroidViewModel {
    private NotiRepository mRepository;
    private LiveData<List<NotiItem>> mAllNotis;

    public NotiViewModel(Application application) {
        super(application);
        mRepository = new NotiRepository(application);
        mAllNotis = mRepository.getAllNotis();
    }

    LiveData<List<NotiItem>> getAllNotis() { return mAllNotis; }
    public void insert(NotiItem notiItem) { mRepository.insert(notiItem); }


}
