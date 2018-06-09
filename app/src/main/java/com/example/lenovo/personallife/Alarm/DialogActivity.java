package com.example.lenovo.personallife.Alarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.lenovo.personallife.R;


public class DialogActivity extends Activity {

    SharedPreferences sp;
//    AlarmDateBase myDateBase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("alarmNote",MODE_PRIVATE);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // hide title
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        winParams.flags |= (WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        createDialog();
    }

    private void createDialog() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher)
                .setTitle(sp.getString("title","null"))
                .setMessage(sp.getString("text","null"))
//                .setPositiveButton("推迟10分钟", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
////                        tenMRemind();
//                        finish();
//                    }
//                })
                .setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }
                }).show();
    }
    /**
     * 推迟10分钟提醒
     */
//    private void tenMRemind(){
//        //设置时间
//        Calendar calendar_now =Calendar.getInstance();
//
//
//        calendar_now.setTimeInMillis(System.currentTimeMillis());
//        calendar_now.set(Calendar.HOUR_OF_DAY, calendar_now.get(Calendar.HOUR_OF_DAY));
//        calendar_now.set(Calendar.MINUTE, calendar_now.get(Calendar.MINUTE)+10);
//        calendar_now.set(Calendar.SECOND, 0);
//        calendar_now.set(Calendar.MILLISECOND, 0);
//
//        //时间选择好了
//        Intent intent = new Intent(this, NotifyService.class);
//        //注册闹钟广播
//        PendingIntent sender = PendingIntent.getBroadcast(
//                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        AlarmManager am;
//        am = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
//        am.set(AlarmManager.RTC_WAKEUP, calendar_now.getTimeInMillis(), sender);
//    }
}