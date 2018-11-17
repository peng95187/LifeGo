package com.lifegofood.jt3282.lifego.traffic;

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
import android.os.PowerManager;
import android.os.RemoteException;
import android.text.format.DateFormat;
import android.util.Log;

import com.lifegofood.jt3282.lifego.IMyAidlInterface2;
import com.lifegofood.jt3282.lifego.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by jt3282 on 2018/4/25.
 */

public class BusService extends Service {
    private Handler handler = new Handler();
    private SharedPreferences.Editor spe = null;
    private SharedPreferences sp = null;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private String [] week = {"一","二","三","四","五","六","日"};
    private PowerManager.WakeLock mWakeLock;

    private MyBinder binder;
    private MyConn conn;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("開始綁定","進行中");
        binder = new MyBinder();
        if(conn==null)
            conn = new MyConn();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null){
            acquireWakeLock();
            handler.postDelayed(showTime, 1000);
        } else {
            Log.i("intent is null","restart now");
            Intent i = new Intent("com.example.jt3282.weather.destroy");
            sendBroadcast(i);
        }
        return Service.START_STICKY;
    }

    class MyBinder extends IMyAidlInterface2.Stub{

        @Override
        public String getServiceName() throws RemoteException {
            return "I am TrafficService";
        }
    }

    class  MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i("Info","与SecondService连接成功");
            ActivityManager activityManager = (ActivityManager) BusService.this
                    .getSystemService(Context.ACTIVITY_SERVICE);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //Toast.makeText(HSRService.this,"SecondService被杀死",Toast.LENGTH_SHORT).show();
            // 启动FirstService
            BusService.this.startService(new Intent(BusService.this,TrafficGuardService.class));
            //绑定FirstService
            BusService.this.bindService(new Intent(BusService.this,TrafficGuardService.class),conn, Context.BIND_IMPORTANT);
        }
    }

    @Override
    public void onDestroy() {
        startService(new Intent(BusService.this,TRAService.class));
        startService(new Intent(BusService.this,BusService.class));
        startService(new Intent(BusService.this,HSRService.class));
        Intent intent = new Intent("com.example.jt3282.bus.destroy");
        sendBroadcast(intent);
        handler.removeCallbacks(showTime);
        releaseWakeLock();
        stopForeground(true);
        super.onDestroy();
    }

    private Runnable showTime = new Runnable() {

        public void run() {

            //log目前時間
            spe = getSharedPreferences("mypref_selfnotify",MODE_PRIVATE).edit();
            sp = getSharedPreferences("mypref_selfnotify", MODE_PRIVATE);
            Calendar mCal = Calendar.getInstance();
            CharSequence s = DateFormat.format("yyyy-MM-dd", mCal.getTime());
            String s2 = DateFormat.format("yyyy-MM-dd HH:mm", mCal.getTime()) + ":00";
            Calendar calendar = Calendar.getInstance();

            for(int i=0;i<5;i++){

                if(!sp.getString("type"+i,"").equals("")){

                    if(sp.getString("time"+i,"").equals("每日")){
                        try {
                            calendar.setTime(sdf.parse(s+" "+sp.getString("arr_time"+i,"")+":00"));
                            if(s2.equals(""+DateFormat.format("yyyy-MM-dd HH:mm:ss", calendar.getTime()))){
                                //notify
                                NotificationManager mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                                Intent notifyIntent = new Intent(BusService.this, BusService.class);
                                notifyIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
                                PendingIntent appIntent = PendingIntent.getActivity(BusService.this, 0, notifyIntent, 0);

                                Notification notification
                                        = new Notification.Builder(BusService.this)
                                        .setContentIntent(appIntent)
                                        .setSmallIcon(R.mipmap.ic_launcher) // 狀態列裡面的圖示（小圖示）　　
                                        .setLargeIcon(BitmapFactory.decodeResource(BusService.this.getResources(), R.mipmap.ic_launcher)) // 下拉下拉清單裡面的圖示（大圖示）
                                        .setTicker("到站提醒") // 設置狀態列的顯示的資訊
                                        .setAutoCancel(false) // 設置通知被使用者點擊後是否清除  //notification.flags = Notification.FLAG_AUTO_CANCEL;
                                        .setContentTitle(sp.getString("type"+i,"").equals("市區公車")?"市區公車到站提醒":"公路客運到站提醒") // 設置下拉清單裡的標題
                                        .setContentText(sp.getString("info"+i,""))// 設置上下文內容
                                        .setOngoing(true)      //true使notification變為ongoing，用戶不能手動清除// notification.flags = Notification.FLAG_ONGOING_EVENT; notification.flags = Notification.FLAG_NO_CLEAR;
                                        .setDefaults(Notification.DEFAULT_ALL) //使用所有默認值，比如聲音，震動，閃屏等等
                                        .build();

                                notification.flags = Notification.FLAG_ONGOING_EVENT;

                                notification.flags = Notification.FLAG_NO_CLEAR;

                                notification.flags = Notification.FLAG_SHOW_LIGHTS;

                                notification.flags = Notification.FLAG_INSISTENT;

                                mNotificationManager.notify(0, notification);

                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else if(sp.getString("time"+i,"").equals("一次")){
                        try {
                            calendar.setTime(sdf.parse(s+" "+sp.getString("arr_time"+i,"")+":00"));
                            if(s2.equals(""+DateFormat.format("yyyy-MM-dd HH:mm:ss", calendar.getTime()))){
                                //notify
                                NotificationManager mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                                Intent notifyIntent = new Intent(BusService.this, BusService.class);
                                notifyIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
                                PendingIntent appIntent = PendingIntent.getActivity(BusService.this, 0, notifyIntent, 0);

                                Notification notification
                                        = new Notification.Builder(BusService.this)
                                        .setContentIntent(appIntent)
                                        .setSmallIcon(R.mipmap.ic_launcher) // 狀態列裡面的圖示（小圖示）　　
                                        .setLargeIcon(BitmapFactory.decodeResource(BusService.this.getResources(), R.mipmap.ic_launcher)) // 下拉下拉清單裡面的圖示（大圖示）
                                        .setTicker("到站提醒") // 設置狀態列的顯示的資訊
                                        .setAutoCancel(false) // 設置通知被使用者點擊後是否清除  //notification.flags = Notification.FLAG_AUTO_CANCEL;
                                        .setContentTitle(sp.getString("type"+i,"").equals("市區公車")?"市區公車到站提醒":"公路客運到站提醒") // 設置下拉清單裡的標題
                                        .setContentText(sp.getString("info"+i,""))// 設置上下文內容
                                        .setOngoing(true)      //true使notification變為ongoing，用戶不能手動清除// notification.flags = Notification.FLAG_ONGOING_EVENT; notification.flags = Notification.FLAG_NO_CLEAR;
                                        .setDefaults(Notification.DEFAULT_ALL) //使用所有默認值，比如聲音，震動，閃屏等等
                                        .build();

                                notification.flags = Notification.FLAG_ONGOING_EVENT;

                                notification.flags = Notification.FLAG_NO_CLEAR;

                                notification.flags = Notification.FLAG_SHOW_LIGHTS;

                                notification.flags = Notification.FLAG_INSISTENT;

                                mNotificationManager.notify(0, notification);

                                spe.remove("type"+i);
                                spe.remove("info"+i);
                                spe.remove("time"+i);
                                spe.remove("pick"+i);
                                spe.remove("arr_time"+i);
                                spe.commit();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else if(sp.getString("time"+i,"").equals("")){
                        if(sp.getString("pick"+i,"").contains(week[mCal.get(Calendar.DAY_OF_WEEK)-1])){
                            try {
                                calendar.setTime(sdf.parse(s+" "+sp.getString("arr_time"+i,"")+":00"));

                                if(s2.equals(""+DateFormat.format("yyyy-MM-dd HH:mm:ss", calendar.getTime()))){
                                    //notify
                                    NotificationManager mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                                    Intent notifyIntent = new Intent(BusService.this, BusService.class);
                                    notifyIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
                                    PendingIntent appIntent = PendingIntent.getActivity(BusService.this, 0, notifyIntent, 0);

                                    Notification notification
                                            = new Notification.Builder(BusService.this)
                                            .setContentIntent(appIntent)
                                            .setSmallIcon(R.mipmap.ic_launcher) // 狀態列裡面的圖示（小圖示）　　
                                            .setLargeIcon(BitmapFactory.decodeResource(BusService.this.getResources(), R.mipmap.ic_launcher)) // 下拉下拉清單裡面的圖示（大圖示）
                                            .setTicker("到站提醒") // 設置狀態列的顯示的資訊
                                            .setAutoCancel(false) // 設置通知被使用者點擊後是否清除  //notification.flags = Notification.FLAG_AUTO_CANCEL;
                                            .setContentTitle(sp.getString("type"+i,"").equals("市區公車")?"市區公車到站提醒":"公路客運到站提醒") // 設置下拉清單裡的標題
                                            .setContentText(sp.getString("info"+i,""))// 設置上下文內容
                                            .setOngoing(true)      //true使notification變為ongoing，用戶不能手動清除// notification.flags = Notification.FLAG_ONGOING_EVENT; notification.flags = Notification.FLAG_NO_CLEAR;
                                            .setDefaults(Notification.DEFAULT_ALL) //使用所有默認值，比如聲音，震動，閃屏等等
                                            .build();

                                    notification.flags = Notification.FLAG_ONGOING_EVENT;

                                    notification.flags = Notification.FLAG_NO_CLEAR;

                                    notification.flags = Notification.FLAG_SHOW_LIGHTS;

                                    notification.flags = Notification.FLAG_INSISTENT;

                                    mNotificationManager.notify(0, notification);

                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            handler.postDelayed(this, 30000);
        }
    };
    private void acquireWakeLock()
    {
        Log.e("MyGPS","正在申請電源鎖");
        if (null == mWakeLock)
    {
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK , BusService.class.getName());
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