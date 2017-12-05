package com.example.jt3282.lifego;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.jt3282.lifego.frame.BottomNavigationViewHelper;
import com.example.jt3282.lifego.frame.FifthFragment;
import com.example.jt3282.lifego.frame.FirstFragment;
import com.example.jt3282.lifego.frame.ForthFragment;
import com.example.jt3282.lifego.frame.SecondFragment;
import com.example.jt3282.lifego.frame.ThirdFragment;

public class FrameActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private BottomNavigationView navigation;
    private ViewPager viewPager;

    private FirstFragment fragment1 = new FirstFragment();
    private SecondFragment fragment2 = new SecondFragment();
    private ThirdFragment fragment3 = new ThirdFragment();
    private ForthFragment fragment4 = new ForthFragment();
    private FifthFragment fragment5 = new FifthFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //添加viewPager事件监听（很容易忘）
        viewPager.addOnPageChangeListener(this);
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
                    case 4:
                        return fragment5;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 5;
            }
        });
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
}

