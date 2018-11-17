package com.lifegofood.jt3282.lifego.traffic.bus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.lifegofood.jt3282.lifego.R;

public class CountySelect extends Activity {

    String [] Type = {"臺北市", "新北市", "桃園市", "臺中市", "臺南市", "高雄市", "基隆市",
            "新竹市", "新竹縣", "苗栗縣:", "彰化縣", "南投縣", "雲林縣",
            "嘉義縣", "嘉義市", "屏東縣", "宜蘭縣", "花蓮縣", "臺東縣",
            "金門縣", "澎湖縣", "連江縣"};

    String [] Min =  {"Taipei", "NewTaipei", "Taoyuan", "Taichung", "Tainan", "Kaohsiung", "Keelung", "Hsinchu", "HsinchuCounty", "MiaoliCounty",
            "ChanghuaCounty", "NantouCounty", "YunlinCounty", "ChiayiCounty", "Chiayi", "PingtungCounty,", "YilanCounty", "HualienCounty",
            "TaitungCounty", "KinmenCounty", "PenghuCounty", "LienchiangCounty"};

    Spinner spinner;
    Button search;
    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.county_select);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(CountySelect.this, android.R.layout.simple_spinner_dropdown_item, Type);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, View view, final int position, long id) {
                search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent.setClass(CountySelect.this, BusSelectSearch.class);
                        intent.putExtra("location", Min[position]);
                        intent.putExtra("county_zh", Type[position]);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search = (Button)findViewById(R.id.search);
    }
}
