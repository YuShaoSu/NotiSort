package com.example.jefflin.notipreference.database;


import android.util.Log;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.jefflin.notipreference.model.SampleCombination;
import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class SampleCombinationDao {
    @Query("SELECT * FROM sample_combination")
    public abstract List<SampleCombination> getAll();

    // 傳一個app name組合的string array進來
    @Query("SELECT * FROM sample_combination WHERE app_name_comb IN(:apps)")
    public abstract List<SampleCombination> getAppComb(String[] apps);

    @Transaction
    public void upsert(List<SampleCombination> sampleCombinations) {
        List<Long> insertResult = insertAll(sampleCombinations);
        List<String> updateList = new ArrayList<>();

        for (int i = 0; i < insertResult.size(); ++i) {
            if (insertResult.get(i) == -1) {
                updateList.add(sampleCombinations.get(i).getAppNameComb());
//                Log.d("SampleCombDao", " update one " + sampleCombinations.get(i).getAppNameComb());
            }
        }

        if (!updateList.isEmpty()) {
            update(updateList.toArray(new String[0]));
        }
    }

    @Query("UPDATE sample_combination SET count = count + 1 WHERE app_name_comb = :app")
    public abstract void update(String app);

    @Query("UPDATE sample_combination SET count = count + 1 WHERE app_name_comb IN(:apps)")
    public abstract void update(String[] apps);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract List<Long> insert(SampleCombination... sampleCombinations);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract List<Long> insertAll(List<SampleCombination> sampleCombinations);

    @Delete
    public abstract void delete(SampleCombination... sampleCombinations);

}
