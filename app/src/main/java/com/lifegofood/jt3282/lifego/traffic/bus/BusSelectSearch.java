package com.lifegofood.jt3282.lifego.traffic.bus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lifegofood.jt3282.lifego.R;

public class BusSelectSearch extends Activity {

    Button routes, stops, stoptostop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_select_way);

        routes = (Button) findViewById(R.id.routes);
        stops = (Button) findViewById(R.id.stops);
        stoptostop = (Button) findViewById(R.id.stoptostop);

        routes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("location",getIntent().getStringExtra("location"));
                intent.setClass(BusSelectSearch.this,BusRoute.class);
                startActivity(intent);
            }
        });

        stops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("location",getIntent().getStringExtra("location"));
                intent.setClass(BusSelectSearch.this,BusStop.class);
                startActivity(intent);
            }
        });

        stoptostop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("location",getIntent().getStringExtra("location"));
                intent.setClass(BusSelectSearch.this,Bus_stoptostop.class);
                startActivity(intent);
            }
        });
    }
}
