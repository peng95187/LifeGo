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

public class HSRQSearchDetail extends Activity {

    String href = "",search_time = "";
    MyAdapter myAdapter;
    SharedPreferences.Editor spe = null;
    SharedPreferences sp = null;
    RecyclerView recyclerView;
    Intent server = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hsrqsearch);

        server.setClass(HSRQSearchDetail.this, HSRService.class);

        spe = getSharedPreferences("mypref_hsrnotify", MODE_PRIVATE).edit();
        sp = getSharedPreferences("mypref_hsrnotify", MODE_PRIVATE);

        String start = getIntent().getStringExtra("stationID");
        String end = getIntent().getStringExtra("stationID2");
        search_time = getIntent().getStringExtra("saveTime");

        recyclerView = (RecyclerView) findViewById(R.id.hsrqsearch_rv);

        href = "http://ptx.transportdata.tw/MOTC/v2/Rail/THSR/DailyTimetable/OD/"+start+"/to/"+end+"/"+search_time+"?$orderby=OriginStopTime%2FArrivalTime&$top=300&$format=JSON";
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
                            Toast.makeText(HSRQSearchDetail.this, "查無資訊", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                if (jObject != null)
                    for (int i = 0; i < jObject.getJSONArray("results").length(); i++) {
                        HashMap<String, String> item = new HashMap<String, String>();
                        String direction = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("DailyTrainInfo").getString("Direction");
                        String trainNo = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("DailyTrainInfo").getString("TrainNo");
                        String endingStationName = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("DailyTrainInfo").getJSONObject("EndingStationName").getString("Zh_tw");
                        String startingStationName = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("DailyTrainInfo").getJSONObject("StartingStationName").getString("Zh_tw");
                        String stime = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("OriginStopTime").getString("DepartureTime");
                        String arr = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("DestinationStopTime").getString("ArrivalTime");
                        String st = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("OriginStopTime").getJSONObject("StationName").getString("Zh_tw");
                        String ft = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("DestinationStopTime").getJSONObject("StationName").getString("Zh_tw");

                        item.put("direction", direction);
                        item.put("trainNo", trainNo);
                        item.put("endingStationName", endingStationName);
                        item.put("startingStationName", startingStationName);
                        item.put("stime", stime);
                        item.put("arr", arr);
                        item.put("st", st);
                        item.put("ft", ft);
                        myAdapter.addItem(item);
                    }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(myAdapter);
                        recyclerView.addItemDecoration(new DividerItemDecoration(HSRQSearchDetail.this, DividerItemDecoration.VERTICAL));
                        recyclerView.setLayoutManager(new LinearLayoutManager(HSRQSearchDetail.this));
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hsr_tb_rv, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {

            //列車資訊
            holder.trainNo.setText(list.get(position).get("trainNo") + "次 ");
            holder.estimatetime.setText(list.get(position).get("st")+" "+list.get(position).get("stime") + "-" +list.get(position).get("arr")+" "+list.get(position).get("ft"));
            holder.endstation.setText(list.get(position).get("startingStationName") + "-" + list.get(position).get("endingStationName"));
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(HSRQSearchDetail.this, HSRSearchDetail.class);
                    intent.putExtra("id", list.get(position).get("trainNo"));
                    startActivity(intent);
                }
            });

            holder.imbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String type = "高鐵";
                    final String arr_time = list.get(position).get("stime");
                    final String info = list.get(position).get("trainNo") + "次  "+list.get(position).get("st")+" "+list.get(position).get("stime") +
                            " 到站  "+list.get(position).get("startingStationName") + "~" + list.get(position).get("endingStationName");
                    AlertDialog.Builder dialog = new AlertDialog.Builder(HSRQSearchDetail.this);
                    dialog.setTitle("設定提醒 : "+list.get(position).get("trainNo") + "次 ");
                    dialog.setMessage(search_time+"\n"+"\n"+list.get(position).get("st")+" "+list.get(position).get("stime") + "-" +list.get(position).get("arr")+" "+list.get(position).get("ft")+
                            " "+list.get(position).get("startingStationName") + "-" + list.get(position).get("endingStationName"));
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
                                        Toast.makeText(HSRQSearchDetail.this, "設定成功", Toast.LENGTH_SHORT).show();
                                        break;
                                    } else if (i == 4 && !sp.getString("info" + i, "").equals("")) {
                                        Toast.makeText(HSRQSearchDetail.this, "高鐵提醒數上限 5", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }else{
                                Toast.makeText(HSRQSearchDetail.this, "已有重複的設定", Toast.LENGTH_SHORT).show();
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

            public TextView trainNo, estimatetime, endstation;
            public LinearLayout linearLayout;
            public ImageButton imbtn;

            public ViewHolder(View itemView) {
                super(itemView);

                trainNo = (TextView) itemView.findViewById(R.id.trainNo);
                estimatetime = (TextView) itemView.findViewById(R.id.estimatetime);
                endstation = (TextView) itemView.findViewById(R.id.endstation);
                linearLayout = (LinearLayout) itemView.findViewById(R.id.ll);
                imbtn = (ImageButton) itemView.findViewById(R.id.imbtn);
            }
        }
    }
}
