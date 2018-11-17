package com.lifegofood.jt3282.lifego.weather.newweather;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
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
import com.lifegofood.jt3282.lifego.weather.cityname.Changhua;
import com.lifegofood.jt3282.lifego.weather.cityname.Chiayi_city;
import com.lifegofood.jt3282.lifego.weather.cityname.Hsinchu;
import com.lifegofood.jt3282.lifego.weather.cityname.Hsinchu_city;
import com.lifegofood.jt3282.lifego.weather.cityname.Hualien;
import com.lifegofood.jt3282.lifego.weather.cityname.Jinmen;
import com.lifegofood.jt3282.lifego.weather.cityname.Kaohsiung;
import com.lifegofood.jt3282.lifego.weather.cityname.Keelung;
import com.lifegofood.jt3282.lifego.weather.cityname.Lianjiang;
import com.lifegofood.jt3282.lifego.weather.cityname.Miaoli;
import com.lifegofood.jt3282.lifego.weather.cityname.Nantou;
import com.lifegofood.jt3282.lifego.weather.cityname.Penghu;
import com.lifegofood.jt3282.lifego.weather.cityname.Pingtung;
import com.lifegofood.jt3282.lifego.weather.cityname.Taichung;
import com.lifegofood.jt3282.lifego.weather.cityname.Tainan;
import com.lifegofood.jt3282.lifego.weather.cityname.Taipei;
import com.lifegofood.jt3282.lifego.weather.cityname.Taipei_new;
import com.lifegofood.jt3282.lifego.weather.cityname.Taitung;
import com.lifegofood.jt3282.lifego.weather.cityname.Taoyuan;
import com.lifegofood.jt3282.lifego.weather.cityname.Yilan;
import com.lifegofood.jt3282.lifego.weather.cityname.Yunlin;

/**
 * Created by user on 2018/8/24.
 */

public class SelectCity extends Activity implements View.OnClickListener,LocationListener {
    private TextView city;
    private ImageView backBtn;
    private ListView cityListLv;
    private String updatecitycode = "-1";
    private String cityname;
    //選擇城市
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_select_city);

        backBtn = (ImageView)findViewById(R.id.title_selectCity_back);
        backBtn.setOnClickListener(this);


        final String[] listData = {"基隆市","臺北市","新北市","桃園市","新竹市","新竹縣","宜蘭縣","苗栗縣","臺中市","彰化縣","南投縣","雲林縣","嘉義市","嘉義縣","臺南市","高雄市","屏東縣","澎湖縣","花蓮縣","臺東縣","金門縣","連江縣"};
        cityListLv = (ListView)findViewById(R.id.selectcity_lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SelectCity.this,android.R.layout.simple_list_item_1,listData);
        cityListLv.setAdapter(adapter);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String text = listData[position];
                //String result = "索引值:" + position + "\n" + "內容" + text;
                String result = "已選擇:"+text;
                Toast.makeText(SelectCity.this, result,Toast.LENGTH_SHORT).show();

                if(text=="基隆市"){
                    updatecitycode = "03";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:基隆市");
                    cityname = text;
                    Intent intent = new Intent(SelectCity.this, Keelung.class);
                    intent.putExtra("citycode", updatecitycode);
                    intent.putExtra("cityname", cityname);
                    startActivity(intent);
                }else if(text=="臺北市"){
                    updatecitycode = "01";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:臺北市");
                    cityname = text;
                    Intent intent = new Intent(SelectCity.this, Taipei.class);
                    intent.putExtra("citycode", updatecitycode);
                    intent.putExtra("cityname", cityname);
                    startActivity(intent);
                }else if(text=="新北市"){
                    updatecitycode = "04";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:新北市");
                    cityname = text;
                    Intent intent = new Intent(SelectCity.this, Taipei_new.class);
                    intent.putExtra("citycode", updatecitycode);
                    intent.putExtra("cityname", cityname);
                    startActivity(intent);
                }else if(text=="桃園市"){
                    updatecitycode = "05";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:桃園市");
                    cityname = text;
                    Intent intent = new Intent(SelectCity.this, Taoyuan.class);
                    intent.putExtra("citycode", updatecitycode);
                    intent.putExtra("cityname", cityname);
                    startActivity(intent);
                }else if(text=="新竹市"){
                    updatecitycode = "14";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:新竹市");
                    cityname = text;
                    Intent intent = new Intent(SelectCity.this, Hsinchu_city.class);
                    intent.putExtra("citycode", updatecitycode);
                    intent.putExtra("cityname", cityname);
                    startActivity(intent);
                }else if(text=="新竹縣"){
                    updatecitycode = "06";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:新竹縣");
                    cityname = text;
                    Intent intent = new Intent(SelectCity.this, Hsinchu.class);
                    intent.putExtra("citycode", updatecitycode);
                    intent.putExtra("cityname", cityname);
                    startActivity(intent);
                }else if(text=="宜蘭縣"){
                    updatecitycode = "17";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:宜蘭縣");
                    cityname = text;
                    Intent intent = new Intent(SelectCity.this, Yilan.class);
                    intent.putExtra("citycode", updatecitycode);
                    intent.putExtra("cityname", cityname);
                    startActivity(intent);
                }else if(text=="苗栗縣"){
                    updatecitycode = "07";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:苗栗縣");
                    cityname = text;
                    Intent intent = new Intent(SelectCity.this, Miaoli.class);
                    intent.putExtra("citycode", updatecitycode);
                    intent.putExtra("cityname", cityname);
                    startActivity(intent);
                }else if(text=="臺中市"){
                    updatecitycode = "08";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:臺中市");
                    cityname = text;
                    Intent intent = new Intent(SelectCity.this, Taichung.class);
                    intent.putExtra("citycode", updatecitycode);
                    intent.putExtra("cityname", cityname);
                    startActivity(intent);
                }else if(text=="彰化縣"){
                    updatecitycode = "09";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:彰化縣");
                    cityname = text;
                    Intent intent = new Intent(SelectCity.this, Changhua.class);
                    intent.putExtra("citycode", updatecitycode);
                    intent.putExtra("cityname", cityname);
                    startActivity(intent);
                }else if(text=="南投縣"){
                    updatecitycode = "10";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:南投縣");
                    cityname = text;
                    Intent intent = new Intent(SelectCity.this, Nantou.class);
                    intent.putExtra("citycode", updatecitycode);
                    intent.putExtra("cityname", cityname);
                    startActivity(intent);
                }else if(text=="雲林縣"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:雲林縣");
                    cityname = text;
                    Intent intent = new Intent(SelectCity.this, Yunlin.class);
                    intent.putExtra("citycode", updatecitycode);
                    intent.putExtra("cityname", cityname);
                    startActivity(intent);
                }else if(text=="嘉義市"){
                    updatecitycode = "16";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:嘉義市");
                    cityname = text;
                    Intent intent = new Intent(SelectCity.this, Chiayi_city.class);
                    intent.putExtra("citycode", updatecitycode);
                    intent.putExtra("cityname", cityname);
                    startActivity(intent);
                }else if(text=="嘉義縣"){
                    updatecitycode = "12";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:嘉義縣");
                    cityname = text;
                    Intent intent = new Intent(SelectCity.this, Chiayi_city.class);
                    intent.putExtra("citycode", updatecitycode);
                    intent.putExtra("cityname", cityname);
                    startActivity(intent);
                }else if(text=="臺南市"){
                    updatecitycode = "13";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:臺南市");
                    cityname = text;
                    Intent intent = new Intent(SelectCity.this, Tainan.class);
                    intent.putExtra("citycode", updatecitycode);
                    intent.putExtra("cityname", cityname);
                    intent.putExtra("citycode", updatecitycode);
                    intent.putExtra("cityname", cityname);
                    startActivity(intent);
                }else if(text=="高雄市"){
                    updatecitycode = "02";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:高雄市");
                    cityname = text;
                    Intent intent = new Intent(SelectCity.this, Kaohsiung.class);
                    intent.putExtra("citycode", updatecitycode);
                    intent.putExtra("cityname", cityname);
                    startActivity(intent);
                }else if(text=="屏東縣"){
                    updatecitycode = "15";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:屏東縣");
                    cityname = text;
                    Intent intent = new Intent(SelectCity.this, Pingtung.class);
                    intent.putExtra("citycode", updatecitycode);
                    intent.putExtra("cityname", cityname);
                    startActivity(intent);
                }else if(text=="澎湖縣"){
                    updatecitycode = "20";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:澎湖縣");
                    cityname = text;
                    Intent intent = new Intent(SelectCity.this, Penghu.class);
                    intent.putExtra("citycode", updatecitycode);
                    intent.putExtra("cityname", cityname);
                    startActivity(intent);
                }else if(text=="花蓮縣"){
                    updatecitycode = "18";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:花蓮縣");
                    cityname = text;
                    Intent intent = new Intent(SelectCity.this, Hualien.class);
                    intent.putExtra("citycode", updatecitycode);
                    intent.putExtra("cityname", cityname);
                    startActivity(intent);
                }else if(text=="臺東縣"){
                    updatecitycode = "19";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:臺東縣");
                    cityname = text;
                    Intent intent = new Intent(SelectCity.this, Taitung.class);
                    intent.putExtra("citycode", updatecitycode);
                    intent.putExtra("cityname", cityname);
                    startActivity(intent);
                }else if(text=="金門縣"){
                    updatecitycode = "21";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:金門縣");
                    cityname = text;
                    Intent intent = new Intent(SelectCity.this, Jinmen.class);
                    intent.putExtra("citycode", updatecitycode);
                    intent.putExtra("cityname", cityname);
                    startActivity(intent);
                }else if(text=="連江縣"){
                    updatecitycode = "22";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:連江縣");
                    cityname = text;
                    Intent intent = new Intent(SelectCity.this, Lianjiang.class);
                    intent.putExtra("citycode", updatecitycode);
                    intent.putExtra("cityname", cityname);
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
                Intent intent = new Intent(this,WeatherManually.class);
               /*
                if(updatecitycode!="-1") {
                    intent.putExtra("citycode", updatecitycode);
                    intent.putExtra("cityname", cityname);
                }*/
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
            Intent intent = new Intent(SelectCity.this,WeatherManually.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
