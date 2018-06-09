package com.example.lenovo.personallife.Alarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.personallife.MainActivity;
import com.example.lenovo.personallife.R;

import java.util.ArrayList;

public class AlarmFragment extends Fragment {
    private Button button;
    private ListView listView;
    private AlarmDateBase myDataBase;
    private LayoutInflater layoutInflater;
    private ArrayList<AlarmNote> arrayList;
//    private AlarmNote note;

    SharedPreferences sp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.alarm_main,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sp = getActivity().getSharedPreferences("alarmNote", Activity.MODE_PRIVATE);
        listView = (ListView) getActivity().findViewById(R.id.listView1_alarm);
        button = (Button) getActivity().findViewById(R.id.button1_alarm);

        //适配并且长按删除
        adapter();

        //增加闹钟按钮监听事件
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AlarmAdd.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void adapter() {
        layoutInflater = getActivity().getLayoutInflater();
        myDataBase = new AlarmDateBase(getActivity());
        arrayList = myDataBase.getArray();

        AlarmAdapter myAdapter =new AlarmAdapter(layoutInflater, arrayList);

        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), AlarmAdd.class);
                //传id到second
                intent.putExtra("id", arrayList.get(i).getId());
                startActivity(intent);
                getActivity().finish();
            }
        });
        //长按删除操作
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                new AlertDialog.Builder(getActivity()).setTitle("删除").setMessage("数据无价，谨慎操作!")
                        .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        myDataBase.deleteNote(arrayList.get(position).getId());
                        arrayList = myDataBase.getArray();
                        AlarmAdapter myAdapter = new AlarmAdapter(layoutInflater, arrayList);
                        listView.setAdapter(myAdapter);

                        SharedPreferences.Editor editor = sp.edit();
                        editor.clear().commit();

//                        Intent intent = new Intent(getActivity(), FriendMainTabFragment.class);
//                        startActivity(intent);
                        Intent intent=new Intent(getActivity(),MainActivity.class);
//            i.setClass(AlarmAdd.this,MainActivity.class);
                        intent.putExtra("id", 0);
                        startActivity(intent);

                        getActivity().finish();
                    }
                }).create().show();
                Log.d("删除", "长按");

                return true;
            }
        });
    }

    @Override
    public void onResume() {
            super.onResume();
            Log.d("onResume","refresh");


            String str = myDataBase.getDate();
            Log.d("time", str);
            Toast toast = Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT);
            toast.show();
            if (str!="nub1") {

            //注册receiver
            Intent intent = new Intent(getActivity(), NotifyService.class);
            getActivity().startService(intent);

//            myDataBase.test();
            SharedPreferences.Editor editor = sp.edit();
            AlarmNote note = myDataBase.getTitleAndContentByDate(str);
            editor.putString("text", note.getContent());
            editor.putString("time", note.getDate());
            editor.putString("title",note.getTitle());
//            editor.putString("nextAlarm",)

            String nextTime = myDataBase.getNextTime();
            editor.putString("nextTime",nextTime);
            Log.d("nextTime","存在吗"+nextTime);
            editor.commit();
            Log.d("你猜", note.getContent() + "我的getcontent");
            Log.d("你猜",note.getDate()+"我的getdate");
        }
    }
}