package com.example.lenovo.personallife.Alarm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class AlarmDateBase {
    Context context;
    SQLiteDatabase sqLiteDatabase;
    AlarmDateBaseHelper myAlarmDatabaseHelper;

    public AlarmDateBase(Context context)
    {
        Log.d("wf", "see you again");
        this.context=context;
        Log.d("wf1", "it is me");
        myAlarmDatabaseHelper=new AlarmDateBaseHelper(context);
        Log.d("wf2","hi baby");
    }
    public ArrayList<AlarmNote> getArray(){
        ArrayList<AlarmNote> arrayList=new ArrayList<AlarmNote>();
        ArrayList<AlarmNote> arrayList1=new ArrayList<AlarmNote>();

        Log.d("wtf2.5","world");
        sqLiteDatabase=myAlarmDatabaseHelper.getReadableDatabase();

        Log.d("wtf3","world");

        Cursor cursor=sqLiteDatabase.rawQuery("select id,title,date from alarm",null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            int id=cursor.getInt(cursor.getColumnIndex("id"));
            String title=cursor.getString(cursor.getColumnIndex("title"));
            String date=cursor.getString(cursor.getColumnIndex("date"));
            AlarmNote note=new AlarmNote(id,title,date);
            arrayList.add(note);
            cursor.moveToNext();

        }cursor.close();
        sqLiteDatabase.close();
        for(int i=arrayList.size();i>0;i--){
            arrayList1.add(arrayList.get(i-1));
        }
        return arrayList1;
        //return  arrayList;
    }

    //第二个activity用到的  根据id获得标题+内容+date
    public AlarmNote getTitleandContent(int id){
        Log.d("我是谁",""+id);
        sqLiteDatabase=myAlarmDatabaseHelper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select title,content,date from alarm where id='" + id + "'", null);
        cursor.moveToFirst();
        String title=cursor.getString(cursor.getColumnIndex("title"));
        String content =cursor.getString(cursor.getColumnIndex("content"));
        String date = cursor.getString(cursor.getColumnIndex("date"));
        AlarmNote note=new AlarmNote(title,content,date);
        cursor.close();
        sqLiteDatabase.close();
        return note;
    }

    //获取所有时间
    public String getDate(){
        Log.d("我是谁","yy");
        sqLiteDatabase=myAlarmDatabaseHelper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select date from alarm where date>strftime('%Y/%m/%d %H:%M','now','localtime') order by date", null);
//        Cursor cursor=sqLiteDatabase.rawQuery("select date from alarm where date>'2020/01/01 00:00'", null);
        cursor.moveToFirst();

        if (cursor.isAfterLast()) return "nub1";

        String str=cursor.getString(cursor.getColumnIndex("date"));
        cursor.close();
        sqLiteDatabase.close();
        return str;
    }

    //获取所有时间
    public String getNextTime(){
        Log.d("NextDate","yy----");
        String str;
        sqLiteDatabase=myAlarmDatabaseHelper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select date from alarm where date>strftime('%Y/%m/%d %H:%M','now','localtime') order by date limit 1,1", null);
        cursor.moveToFirst();
        if (cursor.isAfterLast())
        {
             str= "noexist";
        }
        else {
            str = cursor.getString(cursor.getColumnIndex("date"));
        }
        cursor.close();
        sqLiteDatabase.close();
        return str;
    }

    //获取将来对应时间的title和content
    public AlarmNote getTitleAndContentByDate(String date){
        Log.d("我是谁2","yy--"+date);
//        sqLiteDatabase=myAlarmDatabaseHelper.getWritableDatabase();
        sqLiteDatabase=myAlarmDatabaseHelper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select id,title,content,date from alarm where date>strftime('%Y/%m/%d %H:%M','now','localtime') order by date", null);

//        Cursor cursor=sqLiteDatabase.rawQuery("select date from alarm where date>'2020/01/01 00:00'", null);
        cursor.moveToFirst();

        int id=cursor.getInt(cursor.getColumnIndex("id"));
        Log.d("cursor", "我的id" + id);
        String title=cursor.getString(cursor.getColumnIndex("title"));
//        String title=cursor.getString(cursor.getColumnIndex("title"));
        Log.d("cursor", "我的title" + title);

        String content =cursor.getString(cursor.getColumnIndex("content"));
        Log.d("cursor", "我的content" + content);

        String alarmdate=cursor.getString(cursor.getColumnIndex("date"));
        if (!alarmdate.equals(date))
        {
            Log.d("我","我不行了cursor"+alarmdate+"还有"+date);
        }

        AlarmNote note=new AlarmNote(title,content,date);
        cursor.close();
        sqLiteDatabase.close();
        return note;

    }

    public int getIdByDate(String str)
    {
        Log.d("by date","date:"+str);
        sqLiteDatabase=myAlarmDatabaseHelper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select id from alarm where date='"+str+"'",null);
        cursor.moveToFirst();
        int id=cursor.getInt(cursor.getColumnIndex("id"));
        cursor.close();
        Log.d("by date", "id:"+id);
        sqLiteDatabase.close();
        return id;
    }


    public void test()
    {
        Log.d("试试","nothing");
    }



    //execSQL(String sql, Object[] bindArgs)方法的第一个参数为SQL语句，
    // 第二个参数为SQL语句中占位符参数的值，参数值在数组中的顺序要和占位符的位置对应。
    // 修改数据
    public void toUpdate(AlarmNote note){
        sqLiteDatabase =myAlarmDatabaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("update alarm set title='"+ note.getTitle()+"',date='"+note.getDate()+"',content='"+note.getContent() +"' where id='"+ note.getId()+"'");
        sqLiteDatabase.close();

    }
    //插入数据,新建一条便笺
    public void toInsert(AlarmNote note){
        sqLiteDatabase=myAlarmDatabaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("insert into alarm(title,content,date) values('"+ note.getTitle()+"','"+note.getContent()+"','"+note.getDate()+"')");
        sqLiteDatabase.close();
    }
    //删除一条数据
    public void deleteNote(int id){
        sqLiteDatabase=myAlarmDatabaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("delete  from alarm where id=" + id+ "" );
        sqLiteDatabase.close();
    }
}