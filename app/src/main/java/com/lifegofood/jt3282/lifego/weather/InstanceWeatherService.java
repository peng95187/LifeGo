package com.lifegofood.jt3282.lifego.weather;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;

import com.lifegofood.jt3282.lifego.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jt3282 on 2018/5/12.
 */

public class InstanceWeatherService extends Service {
    String a;
    String[] b;
    String r = "";
    private PowerManager.WakeLock mWakeLock;
    private Handler handler = new Handler();

    TodayWeather todayWeather = null;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    r = updateTodayWeather((TodayWeather) message.obj);
                    if (r != null || !r.equals("")) {
                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                        Intent notifyIntent = new Intent(InstanceWeatherService.this, WeatherActivity.class);
                        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        PendingIntent appIntent = PendingIntent.getActivity(InstanceWeatherService.this, 0, notifyIntent, 0);

                        Notification notification
                                = new Notification.Builder(InstanceWeatherService.this)
                                .setContentIntent(appIntent)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setLargeIcon(BitmapFactory.decodeResource(InstanceWeatherService.this.getResources(), R.mipmap.ic_launcher))
                                .setTicker("警報資訊")
                                .setWhen(System.currentTimeMillis())
                                .setAutoCancel(false)
                                .setContentTitle("警報資訊")
                                .setContentText(r)
                                .setOngoing(true)
                                .setDefaults(Notification.DEFAULT_ALL)
                                //.setVibrate(vibrate)
                                .build();

                        notification.flags = Notification.FLAG_ONGOING_EVENT;
                        notification.flags = Notification.FLAG_NO_CLEAR;
                        notification.flags = Notification.FLAG_SHOW_LIGHTS;
                        notification.flags = Notification.FLAG_INSISTENT;
                        mNotificationManager.notify(0, notification);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void getWeatherDatafromNet() {
        final String address = "https://www.cwb.gov.tw/rss/Data/cwb_warning.xml";

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
                    while ((str = reader.readLine()) != null) {
                        sb.append(str);
                    }
                    String response = sb.toString();

                    todayWeather = parseXML(response);
                    if (todayWeather != null) {
                        Message message = new Message();
                        message.what = 1;
                        message.obj = todayWeather;
                        mHandler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private TodayWeather parseXML(String xmlData) {
        int titleCount = 0;

        TodayWeather todayWeather = null;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));

            int eventType = xmlPullParser.getEventType();
            // Log.d("MWeater", "start parse xml");

            while (eventType != xmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    //文档开始位置
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    //标签元素开始位置
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("rss")) {
                            todayWeather = new TodayWeather();
                        }
                        if (todayWeather != null) {

                            if (xmlPullParser.getName().equals("title")) {
                                titleCount++;
                            }
                            if (xmlPullParser.getName().equals("title") && titleCount == 2) {
                                eventType = xmlPullParser.next();
                                if (xmlPullParser.getText() != null) {
                                    todayWeather.setNotice(xmlPullParser.getText());
                                } else {
                                    todayWeather.setNotice("暫無警報訊息");
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return todayWeather;
    }

    String updateTodayWeather(TodayWeather todayWeather) {
        if (todayWeather.toString_3() != null) {
            a = todayWeather.toString_3();
            b = a.split(" ");
        } else {
            a = "暫無警報訊息";
        }
        return a;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        handler.postDelayed(showTime, 1000);
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(showTime);
        super.onDestroy();
    }

    private Runnable showTime = new Runnable() {
        public void run() {
            getWeatherDatafromNet();
            stopSelf();
        }
    };
}
