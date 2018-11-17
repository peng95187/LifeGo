package com.lifegofood.jt3282.lifego.traffic.hsr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;

import java.util.Calendar;

/**
 * Created by jt3282 on 2018/3/8.
 */

public class HSRQSearch extends Activity {

    String [] Stop = {"請選擇", "南港", "雲林", "彰化", "台北", "板橋", "桃園", "新竹", "台中", "嘉義", "台南", "左營", "苗栗"};
    String [] StopID = {"0990", "1047", "1043", "1000", "1010", "1020", "1030", "1040", "1050", "1060", "1070", "1035"};
    Spinner sp1,sp2,dt;
    Button button;
    String save = "",save2 = "",stop = "",stop2 = "",saveTime = "";
    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hsr_qs);

        Calendar mCal = Calendar.getInstance();

        //插入時間
        final String [] date = new String[15];

        for(int i = 0;i < 15;i++){
            if(i>0) mCal.add(mCal.DATE,1);
            CharSequence s = DateFormat.format("yyyy-MM-dd", mCal.getTime());
            date[i] = ""+s;
        }

        intent.setClass(HSRQSearch.this,HSRQSearchDetail.class);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(HSRQSearch.this, android.R.layout.simple_spinner_dropdown_item, Stop);
        ArrayAdapter<String> dateap = new ArrayAdapter<>(HSRQSearch.this, R.layout.myspinner, date);

        button = (Button) findViewById(R.id.search);
        sp1 = (Spinner) findViewById(R.id.spinner1);
        sp2 = (Spinner) findViewById(R.id.spinner2);
        dt = (Spinner) findViewById(R.id.date);

        sp1.setAdapter(adapter);
        sp2.setAdapter(adapter);
        dt.setAdapter(dateap);

        dt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                saveTime = date[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    save = "unknown";
                }else {
                    stop = Stop[position];
                    save = StopID[position - 1];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    save2 = "unknown";
                }else {
                    stop2 = Stop[position];
                    save2 = StopID[position - 1];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(save.equals("unknown")||save2.equals("unknown")){
                    Toast.makeText(HSRQSearch.this, "未選擇", Toast.LENGTH_SHORT).show();
                }else if(save.equals(save2)) {
                    Toast.makeText(HSRQSearch.this, "選擇相同車站", Toast.LENGTH_SHORT).show();
                }else{
                    intent.putExtra("stationID",save);
                    intent.putExtra("saveTime",saveTime);
                    intent.putExtra("station",stop);
                    intent.putExtra("stationID2",save2);
                    intent.putExtra("station2",stop2);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
