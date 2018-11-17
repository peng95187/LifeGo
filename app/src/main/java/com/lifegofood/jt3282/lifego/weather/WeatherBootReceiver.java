package com.lifegofood.jt3282.lifego.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by jt3282 on 2018/5/12.
 */

public class WeatherBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.example.jt3282.weather.destroy")) {
            //TODO
            //在这里写重新启动service的相关操作

            Log.i("service restart","ok");
            context.startService(new Intent(context,WeatherService.class));
        }
    }
}