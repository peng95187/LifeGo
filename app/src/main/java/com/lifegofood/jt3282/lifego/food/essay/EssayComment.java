package com.lifegofood.jt3282.lifego.food.essay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.food.LoadMoreForRecyclerView;
import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.food.circleimageview.CircleImageView;
import com.lifegofood.jt3282.lifego.food.edit.DownloadImg;
import com.lifegofood.jt3282.lifego.food.userpage.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jt3282 on 2017/12/17.
 */

public class EssayComment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private View rootView ;
    RecyclerView listView;
    private String _code="user",account,user_name,user_img;
    private String UPLOAD_URL = "http://140.121.199.147/getContent.php";
    private String TIME_URL = "http://140.121.199.147/getTime.php";
    private String ONGOOD_URL = "http://140.121.199.147/on_good.php";
    private String COMMENT_URL = "http://140.121.199.147/upload_comment.php";
    private int num;
    private Tools tools=new Tools();
    private MyAdapter itemAdapter;
    private EditText edit_comment;
    private ImageButton button_send;
    private final int EACH_COUNT=10;
    private JSONArray jsonArray;
    private int load = 0;
    ProgressBar loading;
    private SwipeRefreshLayout swiper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        rootView = inflater.inflate(R.layout.essay_comment, container, false);
        _code = getActivity().getIntent().getStringExtra("essay_code");
        edit_comment = (EditText)rootView.findViewById(R.id.ec_comment);
        button_send = (ImageButton)rootView.findViewById(R.id.ec_send);

        loading = (ProgressBar)rootView.findViewById(R.id.ec_progress);
        swiper = (SwipeRefreshLayout) rootView.findViewById(R.id.comment_refresh);
        //为SwipeRefreshLayout设置监听事件
        swiper.setOnRefreshListener(this);
        //为SwipeRefreshLayout设置刷新时的颜色变化，最多可以设置4种
        swiper.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        SharedPreferences preferences = this.getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        account = preferences.getString("USER_NAME", "");
        user_name = preferences.getString("USER","");

        runAPP();
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String comment = edit_comment.getText().toString();
                        if(comment==null||comment.isEmpty()||comment.equals("")){

                        }else{
                            _code = getActivity().getIntent().getStringExtra("essay_code");
                            String check = CheckIsExit(_code);
                            if(comment.contains("'")||comment.contains(";")){
                                Toast.makeText(getContext(), "非法字元", Toast.LENGTH_SHORT).show();
                            } else if(check.equals("no")){
                                String result = DownloadImg.executeQuery(COMMENT_URL,"INSERT INTO essay_comment(account" +
                                        ",essay_code,user_comment,comment_time) VALUE('"+account+"','"+_code+"','"+comment+"','");
                                if(result.contains("ok")){
                                    if(getActivity() == null)
                                        return;
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(rootView.findViewById(R.id.ec_send).getWindowToken(), 0);
                                            edit_comment.setText(null);
                                            Toast.makeText(getContext(), "已送出", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }else Toast.makeText(getContext(), "文章已刪除", Toast.LENGTH_LONG).show();
                        }
                    }
                }).start();
            }
        });

        return rootView;
    }
    private String CheckIsExit(String code){
        String result = DownloadImg.executeQuery(ONGOOD_URL, "SELECT * FROM essay_info WHERE essay_code='" + code + "'");
        if(result.equals("empty")){
            return "yes";
        }else return "no";
    }
    private void runAPP(){
        Thread thread = new Thread() {
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

                new RunTask().execute();
            }
        };
        thread.start();
    }

    @Override
    public void onRefresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                refreshData();
                swiper.setRefreshing(false);
            }
        });
    }
    private void refreshData() {
        if(num!=0)
        listView.setAdapter(null);
        load=0;
        itemAdapter = new MyAdapter(null);
        runAPP();
    }
    private class RunTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loading.setVisibility(View.VISIBLE);
                }
            });
        }
        @Override
        protected String doInBackground(String... params) {
            String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT * FROM essay_comment NATURAL JOIN user_information WHERE essay_code='" + _code + "' " +
                    "ORDER BY comment_time DESC");
            return result;
        }
        @Override
        protected void onPostExecute(final String result) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(getActivity() == null)
                        return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                jsonArray = new JSONArray(result);
                                num = jsonArray.length();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            listView = (RecyclerView) rootView.findViewById(R.id.comment_list);
                            if(num>0){
                                // listView.setOnScrollListener(new PauseOnScrollListener(imageLoader, false, true));
                                final ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
                                itemAdapter = new MyAdapter(list);
                                listView.setLayoutManager(new LinearLayoutManager(getContext()));
                                listView.setAdapter(itemAdapter);
                                loadMoreData(load);
                                load=1;
                                new LoadMoreForRecyclerView(listView, new LoadMoreForRecyclerView.LoadMoreListener() {
                                    @Override
                                    public void loadListener() {
                                        if((10 * load)<num){
                                            loadMoreData(load);
                                            load+=1;
                                        }
                                    }
                                });

                            }else{}
                            loading.setVisibility(View.GONE);
                        }
                    });
                }
            }).start();

        }
    }
    private void loadMoreData(int load){
        int count = itemAdapter.getItemCount();

        if(num>0){

            try {
                for (int i = 0 +(10 * load); i < EACH_COUNT * (load + 1); i++) {
                    if (count+i-(10 * load)<num) {
                        HashMap<String, String> item = new HashMap<String, String>();
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        String account = jsonData.getString("account");
                        String user_name = jsonData.getString("user_name");
                        String icon = jsonData.getString("user_icon");
                        String comment = jsonData.getString("user_comment");
                        String time = jsonData.getString("comment_time");
                        Log.i("comment_time",time);

                        item.put("account",account);
                        item.put("user_name",user_name);
                        item.put("user_icon",icon);
                        item.put("time",time);
                        item.put("comment",comment);
                        itemAdapter.addItem(item);
                    }
                }
            }catch(Exception e) {
                Log.e("log_tag", e.toString());
            }
        }

    }
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();

        public MyAdapter(ArrayList<HashMap<String,String>> newlist) {
            list = newlist;
        }
        public void addItem(HashMap<String, String> item) {
            list.add(item);
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list,parent,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {
            geTime(holder.time,list.get(position).get("time"));
            holder.icon.setTag(list.get(position).get("user_icon"));
            holder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), UserInfo.class);
                    intent.putExtra("account",list.get(position).get("account"));
                    intent.putExtra("user_name",list.get(position).get("user_name"));
                    getActivity().startActivity(intent);
                }
            });
            userIcon(holder.icon,list.get(position).get("user_icon"));
            holder.user_name.setText(list.get(position).get("user_name"));
            holder.comment.setText(list.get(position).get("comment"));
        }
        private void geTime(final TextView tv, final String s){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String get_time = DownloadImg.executeQuery(TIME_URL,s);
                    if(getActivity() == null)
                        return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(get_time);
                        }
                    });
                }
            }).start();
        }
        private void userIcon(CircleImageView icon, String iconURL){

            if(iconURL.equals("user_icon")){
                icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_user));
            }else{
                tools.imageLoading(getContext(),iconURL,icon);
            }
        }
        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView user_name;
            public TextView comment;
            public TextView time;
            public CircleImageView icon;

            public ViewHolder(View itemView) {
                super(itemView);
                user_name = (TextView) itemView.findViewById(R.id.comment_user);
                time = (TextView) itemView.findViewById(R.id.comment_time);
                comment = (TextView) itemView.findViewById(R.id.comment);
                icon = (CircleImageView) itemView.findViewById(R.id.cl_userIcon);
            }
        }

    }
}
