package com.lifegofood.jt3282.lifego.traffic.railway;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lifegofood.jt3282.lifego.R;


public class NorthSouth extends Activity{

    Button north,south;
    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.north_south);

        String id = getIntent().getStringExtra("stationID");
        String name = getIntent().getStringExtra("station");
        String saveTime = getIntent().getStringExtra("saveTime");

        north = (Button)findViewById(R.id.north);
        south = (Button)findViewById(R.id.south);
        intent.setClass(NorthSouth.this,TBlist.class);
        intent.putExtra("stationID",id);
        intent.putExtra("stopname",name);
        intent.putExtra("saveTime",saveTime);

        north.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("direction","0");
                startActivity(intent);
                finish();
            }
        });
        south.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("direction","1");
                startActivity(intent);
                finish();
            }
        });
    }
}
