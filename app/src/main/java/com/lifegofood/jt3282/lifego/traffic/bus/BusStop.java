package com.lifegofood.jt3282.lifego.traffic.bus;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.traffic.TrafficHttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BusStop extends Activity {

    MyAdapter myAdapter;
    RecyclerView recyclerView;
    EditText search;
    private DividerItemDecoration itemd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_stop);
        
        itemd = new DividerItemDecoration(BusStop.this, DividerItemDecoration.VERTICAL);
        
        final String id = getIntent().getStringExtra("location");
        recyclerView = (RecyclerView) findViewById(R.id.bs_rv);
        search = (EditText) findViewById(R.id.search);

        Toast.makeText(BusStop.this, "目前僅顯示300筆內,請輸入縮小搜尋範圍", Toast.LENGTH_SHORT).show();
        runHttp("http://ptx.transportdata.tw/MOTC/v2/Bus/EstimatedTimeOfArrival/City/"+id+"?$orderby=StopUID&$filter=contains(StopName%2FZh_tw%2C'')&$top=300&$format=JSON");

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                myAdapter = null;
                recyclerView.removeItemDecoration(itemd);
                recyclerView.setAdapter(null);
                runHttp("http://ptx.transportdata.tw/MOTC/v2/Bus/EstimatedTimeOfArrival/City/"+id+"?$orderby=StopUID&$filter=contains(StopName%2FZh_tw%2C'"+ Uri.encode(String.valueOf(s))+"')&$top=1000&$format=JSON");

            }
        });

    }

    private void runHttp(final String href) {
        class HttpAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... urls) {
                String result = TrafficHttpRequest.getData(href);
                return result;
            }

            // onPostExecute displays the results of the AsyncTask.
            @Override
            protected void onPostExecute(final String result) {

                try {
                    myAdapter = null;
                    final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                    myAdapter = new MyAdapter(list);
                    JSONObject jObject = new JSONObject(result);
                    int length = jObject.getJSONArray("results").length();

                    if (length != 0) {
                        for (int i = 0; i < length; i++) {
                            HashMap<String, String> item = new HashMap<String, String>();

                            String stops = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("StopName").getString("Zh_tw");
                            String stopID = jObject.getJSONArray("results").getJSONObject(i).getString("StopID");
                            String stopUID = jObject.getJSONArray("results").getJSONObject(i).getString("StopUID");

                            item.put("stopUID",stopUID);
                            item.put("stops", stops);
                            item.put("stopID", stopID);
                            myAdapter.addItem(item);

                        }

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BusStop.this, "查無資訊", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(myAdapter);
                            // recyclerView.addOnItemTouchListener(r);
                            recyclerView.addItemDecoration(itemd);
                            recyclerView.setLayoutManager(new LinearLayoutManager(BusStop.this));
                        }
                    });

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        new HttpAsyncTask().execute();
    }
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();

        public MyAdapter(ArrayList<HashMap<String,String>> newlist) {
            // TODO 自动生成的构造函数存根
            list = newlist;
        }
        public void addItem(HashMap<String, String> item) {

            int check = 0;
            for(int i=0;i<getItemCount();i++) {
                if (item.get("stopUID").equals(list.get(i).get("stopUID")))check = 1;
            }
            if(check ==0) {
                list.add(item);
            }
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.busstop_rv,parent,false);
            MyAdapter.ViewHolder viewHolder = new MyAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {

            holder.stop.setText(list.get(position).get("stops"));
            holder.id.setText(list.get(position).get("stopID"));
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(BusStop.this,StopDetail.class);
                    intent.putExtra("county",getIntent().getStringExtra("location"));
                    intent.putExtra("stopuid",list.get(position).get("stopUID"));
                    intent.putExtra("stopname",list.get(position).get("stops"));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView stop;
            public TextView id;
            public LinearLayout ll;

            public ViewHolder(View itemView) {
                super(itemView);
                stop = (TextView) itemView.findViewById(R.id.stopname);
                id = (TextView) itemView.findViewById(R.id.stopid);
                ll = (LinearLayout) itemView.findViewById(R.id.llbs_rv);
            }
        }
    }
}

