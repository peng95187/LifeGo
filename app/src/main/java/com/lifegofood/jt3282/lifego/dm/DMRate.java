package com.lifegofood.jt3282.lifego.dm;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.food.edit.DownloadImg;

import org.json.JSONArray;
import org.json.JSONObject;

public class DMRate extends Activity {

    private String GET_URL = "http://140.121.199.147/getContent.php";
    private String RATE_URL = "http://140.121.199.147/dmrating.php";
    String account = "",dmname = "",dmtype = "";
    RatingBar cast_rb,plot_rb,se_rb,sd_rb,ad_rb;
    Button send;
    public static DMRate instance;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dmrate);
        sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        account = sp.getString("USER_NAME","");
        dmname = getIntent().getStringExtra("dmname");
        dmtype = getIntent().getStringExtra("type");

        instance = this;

        cast_rb = (RatingBar) findViewById(R.id.cast);
        plot_rb = (RatingBar) findViewById(R.id.plot);
        se_rb = (RatingBar) findViewById(R.id.se);
        sd_rb = (RatingBar) findViewById(R.id.sd);
        ad_rb = (RatingBar) findViewById(R.id.ad);
        send = (Button) findViewById(R.id.send);

        new RatingAsynTask().execute();

    }
    private class RatingAsynTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = DownloadImg.executeQuery(GET_URL, "SELECT * FROM dm_score WHERE dmname='" + dmname + "' AND dmtype = '"+getIntent().getStringExtra("type")+"' "
                    +"AND account='"+account+"'");
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        int num = jsonArray.length();

                        if(num==0){

                        }else{
                            JSONObject jsonData = jsonArray.getJSONObject(0);
                            final int cast = jsonData.getInt("cast");
                            final int plot = jsonData.getInt("plot");
                            final int se = jsonData.getInt("se");
                            final int rd = jsonData.getInt("rd");
                            final int ad = jsonData.getInt("ad");

                            cast_rb.setRating(cast);
                            plot_rb.setRating(plot);
                            se_rb.setRating(se);
                            sd_rb.setRating(rd);
                            ad_rb.setRating(ad);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    send.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            float avg_score = (cast_rb.getRating()+plot_rb.getRating()+se_rb.getRating()+sd_rb.getRating()+
                                                    ad_rb.getRating())/5;
                                            DMComment.updateRate(RATE_URL,account,dmtype,dmname,String.valueOf(cast_rb.getRating()),
                                                    String.valueOf(plot_rb.getRating()),String.valueOf(se_rb.getRating()),String.valueOf(ad_rb.getRating()),
                                                    String.valueOf(sd_rb.getRating()), String.valueOf(avg_score));
                                            Toast.makeText(DMRate.this, "已送出修改", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                                }
                            });
                        }
                    }catch(Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                send.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        float avg_score = (cast_rb.getRating()+plot_rb.getRating()+se_rb.getRating()+sd_rb.getRating()+
                                                ad_rb.getRating())/5;
                                        DMComment.uploadRate(RATE_URL,account,dmtype,dmname,String.valueOf(cast_rb.getRating()),
                                                String.valueOf(plot_rb.getRating()),String.valueOf(se_rb.getRating()) ,String.valueOf(ad_rb.getRating())
                                                ,String.valueOf(sd_rb.getRating()), String.valueOf(avg_score));
                                        Toast.makeText(DMRate.this, "已送出評分", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        });
                        Log.e("log_tag", e.toString());
                    }
                }
            }).start();
        }
    }
}
