package com.lifegofood.jt3282.lifego.traffic.hsr;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.traffic.TrafficHttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.drawable.btn_star_big_off;
import static android.R.drawable.btn_star_big_on;

public class HSRSearchDetail extends Activity {

    String href = "";
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    RelativeLayout linearLayout;
    ImageButton imbtn;
    boolean islike = false;
    int count = 0;
    SharedPreferences.Editor spe = null;
    SharedPreferences sp = null;
    TextView trainNo,startStation,endStation,direction,work;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hsrsearch_detail);

        final String id = getIntent().getStringExtra("id");

        recyclerView = (RecyclerView) findViewById(R.id.hsd_rv);
        trainNo = (TextView) findViewById(R.id.trainNo) ;
        startStation = (TextView) findViewById(R.id.startStation) ;
        endStation = (TextView) findViewById(R.id.endStation) ;
        direction = (TextView) findViewById(R.id.direction) ;
        work = (TextView) findViewById(R.id.work) ;
        linearLayout = (RelativeLayout) findViewById(R.id.llhsd);

        imbtn = (ImageButton) findViewById(R.id.imbtn);

        spe = getSharedPreferences("mypref_hsrid", MODE_PRIVATE).edit();
        sp = getSharedPreferences("mypref_hsrid", MODE_PRIVATE);
        for(int i=0;i<5;i++){
            if(sp.getString("id"+i,"").equals(id)){
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
                    spe.remove("direction"+count);
                    spe.remove("way"+count);
                    spe.commit();
                    imbtn.setBackgroundResource(btn_star_big_off);
                    islike = false;
                }else{
                    for(int i=0;i<5;i++){
                        if(sp.getString("id"+i,"").equals("")){
                            imbtn.setBackgroundResource(btn_star_big_on);
                            spe.putString("id"+i ,id);
                            spe.commit();
                            break;
                        }
                        if(i==4&&(!sp.getString("id"+i,"").equals(""))){
                            Toast.makeText(HSRSearchDetail.this, "最愛路線數上限為5", Toast.LENGTH_SHORT).show();
                        }
                    }
                    islike = true;
                }
            }
        });

        href = "http://ptx.transportdata.tw/MOTC/v2/Rail/THSR/GeneralTimetable/TrainNo/"+id+"?$top=30&$format=JSON";

        new HttpAsyncTask().execute();
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
                final JSONObject jObject = new JSONObject(result);
                final int length = jObject.getJSONArray("results").length();
                final SharedPreferences.Editor spe2 = getSharedPreferences("mypref_hsrid_direction", MODE_PRIVATE).edit();
                final SharedPreferences.Editor spe3 = getSharedPreferences("mypref_hsrid_way", MODE_PRIVATE).edit();

                if(length==1){

                    final String no = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").getString("TrainNo");
                    final String startName = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").getJSONObject("StartingStationName").getString("Zh_tw");
                    final String endName = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").getJSONObject("EndingStationName").getString("Zh_tw");
                    final String drt = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").getString("Direction");
                    final String mon = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("GeneralTimetable").getJSONObject("ServiceDay").getString("Monday");
                    final String tue = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("GeneralTimetable").getJSONObject("ServiceDay").getString("Tuesday");
                    final String wed = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("GeneralTimetable").getJSONObject("ServiceDay").getString("Wednesday");
                    final String thu = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("GeneralTimetable").getJSONObject("ServiceDay").getString("Thursday");
                    final String fri = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("GeneralTimetable").getJSONObject("ServiceDay").getString("Friday");
                    final String sat = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("GeneralTimetable").getJSONObject("ServiceDay").getString("Saturday");
                    final String sun = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("GeneralTimetable").getJSONObject("ServiceDay").getString("Sunday");

                    for (int i = 0; i < jObject.getJSONArray("results").getJSONObject(0).getJSONObject("GeneralTimetable").getJSONArray("StopTimes").length(); i++) {
                        HashMap<String, String> item = new HashMap<String, String>();
                        String stationName = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("GeneralTimetable").getJSONArray("StopTimes").getJSONObject(i).getJSONObject("StationName").getString("Zh_tw");
                        String departureTime = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("GeneralTimetable").getJSONArray("StopTimes").getJSONObject(i).getString("DepartureTime");
                        item.put("station",stationName);
                        item.put("time",departureTime);
                        myAdapter.addItem(item);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for(int i=0;i<5;i++){
                                if(sp.getString("id"+i,"").equals(no)){
                                    spe2.putString("direction"+i ,startName+"~"+endName);
                                    spe3.putString("way"+i ,drt);
                                    spe2.commit();
                                    spe3.commit();
                                    break;
                                }
                            }
                        }
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            linearLayout.setVisibility(View.VISIBLE);
                            trainNo.setText("車次 : "+no);
                            startStation.setText("起點 : "+startName);
                            endStation.setText("終點 : "+endName);

                            if(drt.equals("0")) direction.setText("上/下行 : 下行");
                            else direction.setText("上/下行 : 上行");
                            work.setText("營運(星期) : "+(mon.equals("1")?"一 ":"")+(tue.equals("1")?"二 ":"")+(wed.equals("1")?"三 ":"")
                                    +(thu.equals("1")?"四 ":"")+(fri.equals("1")?"五 ":"")+(sat.equals("1")?"六 ":"")+(sun.equals("1")?"日":""));

                            recyclerView.setAdapter(myAdapter);
                            recyclerView.addItemDecoration(new DividerItemDecoration(HSRSearchDetail.this, DividerItemDecoration.VERTICAL));
                            recyclerView.setLayoutManager(new LinearLayoutManager(HSRSearchDetail.this));
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(HSRSearchDetail.this, "查無資訊", Toast.LENGTH_SHORT).show();
                        }
                    });
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tsd_rv,parent,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {

            holder.station.setText(list.get(position).get("station"));
            holder.time.setText(list.get(position).get("time"));
        }

        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView station,time;

            public ViewHolder(View itemView) {
                super(itemView);

                station = (TextView)itemView.findViewById(R.id.station);
                time = (TextView)itemView.findViewById(R.id.time);
            }
        }
    }
}
