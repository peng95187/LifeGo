package com.lifegofood.jt3282.lifego.traffic.ibus;

import android.app.Activity;
import android.content.Intent;
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
import com.lifegofood.jt3282.lifego.traffic.RecyclerItemClickListener;
import com.lifegofood.jt3282.lifego.traffic.TrafficHttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class IBusStop_list extends Activity {

    String href = "";
    RecyclerView recyclerView;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ibus_stop_list);
        String type = getIntent().getStringExtra("type");
        String enter = getIntent().getStringExtra("enter");
        if(type.equals("code")){

            href = "http://ptx.transportdata.tw/MOTC/v2/Bus/Stop/InterCity?$filter=contains(StopID%2C'"+enter+"')&$top=30&$format=JSON";

        }else if(type.equals("stop")){
            href = "http://ptx.transportdata.tw/MOTC/v2/Bus/Stop/InterCity?$filter=contains(StopName%2FZh_tw%2C'"+Uri.encode(enter)+"')&$top=30&$format=JSON";

        }
        recyclerView = (RecyclerView) findViewById(R.id.ibsl_rv);
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

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        myAdapter = null;
                        final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                        myAdapter = new MyAdapter(list);

                        JSONObject jObject = new JSONObject(result);
                        int length = jObject.getJSONArray("results").length();

                        if (length != 0) {
                            for (int i = 0; i < length; i++) {
                                HashMap<String, String> item = new HashMap<String, String>();
                                String id = jObject.getJSONArray("results").getJSONObject(i).getString("StopID");
                                String stop = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("StopName").getString("Zh_tw");
                                item.put("id",id);
                                item.put("stop",stop);
                                myAdapter.addItem(item);
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(IBusStop_list.this, "查無資訊", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setAdapter(myAdapter);
                                recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(IBusStop_list.this, new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Intent intent = new Intent(IBusStop_list.this, IBusStop_Result.class);
                                        intent.putExtra("stopid",list.get(position).get("id"));
                                        intent.putExtra("stopname",list.get(position).get("stop"));
                                        startActivity(intent);
                                    }
                                }));
                                recyclerView.addItemDecoration(new DividerItemDecoration(IBusStop_list.this, DividerItemDecoration.VERTICAL));
                                recyclerView.setLayoutManager(new LinearLayoutManager(IBusStop_list.this));
                            }
                        });

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }).start();
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ibsl_rv,parent,false);
            MyAdapter.ViewHolder viewHolder = new MyAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {

            holder.id.setText(list.get(position).get("id"));
            holder.stop.setText(list.get(position).get("stop"));

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
