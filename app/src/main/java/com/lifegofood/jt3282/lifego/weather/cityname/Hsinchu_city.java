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

public class Hsinchu_city extends Activity implements View.OnClickListener  {
    private TextView city;
    private ImageView backBtn;
    private ListView cityListLv;
    private String updatecitycode = "-1";
    String citycode = "53";
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


        final String[] listData = {"北區","香山區","東區"};
        cityListLv = (ListView)findViewById(R.id.selectcity_lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Hsinchu_city.this,android.R.layout.simple_list_item_1,listData);
        cityListLv.setAdapter(adapter);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String text = listData[position];
                //String result = "索引值:" + position + "\n" + "內容" + text;
                String result = "已選擇:"+text;
                Toast.makeText(Hsinchu_city.this, result,Toast.LENGTH_SHORT).show();

                if(text=="北區"){
                    updatecitycode = "03";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:北區");
                    countryname = text;
                    weathercode = 0;
                    Intent intent = new Intent(Hsinchu_city.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="香山區"){
                    updatecitycode = "01";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:香山區");
                    countryname = text;
                    weathercode = 1;
                    Intent intent = new Intent(Hsinchu_city.this, WeatherManually.class);
                    intent.putExtra("cityname",cityname);
                    intent.putExtra("countryname",countryname);
                    intent.putExtra("citycode",citycode);
                    intent.putExtra("weathercode",weathercode);
                    startActivity(intent);
                }else if(text=="東區"){
                    updatecitycode = "04";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:東區");
                    countryname = text;
                    weathercode = 2;
                    Intent intent = new Intent(Hsinchu_city.this, WeatherManually.class);
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
