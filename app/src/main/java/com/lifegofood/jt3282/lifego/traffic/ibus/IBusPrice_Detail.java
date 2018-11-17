package com.lifegofood.jt3282.lifego.traffic.ibus;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.traffic.TrafficHttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class IBusPrice_Detail extends Activity {

    MyAdapter myAdapter;
    RecyclerView recyclerView;
    String href = "",starts = "",finals = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ibus_price_detail);

        String id = getIntent().getStringExtra("route");
        starts = getIntent().getStringExtra("start");
        finals = getIntent().getStringExtra("final");
        href = "http://ptx.transportdata.tw/MOTC/v2/Bus/RouteFare/InterCity?$filter=RouteID%20eq%20'"+id+"'&$top=300&$format=JSON";

        recyclerView = (RecyclerView) findViewById(R.id.ipd_rv);
        new HttpAsyncTask().execute();
    }
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
                int check = 0;

                if (length != 0) {
                    for (int i = 0; i < length && check != 1; i++) {
                        JSONArray jArray = jObject.getJSONArray("results").getJSONObject(i).getJSONArray("StageFares");
                        for (int j = 0; j < jArray.length(); j++){
                            if(jArray.getJSONObject(j).getJSONObject("OriginStage").getString("StopName").equals(starts)
                                    &&jArray.getJSONObject(j).getJSONObject("DestinationStage").getString("StopName").equals(finals)) {
                                JSONArray jTypeArray = jArray.getJSONObject(j).getJSONArray("Fares");

                                for (int a = 0; a < jTypeArray.length(); a++) {
                                    HashMap<String, String> item = new HashMap<String, String>();

                                    if(jTypeArray.getJSONObject(a).getInt("Price")>=0) {
                                        String type = jTypeArray.getJSONObject(a).getString("FareName");
                                        String price = jTypeArray.getJSONObject(a).getString("Price");
                                        item.put("type", type);
                                        item.put("price", price);
                                        myAdapter.addItem(item);
                                    }
                                }
                                check = 1;
                                break;
                            }
                        }
                    }

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(IBusPrice_Detail.this, "查無資訊", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(myAdapter);

                        recyclerView.addItemDecoration(new DividerItemDecoration(IBusPrice_Detail.this, DividerItemDecoration.VERTICAL));
                        recyclerView.setLayoutManager(new LinearLayoutManager(IBusPrice_Detail.this));
                    }
                });

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
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
            list.add(item);
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ipd_rv,parent,false);
            MyAdapter.ViewHolder viewHolder = new MyAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final MyAdapter.ViewHolder holder, final int position) {
            holder.type.setText(list.get(position).get("type"));
            holder.price.setText(list.get(position).get("price")+" 元");
            holder.ll1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.ll2.getVisibility()==View.GONE){
                        holder.ll2.setVisibility(View.VISIBLE);
                    }else {
                        holder.ll2.setVisibility(View.GONE);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView type,price;
            public LinearLayout ll1,ll2;

            public ViewHolder(View itemView) {
                super(itemView);
                type = (TextView) itemView.findViewById(R.id.type);
                price = (TextView) itemView.findViewById(R.id.price);
                ll1 = (LinearLayout) itemView.findViewById(R.id.ll1);
                ll2 = (LinearLayout) itemView.findViewById(R.id.ll2);
            }
        }
    }
}

