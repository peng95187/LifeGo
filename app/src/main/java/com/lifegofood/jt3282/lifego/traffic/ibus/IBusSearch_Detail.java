package com.lifegofood.jt3282.lifego.traffic.ibus;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.traffic.RecyclerItemClickListener;
import com.lifegofood.jt3282.lifego.traffic.TrafficHttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static android.R.drawable.btn_star_big_off;
import static android.R.drawable.btn_star_big_on;

public class IBusSearch_Detail extends Activity {

    String href = "";
    MyAdapter myAdapter;
    RecyclerView recyclerView;
    ImageButton imbtn;
    TextView tv,update;
    boolean islike = false;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ibus_search_detail);

        final SharedPreferences.Editor spe = getSharedPreferences("mypref_ibusid", MODE_PRIVATE).edit();
        final SharedPreferences sp = getSharedPreferences("mypref_ibusid", MODE_PRIVATE);

        final String route = getIntent().getStringExtra("route");
        final String subid = getIntent().getStringExtra("subid");
        final String sub = getIntent().getStringExtra("sub");

        href = "http://ptx.transportdata.tw/MOTC/v2/Bus/EstimatedTimeOfArrival/InterCity/"+route+"?$filter=SubRouteID%20eq%20'"+subid+"'&$orderby=StopSequence&$top=300&$format=JSON";
        runHttp();

        recyclerView = (RecyclerView) findViewById(R.id.ibsd_rv);
        tv = (TextView) findViewById(R.id.tv);
        tv.setText(sub);
        update = (TextView) findViewById(R.id.update);
        //更新提示
        Calendar mCal = Calendar.getInstance();
        CharSequence s = DateFormat.format("HH:mm", mCal.getTime());
        update.setText(""+s+"更新 (每分鐘)");
        imbtn = (ImageButton) findViewById(R.id.imbtn);

        for(int i=0;i<5;i++){
            if(sp.getString("id"+i,"").equals(subid)){
                imbtn.setBackgroundResource(btn_star_big_on);
                islike = true;
                count = i;
                break;
            }
        }
        imbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(islike == true){
                    spe.remove("route"+count);
                    spe.remove("id"+count);
                    spe.remove("direction"+count);
                    spe.commit();
                    imbtn.setBackgroundResource(btn_star_big_off);
                    islike = false;
                }else{
                    for(int i=0;i<5;i++){
                        if(sp.getString("id"+i,"").equals("")){
                            imbtn.setBackgroundResource(btn_star_big_on);
                            spe.putString("id"+i ,subid);
                            spe.putString("route"+i ,route);
                            spe.putString("direction"+i ,sub);
                            spe.commit();
                            break;
                        }
                        if(i==4&&(!sp.getString("id"+i,"").equals(""))){
                            Toast.makeText(IBusSearch_Detail.this, "最愛路線數上限為5", Toast.LENGTH_SHORT).show();
                        }
                    }
                    islike = true;
                }
            }
        });
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(Intent.ACTION_TIME_TICK)) {
                    Calendar mCal2 = Calendar.getInstance();
                    CharSequence s2 = DateFormat.format("HH:mm", mCal2.getTime());
                    update.setText(""+s2+"更新 (每分鐘)");
                    runHttp();
                }
            }
        };
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver,filter);
        unregisterReceiver(receiver);
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
                    int length = jObject.getJSONArray("results").length();

                    if (length != 0) {
                        for (int i = 0; i < length; i++) {
                            HashMap<String, String> item = new HashMap<String, String>();
                            String stopname = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("StopName").getString("Zh_tw");
                            Log.i("stopname", String.valueOf(stopname));
                            String stopid = jObject.getJSONArray("results").getJSONObject(i).getString("StopID");
                            if(!jObject.getJSONArray("results").getJSONObject(i).isNull("EstimateTime")) {
                                if (jObject.getJSONArray("results").getJSONObject(i).getInt("EstimateTime") >= 0) {
                                    int Time = jObject.getJSONArray("results").getJSONObject(i).getInt("EstimateTime");
                                    double realtime = (1.0 * Time) / 60.0;

                                    if (jObject.getJSONArray("results").getJSONObject(i).getInt("EstimateTime") >= 0) {
                                        if (realtime <= 2.0) {
                                            item.put("time", "即將到站");
                                        } else if (realtime <= 1.0) {
                                            item.put("time", "進佔中");
                                        } else {
                                            item.put("time", "約 " + String.valueOf(Math.round(realtime)) + " 分鐘");
                                        }
                                    }
                                } else {
                                    item.put("time", "未發車");
                                }
                            }else {
                                item.put("time", "未發車");
                            }
                            item.put("stopid",stopid);
                            item.put("stopname", stopname);
                            myAdapter.addItem(item);
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(IBusSearch_Detail.this, "無法取得資訊", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(myAdapter);
                            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(IBusSearch_Detail.this, new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {

                                }
                            }));
                            recyclerView.addItemDecoration(new DividerItemDecoration(IBusSearch_Detail.this, DividerItemDecoration.VERTICAL));
                            recyclerView.setLayoutManager(new LinearLayoutManager(IBusSearch_Detail.this));
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
            holder.routeID.setText(list.get(position).get("stopid")+"    "+list.get(position).get("stopname"));
            holder.path.setText(list.get(position).get("time"));
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

