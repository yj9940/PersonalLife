package com.example.lenovo.personallife.Weather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


//import com.example.lenovo.personallife.CityChoose.SelectCity;
import com.example.lenovo.personallife.R;
import com.example.lenovo.personallife.Weather.CityChoose.SelectCity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

//import com.example.lenovo.functiontest4.CityChoose.SelectCity;

public class WeatherFragment extends Fragment implements View.OnClickListener{
    private String updateCityCode= "-1";
    TodayWeather todayWeather = null;

    private Button bt_suggest;

    //title
    private ImageView UpdateBtn;
    private ImageView SelectCityBtn;
    private ImageView ShareCity;

    //today
    private TextView cityT,timeT,humidityT,
            weekT,pmDataT,pmQualityT,temperatureT,
            climateT,windT,cityNameT,suggestNameT;
    private ImageView weatherStateImg,pmStateImg;

    //future
    private  TextView week1T,week2T,week3T,temperature1T,temperature2T,temperature3T,
            wind1T,wind2T,wind3T,climate1T,climate2T,climate3T;

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message message)
        {
            switch (message.what)
            {
                case 1:
                    updateTodayWeather((TodayWeather) message.obj);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.weather_layout,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        UpdateBtn = (ImageView) getActivity().findViewById(R.id.title_city_update);
        UpdateBtn.setOnClickListener(this);

        SelectCityBtn = (ImageView) getActivity().findViewById(R.id.title_city_manager);
        SelectCityBtn.setOnClickListener(this);

        ShareCity = (ImageView) getActivity().findViewById(R.id.title_city_share);
        ShareCity.setOnClickListener(this);

        bt_suggest = (Button) getActivity().findViewById(R.id.more_suggest);
        bt_suggest.setOnClickListener(this);


        initView();


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                "CityCodePreference", Activity.MODE_PRIVATE);
        String defaultCityCode = sharedPreferences.getString("citycode","");
        if(defaultCityCode!=null){
            Log.d("defaultCityCode",defaultCityCode);
            getWeatherDatafromNet(defaultCityCode);
        }
        else
        {
            SharedPreferences.Editor edt = sharedPreferences.edit();
            edt.putString("citycode","101190301");
            edt.commit();
            getWeatherDatafromNet("101190301");
        }

//        getWeatherDatafromNet("101190301");

        //检查网络连接状态
        if (CheckNet.getNetState(getActivity()) == CheckNet.NET_NONE) {
//            Log.d("MWeather", "���粻ͨ");
            Toast.makeText(getActivity(), "网络未连接，请打开网络", Toast.LENGTH_LONG).show();
        } else {
            Log.d("MWeather", "网络ok");
            Toast.makeText(getActivity(), "网络ok", Toast.LENGTH_LONG).show();
//            getWeatherDatafromNet("101010100");
        }
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

                    todayWeather = parseXML(response);
                    if(todayWeather!=null)
                    {
                        //Log.d("todayWeatherDate",todayWeather.toString());

                        Message message = new Message();
                        message.what = 1;
                        message.obj = todayWeather;
                        mHandler.sendMessage(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private TodayWeather parseXML(String xmlData)
    {
        TodayWeather todayWeather = null;

        int fengliCount = 0;
        int fengxiangCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));

            int eventType = xmlPullParser.getEventType();
            Log.d("MWeater", "start parse xml");

            while(eventType!=xmlPullParser.END_DOCUMENT)
            {
                switch (eventType)
                {
                    //文档开始位置
                    case XmlPullParser.START_DOCUMENT:
                        Log.d("parse","start doc");
                        break;
                    //标签元素开始位置
                    case XmlPullParser.START_TAG:
                        if(xmlPullParser.getName().equals("resp"))
                        {
                            todayWeather = new TodayWeather();
                        }
                        if(todayWeather!=null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                Log.d("city", xmlPullParser.getText());
                                todayWeather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                Log.d("updatetime", xmlPullParser.getText());
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                Log.d("wendu", xmlPullParser.getText());
                                todayWeather.setWendu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("fengli", xmlPullParser.getText());
                                todayWeather.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                Log.d("shidu", xmlPullParser.getText());
                                todayWeather.setShidu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("fengxiang", xmlPullParser.getText());
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                Log.d("pm25", xmlPullParser.getText());
                                todayWeather.setPm25(xmlPullParser.getText());
                            }
                            else if (xmlPullParser.getName().equals("suggest")) {
                                eventType = xmlPullParser.next();
                                Log.d("suggest", xmlPullParser.getText());
                                todayWeather.setSuggest(xmlPullParser.getText());
                            }
                            else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                Log.d("quelity", xmlPullParser.getText());
                                todayWeather.setQuality(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("date", xmlPullParser.getText());
                                todayWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("high", xmlPullParser.getText());
                                todayWeather.setHigh(xmlPullParser.getText());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("low", xmlPullParser.getText());
                                todayWeather.setLow(xmlPullParser.getText());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("type", xmlPullParser.getText());
                                todayWeather.setType(xmlPullParser.getText());
                                typeCount++;
                            } else if( xmlPullParser.getName().equals("date") && dateCount == 1){
                                eventType = xmlPullParser.next();
                                Log.d("future1 date", xmlPullParser.getText());
                                todayWeather.setDate1(xmlPullParser.getText());
                                dateCount++;
                            } else if(xmlPullParser.getName().equals("low") && lowCount == 1 ){
                                eventType = xmlPullParser.next();
                                todayWeather.setLow1(xmlPullParser.getText());
                                Log.d("future1 low",xmlPullParser.getText());
                                lowCount++;
                            } else if(xmlPullParser.getName().equals("high") && highCount == 1){
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh1(xmlPullParser.getText());
                                Log.d("future1 high",xmlPullParser.getText());
                                highCount++;
                            } else if(xmlPullParser.getName().equals("type") && typeCount == 1){
                                eventType = xmlPullParser.next();
                                todayWeather.setType1(xmlPullParser.getText());
                                Log.d("future1 type",xmlPullParser.getText());
                                typeCount++;
                            } else if(xmlPullParser.getName().equals("fengli") && fengliCount == 1){
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli1(xmlPullParser.getText());
                                Log.d("future1 fengli", xmlPullParser.getText());
                                fengliCount++;
                            } else if( xmlPullParser.getName().equals("date") && dateCount == 2){
                                eventType = xmlPullParser.next();
                                Log.d("future2 date", xmlPullParser.getText());
                                todayWeather.setDate2(xmlPullParser.getText());
                                dateCount++;
                            } else if(xmlPullParser.getName().equals("low") && lowCount == 2 ){
                                eventType = xmlPullParser.next();
                                todayWeather.setLow2(xmlPullParser.getText());
                                Log.d("future2 low",xmlPullParser.getText());
                                lowCount++;
                            } else if(xmlPullParser.getName().equals("high") && highCount == 2){
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh2(xmlPullParser.getText());
                                Log.d("future2 high",xmlPullParser.getText());
                                highCount++;
                            } else if(xmlPullParser.getName().equals("type") && typeCount == 2){
                                eventType = xmlPullParser.next();
                                todayWeather.setType2(xmlPullParser.getText());
                                Log.d("future2 type",xmlPullParser.getText());
                                typeCount++;
                            } else if(xmlPullParser.getName().equals("fengli") && fengliCount == 2){
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli2(xmlPullParser.getText());
                                Log.d("future2 fengli", xmlPullParser.getText());
                                fengliCount++;
                            } else if( xmlPullParser.getName().equals("date") && dateCount == 3){
                                eventType = xmlPullParser.next();
                                Log.d("future3 date", xmlPullParser.getText());
                                todayWeather.setDate3(xmlPullParser.getText());
                                dateCount++;
                            } else if(xmlPullParser.getName().equals("low") && lowCount == 3 ){
                                eventType = xmlPullParser.next();
                                todayWeather.setLow3(xmlPullParser.getText());
                                Log.d("future3 low",xmlPullParser.getText());
                                lowCount++;
                            } else if(xmlPullParser.getName().equals("high") && highCount == 3){
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh3(xmlPullParser.getText());
                                Log.d("future3 high",xmlPullParser.getText());
                                highCount++;
                            } else if(xmlPullParser.getName().equals("type") && typeCount == 3){
                                eventType = xmlPullParser.next();
                                todayWeather.setType3(xmlPullParser.getText());
                                Log.d("future3 type",xmlPullParser.getText());
                                typeCount++;
                            } else if(xmlPullParser.getName().equals("fengli") && fengliCount == 3){
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli3(xmlPullParser.getText());
                                Log.d("future3 fengli",xmlPullParser.getText());
                                fengliCount++;
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
        return todayWeather;
    }

    void updateTodayWeather(TodayWeather todayWeather)
    {
        cityNameT.setText(todayWeather.getCity()+"天气");
        cityT.setText(todayWeather.getCity());
        timeT.setText(todayWeather.getUpdatetime());
        humidityT.setText("湿度："+todayWeather.getShidu());
        pmDataT.setText(todayWeather.getPm25());
        pmQualityT.setText(todayWeather.getQuality());
        weekT.setText(todayWeather.getDate());
        temperatureT.setText(todayWeather.getHigh()+"~"+todayWeather.getLow());
        climateT.setText(todayWeather.getType());
        windT.setText("风力："+todayWeather.getFengli());
        suggestNameT.setText(todayWeather.getSuggest());

        week1T.setText(todayWeather.getDate1());
        week2T.setText(todayWeather.getDate2());
        week3T.setText(todayWeather.getDate3());
        temperature1T.setText(todayWeather.getHigh1()+"~"+todayWeather.getLow1());
        temperature2T.setText(todayWeather.getHigh2()+"~"+todayWeather.getLow2());
        temperature3T.setText(todayWeather.getHigh3()+"~"+todayWeather.getLow3());
        climate1T.setText(todayWeather.getType1());
        climate2T.setText(todayWeather.getType2());
        climate3T.setText(todayWeather.getType3());
        wind1T.setText(todayWeather.getFengli1());
        wind2T.setText(todayWeather.getFengli2());
        wind3T.setText(todayWeather.getFengli3());

        Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
    }

    void initView()
    {
        //title
        cityNameT = (TextView)getActivity().findViewById(R.id.title_city_name);

        //today weather
        cityT = (TextView)getActivity().findViewById(R.id.todayinfo1_cityName);
        timeT = (TextView)getActivity().findViewById(R.id.todayinfo1_updateTime);
        humidityT = (TextView)getActivity().findViewById(R.id.todayinfo1_humidity);
        weekT = (TextView)getActivity().findViewById(R.id.todayinfo2_week);
        pmDataT = (TextView)getActivity().findViewById(R.id.todayinfo1_pm25);
        pmQualityT = (TextView)getActivity().findViewById(R.id.todayinfo1_pm25status);
        temperatureT = (TextView)getActivity().findViewById(R.id.todayinfo2_temperature);
        climateT = (TextView)getActivity().findViewById(R.id.todayinfo2_weatherState);
        windT = (TextView)getActivity().findViewById(R.id.todayinfo2_wind);
        suggestNameT = (TextView) getActivity().findViewById(R.id.tv_fit);

        weatherStateImg = (ImageView)getActivity().findViewById(R.id.todayinfo2_weatherStatusImg);
        pmStateImg = (ImageView)getActivity().findViewById(R.id.todayinfo1_pm25img);

        //future weather
        week1T = (TextView)getActivity().findViewById(R.id.future3_no1_week);
        temperature1T = (TextView)getActivity().findViewById(R.id.future3_no1_temperature);
        climate1T = (TextView)getActivity().findViewById(R.id.future3_no1_weatherState);
        wind1T = (TextView)getActivity().findViewById(R.id.future3_no1_wind);

        week2T = (TextView)getActivity().findViewById(R.id.future3_no2_week);
        temperature2T = (TextView)getActivity().findViewById(R.id.future3_no2_temperature);
        climate2T = (TextView)getActivity().findViewById(R.id.future3_no2_weatherState);
        wind2T = (TextView)getActivity().findViewById(R.id.future3_no2_wind);

        week3T = (TextView)getActivity().findViewById(R.id.future3_no3_week);
        temperature3T = (TextView)getActivity().findViewById(R.id.future3_no3_temperature);
        climate3T = (TextView)getActivity().findViewById(R.id.future3_no3_weatherState);
        wind3T = (TextView)getActivity().findViewById(R.id.future3_no3_wind);


        cityNameT.setText("N/A");

        cityT.setText("N/A");
        timeT.setText("N/A");
        humidityT.setText("N/A");
        weekT.setText("N/A");
        pmDataT.setText("N/A");
        pmQualityT.setText("N/A");
        temperatureT.setText("N/A");
        climateT.setText("N/A");
        windT.setText("N/A");
        suggestNameT.setText("A/N");

        week1T.setText("N/A");
        temperature1T.setText("N/A");
        climate1T.setText("N/A");
        wind1T.setText("N/A");

        week2T.setText("N/A");
        temperature2T.setText("N/A");
        climate2T.setText("N/A");
        wind2T.setText("N/A");

        week3T.setText("N/A");
        temperature3T.setText("N/A");
        climate3T.setText("N/A");
        wind3T.setText("N/A");
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.title_city_update)
        {
            SharedPreferences mySharePre = getActivity().getSharedPreferences("CityCodePreference", Activity.MODE_PRIVATE);
            String sharecode = mySharePre.getString("citycode","");
            if(!sharecode.equals(""))
            {
                Log.d("sharecode",sharecode);
                getWeatherDatafromNet(sharecode);
            }else {
//                getWeatherDatafromNet("101010100");
                getWeatherDatafromNet("101190301");
                SharedPreferences.Editor edt = mySharePre.edit();
                edt.putString("citycode","101190301");
                edt.commit();
            }
        }
        if(v.getId()==R.id.title_city_manager)
        {
            Intent intent = new Intent(getActivity(),SelectCity.class);
            startActivity(intent);

        }
        if (v.getId()==R.id.more_suggest)
        {
            Intent intent = new Intent(getActivity(),MoreSuggestActivityLv.class);
            startActivity(intent);
        }
        if (v.getId()==R.id.title_city_share)
        {
            Intent intent = new Intent(getActivity(),MoreSuggestsActivity.class);
            startActivity(intent);
        }
    }
}