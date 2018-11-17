package com.lifegofood.jt3282.lifego.traffic.bus;

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

public class BusRoute extends Activity {

    String href = "",county = "";
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    EditText search;
    private DividerItemDecoration itemd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stoplist);

        itemd = new DividerItemDecoration(BusRoute.this, DividerItemDecoration.VERTICAL);
        
        county = getIntent().getStringExtra("location") ;
        href = "http://ptx.transportdata.tw/MOTC/v2/Bus/Route/City/"+county+"?$orderby=RouteUID&$top=300&$format=JSON";
        search = (EditText) findViewById(R.id.search);

        recyclerView = (RecyclerView) findViewById(R.id.stoplist);
        runHttp("unChoose");

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.equals("")) {
                    myAdapter = null;
                    recyclerView.removeItemDecoration(itemd);
                    recyclerView.setAdapter(null);
                    runHttp(""+s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
    private void runHttp(final String getID) {
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
                    final JSONObject jObject = new JSONObject(result);
                    final int length = jObject.getJSONArray("results").length();

                    if (length != 0) {
                        for (int i = 0; i < jObject.getJSONArray("results").length(); i++) {
                            HashMap<String, String> item = new HashMap<String, String>();

                            if(!getID.equals("unChoose")&&!getID.equals("")&&getID!=null){
                                String Route = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("RouteName").getString("Zh_tw");
                                if(Route.contains(getID)) {
                                    String Type = jObject.getJSONArray("results").getJSONObject(i).getString("BusRouteType");
                                    String From = "";
                                    if(!county.equals("Taoyuan")) {
                                        From = jObject.getJSONArray("results").getJSONObject(i).getString("DepartureStopNameZh")+"~"+
                                        jObject.getJSONArray("results").getJSONObject(i).getString("DestinationStopNameZh");
                                    }else{
                                        if(!jObject.getJSONArray("results").getJSONObject(i).getJSONArray("SubRoutes").getJSONObject(0).isNull("Headsign")){
                                            From = jObject.getJSONArray("results").getJSONObject(i).getJSONArray("SubRoutes").getJSONObject(0).getString("Headsign");
                                        }
                                    }
                                    if (Type.equals("11")) {
                                        item.put("type", "市區公車");
                                    } else if (Type.equals("12")) {
                                        item.put("type", "公路客運");
                                    } else {
                                        item.put("type", "國道客運");
                                    }
                                    item.put("route", Route);
                                    item.put("from", From);

                                    myAdapter.addItem(item);
                                }
                            }else{
                                String Route = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("RouteName").getString("Zh_tw");
                                String Type = jObject.getJSONArray("results").getJSONObject(i).getString("BusRouteType");
                                String From = "";
                                if(!county.equals("Taoyuan")) {
                                    From = jObject.getJSONArray("results").getJSONObject(i).getString("DepartureStopNameZh")+"~"+
                                            jObject.getJSONArray("results").getJSONObject(i).getString("DestinationStopNameZh");
                                }else{
                                    if(!jObject.getJSONArray("results").getJSONObject(i).getJSONArray("SubRoutes").getJSONObject(0).isNull("Headsign")){
                                        From = jObject.getJSONArray("results").getJSONObject(i).getJSONArray("SubRoutes").getJSONObject(0).getString("Headsign");
                                    }
                                }
                                if (Type.equals("11")) {
                                    item.put("type", "市區公車");
                                } else if (Type.equals("12")) {
                                    item.put("type", "公路客運");
                                } else {
                                    item.put("type", "國道客運");
                                }
                                item.put("route", Route);
                                item.put("from", From);

                                myAdapter.addItem(item);
                            }
                        }
                        if (county.equals("MiaoliCounty")) {
                            for (int i = 0; i < 3; i++) {
                                String sub = "";
                                if (i == 0) sub = "A";
                                else if (i == 1) sub = "B";
                                else sub = "C";
                                HashMap<String, String> item = new HashMap<String, String>();

                                if(!getID.equals("unChoose")&&!getID.equals("")&&getID!=null) {
                                    if (("101" + sub).contains(getID)) {
                                        item.put("route", "101" + sub);
                                        item.put("type", "市區公車");
                                        item.put("from", "竹南科");
                                        item.put("to", "雪霸管理處");
                                        myAdapter.addItem(item);
                                    }
                                }else{
                                    item.put("route", "101" + sub);
                                    item.put("type", "市區公車");
                                    item.put("from", "竹南科");
                                    item.put("to", "雪霸管理處");
                                    myAdapter.addItem(item);
                                }
                            }
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BusRoute.this, "查無資訊", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(myAdapter);
                            recyclerView.addItemDecoration(itemd);
                            recyclerView.setLayoutManager(new LinearLayoutManager(BusRoute.this));
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_stoplist,parent,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {

            holder.stop.setText(list.get(position).get("route"));
            holder.type.setText(list.get(position).get("type"));
            holder.des.setText(list.get(position).get("from"));
            holder.linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    if (county.equals("Taipei") || county.equals("NewTaipei")) {
                        intent.setClass(BusRoute.this, RouteDetail.class);
                    } else intent.setClass(BusRoute.this, RouteList.class);
                    intent.putExtra("location", getIntent().getStringExtra("location"));
                    intent.putExtra("route", list.get(position).get("route"));
                    intent.putExtra("direction", list.get(position).get("from"));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView stop, type, des;
            public LinearLayout linear;

            public ViewHolder(View itemView) {
                super(itemView);
                stop = (TextView) itemView.findViewById(R.id.stop);
                type = (TextView) itemView.findViewById(R.id.type);
                des = (TextView) itemView.findViewById(R.id.fromto);
                linear = (LinearLayout) itemView.findViewById(R.id.ll_rs);
            }
        }
    }
}