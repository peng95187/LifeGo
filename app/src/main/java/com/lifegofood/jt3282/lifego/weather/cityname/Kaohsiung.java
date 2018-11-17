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

public class Kaohsiung extends Activity implements View.OnClickListener {
    private TextView city;
    private ImageView backBtn;
    private ListView cityListLv;
    private String updatecitycode = "-1";
    String citycode = "65";
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


        final String[] listData = {"前鎮區","大寮區","美濃區","鹽埕區","田寮區","梓官區","茄萣區","內門區","左營區","六龜區","大樹區","岡山區","小港區","旗山區","前金區","苓雅區","新興區","鳥松區","那瑪夏區","永安區","楠梓區","三民區","橋頭區","彌陀區","林園區","路竹區","桃源區","旗津區","鼓山區","燕巢區","大社區","阿蓮區","鳳山區","湖內區","仁武區","茂林區","杉林區","甲仙區"};
        cityListLv = (ListView)findViewById(R.id.selectcity_lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Kaohsiung.this,android.R.layout.simple_list_item_1,listData);
        cityListLv.setAdapter(adapter);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String text = listData[position];
                //String result = "索引值:" + position + "\n" + "內容" + text;
                String result = "已選擇:"+text;
                Toast.makeText(Kaohsiung.this, result,Toast.LENGTH_SHORT).show();

                if(text=="前鎮區"){
                    updatecitycode = "03";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:前鎮區");
                    countryname = text;
                    weathercode = 0;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="大寮區"){
                    updatecitycode = "01";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:大寮區");
                    countryname = text;
                    weathercode = 1;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="美濃區"){
                    updatecitycode = "04";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:美濃區");
                    countryname = text;
                    weathercode = 2;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="鹽埕區"){
                    updatecitycode = "05";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:鹽埕區");
                    countryname = text;
                    weathercode = 3;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="田寮區"){
                    updatecitycode = "14";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:田寮區");
                    countryname = text;
                    weathercode = 4;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="梓官區"){
                    updatecitycode = "06";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:梓官區");
                    countryname = text;
                    weathercode = 5;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="茄萣區"){
                    updatecitycode = "17";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:茄萣區");
                    countryname = text;
                    weathercode = 6;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="內門區"){
                    updatecitycode = "07";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:內門區");
                    countryname = text;
                    weathercode = 7;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="左營區"){
                    updatecitycode = "08";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:左營區");
                    countryname = text;
                    weathercode = 8;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="六龜區"){
                    updatecitycode = "09";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:六龜區");
                    countryname = text;
                    weathercode = 9;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="大樹區"){
                    updatecitycode = "10";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:大樹區");
                    countryname = text;
                    weathercode = 10;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="岡山區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:岡山區");
                    countryname = text;
                    weathercode = 11;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="小港區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:小港區");
                    countryname = text;
                    weathercode = 12;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="旗山區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:旗山區");
                    countryname = text;
                    weathercode = 13;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="前金區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:前金區");
                    countryname = text;
                    weathercode = 14;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="苓雅區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:苓雅區");
                    countryname = text;
                    weathercode = 15;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="新興區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:新興區");
                    countryname = text;
                    weathercode = 16;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="鳥松區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:鳥松區");
                    countryname = text;
                    weathercode = 17;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="那瑪夏區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:那瑪夏區");
                    countryname = text;
                    weathercode = 18;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="永安區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:永安區");
                    countryname = text;
                    weathercode = 19;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="楠梓區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:楠梓區");
                    countryname = text;
                    weathercode = 20;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="三民區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:三民區");
                    countryname = text;
                    weathercode = 21;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="橋頭區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:橋頭區");
                    countryname = text;
                    weathercode = 22;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="彌陀區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:彌陀區");
                    countryname = text;
                    weathercode = 23;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="林園區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:林園區");
                    countryname = text;
                    weathercode = 24;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="路竹區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:路竹區");
                    countryname = text;
                    weathercode = 25;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="桃源區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:桃源區");
                    countryname = text;
                    weathercode = 26;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="旗津區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:旗津區");
                    countryname = text;
                    weathercode = 27;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="鼓山區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:鼓山區");
                    countryname = text;
                    weathercode = 28;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="燕巢區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:燕巢區");
                    countryname = text;
                    weathercode = 29;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="大社區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:大社區");
                    countryname = text;
                    weathercode = 30;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="阿蓮區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:阿蓮區");
                    countryname = text;
                    weathercode = 31;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="鳳山區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:鳳山區");
                    countryname = text;
                    weathercode = 32;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="湖內區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:湖內區");
                    countryname = text;
                    weathercode = 33;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="仁武區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:仁武區");
                    countryname = text;
                    weathercode = 34;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="茂林區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:茂林區");
                    countryname = text;
                    weathercode = 35;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="杉林區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:杉林區");
                    countryname = text;
                    weathercode = 36;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="甲仙區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:甲仙區");
                    countryname = text;
                    weathercode = 37;
                    Intent intent = new Intent(Kaohsiung.this, WeatherManually.class);
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
