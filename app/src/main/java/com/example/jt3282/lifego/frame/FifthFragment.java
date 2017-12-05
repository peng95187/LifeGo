package com.example.jt3282.lifego.frame;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jt3282.lifego.R;

/**
 * Created by jt3282 on 2017/12/2.
 */

public class FifthFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        View rootView = inflater.inflate(R.layout.fifth_frame, container, false);

        SharedPreferences preferences = this.getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);  ;
        String account = preferences.getString("USER","");
        TextView user = (TextView)rootView.findViewById(R.id.user);
        user.setText(account);

        return rootView;
    }
}
