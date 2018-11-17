package com.example.jt3282.lifego.essay;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jt3282.lifego.R;
import com.example.jt3282.lifego.edit.DownloadImg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jt3282 on 2017/12/14.
 */

public class EssayInfo extends Fragment {

    private String _code;
    private String UPLOAD_URL = "http://lifego777.000webhostapp.com/getContent.php";
    private String ONGOOD_URL = "http://lifego777.000webhostapp.com/on_good.php";
    private String UPDATE_URL = "http://lifego777.000webhostapp.com/excute_good.php";
    private String NUM_URL = "http://lifego777.000webhostapp.com/num_good.php";
    private String[] im;
    private int num;
    private String account;
    private Tools tools=new Tools();
    private String _title;
    private String _userName;
    private String _location;
    private String _environment;
    private String _price;
    private String _service;
    private String _delicious;
    private String _speed;
    private int _good=0,_support=0,_save=0;
    private String _comment;
    private String num_support,num_unsupport,num_save;
    private int support_num=0;
    private int unsupport_num=0;
    private int save_num=0;

    FloatingActionButton fab_good ;
    Button fab_support ;
    Button fab_unsupport ;
    Button fab_save ;
    private View rootView ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        rootView = inflater.inflate(R.layout.essay_info, container, false);

        SharedPreferences preferences = this.getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        account = preferences.getString("USER_NAME", "");

        getNumGood();

        runAPP();
        return rootView;
    }
    private void runAPP(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                _code = getActivity().getIntent().getStringExtra("essay_code");

                new RunTask().execute();
                new FABTask().execute();
            }
        }).start();
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
                    _location = "null";
                    _userName = "null";
                    _environment ="null";
                    _price = "null";
                    _service = "null";
                    _delicious = "null";
                    _speed = "null";
                    _comment = "null";
                    _location = "null";

                    if(num>0)
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonData = jsonArray.getJSONObject(i);
                            String img1 = jsonData.getString("img1");
                            String img2 = jsonData.getString("img2");
                            String img3 = jsonData.getString("img3");
                            String img4 = jsonData.getString("img4");
                            String img5 = jsonData.getString("img5");
                            String title = jsonData.getString("title");
                            String user_name = jsonData.getString("user_name");
                            String location = jsonData.getString("location");
                            String comment = jsonData.getString("comment");
                            Double environment = jsonData.getDouble("environment");
                            Double price = jsonData.getDouble("price");
                            Double service = jsonData.getDouble("service");
                            Double delicious = jsonData.getDouble("delicious");
                            Double speed = jsonData.getDouble("speed");

                            _location = location;
                            _title = title;
                            _userName = user_name;
                            _comment = comment;
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
        num_support = DownloadImg.executeQuery(NUM_URL,"SELECT * FROM on_good WHERE support='1'");
        num_unsupport = DownloadImg.executeQuery(NUM_URL,"SELECT * FROM on_good WHERE support='-1'");
        num_save = DownloadImg.executeQuery(NUM_URL,"SELECT * FROM on_good WHERE save='1'");

        if(num_support.contains("one")){
            support_num = 1;
        }else if(num_support.contains("zero")){
            support_num = 0;
        }else {
            try {
                support_num = Integer.parseInt(num_support);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        if(num_unsupport.contains("one")){
            unsupport_num = 1;
        }else if(num_unsupport.contains("zero")) {
            unsupport_num = 0;
        }else{
            try {
                unsupport_num = Integer.parseInt(num_unsupport);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        if(num_save.contains("one")){
            save_num = 1;
        }else if(num_save.contains("zero")){
            save_num = 0;
        }else{
            try {
                save_num = Integer.parseInt(num_save);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }
    private void setText(){
        TextView essay_userName = (TextView)rootView.findViewById(R.id.ei_username);
        TextView essay_title = (TextView)rootView.findViewById(R.id.ei_title);
        TextView environment = (TextView)rootView.findViewById(R.id.ei_environment);
        TextView price = (TextView)rootView.findViewById(R.id.ei_price);
        TextView service = (TextView)rootView.findViewById(R.id.ei_service);
        TextView delious = (TextView)rootView.findViewById(R.id.ei_delicious);
        TextView speed = (TextView)rootView.findViewById(R.id.ei_speed);
        TextView comment = (TextView)rootView.findViewById(R.id.ei_comment);
        TextView location = (TextView)rootView.findViewById(R.id.ei_location);

        essay_userName.setText(_userName);
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

        tools.imageLoading(getContext(),im[0],image1);
        tools.imageLoading(getContext(),im[1],image2);
        tools.imageLoading(getContext(),im[2],image3);
        tools.imageLoading(getContext(),im[3],image4);
        tools.imageLoading(getContext(),im[4],image5);

    }

    private class FABTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String check = DownloadImg.executeQuery(ONGOOD_URL, "SELECT * FROM on_good WHERE account='" +
                    account + "' and essay_code='" + _code + "'");
            String search = DownloadImg.executeQuery(UPLOAD_URL, "SELECT * FROM on_good WHERE account='" +
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            buttonActivity();
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

        if(_support==1){
            fab_support.setBackgroundResource(R.drawable.button_coral);
            fab_support.setText("已支持("+support_num+")");
        }
        if(_support==-1){
            fab_unsupport.setBackgroundResource(R.drawable.button_coral);
            fab_unsupport.setText("已反對("+unsupport_num+")");
        }
        if(_save==1){
            fab_save.setBackgroundResource(R.drawable.button_coral);
            fab_save.setText("已收藏("+save_num+")");
        }

        fab_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_good==0&&_support==0&&_save==0){
                    _good = 1;
                    String result = DownloadImg.executeQuery(UPDATE_URL,"INSERT INTO on_good(account,essay_code" +
                            ",good,support,save) VALUE('"+account+"','"+_code+"','1','0','0')");

                    Log.i("result_good",result);
                    Toast.makeText(getContext(),"讚+1", Toast.LENGTH_LONG).show();
                }else if(_good==1){
                    fab_good.setBackgroundColor(getResources().getColor(R.color.steelblue));
                    _good = 0;
                    if(_support==0&&_save==0){
                        String result = DownloadImg.executeQuery(UPDATE_URL,"DELETE FROM on_good " +
                                "WHERE account='"+account+"' and essay_code='"+_code+"'");
                        Log.i("result_good",result);
                        Toast.makeText(getContext(),"已收回讚", Toast.LENGTH_LONG).show();
                    }else{
                        String result = DownloadImg.executeQuery(UPDATE_URL,"UPDATE on_good SET good='0' " +
                                "WHERE account='"+account+"' and essay_code='"+_code+"'");
                        Log.i("result_good",result);
                        Toast.makeText(getContext(),"已收回讚", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    _good = 1;
                    String result = DownloadImg.executeQuery(UPDATE_URL,"UPDATE on_good SET good='1' " +
                            "WHERE account='"+account+"' and essay_code='"+_code+"'");
                    Toast.makeText(getContext(),"讚+1", Toast.LENGTH_LONG).show();
                }
            }
        });
        fab_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_good==0&&_support==0&&_save==0){
                    _support = 1;
                    support_num=support_num+1;
                    String result = DownloadImg.executeQuery(UPDATE_URL,"INSERT INTO on_good(account,essay_code" +
                            ",good,support,save) VALUE('"+account+"','"+_code+"','0','1','0')");
                    fab_support.setText("已支持("+num_support+")");
                    fab_support.setBackgroundResource(R.drawable.button_coral);
                    Toast.makeText(getContext(),"支持+1", Toast.LENGTH_LONG).show();
                }else if(_support==1){
                    _support = 0;
                    support_num=support_num-1;
                    fab_support.setText("支持("+support_num+")");
                    fab_support.setBackgroundResource(R.drawable.button_gray);
                    if(_good==0&&_save==0){
                        String result = DownloadImg.executeQuery(UPDATE_URL,"DELETE FROM on_good " +
                                "WHERE account='"+account+"' and essay_code='"+_code+"'");
                        Toast.makeText(getContext(),"已收回支持", Toast.LENGTH_LONG).show();
                    }else{
                        String result = DownloadImg.executeQuery(UPDATE_URL,"UPDATE on_good SET support='0' " +
                                "WHERE account='"+account+"' and essay_code='"+_code+"'");
                        Toast.makeText(getContext(),"已收回支持", Toast.LENGTH_LONG).show();
                    }
                }else if(_support==-1){
                    Toast.makeText(getContext(),"你已選擇反對", Toast.LENGTH_LONG).show();
                }else{
                    _support=1;
                    support_num+=1;
                    fab_support.setBackgroundResource(R.drawable.button_coral);
                    fab_support.setText("已支持("+num_support+")");
                    String result = DownloadImg.executeQuery(UPDATE_URL,"UPDATE on_good SET support='1' " +
                            "WHERE account='"+account+"' and essay_code='"+_code+"'");
                    Toast.makeText(getContext(),"支持+1", Toast.LENGTH_LONG).show();
                }
            }
        });
        fab_unsupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_good==0&&_support==0&&_save==0){
                    _support = -1;
                    unsupport_num = unsupport_num + 1;
                    String result = DownloadImg.executeQuery(UPDATE_URL,"INSERT INTO on_good(account,essay_code" +
                            ",good,support,save) VALUE('"+account+"','"+_code+"','0','-1','0')");
                    fab_unsupport.setText("已反對("+unsupport_num+")");
                    fab_unsupport.setBackgroundResource(R.drawable.button_coral);
                    Toast.makeText(getContext(),"反對+1", Toast.LENGTH_LONG).show();
                }else if(_support==-1){
                    _support = 0;
                    unsupport_num = unsupport_num - 1;
                    fab_unsupport.setText("反對("+unsupport_num+")");
                    fab_unsupport.setBackgroundResource(R.drawable.button_gray);
                    if(_good==0&&_save==0){
                        String result = DownloadImg.executeQuery(UPDATE_URL,"DELETE FROM on_good " +
                                "WHERE account='"+account+"' and essay_code='"+_code+"'");
                        Log.i("result_good",result);
                        Toast.makeText(getContext(),"已收回反對", Toast.LENGTH_LONG).show();
                    }else{
                        String result = DownloadImg.executeQuery(UPDATE_URL,"UPDATE on_good SET support='0' " +
                                "WHERE account='"+account+"' and essay_code='"+_code+"'");
                        Log.i("result_good",result);
                        Toast.makeText(getContext(),"已收回反對", Toast.LENGTH_LONG).show();
                    }
                }else if(_support==1){
                    Toast.makeText(getContext(),"你已選擇支持", Toast.LENGTH_LONG).show();
                }else{
                    _support=-1;
                    unsupport_num = unsupport_num + 1;
                    fab_unsupport.setBackgroundResource(R.drawable.button_coral);
                    fab_unsupport.setText("已反對("+unsupport_num+")");
                    String result = DownloadImg.executeQuery(UPDATE_URL,"UPDATE on_good SET support='-1' " +
                            "WHERE account='"+account+"' and essay_code='"+_code+"'");
                    Toast.makeText(getContext(),"反對+1", Toast.LENGTH_LONG).show();
                }
            }
        });
        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_good==0&&_support==0&&_save==0){
                    _save = 1;
                    save_num=save_num+1;
                    String result = DownloadImg.executeQuery(UPDATE_URL,"INSERT INTO on_good(account,essay_code" +
                            ",good,support,save) VALUE('"+account+"','"+_code+"','0','0','1')");
                    fab_save.setText("已收藏("+save_num+")");
                    fab_save.setBackgroundResource(R.drawable.button_coral);
                    Toast.makeText(getContext(),"收藏+1", Toast.LENGTH_LONG).show();
                }else if(_save==1){
                    _save = 0;
                    save_num=save_num-1;
                    fab_save.setText("收藏("+save_num+")");
                    fab_save.setBackgroundResource(R.drawable.button_gray);
                    if(_good==0&&_support==0){
                        String result = DownloadImg.executeQuery(UPDATE_URL,"DELETE FROM on_good " +
                                "WHERE account='"+account+"' and essay_code='"+_code+"'");
                        Log.i("result_good",result);
                        Toast.makeText(getContext(),"已收回收藏", Toast.LENGTH_LONG).show();
                    }else{
                        String result = DownloadImg.executeQuery(UPDATE_URL,"UPDATE on_good SET save='0' " +
                                "WHERE account='"+account+"' and essay_code='"+_code+"'");
                        Log.i("result_good",result);
                        Toast.makeText(getContext(),"已收回收藏", Toast.LENGTH_LONG).show();
                    }
                }else{
                    _save=1;
                    save_num=save_num+1;
                    fab_save.setBackgroundResource(R.drawable.button_coral);
                    fab_save.setText("已收藏("+save_num+")");
                    String result = DownloadImg.executeQuery(UPDATE_URL,"UPDATE on_good SET save='1' " +
                            "WHERE account='"+account+"' and essay_code='"+_code+"'");
                    Toast.makeText(getContext(),"收藏+1", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

