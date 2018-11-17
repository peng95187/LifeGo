package com.lifegofood.jt3282.lifego.traffic.minelike;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lifegofood.jt3282.lifego.R;


public class MineLike_select extends Activity {

    Button like_stop,like_route;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_like_select);

        like_stop = (Button) findViewById(R.id.like_stop);
        like_route = (Button) findViewById(R.id.like_route);

        like_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineLike_select.this,Like_stop.class);
                startActivity(intent);
            }
        });

        like_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineLike_select.this,Like_route.class);
                startActivity(intent);
            }
        });
    }
}
