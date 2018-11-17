package com.lifegofood.jt3282.lifego.food.frame;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.food.circleimageview.CircleImageView;
import com.lifegofood.jt3282.lifego.food.edit.DownloadImg;
import com.lifegofood.jt3282.lifego.food.essay.Search;
import com.lifegofood.jt3282.lifego.food.essay.Tools;
import com.lifegofood.jt3282.lifego.food.userpage.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jt3282 on 2017/12/1.
 */

public class ThirdFragment extends Fragment {

    View rootView;
    Spinner rankType;
    private Tools tools=new Tools();
    JSONArray jsonArray;
    private int num,num2;
    private ImageButton th_send;
    private EditText th_editText;
    private MyAdapter fanadapter;
    private MyAdapter2 adapter;
    private RecyclerView user_rank,support_rank;
    String[] Type = {"人氣排行","公信排行(支持總數至少10)" };
    private String UPLOAD_URL = "http://140.121.199.147/getContent.php";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        rootView = inflater.inflate(R.layout.third_frame, container, false);

        th_editText = (EditText)rootView.findViewById(R.id.th_editText);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Type);
        rankType = (Spinner)rootView.findViewById(R.id.spinner1);
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

        th_send = (ImageButton)rootView.findViewById(R.id.th_send);
        th_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = th_editText.getText().toString();
                if(search.contains("'")||search.contains(";"))
                    Toast.makeText(getContext(), "非法字元", Toast.LENGTH_SHORT).show();
                else if(search.length()>20) {
                    Toast.makeText(getContext(), "字串太長了喔 20字元", Toast.LENGTH_SHORT).show();
                }else if(search!=null){
                    Intent intent = new Intent();
                    String s = th_editText.getText().toString();
                    intent.setClass(getContext(),Search.class);
                    intent.putExtra("search",s);
                    startActivity(intent);
                }
            }
        });
        return rootView;
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
        protected String doInBackground(String... params) {
            String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT account,user_name," +
                    "user_icon,count(concerner) AS num FROM user_concern NATURAL JOIN user_information " +
                    "GROUP BY account ORDER BY count(concerner) DESC");
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
            user_rank = (RecyclerView) rootView.findViewById(R.id.user_rank);
            user_rank.setLayoutManager(new LinearLayoutManager(getContext()));
            ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
            fanadapter = new MyAdapter(list);
            user_rank.setAdapter(fanadapter);

            loadMoreData();


        }
    }
    private void loadMoreData(){

        if(num>0){

            try {
                for (int i = 0 ; i < 15 ; i++) {
                        HashMap<String, String> item = new HashMap<String, String>();
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        String account = jsonData.getString("account");
                        String user_name = jsonData.getString("user_name");
                        String icon = jsonData.getString("user_icon");
                        String num = jsonData.getString("num");
                        item.put("account",account);
                        item.put("user_name",user_name);
                        item.put("user_icon",icon);
                        item.put("fan_num",num);
                        fanadapter.addItem(item);

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
            holder.fan_num.setText("紛絲:"+list.get(position).get("fan_num"));
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), UserInfo.class);
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
            public TextView fan_num;
            public CircleImageView icon;
            public LinearLayout linearLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                user_name = (TextView) itemView.findViewById(R.id.rank_user);
                fan_num = (TextView) itemView.findViewById(R.id.rank_fan);
                icon=(CircleImageView)itemView.findViewById(R.id.rank_userIcon);
                linearLayout = (LinearLayout)itemView.findViewById(R.id.rl_ll);
            }
        }
        private void userIcon(CircleImageView icon,String iconURL) {

            if (!iconURL.equals("user_icon")) {
                tools.imageLoading(getContext(), iconURL, icon);
            } else {
                icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_user));
            }

        }
    }
    private class RunTask2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT account,user_name,user_icon,(supportnum/count_support) as spnum " +
                    "FROM (SELECT essay_code,count(support) as count_support FROM on_good WHERE" +
                    " support='1' or support='-1' GROUP BY essay_code) as total_num NATURAL JOIN " +
                    "(SELECT essay_code,count(support) as supportnum FROM on_good WHERE" +
                    " support='1' GROUP BY essay_code) as total_save NATURAL JOIN upload_img NATURAL JOIN user_information WHERE supportnum>=10"+
                    " ORDER BY (supportnum/count_support) DESC");

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
            user_rank = (RecyclerView) rootView.findViewById(R.id.user_rank);
            user_rank.setLayoutManager(new LinearLayoutManager(getContext()));

            ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
            adapter = new MyAdapter2(list);
            user_rank.setAdapter(adapter);
            loadMoreData2();
        }
    }
    private void loadMoreData2(){

        if(num2>0){

            try {
                for (int i = 0 ; i < 15; i++) {

                    HashMap<String, String> item = new HashMap<String, String>();
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    String account = jsonData.getString("account");
                    String user_name = jsonData.getString("user_name");
                    String icon = jsonData.getString("user_icon");
                    Double spnum = jsonData.getDouble("spnum");
                    int rate = (int) (spnum * 100);
                    item.put("account", account);
                    item.put("user_name", user_name);
                    item.put("user_icon", icon);
                    item.put("spnum", String.valueOf(rate));
                    adapter.addItem(item);

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
            holder.suport_rate.setText(list.get(position).get("spnum"));
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), UserInfo.class);
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
            public TextView suport_rate;
            public CircleImageView icon;
            public LinearLayout linearLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                user_name = (TextView) itemView.findViewById(R.id.sr_user);
                suport_rate = (TextView) itemView.findViewById(R.id.sr_rate);
                icon=(CircleImageView)itemView.findViewById(R.id.sr_userIcon);
                linearLayout = (LinearLayout)itemView.findViewById(R.id.sr_ll);
            }
        }
        private void userIcon(CircleImageView icon,String iconURL) {

            if (!iconURL.equals("user_icon")) {
                tools.imageLoading(getContext(), iconURL, icon);
            } else {
                icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_user));
            }

        }
    }

}

