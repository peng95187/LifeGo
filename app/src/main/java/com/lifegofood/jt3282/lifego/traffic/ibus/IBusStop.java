package com.lifegofood.jt3282.lifego.traffic.ibus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;

public class IBusStop extends Activity {

    Button button;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ibus_stop);

        button = (Button) findViewById(R.id.search);
        editText = (EditText) findViewById(R.id.et_ibus);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = editText.getText().toString();
                if(result.equals("")||result==null){
                    Toast.makeText(IBusStop.this, "未輸入", Toast.LENGTH_SHORT).show();
                }else if(result.matches("[0-9]+")){
                    Intent intent = new Intent(IBusStop.this,IBusStop_list.class);
                    intent.putExtra("enter",result);
                    intent.putExtra("type","code");
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(IBusStop.this,IBusStop_list.class);
                    intent.putExtra("enter",result);
                    intent.putExtra("type","stop");
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
