package com.example.jt3282.lifego.frame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jt3282.lifego.R;
import com.example.jt3282.lifego.edit.EditActivity;

/**
 * Created by jt3282 on 2017/12/1.
 */

public class SecondFragment extends Fragment {

    private String account;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        View rootView = inflater.inflate(R.layout.second_frame, container, false);

        FloatingActionButton floatingActionButton = (FloatingActionButton)rootView.findViewById(R.id.floatingActionButton);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);  ;
        account = preferences.getString("USER","");

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(account.equals("訪客")){
                    Toast.makeText(getContext(),"請先登入", Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent();
                    intent.putExtra("account",account);
                    intent.setClass(getContext(), EditActivity.class);
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }
}
