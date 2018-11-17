package com.lifegofood.jt3282.lifego.dm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;

import java.util.ArrayList;

/**
 * Created by Blue_bell on 2017/12/28.
 */

public class DMVoice extends Activity {

    private String all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dm_voice_layout);

        //透過 Intent 的方式開啟內建的語音辨識 Activity...
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "請說話..."); //語音辨識 Dialog 上要顯示的提示文字
        startActivityForResult(intent, 1);
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
                if(all.contains("電影")){
                    Intent i = new Intent(DMVoice.this,Movie_Hot.class);
                    startActivity(i);
                }else if(all.contains("日")&&all.contains("劇")){
                    Intent i = new Intent(DMVoice.this,Drama_Hot.class);
                    startActivity(i);
                } else if(all.contains("韓")&&all.contains("劇")){
                    Intent i = new Intent(DMVoice.this,Drama_Hot.class);
                    startActivity(i);
                }else if(all.contains("動")&&(all.contains("畫")||all.contains("漫"))){
                    Intent i = new Intent(DMVoice.this,Anime_Hot.class);
                    startActivity(i);
                }else {
                    Toast.makeText(this, "抱歉,小的聽不懂ˇˇ", Toast.LENGTH_SHORT).show();
                }
            }
        }
        finish();
    }
}
