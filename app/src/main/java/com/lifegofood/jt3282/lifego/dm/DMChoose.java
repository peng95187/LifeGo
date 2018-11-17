package com.lifegofood.jt3282.lifego.dm;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.FourChoose;
import com.lifegofood.jt3282.lifego.R;

import static android.Manifest.permission.READ_CONTACTS;

public class DMChoose extends Activity {
    private static final int REQUEST_EXTERNAL_STORAGE = 100 ;
    int permission=0;
    Button mv, kr, jp, anime, voice;
    TextView voice_show;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dm_choose);
        sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        final String acc = sp.getString("USER_NAME","");

        mv = (Button) findViewById(R.id.mv);
        mv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DMChoose.this,Movie_Hot.class);
                startActivity(intent);
            }
        });
        kr = (Button) findViewById(R.id.kr);
        kr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DMChoose.this,Drama_Hot.class);
                intent.putExtra("type","kr");
                startActivity(intent);
            }
        });
        jp = (Button) findViewById(R.id.jp);
        jp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DMChoose.this,Drama_Hot.class);
                intent.putExtra("type","jp");
                startActivity(intent);
            }
        });
        anime = (Button) findViewById(R.id.anime);
        anime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DMChoose.this,Anime_Hot.class);
                intent.putExtra("type","jp");
                startActivity(intent);
            }
        });
        voice = (Button) findViewById(R.id.voice);
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    //未取得權限，向使用者要求允許權限
                    ActivityCompat.requestPermissions(DMChoose.this,
                            new String[] {READ_CONTACTS},
                            REQUEST_EXTERNAL_STORAGE
                    );
                }else {
                    if (acc.equals("訪客")) {
                        Toast.makeText(DMChoose.this, "語音模式要登入才可使用唷~", Toast.LENGTH_SHORT).show();
                    }
                    else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!Settings.canDrawOverlays(DMChoose.this)) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intent, 10);
                        } else {
                            Intent intent = new Intent(DMChoose.this,DMFloatWindowService.class);
                            startService(intent);
                            finish();
                            FourChoose.instance.finish();
                        }
                    } else {
                        Intent intent = new Intent(DMChoose.this,DMFloatWindowService.class);
                        startService(intent);
                        finish();
                        FourChoose.instance.finish();
                    }
                }
            }
        });
        voice_show = (TextView) findViewById(R.id.voice_show);
        voice_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DMChoose.this,DMVoiceShow.class);
                startActivity(intent);
            }
        });
    }
}
