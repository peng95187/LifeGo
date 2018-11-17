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

public class Tainan extends Activity implements View.OnClickListener {
    private TextView city;
    private ImageView backBtn;
    private ListView cityListLv;
    private String updatecitycode = "-1";
    String citycode = "77";
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


        final String[] listData = {"後壁區","官田區","鹽水區","麻豆區","關廟區","佳里區","山上區","東區","下營區","南化區","楠西區","玉井區","白河區","仁德區","龍崎區","柳營區","左鎮區","北門區","東山區","將軍區","安南區","永康區","新市區","北區","學甲區","七股區","南區","六甲區","大內區","安定區","新營區","中西區","西港區","歸仁區","新化區","安平區","善化區"};
        cityListLv = (ListView)findViewById(R.id.selectcity_lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Tainan.this,android.R.layout.simple_list_item_1,listData);
        cityListLv.setAdapter(adapter);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String text = listData[position];
                //String result = "索引值:" + position + "\n" + "內容" + text;
                String result = "已選擇:"+text;
                Toast.makeText(Tainan.this, result,Toast.LENGTH_SHORT).show();

                if(text=="後壁區"){
                    updatecitycode = "03";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:後壁區");
                    countryname = text;
                    weathercode = 0;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="官田區"){
                    updatecitycode = "01";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:官田區");
                    countryname = text;
                    weathercode = 1;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="鹽水區"){
                    updatecitycode = "04";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:鹽水區");
                    countryname = text;
                    weathercode = 2;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="麻豆區"){
                    updatecitycode = "05";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:麻豆區");
                    countryname = text;
                    weathercode = 3;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="關廟區"){
                    updatecitycode = "14";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:關廟區");
                    countryname = text;
                    weathercode = 4;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="佳里區"){
                    updatecitycode = "06";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:佳里區");
                    countryname = text;
                    weathercode = 5;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="山上區"){
                    updatecitycode = "17";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:山上區");
                    countryname = text;
                    weathercode = 6;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="東區"){
                    updatecitycode = "07";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:東區");
                    countryname = text;
                    weathercode = 7;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="下營區"){
                    updatecitycode = "08";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:下營區");
                    countryname = text;
                    weathercode = 8;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="南化區"){
                    updatecitycode = "09";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:南化區");
                    countryname = text;
                    weathercode = 9;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="楠西區"){
                    updatecitycode = "10";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:楠西區");
                    countryname = text;
                    weathercode = 10;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="玉井區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:玉井區");
                    countryname = text;
                    weathercode = 11;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="白河區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:白河區");
                    countryname = text;
                    weathercode = 12;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="仁德區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:仁德區");
                    countryname = text;
                    weathercode = 13;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="龍崎區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:龍崎區");
                    countryname = text;
                    weathercode = 14;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="柳營區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:柳營區");
                    countryname = text;
                    weathercode = 15;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="左鎮區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:左鎮區");
                    countryname = text;
                    weathercode = 16;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="北門區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:北門區");
                    countryname = text;
                    weathercode = 17;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="東山區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:東山區");
                    countryname = text;
                    weathercode = 18;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="將軍區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:將軍區");
                    countryname = text;
                    weathercode = 19;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="安南區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:安南區");
                    countryname = text;
                    weathercode = 20;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="永康區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:永康區");
                    countryname = text;
                    weathercode = 21;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="新市區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:新市區");
                    countryname = text;
                    weathercode = 22;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="北區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:北區");
                    countryname = text;
                    weathercode = 23;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="學甲區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:學甲區");
                    countryname = text;
                    weathercode = 24;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="七股區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:七股區");
                    countryname = text;
                    weathercode = 25;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="南區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:南區");
                    countryname = text;
                    weathercode = 26;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="六甲區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:六甲區");
                    countryname = text;
                    weathercode = 27;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="大內區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:大內區");
                    countryname = text;
                    weathercode = 28;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="安定區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:安定區");
                    countryname = text;
                    weathercode = 29;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="新營區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:新營區");
                    countryname = text;
                    weathercode = 30;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="中西區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:中西區");
                    countryname = text;
                    weathercode = 31;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="西港區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:西港區");
                    countryname = text;
                    weathercode = 32;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="歸仁區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:歸仁區");
                    countryname = text;
                    weathercode = 33;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="新化區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:新化區");
                    countryname = text;
                    weathercode = 34;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="安平區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:安平區");
                    countryname = text;
                    weathercode = 35;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="善化區"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:善化區");
                    countryname = text;
                    weathercode = 36;
                    Intent intent = new Intent(Tainan.this, WeatherManually.class);
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
