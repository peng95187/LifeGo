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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class IBusSearch_sub extends Activity {

    String href = "";
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ibus_search_sub);
        href = "http://ptx.transportdata.tw/MOTC/v2/Bus/Route/InterCity?$filter=RouteID%20eq%20'" +getIntent().getStringExtra("route")+"'&$top=300&$format=JSON";
        recyclerView = (RecyclerView) findViewById(R.id.is_rv);
        runHttp();
    }
    private void runHttp() {
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
                    int length = jObject.getJSONArray("results").getJSONObject(0).getJSONArray("SubRoutes").length();

                    if (length != 0) {

                        for (int i = 0; i < length; i++) {

                            HashMap<String, String> item = new HashMap<String, String>();
                            String route = jObject.getJSONArray("results").getJSONObject(0).getJSONArray("SubRoutes").getJSONObject(i).getJSONObject("SubRouteName").getString("Zh_tw");
                            String sub = jObject.getJSONArray("results").getJSONObject(0).getJSONArray("SubRoutes").getJSONObject(i).getString("Headsign");
                            String subid = jObject.getJSONArray("results").getJSONObject(0).getJSONArray("SubRoutes").getJSONObject(i).getString("SubRouteID");
                            item.put("sub", sub);
                            item.put("subid", subid);
                            item.put("route", route);

                            myAdapter.addItem(item);
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(IBusSearch_sub.this, "無法取得資訊", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(myAdapter);
                            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(IBusSearch_sub.this, new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Intent intent = new Intent();
                                    intent.setClass(IBusSearch_sub.this, IBusSearch_Detail.class);
                                    intent.putExtra("subid", list.get(position).get("subid"));
                                    intent.putExtra("sub", list.get(position).get("sub"));
                                    intent.putExtra("route", getIntent().getStringExtra("route"));
                                    startActivity(intent);
                                }
                            }));
                            recyclerView.addItemDecoration(new DividerItemDecoration(IBusSearch_sub.this, DividerItemDecoration.VERTICAL));
                            recyclerView.setLayoutManager(new LinearLayoutManager(IBusSearch_sub.this));
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
            list.add(item);
        }
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.is_rv,parent,false);
            MyAdapter.ViewHolder viewHolder = new MyAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {

            holder.routeID.setText(list.get(position).get("route"));
            holder.path.setText(list.get(position).get("sub"));

        }

        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView routeID;
            public TextView path;

            public ViewHolder(View itemView) {
                super(itemView);
                routeID = (TextView) itemView.findViewById(R.id.routeID);
                path = (TextView) itemView.findViewById(R.id.path);
            }
        }
    }
}

