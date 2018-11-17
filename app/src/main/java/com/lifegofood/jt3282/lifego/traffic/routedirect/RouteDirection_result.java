package com.lifegofood.jt3282.lifego.traffic.routedirect;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lifegofood.jt3282.lifego.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class RouteDirection_result extends Activity {

    private String URL = "";
    private String startLoc = "", endLoc = "";
    private TextView start, end;
    MyAdapter myAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_direction_rst);

        startLoc = getIntent().getStringExtra("startLoc");
        endLoc = getIntent().getStringExtra("endLoc");
        URL = "https://maps.googleapis.com/maps/api/directions/json?origin="+startLoc+"&destination="+ Uri.encode(endLoc)+"&mode=transit&alternatives=true&key=AIzaSyAQT0-QNjRf5PTjhR-tyrN9T_510vcujgc&language=zh-tw";

        //init
        start = (TextView) findViewById(R.id.startloc);
        end = (TextView) findViewById(R.id.endloc);
        recyclerView = (RecyclerView) findViewById(R.id.rdr_rv);

        start.setText(startLoc);
        end.setText(endLoc);
        new HttpAsyncTask().execute();
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls) {
            String result = GET(URL);
            return result;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            try{
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                myAdapter = null;
                myAdapter = new MyAdapter(list);
                JSONObject jObject = new JSONObject(result);
                int length = jObject.getJSONArray("routes").length();

                if (length != 0) {
                    for (int i = 0; i < jObject.getJSONArray("routes").length(); i++) {
                        HashMap<String, String> item = new HashMap<String, String>();
                        final String start_address = jObject.getJSONArray("routes").getJSONObject(i).getJSONArray("legs").getJSONObject(0).getString("start_address");
                        final String end_address = jObject.getJSONArray("routes").getJSONObject(i).getJSONArray("legs").getJSONObject(0).getString("end_address");
                        String duration = jObject.getJSONArray("routes").getJSONObject(i).getJSONArray("legs").getJSONObject(0).getJSONObject("duration").getString("text");
                        String distance = jObject.getJSONArray("routes").getJSONObject(i).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getString("text");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                start.setText(start_address);
                                end.setText(end_address);
                            }
                        });
                        item.put("ways", String.valueOf(i + 1));
                        item.put("duration", duration);
                        item.put("distance", distance);
                        myAdapter.addItem(item);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(myAdapter);
                        recyclerView.addItemDecoration(new DividerItemDecoration(RouteDirection_result.this, DividerItemDecoration.VERTICAL));
                        recyclerView.setLayoutManager(new LinearLayoutManager(RouteDirection_result.this));
                    }
                });

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        private String GET(String url) {

            InputStream inputStream;
            String result = "";

            try {
                // create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                // make GET request to the given URL
                HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
                // receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();
                // convert inputstream to string

                if (inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return result;
        }

        private String convertInputStreamToString(InputStream inputStream) throws IOException {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

            String line;
            String result = "";

            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            inputStream.close();
            return result;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rdr_rv,parent,false);
            MyAdapter.ViewHolder viewHolder = new MyAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {

            holder.way.setText("方案 "+list.get(position).get("ways")+ " (約" +
                    list.get(position).get("duration") + "~" + list.get(position).get("distance") + ")");
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RouteDirection_result.this,RouteDirection_detail.class);
                    intent.putExtra("ways",list.get(position).get("ways"));
                    intent.putExtra("URL",URL);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView way;
            public LinearLayout ll;

            public ViewHolder(View itemView) {
                super(itemView);
                way = (TextView) itemView.findViewById(R.id.ways);
                ll = (LinearLayout) itemView.findViewById(R.id.ll);
            }
        }
    }
}
