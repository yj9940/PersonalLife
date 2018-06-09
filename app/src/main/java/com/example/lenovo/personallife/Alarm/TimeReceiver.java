package com.example.lenovo.personallife.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("recevice");
        context.startService(new Intent(context,NotifyService.class));
    }
}