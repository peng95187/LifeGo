package com.lifegofood.jt3282.lifego.food.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.dm.DMChoose;
import com.lifegofood.jt3282.lifego.food.fab.SelectFunction;

/**
 * Created by jt3282 on 2017/11/28.
 */

public class LogoActivity extends Activity {

    private Button backButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo);
        Intent intent;

        backButton = (Button) findViewById(R.id.btn_back);
        if(getIntent().getStringExtra("whichbtn").equals("food")) {
            intent = new Intent(this, SelectFunction.class);
        }else{
            intent = new Intent(this, DMChoose.class);
        }
        LogoActivity.this.startActivity(intent);
        finish();

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}