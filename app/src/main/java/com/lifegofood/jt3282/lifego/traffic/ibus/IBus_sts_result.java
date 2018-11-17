package com.lifegofood.jt3282.lifego.traffic.ibus;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.traffic.TrafficHttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class IBus_sts_result extends Activity {

    String href = "";
    RecyclerView recyclerView;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ibus_sts_result);
        recyclerView = (RecyclerView) findViewById(R.id.ibsr_rv);

        String start = Uri.encode(getIntent().getStringExtra("start"));
        String end = Uri.encode(getIntent().getStringExtra("end"));

        href = "http://ptx.transportdata.tw/MOTC/v2/Bus/StopOfRoute/InterCity?" +
                "$filter=Stops%2Fany(d%3A(contains(d%2FStopName%2FZh_tw%20%2C%20%27"+start+"%27)))%20and%20Stops%2Fany(d%3A(contains(d%2FStopName%2FZh_tw%20%2C%20%27"+end+"%27)))&$top=30&$format=JSON";

        new HttpAsyncTask().execute(href);
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls) {
            String result = TrafficHttpRequest.getData(href);
            return result;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try{

                myAdapter = null;
                final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                myAdapter = new MyAdapter(list);
                final JSONObject jObject = new JSONObject(result);
                final int length = jObject.getJSONArray("results").length();

                if (length != 0) {
                    for (int i = 0; i < jObject.getJSONArray("results").length(); i++) {
                        HashMap<String, String> item = new HashMap<String, String>();

                        String route = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("RouteName").getString("Zh_tw");
                        item.put("route",route);
                        String sub_route = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("SubRouteName").getString("Zh_tw");
                        item.put("sub_route",sub_route);
                        myAdapter.addItem(item);
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(IBus_sts_result.this, "查無資訊", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(myAdapter);
                        recyclerView.addItemDecoration(new DividerItemDecoration(IBus_sts_result.this, DividerItemDecoration.VERTICAL));
                        recyclerView.setLayoutManager(new LinearLayoutManager(IBus_sts_result.this));
                    }
                });


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Toast.makeText(IBus_sts_result.this, "無法取得資料", Toast.LENGTH_SHORT).show();
            }
        }
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
                if (item.get("sub_route").equals(list.get(i).get("sub_route")))check = 1;
            }
            if(check ==0) {
                list.add(item);
            }
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ibsr_rv,parent,false);
            MyAdapter.ViewHolder viewHolder = new MyAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {

            holder.route.setText(list.get(position).get("route")+"  子路線 : "+list.get(position).get("sub_route"));

        }

        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView route;

            public ViewHolder(View itemView) {
                super(itemView);
                route = (TextView) itemView.findViewById(R.id.route);
            }
        }
    }
}
