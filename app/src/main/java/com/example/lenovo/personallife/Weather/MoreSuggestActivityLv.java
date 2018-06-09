package com.example.lenovo.personallife.Weather;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;

import com.example.lenovo.personallife.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MoreSuggestActivityLv extends Activity{

    private ListView listView;


    private LayoutInflater layoutInflater;
    private ArrayList<SuggestString> arrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggest_show);
        listView = (ListView) findViewById(R.id.lv_suggest);

        SharedPreferences sharedPreferences = getSharedPreferences(
                "CityCodePreference", Activity.MODE_PRIVATE);
        String defaultCityCode = sharedPreferences.getString("citycode","");

        getWeatherDatafromNet(defaultCityCode);
    }

//    private void adapter()
//    {
//        layoutInflater=getLayoutInflater();
//        SuggestAdapter adapter = new SuggestAdapter(layoutInflater,arrayList);
//        listView.setAdapter(adapter);
//    }

    private ArrayList<SuggestString> parseXML(String xmlData)
    {
//        WeatherSuggests sugg_n= null;
        ArrayList<SuggestString> arr =null;

        int zhishu_count=0;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));

            int eventType = xmlPullParser.getEventType();
            Log.d("MWeater2", "start parse xml2");

            while(eventType!=xmlPullParser.END_DOCUMENT)
            {
                switch (eventType)
                {
//                    start of file
                    case XmlPullParser.START_DOCUMENT:
                        Log.d("parse2","start doc2");
                        break;
                    //start of tag
                    case XmlPullParser.START_TAG:
                        if(xmlPullParser.getName().equals("resp"))
                        {
//                            sugg_n = new WeatherSuggests();
                            arr = new ArrayList<SuggestString>();
                        }
                        if(arr!=null) {
                            if (xmlPullParser.getName().equals("detail")&& zhishu_count == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("zhishu", xmlPullParser.getText());
//                                sugg_n.setChenlian(xmlPullParser.getText());
                                SuggestString str =new SuggestString("晨练指数：",xmlPullParser.getText());
                                arr.add(str);
                                zhishu_count++;
                            }
                            else if (xmlPullParser.getName().equals("detail") && zhishu_count == 1) {
                                eventType = xmlPullParser.next();
                                Log.d("shushidu", xmlPullParser.getText());
//                                sugg_n.setShushidu(xmlPullParser.getText());
                                SuggestString str =new SuggestString("舒适度：",xmlPullParser.getText());
                                arr.add(str);
                                zhishu_count++;
                            }
                            else if (xmlPullParser.getName().equals("detail") && zhishu_count == 2) {
                                eventType = xmlPullParser.next();
                                SuggestString str =new SuggestString("穿衣指数：",xmlPullParser.getText());
                                arr.add(str);
                                zhishu_count++;
                            }
                            else if (xmlPullParser.getName().equals("detail") && zhishu_count == 3) {
                                eventType = xmlPullParser.next();
                                SuggestString str =new SuggestString("感冒指数",xmlPullParser.getText());
                                arr.add(str);
                                zhishu_count++;
                            }
                            else if (xmlPullParser.getName().equals("detail") && zhishu_count == 4) {
                                eventType = xmlPullParser.next();
                                SuggestString str =new SuggestString("晾晒指数",xmlPullParser.getText());
                                arr.add(str);
                                zhishu_count++;
                            }
                            else if (xmlPullParser.getName().equals("detail") && zhishu_count == 5) {
                                eventType = xmlPullParser.next();
                                SuggestString str =new SuggestString("旅游指数：",xmlPullParser.getText());
                                arr.add(str);
                                zhishu_count++;
                            }
                            else if (xmlPullParser.getName().equals("detail") && zhishu_count == 6) {
                                eventType = xmlPullParser.next();
                                SuggestString str =new SuggestString("紫外线指数：",xmlPullParser.getText());
                                arr.add(str);
                                zhishu_count++;
                            }
                            else if (xmlPullParser.getName().equals("detail") && zhishu_count == 7) {
                                eventType = xmlPullParser.next();
                                SuggestString str =new SuggestString("洗车指数：",xmlPullParser.getText());
                                arr.add(str);
                                zhishu_count++;
                            }
                            else if (xmlPullParser.getName().equals("detail") && zhishu_count == 8) {
                                eventType = xmlPullParser.next();
                                SuggestString str =new SuggestString("运动指数：",xmlPullParser.getText());
                                arr.add(str);
                                zhishu_count++;
                            }
                            else if (xmlPullParser.getName().equals("detail") && zhishu_count == 9) {
                                eventType = xmlPullParser.next();
                                SuggestString str =new SuggestString("约会指数：",xmlPullParser.getText());
                                arr.add(str);
                                zhishu_count++;
                            }
                            else if (xmlPullParser.getName().equals("detail") && zhishu_count == 10) {
                                eventType = xmlPullParser.next();
                                SuggestString str =new SuggestString("雨伞指数：",xmlPullParser.getText());
                                arr.add(str);
                                zhishu_count++;
                            }

                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }

                eventType=xmlPullParser.next();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return arr;
    }

    private void getWeatherDatafromNet(String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("Address:", address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(address);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(8000);
                    urlConnection.setReadTimeout(8000);
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuffer sb = new StringBuffer();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        sb.append(str);
                        Log.d("date from url", str);
                    }
                    String response = sb.toString();
                    Log.d("response", response);

//                    parseXML(response);

                    arrayList = parseXML(response);
                    if(arrayList!=null)
                    {
                        //Log.d("todayWeatherDate",todayWeather.toString());

                        Message message = new Message();
                        message.what = 3;
                        message.obj = arrayList;

                        mHandler.sendMessage(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message message)
        {
            switch (message.what)
            {
                case 3:
                    updateTodayWeather((ArrayList<SuggestString>) message.obj);
                    break;
                default:
                    break;
            }
        }
    };

    void updateTodayWeather(ArrayList<SuggestString> al)
    {
        layoutInflater=getLayoutInflater();
        SuggestAdapter adapter = new SuggestAdapter(layoutInflater,al);
        listView.setAdapter(adapter);
    }
}