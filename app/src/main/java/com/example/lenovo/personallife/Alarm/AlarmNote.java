package com.example.lenovo.personallife.Alarm;

public class AlarmNote {
    private String title;
    private String content;
    private int id;
    private String date;

    public AlarmNote(int id, String title, String content, String date){
        this.content=content;
        this.title=title;
        this.date=date;
        this.id=id;
    }
    public AlarmNote(int id, String title, String date){
        this.date=date;
        this.title=title;
        this.id=id;
    }
    public AlarmNote(String title, String content){
        this.title=title;
        this.content=content;
    }
    public AlarmNote(String date){
        this.date=date;
    }
    public AlarmNote(String title, String content, String date) {
        this.title=title;
        this.date=date;
        this.content=content;
    }

    public String getTitle(){
        return title;
    }
    public String getContent(){
        return content;
    }
    public int getId(){
        return id;
    }
    public String getDate(){
        return date;
    }
}