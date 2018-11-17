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
import com.lifegofood.jt3282.lifego.weather.newweather.SelectCity;
import com.lifegofood.jt3282.lifego.weather.newweather.WeatherManually;

/**
 * Created by user on 2018/8/27.
 */

public class Taipei_new extends Activity implements View.OnClickListener {
    private TextView city;
    private ImageView backBtn;
    private ListView cityListLv;
    private String updatecitycode = "-1";
    String citycode = "69";
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


        final String[] listData = {"鶯歌區","中和區","土城區","三重區","三峽區","雙溪區","三芝區","樹林區","坪林區","石門區","泰山區","萬里區","永和區","五股區","蘆洲區","板橋區","深坑區","汐止區","新莊區","八里區","林口區","烏來區","平溪區","淡水區","石碇區","新店區","瑞芳區","金山區","貢寮區"};
        cityListLv = (ListView)findViewById(R.id.selectcity_lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Taipei_new.this,android.R.layout.simple_list_item_1,listData);
        cityListLv.setAdapter(adapter);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String text = listData[position];
                //String result = "索引值:" + position + "\n" + "內容" + text;
                String result = "已選擇:"+text;
                Toast.makeText(Taipei_new.this, result,Toast.LENGTH_SHORT).show();

                if(text=="鶯歌區"){
                    updatecitycode = "03";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:鶯歌區");
                    countryname = text;
                    weathercode = 0;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="中和區"){
                    updatecitycode = "01";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:中和區");
                    countryname = text;
                    weathercode = 1;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="土城區"){
                    updatecitycode = "04";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:土城區");
                    countryname = text;
                    weathercode = 2;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="三重區"){
                    updatecitycode = "05";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:三重區");
                    countryname = text;
                    weathercode = 3;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="三峽區"){
                    updatecitycode = "14";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:三峽區");
                    countryname = text;
                    weathercode = 4;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="雙溪區"){
                    updatecitycode = "06";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:雙溪區");
                    countryname = text;
                    weathercode = 5;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="三芝區"){
                    updatecitycode = "17";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:三芝區");
                    countryname = text;
                    weathercode = 6;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="樹林區"){
                    updatecitycode = "07";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:樹林區");
                    countryname = text;
                    weathercode = 7;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="坪林區"){
                    updatecitycode = "08";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:坪林區");
                    countryname = text;
                    weathercode = 8;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="石門區"){
                    updatecitycode = "09";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:石門區");
                    countryname = text;
                    weathercode = 9;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="泰山區"){
                    updatecitycode = "10";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:泰山區");
                    countryname = text;
                    weathercode = 10;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="萬里區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:萬里區");
                    countryname = text;
                    weathercode = 11;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="永和區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:永和區");
                    countryname = text;
                    weathercode = 12;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="五股區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:五股區");
                    countryname = text;
                    weathercode = 13;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="蘆洲區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:蘆洲區");
                    countryname = text;
                    weathercode = 14;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="板橋區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:板橋區");
                    countryname = text;
                    weathercode = 15;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="深坑區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:深坑區");
                    countryname = text;
                    weathercode = 16;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="汐止區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:汐止區");
                    countryname = text;
                    weathercode = 17;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="新莊區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:新莊區");
                    countryname = text;
                    weathercode = 18;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="八里區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:八里區");
                    countryname = text;
                    weathercode = 19;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="林口區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:林口區");
                    countryname = text;
                    weathercode = 20;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="烏來區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:烏來區");
                    countryname = text;
                    weathercode = 21;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="平溪區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:平溪區");
                    countryname = text;
                    weathercode = 22;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="淡水區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:淡水區");
                    countryname = text;
                    weathercode = 23;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="石碇區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:石碇區");
                    countryname = text;
                    weathercode = 24;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="新店區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:新店區");
                    countryname = text;
                    weathercode = 25;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="瑞芳區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:瑞芳區");
                    countryname = text;
                    weathercode = 26;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="金山區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:金山區");
                    countryname = text;
                    weathercode = 27;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="貢寮區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:貢寮區");
                    countryname = text;
                    weathercode = 28;
                    Intent intent = new Intent(Taipei_new.this, WeatherManually.class);
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
