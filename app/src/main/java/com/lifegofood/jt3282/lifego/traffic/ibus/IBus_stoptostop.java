package com.lifegofood.jt3282.lifego.traffic.ibus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;

public class IBus_stoptostop extends Activity {

    EditText start, end;
    Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ibus_stoptostop);

        start = (EditText) findViewById(R.id.start);
        end = (EditText) findViewById(R.id.end);
        search = (Button) findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = start.getText().toString();
                String e = end.getText().toString();

                if(s.equals("")||e.equals("")){
                    Toast.makeText(IBus_stoptostop.this, "未輸入", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(IBus_stoptostop.this, IBus_sts_result.class);
                    intent.putExtra("start",s);
                    intent.putExtra("end",e);
                    startActivity(intent);
                }
            }
        });
    }
}
