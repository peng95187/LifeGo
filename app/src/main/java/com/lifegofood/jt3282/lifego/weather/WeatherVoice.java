package com.lifegofood.jt3282.lifego.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.weather.newweather.WeatherManually;

import java.util.ArrayList;

/**
 * Created by Blue_bell on 2017/12/28.
 */

public class WeatherVoice extends Activity {
    private String updatecitycode = "-1";
    private String cityname;
    private String all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_voice_layout);

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
                if(all.contains("停班")||all.contains("停課")){
                    Intent intent = new Intent(this,WeatherInformation.class);
                    startActivity(intent);
                }
                else if(all.contains("警報")){
                    Intent intent = new Intent(this,InstanceWeatherService.class);
                    startService(intent);
                }
                else if(all.contains("天氣"))
                {
                    Intent intent = new Intent(this,WeatherManually.class);
                    startActivity(intent);
                }
            }
        }
        finish();
    }


}
