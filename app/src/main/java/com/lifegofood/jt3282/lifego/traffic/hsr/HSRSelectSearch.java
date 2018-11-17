package com.lifegofood.jt3282.lifego.traffic.hsr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lifegofood.jt3282.lifego.R;

public class HSRSelectSearch extends Activity {

    Button qsearch,nearly,news,price,train;
    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hsrselect_search);

        qsearch = (Button)findViewById(R.id.qsearch) ;
        nearly = (Button)findViewById(R.id.station);
        price = (Button)findViewById(R.id.price);
        train = (Button)findViewById(R.id.train);
        news = (Button)findViewById(R.id.news);

        qsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(HSRSelectSearch.this,HSRQSearch.class);
                startActivity(intent);
            }
        });

        nearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(HSRSelectSearch.this,HSRStation.class);
                startActivity(intent);
            }
        });

        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(HSRSelectSearch.this,HSRPrice.class);
                startActivity(intent);
            }
        });
        train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(HSRSelectSearch.this,HSRSearch.class);
                startActivity(intent);
            }
        });

        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(HSRSelectSearch.this,HSRNews.class);
                startActivity(intent);
            }
        });
    }
}
