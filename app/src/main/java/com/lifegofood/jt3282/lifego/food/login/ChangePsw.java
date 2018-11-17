package com.lifegofood.jt3282.lifego.food.login;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.food.edit.DownloadImg;

/**
 * Created by jt3282 on 2018/1/2.
 */

public class ChangePsw extends Activity {

    private Button btn_retrun;
    private Button btn_rgt;
    private CheckBox display;
    private EditText rPsw,Account;
    private EditText txtPassword;
    private EditText txtPassword_check;
    private Intent intent;
    private String UPDATE_URL = "http://140.121.199.147/excute_good.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepsw);
        btn_retrun = (Button) findViewById(R.id.btn_return);
        Account = (EditText) findViewById(R.id.recently_account);
        rPsw = (EditText) findViewById(R.id.recently_mima);
        txtPassword = (EditText) findViewById(R.id.ch_psw);
        txtPassword_check = (EditText) findViewById(R.id.ch_check_psw);
        display = (CheckBox) findViewById(R.id.cb_register_display);
        btn_rgt = (Button) findViewById(R.id.btn_sd);
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

        btn_rgt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("871","87");
                String msg_acc = Account.getEditableText().toString();
                String msg_rpsw = rPsw.getEditableText().toString();
                String msg_psw = txtPassword.getEditableText().toString();
                String msg_psw_check = txtPassword_check.getEditableText().toString();

                if(!msg_acc.isEmpty()&&!msg_rpsw.isEmpty()&&!msg_psw.isEmpty()&&!msg_psw_check.isEmpty()){
                    if(msg_psw.equals(msg_psw_check)){
                        CheckPsw(msg_acc,msg_rpsw,msg_psw);
                    }else Toast.makeText(ChangePsw.this, "密碼不相同", Toast.LENGTH_SHORT).show();
                }else Toast.makeText(ChangePsw.this, "欄位為空", Toast.LENGTH_SHORT).show();
            }

        });

        //返回主登入頁面
        btn_retrun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    private void CheckPsw(final String account,final String password,final String newPassword) {
        class CheckTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                String result_login = PhpLogin.sendPostDataToInternet(account,password);
                return result_login;
            }

            @Override
            protected void onPostExecute(String result) {
                String reg = "[a-zA-Z0-9]{8,20}";

                if((result==null)&&(account!=null)){
                    Toast.makeText(ChangePsw.this,"無法連上伺服器", Toast.LENGTH_LONG).show();
                }else if(result.equals("success")){
                    if(!newPassword.matches(reg))Toast.makeText(ChangePsw.this,"新密碼格式錯誤", Toast.LENGTH_LONG).show();
                    else{
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String r = DownloadImg.executeQuery(UPDATE_URL, "UPDATE user_information SET password = '"+newPassword+"' " +
                                        "WHERE account = '" + account + "'");
                            }
                        }).start();
                        Toast.makeText(ChangePsw.this, "更改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else Toast.makeText(ChangePsw.this,"帳號或密碼錯誤", Toast.LENGTH_LONG).show();
            }
        }
        new CheckTask().execute();
    }
}