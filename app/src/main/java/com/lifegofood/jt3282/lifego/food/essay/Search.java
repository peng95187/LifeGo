package com.lifegofood.jt3282.lifego.food.essay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.food.circleimageview.CircleImageView;
import com.lifegofood.jt3282.lifego.food.edit.DownloadImg;
import com.lifegofood.jt3282.lifego.food.userpage.UserInfo;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
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
 * Created by jt3282 on 2017/12/23.
 */

public class Search extends Activity {

    private ListView search1,search2;
    private String UPLOAD_URL = "http://140.121.199.147/getContent.php";
    private String TIME_URL = "http://140.121.199.147/getTime.php";
    static String[][] im1;
    private int num;
    private Tools tools=new Tools();
    JSONArray jsonArray;
    View mFooterView;
    private Adapter adapter;
    private ItemAdapter itemAdapter;
    private int num2;
    private boolean isLoading,isLoading2;
    private Button loadMoreButton,loadMoreButton2;
    private int load = 0,load2=0;
    private Handler handler = new Handler();
    private String search;
    private DisplayImageOptions options;
    private TextView tv_search1,tv_search2;
    private ImageLoader imageLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        mFooterView = getLayoutInflater().inflate(R.layout.loadmore, null);
        tv_search1 = (TextView)findViewById(R.id.tv_search1);
        tv_search2 = (TextView)findViewById(R.id.tv_search2);
        options = getSimpleOptions();
        imageLoader = ImageLoader.getInstance();

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

                new RunTask2().execute();
                new RunTask().execute();
            }
        };
        thread.start();
    }
    private class RunTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result;
            search = getIntent().getStringExtra("search");
            init();
            result = DownloadImg.executeQuery(UPLOAD_URL,"SELECT * FROM upload_img NATURAL JOIN upload_info" +
                    " WHERE title LIKE '%"+search+"%'");


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
                            loadMoreButton = (Button) mFooterView.findViewById(R.id.loadMoreButton);

                            search2 = (ListView)findViewById(R.id.search2);
                            if(num>0){
                                tv_search2.setVisibility(View.GONE);
                                search2.setOnScrollListener(new PauseOnScrollListener(imageLoader, false, true));
                                final ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
                                itemAdapter = new ItemAdapter(list);
                                search2.setAdapter(itemAdapter);
                                if(num>10)
                                    search2.addFooterView(mFooterView);
                                loadMoreData(load);

                                search2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent intent = new Intent();
                                        intent.setClass(Search.this, EssayFrame.class);
                                        intent.putExtra("essay_code",list.get(position).get("essay_code"));
                                        startActivity(intent);
                                    }
                                });
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_search2.setText("沒有更多");
                                    }
                                });
                            }

                            loadMoreButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!isLoading) {
                                        //isLoading = true 表示正在加载，加载完毕设置isLoading =false；
                                        isLoading = true;
                                        //如果服务端还有数据，则继续加载更多，否则隐藏底部的加载更多
                                        if (10*(load+1)<=num&&num!=0) {
                                            //Log.i("count","count="+totalItemCount);
                                            //等待2秒之后才加载，模拟网络等待时间为2s
                                            handler.postDelayed(new Runnable() {
                                                public void run() {
                                                    loadMoreData(load);
                                                    if(((load+1)*10)<num)
                                                        load = load + 1;
                                                }
                                            },2000);
                                        }else{
                                            if (search2.getFooterViewsCount()!=0) {
                                                search2.removeFooterView(mFooterView);
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    });
                }
            }).start();
        }
    }

    private void loadMoreData(int load) {
        int count = itemAdapter.getCount();

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

                        item.put("essay_code",essay_code);
                        item.put("time",time);
                        item.put("location",location);
                        item.put("title",title);
                        item.put("essay_code",essay_code);
                        item.put("img1",img1);
                        item.put("img2",img2);
                        item.put("img3",img3);
                        itemAdapter.addItem(item);

                    } else {
                        search2.removeFooterView(mFooterView);
                        isLoading = true;
                    }
                }
                itemAdapter.notifyDataSetChanged();
                isLoading = false;
            } catch (Exception e) {
                Log.e("log_tag", e.toString());
            }
        }
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

    class ItemAdapter extends BaseAdapter {

        //图片第一次加载的监听器
        ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
        //图片第一次加载的监听器
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        private String[][] picArrs;

        public ItemAdapter(ArrayList<HashMap<String,String>> newlist) {
            // TODO 自动生成的构造函数存根
            list = newlist;
        }
        public void addItem(HashMap<String, String> item) {
            list.add(item);
        }

        private class ViewHolder {
            public TextView title;
            public TextView location;
            public TextView time;
            public ImageView image;
            public ImageView image2;
            public ImageView image3;
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

            final ItemAdapter.ViewHolder holder;
            //通过convertView来判断是否已经加载过了，如果没有就加载
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.suggest_list, parent, false);
                holder = new ItemAdapter.ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.sl_title);
                holder.location = (TextView) convertView.findViewById(R.id.sl_address);
                holder.time = (TextView) convertView.findViewById(R.id.sl_time);
                holder.image = (ImageView) convertView.findViewById(R.id.imv1);
                holder.image2 = (ImageView) convertView.findViewById(R.id.imv2);
                holder.image3 = (ImageView) convertView.findViewById(R.id.imv3);
                convertView.setTag(holder);// 给View添加一个格外的数据
            } else {
                holder = (ItemAdapter.ViewHolder) convertView.getTag(); // 把数据取出来
            }

            /**
             * 显示图片
             * 参数1：图片url
             * 参数2：显示图片的控件
             * 参数3：显示图片的设置
             * 参数4：监听器
             */

            holder.title.setText(list.get(position).get("title"));
            holder.location.setText(list.get(position).get("location"));
            holder.time.setText(list.get(position).get("time"));
            imageLoader.displayImage(list.get(position).get("img1"), holder.image, options,animateFirstListener);
            imageLoader.displayImage(list.get(position).get("img2"), holder.image2, options,animateFirstListener);
            imageLoader.displayImage(list.get(position).get("img3"), holder.image3, options,animateFirstListener);

            return convertView;
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
    private class RunTask2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            search = getIntent().getStringExtra("search");
            String result = DownloadImg.executeQuery(UPLOAD_URL,"SELECT * FROM user_information " +
                    "WHERE user_name LIKE '%"+search+"%'");

            return result;
        }

        @Override
        protected void onPostExecute(final String result) {

            try {
                jsonArray = new JSONArray(result);
                num2 = jsonArray.length();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            search1 = (ListView) findViewById(R.id.search1);
            loadMoreButton2 = (Button) mFooterView.findViewById(R.id.loadMoreButton);

            final ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
            adapter = new Adapter(list);
            search1.setAdapter(adapter);
            if(num2>10)
                search1.addFooterView(mFooterView);
            loadMoreData2(load2);

            search1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.setClass(Search.this, UserInfo.class);
                    intent.putExtra("account",list.get(position).get("account"));
                    intent.putExtra("user_name",list.get(position).get("user_name"));
                    startActivity(intent);
                }
            });
            loadMoreButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isLoading2) {
                        //isLoading = true 表示正在加载，加载完毕设置isLoading =false；
                        isLoading2 = true;
                        //如果服务端还有数据，则继续加载更多，否则隐藏底部的加载更多
                        if (10*(load2+1)<=20&&num2!=0) {
                            //Log.i("count","count="+totalItemCount);
                            //等待2秒之后才加载，模拟网络等待时间为2s
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    loadMoreData2(load2);
                                    if(((load2+1)*10)<num2)
                                        load2 = load2 + 1;
                                }
                            },1000);
                        }else{
                            if (search1.getFooterViewsCount()!=0) {
                                search1.removeFooterView(mFooterView);
                            }
                        }
                    }
                }
            });

        }
    }
    private void loadMoreData2(int load){
        int count = adapter.getCount();

        if(num2>0){
            tv_search1.setVisibility(View.GONE);
            try {
                for (int i = 0 +(10 * load); i < 10 * (load + 1); i++) {
                    if (count+i-(10 * load)<num2) {
                        HashMap<String, String> item = new HashMap<String, String>();
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        String account = jsonData.getString("account");
                        String user_name = jsonData.getString("user_name");
                        String icon = jsonData.getString("user_icon");

                        item.put("account",account);
                        item.put("user_name",user_name);
                        item.put("user_icon",icon);
                        adapter.addItem(item);

                    }else{
                        search1.removeFooterView(mFooterView);
                        isLoading2 = true;
                    }
                }
                adapter.notifyDataSetChanged();
                isLoading2 = false;
            }catch(Exception e) {
                Log.e("log_tag", e.toString());
            }
        }else tv_search1.setText("沒有更多");

    }

    class Adapter extends BaseAdapter {

        ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();

        public Adapter(ArrayList<HashMap<String,String>> newlist){
            list = newlist;
        }

        @Override
        public int getCount() {
            return list==null?0:list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        public void addItem(HashMap<String, String> item) {
            list.add(item);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            public TextView user_name;
            public CircleImageView icon;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Adapter.ViewHolder holder;
            //通过convertView来判断是否已经加载过了，如果没有就加载
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.support_rank, parent, false);
                holder = new Adapter.ViewHolder();
                holder.user_name = (TextView) convertView.findViewById(R.id.sr_user);
                holder.icon = (CircleImageView)convertView.findViewById(R.id.sr_userIcon);

                convertView.setTag(holder);// 给View添加一个格外的数据
            } else {
                holder = (Adapter.ViewHolder) convertView.getTag(); // 把数据取出来
            }

            holder.icon.setTag(list.get(position).get("user_icon"));
            userIcon(holder.icon,list.get(position).get("user_icon"));
            holder.user_name.setText(list.get(position).get("user_name"));
            //holder.time.setText();
            return convertView;
        }

        private void userIcon(CircleImageView icon,String iconURL){
            if(iconURL.equals("user_icon")){
                icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_user));
            }else{
                tools.imageLoading(Search.this,iconURL,icon);
            }
        }
    }
}


