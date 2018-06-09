package com.example.lenovo.personallife.Weather.CityChoose;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;


import com.example.lenovo.personallife.MainActivity;
import com.example.lenovo.personallife.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

//import com.example.lenovo.functiontest4.CityChoose.*;
//import com.example.lenovo.functiontest4.CityChoose.City;
//import com.example.lenovo.functiontest4.CityChoose.CityDB;

public class SelectCity extends Activity implements View.OnClickListener{

    private List<com.example.lenovo.personallife.Weather.CityChoose.City> mCityList;
    private com.example.lenovo.personallife.Weather.CityChoose.CityDB mCityDB;

    private ListView cityListLv;
    private EditText searchEt;
    private ImageView searchBtn;
    private ArrayList<String> mArrayList;
    ArrayAdapter<String> adapter;
    private String updateCityCode = "-1";
    private String selectNo;

    boolean searched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);

        searchEt = (EditText)findViewById(R.id.selectcity_search);
        searchBtn = (ImageView)findViewById(R.id.selectcity_search_button);
        searchBtn.setOnClickListener(this);

        mCityDB = openCityDB();
        initCityList();

    }

    private void adapter(List<com.example.lenovo.personallife.Weather.CityChoose.City> cl) {
        mArrayList = new ArrayList<String>();//不new会指向空
        for(int i=0;i<cl.size();i++)
        {
            String No_ = Integer.toString(i+1);
            String number= cl.get(i).getNumber();
            String provinceName = cl.get(i).getProvince();
            String cityName = cl.get(i).getCity();
            mArrayList.add("NO."+No_+":"+number+"-"+provinceName+"-"+cityName);
        }
        cityListLv = (ListView)findViewById(R.id.selectcity_lv);
        adapter = new ArrayAdapter<String>(com.example.lenovo.personallife.Weather.CityChoose.SelectCity.this,android.R.layout.simple_list_item_1,mArrayList);
        adapter.notifyDataSetChanged();
        cityListLv.setAdapter(adapter);
    }


    //为读取数据库准备的操作
    public com.example.lenovo.personallife.Weather.CityChoose.CityDB openCityDB()
    {
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + getPackageName()
                + File.separator + "databases"
                + File.separator + com.example.lenovo.personallife.Weather.CityChoose.CityDB.CITY_DB_NAME;
        Log.d("file path", path);
        File db = new File(path);
        Log.d("db",path);

        try {
            InputStream is = getAssets().open("city.db");

            FileOutputStream fos = new FileOutputStream(db);
            int len = -1;
            byte[] buffer = new byte[1024];
            while((len = is.read(buffer))!=-1)
            {
                fos.write(buffer,0,len);
                //Log.d("write buffer",buffer.toString());
                fos.flush();
            }
            fos.close();
            is.close();
        }catch (Exception e)
        {
            e.printStackTrace();
            System.exit(0);
        }

        return new CityDB(this,path);
    }

    private List<City> prepareCityList()
    {
        List<City> cityList = null;
        cityList = mCityDB.getCityList();
        for(City city:cityList)
        {
            String cityName = city.getCity();
            Log.d("CityDB",cityName);
        }
        Log.d("length1", cityList.size() + "");
        return cityList;
    }

    private void initCityList()
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                mCityList = prepareCityList();
                Log.d("length2",mCityList.size()+"");
                Message message = new Message();
                message.what = 4;
                message.obj = mCityList;
                mHandler.sendMessage(message);
            }
        }).start();
    }

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message message)
        {
            switch (message.what)
            {
                case 4:
                    adapter((List<City>) message.obj);
                    dosth();
                    break;
                default:
                    break;
            }
        }
    };

    private void dosth() {

//        final Intent intent = new Intent(this,MainActivity.class);
        final Intent intent = new Intent(this,MainActivity.class).setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        //添加ListView项的点击事件的动作
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(searched)
                {
                    updateCityCode = mCityList.get(Integer.parseInt(selectNo)).getNumber();
                }else {
                    updateCityCode = mCityList.get(position).getNumber();
                }
                Log.d("update city code", updateCityCode);

                //用Shareperference 存储最近一次的citycode
                SharedPreferences sharedPreferences = getSharedPreferences("CityCodePreference",Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("citycode",updateCityCode);
                editor.commit();

                intent.putExtra("id",1);
                startActivity(intent);
                finish();
            }
        };
        //为组件绑定监听
        cityListLv.setOnItemClickListener(itemClickListener);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.selectcity_search_button:
                String cityKey = searchEt.getText().toString();
                Log.d("Search",cityKey);
                //ArrayList<String> mSearchList = new ArrayList<String>();
                for(int i=0;i<mCityList.size();i++)
                {
                    String No_ = Integer.toString(i+1);
                    String number= mCityList.get(i).getNumber();
                    String provinceName = mCityList.get(i).getProvince();
                    String cityName = mCityList.get(i).getCity();
                    if(number.equals(cityKey)||cityName.equals(cityKey)) {
                        searched = true;
                        selectNo = Integer.toString(i);
                        mArrayList.clear();
                        mArrayList.add("NO." + No_ + ":" + number + "-" + provinceName + "-" + cityName);
                        Log.d("changed adapter data","NO." + No_ + ":" + number + "-" + provinceName + "-" + cityName);
                    }

                    adapter = new ArrayAdapter<String>(com.example.lenovo.personallife.Weather.CityChoose.SelectCity.this,android.R.layout.simple_list_item_1,mArrayList);
                    cityListLv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            default:
                break;
        }
    }
}