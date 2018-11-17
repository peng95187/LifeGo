package com.lifegofood.jt3282.lifego.weather.cityname;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.weather.newweather.WeatherManually;
import com.lifegofood.jt3282.lifego.weather.newweather.SelectCity;

/**
 * Created by user on 2018/8/27.
 */

public class Hsinchu extends Activity implements View.OnClickListener{
    private TextView city;
    private ImageView backBtn;
    private ListView cityListLv;
    private String updatecitycode = "-1";
    String citycode = "09";
    int weathercode;
    private String cityname,countryname;
        //選擇城市
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.select_country);
            cityname = getIntent().getStringExtra("cityname");
            backBtn = (ImageView)findViewById(R.id.title_selectCity_back);
            backBtn.setOnClickListener(this);


            final String[] listData = {"峨眉鄉","關西鎮","芎林鄉","湖口鄉","新豐鄉","新埔鎮","橫山鄉","北埔鄉","寶山鄉","五峰鄉","尖石鄉","竹東鎮","竹北市"};
            cityListLv = (ListView)findViewById(R.id.selectcity_lv);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Hsinchu.this,android.R.layout.simple_list_item_1,listData);
            cityListLv.setAdapter(adapter);

            AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    String text = listData[position];
                    //String result = "索引值:" + position + "\n" + "內容" + text;
                    String result = "已選擇:"+text;
                    Toast.makeText(Hsinchu.this, result,Toast.LENGTH_SHORT).show();

                    if(text=="峨眉鄉"){
                        updatecitycode = "03";
                        city = (TextView)findViewById(R.id.title_selectCity_name);
                        city.setText("當前城市:峨眉鄉");
                        countryname = text;
                        weathercode = 0;
                        Intent intent = new Intent(Hsinchu.this, WeatherManually.class);
                        intent.putExtra("cityname",cityname);
                        intent.putExtra("countryname",countryname);
                        intent.putExtra("citycode",citycode);
                        intent.putExtra("weathercode",weathercode);
                        startActivity(intent);
                    }else if(text=="關西鎮"){
                        updatecitycode = "01";
                        city = (TextView)findViewById(R.id.title_selectCity_name);
                        city.setText("當前城市:關西鎮");
                        countryname = text;
                        weathercode = 1;
                        Intent intent = new Intent(Hsinchu.this, WeatherManually.class);
                        intent.putExtra("cityname",cityname);
                        intent.putExtra("countryname",countryname);
                        intent.putExtra("citycode",citycode);
                        intent.putExtra("weathercode",weathercode);
                        startActivity(intent);
                    }else if(text=="芎林鄉"){
                        updatecitycode = "04";
                        city = (TextView)findViewById(R.id.title_selectCity_name);
                        city.setText("當前城市:芎林鄉");
                        countryname = text;
                        weathercode = 2;
                        Intent intent = new Intent(Hsinchu.this, WeatherManually.class);
                        intent.putExtra("cityname",cityname);
                        intent.putExtra("countryname",countryname);
                        intent.putExtra("citycode",citycode);
                        intent.putExtra("weathercode",weathercode);
                        startActivity(intent);
                    }else if(text=="湖口鄉"){
                        updatecitycode = "05";
                        city = (TextView)findViewById(R.id.title_selectCity_name);
                        city.setText("當前城市:湖口鄉");
                        countryname = text;
                        weathercode = 3;
                        Intent intent = new Intent(Hsinchu.this, WeatherManually.class);
                        intent.putExtra("cityname",cityname);
                        intent.putExtra("countryname",countryname);
                        intent.putExtra("citycode",citycode);
                        intent.putExtra("weathercode",weathercode);
                        startActivity(intent);
                    }else if(text=="新豐鄉"){
                        updatecitycode = "14";
                        city = (TextView)findViewById(R.id.title_selectCity_name);
                        city.setText("當前城市:新豐鄉");
                        countryname = text;
                        weathercode = 4;
                        Intent intent = new Intent(Hsinchu.this, WeatherManually.class);
                        intent.putExtra("cityname",cityname);
                        intent.putExtra("countryname",countryname);
                        intent.putExtra("citycode",citycode);
                        intent.putExtra("weathercode",weathercode);
                        startActivity(intent);
                    }else if(text=="新埔鎮"){
                        updatecitycode = "06";
                        city = (TextView)findViewById(R.id.title_selectCity_name);
                        city.setText("當前城市:新埔鎮");
                        countryname = text;
                        weathercode = 5;
                        Intent intent = new Intent(Hsinchu.this, WeatherManually.class);
                        intent.putExtra("cityname",cityname);
                        intent.putExtra("countryname",countryname);
                        intent.putExtra("citycode",citycode);
                        intent.putExtra("weathercode",weathercode);
                        startActivity(intent);
                    }else if(text=="橫山鄉"){
                        updatecitycode = "17";
                        city = (TextView)findViewById(R.id.title_selectCity_name);
                        city.setText("當前城市:橫山鄉");
                        countryname = text;
                        weathercode = 6;
                        Intent intent = new Intent(Hsinchu.this, WeatherManually.class);
                        intent.putExtra("cityname",cityname);
                        intent.putExtra("countryname",countryname);
                        intent.putExtra("citycode",citycode);
                        intent.putExtra("weathercode",weathercode);
                        startActivity(intent);
                    }else if(text=="北埔鄉"){
                        updatecitycode = "07";
                        city = (TextView)findViewById(R.id.title_selectCity_name);
                        city.setText("當前城市:北埔鄉");
                        countryname = text;
                        weathercode = 7;
                        Intent intent = new Intent(Hsinchu.this, WeatherManually.class);
                        intent.putExtra("cityname",cityname);
                        intent.putExtra("countryname",countryname);
                        intent.putExtra("citycode",citycode);
                        intent.putExtra("weathercode",weathercode);
                        startActivity(intent);
                    }else if(text=="寶山鄉"){
                        updatecitycode = "08";
                        city = (TextView)findViewById(R.id.title_selectCity_name);
                        city.setText("當前城市:寶山鄉");
                        countryname = text;
                        weathercode = 8;
                        Intent intent = new Intent(Hsinchu.this, WeatherManually.class);
                        intent.putExtra("cityname",cityname);
                        intent.putExtra("countryname",countryname);
                        intent.putExtra("citycode",citycode);
                        intent.putExtra("weathercode",weathercode);
                        startActivity(intent);
                    }else if(text=="五峰鄉"){
                        updatecitycode = "09";
                        city = (TextView)findViewById(R.id.title_selectCity_name);
                        city.setText("當前城市:五峰鄉");
                        countryname = text;
                        weathercode = 9;
                        Intent intent = new Intent(Hsinchu.this, WeatherManually.class);
                        intent.putExtra("cityname",cityname);
                        intent.putExtra("countryname",countryname);
                        intent.putExtra("citycode",citycode);
                        intent.putExtra("weathercode",weathercode);
                        startActivity(intent);
                    }else if(text=="尖石鄉"){
                        updatecitycode = "10";
                        city = (TextView)findViewById(R.id.title_selectCity_name);
                        city.setText("當前城市:尖石鄉");
                        countryname = text;
                        weathercode = 10;
                        Intent intent = new Intent(Hsinchu.this, WeatherManually.class);
                        intent.putExtra("cityname",cityname);
                        intent.putExtra("countryname",countryname);
                        intent.putExtra("citycode",citycode);
                        intent.putExtra("weathercode",weathercode);
                        startActivity(intent);
                    }else if(text=="竹東鎮"){
                        updatecitycode = "11";
                        city = (TextView)findViewById(R.id.title_selectCity_name);
                        city.setText("當前城市:竹東鎮");
                        countryname = text;
                        weathercode = 11;
                        Intent intent = new Intent(Hsinchu.this, WeatherManually.class);
                        intent.putExtra("cityname",cityname);
                        intent.putExtra("countryname",countryname);
                        intent.putExtra("citycode",citycode);
                        intent.putExtra("weathercode",weathercode);
                        startActivity(intent);
                    }else if(text=="竹北市"){
                        updatecitycode = "11";
                        city = (TextView)findViewById(R.id.title_selectCity_name);
                        city.setText("當前城市:竹北市");
                        countryname = text;
                        weathercode = 12;
                        Intent intent = new Intent(Hsinchu.this, WeatherManually.class);
                        intent.putExtra("cityname",cityname);
                        intent.putExtra("countryname",countryname);
                        intent.putExtra("citycode",citycode);
                        intent.putExtra("weathercode",weathercode);
                        startActivity(intent);
                    }
                    finish();
                }
            };
            cityListLv.setOnItemClickListener(itemClickListener);
        }
        //把citycode值傳到MainActivity
        @Override
        public void onClick(View v){
            switch (v.getId())
            {
                case R.id.title_selectCity_back:
                    // finish();
                    Intent intent = new Intent(this,SelectCity.class);
                    if(updatecitycode!="-1") {
                        intent.putExtra("citycode", updatecitycode);
                        intent.putExtra("cityname", cityname);
                    }
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    //key back
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(this,SelectCity.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}


