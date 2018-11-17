package com.lifegofood.jt3282.lifego.weather;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.lifegofood.jt3282.lifego.FourChoose;
import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.weather.newweather.WeatherManually;

import static android.Manifest.permission.READ_CONTACTS;


public class WeatherActivity extends Activity {

    private static final int REQUEST_EXTERNAL_STORAGE = 100 ;
    int permission=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_main);

        ImageButton startFloatWindow = (ImageButton) findViewById(R.id.start_float_window);
        startFloatWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    //未取得權限，向使用者要求允許權限
                    ActivityCompat.requestPermissions(WeatherActivity.this,
                            new String[] {READ_CONTACTS},
                            REQUEST_EXTERNAL_STORAGE
                    );
                }else{
                    Intent intent = new Intent(WeatherActivity.this, WeatherFloatWindowService.class);
                    startService(intent);
                    finish();
                    FourChoose.instance.finish();
                }
            }
        });
        ImageButton startmanuallyWindow = (ImageButton) findViewById(R.id.start_manually_window);
        startmanuallyWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(WeatherActivity.this, WeatherManually.class);
                startActivity(intent);
                finish();
            }
        });

        LinearLayout check_command = (LinearLayout) findViewById(R.id.check_command);
        check_command.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, WeatherVoice_show.class);
                startActivity(intent);
            }
        });

    }
}
