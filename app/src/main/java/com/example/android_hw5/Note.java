package com.example.android_hw5;

import android.database.Cursor;

public class Note {
    public String title;
    public String content;
    public long date;

    public Note(){}
    public Note(String title, String content,long date) {
        this.date=date;
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String str) {
        this.content = str;
    }
    public static String TABLE_NAME = "Notes";
    public static String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT, content TEXT,date INTEGER)";

    public Note(Cursor c) {
        title = c.getString(1);
        content = c.getString(2);
        date = c.getLong(3);
    }

    public String toString(){
        return  title + ", " + content +", "+date;
    }
}
