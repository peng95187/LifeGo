package com.lifegofood.jt3282.lifego.weather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherInformation extends Activity {

    String[] detailArray;
    String[] timeArray;
    TextView time,info;
    ImageView back,notify;
    CheckBox checkBox;
    boolean ischeck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_information);
        time = (TextView) findViewById(R.id.time);
        info = (TextView) findViewById(R.id.info);
        back = (ImageView) findViewById(R.id.title_back);
        notify = (ImageView) findViewById(R.id.notify);
        checkBox = (CheckBox) findViewById(R.id.checkbox);

        SharedPreferences settings = getSharedPreferences("ServiceCheck",0);
        final SharedPreferences.Editor PE = settings.edit();

        //判斷警報服務是否啟用
        if(settings.getString("ischecked","").equals("true")||settings.getString("ischecked","").equals("")){
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }
        //給一boolean判斷
        if(checkBox.isChecked()){
            ischeck = true;
        }else{
            ischeck = false;
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(ischeck==true){
                    stopService(new Intent(WeatherInformation.this,WeatherService.class));
                    stopService(new Intent(WeatherInformation.this,WeatherGuardService.class));
                    PE.putString("ischecked", "false");
                    PE.commit();
                    ischeck = false;
                }else{
                    startService(new Intent(WeatherInformation.this,WeatherService.class));
                    startService(new Intent(WeatherInformation.this,WeatherGuardService.class));
                    PE.putString("ischecked", "true");
                    PE.commit();
                    ischeck = true;
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WeatherInformation.this, InstanceWeatherService.class);
                startService(i);
                Toast.makeText(WeatherInformation.this, "取得警報訊息中", Toast.LENGTH_SHORT).show();
            }
        });

        getWeatherDatafromNet();
    }
    private void getWeatherDatafromNet()
    {
        final String address = "https://alerts.ncdr.nat.gov.tw/RssAtomFeed.ashx?AlertType=33";

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
                    }
                    String response = sb.toString();

                    parseXML(response);
                    parseXML_2(response);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void parseXML(String xmlData)
    {
        int titleCount = 0;

        TodayWeather todayWeather = null;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            final XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));

            int eventType = xmlPullParser.getEventType();
            // Log.d("MWeater", "start parse xml");

            while (eventType!=xmlPullParser.END_DOCUMENT)
            {
                switch (eventType)
                {
                    //文??始位置
                    case XmlPullParser.START_DOCUMENT:
                        Log.d("parse", "開始解析");
                        break;
                    //??元素?始位置
                    case XmlPullParser.START_TAG:

                        if (xmlPullParser.getName().equals("summary")) {
                            eventType = xmlPullParser.next();
                            final String[] mes = {xmlPullParser.getText()};
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mes[0] = mes[0].replace("]","]\n\n");
                                    mes[0] = mes[0].replace("。","\n\n");
                                    info.setText(mes[0]);
                                }
                            });

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
    }
    private void parseXML_2(String xmlData)
    {
        int titleCount = 0;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            final XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));

            int eventType = xmlPullParser.getEventType();
            // Log.d("MWeater", "start parse xml");

            while (eventType!=xmlPullParser.END_DOCUMENT)
            {
                switch (eventType)
                {
                    //文??始位置
                    case XmlPullParser.START_DOCUMENT:
                        Log.d("parse", "開始解析");
                        break;
                    //??元素?始位置
                    case XmlPullParser.START_TAG:

                        if (xmlPullParser.getName().equals("updated")) {
                            eventType = xmlPullParser.next();
                            final String mes = xmlPullParser.getText();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    timeArray = mes.split("\\+");
                                    timeArray[0] = timeArray[0].replaceAll("T","  ");
                                    time.setText(timeArray[0]);
                                }
                            });
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
    }

}
