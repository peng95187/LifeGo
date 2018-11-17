package com.lifegofood.jt3282.lifego.food;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;


import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.food.fab.SelectFunction;

public class CheckCommand extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_command);

        ImageButton imageButton = (ImageButton)findViewById(R.id.check_command);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CheckCommand.this, SelectFunction.class);
                startActivity(intent);
                CheckCommand.this.finish();
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            Intent intent = new Intent();
            intent.setClass(CheckCommand.this, SelectFunction.class);
            startActivity(intent);
            CheckCommand.this.finish();
        }
        return true;
    }
}
