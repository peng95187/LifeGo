package com.lifegofood.jt3282.lifego.traffic.railway;

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

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static android.R.drawable.btn_star_big_off;
import static android.R.drawable.btn_star_big_on;

public class RWDetail extends Activity {

    TextView stationName,stationNameen,update;
    String station,stationID,href,delayHref,stationinen;
    String [] train,delay;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    ImageButton imbtn;
    Calendar mCal = Calendar.getInstance();
    Time nt;
    int count = 0;
    boolean islike = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rwdetail);

        final SharedPreferences.Editor spe = getSharedPreferences("mypref_trainstop", MODE_PRIVATE).edit();
        final SharedPreferences sp = getSharedPreferences("mypref_trainstop", MODE_PRIVATE);
        station = getIntent().getStringExtra("station");
        stationID = getIntent().getStringExtra("stationID");

        imbtn = (ImageButton) findViewById(R.id.imbtn);

        for(int i=0;i<5;i++){
            if(sp.getString("id"+i,"").equals(stationID)){
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
                    spe.remove("stop"+count);
                    spe.commit();
                    imbtn.setBackgroundResource(btn_star_big_off);
                    islike = false;
                }else{
                    for(int i=0;i<5;i++){
                        if(sp.getString("id"+i,"").equals("")){
                            imbtn.setBackgroundResource(btn_star_big_on);
                            spe.putString("id"+i ,stationID);
                            spe.putString("stop"+i ,station);
                            spe.commit();
                            break;
                        }
                        if(i==4&&(!sp.getString("id"+i,"").equals(""))){
                            Toast.makeText(RWDetail.this, "最愛站牌數上限為5", Toast.LENGTH_SHORT).show();
                        }
                    }
                    islike = true;
                }
            }
        });

        CharSequence s1 = DateFormat.format("yyyy-MM-dd", mCal.getTime());
        href = "http://ptx.transportdata.tw/MOTC/v2/Rail/TRA/DailyTimetable/Station/"+stationID+"/"+s1+"?$top=300&$format=JSON";
        delayHref = "http://ptx.transportdata.tw/MOTC/v2/Rail/TRA/LiveBoard/Station/"+stationID+"?$top=30&$format=JSON";
        CharSequence s2 = DateFormat.format("HH:mm:ss", mCal.getTime());
        nt = Time.valueOf(""+s2);
        new DelayAsyncTask().execute();
        new HttpAsyncTask().execute();

        stationName = (TextView)findViewById(R.id.stop);
        stationNameen = (TextView)findViewById(R.id.stopen);
        update = (TextView)findViewById(R.id.update);
        update.setText(""+s2+"更新 (每分鐘更新)");
        stationName.setText(station+"車站");
        recyclerView = (RecyclerView)findViewById(R.id.rcv);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(Intent.ACTION_TIME_TICK)) {
                    Calendar mCal2 = Calendar.getInstance();
                    CharSequence s2 = DateFormat.format("HH:mm:ss", mCal2.getTime());
                    update.setText(""+s2+"更新 (每分鐘更新)");
                    nt = Time.valueOf(""+s2);
                    new DelayAsyncTask().execute();
                    new HttpAsyncTask().execute();
                }
            }
        };
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver,filter);
        unregisterReceiver(receiver);
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = TrafficHttpRequest.getData(href);
            return result;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(final String result) {

            try {
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                myAdapter = new MyAdapter(list);
                JSONObject jObject = new JSONObject(result);

                stationinen = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("StationName").getString("En");
                for (int i = 0,j = 0;j < 6 && i < jObject.getJSONArray("results").length(); i++) {
                    HashMap<String, String> item = new HashMap<String, String>();
                    String departureTime = jObject.getJSONArray("results").getJSONObject(i).getString("DepartureTime");//到站
                    Time dt = Time.valueOf(departureTime + ":00");
                    if (dt.after(nt)) {
                        if(stationID.equals("1001")){
                            if(jObject.getJSONArray("results").getJSONObject(i).getString("Direction").equals("1")) {
                                String trainNo = jObject.getJSONArray("results").getJSONObject(i).getString("TrainNo");
                                String trainClassificationName = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("TrainTypeName").getString("Zh_tw");
                                String endingStationName = jObject.getJSONArray("results").getJSONObject(i).getString("EndingStationName");
                                String tripLine = jObject.getJSONArray("results").getJSONObject(i).getString("TripLine");
                                String direction = jObject.getJSONArray("results").getJSONObject(i).getString("Direction");
                                j++;
                                departureTime = convertTime(departureTime);

                                item.put("departureTime", departureTime);
                                item.put("trainNo", trainNo);
                                item.put("trainClassificationName", trainClassificationName);
                                item.put("endingStationName", endingStationName);
                                item.put("tripLine", tripLine);
                                item.put("direction", direction);
                                myAdapter.addItem(item);
                            }
                        }else {
                            String trainNo = jObject.getJSONArray("results").getJSONObject(i).getString("TrainNo");
                            String trainClassificationName = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("TrainTypeName").getString("Zh_tw");
                            String endingStationName = jObject.getJSONArray("results").getJSONObject(i).getString("EndingStationName");
                            String tripLine = jObject.getJSONArray("results").getJSONObject(i).getString("TripLine");
                            String direction = jObject.getJSONArray("results").getJSONObject(i).getString("Direction");
                            j++;
                            departureTime = convertTime(departureTime);

                            item.put("departureTime", departureTime);
                            item.put("trainNo", trainNo);
                            item.put("trainClassificationName", trainClassificationName);
                            item.put("endingStationName", endingStationName);
                            item.put("tripLine", tripLine);
                            item.put("direction", direction);
                            myAdapter.addItem(item);
                        }
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stationNameen.setText(stationinen);
                        recyclerView.setAdapter(myAdapter);
                        recyclerView.addItemDecoration(new DividerItemDecoration(RWDetail.this, DividerItemDecoration.VERTICAL));
                        recyclerView.setLayoutManager(new LinearLayoutManager(RWDetail.this));
                    }
                });

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        private String convertTime(String time) {

            String result = "";
            result += time.subSequence(0, 5);
            return result;
        }
    }
    private class DelayAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = TrafficHttpRequest.getData(delayHref);
            return result;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(final String result) {

            try {
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                myAdapter = new MyAdapter(list);
                JSONObject jObject = new JSONObject(result);
                train = new String[jObject.getJSONArray("results").length()] ;
                delay = new String[jObject.getJSONArray("results").length()] ;

                for (int i = 0 ; i < jObject.getJSONArray("results").length(); i++) {
                    HashMap<String, String> item = new HashMap<String, String>();
                    String trainNo = jObject.getJSONArray("results").getJSONObject(i).getString("TrainNo");
                    String delayTime = jObject.getJSONArray("results").getJSONObject(i).getString("DelayTime");

                    train[i] = trainNo;
                    delay[i] = delayTime;
                }

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
            list.add(item);
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_rwdetail,parent,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {

            //北 或 南向
            if(list.get(position).get("direction").equals("0")) {
                holder.direction.setText("上行");
            }else{
                holder.direction.setText("下行");
            }
            //列車資訊
            holder.traininfo.setText(list.get(position).get("trainNo")+"次 "+list.get(position).get("trainClassificationName"));
            //到站
            holder.estimatetime.setText(list.get(position).get("departureTime"));
            holder.endstation.setText("開往 "+list.get(position).get("endingStationName"));
            holder.delaytime.setText("晚 "+checkDelay(list.get(position).get("trainNo"))+" 分鐘");

            if(list.get(position).get("tripLine").equals("0")){
                holder.tripline.setText("不經山海");
            }else if(list.get(position).get("tripLine").equals("1")){
                holder.tripline.setText("山線");
            }else if(list.get(position).get("tripLine").equals("2")){
                holder.tripline.setText("海線");
            }else {
                holder.tripline.setText("");
            }

        }

        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView direction,traininfo,estimatetime,endstation,delaytime,tripline;

            public ViewHolder(View itemView) {
                super(itemView);

                direction = (TextView)itemView.findViewById(R.id.direction);
                traininfo = (TextView)itemView.findViewById(R.id.traininfo);
                estimatetime = (TextView)itemView.findViewById(R.id.estimatetime);
                endstation = (TextView)itemView.findViewById(R.id.endstation);
                delaytime = (TextView)itemView.findViewById(R.id.delaytime);
                tripline = (TextView)itemView.findViewById(R.id.tripline);
            }
        }
        public String checkDelay(String trainno){
            String result = "0";
            for(int i = 0;i < train.length ; i++){
                if(trainno.equals(train[i])){
                    result = delay[i];
                }
            }
            return result;
        }
    }
}
