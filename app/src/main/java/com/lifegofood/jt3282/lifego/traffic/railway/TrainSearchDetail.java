package com.lifegofood.jt3282.lifego.traffic.railway;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.traffic.TrafficHttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.drawable.arrow_down_float;
import static android.R.drawable.arrow_up_float;
import static android.R.drawable.btn_star_big_off;
import static android.R.drawable.btn_star_big_on;

public class TrainSearchDetail extends Activity {

    String href,finalResponse = "";
    TextView trainNo,trainType,startStation,endStation,direction,tripline,wheel,bike,breastfeeding,packages,note;
    LinearLayout linearLayout2;
    RelativeLayout linearLayout;
    RelativeLayout relativeLayout;
    ImageButton imageButton;
    MyAdapter myAdapter;
    ImageButton imbtn;
    RecyclerView recyclerView;
    boolean islike = false;
    int count = 0;
    SharedPreferences.Editor spe = null;
    SharedPreferences sp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.train_search_detail);

        final String id = getIntent().getStringExtra("id");

        imbtn = (ImageButton) findViewById(R.id.imbtn);
        spe = getSharedPreferences("mypref_trainid", MODE_PRIVATE).edit();
        sp = getSharedPreferences("mypref_trainid", MODE_PRIVATE);
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
                    spe.remove("type"+count);
                    spe.remove("direction"+count);
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
                            Toast.makeText(TrainSearchDetail.this, "最愛路線數上限為5", Toast.LENGTH_SHORT).show();
                        }
                    }
                    islike = true;
                }
            }
        });

        trainNo = (TextView)findViewById(R.id.trainNo);
        trainType = (TextView)findViewById(R.id.trainType);
        startStation = (TextView)findViewById(R.id.startStation);
        endStation = (TextView)findViewById(R.id.endStation);
        direction = (TextView)findViewById(R.id.direction);
        tripline = (TextView)findViewById(R.id.tripline);
        wheel = (TextView)findViewById(R.id.wheel);
        bike = (TextView)findViewById(R.id.bike);
        breastfeeding = (TextView)findViewById(R.id.breastfeeding);
        packages = (TextView)findViewById(R.id.packages);
        note = (TextView)findViewById(R.id.note);
        linearLayout = (RelativeLayout) findViewById(R.id.lltsd);
        recyclerView = (RecyclerView)findViewById(R.id.tsd_rv);

        linearLayout2 = (LinearLayout)findViewById(R.id.llopen);
        imageButton = (ImageButton)findViewById(R.id.imbopen);
        relativeLayout = (RelativeLayout)findViewById(R.id.rlbopen);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearLayout2.getVisibility()==View.GONE) {
                    linearLayout2.setVisibility(View.VISIBLE);
                    imageButton.setBackgroundResource(arrow_up_float);
                }else{
                    linearLayout2.setVisibility(View.GONE);
                    imageButton.setBackgroundResource(arrow_down_float);
                }
            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearLayout2.getVisibility()==View.GONE) {
                    linearLayout2.setVisibility(View.VISIBLE);
                    imageButton.setBackgroundResource(arrow_up_float);
                }else{
                    linearLayout2.setVisibility(View.GONE);
                    imageButton.setBackgroundResource(arrow_down_float);
                }
            }
        });

        href = "http://ptx.transportdata.tw/MOTC/v2/Rail/TRA/GeneralTimetable/TrainNo/"+id+"?$top=30&$format=JSON";
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
                final SharedPreferences.Editor spe2 = getSharedPreferences("mypref_trainid_type", MODE_PRIVATE).edit();
                final SharedPreferences.Editor spe3 = getSharedPreferences("mypref_trainid_direction", MODE_PRIVATE).edit();
                if(length==1){

                    final String no = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").getString("TrainNo");
                    final String type = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").getJSONObject("TrainTypeName").getString("Zh_tw");
                    final String startName = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").getJSONObject("StartingStationName").getString("Zh_tw");
                    final String endName = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").getJSONObject("EndingStationName").getString("Zh_tw");
                    final String drt = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").getString("Direction");
                    final String tl = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").getString("TripLine");
                    final String w = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").getString("WheelchairFlag");
                    final String b = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").getString("BikeFlag");
                    final String bf = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").getString("BreastFeedingFlag");
                    final String p = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").getString("PackageServiceFlag");
                    final String n = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("GeneralTimetable").getJSONObject("GeneralTrainInfo").getJSONObject("Note").getString("Zh_tw");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for(int i=0;i<5;i++){
                                if(sp.getString("id"+i,"").equals(no)){
                                    spe2.putString("type"+i ,type);
                                    spe3.putString("direction"+i ,startName+"~"+endName);
                                    spe2.commit();
                                    spe3.commit();
                                    break;
                                }
                            }
                        }
                    });

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

                            linearLayout.setVisibility(View.VISIBLE);
                            trainNo.setText("車次 : "+no);
                            trainType.setText("車種 : "+type);
                            startStation.setText("起點 : "+startName);
                            endStation.setText("終點 : "+endName);

                            if(drt.equals("0")) direction.setText("上/下行 : 上行");
                            else direction.setText("上/下行 : 下行");

                            if(tl.equals("0")) tripline.setText("山/海線 : 不經山海");
                            else if(tl.equals("1")) tripline.setText("山/海線 : 山線");
                            else tripline.setText("山/海線 : 海線");

                            wheel.setText("是否提供輪椅服務 : "+(w.equals("true")?"是":"否"));
                            bike.setText("是否可攜帶自行車 : "+(b.equals("true")?"是":"否"));
                            packages.setText("是否提供行李服務 : "+(p.equals("true")?"是":"否"));
                            breastfeeding.setText("是否設有哺乳室 : "+(bf.equals("true")?"是":"否"));
                            note.setText("附註 : "+n);

                            recyclerView.setAdapter(myAdapter);
                            recyclerView.addItemDecoration(new DividerItemDecoration(TrainSearchDetail.this, DividerItemDecoration.VERTICAL));
                            recyclerView.setLayoutManager(new LinearLayoutManager(TrainSearchDetail.this));
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TrainSearchDetail.this, "查無資訊", Toast.LENGTH_SHORT).show();
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
