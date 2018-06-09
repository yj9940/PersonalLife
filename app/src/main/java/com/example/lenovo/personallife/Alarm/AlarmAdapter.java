package com.example.lenovo.personallife.Alarm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.lenovo.personallife.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AlarmAdapter extends BaseAdapter{
    LayoutInflater layoutInflater;
    ArrayList<AlarmNote> arrayList;
    public AlarmAdapter(LayoutInflater layoutInflater, ArrayList<AlarmNote> arrayList){
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
        AlarmViewHolder viewHolder=new AlarmViewHolder();
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.alarm_adapter_listview,null);
            viewHolder.textView1=(TextView)convertView.findViewById(R.id.textview1_alarm);
            viewHolder.textView2=(TextView)convertView.findViewById(R.id.textview2_alarm);
            viewHolder.image= (ImageView) convertView.findViewById(R.id.iv_show_alarm);
            convertView.setTag(viewHolder);
        }
        viewHolder=(AlarmViewHolder)convertView.getTag();
        viewHolder.textView1.setText(arrayList.get(position).getTitle());
        viewHolder.textView2.setText(arrayList.get(position).getDate());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String curTime = sdf.format(new Date());

        if (arrayList.get(position).getDate().compareTo(curTime)<=0) {
            viewHolder.textView1.setTextColor(0xff808080);
            viewHolder.textView2.setTextColor(0xff808080);
            viewHolder.image.setVisibility(View.GONE);
        }
//        else
//        {
//            viewHolder.image.setVisibility(View.VISIBLE);
//        }
        return convertView;
    }
}