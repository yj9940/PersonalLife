package com.example.lenovo.personallife.Weather.CityChoose;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

//import com.example.lenovo.functiontest4.CityChoose.*;
//import com.example.lenovo.functiontest4.CityChoose.City;

public class CityDB {

    public static final String CITY_DB_NAME = "city.db";
    private static final String CITY_TABLE_NAME = "city";
    private SQLiteDatabase db;

    public CityDB(Context context, String path){
        Log.d("here","我就是力量的化身");
        db = context.openOrCreateDatabase(CITY_DB_NAME,Context.MODE_PRIVATE,null);
    }

    public List<com.example.lenovo.personallife.Weather.CityChoose.City> getCityList()
    {
        List<com.example.lenovo.personallife.Weather.CityChoose.City> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * from "+CITY_TABLE_NAME,null);
        while(cursor.moveToNext())
        {
            String province = cursor.getString(cursor.getColumnIndex("province"));
            String city = cursor.getString(cursor.getColumnIndex("city"));
            String number = cursor.getString(cursor.getColumnIndex("number"));
            String allPY = cursor.getString(cursor.getColumnIndex("allpy"));
            String allFirstPY = cursor.getString(cursor.getColumnIndex("allfirstpy"));
            String firstPY = cursor.getString(cursor.getColumnIndex("firstpy"));
            com.example.lenovo.personallife.Weather.CityChoose.City item = new City(province,city,number,allPY,allFirstPY,firstPY);
            list.add(item);
        }
        return list;
    }
}