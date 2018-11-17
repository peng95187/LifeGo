package com.lifegofood.jt3282.lifego.traffic.notify;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.traffic.BusService;


public class Notification_add extends Activity implements CompoundButton.OnCheckedChangeListener{

    RadioButton type_bus,type_ibus
            ,times_once,times_every,times_other;
    CheckBox mon,tue,thi,thu,fri,sat,sun;
    Button timeset_btn,save;
    LinearLayout week_choose;
    String type = "",times = "",time = "08:00";
    Boolean [] week = new Boolean[7];
    String [] weekpick = {"一","二","三","四","五","六","日"};
    EditText editText;
    TextView textView;
    SharedPreferences sp = null;
    SharedPreferences.Editor spe = null;
    Intent service = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_add);

        service.setClass(Notification_add.this, BusService.class);

        sp = getSharedPreferences("mypref_selfnotify", MODE_PRIVATE);
        spe = getSharedPreferences("mypref_selfnotify", MODE_PRIVATE).edit();

        type_bus = (RadioButton) findViewById(R.id.type_bus);
        type_ibus = (RadioButton) findViewById(R.id.type_ibus);
        times_once = (RadioButton) findViewById(R.id.times_once);
        times_every = (RadioButton) findViewById(R.id.times_every);
        times_other = (RadioButton) findViewById(R.id.times_other);
        mon = (CheckBox) findViewById(R.id.mon);
        tue = (CheckBox) findViewById(R.id.tue);
        thi = (CheckBox) findViewById(R.id.thi);
        thu = (CheckBox) findViewById(R.id.thu);
        fri = (CheckBox) findViewById(R.id.fri);
        sat = (CheckBox) findViewById(R.id.sat);
        sun = (CheckBox) findViewById(R.id.sun);
        week_choose = (LinearLayout) findViewById(R.id.week_choose);
        timeset_btn = (Button) findViewById(R.id.timeset_btn);
        save = (Button) findViewById(R.id.save);
        editText = (EditText) findViewById(R.id.edittext);
        textView = (TextView) findViewById(R.id.time);
        textView.setText(time);

        if(type_bus.isChecked()){ type = "市區公車"; }
        if(times_once.isChecked()){ times = "一次"; }

        for(int i=0;i<7;i++){
            week[i] = false;
        }

        type_bus.setOnCheckedChangeListener(this);
        type_ibus.setOnCheckedChangeListener(this);
        times_once.setOnCheckedChangeListener(this);
        times_every.setOnCheckedChangeListener(this);
        times_other.setOnCheckedChangeListener(this);
        mon.setOnCheckedChangeListener(this);
        tue.setOnCheckedChangeListener(this);
        thi.setOnCheckedChangeListener(this);
        thu.setOnCheckedChangeListener(this);
        fri.setOnCheckedChangeListener(this);
        sat.setOnCheckedChangeListener(this);
        sun.setOnCheckedChangeListener(this);

        timeset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Notification_add.this);
                dialog.setTitle("設定時間");

                final TimePicker timePicker = new TimePicker(Notification_add.this);
                dialog.setView(timePicker);

                dialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //not to do
                    }

                });
                dialog.setPositiveButton("確定",new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        time = timePicker.getHour() + ":" + ((timePicker.getMinute()<10)?("0"+timePicker.getMinute()):timePicker.getMinute());
                        textView.setText(time);
                    }
                });
                dialog.show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!type.equals("")){
                    if(!times.equals("")||(times.equals("")&&(week[0]==true||week[1]==true||week[2]==true||week[3]==true||week[4]==true||week[5]==true
                            ||week[6]==true))){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Notification_add.this);
                        dialog.setTitle("確定儲存");

                        dialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                //not to do
                            }

                        });
                        dialog.setPositiveButton("確定",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                String daypick = "";

                                for(int i=0;i<7;i++){
                                    if(week[i]==true){
                                        daypick += (weekpick[i]+",");
                                    }
                                }
                                for (int i = 0; i < 5; i++) {
                                    if (sp.getString("type" + i, "").equals("")) {
                                        spe.putString("type"+i, type);
                                        spe.putString("info"+i, editText.getText().toString().equals("")?"出門搭車囉 !":editText.getText().toString());
                                        spe.putString("time"+i, times);
                                        spe.putString("pick"+i, daypick);
                                        spe.putString("arr_time"+i, time);
                                        Toast.makeText(Notification_add.this, "設定成功", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(Notification_add.this,Notification.class);
                                        startActivity(intent);
                                        startService(service);
                                        spe.commit();
                                        break;
                                    }else if (i == 4 && !sp.getString("type" + i, "").equals("")) {
                                        Toast.makeText(Notification_add.this, "個人提醒數上限 5", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                        dialog.show();
                    }
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView==type_bus){
            if(isChecked){
                type = "市區公車";
            }
        }
        if(buttonView==type_ibus){
            if(isChecked){
                type = "公路客運";
            }
        }
        if(buttonView==times_once){
            if(isChecked){
                times = "一次";
            }
        }
        if(buttonView==times_every){
            if(isChecked){
                times = "每日";
            }
        }
        if(buttonView==times_other){
            if(isChecked){
                times = "";
                week_choose.setVisibility(View.VISIBLE);
            }else{
                week_choose.setVisibility(View.GONE);
            }
        }
        if(buttonView==mon){
            if(times.equals("")) {
                if (isChecked) {
                    week[0] = true;
                } else {
                    week[0] = false;
                }
            }
        }
        if(buttonView==tue){
            if(times.equals("")) {
                if (isChecked) {
                    week[1] = true;
                } else {
                    week[1] = false;
                }
            }
        }
        if(buttonView==thi){
            if(times.equals("")) {
                if (isChecked) {
                    week[2] = true;
                } else {
                    week[2] = false;
                }
            }
        }
        if(buttonView==thu){
            if(times.equals("")) {
                if (isChecked) {
                    week[3] = true;
                } else {
                    week[3] = false;
                }
            }
        }
        if(buttonView==fri){
            if(times.equals("")) {
                if (isChecked) {
                    week[4] = true;
                } else {
                    week[4] = false;
                }
            }
        }
        if(buttonView==sat){
            if(times.equals("")) {
                if (isChecked) {
                    week[5] = true;
                } else {
                    week[5] = false;
                }
            }
        }
        if(buttonView==sun){
            if(times.equals("")) {
                if (isChecked) {
                    week[6] = true;
                } else {
                    week[6] = false;
                }
            }
        }
    }
}
