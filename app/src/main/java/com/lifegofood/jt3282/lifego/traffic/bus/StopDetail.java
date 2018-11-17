package com.lifegofood.jt3282.lifego.traffic.bus;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.traffic.TrafficHttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static android.R.drawable.btn_star_big_off;
import static android.R.drawable.btn_star_big_on;

public class StopDetail extends Activity {

    String href = "";
    MyAdapter myAdapter;
    RecyclerView recyclerView;
    TextView textView,stopname,update;
    ImageButton imbtn;
    boolean islike = false;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_stop_detail);

        final String county = getIntent().getStringExtra("county");
        final String stopuid = getIntent().getStringExtra("stopuid");
        final String name = getIntent().getStringExtra("stopname");

        href = "http://ptx.transportdata.tw/MOTC/v2/Bus/EstimatedTimeOfArrival/City/"+county+"?$filter=StopUID%20eq%20'"+stopuid+"'&$top=30000&$format=JSON";
        runHttp();
        recyclerView = (RecyclerView) findViewById(R.id.bsd_rv);
        textView = (TextView) findViewById(R.id.tv_info);
        stopname = (TextView) findViewById(R.id.stopname);
        stopname.setText(getIntent().getStringExtra("stopname"));
        update = (TextView) findViewById(R.id.update);

        //更新提示
        Calendar mCal = Calendar.getInstance();
        CharSequence s = DateFormat.format("HH:mm", mCal.getTime());
        update.setText(""+s+"更新 (每分鐘)");

        final SharedPreferences.Editor spe = getSharedPreferences("mypref_busstop", MODE_PRIVATE).edit();
        final SharedPreferences sp = getSharedPreferences("mypref_busstop", MODE_PRIVATE);
        imbtn = (ImageButton) findViewById(R.id.imbtn);

        for(int i=0;i<5;i++){
            if(sp.getString("stop"+i,"").equals(stopuid)){
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
                    spe.remove("stop"+count);
                    spe.remove("county"+count);
                    spe.remove("name"+count);
                    spe.commit();
                    imbtn.setBackgroundResource(btn_star_big_off);
                    islike = false;
                }else{
                    for(int i=0;i<5;i++){
                        if(sp.getString("stop"+i,"").equals("")){
                            imbtn.setBackgroundResource(btn_star_big_on);
                            spe.putString("stop"+i ,stopuid);
                            spe.putString("county"+i ,county);
                            spe.putString("name"+i ,name);
                            spe.commit();
                            break;
                        }
                        if(i==4&&(!sp.getString("stop"+i,"").equals(""))){
                            Toast.makeText(StopDetail.this, "最愛站牌數上限為5", Toast.LENGTH_SHORT).show();
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
                    int total = 0;

                    if (length != 0) {
                        for (int i = 0; i < length; i++) {
                            HashMap<String, String> item = new HashMap<String, String>();

                            if(!jObject.getJSONArray("results").getJSONObject(i).isNull("EstimateTime")){
                                total += 1;
                                if(jObject.getJSONArray("results").getJSONObject(i).getInt("EstimateTime")>=0) {
                                    String route = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("RouteName").getString("Zh_tw");
                                    int Time = jObject.getJSONArray("results").getJSONObject(i).getInt("EstimateTime");
                                    double realtime = (1.0 * Time) / 60.0;

                                    if (realtime <= 2.0) {
                                        item.put("time","即將到站");
                                    } else if (realtime <= 1.0) {
                                        item.put("time","進佔中");
                                    } else {
                                        item.put("time","約 " + String.valueOf(Math.round(realtime)) + " 分鐘");
                                    }

                                    item.put("route", route);
                                    myAdapter.addItem(item);
                                }
                            }
                        }

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StopDetail.this, "查無資訊", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    if(total==0){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(myAdapter);
                            // recyclerView.addOnItemTouchListener(r);
                            recyclerView.addItemDecoration(new DividerItemDecoration(StopDetail.this, DividerItemDecoration.VERTICAL));
                            recyclerView.setLayoutManager(new LinearLayoutManager(StopDetail.this));
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.busstop_rv,parent,false);
            MyAdapter.ViewHolder viewHolder = new MyAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {

            holder.id.setText(list.get(position).get("route"));
            holder.stop.setText(list.get(position).get("time"));

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
