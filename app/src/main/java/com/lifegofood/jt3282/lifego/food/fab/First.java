package com.lifegofood.jt3282.lifego.food.fab;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.food.RecyclerItemClickListener;
import com.lifegofood.jt3282.lifego.food.edit.DownloadImg;
import com.lifegofood.jt3282.lifego.food.essay.EssayFrame;
import com.lifegofood.jt3282.lifego.food.frame.FirstFragment;
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
 * Created by jt3282 on 2017/12/28.
 */

public class First extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    private String UPLOAD_URL = "http://140.121.199.147/getContent.php";
    private String TIME_URL = "http://140.121.199.147/getTime.php";
    static String[][] im1;
    private int num;
    private MyAdapter itemAdapter;
    RecyclerView listView;
    RecyclerItemClickListener n;
    private JSONArray jsonArray;
    private SwipeRefreshLayout swiper;
    final private DisplayImageOptions options = getSimpleOptions();
    final private ImageLoader imageLoader = ImageLoader.getInstance();
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_frame);

        init();
        loading = (ProgressBar)findViewById(R.id.first_progress);
        swiper = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        //为SwipeRefreshLayout设置监听事件
        swiper.setOnRefreshListener(this);
        //为SwipeRefreshLayout设置刷新时的颜色变化，最多可以设置4种
        swiper.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

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

    @Override
    public void onRefresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                String s = refreshData();
                if(s.equals("ok")){
                    swiper.setRefreshing(false);
                    //swiper.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private String refreshData() {
        if(num!=0)
        listView.setAdapter(null);
        init();
        itemAdapter = new MyAdapter(null);
        listView.removeOnItemTouchListener(n);
        runAPP();
        return "ok";
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
            String result = DownloadImg.executeQuery(UPLOAD_URL,"SELECT * FROM upload_img NATURAL JOIN upload_info WHERE 1 ORDER BY post_time DESC");
            return result;
        }
        @Override
        protected void onPostExecute(final String result) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                            try {
                                jsonArray = new JSONArray(result);
                                num = jsonArray.length();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            final ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
                            itemAdapter = new MyAdapter(list);

                            if(num>0){
                                loadMoreData();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        listView = (RecyclerView) findViewById(R.id.suggest_list);

                                        // listView.setOnScrollListener(new PauseOnScrollListener(imageLoader, false, true));
                                        listView.setLayoutManager(new LinearLayoutManager(First.this));

                                        listView.setAdapter(itemAdapter);
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
                                        n = new RecyclerItemClickListener(First.this, new RecyclerItemClickListener.OnItemClickListener() {
                                            @Override public void onItemClick(View view, int position) {
                                                Intent intent = new Intent();
                                                intent.setClass(First.this, EssayFrame.class);
                                                intent.putExtra("essay_code",list.get(position).get("essay_code"));
                                                startActivity(intent);
                                            }
                                        });

                                        listView.addOnItemTouchListener(n);

                                        FirstFragment.setMaxFlingVelocity(listView,10000);
                               /* listview swiper 衝突
                               listView.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        if (event.getAction() == MotionEvent.ACTION_MOVE) {
                                            if (listView.getFirstVisiblePosition() == 0 &&
                                                    listView.getChildAt(0).getTop() >= listView.getListPaddingTop()) {
                                                swiper.setEnabled(true);
                                            }else swiper.setEnabled(false);
                                        }
                                        return false;
                                    }
                                });*/
                                        loading.setVisibility(View.GONE);
                                    }

                                });
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        loading.setVisibility(View.GONE);
                                        Toast.makeText(First.this, "沒有更多文章", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                    }).start();
        }
    }
    private void loadMoreData() {
        //int count = itemAdapter.getCount();

        if (num > 0) {

            try {
                for (int i = 0 ; i < 20 ; i++) {
                    HashMap<String, String> item = new HashMap<String, String>();
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    String essay_code = jsonData.getString("essay_code");
                    String img1 = jsonData.getString("img1");
                    String img2 = jsonData.getString("img2");
                    String img3 = jsonData.getString("img3");
                    String title = jsonData.getString("title");
                    String location = jsonData.getString("location");
                    String time = jsonData.getString("post_time");

                    item.put("essay_code",essay_code);
                    item.put("time",time);
                    Good(essay_code,item);
                    item.put("location",location);
                    item.put("title",title);
                    item.put("essay_code",essay_code);
                    item.put("img1",img1);
                    item.put("img2",img2);
                    item.put("img3",img3);
                    itemAdapter.addItem(item);
                }
            } catch (Exception e) {
                Log.e("log_tag", e.toString());
            }
        }
    }
    //test
    private void Good(final String essay_code, final HashMap<String, String> item){

        String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT essay_code,count(saver) as goodNum from on_good where essay_code='"+essay_code+"' group by essay_code");

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
    public static String[][] picArray(){
        String[][] arr;
        arr = im1;
        return arr;
    }
    /* ListView 用法
    class ItemAdapter extends BaseAdapter{

        ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
        //图片第一次加载的监听器
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        private String[][] picArrs;

        public ItemAdapter(ArrayList<HashMap<String,String>> newlist) {
            // TODO 自动生成的构造函数存根
            list = newlist;
        }

        private class ViewHolder {
            public TextView title;
            public TextView location;
            public TextView time;
            public ImageView image;
            public ImageView image2;
            public ImageView image3;
        }
        public void addItem(HashMap<String, String> item) {
            list.add(item);
        }
        @Override
        public int getCount() {
            return list==null?0:list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;
            //通过convertView来判断是否已经加载过了，如果没有就加载
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.suggest_list, parent, false);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.sl_title);
                holder.location = (TextView) convertView.findViewById(R.id.sl_address);
                holder.time = (TextView) convertView.findViewById(R.id.sl_time);
                holder.image = (ImageView) convertView.findViewById(R.id.imv1);
                holder.image2 = (ImageView) convertView.findViewById(R.id.imv2);
                holder.image3 = (ImageView) convertView.findViewById(R.id.imv3);
                convertView.setTag(holder);// 给View添加一个格外的数据
            } else {
                holder = (ViewHolder) convertView.getTag(); // 把数据取出来
            }

            /**
             * 显示图片
             * 参数1：图片url
             * 参数2：显示图片的控件
             * 参数3：显示图片的设置
             * 参数4：监听器

            init();
            holder.title.setText(list.get(position).get("title"));
            holder.location.setText(list.get(position).get("location"));
            holder.time.setText(list.get(position).get("time"));
            imageLoader.displayImage(list.get(position).get("img1"), holder.image, options,animateFirstListener);
            imageLoader.displayImage(list.get(position).get("img2"), holder.image2, options,animateFirstListener);
            imageLoader.displayImage(list.get(position).get("img3"), holder.image3, options,animateFirstListener);

            return convertView;
        }
    }
    */
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
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {

            holder.title.setText(list.get(position).get("title"));
            holder.location.setText(list.get(position).get("location"));
            holder.time.setText(list.get(position).get("time"));
            holder.goodNum.setText(list.get(position).get("good")+" 個讚");

            if(!list.get(position).get("img1").equals("http://140.121.199.147/PhotoUpload/noimage")){
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵

            finish();
        }
        return true;
    }
}

