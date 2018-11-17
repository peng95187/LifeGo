package com.lifegofood.jt3282.lifego.food.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;


public class RegisterActivity extends Activity {
    private Button btn_retrun;
    private Button btn_rgt;
    private CheckBox display;
    private EditText txtAccount;
    private EditText Username;
    private EditText txtPassword;
    private EditText txtPassword_check;
    private SharedPreferences sp;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn_retrun = (Button) findViewById(R.id.btn_return);
        txtAccount = (EditText) findViewById(R.id.et_mail);
        Username = (EditText) findViewById(R.id.et_name);
        txtPassword = (EditText) findViewById(R.id.et_pass);
        txtPassword_check = (EditText) findViewById(R.id.et_pass_re);
        display = (CheckBox) findViewById(R.id.cb_register_display);
        btn_rgt = (Button) findViewById(R.id.btn_rgt);
        intent = new Intent();

        //判斷顯示密碼是否選中
        display.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    txtPassword_check.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    txtPassword_check.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //判斷註冊是否成功
        btn_rgt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  msg_act = txtAccount.getEditableText().toString();
                String  user_name = Username.getEditableText().toString();
                String  msg_psw = txtPassword.getEditableText().toString();
                String  msg_psw_check = txtPassword_check.getEditableText().toString();

                sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("USER_NAME", msg_act);
                editor.putString("PASSWORD",msg_psw);
                editor.putString("USER",user_name);
                editor.commit();
                //email format
                String format = "\\p{Alpha}\\w{2,15}[@][a-z0-9]{3,}[.]\\p{Lower}{2,}";
                String reg = "[A-Za-z0-9]{8,20}";

                if(user_name.contains("'")||user_name.contains(";")){
                    Toast.makeText(RegisterActivity.this, "非法字元", Toast.LENGTH_SHORT).show();
                }else if (!(user_name.isEmpty())&&(msg_act.matches(format)) && (msg_psw.length() >= 8) && !(msg_act.isEmpty()) && !(msg_psw.isEmpty()) && msg_psw_check.equals(msg_psw)) {
                    //註冊資料送給php

                    String result_php = PhpRequest.sendPostDataToInternet(msg_act, msg_psw, user_name,"user_icon","no");


                    if(result_php.contains("Duplicate")){
                        Toast.makeText(RegisterActivity.this, "信箱已被註冊", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(RegisterActivity.this, "註冊成功", Toast.LENGTH_LONG).show();
                        intent.setClass(RegisterActivity.this,LogoActivity.class);
                        intent.putExtra("whichbtn",getIntent().getStringExtra("whichbtn"));
                        editor.putBoolean("AUTO_ISCHECK", true).commit();

                        editor.commit();
                        RegisterActivity.this.startActivity(intent);
                        finish();
                    }
                } else if (!(user_name.isEmpty())&&!(msg_act.matches(format)) && !(msg_act.isEmpty()) && !(msg_psw.isEmpty())) {
                    Toast.makeText(RegisterActivity.this, "信箱格式錯誤", Toast.LENGTH_LONG).show();
                } else if (!(user_name.isEmpty())&&!(msg_psw.matches(reg)) && (msg_act.matches(format)) && (msg_act.isEmpty()) && !(msg_psw.isEmpty()) && msg_psw_check.equals(msg_psw)) {
                    Toast.makeText(RegisterActivity.this, "密碼長度錯誤", Toast.LENGTH_LONG).show();
                } else if (!(user_name.isEmpty())&&!(msg_act.isEmpty()) && !(msg_psw.isEmpty()) && !(msg_psw_check.isEmpty()) && !(msg_psw_check.equals(msg_psw))) {
                    Toast.makeText(RegisterActivity.this, "密碼不相同", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(RegisterActivity.this, "請輸入完整資料", Toast.LENGTH_LONG).show();
            }

        });

        //返回主登入頁面
        btn_retrun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(RegisterActivity.this,LoginActivity.class);
                RegisterActivity.this.startActivity(intent);
                finish();
            }
        });
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
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            Intent intent = new Intent();
            intent.setClass(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
}
