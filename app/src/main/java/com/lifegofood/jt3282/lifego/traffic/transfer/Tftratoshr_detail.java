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

public class Tftratoshr_detail extends Activity {

    String href1 = "",HSR = "",date = "",time = "",start = "",mid = "",end = "";

    MyAdapter myAdapter;
    RecyclerView recyclerView;
    String [] TransTRA = {"1006","1008","1011","2214","1304","1324","5102","1242"};
    String [] TransHSR = {"0990","1000","1010","1030","1035","1040","1060","1070"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_detail);

        recyclerView = (RecyclerView) findViewById(R.id.td_rv);

        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");
        start = getIntent().getStringExtra("start");
        mid   = getIntent().getStringExtra("mid");
        end   = getIntent().getStringExtra("end");

        href1 = "http://ptx.transportdata.tw/MOTC/v2/Rail/TRA/DailyTimetable/OD/"+start+"/to/"+mid+"/"+date+"?$filter=OriginStopTime%2FDepartureTime%20ge%20'"
                + Uri.encode(time)+"'&$orderby=OriginStopTime%2FDepartureTime&$top=8&$format=JSON";

        for(int i=0;i<TransTRA.length;i++){
            if(mid.equals(TransTRA[i])){
                HSR = TransHSR[i];
            }
        }

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
                                String hsr_result = TrafficHttpRequest.getData("http://ptx.transportdata.tw/MOTC/v2/Rail/THSR/DailyTimetable/OD/"+HSR+"/to/"+end+"/"+date+"?$filter=OriginStopTime%2FDepartureTime%20ge%20'"+Uri.encode(arrivalTime)+"'&$orderby=OriginStopTime%2FDepartureTime&$top=4&$format=JSON");
                                JSONObject jObject2 = new JSONObject(hsr_result);
                                if(jObject2.getJSONArray("results").length()!=0){
                                    String hsr_id = "",hsr_des = "",hsr_arr = "";
                                    for (int j = 0;j < jObject2.getJSONArray("results").length();j++){
                                        hsr_id += (jObject2.getJSONArray("results").getJSONObject(j).getJSONObject("DailyTrainInfo").getString("TrainNo")+(j==jObject2.getJSONArray("results").length()-1?"":"\n"));
                                        hsr_des += (jObject2.getJSONArray("results").getJSONObject(j).getJSONObject("OriginStopTime").getString("DepartureTime")+(j==jObject2.getJSONArray("results").length()-1?"":"\n"));
                                        hsr_arr += (jObject2.getJSONArray("results").getJSONObject(j).getJSONObject("DestinationStopTime").getString("ArrivalTime")+(j==jObject2.getJSONArray("results").length()-1?"":"\n"));
                                    }

                                    item.put("trainType",trainType);
                                    item.put("trainID",trainID);
                                    item.put("tf_station",tf_station);
                                    item.put("departureTime",departureTime);
                                    item.put("arrivalTime",arrivalTime);
                                    item.put("hsr_id",hsr_id);
                                    item.put("hsr_des",hsr_des);
                                    item.put("hsr_arr",hsr_arr);
                                    myAdapter.addItem(item);
                                }else{
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(Tftratoshr_detail.this, "查無相關車次", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(length==0){
                                    Toast.makeText(Tftratoshr_detail.this, "查無相關車次", Toast.LENGTH_SHORT).show();
                                }else{
                                    recyclerView.setAdapter(myAdapter);
                                    recyclerView.addItemDecoration(new DividerItemDecoration(Tftratoshr_detail.this, DividerItemDecoration.VERTICAL));
                                    recyclerView.setLayoutManager(new LinearLayoutManager(Tftratoshr_detail.this));
                                }
                            }
                        });
                    } catch (JSONException e) {
                        Toast.makeText(Tftratoshr_detail.this, "無法取得資訊", Toast.LENGTH_SHORT).show();
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.td_rv,parent,false);
            MyAdapter.ViewHolder viewHolder = new MyAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {

            holder.type.setText(list.get(position).get("trainType").contains("自強")?"自強":list.get(position).get("trainType"));
            holder.id.setText(list.get(position).get("trainID"));
            holder.des.setText(list.get(position).get("departureTime"));
            holder.arr.setText(list.get(position).get("arrivalTime"));
            holder.trans.setText(list.get(position).get("tf_station"));
            holder.hsr_id.setText(list.get(position).get("hsr_id"));
            holder.hsr_des.setText(list.get(position).get("hsr_des"));
            holder.hsr_arr.setText(list.get(position).get("hsr_arr"));

        }
        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView num,type,id,des,arr,trans,hsr_id,hsr_des,hsr_arr;

            public ViewHolder(View itemView) {
                super(itemView);
                type = (TextView) itemView.findViewById(R.id.type);
                id = (TextView) itemView.findViewById(R.id.id);
                des = (TextView) itemView.findViewById(R.id.des);
                arr = (TextView) itemView.findViewById(R.id.arr);
                trans = (TextView) itemView.findViewById(R.id.trans);
                hsr_id = (TextView) itemView.findViewById(R.id.hsr_id);
                hsr_des = (TextView) itemView.findViewById(R.id.hsr_des);
                hsr_arr = (TextView) itemView.findViewById(R.id.hsr_arr);
            }
        }
    }
}
