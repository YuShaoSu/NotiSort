package com.example.jefflin.notipreference.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.jefflin.notipreference.model.AnswerJson;

import java.util.List;

@Dao
public interface AnswerJsonDao {

    @Insert
    void insert(AnswerJson answerJson);

    @Query("SELECT * FROM answer")
    List<AnswerJson> getAll();

    @Query("DELETE FROM answer")
    void deleteAll();

}
