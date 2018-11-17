package com.lifegofood.jt3282.lifego.food.userpage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.food.LoadMoreForRecyclerView;
import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.food.RecyclerItemClickListener;
import com.lifegofood.jt3282.lifego.food.edit.DownloadImg;
import com.lifegofood.jt3282.lifego.food.essay.EssayFrame;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jt3282 on 2017/12/19.
 */

public class EssayOwn extends Activity{

    private String UPLOAD_URL = "http://140.121.199.147/getContent.php";
    private String TIME_URL = "http://140.121.199.147/getTime.php";
    static String[][] im1;
    private int num,load=0;
    private MyAdapter itemAdapter;
    private JSONArray jsonArray;
    private String account,key;
    RecyclerView listView;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    ProgressBar loading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.essay_own);
        account = getIntent().getStringExtra("account");
        options = getSimpleOptions();
        imageLoader = ImageLoader.getInstance();
        loading = (ProgressBar)findViewById(R.id.first_progress);
        init();
        runAPP();
    }
    private void runAPP(){
        Thread thread = new Thread() {
            @Override
            public void run() {
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                        .detectDiskReads()
                        .detectDiskWrites()
                        .detectNetwork()
                        .penaltyLog()
                        .build());
                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                        .detectLeakedSqlLiteObjects()
                        .penaltyLog()
                        .penaltyDeath()
                        .build());

                new RunTask().execute();
            }
        };
        thread.start();
    }
    private class RunTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loading.setVisibility(View.VISIBLE);
                }
            });
        }
        @Override
        protected String doInBackground(String... params) {
            String result;
            key = getIntent().getStringExtra("key");
            account = getIntent().getStringExtra("account");
            if(key.equals("no")){
                result = DownloadImg.executeQuery(UPLOAD_URL,"SELECT * FROM upload_img NATURAL JOIN upload_info WHERE account=" +
                        "'"+account+"' ORDER BY post_time DESC");
            }else if(key.equals("yes")){
                result = DownloadImg.executeQuery(UPLOAD_URL,"SELECT * FROM upload_img NATURAL JOIN upload_info NATURAL" +
                        " JOIN on_good WHERE saver='"+account+"' and save='1' ORDER BY post_time DESC");
            }else {
                result = DownloadImg.executeQuery(UPLOAD_URL,"SELECT * FROM upload_img NATURAL JOIN upload_info" +
                        " WHERE location LIKE '%"+key+"%' ORDER BY post_time DESC");
            }

            return result;
        }
        @Override
        protected void onPostExecute(final String result) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                jsonArray = new JSONArray(result);
                                num = jsonArray.length();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            listView = (RecyclerView) findViewById(R.id.suggest_list);
                            if(num>0){
                                // listView.setOnScrollListener(new PauseOnScrollListener(imageLoader, false, true));
                                final ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
                                itemAdapter = new MyAdapter(list);
                                listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                        switch (newState){
                                            case RecyclerView.SCROLL_STATE_DRAGGING:
                                                //正在滑动
                                                imageLoader.pause();
                                                break;
                                            case RecyclerView.SCROLL_STATE_IDLE:
                                                //滑动停止
                                                imageLoader.resume();
                                                break;
                                        }
                                    }
                                });
                                listView.addOnItemTouchListener(new RecyclerItemClickListener(EssayOwn.this, new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override public void onItemClick(View view, int position) {
                                        Intent intent = new Intent();
                                        intent.setClass(EssayOwn.this, EssayFrame.class);
                                        intent.putExtra("essay_code",list.get(position).get("essay_code"));
                                        startActivity(intent);
                                    }
                                }));
                                listView.setLayoutManager(new LinearLayoutManager(EssayOwn.this));
                                listView.setAdapter(itemAdapter);
                                loadMoreData(load);
                                load=1;
                                new LoadMoreForRecyclerView(listView, new LoadMoreForRecyclerView.LoadMoreListener() {
                                    @Override
                                    public void loadListener() {
                                        if((10 * load)<num){
                                            loadMoreData(load);
                                            load+=1;
                                        }
                                    }
                                });

                            }else{
                                Toast.makeText(EssayOwn.this, "沒有更多文章", Toast.LENGTH_SHORT).show();
                            }
                            loading.setVisibility(View.GONE);
                        }
                    });
                }
            }).start();
        }
    }

    private void loadMoreData(int load) {
        int count = itemAdapter.getItemCount();

        if (num > 0) {

            try {
                for (int i = 0 + (10 * load); i < 10 * (load + 1); i++) {
                    if (count + i - (10 * load) < num) {
                        HashMap<String, String> item = new HashMap<String, String>();
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        String essay_code = jsonData.getString("essay_code");
                        String img1 = jsonData.getString("img1");
                        String img2 = jsonData.getString("img2");
                        String img3 = jsonData.getString("img3");
                        String title = jsonData.getString("title");
                        String location = jsonData.getString("location");
                        String time = jsonData.getString("post_time");
                        Good(essay_code,item);
                        item.put("essay_code",essay_code);
                        item.put("time",time);
                        item.put("location",location);
                        item.put("title",title);
                        item.put("essay_code",essay_code);
                        item.put("img1",img1);
                        item.put("img2",img2);
                        item.put("img3",img3);
                        itemAdapter.addItem(item);
                    }
                }
                itemAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Log.e("log_tag", e.toString());
            }
        }
    }

    private void Good(final String essay_code, final HashMap<String, String> item){
        class GoodTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT essay_code,count(saver) as goodNum from on_good where essay_code='"+essay_code+"' group by essay_code");
                return result;
            }

            @Override
            protected void onPostExecute(final String result) {

                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonData = jsonArray.getJSONObject(0);
                    String good = jsonData.getString("goodNum");
                    item.put("good",good);
                }catch (Exception e) {
                    item.put("good","0");
                    Log.e("log_tag", e.toString());
                }
            }
        }
        new GoodTask().execute();
    }

    //test

    private DisplayImageOptions getSimpleOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_error)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                .build();//构建完成
        return options;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
        //图片第一次加载的监听器
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        public MyAdapter(ArrayList<HashMap<String,String>> newlist) {
            // TODO 自动生成的构造函数存根
            list = newlist;
        }
        public void addItem(HashMap<String, String> item) {
            list.add(item);
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggest_list,parent,false);
            MyAdapter.ViewHolder viewHolder = new MyAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {

            holder.title.setText(list.get(position).get("title"));
            holder.location.setText(list.get(position).get("location"));
            holder.time.setText(list.get(position).get("time"));
            if(list.get(position).get("good")!=null)
                holder.goodNum.setText(list.get(position).get("good")+" 個讚");
            if(!list.get(position).get("img1").equals("http:/140.121.199.147/PhotoUpload/noimage")){
                imageLoader.displayImage(list.get(position).get("img1"), holder.image, options,animateFirstListener);
            }else holder.image.setImageBitmap(null);
            if(!list.get(position).get("img2").equals("http://140.121.199.147/PhotoUpload/noimage")) {
                imageLoader.displayImage(list.get(position).get("img2"), holder.image2, options, animateFirstListener);
            }else holder.image2.setImageBitmap(null);
            if(!list.get(position).get("img3").equals("http://140.121.199.147/PhotoUpload/noimage")) {
                imageLoader.displayImage(list.get(position).get("img3"), holder.image3, options, animateFirstListener);
            }else holder.image3.setImageBitmap(null);
        }
        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView title;
            public TextView location;
            public TextView time;
            public ImageView image;
            public ImageView image2;
            public ImageView image3;
            public TextView goodNum;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.sl_title);
                location = (TextView) itemView.findViewById(R.id.sl_address);
                time = (TextView) itemView.findViewById(R.id.sl_time);
                image = (ImageView) itemView.findViewById(R.id.imv1);
                image2 = (ImageView) itemView.findViewById(R.id.imv2);
                image3 = (ImageView) itemView.findViewById(R.id.imv3);
                goodNum = (TextView) itemView.findViewById(R.id.goodNum);
            }
        }
    }


    private void init(){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .memoryCacheExtraOptions(480, 800) //保存每個緩存圖片的最大寬高
                .threadPriority(Thread.NORM_PRIORITY - 2) //線池中的緩存數
                .denyCacheImageMultipleSizesInMemory() //禁止緩存多張圖片
                .memoryCache(new FIFOLimitedMemoryCache(2 * 1024 * 1024))//缓存策略
//                .memoryCacheSize(50 * 1024 * 1024) //設置內存緩存的大小
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //缓存文件名的保存方式
//                .diskCacheSize(200 * 1024 * 1024) //緩存大小
                .tasksProcessingOrder(QueueProcessingType.LIFO) //工作序列
                .diskCacheFileCount(200) //緩存的文件數量
                .build();
        if (!ImageLoader.getInstance().isInited()) {//偵測如果imagloader已經init，就不再init
            ImageLoader.getInstance().init(config);
        }
    }
    /**
     * 图片加载第一次显示监听器
     * @author Administrator
     *
     */
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                // 是否第一次显示
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    // 图片淡入效果
                    FadeInBitmapDisplayer.animate(imageView, 200);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}

