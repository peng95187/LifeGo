package com.lifegofood.jt3282.lifego.traffic;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;

import com.lifegofood.jt3282.lifego.FourChoose;
import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.traffic.bus.CountySelect;
import com.lifegofood.jt3282.lifego.traffic.hsr.HSRSelectSearch;
import com.lifegofood.jt3282.lifego.traffic.ibus.IBusSelect;
import com.lifegofood.jt3282.lifego.traffic.minelike.MineLike_select;
import com.lifegofood.jt3282.lifego.traffic.notify.Notification;
import com.lifegofood.jt3282.lifego.traffic.railway.TRASelectSearch;
import com.lifegofood.jt3282.lifego.traffic.transfer.Transfer_Select;
import com.lifegofood.jt3282.lifego.traffic.voice.TrafficFloatWindowService;
import com.lifegofood.jt3282.lifego.traffic.voice.TrafficVoice_showCommand;

import static android.Manifest.permission.READ_CONTACTS;

public class TrafficChoose extends Activity {

    Button bus,railway,hsr,ibus,transfer,like,voice,notify;
    Intent intent = new Intent();
    Intent service = new Intent();
    Intent service2 = new Intent();
    Intent service3 = new Intent();
    Button voice_show;
    private static final int REQUEST_EXTERNAL_STORAGE = 100 ;
    int permission=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traffic_choose);

        service.setClass(TrafficChoose.this,HSRService.class);
        startService(service);
        service2.setClass(TrafficChoose.this,TRAService.class);
        startService(service2);
        service3.setClass(TrafficChoose.this,BusService.class);
        startService(service3);

        railway = (Button) findViewById(R.id.railway);
        hsr = (Button) findViewById(R.id.HSR);
        railway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(TrafficChoose.this,TRASelectSearch.class);
                startActivity(intent);
            }
        });
        hsr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(TrafficChoose.this,HSRSelectSearch.class);
                startActivity(intent);
            }
        });

        bus = (Button)findViewById(R.id.bus);
        bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(TrafficChoose.this,CountySelect.class);
                startActivity(intent);
            }
        });

        ibus = (Button)findViewById(R.id.RB);
        ibus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(TrafficChoose.this,IBusSelect.class);
                startActivity(intent);
            }
        });

        transfer = (Button)findViewById(R.id.TS);
        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(TrafficChoose.this,Transfer_Select.class);
                startActivity(intent);
            }
        });

        like = (Button)findViewById(R.id.LIKE);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(TrafficChoose.this,MineLike_select.class);
                startActivity(intent);
            }
        });

        voice = (Button)findViewById(R.id.voice);
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    //未取得權限，向使用者要求允許權限
                    ActivityCompat.requestPermissions(TrafficChoose.this,
                            new String[] {READ_CONTACTS},
                            REQUEST_EXTERNAL_STORAGE
                    );
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!Settings.canDrawOverlays(TrafficChoose.this)) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intent, 10);
                        } else {
                            Intent intent = new Intent(TrafficChoose.this, TrafficFloatWindowService.class);
                            startService(intent);
                            finish();
                            FourChoose.instance.finish();
                        }
                    } else {
                        Intent intent = new Intent(TrafficChoose.this, TrafficFloatWindowService.class);
                        startService(intent);
                        finish();
                        FourChoose.instance.finish();
                    }
                }
            }
        });

        notify = (Button)findViewById(R.id.notify);
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(TrafficChoose.this,Notification.class);
                startActivity(intent);
            }
        });

        voice_show = (Button) findViewById(R.id.voice_show);
        voice_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(TrafficChoose.this,TrafficVoice_showCommand.class);
                startActivity(intent2);
            }
        });
    }
}
