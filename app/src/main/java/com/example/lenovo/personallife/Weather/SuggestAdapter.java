package com.example.lenovo.personallife.Weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lenovo.personallife.R;

import java.util.ArrayList;

public class SuggestAdapter extends BaseAdapter{
    LayoutInflater layoutInflater;
    ArrayList<SuggestString> arrayList;
    public SuggestAdapter(LayoutInflater layoutInflater, ArrayList<SuggestString> arrayList){
        this.arrayList=arrayList;
        this.layoutInflater=layoutInflater;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SuggestViewHolder viewHolder=new SuggestViewHolder();
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.weather_listview,null);
            viewHolder.m_zhishu=(TextView)convertView.findViewById(R.id.zhishu);
            viewHolder.m_suggest=(TextView)convertView.findViewById(R.id.suggest);
            convertView.setTag(viewHolder);
        }
        viewHolder=(SuggestViewHolder)convertView.getTag();
        viewHolder.m_zhishu.setText(arrayList.get(position).getZhishu());
        viewHolder.m_suggest.setText(arrayList.get(position).getSuggest());

        return convertView;
    }
}
