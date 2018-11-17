package com.example.jt3282.lifego.frame;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jt3282.lifego.R;
import com.example.jt3282.lifego.edit.DownloadImg;
import com.example.jt3282.lifego.edit.UploadImg;
import com.example.jt3282.lifego.essay.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jt3282 on 2017/12/1.
 */

public class ForthFragment extends Fragment {

    private String UPLOAD_URL = "http://lifego777.000webhostapp.com/getContent.php";
    private String UPDATE_URL = "http://lifego777.000webhostapp.com/excute_good.php";
    private String ICON_URL = "http://lifego777.000webhostapp.com/PhotoUpload/icon.php";
    private String UNLINK_ICON = "http://lifego777.000webhostapp.com/PhotoUpload/delete.php";
    private String user_img,save_num,essay_num,fan_num,concerner_num,bitmapURL;
    View rootView;
    private Uri filePath;
    private int concernnum;
    private int PICK_IMAGE_REQUEST = 1;
    private boolean isconcerned;
    private ImageButton icon;
    private Tools tools=new Tools();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        rootView = inflater.inflate(R.layout.fouth_frame, container, false);

        SharedPreferences preferences = this.getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);  ;
        String user_name = preferences.getString("USER","");

        icon = (ImageButton)rootView.findViewById(R.id.btn_icon);
        TextView user = (TextView)rootView.findViewById(R.id.user);

        user.setText(user_name);
        final Button btn_concern = (Button)rootView.findViewById(R.id.btn_concern);
        isconcern();
        if(isconcerned==true){
            btn_concern.setText("已關注");
        }else {
            btn_concern.setText("關注TA");
        }
        btn_concern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isconcerned==true){
                    btn_concern.setText("關注TA");
                    disconcern();
                    isconcerned=false;
                }else {
                    btn_concern.setText("已關注");
                    concern();
                    isconcerned=true;
                }
            }
        });

        runAPP();
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        return rootView;
    }
    private void isconcern() {

        SharedPreferences preferences = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        ;
        String account = preferences.getString("USER_NAME", "");
        String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT * FROM user_concern WHERE " +
                "account = '" + account + "' AND concerner = '" + account + "'");
        try {
            JSONArray jsonArray = new JSONArray(result);
            concernnum = jsonArray.length();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (concernnum == 0) {
            isconcerned = false;
        } else isconcerned = true;

    }
    private void disconcern(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);  ;
                String account = preferences.getString("USER_NAME","");
                String result = DownloadImg.executeQuery(UPDATE_URL,"DELETE FROM user_concern WHERE " +
                        "account = '"+account+"' AND concerner = '"+account+"'");
            }
        }).start();
    }
    private void concern(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);  ;
                String account = preferences.getString("USER_NAME","");
                String user_name = preferences.getString("USER","");
                String result = DownloadImg.executeQuery(UPDATE_URL,"INSERT INTO user_concern(account,concerner,concerner_name) " +
                        "VALUE('"+account+"','"+account+"','"+user_name+"')");
            }
        }).start();
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
            }
        }).start();
    }
    private class RunTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            SharedPreferences preferences = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);  ;
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
            Log.i("87878787","8787"+img);
            user_img = img;

            setIcon();

        }catch(Exception e) {
            Log.e("log_tag", e.toString());
        }
    }
    private void setIcon(){

        if(user_img.equals("user_icon")){
            icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_user));
        }else{
            tools.imageLoading(getContext(),user_img,icon);
        }
    }
    private class GETTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            SharedPreferences preferences = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);  ;
            String account = preferences.getString("USER_NAME","");
            String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT count(essay_code) AS num FROM on_good" +
                    " WHERE account='"+account+"' AND save='1' GROUP BY account");
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {
            getnum(result);
            TextView saveNum = (TextView)rootView.findViewById(R.id.saveNum);
            saveNum.setText(save_num);
        }
    }
    private void getnum(String result){

        try {
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonData = jsonArray.getJSONObject(0);
            //取得關注文章數
            String num = jsonData.getString("num");
            save_num = num;

        }catch(Exception e) {
            Log.e("log_tag", e.toString());
        }
    }
    private class GETTask2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            SharedPreferences preferences = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);  ;
            String account = preferences.getString("USER_NAME","");
            String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT count(essay_code) AS num FROM upload_img" +
                    " WHERE account='"+account+"' GROUP BY account");
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {
            getnum2(result);
            TextView essaynum = (TextView)rootView.findViewById(R.id.essay_num);
            essaynum.setText(essay_num);
        }
    }
    private void getnum2(String result){

        try {
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonData = jsonArray.getJSONObject(0);
            //取得關注文章數
            String num = jsonData.getString("num");
            essay_num = num;

        }catch(Exception e) {
            Log.e("log_tag", e.toString());
        }
    }
    private class FanTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            SharedPreferences preferences = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
            String account = preferences.getString("USER_NAME", "");
            String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT * FROM user_concern WHERE account='" + account + "'");
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                fan_num = String.valueOf(jsonArray.length());
                TextView fan = (TextView)rootView.findViewById(R.id.fan);
                fan.setText(fan_num);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private class ConcernerTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            SharedPreferences preferences = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
            String account = preferences.getString("USER_NAME", "");
            String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT * FROM user_concern WHERE concerner='" + account + "'");
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                concerner_num = String.valueOf(jsonArray.length());
                TextView concerner = (TextView)rootView.findViewById(R.id.concern);
                concerner.setText(concerner_num);
            } catch (JSONException e) {
                e.printStackTrace();
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

                Bitmap bitmap = getBitmapFormUri(getActivity(), filePath);

                bitmapURL = getStringImage(bitmap);
                icon.setImageBitmap(bitmap);
                uploadImage();
                if(user_img.equals("user_icon")){
                    Log.i("first time","OK");
                }else{
                    deleteBitmap();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void deleteBitmap(){

        String result = DownloadImg.executeQuery(UNLINK_ICON, user_img);

    }
    private void uploadImage() {
        class UploadImage extends AsyncTask<Bitmap, Void, String> {

            ProgressDialog loading;
            UploadImg rh = new UploadImg();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getContext(), "Uploading...", null, true, true);
                Log.i("NOW", "LOADING....");
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();

                if (s.contains("Success"))
                    getActivity().finish();
            }

            @Override
            protected String doInBackground(Bitmap... params) {

                SharedPreferences preferences = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
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
}
