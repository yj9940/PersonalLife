package com.example.lenovo.personallife.Weather;

public class SuggestString {
    private String zhishu;
    private String suggest;

    SuggestString(String str1,String str2)
    {
        this.zhishu = str1;
        this.suggest = str2;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public String getZhishu() {
        return zhishu;
    }

    public void setZhishu(String zhishu) {
        this.zhishu = zhishu;
    }


}