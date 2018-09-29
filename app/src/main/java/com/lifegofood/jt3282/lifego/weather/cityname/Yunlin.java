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

public class Yunlin extends Activity implements View.OnClickListener {
    private TextView city;
    private ImageView backBtn;
    private ListView cityListLv;
    private String updatecitycode = "-1";
    String citycode = "25";
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


        final String[] listData = {"水林鄉","林內鄉","東勢鄉","四湖鄉","斗六市","大埤鄉","北港鎮","元長鄉","斗南鎮","古坑鄉","莿桐鄉","西螺鎮","土庫鎮","麥寮鄉","虎尾鎮","崙背鄉","口湖鄉","褒忠鄉","二崙鄉","臺西鄉"};
        cityListLv = (ListView)findViewById(R.id.selectcity_lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Yunlin.this,android.R.layout.simple_list_item_1,listData);
        cityListLv.setAdapter(adapter);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String text = listData[position];
                //String result = "索引值:" + position + "\n" + "內容" + text;
                String result = "已選擇:"+text;
                Toast.makeText(Yunlin.this, result,Toast.LENGTH_SHORT).show();

                if(text=="水林鄉"){
                    updatecitycode = "03";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:水林鄉");
                    countryname = text;
                    weathercode = 0;
                    Intent intent = new Intent(Yunlin.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="林內鄉"){
                    updatecitycode = "01";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:林內鄉");
                    countryname = text;
                    weathercode = 1;
                    Intent intent = new Intent(Yunlin.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="東勢鄉"){
                    updatecitycode = "04";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:東勢鄉");
                    countryname = text;
                    weathercode = 2;
                    Intent intent = new Intent(Yunlin.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="四湖鄉"){
                    updatecitycode = "05";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:四湖鄉");
                    countryname = text;
                    weathercode = 3;
                    Intent intent = new Intent(Yunlin.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="斗六市"){
                    updatecitycode = "14";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:斗六市");
                    countryname = text;
                    weathercode = 4;
                    Intent intent = new Intent(Yunlin.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="大埤鄉"){
                    updatecitycode = "06";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:大埤鄉");
                    countryname = text;
                    weathercode = 5;
                    Intent intent = new Intent(Yunlin.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="北港鎮"){
                    updatecitycode = "17";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:北港鎮");
                    countryname = text;
                    weathercode = 6;
                    Intent intent = new Intent(Yunlin.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="元長鄉"){
                    updatecitycode = "07";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:元長鄉");
                    countryname = text;
                    weathercode = 7;
                    Intent intent = new Intent(Yunlin.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="斗南鎮"){
                    updatecitycode = "08";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:斗南鎮");
                    countryname = text;
                    weathercode = 8;
                    Intent intent = new Intent(Yunlin.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="古坑鄉"){
                    updatecitycode = "09";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:古坑鄉");
                    countryname = text;
                    weathercode = 9;
                    Intent intent = new Intent(Yunlin.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="莿桐鄉"){
                    updatecitycode = "10";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:莿桐鄉");
                    countryname = text;
                    weathercode = 10;
                    Intent intent = new Intent(Yunlin.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="西螺鎮"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:西螺鎮");
                    countryname = text;
                    weathercode = 11;
                    Intent intent = new Intent(Yunlin.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="土庫鎮"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:土庫鎮");
                    countryname = text;
                    weathercode = 12;
                    Intent intent = new Intent(Yunlin.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="麥寮鄉"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:麥寮鄉");
                    countryname = text;
                    weathercode = 13;
                    Intent intent = new Intent(Yunlin.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="虎尾鎮"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:虎尾鎮");
                    countryname = text;
                    weathercode = 14;
                    Intent intent = new Intent(Yunlin.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="崙背鄉"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:崙背鄉");
                    countryname = text;
                    weathercode = 15;
                    Intent intent = new Intent(Yunlin.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="口湖鄉"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:口湖鄉");
                    countryname = text;
                    weathercode = 16;
                    Intent intent = new Intent(Yunlin.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="褒忠鄉"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:褒忠鄉");
                    countryname = text;
                    weathercode = 17;
                    Intent intent = new Intent(Yunlin.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="二崙鄉"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:二崙鄉");
                    countryname = text;
                    weathercode = 18;
                    Intent intent = new Intent(Yunlin.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="臺西鄉"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:臺西鄉");
                    countryname = text;
                    weathercode = 19;
                    Intent intent = new Intent(Yunlin.this, WeatherManually.class);
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
