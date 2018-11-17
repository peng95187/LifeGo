package com.lifegofood.jt3282.lifego.traffic.ibus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;

public class IBusPrice extends Activity {

    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ibus_price);

        button = (Button) findViewById(R.id.search);
        editText = (EditText) findViewById(R.id.et_ibus);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = editText.getText().toString();
                if(result.equals("")||result==null){
                    Toast.makeText(IBusPrice.this, "未輸入", Toast.LENGTH_SHORT).show();
                }else if(result.matches("[0-9]+")){
                    Intent intent = new Intent(IBusPrice.this,IBusPrice_Result.class);
                    intent.putExtra("id",result);
                    startActivity(intent);
                    finish();
                }else Toast.makeText(IBusPrice.this, "代碼為數字組合", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
