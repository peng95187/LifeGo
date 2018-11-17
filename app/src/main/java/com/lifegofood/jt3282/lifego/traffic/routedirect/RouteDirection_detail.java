package com.lifegofood.jt3282.lifego.traffic.routedirect;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class RouteDirection_detail extends Activity {

    private MyAdapter myAdapter;
    private RecyclerView recyclerView;
    private String URL = "";
    private int way = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_direction_detail);

        //init
        recyclerView = (RecyclerView) findViewById(R.id.rdd_rv) ;

        URL = getIntent().getStringExtra("URL");
        way = Integer.parseInt(getIntent().getStringExtra("ways")) - 1;
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
                int i = way;

                if (length != 0) {

                    for (int j = 0; j < jObject.getJSONArray("routes").getJSONObject(i).getJSONArray("legs").getJSONObject(0).getJSONArray("steps").length(); j++) {
                        HashMap<String, String> item = new HashMap<String, String>();
                        String combine = "";
                        String route = jObject.getJSONArray("routes").getJSONObject(i).getJSONArray("legs").getJSONObject(0).getJSONArray("steps").getJSONObject(j).getString("html_instructions");
                        Log.i("route",route);

                        if (jObject.getJSONArray("routes").getJSONObject(i).getJSONArray("legs").getJSONObject(0).getJSONArray("steps").getJSONObject(j).has("steps"))
                            for (int k = 0; k < jObject.getJSONArray("routes").getJSONObject(i).getJSONArray("legs").getJSONObject(0).getJSONArray("steps").getJSONObject(j).getJSONArray("steps").length(); k++){
                                String step = jObject.getJSONArray("routes").getJSONObject(i).getJSONArray("legs").getJSONObject(0).getJSONArray("steps").getJSONObject(j).getJSONArray("steps").getJSONObject(k).getString("html_instructions");
                                String duration = jObject.getJSONArray("routes").getJSONObject(i).getJSONArray("legs").getJSONObject(0).getJSONArray("steps").getJSONObject(j).getJSONArray("steps").getJSONObject(k).getJSONObject("duration").getString("text");
                                String distance = jObject.getJSONArray("routes").getJSONObject(i).getJSONArray("legs").getJSONObject(0).getJSONArray("steps").getJSONObject(j).getJSONArray("steps").getJSONObject(k).getJSONObject("distance").getString("text");
                                String stepfinal = "";
                                String [] steps = step.split("<");

                                for (int a = 0; a < steps.length; a++){
                                    if(steps[a].contains(">")){
                                        String [] rex = ("/"+steps[a]+"/").split(">");
                                        steps[a] = rex[1].replaceAll("/","");
                                    }
                                    stepfinal += (steps[a]+" ");

                                }
                                Log.i("duration",duration);
                                Log.i("distance",distance);
                                Log.i("steps", stepfinal);
                                if (k!=0) {
                                    combine += ("&%" + stepfinal + "(約" +duration + "~" + distance + ")");
                                }else{
                                    combine += (stepfinal +  "(約" +duration + "~" + distance + ")");
                                }
                        }
                        item.put("route", route);
                        item.put("ways", combine);
                        myAdapter.addItem(item);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(myAdapter);
                        recyclerView.addItemDecoration(new DividerItemDecoration(RouteDirection_detail.this, DividerItemDecoration.VERTICAL));
                        recyclerView.setLayoutManager(new LinearLayoutManager(RouteDirection_detail.this));
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rdd_rv,parent,false);
            MyAdapter.ViewHolder viewHolder = new MyAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {
            holder.route.setText("step "+(position+1)+" - "+list.get(position).get("route"));
            if (!list.get(position).get("ways").equals("")) {
                holder.way.setVisibility(View.VISIBLE);
                holder.way.setText(list.get(position).get("ways").replaceAll("&%", "\n\n"));
            }
        }

        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView way, route;

            public ViewHolder(View itemView) {
                super(itemView);
                way = (TextView) itemView.findViewById(R.id.ways);
                route = (TextView) itemView.findViewById(R.id.route);
            }
        }
    }
}
