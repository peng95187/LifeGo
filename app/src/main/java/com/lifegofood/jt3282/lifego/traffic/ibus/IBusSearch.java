package com.lifegofood.jt3282.lifego.traffic.ibus;

import android.app.Activity;
import android.content.Intent;
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

public class IBusSearch extends Activity {

    String href = "";
    MyAdapter myAdapter;
    RecyclerView recyclerView;
    EditText search;
    private DividerItemDecoration itemd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ibus_search);

        itemd = new DividerItemDecoration(IBusSearch.this, DividerItemDecoration.VERTICAL);

        href = "http://ptx.transportdata.tw/MOTC/v2/Bus/Route/InterCity?$top=300&$format=JSON";
        Toast.makeText(this, "目前300筆,請輸入縮小範圍", Toast.LENGTH_SHORT).show();
        recyclerView = (RecyclerView) findViewById(R.id.is_rv);
        runHttp(href);

        search = (EditText) findViewById(R.id.search);

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
                String f = "";
                if(search.getText().toString()!=null||!search.getText().toString().equals("")){
                    f = "$filter=contains(RouteID,'"+search.getText().toString()+"')&";
                    runHttp("http://ptx.transportdata.tw/MOTC/v2/Bus/Route/InterCity?"+f+"$top=300&$format=JSON");

                }else{
                    f = "";
                    runHttp("http://ptx.transportdata.tw/MOTC/v2/Bus/Route/InterCity?"+f+"$top=300&$format=JSON");
                }
            }
        });
    }
    private void runHttp(final String httphref) {
        class HttpAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... urls) {
                String result = TrafficHttpRequest.getData(httphref);
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
                            String route = jObject.getJSONArray("results").getJSONObject(i).getString("RouteID");
                            boolean hassub = jObject.getJSONArray("results").getJSONObject(i).getBoolean("HasSubRoutes");
                            String starts = jObject.getJSONArray("results").getJSONObject(i).getString("DepartureStopNameZh");
                            String finals = jObject.getJSONArray("results").getJSONObject(i).getString("DestinationStopNameZh");

                            if(hassub==true) {
                                item.put("hassub", "true");
                            }else{
                                item.put("hassub", "false");
                            }
                            item.put("route", route);
                            item.put("start", starts);
                            item.put("final", finals);
                            myAdapter.addItem(item);
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(IBusSearch.this, "查無資訊", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(myAdapter);
                            recyclerView.addItemDecoration(itemd);
                            recyclerView.setLayoutManager(new LinearLayoutManager(IBusSearch.this));
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
            holder.path.setText(list.get(position).get("start")+" - "+list.get(position).get("final"));
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    if(list.get(position).get("hassub").equals("true")) {
                        intent.setClass(IBusSearch.this, IBusSearch_sub.class);
                        intent.putExtra("route", list.get(position).get("route"));
                        startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView routeID;
            public TextView path;
            public LinearLayout ll;

            public ViewHolder(View itemView) {
                super(itemView);
                routeID = (TextView) itemView.findViewById(R.id.routeID);
                path = (TextView) itemView.findViewById(R.id.path);
                ll = (LinearLayout) itemView.findViewById(R.id.ll);
            }
        }
    }
}

