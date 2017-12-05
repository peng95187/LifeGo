package com.example.jt3282.lifego.frame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jt3282.lifego.R;

/**
 * Created by jt3282 on 2017/12/1.
 */

public class ForthFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        View rootView = inflater.inflate(R.layout.fouth_frame, container, false);
        return rootView;
    }
}
