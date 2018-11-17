package com.lifegofood.jt3282.lifego.dm;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.lifegofood.jt3282.lifego.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class DMCast extends Activity {

    String detaillink = "";
    TextView detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dmcast);
        //div class="qgaSHe
        detail = (TextView) findViewById(R.id.detail);
        detaillink = getIntent().getStringExtra("detaillink");
        new HttpAsynTask().execute();
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
                        Document doc = Jsoup.connect("https://play.google.com"+detaillink+"&hl=zh_TW").get();
                        Elements title = doc.select("div[class=qgaSHe]"); //抓取為tr且有class屬性的所有Tag

                        //span itemprop="director"
                        for(int i=0;i<title.size();i++) {
                            String tp = title.get(i).select("h2").text();
                            String dt = title.get(i).text().replace(tp,"");
                            r = r + "《"+tp+"》:" + "\n\n" + dt + "\n\n\n";
                        }

                        final String finalR = r;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                detail.setText(finalR);
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
