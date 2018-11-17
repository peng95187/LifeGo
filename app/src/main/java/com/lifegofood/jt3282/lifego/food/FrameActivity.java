package com.lifegofood.jt3282.lifego.food;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.lifegofood.jt3282.lifego.FourChoose;
import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.food.edit.DownloadImg;
import com.lifegofood.jt3282.lifego.food.frame.BottomNavigationViewHelper;
import com.lifegofood.jt3282.lifego.food.frame.FirstFragment;
import com.lifegofood.jt3282.lifego.food.frame.ForthFragment;
import com.lifegofood.jt3282.lifego.food.frame.SecondFragment;
import com.lifegofood.jt3282.lifego.food.frame.ThirdFragment;

import org.json.JSONArray;
import org.json.JSONObject;

public class FrameActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private BottomNavigationView navigation;
    private ViewPager viewPager;
    private String UPLOAD_URL = "http://140.121.199.147/getContent.php";
    private FirstFragment fragment1 = new FirstFragment();
    private SecondFragment fragment2 = new SecondFragment();
    private ThirdFragment fragment3 = new ThirdFragment();
    private ForthFragment fragment4 = new ForthFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);

        //getUserName();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //添加viewPager事件监听（很容易忘）
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(3);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return fragment1;
                    case 1:
                        return fragment2;
                    case 2:
                        return fragment3;
                    case 3:
                        return fragment4;

                }
                return null;
            }

            @Override
            public int getCount() {
                return 4;
            }
        });
    }

    private void getUserName(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);  ;
                String account = sp.getString("USER_NAME","");
                String result = DownloadImg.executeQuery(UPLOAD_URL,"SELECT user_name FROM user_information WHERE account='"+account+"'");

                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonData = jsonArray.getJSONObject(0);

                    String user_name = jsonData.getString("user_name");
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("USER", user_name);

                }catch(Exception e) {
                    Log.e("log_tag", e.toString());
                }
            }
        }).start();

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //点击BottomNavigationView的Item项，切换ViewPager页面
            //menu/navigation.xml里加的android:orderInCategory属性就是下面item.getOrder()取的值
            viewPager.setCurrentItem(item.getOrder());
            return true;
        }

    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //页面滑动的时候，改变BottomNavigationView的Item高亮
        navigation.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(FrameActivity.this,FourChoose.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}

