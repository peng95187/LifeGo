package com.lifegofood.jt3282.lifego.food.essay;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.food.Report;
import com.lifegofood.jt3282.lifego.food.circleimageview.CircleImageView;
import com.lifegofood.jt3282.lifego.food.edit.DownloadImg;
import com.lifegofood.jt3282.lifego.food.userpage.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jt3282 on 2017/12/14.
 */

public class EssayInfo extends Fragment {

    private String _code;
    private String UPLOAD_URL = "http://140.121.199.147/getContent.php";
    private String ONGOOD_URL = "http://140.121.199.147/on_good.php";
    private String UPDATE_URL = "http://140.121.199.147/excute_good.php";
    private String DELETE_URL = "http://140.121.199.147/PhotoUpload/deletefile.php";
    private String[] im;
    private int num;
    private String account;
    private Tools tools=new Tools();
    private String _title;
    private String acc,_userName,user_img,_userAccount;
    private String _location;
    private String _environment;
    private String _price;
    private String _service;
    private String _delicious;
    private String _speed;
    private int _good=0,_support=0,_save=0;
    private String _comment;
    private String num_support,num_unsupport,num_save,_storeName,_time;
    private int support_num=0;
    private int unsupport_num=0;
    private int save_num=0;
    FloatingActionButton fab_good ;
    Button fab_support ;
    Button fab_unsupport ;
    Button fab_save ;
    private View rootView ;
    private CircleImageView icon;
    private TextView essay_userName;
    private LinearLayout essay_reposrt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        rootView = inflater.inflate(R.layout.essay_info, container, false);

        _code = getActivity().getIntent().getStringExtra("essay_code");
        SharedPreferences preferences = this.getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        account = preferences.getString("USER_NAME", "");
        icon = (CircleImageView)rootView.findViewById(R.id.ei_userIcon);
        essay_userName = (TextView)rootView.findViewById(R.id.ei_username);
        essay_reposrt = (LinearLayout)rootView.findViewById(R.id.essay_reposrt);
        essay_reposrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), Report.class);
                intent.putExtra("report",_code);
                startActivity(intent);
            }
        });
        fab_support = (Button)rootView.findViewById(R.id.ei_support);
        fab_unsupport = (Button)rootView.findViewById(R.id.ei_notsupport);
        fab_save = (Button)rootView.findViewById(R.id.ei_save);

        getNumGood();
        runAPP();

        return rootView;
    }
    private void runAPP(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                _code = getActivity().getIntent().getStringExtra("essay_code");

                new IconTask().execute();
                new RunTask().execute();
                new FABTask().execute();

            }
        }).start();
    }
    private void checkIsYou(){

        if(acc.equals(account)) {

            LinearLayout tv = (LinearLayout) rootView.findViewById(R.id.essay_delete);
            tv.setVisibility(View.VISIBLE);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder editDialog = new AlertDialog.Builder(getContext());
                    editDialog.setTitle("是否刪除");

                    editDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                            new DeleteTask().execute();
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
            });
        }
    }
    private class DeleteTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            _code = getActivity().getIntent().getStringExtra("essay_code");
            String result = DownloadImg.executeQuery(UPDATE_URL, "DELETE FROM upload_info WHERE essay_code='"+_code+"'");
            String result2 = DownloadImg.executeQuery(UPDATE_URL, "DELETE FROM upload_rating WHERE essay_code='"+_code+"'");
            String result3 = DownloadImg.executeQuery(UPDATE_URL, "DELETE FROM upload_img WHERE essay_code='"+_code+"'");
            String result4 = DownloadImg.executeQuery(UPDATE_URL, "DELETE FROM essay_comment WHERE essay_code='"+_code+"'");
            String result5 = DownloadImg.executeQuery(UPDATE_URL, "DELETE FROM on_good WHERE essay_code='"+_code+"'");
            String f_result = DownloadImg.executeQuery(DELETE_URL, _code);
            return f_result;
        }

        @Override
        protected void onPostExecute(String result) {
            if(getActivity()==null)return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), "已刪除", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private class IconTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            if(getActivity()==null)return null;
            _code = getActivity().getIntent().getStringExtra("essay_code");
            String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT account,user_icon,user_name FROM user_information NATURAL JOIN upload_img" +
                    " WHERE essay_code='"+_code+"'");
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
            String user_name = jsonData.getString("user_name");
            String account = jsonData.getString("account");
            acc = account;
            _userName=user_name;
            icon.setTag(img);
            user_img = img;
            essay_userName.setText(user_name);
            setIcon();
            checkIsYou();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setClass(getContext(), UserInfo.class);
                            intent.putExtra("user_name",_userName);
                            intent.putExtra("account",_userAccount);
                            startActivity(intent);
                        }
                    });
                }
            });

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
    private class RunTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = DownloadImg.executeQuery(UPLOAD_URL,"SELECT * FROM upload_img NATURAL JOIN upload_info " +
                    "NATURAL JOIN upload_rating WHERE essay_code='"+_code+"'");
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            getText(result);
        }
    }
    private void getText(final String result){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    JSONArray jsonArray = new JSONArray(result);
                    im = new String[5];
                    num = jsonArray.length();
                    _title = "null";
                    _storeName = "null";
                    _location = "null";
                    _environment ="null";
                    _price = "null";
                    _userAccount = "null";
                    _service = "null";
                    _delicious = "null";
                    _speed = "null";
                    _comment = "null";
                    _location = "null";
                    _time="null";

                    if(num>0) {

                        JSONObject jsonData = jsonArray.getJSONObject(0);
                        String img1 = jsonData.getString("img1");
                        String img2 = jsonData.getString("img2");
                        String img3 = jsonData.getString("img3");
                        String img4 = jsonData.getString("img4");
                        String img5 = jsonData.getString("img5");
                        String title = jsonData.getString("title");
                        String time = jsonData.getString("suggest_time");
                        String store = jsonData.getString("store_name");
                        String account = jsonData.getString("account");
                        String location = jsonData.getString("location");
                        String comment = jsonData.getString("comment");
                        Double environment = jsonData.getDouble("environment");
                        Double price = jsonData.getDouble("price");
                        Double service = jsonData.getDouble("service");
                        Double delicious = jsonData.getDouble("delicious");
                        Double speed = jsonData.getDouble("speed");

                        _location = location;
                        _title = title;
                        _userAccount = account;
                        _comment = comment;
                        _storeName = store;
                        _time = time;
                        _environment = String.valueOf(environment);
                        _price = String.valueOf(price);
                        _service = String.valueOf(service);
                        _delicious = String.valueOf(delicious);
                        _speed = String.valueOf(speed);
                        _location = location;
                        im[0] = img1;
                        im[1] = img2;
                        im[2] = img3;
                        im[3] = img4;
                        im[4] = img5;

                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setText();
                            Imageload();



                        }
                    });
                }catch(Exception e) {
                    Log.e("log_tag", e.toString());
                }
            }
        }).start();
    }
    private void getNumGood(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                _code = getActivity().getIntent().getStringExtra("essay_code");
                num_support = DownloadImg.executeQuery(UPLOAD_URL,"SELECT count(saver) as supportnum FROM on_good WHERE support='1' and essay_code='"+_code+"' GROUP BY essay_code");
                num_unsupport = DownloadImg.executeQuery(UPLOAD_URL,"SELECT count(saver) as unsupportnum FROM on_good WHERE support='-1' and essay_code='"+_code+"' GROUP BY essay_code");
                num_save = DownloadImg.executeQuery(UPLOAD_URL,"SELECT count(saver) as savenum FROM on_good WHERE save='1' and essay_code='"+_code+"' GROUP BY essay_code");

                try {
                    JSONArray result1 = new JSONArray(num_support);
                    JSONObject jsonData = result1.getJSONObject(0);
                    support_num = jsonData.getInt("supportnum");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray result2 = new JSONArray(num_unsupport);
                    JSONObject jsonData = result2.getJSONObject(0);
                    unsupport_num = jsonData.getInt("unsupportnum");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray result3 = new JSONArray(num_save);
                    JSONObject jsonData = result3.getJSONObject(0);
                    save_num = jsonData.getInt("savenum");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void setText(){
        TextView essay_title = (TextView)rootView.findViewById(R.id.ei_title);
        TextView environment = (TextView)rootView.findViewById(R.id.ei_environment);
        TextView price = (TextView)rootView.findViewById(R.id.ei_price);
        TextView service = (TextView)rootView.findViewById(R.id.ei_service);
        TextView delious = (TextView)rootView.findViewById(R.id.ei_delicious);
        TextView speed = (TextView)rootView.findViewById(R.id.ei_speed);
        TextView comment = (TextView)rootView.findViewById(R.id.ei_comment);
        TextView location = (TextView)rootView.findViewById(R.id.ei_location);
        TextView storeName = (TextView)rootView.findViewById(R.id.ei_shop);
        TextView ei_time = (TextView)rootView.findViewById(R.id.ei_time);

        ei_time.setText("建議時段:"+_time);
        storeName.setText(_storeName);
        essay_title.setText(_title);
        environment.setText(_environment);
        price.setText(_price);
        service.setText(_service);
        delious.setText(_delicious);
        speed.setText(_speed);
        comment.setText(_comment);
        location.setText(_location);
    }
    private void Imageload(){
        ImageView image1 = (ImageView)rootView.findViewById(R.id.ei_im1);
        ImageView image2 = (ImageView)rootView.findViewById(R.id.ei_im2);
        ImageView image3 = (ImageView)rootView.findViewById(R.id.ei_im3);
        ImageView image4 = (ImageView)rootView.findViewById(R.id.ei_im4);
        ImageView image5 = (ImageView)rootView.findViewById(R.id.ei_im5);

        if(!im[0].equals("http://140.121.199.147/PhotoUpload/noimage")){
            image1.setTag(im[0]);tools.imageLoading(getContext(),im[0],image1);
        }else image1.setVisibility(View.GONE);
        if(!im[1].equals("http://140.121.199.147/PhotoUpload/noimage")){
            image2.setTag(im[1]);tools.imageLoading(getContext(),im[1],image2);
        }else image2.setVisibility(View.GONE);
        if(!im[2].equals("http://140.121.199.147/PhotoUpload/noimage")){
            image3.setTag(im[2]);tools.imageLoading(getContext(),im[2],image3);
        }else image3.setVisibility(View.GONE);
        if(!im[3].equals("http://140.121.199.147/PhotoUpload/noimage")){
            image4.setTag(im[3]);tools.imageLoading(getContext(),im[3],image4);
        }else image4.setVisibility(View.GONE);
        if(!im[4].equals("http://140.121.199.147/PhotoUpload/noimage")){
            image5.setTag(im[4]);tools.imageLoading(getContext(),im[4],image5);
        }else image5.setVisibility(View.GONE);

        final Intent intent = new Intent();
        intent.setClass(getContext(),ViewImage.class);

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("image",im[0]);
                startActivity(intent);
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("image",im[1]);
                startActivity(intent);
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("image",im[2]);
                startActivity(intent);
            }
        });
        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("image",im[3]);
                startActivity(intent);
            }
        });
        image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("image",im[4]);
                startActivity(intent);
            }
        });

    }

    private class FABTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String check = DownloadImg.executeQuery(ONGOOD_URL, "SELECT * FROM on_good WHERE saver='" +
                    account + "' and essay_code='" + _code + "'");
            String search = DownloadImg.executeQuery(UPLOAD_URL, "SELECT * FROM on_good WHERE saver='" +
                    account + "' and essay_code='" + _code + "'");
            if(check.equals("empty")){
                return check;
            }else return search;
        }

        @Override
        protected void onPostExecute(final String result) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    if (result.equals("empty")) {
                        _good = 0;
                        _support = 0;
                        _save = 0;
                    } else {
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonData = jsonArray.getJSONObject(i);
                                int good = jsonData.getInt("good");
                                int support = jsonData.getInt("support");
                                int save = jsonData.getInt("save");

                                _good = good;
                                _support = support;
                                _save = save;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if(getActivity() == null)
                        return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!account.equals("訪客")) buttonActivity();
                            else{
                                Toast.makeText(getContext(), "請登入", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }).start();
        }
    }
    private void buttonActivity(){
        fab_good = (FloatingActionButton)rootView.findViewById(R.id.ei_good);
        fab_support = (Button)rootView.findViewById(R.id.ei_support);
        fab_unsupport = (Button)rootView.findViewById(R.id.ei_notsupport);
        fab_save = (Button)rootView.findViewById(R.id.ei_save);

        if(_good==1){
            fab_good.setImageResource(R.drawable.good);
        }else fab_good.setImageResource(R.drawable.ungood);
        if(_support==1){
            fab_support.setBackgroundResource(R.drawable.button_coral);
            fab_support.setText("已支持("+support_num+")");
        }else fab_support.setText("支持("+support_num+")");
        if(_support==-1){
            fab_unsupport.setBackgroundResource(R.drawable.button_coral);
            fab_unsupport.setText("已反對("+unsupport_num+")");
        }fab_unsupport.setText("反對("+unsupport_num+")");
        if(_save==1){
            fab_save.setBackgroundResource(R.drawable.button_coral);
            fab_save.setText("已收藏("+save_num+")");
        }fab_save.setText("收藏("+save_num+")");

        fab_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(_good==0&&_support==0&&_save==0){
                            _good = 1;
                            String result = DownloadImg.executeQuery(UPDATE_URL,"INSERT INTO on_good(saver,essay_code" +
                                    ",good,support,save) VALUE('"+account+"','"+_code+"','1','0','0')");

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    fab_good.setImageResource(R.drawable.good);
                                    Toast.makeText(getContext(),"讚+1", Toast.LENGTH_LONG).show();
                                }
                            });
                        }else if(_good==1){
                            _good = 0;
                            if(_support==0&&_save==0){
                                String result = DownloadImg.executeQuery(UPDATE_URL,"DELETE FROM on_good " +
                                        "WHERE saver='"+account+"' and essay_code='"+_code+"'");

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        fab_good.setImageResource(R.drawable.ungood);
                                        Toast.makeText(getContext(),"已收回讚", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }else{
                                String result = DownloadImg.executeQuery(UPDATE_URL,"UPDATE on_good SET good='0' " +
                                        "WHERE saver='"+account+"' and essay_code='"+_code+"'");

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        fab_good.setImageResource(R.drawable.ungood);
                                        Toast.makeText(getContext(),"已收回讚", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                        else{
                            _good = 1;
                            String result = DownloadImg.executeQuery(UPDATE_URL,"UPDATE on_good SET good='1' " +
                                    "WHERE saver='"+account+"' and essay_code='"+_code+"'");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    fab_good.setImageResource(R.drawable.good);
                                    Toast.makeText(getContext(),"讚+1", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
        fab_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(_good==0&&_support==0&&_save==0){
                            _support = 1;
                            support_num=support_num+1;
                            String result = DownloadImg.executeQuery(UPDATE_URL,"INSERT INTO on_good(saver,essay_code" +
                                    ",good,support,save) VALUE('"+account+"','"+_code+"','0','1','0')");
                            getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                fab_support.setText("已支持(" + support_num + ")");
                                                                fab_support.setBackgroundResource(R.drawable.button_coral);
                                                                Toast.makeText(getContext(), "支持+1", Toast.LENGTH_LONG).show();
                                                            }
                                                        });

                        }else if(_support==1){
                            _support = 0;
                            support_num=support_num-1;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    fab_support.setText("支持(" + support_num + ")");
                                    fab_support.setBackgroundResource(R.drawable.button_gray);
                                }
                            });
                            if(_good==0&&_save==0){
                                String result = DownloadImg.executeQuery(UPDATE_URL,"DELETE FROM on_good " +
                                        "WHERE saver='"+account+"' and essay_code='"+_code+"'");
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "已收回支持", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }else{
                                String result = DownloadImg.executeQuery(UPDATE_URL,"UPDATE on_good SET support='0' " +
                                        "WHERE saver='"+account+"' and essay_code='"+_code+"'");
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "已收回支持", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }else if(_support==-1){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "你已選擇反對", Toast.LENGTH_LONG).show();
                                }
                            });
                        }else{
                            _support=1;
                            support_num+=1;
                            getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                fab_support.setBackgroundResource(R.drawable.button_coral);
                                                                fab_support.setText("已支持(" + support_num + ")");
                                                                Toast.makeText(getContext(), "支持+1", Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                            String result = DownloadImg.executeQuery(UPDATE_URL,"UPDATE on_good SET support='1' " +
                                    "WHERE saver='"+account+"' and essay_code='"+_code+"'");
                        }
                    }
                }).start();
            }
        });
        fab_unsupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(_good==0&&_support==0&&_save==0){
                            _support = -1;
                            unsupport_num = unsupport_num + 1;
                            String result = DownloadImg.executeQuery(UPDATE_URL,"INSERT INTO on_good(saver,essay_code" +
                                    ",good,support,save) VALUE('"+account+"','"+_code+"','0','-1','0')");
                            getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                fab_unsupport.setText("已反對(" + unsupport_num + ")");
                                                                fab_unsupport.setBackgroundResource(R.drawable.button_coral);
                                                                Toast.makeText(getContext(), "反對+1", Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                        }else if(_support==-1){
                            _support = 0;
                            unsupport_num = unsupport_num - 1;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    fab_unsupport.setText("反對(" + unsupport_num + ")");
                                    fab_unsupport.setBackgroundResource(R.drawable.button_gray);
                                }
                            });
                            if(_good==0&&_save==0){
                                String result = DownloadImg.executeQuery(UPDATE_URL,"DELETE FROM on_good " +
                                        "WHERE saver='"+account+"' and essay_code='"+_code+"'");
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "已收回反對", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }else{
                                String result = DownloadImg.executeQuery(UPDATE_URL,"UPDATE on_good SET support='0' " +
                                        "WHERE saver='"+account+"' and essay_code='"+_code+"'");

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "已收回反對", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }else if(_support==1){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "你已選擇支持", Toast.LENGTH_LONG).show();
                                }
                            });
                        }else{
                            _support=-1;
                            unsupport_num = unsupport_num + 1;
                            getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                fab_unsupport.setBackgroundResource(R.drawable.button_coral);
                                                                fab_unsupport.setText("已反對(" + unsupport_num + ")");
                                                                Toast.makeText(getContext(), "反對+1", Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                            String result = DownloadImg.executeQuery(UPDATE_URL,"UPDATE on_good SET support='-1' " +
                                    "WHERE saver='"+account+"' and essay_code='"+_code+"'");
                        }
                    }
                }).start();
            }
        });
        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(_good==0&&_support==0&&_save==0){
                            _save = 1;
                            save_num=save_num+1;
                            String result = DownloadImg.executeQuery(UPDATE_URL,"INSERT INTO on_good(saver,essay_code" +
                                    ",good,support,save) VALUE('"+account+"','"+_code+"','0','0','1')");
                            getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                fab_save.setText("已收藏(" + save_num + ")");
                                                                fab_save.setBackgroundResource(R.drawable.button_coral);
                                                                Toast.makeText(getContext(), "收藏+1", Toast.LENGTH_LONG).show();
                                                            }
                                                        });

                        }else if(_save==1){
                            _save = 0;
                            save_num=save_num-1;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    fab_save.setText("收藏(" + save_num + ")");
                                    fab_save.setBackgroundResource(R.drawable.button_gray);
                                }
                            });
                            if(_good==0&&_support==0){
                                String result = DownloadImg.executeQuery(UPDATE_URL,"DELETE FROM on_good " +
                                        "WHERE saver='"+account+"' and essay_code='"+_code+"'");

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "已收回收藏", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }else{
                                String result = DownloadImg.executeQuery(UPDATE_URL,"UPDATE on_good SET save='0' " +
                                        "WHERE saver='"+account+"' and essay_code='"+_code+"'");

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "已收回收藏", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }else{
                            _save=1;
                            save_num=save_num+1;
                            getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                fab_save.setBackgroundResource(R.drawable.button_coral);
                                                                fab_save.setText("已收藏(" + save_num + ")");
                                                                Toast.makeText(getContext(), "收藏+1", Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                            String result = DownloadImg.executeQuery(UPDATE_URL,"UPDATE on_good SET save='1' " +
                                    "WHERE saver='"+account+"' and essay_code='"+_code+"'");

                        }
                    }
                }).start();

            }
        });
    }
}

