package com.lifegofood.jt3282.lifego.traffic.railway;

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
import com.lifegofood.jt3282.lifego.traffic.TRAService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.lifegofood.jt3282.lifego.traffic.TrafficHttpRequest.getData;

public class TRAQsearch_detail extends Activity {

    String href = "",dt = "";
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    SharedPreferences.Editor spe = null;
    SharedPreferences sp = null;
    Intent server = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traqsearch);

        server.setClass(TRAQsearch_detail.this, TRAService.class);

        spe = getSharedPreferences("mypref_tranotify", MODE_PRIVATE).edit();
        sp = getSharedPreferences("mypref_tranotify", MODE_PRIVATE);

        String start = getIntent().getStringExtra("stationID");
        String end = getIntent().getStringExtra("stationID2");

        recyclerView = (RecyclerView)findViewById(R.id.traqsearch_rv) ;

        dt = getIntent().getStringExtra("saveTime");

        href = "http://ptx.transportdata.tw/MOTC/v2/Rail/TRA/DailyTimetable/OD/"+start+"/to/"+end+"/"+dt+"?$orderby=OriginStopTime%2FDepartureTime&$top=300&$format=JSON";

        new HttpAsyncTask().execute();
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = getData(href);
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
                if (length==0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TRAQsearch_detail.this, "查無資訊", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                if (jObject != null)
                    for (int i = 0; i < jObject.getJSONArray("results").length(); i++) {
                        HashMap<String, String> item = new HashMap<String, String>();
                        String direction = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("DailyTrainInfo").getString("Direction");
                        String trainNo = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("DailyTrainInfo").getString("TrainNo");
                        String trainClassificationName = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("DailyTrainInfo").getJSONObject("TrainTypeName").getString("Zh_tw");
                        String endingStationName = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("DailyTrainInfo").getJSONObject("EndingStationName").getString("Zh_tw");
                        String startingStationName = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("DailyTrainInfo").getJSONObject("StartingStationName").getString("Zh_tw");
                        String tripLine = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("DailyTrainInfo").getString("TripLine");
                        String stime = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("OriginStopTime").getString("DepartureTime");
                        String station = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("OriginStopTime").getJSONObject("StationName").getString("Zh_tw");

                        item.put("direction", direction);
                        item.put("trainNo", trainNo);
                        item.put("trainClassificationName", trainClassificationName);
                        item.put("endingStationName", endingStationName);
                        item.put("startingStationName", startingStationName);
                        item.put("tripLine", tripLine);
                        item.put("stime", stime);
                        item.put("station", station);

                        myAdapter.addItem(item);

                    }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(myAdapter);
                        recyclerView.addItemDecoration(new DividerItemDecoration(TRAQsearch_detail.this, DividerItemDecoration.VERTICAL));
                        recyclerView.setLayoutManager(new LinearLayoutManager(TRAQsearch_detail.this));
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tblist_rv, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {

            //列車資訊
            holder.traininfo.setText(list.get(position).get("trainClassificationName"));
            holder.trainNo.setText(list.get(position).get("trainNo") + "次 ");
            holder.estimatetime.setText(list.get(position).get("stime")+" 出站");
            holder.endstation.setText(list.get(position).get("startingStationName") +"-" + list.get(position).get("endingStationName"));

            if (list.get(position).get("tripLine").equals("0")) {
                holder.tripline.setText("不經山海");
            } else if (list.get(position).get("tripLine").equals("1")) {
                holder.tripline.setText("行經山線");
            } else if (list.get(position).get("tripLine").equals("2")) {
                holder.tripline.setText("行經海線");
            } else {
                holder.tripline.setText("");
            }
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(TRAQsearch_detail.this,TrainSearchDetail.class);
                    intent.putExtra("id",list.get(position).get("trainNo"));
                    startActivity(intent);
                }
            });
            holder.imbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String type = "台鐵";
                    final String arr_time = list.get(position).get("stime");
                    final String info = list.get(position).get("trainNo") + "次  "+list.get(position).get("station")+" "+list.get(position).get("stime") +
                            " 出站  "+list.get(position).get("startingStationName") + "~" + list.get(position).get("endingStationName");
                    AlertDialog.Builder dialog = new AlertDialog.Builder(TRAQsearch_detail.this);
                    dialog.setTitle("設定提醒 : "+list.get(position).get("trainNo") + "次 ");
                    dialog.setMessage(dt+"\n"+"\n"+list.get(position).get("station")+" "+list.get(position).get("stime") +
                            " 出站  "+list.get(position).get("startingStationName") + "~" + list.get(position).get("endingStationName"));
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
                                        spe.putString("type" + i, type);
                                        spe.putString("time" + i, "10分鐘前");
                                        spe.putString("arr_time" + i, dt + " " + arr_time);
                                        spe.commit();
                                        startService(server);
                                        Toast.makeText(TRAQsearch_detail.this, "設定成功", Toast.LENGTH_SHORT).show();
                                        break;
                                    } else if (i == 4 && !sp.getString("info" + i, "").equals("")) {
                                        Toast.makeText(TRAQsearch_detail.this, "台鐵提醒數上限 5", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }else{
                                Toast.makeText(TRAQsearch_detail.this, "已有重複的設定", Toast.LENGTH_SHORT).show();
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

            public TextView direction, traininfo,trainNo, estimatetime, endstation, delaytime, tripline;
            public LinearLayout ll;
            public ImageButton imbtn;

            public ViewHolder(View itemView) {
                super(itemView);

                direction = (TextView) itemView.findViewById(R.id.direction);
                trainNo = (TextView) itemView.findViewById(R.id.trainNo);
                traininfo = (TextView) itemView.findViewById(R.id.traininfo);
                estimatetime = (TextView) itemView.findViewById(R.id.estimatetime);
                endstation = (TextView) itemView.findViewById(R.id.endstation);
                delaytime = (TextView) itemView.findViewById(R.id.delaytime);
                tripline = (TextView) itemView.findViewById(R.id.tripline);
                ll = (LinearLayout) itemView.findViewById(R.id.ll);
                imbtn = (ImageButton) itemView.findViewById(R.id.imbtn);
            }
        }
    }
}
