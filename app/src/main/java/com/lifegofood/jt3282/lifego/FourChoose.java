package com.lifegofood.jt3282.lifego;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;

import com.lifegofood.jt3282.lifego.food.login.LoginActivity;
import com.lifegofood.jt3282.lifego.traffic.BusService;
import com.lifegofood.jt3282.lifego.traffic.HSRService;
import com.lifegofood.jt3282.lifego.traffic.TRAService;
import com.lifegofood.jt3282.lifego.traffic.TrafficChoose;
import com.lifegofood.jt3282.lifego.traffic.TrafficGuardService;
import com.lifegofood.jt3282.lifego.weather.WeatherActivity;
import com.lifegofood.jt3282.lifego.weather.WeatherGuardService;
import com.lifegofood.jt3282.lifego.weather.WeatherService;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;

public class FourChoose extends Activity {

    Button food, weather,traffic,movie;
    public static FourChoose instance;
    private static final int REQUEST_EXTERNAL_STORAGE = 100 ;
    int permission=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.four_choose);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            permission = android.support.v4.app.ActivityCompat.checkSelfPermission(FourChoose.this,
                    ACCESS_FINE_LOCATION);
        }
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //未取得權限，向使用者要求允許權限
            ActivityCompat.requestPermissions(FourChoose.this,
                    new String[] {READ_CONTACTS,ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION},
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        instance = this;

        SharedPreferences settings = getSharedPreferences("ServiceCheck",0);

        //判斷警報服務是否啟用
        if(settings.getString("ischecked","").equals("true")){
            startService(new Intent(FourChoose.this, WeatherService.class));
            startService(new Intent(FourChoose.this, WeatherGuardService.class));
        }
        startService(new Intent(FourChoose.this, HSRService.class));
        startService(new Intent(FourChoose.this, TrafficGuardService.class));
        startService(new Intent(FourChoose.this, TRAService.class));
        startService(new Intent(FourChoose.this, BusService.class));

        food = (Button) findViewById(R.id.food);
        weather = (Button) findViewById(R.id.weather);
        traffic = (Button) findViewById(R.id.traffic);
        movie = (Button) findViewById(R.id.movie);

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FourChoose.this,LoginActivity.class);
                intent.putExtra("whichbtn","food");
                startActivity(intent);
            }
        });

        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FourChoose.this,WeatherActivity.class);
                startActivity(intent);
            }
        });

        traffic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FourChoose.this,TrafficChoose.class);
                startActivity(intent);
            }
        });

        movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FourChoose.this,LoginActivity.class);
                intent.putExtra("whichbtn","dm");
                startActivity(intent);
            }
        });
    }
}
