package com.lifegofood.jt3282.lifego.traffic.railway;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lifegofood.jt3282.lifego.R;


public class TRASelectSearch extends Activity {

    Button qsearch,nearly,timeboard,price,train;
    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traselect_search);

        qsearch = (Button)findViewById(R.id.qsearch) ;
        nearly = (Button)findViewById(R.id.nearly);
        timeboard = (Button)findViewById(R.id.timeboard);
        price = (Button)findViewById(R.id.price);
        train = (Button)findViewById(R.id.train);


        qsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(TRASelectSearch.this,TRAQsearch.class);
                startActivity(intent);
            }
        });
        nearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(TRASelectSearch.this,Railway.class);
                startActivity(intent);
            }
        });
        timeboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(TRASelectSearch.this,Timeboard.class);
                startActivity(intent);
            }
        });
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(TRASelectSearch.this,TRAPrice.class);
                startActivity(intent);
            }
        });
        train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(TRASelectSearch.this,TrainSearch.class);
                startActivity(intent);
            }
        });
    }
}
