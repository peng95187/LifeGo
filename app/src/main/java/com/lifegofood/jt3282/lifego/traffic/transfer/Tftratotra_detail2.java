package com.lifegofood.jt3282.lifego.traffic.transfer;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.traffic.TrafficHttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Tftratotra_detail2 extends Activity {

    String href1 = "",date = "",time = "",start = "",mid1 = "",mid2 = "",end = "";
    MyAdapter myAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tftratotra_detail2);
        recyclerView = (RecyclerView) findViewById(R.id.tr_rv2);

        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");
        start = getIntent().getStringExtra("start");
        mid1   = getIntent().getStringExtra("mid1");
        mid2   = getIntent().getStringExtra("mid2");
        end   = getIntent().getStringExtra("end");

        href1 = "http://ptx.transportdata.tw/MOTC/v2/Rail/TRA/DailyTimetable/OD/"+start+"/to/"+mid1+"/"+date+"?$filter=OriginStopTime%2FDepartureTime%20ge%20'"
                + Uri.encode(time)+"'&$orderby=OriginStopTime%2FDepartureTime&$top=8&$format=JSON";

        new HttpAsyncTask().execute();
    }
    class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = TrafficHttpRequest.getData(href1);
            return result;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(final String result) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                        myAdapter = new MyAdapter(list);
                        JSONObject jObject = new JSONObject(result);

                        final int length = jObject.getJSONArray("results").length();

                        if(length!=0){
                            for(int i = 0;i < jObject.getJSONArray("results").length();i++) {
                                HashMap<String, String> item = new HashMap<String, String>();
                                String trainType = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("DailyTrainInfo").getJSONObject("TrainTypeName").getString("Zh_tw");
                                String trainID = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("DailyTrainInfo").getString("TrainNo");
                                String tf_station = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("DestinationStopTime").getJSONObject("StationName").getString("Zh_tw");
                                String departureTime = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("OriginStopTime").getString("DepartureTime");
                                String arrivalTime = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("DestinationStopTime").getString("ArrivalTime");
                                String tra_result = TrafficHttpRequest.getData("http://ptx.transportdata.tw/MOTC/v2/Rail/TRA/DailyTimetable/OD/"+mid1+"/to/"+mid2+"/"+date+"?$filter=OriginStopTime%2FDepartureTime%20ge%20'"
                                        + Uri.encode(arrivalTime)+"'&$orderby=OriginStopTime%2FDepartureTime&$top=1&$format=JSON");
                                JSONObject jObject2 = new JSONObject(tra_result);

                                if(jObject2.getJSONArray("results").length()!=0){
                                    String tra_id = "",tra_des = "",tra_arr = "",tra_type = "",tra_trans = "";
                                    tra_type += jObject2.getJSONArray("results").getJSONObject(0).getJSONObject("DailyTrainInfo").getJSONObject("TrainTypeName").getString("Zh_tw");
                                    tra_id += (jObject2.getJSONArray("results").getJSONObject(0).getJSONObject("DailyTrainInfo").getString("TrainNo"));
                                    tra_des += (jObject2.getJSONArray("results").getJSONObject(0).getJSONObject("OriginStopTime").getString("DepartureTime"));
                                    tra_arr += (jObject2.getJSONArray("results").getJSONObject(0).getJSONObject("DestinationStopTime").getString("ArrivalTime"));
                                    tra_trans += jObject2.getJSONArray("results").getJSONObject(0).getJSONObject("DestinationStopTime").getJSONObject("StationName").getString("Zh_tw");

                                    String tra_result2 = TrafficHttpRequest.getData("http://ptx.transportdata.tw/MOTC/v2/Rail/TRA/DailyTimetable/OD/"+mid2+"/to/"+end+"/"+date+"?$filter=OriginStopTime%2FDepartureTime%20ge%20'"
                                            + Uri.encode(tra_arr)+"'&$orderby=OriginStopTime%2FDepartureTime&$top=1&$format=JSON");
                                    JSONObject jObject3 = new JSONObject(tra_result2);

                                    if(jObject3.getJSONArray("results").length()!=0){
                                        String tra_id2 = "",tra_des2 = "",tra_arr2 = "",tra_type2 = "";
                                        tra_type2 += jObject3.getJSONArray("results").getJSONObject(0).getJSONObject("DailyTrainInfo").getJSONObject("TrainTypeName").getString("Zh_tw");
                                        tra_id2 += (jObject3.getJSONArray("results").getJSONObject(0).getJSONObject("DailyTrainInfo").getString("TrainNo"));
                                        tra_des2 += (jObject3.getJSONArray("results").getJSONObject(0).getJSONObject("OriginStopTime").getString("DepartureTime"));
                                        tra_arr2 += (jObject3.getJSONArray("results").getJSONObject(0).getJSONObject("DestinationStopTime").getString("ArrivalTime"));

                                        item.put("trainType1",trainType);
                                        item.put("trainID",trainID);
                                        item.put("tf_station",tf_station);
                                        item.put("tf_station2",tra_trans);
                                        item.put("departureTime",departureTime);
                                        item.put("arrivalTime",arrivalTime);
                                        item.put("trainType2",tra_type);
                                        item.put("tra_id",tra_id);
                                        item.put("tra_des",tra_des);
                                        item.put("tra_arr",tra_arr);
                                        item.put("trainType3",tra_type2);
                                        item.put("tra_id2",tra_id2);
                                        item.put("tra_des2",tra_des2);
                                        item.put("tra_arr2",tra_arr2);
                                        myAdapter.addItem(item);

                                    }else{
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(Tftratotra_detail2.this, "查無相關車次", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }else{
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(Tftratotra_detail2.this, "查無相關車次", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(length==0){
                                    Toast.makeText(Tftratotra_detail2.this, "查無相關車次", Toast.LENGTH_SHORT).show();
                                }else{
                                    recyclerView.setAdapter(myAdapter);
                                    recyclerView.addItemDecoration(new DividerItemDecoration(Tftratotra_detail2.this, DividerItemDecoration.VERTICAL));
                                    recyclerView.setLayoutManager(new LinearLayoutManager(Tftratotra_detail2.this));
                                }
                            }
                        });
                    } catch (JSONException e) {
                        Toast.makeText(Tftratotra_detail2.this, "無法取得資訊", Toast.LENGTH_SHORT).show();
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tr_rv2,parent,false);
            MyAdapter.ViewHolder viewHolder = new MyAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {

            holder.type1.setText(list.get(position).get("trainType1").contains("自強")?"自強":list.get(position).get("trainType1"));
            holder.type2.setText(list.get(position).get("trainType2").contains("自強")?"自強":list.get(position).get("trainType2"));
            holder.type3.setText(list.get(position).get("trainType3").contains("自強")?"自強":list.get(position).get("trainType3"));
            holder.id.setText(list.get(position).get("trainID"));
            holder.des.setText(list.get(position).get("departureTime"));
            holder.arr.setText(list.get(position).get("arrivalTime"));
            holder.trans.setText(list.get(position).get("tf_station")+"-"+list.get(position).get("tf_station2"));
            holder.tra_id.setText(list.get(position).get("tra_id"));
            holder.tra_des.setText(list.get(position).get("tra_des"));
            holder.tra_arr.setText(list.get(position).get("tra_arr"));
            holder.tra_id2.setText(list.get(position).get("tra_id2"));
            holder.tra_des2.setText(list.get(position).get("tra_des2"));
            holder.tra_arr2.setText(list.get(position).get("tra_arr2"));

        }
        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView type1,type2,type3,id,des,arr,trans,tra_id,tra_des,tra_arr,tra_id2,tra_des2,tra_arr2;

            public ViewHolder(View itemView) {
                super(itemView);
                type1 = (TextView) itemView.findViewById(R.id.type1);
                type2 = (TextView) itemView.findViewById(R.id.type2);
                type3 = (TextView) itemView.findViewById(R.id.type3);
                id = (TextView) itemView.findViewById(R.id.id);
                des = (TextView) itemView.findViewById(R.id.des);
                arr = (TextView) itemView.findViewById(R.id.arr);
                trans = (TextView) itemView.findViewById(R.id.trans);
                tra_id = (TextView) itemView.findViewById(R.id.tra_id);
                tra_des = (TextView) itemView.findViewById(R.id.tra_des);
                tra_arr = (TextView) itemView.findViewById(R.id.tra_arr);
                tra_id2 = (TextView) itemView.findViewById(R.id.tra_id2);
                tra_des2 = (TextView) itemView.findViewById(R.id.tra_des2);
                tra_arr2 = (TextView) itemView.findViewById(R.id.tra_arr2);
            }
        }
    }
}