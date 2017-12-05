package com.example.jt3282.lifego.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.jt3282.lifego.FrameActivity;
import com.example.jt3282.lifego.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    private EditText userName, password;
    private CheckBox rem_pw, auto_login, display;
    private Button btn_login;
    private Button btn_register;
    private Button btn_anonymous;
    private ImageButton btnQuit;
    private String userNameValue,passwordValue;
    private SharedPreferences sp;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());

        //获得实例对象
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userName = (EditText) findViewById(R.id.et_zh);
        password = (EditText) findViewById(R.id.et_mima);
        rem_pw = (CheckBox) findViewById(R.id.cb_mima);
        auto_login = (CheckBox) findViewById(R.id.cb_auto);
        display = (CheckBox) findViewById(R.id.cb_display);
        btn_login = (Button) findViewById(R.id.btn_login);
        btnQuit = (ImageButton) findViewById(R.id.img_btn);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_anonymous = (Button) findViewById(R.id.btn_anonymous);

        display.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                else
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });


        //判断记住密码多选框的状态
        if(sp.getBoolean("ISCHECK", false))
        {
            //设置默认是记录密码状态
            rem_pw.setChecked(true);
            userName.setText(sp.getString("USER_NAME", ""));
            password.setText(sp.getString("PASSWORD", ""));
            //判断自动登陆多选框状态
            if(sp.getBoolean("AUTO_ISCHECK", false))
            {
                //设置默认是自动登录状态
                auto_login.setChecked(true);
                //跳转界面
                Intent intent = new Intent(LoginActivity.this,LogoActivity.class);
                LoginActivity.this.startActivity(intent);

            }
        }
        btn_anonymous.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, FrameActivity.class);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("USER","訪客");
                editor.commit();
                startActivity(intent);
            }
        });

        // 登录监听事件  现在默认为用户名为：liu 密码：123
        btn_login.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                userNameValue = userName.getText().toString();
                passwordValue = password.getText().toString();
                String result_login = PhpLogin.sendPostDataToInternet(userNameValue,passwordValue);

                if((result_login==null)&&(userNameValue!=null)){
                    Toast.makeText(LoginActivity.this,"無法連上伺服器", Toast.LENGTH_LONG).show();
                }else if(result_login.equals("success"))
                {
                    Toast.makeText(LoginActivity.this,"登入成功", Toast.LENGTH_SHORT).show();
                    //登录成功和记住密码框为选中状态才保存用户信息
                    if(rem_pw.isChecked())
                    {
                        //记住用户名、密码、
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("USER_NAME",userNameValue);
                        editor.putString("PASSWORD",passwordValue);
                        editor.commit();
                    }
                    //跳转界面
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this,LogoActivity.class);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("USER",userNameValue);
                    editor.commit();
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(LoginActivity.this,"用戶名或密碼錯誤，請重新登入", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
                finish();
            }
        });
        //监听记住密码多选框按钮事件
        rem_pw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (rem_pw.isChecked()) {

                    System.out.println("記住密碼已選中");
                    sp.edit().putBoolean("ISCHECK", true).commit();

                }else {

                    System.out.println("記住密碼未被選中");
                    sp.edit().putBoolean("ISCHECK", false).commit();

                }

            }
        });

        //监听自动登录多选框事件
        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (auto_login.isChecked()) {
                    System.out.println("自動登入已選中");
                    sp.edit().putBoolean("AUTO_ISCHECK", true).commit();

                } else {
                    System.out.println("自動登入未被選中");
                    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
                }
            }
        });

        btnQuit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
