package com.lifegofood.jt3282.lifego.traffic;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.lifegofood.jt3282.lifego.IMyAidlInterface2;
import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.weather.WeatherClearService;

/**
 * Created by jt3282 on 2018/5/15.
 */

public class TrafficGuardService extends Service {

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

        TrafficGuardService.this.bindService(new Intent(this,BusService.class),conn, Context.BIND_IMPORTANT);
        TrafficGuardService.this.bindService(new Intent(this,HSRService.class),conn, Context.BIND_IMPORTANT);
        TrafficGuardService.this.bindService(new Intent(this,TRAService.class),conn, Context.BIND_IMPORTANT);
    }

    class MyBinder extends IMyAidlInterface2.Stub{

        @Override
        public String getServiceName() throws RemoteException {
            return "I am TrafficGuardService";
        }
    }

    class  MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i("Info","TrafficService連結成功"+TrafficGuardService.this.getApplicationInfo().processName);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            // 启动FirstService
            TrafficGuardService.this.startService(new Intent(TrafficGuardService.this,HSRService.class));
            TrafficGuardService.this.startService(new Intent(TrafficGuardService.this,TRAService.class));
            TrafficGuardService.this.startService(new Intent(TrafficGuardService.this,BusService.class));
            //绑定FirstService
            TrafficGuardService.this.bindService(new Intent(TrafficGuardService.this,HSRService.class),conn, Context.BIND_IMPORTANT);
            TrafficGuardService.this.bindService(new Intent(TrafficGuardService.this,TRAService.class),conn, Context.BIND_IMPORTANT);
            TrafficGuardService.this.bindService(new Intent(TrafficGuardService.this,BusService.class),conn, Context.BIND_IMPORTANT);

        }
    }
}
