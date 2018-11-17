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

public class RouteDetail extends Activity {

    RecyclerView recyclerView;
    String href = "",href2 = "",hrefecode = "",route = "",sub = "",county = "",direction_ = "";
    MyAdapter myAdapter;
    RadioButton radioButton1,radioButton2;
    ImageButton imbtn;
    TextView update;
    boolean islike = false;
    int count = 0,check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stop_list);

        sub = getIntent().getStringExtra("route");

        county = getIntent().getStringExtra("location");
        route = getIntent().getStringExtra("route");
        direction_ = getIntent().getStringExtra("direction");
        update = (TextView) findViewById(R.id.update);

        Calendar mCal = Calendar.getInstance();
        CharSequence s = DateFormat.format("HH:mm", mCal.getTime());
        update.setText(""+s+"更新 (每分鐘)");

        hrefecode = Uri.encode(route);

        href = "http://ptx.transportdata.tw/MOTC/v2/Bus/DisplayStopOfRoute/City/"+county+"/"+hrefecode+"?$top=300&$format=JSON";
        href2 = "http://ptx.transportdata.tw/MOTC/v2/Bus/EstimatedTimeOfArrival/City/"+county+"/"+hrefecode+"?$orderby=StopSequence&$top=300&$format=JSON;";

        radioButton1 = (RadioButton) findViewById(R.id.radio1) ;
        radioButton2 = (RadioButton) findViewById(R.id.radio2) ;

        if(radioButton1.isChecked())excuteHttp("0");

        radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    recyclerView.setAdapter(null);
                    excuteHttp("0");
                    check = 0;
                }
            }
        });
        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    recyclerView.setAdapter(null);
                    excuteHttp("1");
                    check = 1;
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
                            Toast.makeText(RouteDetail.this, "最愛路線數上限為5", Toast.LENGTH_SHORT).show();
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

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result2 = TrafficHttpRequest.getData(href2);
                try {
                    final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                    myAdapter = new MyAdapter(list);

                    JSONObject jObject = new JSONObject(result);
                    JSONObject jObject2 = new JSONObject(result2);
                    int length = jObject.getJSONArray("results").length();
                    int start = 0;

                    if(length!=0){

                        for(int i = 0;i < length;i++){
                            if(jObject.getJSONArray("results").getJSONObject(i).getJSONObject("RouteName").getString("Zh_tw").equals(route)) {
                                start = i;
                                break;
                            }
                        }

                        for(int i = 0;i < jObject.getJSONArray("results").getJSONObject(start+Integer.parseInt(direction)).getJSONArray("Stops").length();i++) {
                            HashMap<String, String> item = new HashMap<String, String>();
                            if(jObject.getJSONArray("results").getJSONObject(start+Integer.parseInt(direction)).getJSONObject("RouteName").getString("Zh_tw").equals(route)) {
                                String stop = jObject.getJSONArray("results").getJSONObject(start+Integer.parseInt(direction)).getJSONArray("Stops").getJSONObject(i).getJSONObject("StopName").getString("Zh_tw");

                                String time = "未發車";
                                for (int j = 0; j < jObject2.getJSONArray("results").length(); j++) {
                                    if (jObject2.getJSONArray("results").getJSONObject(j).getString("Direction").equals(direction) &&
                                            jObject2.getJSONArray("results").getJSONObject(j).getJSONObject("RouteName").getString("Zh_tw").equals(route)) {
                                        String Route = jObject2.getJSONArray("results").getJSONObject(j).getJSONObject("StopName").getString("Zh_tw");
                                        String UID = jObject2.getJSONArray("results").getJSONObject(j).getString("StopUID");

                                        item.put("stopuid", UID);
                                        if (Route.equals(stop)) {
                                            if (!jObject2.getJSONArray("results").getJSONObject(j).isNull("EstimateTime")) {
                                                int Time = jObject2.getJSONArray("results").getJSONObject(j).getInt("EstimateTime");
                                                double realtime = (1.0 * Time) / 60.0;
                                                if (jObject2.getJSONArray("results").getJSONObject(j).getInt("EstimateTime") >= 0) {

                                                    if (realtime <= 2.0) {
                                                        time = "即將到站";
                                                        break;
                                                    } else if (realtime <= 1.0) {
                                                        time = "進佔中";
                                                        break;
                                                    } else {
                                                        time = "約 " + String.valueOf(Math.round(realtime)) + " 分鐘";
                                                        break;
                                                    }
                                                }
                                            } else {
                                                time = "未發車";
                                                break;
                                            }
                                        }
                                    }
                                }
                                item.put("time", time);
                                item.put("route", stop);
                                myAdapter.addItem(item);
                            }
                        }

                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RouteDetail.this, "查無資訊", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(RouteDetail.this, "查無 "+ d+" 資訊", Toast.LENGTH_SHORT).show();
                            }else {
                                recyclerView.setAdapter(myAdapter);
                                recyclerView.addItemDecoration(new DividerItemDecoration(RouteDetail.this, DividerItemDecoration.VERTICAL));
                                recyclerView.setLayoutManager(new LinearLayoutManager(RouteDetail.this));
                            }
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
            }
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
                    Intent intent = new Intent(RouteDetail.this, StopDetail.class);
                    intent.putExtra("county", getIntent().getStringExtra("location"));
                    intent.putExtra("stopuid", list.get(position).get("stopuid"));
                    intent.putExtra("stopname", list.get(position).get("route"));
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
