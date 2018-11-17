package com.lifegofood.jt3282.lifego.traffic.ibus;

import android.app.Activity;
import android.content.Intent;
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
import com.lifegofood.jt3282.lifego.traffic.RecyclerItemClickListener;
import com.lifegofood.jt3282.lifego.traffic.TrafficHttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class IBusPrice_Result extends Activity {

    String href = "";
    MyAdapter myAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ibus_price_result);

        String id = getIntent().getStringExtra("id");
        href = "http://ptx.transportdata.tw/MOTC/v2/Bus/RouteFare/InterCity?$filter=RouteID%20eq%20'"+id+"'&$top=300&$format=JSON";
        recyclerView = (RecyclerView) findViewById(R.id.ipr_rv);
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

                if (length != 0) {
                    for (int i = 0; i < length; i++) {
                        JSONArray jArray = jObject.getJSONArray("results").getJSONObject(i).getJSONArray("StageFares");
                        for (int j = 0; j < jArray.length(); j++){
                            HashMap<String, String> item = new HashMap<String, String>();
                            String route = jObject.getJSONArray("results").getJSONObject(i).getString("RouteName");
                            String starts = jArray.getJSONObject(j).getJSONObject("OriginStage").getString("StopName");
                            String finals = jArray.getJSONObject(j).getJSONObject("DestinationStage").getString("StopName");

                            item.put("route",route);
                            item.put("start",starts);
                            item.put("final",finals);
                            myAdapter.addItem(item);
                        }
                    }

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(IBusPrice_Result.this, "查無資訊", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(myAdapter);
                        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(IBusPrice_Result.this, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent();
                                intent.setClass(IBusPrice_Result.this,IBusPrice_Detail.class);
                                intent.putExtra("route",list.get(position).get("route"));
                                intent.putExtra("start",list.get(position).get("start"));
                                intent.putExtra("final",list.get(position).get("final"));
                                startActivity(intent);
                            }
                        }));
                        recyclerView.addItemDecoration(new DividerItemDecoration(IBusPrice_Result.this, DividerItemDecoration.VERTICAL));
                        recyclerView.setLayoutManager(new LinearLayoutManager(IBusPrice_Result.this));
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
            int check = 0;
            for(int i=0;i<getItemCount();i++) {
                if ((item.get("start")+item.get("final")).equals(list.get(i).get("start")+list.get(i).get("final")))check = 1;
            }
            if(check ==0) {
                list.add(item);
            }
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ipr_rv,parent,false);
            MyAdapter.ViewHolder viewHolder = new MyAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {

            holder.id.setText(list.get(position).get("route"));
            holder.stop.setText(list.get(position).get("start")+" - "+list.get(position).get("final"));

        }

        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView stop;
            public TextView id;

            public ViewHolder(View itemView) {
                super(itemView);
                stop = (TextView) itemView.findViewById(R.id.stopname);
                id = (TextView) itemView.findViewById(R.id.stopid);
            }
        }
    }
}
