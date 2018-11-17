package com.lifegofood.jt3282.lifego.traffic.ibus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lifegofood.jt3282.lifego.R;

public class IBusSelect extends Activity {

    Button price,search,stop,stoptostop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ibus_select);

        price = (Button) findViewById(R.id.price);
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(IBusSelect.this,IBusPrice.class);
                startActivity(intent);
            }
        });

        search = (Button) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(IBusSelect.this,IBusSearch.class);
                startActivity(intent);
            }
        });
        stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(IBusSelect.this,IBusStop.class);
                startActivity(intent);
            }
        });
        stoptostop = (Button) findViewById(R.id.stoptostop);
        stoptostop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(IBusSelect.this,IBus_stoptostop.class);
                startActivity(intent);
            }
        });
    }
}
