package com.example.jt3282.lifego.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jt3282.lifego.FrameActivity;
import com.example.jt3282.lifego.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    private EditText userName, password;
    private CheckBox rem_pw,  display;
    private Button btn_login;
    private Button btn_register;
    private Button btn_anonymous;
    private String userNameValue,passwordValue;
    private SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        runAPP();
    }
    private void runAPP(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                AutoPage();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

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

                        userName = (EditText) findViewById(R.id.et_zh);
                        password = (EditText) findViewById(R.id.et_mima);
                        rem_pw = (CheckBox) findViewById(R.id.cb_mima);
                        display = (CheckBox) findViewById(R.id.cb_display);
                        btn_login = (Button) findViewById(R.id.btn_login);
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

                        btn_anonymous.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                nextPage();
                            }
                        });

                        // 登录监听事件
                        btn_login.setOnClickListener(new View.OnClickListener() {

                            public void onClick(View v) {
                                userNameValue = userName.getText().toString();
                                passwordValue = password.getText().toString();

                                new RunTask().execute();
                            }
                        });

                        btn_register.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                regiPage();
                            }
                        });
                        //监听记住密码多选框按钮事件
                    }
                });
            }
        }).start();
    }
    private void AutoPage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(sp.getBoolean("AUTO_ISCHECK", false))
                {
                    //跳转界面
                    Intent intent = new Intent(LoginActivity.this,LogoActivity.class);
                    LoginActivity.this.startActivity(intent);
                    finish();
                }

            }
        }).start();
    }
    private void regiPage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        }).start();
    }
    private void nextPage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, FrameActivity.class);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("USER", "訪客");
                editor.commit();
                startActivity(intent);
            }
        }).start();
    }
    private class RunTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String result_login = PhpLogin.sendPostDataToInternet(userNameValue,passwordValue);
            return result_login;
        }
        @Override
        protected void onPostExecute(String result) {
            if((result==null)&&(userNameValue!=null)){
                Toast.makeText(LoginActivity.this,"無法連上伺服器", Toast.LENGTH_LONG).show();
            }else if(result.equals("success"))
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
                editor.putBoolean("AUTO_ISCHECK", false).commit();
                editor.putString("USER_NAME",userNameValue);
                editor.putString("USER",userNameValue);
                editor.commit();

                startActivity(intent);
                finish();

            }else{
                Toast.makeText(LoginActivity.this,"用戶名或密碼錯誤，請重新登入", Toast.LENGTH_LONG).show();
            }
        }
    }
}
