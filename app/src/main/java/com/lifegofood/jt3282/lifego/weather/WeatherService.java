package com.lifegofood.jt3282.lifego.weather;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.text.format.DateFormat;
import android.util.Log;

import com.lifegofood.jt3282.lifego.IMyAidlInterface;
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
 * Created by jt3282 on 2018/5/10.
 */

public class WeatherService extends Service {
    String a;
    String[]b;
    String r = "";
    private PowerManager.WakeLock mWakeLock;
    private Handler handler = new Handler();
    private SharedPreferences.Editor spe = null;
    private SharedPreferences sp = null;
    TodayWeather todayWeather = null;

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message message)
        {
            switch (message.what)
            {
                case 1:
                    r = updateTodayWeather((TodayWeather) message.obj);

                    if(!r.equals(sp.getString("warnig",""))) {
                        if (r != null && !r.equals("暫無警報訊息")) {
                            spe.remove("warnig");
                            spe.commit();

                            spe.putString("warnig",r);
                            spe.commit();
                            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            Intent notifyIntent = new Intent(WeatherService.this, WeatherActivity.class);
                            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            PendingIntent appIntent = PendingIntent.getActivity(WeatherService.this, 0, notifyIntent, 0);

                            Notification notification
                                    = new Notification.Builder(WeatherService.this)
                                    .setContentIntent(appIntent)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setLargeIcon(BitmapFactory.decodeResource(WeatherService.this.getResources(), R.mipmap.ic_launcher))
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
                    }

                    break;
                default:
                    break;
            }
        }
    };

    private void getWeatherDatafromNet()
    {
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
                    while((str=reader.readLine())!=null)
                    {
                        sb.append(str);
                    }
                    String response = sb.toString();

                    todayWeather = parseXML(response);
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
    private TodayWeather parseXML(String xmlData)
    {
        int titleCount = 0;

        TodayWeather todayWeather = null;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));

            int eventType = xmlPullParser.getEventType();
            // Log.d("MWeater", "start parse xml");

            while (eventType!=xmlPullParser.END_DOCUMENT)
            {
                switch (eventType)
                {
                    //文档开始位置
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    //标签元素开始位置
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("rss"))
                        {
                            todayWeather = new TodayWeather();
                        }
                        if (todayWeather != null) {

                            if (xmlPullParser.getName().equals("title")) {
                                titleCount++;
                            }
                            if(xmlPullParser.getName().equals("title") && titleCount==2)
                            {
                                eventType = xmlPullParser.next();
                                if(xmlPullParser.getText()!=null) {
                                    todayWeather.setNotice(xmlPullParser.getText());
                                }else{
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
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return todayWeather;
    }
    String updateTodayWeather(TodayWeather todayWeather)
    {
        if(todayWeather.toString_3()!=null) {
            a = todayWeather.toString_3();
            b = a.split(" ");
        }else{
            a = "暫無警報訊息";
        }
        return a;
    }

    private MyBinder binder;
    private MyConn conn;

    @Override
    public void onCreate() {
        super.onCreate();

        spe = getSharedPreferences("weather_notify",MODE_PRIVATE).edit();
        sp = getSharedPreferences("weather_notify", MODE_PRIVATE);
        Log.i("開始綁定","進行中");
        binder = new MyBinder();
        if(conn==null)
            conn = new MyConn();

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("binder"," = " + binder);
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent != null) {
            Log.i("onstartcommand success","start ok");
            acquireWakeLock();
            startService(new Intent(this,WeatherClearService.class));
            this.bindService(new Intent(this,WeatherGuardService.class),conn, Context.BIND_IMPORTANT);
            handler.postDelayed(showTime, 1000);
/*
            Notification.Builder builder = new Notification.Builder(this);//新建Notification.Builder对象
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, WeatherService.class), 0);
            builder.setContentTitle("weather");//设置标题
            builder.setContentText("weather is running");//设置内容
            builder.setSmallIcon(R.mipmap.ic_launcher);//设置图片
            builder.setContentIntent(pendingIntent);//执行intent
            Notification notification = builder.getNotification();//将builder对象转换为普通的notification

            //让该service前台运行，避免手机休眠时系统自动杀掉该服务
            //如果 id 为 0 ，那么状态栏的 notification 将不会显示。
            startForeground(0, notification);
            */

        } else {
            Log.i("intent is null","restart now");
            Intent i = new Intent("com.example.jt3282.weather.destroy");
            sendBroadcast(i);
        }
        return Service.START_STICKY;
    }
    class MyBinder extends IMyAidlInterface.Stub{

        @Override
        public String getServiceName() throws RemoteException {
            return "I am FirstService";
        }
    }

    class  MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i("Info","与SecondService连接成功");
            ActivityManager activityManager = (ActivityManager) WeatherService.this
                    .getSystemService(Context.ACTIVITY_SERVICE);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // 启动FirstService
            WeatherService.this.startService(new Intent(WeatherService.this,WeatherGuardService.class));
            //绑定FirstService
            WeatherService.this.bindService(new Intent(WeatherService.this,WeatherGuardService.class),conn, Context.BIND_IMPORTANT);
        }
    }

    @Override
    public void onDestroy() {
        startService(new Intent(WeatherService.this,WeatherService.class));
        Intent intent = new Intent("com.example.jt3282.weather.destroy");
        sendBroadcast(intent);
        handler.removeCallbacks(showTime);
        releaseWakeLock();
        stopForeground(true);
    }

    private Runnable showTime = new Runnable() {
        public void run() {

            java.util.Calendar mCal = java.util.Calendar.getInstance();
            String min = DateFormat.format("mm", mCal.getTime()) + "";

            if(min.equals("00")||min.equals("30")){
                getWeatherDatafromNet();
            }

            handler.postDelayed(this, 60000);
        }
    };

    private void acquireWakeLock()
    {
        Log.e("MyGPS","正在申請電源鎖");
        if (null == mWakeLock)
        {
            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WeatherService.class.getName());
            if (null != mWakeLock)
            {
                mWakeLock.acquire();
                Log.e("MyGPS","電源鎖申請成功");
            }
        }
    }
    private void releaseWakeLock()
    {
        Log.e("MyGPS","正在釋放電源鎖");
        if (null != mWakeLock)
        {
            mWakeLock.release(); mWakeLock = null;
            Log.e("MyGPS","電源鎖釋放成功");
        }
    }
}