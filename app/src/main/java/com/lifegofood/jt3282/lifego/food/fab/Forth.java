package com.lifegofood.jt3282.lifego.food.fab;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.food.edit.DownloadImg;
import com.lifegofood.jt3282.lifego.food.edit.UploadImg;
import com.lifegofood.jt3282.lifego.food.essay.Tools;
import com.lifegofood.jt3282.lifego.food.login.LoginActivity;
import com.lifegofood.jt3282.lifego.food.userpage.EssayOwn;
import com.lifegofood.jt3282.lifego.food.userpage.FanFollowList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by jt3282 on 2017/12/28.
 */

public class Forth extends Activity implements SwipeRefreshLayout.OnRefreshListener{

    private String UPLOAD_URL = "http://140.121.199.147/getContent.php";
    private String UPDATE_URL = "http://140.121.199.147/excute_good.php";
    private String ICON_URL = "http://140.121.199.147/PhotoUpload/icon.php";
    private String UNLINK_ICON = "http://140.121.199.147/PhotoUpload/delete.php";
    private String user_img,save_num,essay_num,fan_num,concerner_num,bitmapURL;
    private LinearLayout essay_own,essay_save,Name_change;
    private Uri filePath;
    private int PICK_IMAGE_REQUEST = 1;
    private ImageButton icon;
    private Button btn_logout;
    private TextView user;
    private Tools tools=new Tools();
    SharedPreferences sp;
    View mFooterView;
    private LinearLayout fanconcern;
    SwipeRefreshLayout refreshLayout;
    private String check="no";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fouth_frame);

        sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        String user_name = sp.getString("USER","");
        final String acc = sp.getString("USER_NAME","");
        refreshLayout=(SwipeRefreshLayout)findViewById(R.id.forth_refresh);
        refreshLayout.setOnRefreshListener(this);
        //为SwipeRefreshLayout设置刷新时的颜色变化，最多可以设置4种
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        fanconcern = (LinearLayout)findViewById(R.id.fanconcern);
        fanconcern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Forth.this, FanFollowList.class);
                intent.putExtra("account",acc);
                startActivity(intent);
            }
        });

        mFooterView = getLayoutInflater().inflate(R.layout.activity_login, null);
        final LoginButton login_button = (LoginButton)mFooterView.findViewById(R.id.login_button);

        icon = (ImageButton)findViewById(R.id.btn_icon);
        user = (TextView)findViewById(R.id.user);
        btn_logout = (Button)findViewById(R.id.btn_logout);
        Name_change = (LinearLayout)findViewById(R.id.Name_change);
        Name_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Change_name();
            }
        });
        if(acc.equals("訪客")){
            btn_logout.setText("請登入");
        }else btn_logout.setText("登出");

        user.setText(user_name);

        runAPP();
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(acc.equals("訪客")) {
                    Toast.makeText(Forth.this, "請先登入", Toast.LENGTH_SHORT).show();
                } else {
                    showFileChooser();
                }
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check.equals("no")){
                    Toast.makeText(Forth.this, "請稍後", Toast.LENGTH_SHORT).show();
                }else {
                    if (btn_logout.getText().equals("請登入")) {
                        Intent intent = new Intent();
                        intent.setClass(Forth.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        AlertDialog.Builder editDialog = new AlertDialog.Builder(Forth.this);
                        editDialog.setTitle("是否登出");

                        editDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            // do something when the button is clicked
                            public void onClick(DialogInterface arg0, int arg1) {

                                if (sp.getBoolean("FB_ISCHECK", false)) {
                                    //login_button.performClick();
                                    LoginManager.getInstance().logOut();

                                }
                                Intent intent = new Intent();
                                intent.setClass(Forth.this, LoginActivity.class);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putBoolean("AUTO_ISCHECK", false).commit();
                                startActivity(intent);
                                finish();
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
                }
            }
        });
    }
    @Override
    public void onRefresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                check="no";
                refreshData();
                refreshLayout.setRefreshing(false);
            }
        });
    }
    private void refreshData() {
        runAPP();
    }
    private void Change_name(){
        AlertDialog.Builder editDialog = new AlertDialog.Builder(Forth.this);
        editDialog.setTitle("修改暱稱");

        final EditText editText = new EditText(Forth.this);
        editText.setText(user.getText());
        editDialog.setView(editText);

        editDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                if(editText.getText().toString().contains(";")||editText.getText().toString().contains("'")){
                    Toast.makeText(Forth.this, "非法字元", Toast.LENGTH_SHORT).show();
                }else {
                    SharedPreferences preferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
                    String account = preferences.getString("USER_NAME", "");
                    user.setText(editText.getText().toString());
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("USER", editText.getText().toString());
                    editor.commit();
                    String result = DownloadImg.executeQuery(UPDATE_URL, "UPDATE user_information SET user_name='" + editText.getText().toString() + "'" +
                            " WHERE account='" + account + "'");
                }
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
    private void runAPP(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new RunTask().execute();
                new ConcernerTask().execute();
                new FanTask().execute();
                new GETTask2().execute();
                new GETTask().execute();
                check="yes";
            }
        }).start();
    }
    private class RunTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            SharedPreferences preferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);  ;
            String account = preferences.getString("USER_NAME","");
            String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT user_icon FROM user_information WHERE account='"+account+"'");
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
            user_img = img;
            icon.setTag(user_img);
            setIcon();
        }catch(Exception e) {
            Log.e("log_tag", e.toString());
        }
    }
    private void setIcon(){

        if(user_img.equals("user_icon")){
            icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_user));
        }else{
            tools.imageLoading(Forth.this,user_img,icon);
        }
    }
    private class GETTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            SharedPreferences preferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);  ;
            String account = preferences.getString("USER_NAME","");
            String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT count(essay_code) AS num FROM on_good" +
                    " WHERE saver='"+account+"' AND save='1' GROUP BY saver");
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {
            getnum(result);

            SharedPreferences preferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);  ;
            final String user = preferences.getString("USER_NAME","");

            essay_save = (LinearLayout) findViewById(R.id.essay_save);
            essay_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("account",user);
                    intent.putExtra("key","yes");
                    intent.setClass(Forth.this,EssayOwn.class);
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

            TextView saveNum = (TextView)findViewById(R.id.saveNum);
            saveNum.setText(num);

        }catch(Exception e) {
            Log.e("log_tag", e.toString());
        }
    }
    private class GETTask2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            SharedPreferences preferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);  ;
            String account = preferences.getString("USER_NAME","");
            String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT count(essay_code) AS num FROM upload_img" +
                    " WHERE account='"+account+"' GROUP BY account");
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {
            getnum2(result);

            SharedPreferences preferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);  ;
            final String user = preferences.getString("USER_NAME","");

            essay_own = (LinearLayout) findViewById(R.id.essay_own);
            essay_own.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("account",user);
                    intent.putExtra("key","no");
                    intent.setClass(Forth.this,EssayOwn.class);
                    startActivity(intent);
                }
            });
        }
    }
    private void getnum2(String result){

        try {
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonData = jsonArray.getJSONObject(0);
            //取得關注文章數
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
            SharedPreferences preferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
            String account = preferences.getString("USER_NAME", "");
            String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT * FROM user_concern WHERE account='" + account + "'");
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {

            int number = 0;
            TextView fan = (TextView)findViewById(R.id.fan);
            try {
                JSONArray jsonArray = new JSONArray(result);
                number = jsonArray.length();
                fan_num = String.valueOf(number);
                fan.setText(fan_num);
            } catch (JSONException e) {
                e.printStackTrace();
                fan.setText("0");
            }
        }
    }
    private class ConcernerTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            SharedPreferences preferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
            String account = preferences.getString("USER_NAME", "");
            String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT * FROM user_concern WHERE concerner='" + account + "'");
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {

            TextView concerner = (TextView)findViewById(R.id.concern);
            try {
                JSONArray jsonArray = new JSONArray(result);
                concerner_num = String.valueOf(jsonArray.length());
                concerner.setText(concerner_num);
            } catch (JSONException e) {
                e.printStackTrace();
                concerner.setText("0");
            }
        }
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {

                Bitmap bitmap = getBitmapFormUri(Forth.this, filePath);

                bitmapURL = getStringImage(bitmap);
                icon.setImageBitmap(bitmap);
                uploadImage();
                if(user_img.equals("user_icon")){
                    Log.i("first time","OK");
                }else{
                    deleteBitmap(user_img.replace("http://140.121.199.147/PhotoUpload/accounts",""));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void deleteBitmap(String img){
        String result = DownloadImg.executeQuery(UNLINK_ICON, img);
    }
    private void uploadImage() {
        class UploadImage extends AsyncTask<Bitmap, Void, String> {

            ProgressDialog loading;
            UploadImg rh = new UploadImg();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Forth.this, "請稍後...", null, true, true);
                Log.i("NOW", "請稍後....");
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                if (s.contains("Success")){

                }else Toast.makeText(Forth.this, "無法上傳", Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {

                SharedPreferences preferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
                String account = preferences.getString("USER_NAME", "");

                HashMap<String, String> data = new HashMap<>();

                //post item
                data.put("account", account);
                data.put("image", bitmapURL);

                String result = rh.sendPostRequest(ICON_URL, data);
                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute();
    }
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵

            finish();
        }
        return true;
    }
}
