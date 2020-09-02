package com.example.jefflin.notipreference.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.jefflin.notipreference.model.NotiModelRemove;

import java.util.List;

@Dao
public interface NotiModelRemoveDao {

    @Query("SELECT * FROM noti_model_remove")
    List<NotiModelRemove> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNotiRemove(NotiModelRemove notiModelRemove);

    @Delete
    void deleteNotiRemove(NotiModelRemove... notiModelRemoves);

    @Query("DELETE FROM noti_model_remove")
    void deleteAll();

    @Query("SELECT * FROM noti_model_remove ORDER BY id LIMIT :limit")
    List<NotiModelRemove> getPartial(int limit);

    @Query("SELECT count(id) FROM noti_model_remove")
    int getCount();

    @Query("SELECT id FROM noti_model_remove ORDER BY id DESC LIMIT 1")
    int getLargestID();

    @Query("SELECT id FROM noti_model_remove ORDER BY id LIMIT 1")
    int getSmallestID();

    @Delete
    void deleteNotiRemoveList(List<NotiModelRemove> notiModelRemoves);


}
