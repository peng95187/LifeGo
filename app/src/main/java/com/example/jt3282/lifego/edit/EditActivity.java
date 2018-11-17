package com.example.jt3282.lifego.edit;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jt3282.lifego.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by jt3282 on 2017/12/3.
 */

public class EditActivity extends Activity implements View.OnClickListener,LocationListener {

    private TextView tv_evaluate;
    private ImageButton btn_expand;
    private LinearLayout ll_evaluate;
    private RelativeLayout rl_expand;

    //location
    private LocationManager locationManager;
    private String provider;
    private static final int REQUEST_FINE_LOCATION_PERMISSION = 102;

    private EditText edit_title;
    private EditText edit_store_name;
    private EditText edit_comment;

    private ImageButton imageButton_load = null;
    private ImageButton imageButton1 = null;
    private ImageButton imageButton2 = null;
    private ImageButton imageButton3 = null;
    private ImageButton imageButton4 = null;
    private ImageButton imageButton5 = null;
    private TextView edit_address;

    private RatingBar rating_environment;
    private RatingBar rating_price;
    private RatingBar rating_service;
    private RatingBar rating_delicious;
    private RatingBar rating_speed;
    private Spinner spinner_open;

    private String uploadImage1;
    private String uploadImage2;
    private String uploadImage3;
    private String uploadImage4;
    private String uploadImage5;

    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private Bitmap bitmap3;
    private Bitmap bitmap4;
    private Bitmap bitmap5;

    private FloatingActionButton edit_upload;

    private Uri filePath;
    private int PICK_IMAGE_REQUEST = 1;
    private double lat;
    private double longt;
    private String address = "NowLocattion";
    public static final String UPLOAD_URL = "https://lifego777.000webhostapp.com/PhotoUpload/EssayUpload.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        provider = locationManager.NETWORK_PROVIDER;

        tv_evaluate = (TextView)findViewById(R.id.tv_evaluate);
        btn_expand = (ImageButton)findViewById(R.id.btn_expand);
        ll_evaluate = (LinearLayout)findViewById(R.id.ll_evaluate);
        rl_expand = (RelativeLayout)findViewById(R.id.rl_expand);

        edit_title =  (EditText)findViewById(R.id.edit_title);
        edit_store_name = (EditText)findViewById(R.id.edit_store_name);
        edit_comment = (EditText)findViewById(R.id.edit_comment);

        rating_environment = (RatingBar)findViewById(R.id.rating_environment);
        rating_price = (RatingBar)findViewById(R.id.rating_price);
        rating_service = (RatingBar)findViewById(R.id.rating_service);
        rating_delicious = (RatingBar)findViewById(R.id.rating_delicious);
        rating_speed = (RatingBar)findViewById(R.id.rating_speed);
        spinner_open = (Spinner)findViewById(R.id.spinner_open);

        imageButton_load = (ImageButton)findViewById(R.id.edit_load);
        imageButton1 = (ImageButton)findViewById(R.id.edit_imbtn1);
        imageButton2 = (ImageButton)findViewById(R.id.edit_imbtn2);
        imageButton3 = (ImageButton)findViewById(R.id.edit_imbtn3);
        imageButton4 = (ImageButton)findViewById(R.id.edit_imbtn4);
        imageButton5 = (ImageButton)findViewById(R.id.edit_imbtn5);

        edit_upload = (FloatingActionButton)findViewById(R.id.upload) ;

        imageButton_load.setOnClickListener(this);
        imageButton1.setOnClickListener(this);
        imageButton2.setOnClickListener(this);
        imageButton3.setOnClickListener(this);
        imageButton4.setOnClickListener(this);
        imageButton5.setOnClickListener(this);
        edit_upload.setOnClickListener(this);
        rl_expand.setOnClickListener(this);
        btn_expand.setOnClickListener(this);
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onClick(View v) {
        if(v == rl_expand || v == btn_expand){
            if(ll_evaluate.getVisibility()==View.GONE){
                btn_expand.setBackgroundResource(android.R.drawable.arrow_up_float);
                ll_evaluate.setVisibility(View.VISIBLE);
                tv_evaluate.setVisibility(View.GONE);
            }else if(ll_evaluate.getVisibility()==View.VISIBLE){
                btn_expand.setBackgroundResource(android.R.drawable.arrow_down_float);
                ll_evaluate.setVisibility(View.GONE);
                tv_evaluate.setVisibility(View.VISIBLE);
            }
        }
        if(v == imageButton_load){
            showFileChooser();
        }
        if (v == imageButton1) {
            if(imageButton1.getDrawable()!=null){
                alertDelete(v);
            }
        }
        if(v == imageButton2){
            if(imageButton2.getDrawable()!=null){
                alertDelete(v);
            }
        }
        if(v == imageButton3){
            if(imageButton3.getDrawable()!=null){
                alertDelete(v);
            }
        }
        if(v == imageButton4){
            if(imageButton4.getDrawable()!=null){
                alertDelete(v);
            }
        }
        if(v == imageButton5){
            if(imageButton5.getDrawable()!=null){
                alertDelete(v);
            }
        }
        if(v == edit_upload){
            String title = edit_title.getText().toString();
            String store_name = edit_store_name.getText().toString();
            String comment = edit_comment.getText().toString();
            float environment = rating_environment.getRating();
            float price = rating_price.getRating();
            float service = rating_service.getRating();
            float delicious = rating_delicious.getRating();
            float speed = rating_speed.getRating();
            String suggest_time = spinner_open.getSelectedItem().toString();

            uploadImage(title,store_name,comment,environment,price,service,delicious,speed,suggest_time);
        }
    }
    private void alertDelete(final View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton(R.string.okLabel,
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(EditActivity.this, "已經成功删除",
                                Toast.LENGTH_LONG).show();
                        deleteBitmap(view);
                        imageButton_load.setVisibility(View.VISIBLE);
                    }
                });
        builder.setNegativeButton(R.string.cancelLabel,
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
        builder.create().show();
    }
    private void deleteBitmap(View v){
        if(v == imageButton1){
            imageButton1.setImageDrawable(null);
            if(bitmap1!=null && !bitmap1.isRecycled()) {
                bitmap1.recycle();
                bitmap1 = null;
            }
            imageButton1.setVisibility(View.GONE);
        }
        if(v == imageButton2){
            imageButton2.setImageDrawable(null);
            if(bitmap2!=null && !bitmap2.isRecycled()) {
                bitmap2.recycle();
                bitmap2 = null;
            }
            imageButton2.setVisibility(View.GONE);
        }
        if(v == imageButton3){
            imageButton3.setImageDrawable(null);
            if(bitmap3!=null && !bitmap3.isRecycled()) {
                bitmap3.recycle();
                bitmap3 = null;
            }
            imageButton3.setVisibility(View.GONE);
        }
        if(v == imageButton4){
            imageButton4.setImageDrawable(null);
            if(bitmap4!=null && !bitmap4.isRecycled()) {
                bitmap4.recycle();
                bitmap4 = null;
            }
            imageButton4.setVisibility(View.GONE);
        }
        if(v == imageButton5){
            imageButton5.setImageDrawable(null);
            if(bitmap5!=null && !bitmap5.isRecycled()) {
                bitmap5.recycle();
                bitmap5 = null;
            }
            imageButton5.setVisibility(View.GONE);
        }
        System.gc();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {

                if(imageButton1.getDrawable()==null){
                    bitmap1 = getBitmapFormUri(this, filePath);
                    imageButton1.setImageBitmap(bitmap1);
                    imageButton1.setVisibility(View.VISIBLE);
                    if(imageButton1.getDrawable()!=null && imageButton2.getDrawable()!=null
                            &&imageButton3.getDrawable()!=null && imageButton4.getDrawable()!=null
                            &&imageButton5.getDrawable()!=null){
                        imageButton_load.setVisibility(View.GONE);
                    }else
                        imageButton_load.setVisibility(View.VISIBLE);
                    uploadImage1 = getStringImage(bitmap1);

                }else if(imageButton2.getDrawable()==null){
                    bitmap2 = getBitmapFormUri(this, filePath);
                    imageButton2.setImageBitmap(bitmap2);
                    imageButton2.setVisibility(View.VISIBLE);
                    if(imageButton1.getDrawable()!=null && imageButton2.getDrawable()!=null
                            &&imageButton3.getDrawable()!=null && imageButton4.getDrawable()!=null
                            &&imageButton5.getDrawable()!=null){
                        imageButton_load.setVisibility(View.GONE);
                    }else
                    imageButton_load.setVisibility(View.VISIBLE);
                    uploadImage2 = getStringImage(bitmap2);

                }else if(imageButton3.getDrawable()==null){
                    bitmap3 = getBitmapFormUri(this, filePath);
                    imageButton3.setImageBitmap(bitmap3);
                    imageButton3.setVisibility(View.VISIBLE);
                    if(imageButton1.getDrawable()!=null && imageButton2.getDrawable()!=null
                            &&imageButton3.getDrawable()!=null && imageButton4.getDrawable()!=null
                            &&imageButton5.getDrawable()!=null){
                        imageButton_load.setVisibility(View.GONE);
                    }else
                    imageButton_load.setVisibility(View.VISIBLE);
                    uploadImage3 = getStringImage(bitmap3);

                }else if(imageButton4.getDrawable()==null){
                    bitmap4 = getBitmapFormUri(this, filePath);
                    imageButton4.setImageBitmap(bitmap4);
                    if(imageButton1.getDrawable()!=null && imageButton2.getDrawable()!=null
                            &&imageButton3.getDrawable()!=null && imageButton4.getDrawable()!=null
                            &&imageButton5.getDrawable()!=null){
                        imageButton_load.setVisibility(View.GONE);
                    }else
                    imageButton_load.setVisibility(View.VISIBLE);
                    imageButton4.setVisibility(View.VISIBLE);
                    uploadImage4 = getStringImage(bitmap4);
                }else{
                    bitmap5 = getBitmapFormUri(this, filePath);
                    imageButton5.setImageBitmap(bitmap5);
                    imageButton5.setVisibility(View.VISIBLE);
                    uploadImage5 = getStringImage(bitmap5);
                    if(imageButton1.getDrawable()!=null && imageButton2.getDrawable()!=null
                            &&imageButton3.getDrawable()!=null && imageButton4.getDrawable()!=null
                            &&imageButton5.getDrawable()!=null){
                        imageButton_load.setVisibility(View.GONE);
                    }else
                    imageButton_load.setVisibility(View.VISIBLE);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
    private void uploadImage(final String title, final String store_name, final String comment,
                             final float environment,final float price,final float service,
                             final float delicious,final float speed,final String suggest_time){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            UploadImg rh = new UploadImg();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EditActivity.this, "Uploading...", null,true,true);
                Log.i("NOW","LOADING....");
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

                if(s.contains("Success"))
                EditActivity.this.finish();
            }

            @Override
            protected String doInBackground(Bitmap... params) {

                String code = getIntent().getStringExtra("account");
                SharedPreferences preferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);  ;
                String user_name = preferences.getString("USER","");

                HashMap<String,String> data = new HashMap<>();

                //post item
                data.put("code",code);
                data.put("title",title);
                data.put("store_name",store_name);
                data.put("location",address);
                data.put("user_name",user_name);
                data.put("class","其他");

                data.put("environment", String.valueOf(environment));
                data.put("price", String.valueOf(price));
                data.put("service", String.valueOf(service));
                data.put("delicious", String.valueOf(delicious));
                data.put("speed", String.valueOf(speed));

                data.put("image1", uploadImage1);
                data.put("image2", uploadImage2);
                data.put("image3", uploadImage3);
                data.put("image4", uploadImage4);
                data.put("image5", uploadImage5);

                data.put("comment",comment);
                data.put("suggest_time",suggest_time);

                String result = rh.sendPostRequest(UPLOAD_URL,data);
                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute();
    }

    //location get
    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        longt = location.getLongitude();
        address = getAddressByLocation(location);
        edit_address = (TextView)findViewById(R.id.edit_address);
        edit_address.setText(address);
        locationManager.removeUpdates(this);
    }
    private String getAddressByLocation(Location location) {
        String returnAddress = "";
        try {
            if (location != null) {
                Double longitude = location.getLongitude();        //取得經度
                Double latitude = location.getLatitude();        //取得緯度

                Geocoder gc = new Geocoder(this, Locale.TRADITIONAL_CHINESE);        //地區:台灣
                //自經緯度取得地址
                List<Address> lstAddress = gc.getFromLocation(latitude, longitude, 1);

                if (!Geocoder.isPresent()){ //Since: API Level 9
                    returnAddress = "Sorry! Geocoder service not Present.";
                }
                returnAddress = lstAddress.get(0).getAddressLine(0);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return returnAddress;
    }
    @Override
    protected void onResume() {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 1, this);
        super.onResume();
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                requestLocationPermission();
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, this);
        } else {
            requestLocationPermission();
            Toast.makeText(getBaseContext(), "請開啟定位服務", Toast.LENGTH_SHORT).show();
        }
    }

    //not work !!
    private void requestLocationPermission() {
        // 如果裝置版本是6.0（包含）以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 取得授權狀態，參數是請求授權的名稱
            int hasPermission = checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION);

            // 如果未授權
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                // 請求授權
                //     第一個參數是請求授權的名稱
                //     第二個參數是請求代碼
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_FINE_LOCATION_PERMISSION);
            }
            else {
                // 啟動地圖與定位元件

            }
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        provider = locationManager.NETWORK_PROVIDER;
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
