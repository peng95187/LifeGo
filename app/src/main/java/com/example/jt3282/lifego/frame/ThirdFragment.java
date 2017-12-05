package com.example.jt3282.lifego.frame;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jt3282.lifego.R;
import com.example.jt3282.lifego.edit.EditActivity;

/**
 * Created by jt3282 on 2017/12/1.
 */

public class ThirdFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        View rootView = inflater.inflate(R.layout.third_frame, container, false);

        FloatingActionButton floatingActionButton = (FloatingActionButton)rootView.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), EditActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
