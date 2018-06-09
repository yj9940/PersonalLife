package com.example.lenovo.personallife.Alarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AlarmDateBaseHelper extends SQLiteOpenHelper{
    Context context;
    public static final String CREATE_ALARM="create table if not exists alarm (id integer primary key autoincrement,title text,content text,date text)";
//                                              create table if not exists note (id integer primary key autoincrement,title text,content text,date text
    public AlarmDateBaseHelper(Context context)
    {

        super(context,"alarm",null,1);
        this.context=context;
        Log.d("hi1", "hello");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ALARM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}