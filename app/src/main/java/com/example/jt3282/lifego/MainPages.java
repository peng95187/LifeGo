package com.example.jt3282.lifego;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Created by jt3282 on 2017/11/20.
 */

public class MainPages extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button button_google = (Button)findViewById(R.id.button_google);
        button_google.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RelativeLayout relativeLayout =(RelativeLayout)findViewById(R.id.loadingPanel);
                        relativeLayout.setVisibility(View.VISIBLE);
                    }
                });

                //開另一個Thread 關閉
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent();
                        intent.setClass(MainPages.this, MainActivity.class);
                        startActivity(intent);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.loadingPanel);
                                relativeLayout.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }).start();
            }
        });
    }
}
