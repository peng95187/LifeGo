package com.lifegofood.jt3282.lifego.weather;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lifegofood.jt3282.lifego.R;


/**
 * Created by Blue_bell on 2017/12/19.
 */

public class WeatherFloatWindowBigView extends LinearLayout{
    /**
     * 记录大悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录大悬浮窗的高度
     */
    public static int viewHeight;

    public WeatherFloatWindowBigView(final Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.weather_float_window_big, this);
        View view = findViewById(R.id.big_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        Button close = (Button) findViewById(R.id.close);
        Button back = (Button) findViewById(R.id.back);
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击关闭悬浮窗的时候，移除所有悬浮窗，并停止Service
                WeatherMyWindowManager.removeBigWindow(context);
                WeatherMyWindowManager.removeSmallWindow(context);
                Intent intent = new Intent(getContext(), WeatherFloatWindowService.class);
                context.stopService(intent);
            }
        });
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击返回的时候，移除大悬浮窗，创建小悬浮窗
                WeatherMyWindowManager.removeBigWindow(context);
                WeatherMyWindowManager.createSmallWindow(context);
            }
        });
    }
}
