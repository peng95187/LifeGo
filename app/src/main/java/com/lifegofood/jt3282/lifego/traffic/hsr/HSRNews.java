package com.lifegofood.jt3282.lifego.traffic.hsr;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.lifegofood.jt3282.lifego.traffic.TrafficHttpRequest.getData;

public class HSRNews extends Activity {

    TextView news,update,occurs;
    String href = "",finalResponse = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hsrnews);

        news = (TextView) findViewById(R.id.news);
        update = (TextView) findViewById(R.id.update);
        occurs = (TextView) findViewById(R.id.occurs);

        href = "http://ptx.transportdata.tw/MOTC/v2/Rail/THSR/AlertInfo?$top=30&$format=JSON";

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

                final JSONObject jObject = new JSONObject(result);
                final int length = jObject.getJSONArray("results").length();

                if(length==1){

                    final String Level = jObject.getJSONArray("results").getJSONObject(0).getString("Level");
                    final String OccuredTime = jObject.getJSONArray("results").getJSONObject(0).getString("OccuredTime");
                    final String UpdateTime  = jObject.getJSONArray("results").getJSONObject(0).getString("UpdateTime");

                    if(Level.equals("2")) {
                        final String Title  = jObject.getJSONArray("results").getJSONObject(0).getString("Title");
                        final String Description  = jObject.getJSONArray("results").getJSONObject(0).getString("Description");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                news.setText("有異常狀況\n\n"+Title+"\n\n"+Description);
                                occurs.setText("發生時間 : "+OccuredTime);
                                update.setText("上次更新 : "+UpdateTime);
                            }
                        });
                    }else runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            news.setText("全線正常運行");
                            update.setText("上次更新 : "+UpdateTime);
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(HSRNews.this, "查無資訊", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
