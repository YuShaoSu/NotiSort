package com.example.jefflin.notipreference.model;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "sample_combination", indices = {@Index(value = {"app_name_comb"}, unique = true)})
public class SampleCombination {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "app_name_comb")
    private String appNameComb;


    @ColumnInfo(name = "count")
    private int count;

    @Ignore
    public SampleCombination(){
        appNameComb = "";
    }

    public SampleCombination(@NonNull String appNameComb){
        this.appNameComb = appNameComb;
        this.count = 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public String getAppNameComb() {
        return appNameComb;
    }

    public void setAppNameComb(String appNameComb) {
        this.appNameComb = appNameComb;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
