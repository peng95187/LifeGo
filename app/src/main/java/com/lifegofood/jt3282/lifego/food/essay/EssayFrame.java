package com.lifegofood.jt3282.lifego.food.essay;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.lifegofood.jt3282.lifego.R;


/**
 * Created by jt3282 on 2017/12/17.
 */

public class EssayFrame extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private EssayInfo fragment1 = new EssayInfo();
    private EssayComment fragment2 = new EssayComment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.essay_frame);

        viewPager = (ViewPager) findViewById(R.id.vp_essay);
        viewPager.addOnPageChangeListener(this);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return fragment1;
                    case 1:
                        return fragment2;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
