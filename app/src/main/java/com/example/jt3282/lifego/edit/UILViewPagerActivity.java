package com.example.jt3282.lifego.edit;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jt3282.lifego.R;
import com.example.jt3282.lifego.frame.FirstFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by jt3282 on 2017/12/11.
 */

public class UILViewPagerActivity extends Activity {

    ViewPager pager;
    DisplayImageOptions options;
    ImageLoader imageLoader;

    private static final String STATE_POSITION = "STATE_POSITION";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO 自动生成的方法存根
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager_layout);

        int pagerPosition = 0;

        // 如果之前有保存用户数据
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        options = getSimpleOptions();
        imageLoader = ImageLoader.getInstance();

        pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new ImagePagerAdapter(FirstFragment.picArray()));
        pager.setCurrentItem(pagerPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //
        outState.putInt(STATE_POSITION, pager.getCurrentItem());
    }


    /**
     * 设置常用的设置项
     *
     * @return
     */
    private DisplayImageOptions getSimpleOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_launcher)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_error) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 设置图片以如何的编码方式显示
                .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                .build();// 构建完成
        return options;
    }

    private class ImagePagerAdapter extends PagerAdapter {

        private String[][] images;
        private LayoutInflater inflater;

        ImagePagerAdapter(String[][] images) {
            this.images = images;
            inflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View viewLayout = inflater.inflate(R.layout.viewpager_item, view,
                    false);
            ImageView imageView = (ImageView) viewLayout
                    .findViewById(R.id.image);
            final ProgressBar spinner = (ProgressBar) viewLayout
                    .findViewById(R.id.loading);

            imageLoader.displayImage(images[position][0], imageView, options,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            spinner.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view,
                                                    FailReason failReason) {
                            String message = null;
                            switch (failReason.getType()) { // 获取图片失败类型
                                case IO_ERROR: // 文件I/O错误
                                    message = "Input/Output error";
                                    break;
                                case DECODING_ERROR: // 解码错误
                                    message = "Image can't be decoded";
                                    break;
                                case NETWORK_DENIED: // 网络延迟
                                    message = "Downloads are denied";
                                    break;
                                case OUT_OF_MEMORY: // 内存不足
                                    message = "Out Of Memory error";
                                    break;
                                case UNKNOWN: // 原因不明
                                    message = "Unknown error";
                                    break;
                            }
                            Toast.makeText(getApplicationContext(), message,
                                    Toast.LENGTH_SHORT).show();

                            spinner.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri,
                                                      View view, Bitmap loadedImage) {
                            spinner.setVisibility(View.GONE); // 不显示圆形进度条
                        }
                    });

            ((ViewPager) view).addView(viewLayout, 0); // 将图片增加到ViewPager
            return viewLayout;
        }

        @Override
        public void finishUpdate(View container) {
        }
    }
}
