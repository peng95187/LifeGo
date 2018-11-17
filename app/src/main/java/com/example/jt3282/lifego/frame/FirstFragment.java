package com.example.jt3282.lifego.frame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jt3282.lifego.R;
import com.example.jt3282.lifego.edit.DownloadImg;
import com.example.jt3282.lifego.essay.EssayFrame;
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
import org.json.JSONObject;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jt3282 on 2017/12/1.
 */

public class FirstFragment extends Fragment {

    private View rootView ;
    private String UPLOAD_URL = "http://lifego777.000webhostapp.com/getContent.php";
    private String TIME_URL = "http://lifego777.000webhostapp.com/getTime.php";
    static String[][] im1;
    private int num;
    private String[] _code;
    private String[] _title;
    private String[] _location,_time;
    ListView listView;
    final private DisplayImageOptions options = getSimpleOptions();
    final private ImageLoader imageLoader = ImageLoader.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        rootView = inflater.inflate(R.layout.first_frame, container, false);

        runAPP();
        return rootView;
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
        protected String doInBackground(String... params) {
            String result = DownloadImg.executeQuery(UPLOAD_URL,"SELECT * FROM upload_img NATURAL JOIN upload_info WHERE 1");
            return result;
        }
        @Override
        protected void onPostExecute(final String result) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    getbitmap(result);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView = (ListView)rootView.findViewById(R.id.suggest_list);
                            if(num>0){
                                listView.setOnScrollListener(new PauseOnScrollListener(imageLoader, false, true));
                                listView.setAdapter(new ItemAdapter());
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent intent = new Intent();
                                        intent.setClass(getContext(), EssayFrame.class);
                                        intent.putExtra("essay_code",_code[position]);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    });
                }
            }).start();
        }
    }
    private void getbitmap(String result){
        try {

            JSONArray jsonArray = new JSONArray(result);
            im1 = new String[jsonArray.length()][3];
            num = jsonArray.length();
            _code = new String[jsonArray.length()];
            _title = new String[jsonArray.length()];
            _location = new String[jsonArray.length()];
            _time = new String[jsonArray.length()];

            if(num>0)
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                String essay_code = jsonData.getString("essay_code");
                String img1 = jsonData.getString("img1");
                String img2 = jsonData.getString("img2");
                String img3 = jsonData.getString("img3");
                String title = jsonData.getString("title");
                String location = jsonData.getString("location");
                String time = jsonData.getString("post_time");
                String get_time = DownloadImg.executeQuery(TIME_URL,time);

                _time[i] = get_time;
                _location[i] = location;
                _title[i] = title;
                _code[i] = essay_code;
                im1[i][0] = img1;
                im1[i][1] = img2;
                im1[i][2] = img3;
            }

        }catch(Exception e) {
            Log.e("log_tag", e.toString());
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
    public static String[][] picArray(){
        String[][] arr;
        arr = im1;
        return arr;
    }

    class ItemAdapter extends BaseAdapter{

        //图片第一次加载的监听器
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        private String[][] picArrs;

        public ItemAdapter() {
            // TODO 自动生成的构造函数存根
            picArrs = im1;
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
            return picArrs.length;
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
             */
            init();
            holder.title.setText(_title[position]);
            holder.location.setText(_location[position]);
            holder.time.setText(_time[position]);
            imageLoader.displayImage(picArrs[position][0], holder.image, options,animateFirstListener);
            imageLoader.displayImage(picArrs[position][1], holder.image2, options,animateFirstListener);
            imageLoader.displayImage(picArrs[position][2], holder.image3, options,animateFirstListener);

            return convertView;
        }
    }

    private void init(){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getContext())
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

