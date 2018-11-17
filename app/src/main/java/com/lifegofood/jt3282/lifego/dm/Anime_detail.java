package com.lifegofood.jt3282.lifego.dm;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.food.edit.DownloadImg;
import com.lifegofood.jt3282.lifego.food.essay.Tools;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Anime_detail extends Activity {

    String link = "",dmname = "";
    Tools tools = new Tools();
    public TextView name,cls,detail,rating,detail2;
    public ImageView detaillink;
    private String GET_URL = "http://140.121.199.147/getContent.php";
    LinearLayout comment_ll,rating_ll,llpre;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anime_detail);
        initialView();

        sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        final String acc = sp.getString("USER_NAME","");
        link = getIntent().getStringExtra("detaillink");

        new HttpAsynTask().execute();
        new RatingAsynTask().execute();

        rating = (TextView) findViewById(R.id.rating);
        llpre = (LinearLayout) findViewById(R.id.llpre);
        llpre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urllink = "https://www.youtube.com/results?search_query="+dmname+"+anime+pv";
                startActivity(new Intent( Intent.ACTION_VIEW, Uri.parse( urllink ) ));
            }
        });
        rating_ll = (LinearLayout) findViewById(R.id.rating_ll);
        rating_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (acc.equals("訪客")) {
                    Toast.makeText(Anime_detail.this, "登入才可使用唷~", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(Anime_detail.this, DMRate.class);
                    if (!dmname.equals("")) {
                        intent.putExtra("dmname", dmname);
                        intent.putExtra("type", "anime");
                    }
                    startActivity(intent);
                }
            }
        });

        comment_ll = (LinearLayout) findViewById(R.id.comment_ll);
        comment_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Anime_detail.this)
                        .setTitle("使用者及觀眾權益")
                        .setMessage("前方可能有暴雷，請評估")
                        .setPositiveButton("謹慎進入", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Anime_detail.this,DMCmt.class);
                                if(!dmname.equals("")){
                                    intent.putExtra("dmname",dmname);
                                    intent.putExtra("type","anime");
                                }
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("我在想想", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
    }
    void setImg(ImageView img, String ImgURL){
        tools.imageLoading(Anime_detail.this,ImgURL,img);
    }
    void initialView(){

        name = (TextView)findViewById(R.id.name);
        detaillink = (ImageView) findViewById(R.id.link);
        detail = (TextView) findViewById(R.id.detail);
        detail2 = (TextView) findViewById(R.id.detail2);

        name.setText(getIntent().getStringExtra("name"));
        dmname = getIntent().getStringExtra("name");
        detaillink.setTag(getIntent().getStringExtra("link"));
        setImg(detaillink,getIntent().getStringExtra("link"));
    }

    private class HttpAsynTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return null;
        }
        @Override
        protected void onPostExecute(String result) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        String r = "";
                        Document doc = Jsoup.connect(link).get();
                        Element title = doc.select("meta[name=description]").get(0); //抓取為tr且有class屬性的所有Tag
                        Elements title2 = doc.select("ul[class=mov_deta]").get(0).select("li"); //抓取為tr且有class屬性的所有Tag

                        String dl = title.attr("content");
                        for(int i=0;i<title2.size();i++){
                            r = r + title2.get(i).text().replaceAll("　　","") + "\n";
                        }
                        //String date = title.get(1).text();
                        final String result = dl;
                        final String finalR = r;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                detail2.setText(finalR);
                                detail.setText(result);
                            }
                        });

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new HttpAsynTask().execute();
                            }
                        });
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }
    private class RatingAsynTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = DownloadImg.executeQuery(GET_URL, "SELECT avg(average_score) AS avg FROM dm_score WHERE dmname='" + dmname + "' AND dmtype = 'mv'");
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        final int num = jsonArray.length();
                        JSONObject jsonData = jsonArray.getJSONObject(0);
                        final double avg_f = jsonData.getDouble("avg")+0.05;
                        String avg = String.valueOf(avg_f);
                        final String avg_2 = avg.substring(0, avg.indexOf(".") + 2);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(num==0){
                                    rating.setText("尚無評分");
                                }else{
                                    rating.setText("綜合評分 : "+avg_2);
                                }
                            }
                        });

                    }catch(Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                rating.setText("尚無評分");
                            }
                        });
                        Log.e("log_tag", e.toString());
                    }
                }
            }).start();
        }
    }
}