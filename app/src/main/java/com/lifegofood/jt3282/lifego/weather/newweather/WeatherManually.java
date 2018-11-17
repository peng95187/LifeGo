package com.lifegofood.jt3282.lifego.weather.newweather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.weather.WeatherInformation;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user on 2018/8/30.
 */

public class WeatherManually extends Activity implements View.OnClickListener{
    private String morning;
    private String[] am,bm,cm;
    private String rain_morning,temper_morning,state_morning;
    private String cityname,countryname,citycode,wcode_str;
    private ImageView SelectCityBtn,UpdateBtn,ReLocate,RunService;
    private int weathercode;

    private TextView cityT,countryT,temperT_morning,rainT_morning,stateT_morning;


    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message message)
        {
            switch (message.what)
            {
                case 1:
                    updateTodayWeather((TodayWeather) message.obj);
                    break;
                default:
                    break;
            }
        }
    };
    TodayWeather todayWeather = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_manually);

        cityname = getIntent().getStringExtra("cityname");
        countryname = getIntent().getStringExtra("countryname");
        citycode = getIntent().getStringExtra("citycode");
        getIntent().getIntExtra("weathercode",weathercode);
        wcode_str = getIntent().getStringExtra("weathercode");


        SelectCityBtn = (ImageView)findViewById(R.id.title_city_manager);
        UpdateBtn = (ImageView)findViewById(R.id.title_city_update);
        ReLocate = (ImageView)findViewById(R.id.title_city_locate);
        RunService = (ImageView)findViewById(R.id.title_city_share);
        SelectCityBtn.setOnClickListener(this);
        UpdateBtn.setOnClickListener(this);
        ReLocate.setOnClickListener(this);
        RunService.setOnClickListener(this);

        //intent.putExtra("countryname",countryname);
        //intent.putExtra("citycode",citycode);
        //intent.putExtra("weathercode",weathercode);

        initView();

        if(countryname==null){
            cityname = getConfig(WeatherManually.this, "myConfig", "myCityName", "mine");
            countryname = getConfig(WeatherManually.this, "myConfig_2", "myCoutryName", "mine");
            wcode_str = getConfig(WeatherManually.this, "myConfig_3", "myWeatherCode", "mine");
            citycode = getConfig(WeatherManually.this, "myConfig__4", "myCityCode", "mine");
            //cityT.setText(mycityname);
            getWeatherDatafromNet(citycode, weathercode);
        }else{
            setConfig(WeatherManually.this, "myConfig", "myCityName", cityname);
            setConfig(WeatherManually.this, "myConfig_2", "myCoutryName", countryname);
            setConfig(WeatherManually.this, "myConfig_3", "myWeatherCode", wcode_str);
            setConfig(WeatherManually.this, "myConfig__4", "myCityCode", citycode);

            getWeatherDatafromNet(citycode, weathercode);
        }


        LinearLayout city_select = (LinearLayout) findViewById(R.id.today_relative_2);
        city_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherManually.this,SelectCity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public static void setConfig(Context context, String name, String key, String value)
    {
        SharedPreferences settings =context.getSharedPreferences(name,0);
        SharedPreferences.Editor PE = settings.edit();
        PE.putString(key, value);
        PE.commit();
    }

    //設定檔讀取
    public static String getConfig(Context context , String name , String key , String def)
    {
        SharedPreferences settings =context.getSharedPreferences(name,0);
        return settings.getString(key, def);
    }
    private void getWeatherDatafromNet(String citycode, final int weathercode)
    {
        final String address = "http://opendata.cwb.gov.tw/opendataapi?dataid=F-D0047-0"+citycode+"&authorizationkey=CWB-31C940FF-CACC-446B-B684-121414E83C0D";
        Log.d("Address:",address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(address);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(8000);
                    urlConnection.setReadTimeout(8000);
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuffer sb = new StringBuffer();
                    String str;
                    while((str=reader.readLine())!=null)
                    {
                        sb.append(str);
                        //Log.d("date from url",str);
                    }
                    String response = sb.toString();
                    Log.d("response",response);
                    todayWeather = parseXML(response,weathercode);
                    if(todayWeather!=null)
                    {
                        Message message = new Message();
                        message.what = 1;
                        message.obj = todayWeather;
                        mHandler.sendMessage(message);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private TodayWeather parseXML(String xmlData, int weathercode)
    {

        TodayWeather todayWeather = null;
        int count = 0;
        int temp_a = 283;
        int temp_b = 289;
        int a = 283;
        int b = 23;
        String string;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));

            int eventType = xmlPullParser.getEventType();


            while (eventType!=xmlPullParser.END_DOCUMENT)
            {
                switch (eventType)
                {
                    //文档开始位置
                    case XmlPullParser.START_DOCUMENT:
                        Log.d("parse", "開始解析");
                        break;
                    //标签元素开始位置
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("cwbopendata"))
                        {
                            todayWeather = new TodayWeather();
                        }
                        if (todayWeather != null) {
                            if (xmlPullParser.getName().equals("value"))
                            {
                                count = count + 1;

                                /*eventType = xmlPullParser.next();
                                todayWeather.setDetail(xmlPullParser.getText());
                                Log.d("86868686", xmlPullParser.getText());*/
                               /* String count1 = String.valueOf(count);
                                Log.d("0000",count1);*/

                                if(count==temp_a+weathercode*a+weathercode*b){
                                    eventType = xmlPullParser.next();
                                    todayWeather.setMorning(xmlPullParser.getText());
                                }

                            }else if(xmlPullParser.getName().equals("locationName")){
                                eventType = xmlPullParser.next();
                                todayWeather.setNight(xmlPullParser.getText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return todayWeather;
    }
    void initView(){
        cityT = (TextView)findViewById(R.id.title_city_name_2);
        countryT = (TextView)findViewById(R.id.time_2);
        temperT_morning = (TextView)findViewById(R.id.temperature_2);

        rainT_morning = (TextView)findViewById(R.id.rainfall_2);

        stateT_morning = (TextView)findViewById(R.id.status_2);

    }
    void updateTodayWeather(TodayWeather todayWeather)
    {
        morning = todayWeather.toString_Morning();

        am = morning.split("。");
        bm = am[1].split(" ");
        cm = am[2].split("氏|度");
        rain_morning = bm[1];
        temper_morning = cm[2];
        state_morning = am[0];
        temperT_morning.setText("溫度:"+temper_morning+"℃");
        rainT_morning.setText("降雨:"+rain_morning);
        stateT_morning.setText(state_morning);

        cityT.setText(cityname);
        countryT.setText("("+countryname+")");
        /*for(int i=0;i<c.length;i++){
            System.out.println("array["+i+"] = "+c[i]);
            Log.d("5555555",c[i]);
        }*/
        LinearLayout background = (LinearLayout) findViewById(R.id.today_relative_2);

        if (stateT_morning.getText() != null) {
            switch (stateT_morning.getText().toString()) {
                case "晴":
                    background.setBackgroundResource(R.drawable.biz_plugin_weather_cloudy);
                    break;
                case "多雲":
                    background.setBackgroundResource(R.drawable.biz_plugin_weather_yin);
                    break;
                case "多雲時晴":
                    background.setBackgroundResource(R.drawable.biz_plugin_weather_qing);
                    break;
                case "多雲時陰":
                    background.setBackgroundResource(R.drawable.biz_plugin_weather_yin);
                    break;
                case "短暫陣雨":
                    background.setBackgroundResource(R.drawable.biz_plugin_weather_rain);
                    break;
            }
            /*
            background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(WeatherManually.this, "所在縣市 : " + cityT.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });*/
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.title_city_manager)
        {
            Intent intent = new Intent(this,SelectCity.class);
            startActivity(intent);
            finish();
        }else  if(v.getId()==R.id.title_city_locate)
        {
            /*if(address.equals("NowLocation")||address==null){
                Toast.makeText(this, "請開啟定位服務", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "定位完成", Toast.LENGTH_SHORT).show();
            }*/
        }else if(v.getId()==R.id.title_city_update)
        {
            /*getWeatherDatafromNet(updatemycitycode);
            getWeatherDatafromNet_2(updatecitycode);
            Toast.makeText(this, "更新完成", Toast.LENGTH_SHORT).show();*/
        } else if(v.getId()==R.id.title_city_share)
        {
            Intent s = new Intent(WeatherManually.this, WeatherInformation.class);
            startActivity(s);
        }
    }
}
