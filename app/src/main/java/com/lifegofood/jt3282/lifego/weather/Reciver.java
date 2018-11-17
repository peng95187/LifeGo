package com.lifegofood.jt3282.lifego.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by jt3282 on 2018/4/30.
 */

public class Reciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Intent service = new Intent(context,WeatherService.class);
            context.startService(service);
        }
    }
}
