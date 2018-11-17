package com.lifegofood.jt3282.lifego.weather;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.lifegofood.jt3282.lifego.IMyAidlInterface;
import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.traffic.BusService;
import com.lifegofood.jt3282.lifego.traffic.HSRService;
import com.lifegofood.jt3282.lifego.traffic.TRAService;


/**
 * Created by jt3282 on 2018/5/14.
 */

public class WeatherGuardService extends Service{

    private MyBinder binder;
    private MyConn conn;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new MyBinder();
        if(conn==null)
            conn = new MyConn();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        //创建通知栏
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);

        //显示通知栏，服务进程提权为前台服务。
        startForeground(250, builder.build());//使用id：250标记该通知栏

        // 启动第三个服务ThirdService来消除通知栏
        startService(new Intent(this,WeatherClearService.class));

        WeatherGuardService.this.bindService(new Intent(this,WeatherService.class),conn, Context.BIND_IMPORTANT);
        WeatherGuardService.this.bindService(new Intent(this,HSRService.class),conn, Context.BIND_IMPORTANT);
        WeatherGuardService.this.bindService(new Intent(this,TRAService.class),conn, Context.BIND_IMPORTANT);
        WeatherGuardService.this.bindService(new Intent(this,BusService.class),conn, Context.BIND_IMPORTANT);
    }

    class MyBinder extends IMyAidlInterface.Stub{

        @Override
        public String getServiceName() throws RemoteException {
            return "I am SecondService";
        }
    }

    class  MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i("Info","WeatherService連結成功"+WeatherGuardService.this.getApplicationInfo().processName);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            // 启动FirstService
            WeatherGuardService.this.startService(new Intent(WeatherGuardService.this,WeatherService.class));
            WeatherGuardService.this.startService(new Intent(WeatherGuardService.this,HSRService.class));
            WeatherGuardService.this.startService(new Intent(WeatherGuardService.this,TRAService.class));
            WeatherGuardService.this.startService(new Intent(WeatherGuardService.this,BusService.class));
            //绑定FirstService
            WeatherGuardService.this.bindService(new Intent(WeatherGuardService.this,WeatherService.class),conn, Context.BIND_IMPORTANT);
            WeatherGuardService.this.bindService(new Intent(WeatherGuardService.this,HSRService.class),conn, Context.BIND_IMPORTANT);
            WeatherGuardService.this.bindService(new Intent(WeatherGuardService.this,TRAService.class),conn, Context.BIND_IMPORTANT);
            WeatherGuardService.this.bindService(new Intent(WeatherGuardService.this,BusService.class),conn, Context.BIND_IMPORTANT);

        }
    }
}
