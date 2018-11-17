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
import org.jsoup.select.Elements;

/**
 * Created by Blue_bell on 2018/5/20.
 */

public class Movie_Detail extends Activity {

    LinearLayout comment_ll,rating_ll,llpre,llcast;
    Tools tools = new Tools();
    public TextView name,cls,detail,rating,cast,date;
    //data
    private String GET_URL = "http://140.121.199.147/getContent.php";
    String dmname = "",detaillink;
    public ImageView link;
    private SharedPreferences sp;
    private String account = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        //detail
        sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        account = sp.getString("USER_NAME","");
        detaillink = getIntent().getStringExtra("detaillink");

        initialView();
        new HttpAsynTask().execute();
        new RatingAsynTask().execute();

        rating = (TextView) findViewById(R.id.rating);
        rating_ll = (LinearLayout) findViewById(R.id.rating_ll);
        //rating
        rating_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account.equals("訪客")||account.equals("")) {
                    Toast.makeText(Movie_Detail.this, "登入才可使用唷~", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(Movie_Detail.this, DMRate.class);
                    if (!dmname.equals("")) {
                        intent.putExtra("dmname", dmname);
                        intent.putExtra("type", "mv");
                    }
                    startActivity(intent);
                }
            }
        });

        llpre = (LinearLayout) findViewById(R.id.llpre);
        llcast = (LinearLayout) findViewById(R.id.llcast);

        //comment
        comment_ll = (LinearLayout) findViewById(R.id.comment_ll);
        comment_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Movie_Detail.this)
                        .setTitle("使用者及觀眾權益")
                        .setMessage("前方可能有暴雷情況發生，請評估")
                        .setPositiveButton("謹慎進入", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Movie_Detail.this,DMCmt.class);
                                if(!dmname.equals("")){
                                    intent.putExtra("dmname",dmname);
                                    intent.putExtra("type","mv");
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

                        Document doc = Jsoup.connect("https://play.google.com"+detaillink+"&hl=zh_TW").get();
                        Elements title = doc.select("div[class=DWPxHb]"); //抓取為tr且有class屬性的所有Tag
                        Elements title2 = doc.select("button[class=lgooh]"); //抓取為tr且有class屬性的所有Tag
                        Elements title3 = doc.select("span[itemprop=director]"); //抓取為tr且有class屬性的所有Tag
                        Elements title4 = doc.select("span[class=UAO9ie]"); //抓取為tr且有class屬性的所有Tag
                        //span itemprop="director"
                        //init
                        String dl = "";
                        String video = "";
                        String de = "";
                        String dt = "";

                        if(!title.isEmpty()) {
                            dl = title.get(0).text();
                        }
                        if(!title2.isEmpty()) {
                            video = title2.get(0).attr("data-trailer-url");
                        }
                        if(!title3.isEmpty()) {
                            de = title3.get(0).text();
                        }
                        if(!title4.isEmpty()) {
                            dt = title4.get(0).text();
                        }

                        final String result = dl;
                        final String finalVideo = video;
                        final String finalDt = dt;
                        final String finalDe = de;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                llpre.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(finalVideo.equals("")){
                                            String urllink = "https://www.youtube.com/results?search_query="+dmname+"+電影預告";
                                            startActivity(new Intent( Intent.ACTION_VIEW, Uri.parse( urllink ) ));
                                        }else {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(finalVideo)));
                                        }
                                    }
                                });
                                llcast.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Movie_Detail.this,DMCast.class);
                                        intent.putExtra("detaillink",detaillink);
                                        startActivity(intent);
                                    }
                                });
                                date.setText("上映時間 : " + finalDt);
                                cast.setText("導演 : "+ finalDe + " >>");
                                detail.setText(result);
                            }
                        });

                    } catch (Exception e) {
                        // TODO Auto-generated catch block

                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }
    void setImg(ImageView img, String ImgURL){
        tools.imageLoading(Movie_Detail.this,ImgURL,img);
    }
    void initialView(){

        String namesplit = getIntent().getStringExtra("name");

        name = (TextView)findViewById(R.id.name);
        date = (TextView)findViewById(R.id.date);
        cast = (TextView)findViewById(R.id.cast);
        cls = (TextView) findViewById(R.id.cls);
        link = (ImageView) findViewById(R.id.link);
        detail = (TextView) findViewById(R.id.detail);

        if(namesplit.contains(". ")){
            String [] newname = namesplit.split(" ");
            name.setText(newname[1]);
            dmname = newname[1];
        }else{
            name.setText(namesplit);
            dmname = namesplit;
        }

        cls.setText(getIntent().getStringExtra("type").equals("")?"無分類":getIntent().getStringExtra("type"));
        link.setTag(getIntent().getStringExtra("link"));
        setImg(link,getIntent().getStringExtra("link"));
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
