package com.lifegofood.jt3282.lifego.traffic.hsr;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lifegofood.jt3282.lifego.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.lifegofood.jt3282.lifego.traffic.TrafficHttpRequest.getData;

public class HSRPriceSearch extends Activity {

    String href,finalResponse="";
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hsrprice_search);

        String start = getIntent().getStringExtra("stationID");
        String end = getIntent().getStringExtra("stationID2");
        String startName = getIntent().getStringExtra("station");
        String endName = getIntent().getStringExtra("station2");

        textView = (TextView) findViewById(R.id.station) ;
        recyclerView = (RecyclerView) findViewById(R.id.hsrprice_rv) ;

        textView.setText(startName+" 開往 "+endName);
        href = "http://ptx.transportdata.tw/MOTC/v2/Rail/THSR/ODFare/"+start+"/to/"+end+"?$top=30&$format=JSON";

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
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                myAdapter = new MyAdapter(list);
                JSONObject jObject = new JSONObject(result);

                if(jObject!=null)
                    for (int i = 0; i < jObject.getJSONArray("results").getJSONObject(0).getJSONArray("Fares").length(); i++) {
                        HashMap<String, String> item = new HashMap<String, String>();

                        String classes = jObject.getJSONArray("results").getJSONObject(0).getJSONArray("Fares").getJSONObject(i).getString("TicketType");
                        String price = jObject.getJSONArray("results").getJSONObject(0).getJSONArray("Fares").getJSONObject(i).getString("Price");

                        item.put("classes",classes);
                        item.put("price",price);

                        myAdapter.addItem(item);
                    }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(myAdapter);
                        recyclerView.addItemDecoration(new DividerItemDecoration(HSRPriceSearch.this, DividerItemDecoration.VERTICAL));
                        recyclerView.setLayoutManager(new LinearLayoutManager(HSRPriceSearch.this));
                    }
                });

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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trapricesearch_rv,parent,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {

            holder.price.setText(list.get(position).get("price"));
            holder.classes.setText(list.get(position).get("classes"));

        }

        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView price,classes;

            public ViewHolder(View itemView) {
                super(itemView);

                price = (TextView)itemView.findViewById(R.id.price);
                classes = (TextView)itemView.findViewById(R.id.classes);
            }
        }
    }
}
