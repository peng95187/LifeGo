package com.lifegofood.jt3282.lifego.food.userpage;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.lifegofood.jt3282.lifego.food.LoadMoreForRecyclerView;
import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.food.circleimageview.CircleImageView;
import com.lifegofood.jt3282.lifego.food.edit.DownloadImg;
import com.lifegofood.jt3282.lifego.food.essay.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jt3282 on 2018/1/3.
 */

public class FanFollowList extends Activity{

    Spinner rankType;
    private Tools tools=new Tools();
    JSONArray jsonArray;
    private int num,num2;
    private String account;
    private MyAdapter fanadapter;
    private MyAdapter2 adapter;
    private int load1 = 0,load2 = 0;
    private ProgressBar loading;
    private RecyclerView user_rank,support_rank;
    String[] Type = {"粉絲" ,"追蹤"};
    private String UPLOAD_URL = "http://140.121.199.147/getContent.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fanfollow);

        loading = (ProgressBar)findViewById(R.id.progress);
        account = getIntent().getStringExtra("account");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(FanFollowList.this, android.R.layout.simple_spinner_dropdown_item, Type);
        rankType = (Spinner)findViewById(R.id.spinner1);
        rankType.setAdapter(adapter);
        rankType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        runAPP();
                        break;
                    case 1:
                        runAPP2();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
    private void runAPP2(){
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

                new RunTask2().execute();

            }
        };
        thread.start();
    }

    private class RunTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loading.setVisibility(View.VISIBLE);
                }
            });
        }
        @Override
        protected String doInBackground(String... params) {
            String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT account,user_name," +
                    "user_icon FROM (SELECT concerner as account FROM user_concern WHERE account='"+account+"') AS fanlist NATURAL JOIN user_information " +
                    "WHERE 1");
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {

            try {
                jsonArray = new JSONArray(result);
                if(jsonArray!=null){
                    num = jsonArray.length();
                }else num = 0;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            user_rank = null;
            user_rank = (RecyclerView) findViewById(R.id.user_rank);
            if(num>0) {
                user_rank.setLayoutManager(new LinearLayoutManager(FanFollowList.this));
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                fanadapter = new MyAdapter(list);
                user_rank.setAdapter(fanadapter);
                loadMoreData(load1);
                load1=1;
                new LoadMoreForRecyclerView(user_rank, new LoadMoreForRecyclerView.LoadMoreListener() {
                    @Override
                    public void loadListener() {
                        if((10 * load1)<num){
                            loadMoreData(load1);
                            load1+=1;
                        }
                    }
                });
            }
            loading.setVisibility(View.GONE);
        }
    }
    private void loadMoreData(int load){
        int count = fanadapter.getItemCount();
        if(num>0){

            try {
                for (int i = 0 +(10 * load); i < 10 * (load + 1); i++) {
                    if (count+i-(10 * load)<num) {
                        HashMap<String, String> item = new HashMap<String, String>();
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        String account = jsonData.getString("account");
                        String user_name = jsonData.getString("user_name");
                        String icon = jsonData.getString("user_icon");

                        item.put("account", account);
                        item.put("user_name", user_name);
                        item.put("user_icon", icon);

                        fanadapter.addItem(item);
                    }
                }
            }catch(Exception e) {
                Log.e("log_tag", e.toString());
            }
        }

    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
        //图片第一次加载的监听器

        public MyAdapter(ArrayList<HashMap<String,String>> newlist) {
            // TODO 自动生成的构造函数存根
            list = newlist;
        }
        public void addItem(HashMap<String, String> item) {
            list.add(item);
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_list,parent,false);
            MyAdapter.ViewHolder viewHolder = new MyAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {
            holder.icon.setTag(list.get(position).get("user_icon"));
            userIcon(holder.icon,list.get(position).get("user_icon"));
            holder.user_name.setText(list.get(position).get("user_name"));
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(FanFollowList.this, UserInfo.class);
                    intent.putExtra("user_name",list.get(position).get("user_name"));
                    intent.putExtra("account",list.get(position).get("account"));
                    startActivity(intent);
                }
            });
        }
        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView user_name;
            public CircleImageView icon;
            public LinearLayout linearLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                user_name = (TextView) itemView.findViewById(R.id.rank_user);
                icon=(CircleImageView)itemView.findViewById(R.id.rank_userIcon);
                linearLayout = (LinearLayout)itemView.findViewById(R.id.rl_ll);
            }
        }
        private void userIcon(CircleImageView icon,String iconURL) {

            if (!iconURL.equals("user_icon")) {
                tools.imageLoading(FanFollowList.this, iconURL, icon);
            } else {
                icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_user));
            }

        }
    }
    private class RunTask2 extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loading.setVisibility(View.VISIBLE);
                }
            });
        }
        @Override
        protected String doInBackground(String... params) {
            String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT account,user_name," +
                    "user_icon FROM user_concern NATURAL JOIN user_information " +
                    "WHERE concerner='"+account+"'");

            return result;
        }

        @Override
        protected void onPostExecute(final String result) {

            try {
                jsonArray = new JSONArray(result);
                num2 = jsonArray.length();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            user_rank = null;
            user_rank = (RecyclerView) findViewById(R.id.user_rank);

            if(num2>0) {
                user_rank.setLayoutManager(new LinearLayoutManager(FanFollowList.this));
                ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
                adapter = new MyAdapter2(list);
                user_rank.setAdapter(adapter);
                loadMoreData2(load2);
                load2=1;
                new LoadMoreForRecyclerView(user_rank, new LoadMoreForRecyclerView.LoadMoreListener() {
                    @Override
                    public void loadListener() {
                        if((10 * load2)<num2){
                            loadMoreData(load2);
                            load2+=1;
                        }
                    }
                });
            }
            loading.setVisibility(View.GONE);
        }
    }
    private void loadMoreData2(int load){

        int count = adapter.getItemCount();
        if(num2>0){

            try {
                for (int i = 0 +(10 * load); i < 10 * (load + 1); i++) {
                    if (count+i-(10 * load)<num2) {

                        HashMap<String, String> item = new HashMap<String, String>();
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        String account = jsonData.getString("account");
                        String user_name = jsonData.getString("user_name");
                        String icon = jsonData.getString("user_icon");

                        item.put("account", account);
                        item.put("user_name", user_name);
                        item.put("user_icon", icon);
                        adapter.addItem(item);
                    }

                }
            }catch(Exception e) {
                Log.e("log_tag", e.toString());
            }
        }

    }


    public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.ViewHolder>{

        ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
        //图片第一次加载的监听器

        public MyAdapter2(ArrayList<HashMap<String,String>> newlist) {
            // TODO 自动生成的构造函数存根
            list = newlist;
        }
        public void addItem(HashMap<String, String> item) {
            list.add(item);
        }

        @Override
        public MyAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.support_rank,parent,false);
            MyAdapter2.ViewHolder viewHolder = new MyAdapter2.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter2.ViewHolder holder, final int position) {
            holder.icon.setTag(list.get(position).get("user_icon"));
            userIcon(holder.icon,list.get(position).get("user_icon"));
            holder.user_name.setText(list.get(position).get("user_name"));
            //holder.time.setText();
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(FanFollowList.this, UserInfo.class);
                    intent.putExtra("account",list.get(position).get("account"));
                    intent.putExtra("user_name",list.get(position).get("user_name"));
                    startActivity(intent);
                }
            });
        }
        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView user_name;
            public CircleImageView icon;
            public LinearLayout linearLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                user_name = (TextView) itemView.findViewById(R.id.sr_user);
                icon=(CircleImageView)itemView.findViewById(R.id.sr_userIcon);
                linearLayout = (LinearLayout)itemView.findViewById(R.id.sr_ll);
            }
        }
        private void userIcon(CircleImageView icon,String iconURL) {

            if (!iconURL.equals("user_icon")) {
                tools.imageLoading(FanFollowList.this, iconURL, icon);
            } else {
                icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_user));
            }

        }
    }
}

