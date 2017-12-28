package com.example.user.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Voice extends Activity {

    private Button btnDialog;
    private TextView textView;
    private String all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice_layout);

      //  View mFootView = getLayoutInflater().inflate(R.layout.float_window_big,null);
        textView = (TextView) findViewById(R.id.tv1);
       // Button button = (Button) findViewById(R.id.btn1);

       // button.setOnClickListener(new View.OnClickListener() {
         //   @Override
         //   public void onClick(View v) {

                //透過 Intent 的方式開啟內建的語音辨識 Activity...
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "請說話..."); //語音辨識 Dialog 上要顯示的提示文字

                startActivityForResult(intent, 1);
        //    }
       // });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //把所有辨識的可能結果印出來看一看，第一筆是最 match 的。

                ArrayList result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                all = "";
                for (Object r : result) {
                    all = all + r + "\n";
                }
                if(all.contains("你好")) Toast.makeText(this, "幹你老師", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }


}
