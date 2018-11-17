package com.lifegofood.jt3282.lifego.food.essay;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.lifegofood.jt3282.lifego.R;


/**
 * Created by jt3282 on 2018/1/6.
 */

public class ViewImage extends Activity{

    private ImageView image;
    private Tools tools = new Tools();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewimage);

        image = (ImageView)findViewById(R.id.image);
        String url = getIntent().getStringExtra("image");

        image.setTag(url);
        tools.imageLoading(ViewImage.this,url,image);

    }
}
