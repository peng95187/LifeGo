package com.lifegofood.jt3282.lifego.traffic.hsr;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.traffic.HSRService;
import com.lifegofood.jt3282.lifego.traffic.TrafficHttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.drawable.btn_star_big_off;
import static android.R.drawable.btn_star_big_on;

public class HSRStationSearch extends Activity {

    String href = "",search_time = "",st = "";
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    TextView station,search_date;
    ImageButton imbtn;
    boolean islike = false;
    SharedPreferences.Editor spe = null;
    SharedPreferences sp = null;
    int count = 0;
    Intent server = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hsr_station);

        server.setClass(HSRStationSearch.this, HSRService.class);
        spe = getSharedPreferences("mypref_hsrnotify", MODE_PRIVATE).edit();
        sp = getSharedPreferences("mypref_hsrnotify", MODE_PRIVATE);

        recyclerView = (RecyclerView) findViewById(R.id.hsrqsearch_rv);
        station = (TextView) findViewById(R.id.station) ;
        search_date = (TextView) findViewById(R.id.date) ;

        final String id = getIntent().getStringExtra("stationID");
        st = getIntent().getStringExtra("station");
        search_time = getIntent().getStringExtra("saveTime");

        final SharedPreferences.Editor spe = getSharedPreferences("mypref_hsrstop", MODE_PRIVATE).edit();
        final SharedPreferences sp = getSharedPreferences("mypref_hsrstop", MODE_PRIVATE);

        imbtn = (ImageButton) findViewById(R.id.imbtn);

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
                    spe.remove("stop"+count);
                    spe.remove("date"+count);
                    spe.commit();
                    imbtn.setBackgroundResource(btn_star_big_off);
                    islike = false;
                }else{
                    for(int i=0;i<5;i++){
                        if(sp.getString("id"+i,"").equals("")){
                            imbtn.setBackgroundResource(btn_star_big_on);
                            spe.putString("stop"+i ,st);
                            spe.putString("id"+i ,id);
                            spe.putString("date"+i ,search_time);
                            spe.commit();
                            break;
                        }
                        if(i==4&&(!sp.getString("id"+i,"").equals(""))){
                            Toast.makeText(HSRStationSearch.this, "最愛站牌數上限為5", Toast.LENGTH_SHORT).show();
                        }
                    }
                    islike = true;
                }
            }
        });

        station.setText(st);
        search_date.setText(search_time);
        href = "http://ptx.transportdata.tw/MOTC/v2/Rail/THSR/DailyTimetable/Station/"+id+"/"+search_time+"?$top=300&$format=JSON";
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
                final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                myAdapter = new MyAdapter(list);
                JSONObject jObject = new JSONObject(result);

                int length = jObject.getJSONArray("results").length();
                if (length == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(HSRStationSearch.this, "查無資訊", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                if (jObject != null)
                    for (int i = 0; i < jObject.getJSONArray("results").length(); i++) {
                        HashMap<String, String> item = new HashMap<String, String>();
                        String direction = jObject.getJSONArray("results").getJSONObject(i).getString("Direction");
                        String trainNo = jObject.getJSONArray("results").getJSONObject(i).getString("TrainNo");
                        String endingStationName = jObject.getJSONArray("results").getJSONObject(i).getString("EndingStationName");
                        String startingStationName = jObject.getJSONArray("results").getJSONObject(i).getString("StartingStationName");
                        String stime = jObject.getJSONArray("results").getJSONObject(i).getString("ArrivalTime");

                        item.put("direction", direction);
                        item.put("trainNo", trainNo);
                        item.put("endingStationName", endingStationName);
                        item.put("startingStationName", startingStationName);
                        item.put("stime", stime);

                        myAdapter.addItem(item);

                    }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(myAdapter);
                        recyclerView.addItemDecoration(new DividerItemDecoration(HSRStationSearch.this, DividerItemDecoration.VERTICAL));
                        recyclerView.setLayoutManager(new LinearLayoutManager(HSRStationSearch.this));
                    }
                });

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        public MyAdapter(ArrayList<HashMap<String, String>> newlist) {
            // TODO 自动生成的构造函数存根
            list = newlist;
        }

        public void addItem(HashMap<String, String> item) {
            list.add(item);
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hsrstation_rv, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {

            holder.trainNo.setText(list.get(position).get("trainNo") + "次 ");
            holder.estimatetime.setText(list.get(position).get("stime") + " 到站");
            holder.endstation.setText(list.get(position).get("startingStationName") + "-" + list.get(position).get("endingStationName"));
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(HSRStationSearch.this, HSRSearchDetail.class);
                    intent.putExtra("id", list.get(position).get("trainNo"));
                    startActivity(intent);
                }
            });
            holder.imbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String type = "高鐵";
                    final String arr_time = list.get(position).get("stime");
                    final String info = list.get(position).get("trainNo") + "次  "+st+" "+list.get(position).get("stime") +
                            " 到站  "+list.get(position).get("startingStationName") + "~" + list.get(position).get("endingStationName");
                    AlertDialog.Builder dialog = new AlertDialog.Builder(HSRStationSearch.this);
                    dialog.setTitle("設定提醒 : "+list.get(position).get("trainNo") + "次 ");
                    dialog.setMessage(search_time+"\n"+"\n"+st+" "+list.get(position).get("stime") +
                            " 到站  "+list.get(position).get("startingStationName") + "~" + list.get(position).get("endingStationName"));
                    dialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //not to do
                        }

                    });
                    dialog.setPositiveButton("確定",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            boolean same = false;
                            for(int i=0;i<5;i++) {
                                if (sp.getString("info" + i, "").equals(info)&&sp.getString("time" + i, "").equals("10分鐘前")){
                                    same = true;
                                    break;
                                }
                            }
                            if(same == false) {
                                for (int i = 0; i < 5; i++) {
                                    if (sp.getString("info" + i, "").equals("")) {
                                        spe.putString("info" + i, info);
                                        spe.putString("type" + i, type);
                                        spe.putString("time" + i, "10分鐘前");
                                        spe.putString("arr_time" + i, search_time + " " + arr_time);
                                        spe.commit();
                                        startService(server);
                                        Toast.makeText(HSRStationSearch.this, "設定成功", Toast.LENGTH_SHORT).show();
                                        break;
                                    } else if (i == 4 && !sp.getString("info" + i, "").equals("")) {
                                        Toast.makeText(HSRStationSearch.this, "高鐵提醒數上限 5", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }else{
                                Toast.makeText(HSRStationSearch.this, "已有重複的設定", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                    dialog.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView direction, trainNo, estimatetime, endstation, delaytime;
            public LinearLayout ll;
            public ImageButton imbtn;

            public ViewHolder(View itemView) {
                super(itemView);

                direction = (TextView) itemView.findViewById(R.id.direction);
                trainNo = (TextView) itemView.findViewById(R.id.trainNo);
                estimatetime = (TextView) itemView.findViewById(R.id.estimatetime);
                endstation = (TextView) itemView.findViewById(R.id.endstation);
                delaytime = (TextView) itemView.findViewById(R.id.delaytime);
                ll = (LinearLayout)  itemView.findViewById(R.id.ll);
                imbtn = (ImageButton)  itemView.findViewById(R.id.imbtn);
            }
        }
    }
}
