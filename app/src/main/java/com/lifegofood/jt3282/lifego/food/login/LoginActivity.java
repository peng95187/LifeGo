package com.lifegofood.jt3282.lifego.food.login;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.lifegofood.jt3282.lifego.FourChoose;
import com.lifegofood.jt3282.lifego.food.GMailSender;
import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.food.edit.DownloadImg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    private static final int REQUEST_EXTERNAL_STORAGE = 100;
    private EditText userName, password;
    private CheckBox rem_pw,  display;
    private LinearLayout forget,change;
    private Button btn_login;
    private Button btn_register;
    private Button btn_anonymous;
    private String userNameValue,passwordValue;
    private SharedPreferences sp;
    LoginButton login_button;
    ImageButton facebookView;
    private String CONTENT_URL = "http://140.121.199.147/getContent.php";
    private String ONGOOD_URL = "http://l140.121.199.147/on_good.php";
    String id,name;
    CallbackManager callbackManager;
    int permission=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        printHashKey();

        forget = (LinearLayout)findViewById(R.id.forget_psw);
        change = (LinearLayout)findViewById(R.id.change_psw);
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String account = userName.getText().toString();
                if(!account.isEmpty()){
                    AlertDialog.Builder editDialog = new AlertDialog.Builder(LoginActivity.this);
                    editDialog.setTitle("取回密碼將會寄到您的信箱");

                    editDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                            ForgetPsw(account);
                            Toast.makeText(LoginActivity.this, "信件已寄出，若未收到請稍後再試", Toast.LENGTH_SHORT).show();
                        }
                    });
                    editDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                            //...
                        }
                    });
                    editDialog.show();
                }
                else Toast.makeText(LoginActivity.this, "信箱不能為空", Toast.LENGTH_SHORT).show();
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,ChangePsw.class);
                startActivity(Intent.createChooser(intent,"GMAIL"));
            }
        });
        if(sp.getBoolean("AUTO_ISCHECK", false)) {
            //跳转界面
            Intent intent = new Intent(LoginActivity.this,LogoActivity.class);
            intent.putExtra("whichbtn",getIntent().getStringExtra("whichbtn"));
            LoginActivity.this.startActivity(intent);
            finish();
        }else{
            runAPP();
        }

        //printHashKey();
        facebookView = (ImageButton)findViewById(R.id.facebookView);
        facebookView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    permission = ActivityCompat.checkSelfPermission(LoginActivity.this,
                            WRITE_EXTERNAL_STORAGE);
                }

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    //未取得權限，向使用者要求允許權限
                    ActivityCompat.requestPermissions(LoginActivity.this,
                            new String[] {WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},
                            REQUEST_EXTERNAL_STORAGE
                    );
                }else {
                    if (v.getId() == R.id.facebookView) {
                        login_button.performClick();
                    }
                }
            }
        });
        ImageButton imageButton = (ImageButton)findViewById(R.id.img_btn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initail();
        loginWithFB();
    }
    private void ForgetPsw(final String account){
        class ForgetTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                String result = DownloadImg.executeQuery(CONTENT_URL, "SELECT password FROM user_information WHERE account = '" + account + "'");
                return result;
            }

            @Override
            protected void onPostExecute(String result) {

                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonData = jsonArray.getJSONObject(0);
                    //取得頭像url
                    String password = jsonData.getString("password");
                    if(!password.isEmpty()){
                        try {
                            GMailSender sender = new GMailSender("lifegoofficial@gmail.com", "LIFEGO777");
                            sender.sendMail("LifeGo 美食手冊 忘記密碼通知",
                                    "您的密碼是 : "+password,
                                    "lifegoofficial@gmail.com",
                                    account);
                        } catch (Exception e) {
                            Log.e("SendMail", e.getMessage(), e);
                        }


                    }else Toast.makeText(LoginActivity.this, "查無此帳號", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this, "查無此帳號", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }
        new ForgetTask().execute();
    }

    private void runAPP(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

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

                        if(sp.getBoolean("ISCHECK", false)) {
                            //设置默认是记录密码状态
                            rem_pw.setChecked(true);
                            userName.setText(sp.getString("USER_NAME", ""));
                            password.setText(sp.getString("PASSWORD", ""));
                        }

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
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                    permission = ActivityCompat.checkSelfPermission(LoginActivity.this,
                                            WRITE_EXTERNAL_STORAGE);
                                }

                                if (permission != PackageManager.PERMISSION_GRANTED) {
                                    //未取得權限，向使用者要求允許權限
                                    ActivityCompat.requestPermissions(LoginActivity.this,
                                            new String[] {WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},
                                            REQUEST_EXTERNAL_STORAGE
                                    );
                                }else {
                                    new RunTask().execute();
                                }
                            }
                        });

                        btn_register.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                    permission = ActivityCompat.checkSelfPermission(LoginActivity.this,
                                            WRITE_EXTERNAL_STORAGE);
                                }

                                if (permission != PackageManager.PERMISSION_GRANTED) {
                                    //未取得權限，向使用者要求允許權限
                                    ActivityCompat.requestPermissions(LoginActivity.this,
                                            new String[] {WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},
                                            REQUEST_EXTERNAL_STORAGE
                                    );
                                }else {
                                    regiPage();
                                }
                            }
                        });
                        //监听记住密码多选框按钮事件
                        rem_pw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                                if (rem_pw.isChecked()) {
                                    sp.edit().putBoolean("ISCHECK", true).commit();

                                }else {
                                    sp.edit().putBoolean("ISCHECK", false).commit();
                                }

                            }
                        });
                    }
                });
            }
        }).start();
    }

    private void regiPage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,RegisterActivity.class);
                intent.putExtra("whichbtn",getIntent().getStringExtra("whichbtn"));
                LoginActivity.this.startActivity(intent);
                finish();
            }
        }).start();
    }
    private void nextPage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, LogoActivity.class);
                intent.putExtra("whichbtn",getIntent().getStringExtra("whichbtn"));
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("USER", "訪客");
                editor.putString("USER_NAME", "訪客");
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
                intent.putExtra("whichbtn",getIntent().getStringExtra("whichbtn"));
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("AUTO_ISCHECK", true).commit();
                editor.putBoolean("FB_ISCHECK", false).commit();
                editor.putString("USER_NAME",userNameValue);
                editor.commit();

                startActivity(intent);
                finish();

            }else{
                Toast.makeText(LoginActivity.this,"用戶名或密碼錯誤，請重新登入", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initail(){
        callbackManager = CallbackManager.Factory.create();
        login_button = (LoginButton)findViewById(R.id.login_button);
        login_button.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

    }
    private void loginWithFB(){
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                //https://graph.facebook.com/" + FB.UserId + "/picture?type=large
                                // Application code
                                try {
                                    id = object.getString("id");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    name = object.getString("name"); // 01/31/1980 format
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                checkFB(id,name);
                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this,LogoActivity.class);
                                intent.putExtra("whichbtn",getIntent().getStringExtra("whichbtn"));
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putBoolean("AUTO_ISCHECK", true).commit();
                                editor.putString("USER_NAME",id);
                                editor.putString("USER",name);
                                editor.putBoolean("FB_ISCHECK", true).commit();
                                editor.commit();

                                startActivity(intent);
                                finish();
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }
    @Override
    protected void onActivityResult(int requsetCode, int resultCode, Intent data){
        super.onActivityResult(requsetCode,resultCode,data);
        callbackManager.onActivityResult(requsetCode,resultCode,data);
    }

    public void printHashKey() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.lifegofood.jt3282.lifego",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("TEMPTAGHASH KEY:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void checkFB(final String id, final String name) {
        class UploadImage extends AsyncTask<String, Void, String> {

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
            }

            @Override
            protected String doInBackground(String... params) {
                String URL = "https://graph.facebook.com/" + id + "/picture?type=large";
                String s = DownloadImg.executeQuery(ONGOOD_URL,"SELECT * FROM user_information WHERE account='"+id+"' and connect_fb='yes'");
                if(s.contains("not empty")){
                }else {
                    String result = PhpRequest.sendPostDataToInternet(id, "officailuserfb",name,URL,"yes");
                }
                return "ok";
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵

            Intent intent = new Intent(LoginActivity.this,FourChoose.class);
            startActivity(intent);

            finish();
        }
        return true;
    }
}
