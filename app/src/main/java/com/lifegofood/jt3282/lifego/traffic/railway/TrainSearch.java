package com.lifegofood.jt3282.lifego.traffic.railway;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;

public class TrainSearch extends Activity {

    EditText editText;
    Button button;
    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.train_search);

        intent.setClass(TrainSearch.this,TrainSearchDetail.class);
        editText = (EditText)findViewById(R.id.edt_train);
        button = (Button)findViewById(R.id.search);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = editText.getText().toString();
                if(text.equals("")){
                    Toast.makeText(TrainSearch.this, "未輸入", Toast.LENGTH_SHORT).show();
                }else if(text.matches("[0-9]+")){
                    intent.putExtra("id",text);
                    startActivity(intent);
                    finish();
                }else Toast.makeText(TrainSearch.this, "車次ID為數字組合", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
