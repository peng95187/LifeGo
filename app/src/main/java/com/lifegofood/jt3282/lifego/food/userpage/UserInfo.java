package com.lifegofood.jt3282.lifego.food.userpage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.food.Report;
import com.lifegofood.jt3282.lifego.food.edit.DownloadImg;
import com.lifegofood.jt3282.lifego.food.essay.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jt3282 on 2017/12/19.
 */

public class UserInfo extends Activity {

    private String UPLOAD_URL = "http://140.121.199.147/getContent.php";
    private String UPDATE_URL = "http://140.121.199.147/excute_good.php";
    private String ONGOOD_URL = "http://140.121.199.147/on_good.php";
    private String user_img,save_num,essay_num,fan_num,concerner_num;
    private int fan;
    private boolean isconcerned;
    private ImageView icon,connect_fb;
    private TextView trust;
    private LinearLayout fanconcern;
    private LinearLayout essay_own,essay_save;
    private Button user_btn_concern,user_btn_report;
    private Tools tools=new Tools();
    private String account,user_name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);

        SharedPreferences preferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        String mineAccount = preferences.getString("USER_NAME", "");

        account = getIntent().getStringExtra("account");
        user_name = getIntent().getStringExtra("user_name");

        fanconcern = (LinearLayout)findViewById(R.id.fanconcern);
        user_btn_report = (Button)findViewById(R.id.user_btn_report);
        user_btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(UserInfo.this, Report.class);
                intent.putExtra("report",account);
                startActivity(intent);
            }
        });
        fanconcern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(UserInfo.this,FanFollowList.class);
                intent.putExtra("account",account);
                startActivity(intent);
            }
        });
        icon = (ImageView)findViewById(R.id.user_img_icon);
        user_btn_concern = (Button)findViewById(R.id.user_btn_concern);
        trust = (TextView)findViewById(R.id.user_trust);
        connect_fb = (ImageView)findViewById(R.id. fbconnect);

        isconcern(mineAccount);

        TextView user = (TextView)findViewById(R.id.user_user);

        user.setText(user_name);

        runAPP();

    }
    private void isconcern(final String acc) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);

                String result = DownloadImg.executeQuery(ONGOOD_URL, "SELECT * FROM user_concern WHERE " +
                        "account = '" + account + "' AND concerner = '" + acc + "'");

                if (result.contains("not empty")) {
                    isconcerned = true;
                } else isconcerned = false;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(acc==account){
                            user_btn_concern.setVisibility(View.GONE);
                        }else {
                            if (isconcerned==true){
                                user_btn_concern.setText("已追蹤");
                            }else{
                                user_btn_concern.setText("追蹤");
                            }
                        }
                        user_btn_concern.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView fans = (TextView)findViewById(R.id.user_fan);
                                if (isconcerned==true){
                                    disconcern();
                                    String s = String.valueOf(fan-1);
                                    fans.setText(s);
                                    isconcerned = false;
                                    user_btn_concern.setText("追蹤");
                                }else{
                                    concern();
                                    String s = String.valueOf(fan+1);
                                    fans.setText(s);
                                    isconcerned = true;
                                    user_btn_concern.setText("已追蹤");
                                }


                            }
                        });
                    }
                });

            }
        }).start();
    }
    private void disconcern(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);  ;
                String concerner = preferences.getString("USER_NAME","");
                String result = DownloadImg.executeQuery(UPDATE_URL,"DELETE FROM user_concern WHERE " +
                        "account = '"+account+"' AND concerner = '"+concerner+"'");
            }
        }).start();
    }
    private void concern(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);  ;
                String mineAccount = preferences.getString("USER_NAME","");
                String user_name = preferences.getString("USER","");
                String result = DownloadImg.executeQuery(UPDATE_URL,"INSERT INTO user_concern(account,concerner,concerner_name) " +
                        "VALUE('"+account+"','"+mineAccount+"','"+user_name+"')");
            }
        }).start();
    }
    private void runAPP(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new TrustTask().execute();
                new RunTask().execute();
                new ConcernTask().execute();
                new FanTask().execute();
                new GETTask2().execute();
                new GETTask().execute();
            }
        }).start();
    }
    private class TrustTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT count_support,(supportnum/count_support) as spnum " +
                    "FROM (SELECT essay_code,count(support) as count_support FROM on_good WHERE" +
                    " support='1' or support='-1' GROUP BY essay_code) as total_num NATURAL JOIN " +
                    "(SELECT essay_code,count(support) as supportnum FROM on_good WHERE" +
                    " support='1' GROUP BY essay_code) as total_save NATURAL JOIN upload_img WHERE account='"+account+"'");

            return result;
        }

        @Override
        protected void onPostExecute(final String result) {

            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonData = jsonArray.getJSONObject(0);
                //取得頭像url
                Double spnum = jsonData.getDouble("spnum");
                final int num = jsonData.getInt("count_support");
                final int rate = (int) (spnum * 100);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(num==0)trust.setText("100");
                        else trust.setText(String.valueOf(rate));
                    }
                });
            }catch(Exception e) {
                Log.e("log_tag", e.toString());
            }
        }
    }
    private class RunTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT user_icon,connect_fb FROM user_information WHERE account='"+account+"'");
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {
            getbitmap(result);
        }
    }
    private void getbitmap(String result){
        try {
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonData = jsonArray.getJSONObject(0);
            //取得頭像url
            String img = jsonData.getString("user_icon");
            String fb = jsonData.getString("connect_fb");
            user_img = img;
            icon.setTag(user_img);
            if(fb.equals("yes")){
                connect_fb.setVisibility(View.VISIBLE);
            }
            setIcon();

        }catch(Exception e) {
            Log.e("log_tag", e.toString());
        }
    }
    private void setIcon(){

        if(user_img.equals("user_icon")){
            icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_user));
        }else{
            tools.imageLoading(UserInfo.this,user_img,icon);
        }
    }
    private class GETTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT count(essay_code) AS num FROM on_good" +
                    " WHERE saver='"+account+"' AND save='1' GROUP BY saver");
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {
            getnum(result);
            essay_save = (LinearLayout) findViewById(R.id.essay_save);
            essay_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("account",account);
                    intent.putExtra("key","yes");
                    intent.setClass(UserInfo.this,EssayOwn.class);
                    startActivity(intent);
                }
            });
        }
    }
    private void getnum(String result){

        try {
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonData = jsonArray.getJSONObject(0);
            //取得關注文章數
            String num = jsonData.getString("num");

            TextView saveNum = (TextView)findViewById(R.id.user_saveNum);
            saveNum.setText(num);

        }catch(Exception e) {
            Log.e("log_tag", e.toString());
        }
    }
    private class GETTask2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT count(essay_code) AS num FROM upload_img" +
                    " WHERE account='"+account+"' GROUP BY account");
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {
            getnum2(result);

            essay_own = (LinearLayout) findViewById(R.id.essay_own);
            essay_own.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("account",account);
                    intent.putExtra("key","no");
                    intent.setClass(UserInfo.this,EssayOwn.class);
                    startActivity(intent);
                }
            });
        }
    }
    //取得追蹤文章數
    private void getnum2(String result){

        try {
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonData = jsonArray.getJSONObject(0);

            String num = jsonData.getString("num");
            TextView essaynum = (TextView)findViewById(R.id.essay_num);
            essaynum.setText(num);
        }catch(Exception e) {
            Log.e("log_tag", e.toString());
        }
    }
    private class FanTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT * FROM user_concern WHERE account='" + account + "'");
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                fan = jsonArray.length();
                fan_num = String.valueOf(jsonArray.length());
                TextView fan = (TextView)findViewById(R.id.user_fan);
                fan.setText(fan_num);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private class ConcernTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT * FROM user_concern WHERE concerner='" + account + "'");
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {

            try {
                JSONArray jsonArray = new JSONArray(result);
                String num = String.valueOf(jsonArray.length());
                TextView concerner = (TextView)findViewById(R.id.user_concern);

                concerner.setText(num);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

