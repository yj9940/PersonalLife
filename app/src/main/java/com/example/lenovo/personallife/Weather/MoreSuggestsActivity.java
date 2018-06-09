package com.example.lenovo.personallife.Weather;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.personallife.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MoreSuggestsActivity extends Activity{

    private TextView tv_chenlian;
    private TextView tv_shushidu;
    private WeatherSuggests sugg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_suggests);

        tv_chenlian = (TextView) findViewById(R.id.chenlian);
        tv_shushidu= (TextView) findViewById(R.id.shushidu);

        getWeatherDatafromNet("101190301");
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

                    sugg = parseXML(response);
                    if(sugg!=null)
                    {
                        //Log.d("todayWeatherDate",todayWeather.toString());

                        Message message = new Message();
                        message.what = 2;
                        message.obj = sugg;
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
                case 2:
                    updateTodayWeather((WeatherSuggests) message.obj);
                    break;
                default:
                    break;
            }
        }
    };

    private WeatherSuggests parseXML(String xmlData)
    {
        WeatherSuggests sugg_n= null;

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
                    //start of file
                    case XmlPullParser.START_DOCUMENT:
                        Log.d("parse2","start doc2");
                        break;
                    //start of tag
                    case XmlPullParser.START_TAG:
                        if(xmlPullParser.getName().equals("resp"))
                        {
                            sugg_n = new WeatherSuggests();
                        }
                        if(sugg_n!=null) {
                            if (xmlPullParser.getName().equals("detail")&& zhishu_count == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("zhishu", xmlPullParser.getText());
                                sugg_n.setChenlian(xmlPullParser.getText());
                                zhishu_count++;
                            }
                            else if (xmlPullParser.getName().equals("detail") && zhishu_count == 1) {
                                eventType = xmlPullParser.next();
                                Log.d("shushidu", xmlPullParser.getText());
                                sugg_n.setShushidu(xmlPullParser.getText());
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
        return sugg_n;
    }

    void updateTodayWeather(WeatherSuggests ws)
    {

        tv_chenlian.setText(ws.getChenlian());
        tv_shushidu.setText(ws.getShushidu());


        Toast.makeText(this, "当前界面更新成功", Toast.LENGTH_SHORT).show();
    }
}