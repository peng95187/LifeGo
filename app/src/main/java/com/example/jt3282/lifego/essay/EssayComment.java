package com.example.jt3282.lifego.essay;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jt3282.lifego.R;
import com.example.jt3282.lifego.edit.DownloadImg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jt3282 on 2017/12/17.
 */

public class EssayComment extends Fragment {

    private View rootView ;
    ListView listView;
    private String _code,account,user_name;
    private String UPLOAD_URL = "http://lifego777.000webhostapp.com/getContent.php";
    private String TIME_URL = "http://lifego777.000webhostapp.com/getTime.php";
    private String COMMENT_URL = "http://lifego777.000webhostapp.com/upload_comment.php";
    private int num;
    private CommentAdapter adapter;
    private int load = 0;
    private EditText edit_comment;
    private ImageButton button_send;
    private Button loadMoreButton;
    private View mFooterView;
    private final int EACH_COUNT=15;
    private Handler handler = new Handler();
    private boolean isLoading;
    private JSONArray jsonArray;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        rootView = inflater.inflate(R.layout.essay_comment, container, false);

        edit_comment = (EditText)rootView.findViewById(R.id.ec_comment);
        button_send = (ImageButton)rootView.findViewById(R.id.ec_send);

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
                        if(comment==null){

                        }else{
                            _code = getActivity().getIntent().getStringExtra("essay_code");
                            String result = DownloadImg.executeQuery(COMMENT_URL,"INSERT INTO essay_comment(account" +
                                    ",essay_code,user_name,user_comment,comment_time) VALUE('"+account+"','"+_code+"','" +
                                    user_name+"','"+comment+"','");
                            if(result.contains("ok")){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(rootView.findViewById(R.id.ec_send).getWindowToken(), 0);
                                    }
                                });
                            }
                        }
                    }
                }).start();
            }
        });
        mFooterView = getActivity().getLayoutInflater().inflate(R.layout.loadmore, null);

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
    private class RunTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            _code = getActivity().getIntent().getStringExtra("essay_code");
            String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT * FROM essay_comment WHERE essay_code='" + _code + "'");
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {

            try {
                jsonArray = new JSONArray(result);
                num = jsonArray.length();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            listView = (ListView) rootView.findViewById(R.id.comment_list);
            loadMoreButton = (Button) mFooterView.findViewById(R.id.loadMoreButton);

            ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
            adapter = new CommentAdapter(list);
            listView.setAdapter(adapter);
            if(num>0)
            listView.addFooterView(mFooterView);
            loadMoreData(load);

            loadMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isLoading) {
                        //isLoading = true 表示正在加载，加载完毕设置isLoading =false；
                        isLoading = true;
                        //如果服务端还有数据，则继续加载更多，否则隐藏底部的加载更多
                        if (10*(load+1)<=num&&num!=0) {
                            //Log.i("count","count="+totalItemCount);
                            //等待2秒之后才加载，模拟网络等待时间为2s
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    loadMoreData(load);
                                    if(((load+1)*10)<num)
                                        load = load + 1;
                                }
                            },2000);
                        }else{
                            if (listView.getFooterViewsCount()!=0) {
                                listView.removeFooterView(mFooterView);
                            }
                        }
                    }
                }
            });

        }
    }
    private void loadMoreData(int load){
        int count = adapter.getCount();

        if(num>0){
            try {
                for (int i = 0 +(10 * load); i < EACH_COUNT * (load + 1); i++) {
                    if (count+i-(10 * load)<num) {
                        HashMap<String, String> item = new HashMap<String, String>();
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        String user_name = jsonData.getString("user_name");
                        String comment = jsonData.getString("user_comment");
                        String time = jsonData.getString("comment_time");
                        String get_time = DownloadImg.executeQuery(TIME_URL,time);

                        item.put("user_name",user_name);
                        item.put("time",get_time);
                        item.put("comment",comment);
                        adapter.addItem(item);

                    }else{
                        listView.removeFooterView(mFooterView);
                        isLoading = true;
                    }
                }
                adapter.notifyDataSetChanged();
                isLoading = false;
            }catch(Exception e) {
                Log.e("log_tag", e.toString());
            }
        }

    }

    class CommentAdapter extends BaseAdapter {

        ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();

        public CommentAdapter(ArrayList<HashMap<String,String>> newlist){
            list = newlist;
        }

        @Override
        public int getCount() {
            return list==null?0:list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        public void addItem(HashMap<String, String> item) {
            list.add(item);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            public TextView user_name;
            public TextView comment;
            public TextView time;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            //通过convertView来判断是否已经加载过了，如果没有就加载
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.comment_list, parent, false);
                holder = new ViewHolder();
                holder.user_name = (TextView) convertView.findViewById(R.id.comment_user);
                holder.time = (TextView) convertView.findViewById(R.id.comment_time);
                holder.comment = (TextView) convertView.findViewById(R.id.comment);

                convertView.setTag(holder);// 给View添加一个格外的数据
            } else {
                holder = (ViewHolder) convertView.getTag(); // 把数据取出来
            }

            holder.user_name.setText(list.get(position).get("user_name"));
            holder.time.setText(list.get(position).get("time"));
            holder.comment.setText(list.get(position).get("comment"));
            return convertView;
        }
    }
}
