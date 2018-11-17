package com.lifegofood.jt3282.lifego.dm;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.food.essay.Tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class KRDrama_detail extends Activity {

    String link = "";
    Tools tools = new Tools();
    public TextView name,cls,detail;
    public ImageView detaillink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_krdrama);
        initialView();
        link = getIntent().getStringExtra("detaillink");
        new HttpAsynTask().execute();
    }
    void setImg(ImageView img, String ImgURL){
        tools.imageLoading(KRDrama_detail.this,ImgURL,img);
    }
    void initialView(){

        name = (TextView)findViewById(R.id.name);
        cls = (TextView) findViewById(R.id.cls);
        detaillink = (ImageView) findViewById(R.id.link);
        detail = (TextView) findViewById(R.id.detail);

        name.setText(getIntent().getStringExtra("name"));
        cls.setText("("+getIntent().getStringExtra("actor")+")");
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

                        Document doc = Jsoup.connect("https://www.y3600.com"+link).get();
                        Elements title = doc.select("ul.intro"); //抓取為tr且有class屬性的所有Tag0

                        final String d = title.get(0).text();
                        final String[] b = d.split("【剧情简介】");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                detail.setText(b[1]);
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
}
