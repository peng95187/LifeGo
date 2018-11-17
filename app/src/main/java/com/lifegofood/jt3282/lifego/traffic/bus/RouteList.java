package com.lifegofood.jt3282.lifego.traffic.bus;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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

public class RouteList extends Activity {

    RecyclerView recyclerView;
    String href = "",hrefecode = "",route = "",sub = "",county = "",direction_ = "";
    MyAdapter myAdapter;
    RadioButton radioButton1,radioButton2;
    TextView update;
    ImageButton imbtn;
    boolean islike = false;
    int count = 0,check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stop_list);

        sub = getIntent().getStringExtra("route");
        county = getIntent().getStringExtra("location");
        direction_ = getIntent().getStringExtra("direction");

        if(county.equals("MiaoliCounty"))route="101";
        else route = getIntent().getStringExtra("route");

        update = (TextView) findViewById(R.id.update);

        Calendar mCal = Calendar.getInstance();
        CharSequence s = DateFormat.format("HH:mm", mCal.getTime());
        update.setText(""+s+"更新 (每分鐘)");

        hrefecode = Uri.encode(route);

        href = "http://ptx.transportdata.tw/MOTC/v2/Bus/EstimatedTimeOfArrival/City/"+county+"/"+hrefecode+"?$orderby=StopSequence&$top=300&$format=JSON";

        radioButton1 = (RadioButton) findViewById(R.id.radio1) ;
        radioButton2 = (RadioButton) findViewById(R.id.radio2) ;

        if(radioButton1.isChecked())excuteHttp("0");

        radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    recyclerView.setAdapter(null);
                    excuteHttp("0");
                }
            }
        });
        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    recyclerView.setAdapter(null);
                    excuteHttp("1");
                }
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.list);

        final SharedPreferences.Editor spe = getSharedPreferences("mypref_busid", MODE_PRIVATE).edit();
        final SharedPreferences sp = getSharedPreferences("mypref_busid", MODE_PRIVATE);

        imbtn = (ImageButton) findViewById(R.id.imbtn);

        for(int i=0;i<5;i++){
            if(sp.getString("id"+i,"").equals(route)){
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
                    spe.remove("id"+count);
                    spe.remove("county"+count);
                    spe.remove("direction"+count);
                    spe.commit();
                    imbtn.setBackgroundResource(btn_star_big_off);
                    islike = false;
                }else{
                    for(int i=0;i<5;i++){
                        if(sp.getString("id"+i,"").equals("")){
                            imbtn.setBackgroundResource(btn_star_big_on);
                            spe.putString("id"+i ,route);
                            spe.putString("county"+i ,county);
                            spe.putString("direction"+i ,direction_);
                            spe.commit();
                            break;
                        }
                        if(i==4&&(!sp.getString("id"+i,"").equals(""))){
                            Toast.makeText(RouteList.this, "最愛站牌數上限為5", Toast.LENGTH_SHORT).show();
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
                    Calendar mCal = Calendar.getInstance();
                    CharSequence s = DateFormat.format("HH:mm", mCal.getTime());

                    update.setText(""+s+"更新 (每分鐘更新)");
                    if(check==0){
                        excuteHttp("0");
                    }else{
                        excuteHttp("1");
                    }
                }
            }
        };
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver,filter);
        unregisterReceiver(receiver);

    }
    private void excuteHttp(final String direction){
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
                    final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                    myAdapter = new MyAdapter(list);

                    JSONObject jObject = new JSONObject(result);
                    int length = jObject.getJSONArray("results").length();

                    if(length!=0){

                        if(county.equals("MiaoliCounty")){

                            for(int i = 0;i < jObject.getJSONArray("results").length();i++){
                                HashMap<String, String> item = new HashMap<String, String>();
                                if(jObject.getJSONArray("results").getJSONObject(i).getString("Direction").equals(direction)
                                        &&jObject.getJSONArray("results").getJSONObject(i).getJSONObject("SubRouteName").getString("Zh_tw").equals(sub)){
                                    String Route = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("StopName").getString("Zh_tw");

                                    if(!jObject.getJSONArray("results").getJSONObject(i).isNull("EstimateTime")){
                                        int Time = jObject.getJSONArray("results").getJSONObject(i).getInt("EstimateTime");
                                        double realtime = (1.0 * Time) / 60.0;

                                        if (jObject.getJSONArray("results").getJSONObject(i).getInt("EstimateTime") >= 0) {
                                            item.put("route",Route);
                                            if (realtime <= 1.0) {
                                                item.put("time","即將到站");
                                            } else{
                                                item.put("time","約 " + String.valueOf(Math.round(realtime)) + " 分鐘");
                                            }
                                            myAdapter.addItem(item);
                                        }
                                    }else{
                                        item.put("route",Route);
                                        item.put("time","未發車");
                                        myAdapter.addItem(item);
                                    }
                                }
                            }
                        }else {
                            for (int i = 0; i < jObject.getJSONArray("results").length(); i++) {
                                HashMap<String, String> item = new HashMap<String, String>();
                                if (jObject.getJSONArray("results").getJSONObject(i).getString("Direction").equals(direction)
                                        &&jObject.getJSONArray("results").getJSONObject(i).getJSONObject("SubRouteName").getString("Zh_tw").equals(sub)) {
                                    String Route = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("StopName").getString("Zh_tw");
                                    String UID = jObject.getJSONArray("results").getJSONObject(i).getString("StopUID");

                                    if (!jObject.getJSONArray("results").getJSONObject(i).isNull("EstimateTime")) {
                                        int Time = jObject.getJSONArray("results").getJSONObject(i).getInt("EstimateTime");
                                        double realtime = (1.0 * Time) / 60.0;

                                        if (jObject.getJSONArray("results").getJSONObject(i).getInt("EstimateTime") >= 0) {
                                            item.put("route", Route);
                                            item.put("stopuid", UID);
                                            if (realtime <= 2.0) {
                                                item.put("time", "即將到站");
                                            }else if(realtime <= 1.0) {
                                                item.put("time", "進佔中");
                                            } else {
                                                item.put("time", "約 " + String.valueOf(Math.round(realtime)) + " 分鐘");
                                            }
                                            myAdapter.addItem(item);
                                        }
                                    } else {
                                        item.put("route", Route);
                                        item.put("stopuid", UID);
                                        item.put("time", "未發車");
                                        myAdapter.addItem(item);
                                    }
                                }
                            }
                        }
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RouteList.this, "查無資訊", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(myAdapter.getItemCount()==0){
                                String d = "";
                                if(direction.equals("0")){
                                    d = "去程";
                                }else d = "返程";
                                Toast.makeText(RouteList.this, "查無 "+ d+" 資訊", Toast.LENGTH_SHORT).show();
                            }else {
                                recyclerView.setAdapter(myAdapter);
                                recyclerView.addItemDecoration(new DividerItemDecoration(RouteList.this, DividerItemDecoration.VERTICAL));
                                recyclerView.setLayoutManager(new LinearLayoutManager(RouteList.this));
                            }
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
            int check = 0;
            for(int i=0;i<getItemCount();i++) {
                if (item.get("route").equals(list.get(i).get("route")))check = 1;
            }
            if(check ==0) {
                list.add(item);
            }else Log.i("dup",item.get("route"));
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stop_status,parent,false);
            MyAdapter.ViewHolder viewHolder = new MyAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {
            holder.stop.setText(list.get(position).get("route"));
            holder.status.setText(list.get(position).get("time"));
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RouteList.this,StopDetail.class);
                    intent.putExtra("county",getIntent().getStringExtra("location"));
                    intent.putExtra("stopuid",list.get(position).get("stopuid"));
                    intent.putExtra("stopname",list.get(position).get("route"));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView stop;
            public TextView status;
            public LinearLayout ll;

            public ViewHolder(View itemView) {
                super(itemView);
                stop = (TextView) itemView.findViewById(R.id.stop);
                status = (TextView) itemView.findViewById(R.id.status);
                ll = (LinearLayout) itemView.findViewById(R.id.ll);
            }
        }
    }
}