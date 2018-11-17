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

public class IBusStop_Result extends Activity {

    String href = "";
    MyAdapter myAdapter;
    RecyclerView recyclerView;
    TextView textView,info,update;
    String route,subid;
    boolean islike = false;
    int count = 0;
    ImageButton imbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ibus_stop_result);

        final SharedPreferences.Editor spe = getSharedPreferences("mypref_ibusstop", MODE_PRIVATE).edit();
        final SharedPreferences sp = getSharedPreferences("mypref_ibusstop", MODE_PRIVATE);

        final String enter = getIntent().getStringExtra("stopid");
        final String name = getIntent().getStringExtra("stopname");

        href = "http://ptx.transportdata.tw/MOTC/v2/Bus/EstimatedTimeOfArrival/InterCity?$filter=StopID%20eq%20'"+enter+"'&$top=300&$format=JSON";

        recyclerView = (RecyclerView) findViewById(R.id.isr_rv);
        textView = (TextView) findViewById(R.id.tv_info);
        update = (TextView) findViewById(R.id.update);
        //更新提示
        Calendar mCal = Calendar.getInstance();
        CharSequence s = DateFormat.format("HH:mm", mCal.getTime());
        update.setText(""+s+"更新 (每分鐘)");

        info = (TextView) findViewById(R.id.tv);
        info.setText(name);
        imbtn = (ImageButton)  findViewById(R.id.imbtn);
        new HttpAsyncTask().execute();

        for(int i=0;i<5;i++){
            if(sp.getString("id"+i,"").equals(enter)){
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
                    spe.remove("name"+count);
                    spe.remove("id"+count);
                    spe.commit();
                    imbtn.setBackgroundResource(btn_star_big_off);
                    islike = false;
                }else{
                    for(int i=0;i<5;i++){
                        if(sp.getString("id"+i,"").equals("")){
                            imbtn.setBackgroundResource(btn_star_big_on);
                            spe.putString("id"+i ,enter);
                            spe.putString("name"+i ,name);
                            spe.commit();
                            break;
                        }
                        if(i==4&&(!sp.getString("id"+i,"").equals(""))){
                            Toast.makeText(IBusStop_Result.this, "最愛站牌數上限為5", Toast.LENGTH_SHORT).show();
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
                    new HttpAsyncTask().execute();
                }
            }
        };
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver,filter);
        unregisterReceiver(receiver);
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
                        int total = 0;

                        if (length != 0) {
                            for (int i = 0; i < length; i++) {
                                HashMap<String, String> item = new HashMap<String, String>();
                                if(!jObject.getJSONArray("results").getJSONObject(i).isNull("EstimateTime")){
                                    if(jObject.getJSONArray("results").getJSONObject(i).getInt("EstimateTime")>=0) {
                                        total+=1;
                                        route = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("RouteName").getString("Zh_tw");
                                        subid = jObject.getJSONArray("results").getJSONObject(i).getString("SubRouteID");
                                        String sub = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("SubRouteName").getString("Zh_tw");
                                        int Time = jObject.getJSONArray("results").getJSONObject(i).getInt("EstimateTime");

                                        String result2 = TrafficHttpRequest.getData("http://ptx.transportdata.tw/MOTC/v2/Bus/Route/InterCity/"+route+"?$top=300&$format=JSON");
                                        String headsign = "";

                                        try {
                                            final JSONObject jObject2 = new JSONObject(result2);
                                            int length2 = jObject.getJSONArray("results").length();

                                            if (length2 != 0) {
                                                for(int j = 0;j < jObject2.getJSONArray("results").getJSONObject(0).getJSONArray("SubRoutes").length(); j++){
                                                    if(jObject2.getJSONArray("results").getJSONObject(0).getJSONArray("SubRoutes").getJSONObject(j).getString("SubRouteID").equals(subid)){
                                                        headsign = jObject2.getJSONArray("results").getJSONObject(0).getJSONArray("SubRoutes").getJSONObject(j).getString("Headsign");
                                                    }
                                                }
                                            }
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }

                                        double realtime = (1.0 * Time) / 60.0;

                                        if (realtime <= 2.0) {
                                            item.put("time", "即將到站");
                                        } else if (realtime <= 1.0) {
                                            item.put("time", "進佔中");
                                        } else {
                                            item.put("time", "約 " + String.valueOf(Math.round(realtime)) + " 分鐘");
                                        }
                                        item.put("headsign",headsign);
                                        item.put("route",route);
                                        item.put("sub",sub);
                                        myAdapter.addItem(item);
                                    }
                                }
                            }
                            if(total==0){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setVisibility(View.VISIBLE);
                                    }
                                });
                            }

                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(IBusStop_Result.this, "查無資訊", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                recyclerView.setAdapter(myAdapter);
                                recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(IBusStop_Result.this, new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {

                                    }
                                }));
                                recyclerView.addItemDecoration(new DividerItemDecoration(IBusStop_Result.this, DividerItemDecoration.VERTICAL));
                                recyclerView.setLayoutManager(new LinearLayoutManager(IBusStop_Result.this));
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ipr_rv,parent,false);
            MyAdapter.ViewHolder viewHolder = new MyAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {

            holder.id.setText(list.get(position).get("route")+"("+list.get(position).get("sub")+")\n\n"+list.get(position).get("headsign"));
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
