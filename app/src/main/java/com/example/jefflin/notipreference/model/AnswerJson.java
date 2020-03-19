package com.example.jefflin.notipreference.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "answer")
public class AnswerJson {

    @ColumnInfo
    private String jsonString;

    @PrimaryKey(autoGenerate = true)
    private int id;

    public AnswerJson() {
    }

    public AnswerJson(String answer) {
        this.jsonString = answer;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String answer) {
        this.jsonString = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
