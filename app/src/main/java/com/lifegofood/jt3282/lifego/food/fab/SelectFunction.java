package com.lifegofood.jt3282.lifego.food.fab;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.FourChoose;
import com.lifegofood.jt3282.lifego.food.CheckCommand;
import com.lifegofood.jt3282.lifego.food.FrameActivity;
import com.lifegofood.jt3282.lifego.R;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;

/**
 * Created by jt3282 on 2017/12/26.
 */

public class SelectFunction extends Activity {


    private static final int REQUEST_EXTERNAL_STORAGE = 100 ;
    int permission=0;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_function);
        sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        final String acc = sp.getString("USER_NAME","");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (! Settings.canDrawOverlays(SelectFunction.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent,10);
            }
        }


        LinearLayout check_command = (LinearLayout)findViewById(R.id.check_command);
        ImageButton startFloatWindow = (ImageButton) findViewById(R.id.voice);
        ImageButton startTouchWindow = (ImageButton) findViewById(R.id.touch);
        startFloatWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    permission = ActivityCompat.checkSelfPermission(SelectFunction.this,
                            ACCESS_FINE_LOCATION);
                }
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    //未取得權限，向使用者要求允許權限
                    ActivityCompat.requestPermissions(SelectFunction.this,
                            new String[] {READ_CONTACTS,ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION},
                            REQUEST_EXTERNAL_STORAGE
                    );
                }else {
                    if (acc.equals("訪客")) {
                        Toast.makeText(SelectFunction.this, "語音模式要登入才可使用唷~", Toast.LENGTH_SHORT).show();
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!Settings.canDrawOverlays(SelectFunction.this)) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intent, 10);
                        } else {
                            Intent intent = new Intent(SelectFunction.this, FloatWindowService.class);
                            startService(intent);
                            finish();
                            FourChoose.instance.finish();
                        }
                    } else {
                        Intent intent = new Intent(SelectFunction.this, FloatWindowService.class);
                        startService(intent);
                        finish();
                        FourChoose.instance.finish();
                    }
                }
            }
        });
        check_command.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(SelectFunction.this, CheckCommand.class);
                startActivity(intent);
                finish();
            }
        });
        startTouchWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    permission = ActivityCompat.checkSelfPermission(SelectFunction.this,
                            ACCESS_FINE_LOCATION);
                }
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    //未取得權限，向使用者要求允許權限
                    ActivityCompat.requestPermissions(SelectFunction.this,
                            new String[] {READ_CONTACTS,ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION},
                            REQUEST_EXTERNAL_STORAGE
                    );
                }else {
                    Intent intent = new Intent(SelectFunction.this, FrameActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
