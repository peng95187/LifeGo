package com.example.jt3282.lifego.frame;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.jt3282.lifego.R;
import com.example.jt3282.lifego.edit.DownloadImg;

/**
 * Created by jt3282 on 2017/12/1.
 */

public class ThirdFragment extends Fragment {

    Spinner rankType;
    ListView userlist;
    String[] Type = {"人氣排行","用戶排行","公信排行" };
    private String UPLOAD_URL = "http://lifego777.000webhostapp.com/getContent.php";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        View rootView = inflater.inflate(R.layout.third_frame, container, false);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Type);
        rankType = (Spinner)rootView.findViewById(R.id.spinner1);
        rankType.setAdapter(adapter);
        rankType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }
    private class RunTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = DownloadImg.executeQuery(UPLOAD_URL, "SELECT user_name,count(concerner) FROM user_concern GROUP BY account ORDER BY 2 DESC");
            return result;
        }

        @Override
        protected void onPostExecute(final String result) {

        }
    }
}
