package com.lifegofood.jt3282.lifego.food.frame;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.food.GMActivity;
import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.food.edit.EditActivity;
import com.lifegofood.jt3282.lifego.food.essay.EssayByClass;
import com.lifegofood.jt3282.lifego.food.userpage.EssayOwn;

import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by jt3282 on 2017/12/1.
 */

public class SecondFragment extends Fragment implements View.OnClickListener,LocationListener{

    private String account;
    private double lat;
    private double longt;
    private String provider;
    private LocationManager locationManager;
    private String address = "NowLocattion";
    private Button All,Light,Snack,Pot,BB,AftTee,Exquisite,Noodle,Roast,Japan,Thai,HK
            ,SeaFood,Fried,Drink,Other;
    private ImageButton nearly,google;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        View rootView = inflater.inflate(R.layout.second_frame, container, false);

        locationManager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);
        provider = locationManager.NETWORK_PROVIDER;

        All=(Button)rootView.findViewById(R.id.all);
        All.setOnClickListener(this);
        Light=(Button)rootView.findViewById(R.id.light_food);
        Light.setOnClickListener(this);
        Snack=(Button)rootView.findViewById(R.id.snack);
        Snack.setOnClickListener(this);
        Pot=(Button)rootView.findViewById(R.id.pot_food);
        Pot.setOnClickListener(this);
        BB=(Button)rootView.findViewById(R.id.bandb);
        BB.setOnClickListener(this);
        AftTee=(Button)rootView.findViewById(R.id.afternoon_tea);
        AftTee.setOnClickListener(this);
        Exquisite=(Button)rootView.findViewById(R.id.exquisite);
        Exquisite.setOnClickListener(this);
        Noodle=(Button)rootView.findViewById(R.id.noodle);
        Noodle.setOnClickListener(this);
        Roast=(Button)rootView.findViewById(R.id.roast);
        Roast.setOnClickListener(this);
        Japan=(Button)rootView.findViewById(R.id.japan);
        Japan.setOnClickListener(this);
        Thai=(Button)rootView.findViewById(R.id.thai);
        Thai.setOnClickListener(this);
        HK=(Button)rootView.findViewById(R.id.honkong);
        HK.setOnClickListener(this);
        SeaFood=(Button)rootView.findViewById(R.id.feafood);
        SeaFood.setOnClickListener(this);
        Fried=(Button)rootView.findViewById(R.id.fried);
        Fried.setOnClickListener(this);
        Drink=(Button)rootView.findViewById(R.id.drink);
        Drink.setOnClickListener(this);
        Other=(Button)rootView.findViewById(R.id.other);
        Other.setOnClickListener(this);
        nearly=(ImageButton)rootView.findViewById(R.id.nearly);
        nearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(address==""||address.equals("NowLocattion")){
                    Toast.makeText(getContext(),"無法取得位置", Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent();
                    intent.setClass(getContext(), EssayOwn.class);
                    intent.putExtra("key",address);
                    startActivity(intent);
                }
            }
        });
        google=(ImageButton)rootView.findViewById(R.id.google);
        google.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(address==""||address.equals("NowLocattion")){
                    Toast.makeText(getContext(),"無法取得位置", Toast.LENGTH_LONG).show();
                }else {
                    Intent intend = new Intent();
                    intend.setClass(getContext(), GMActivity.class);
                    startActivity(intend);
                }
            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton)rootView.findViewById(R.id.floatingActionButton);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);  ;
        account = preferences.getString("USER_NAME","");

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(account.equals("訪客")){
                    Toast.makeText(getContext(),"請先登入", Toast.LENGTH_LONG).show();
                }else{
                    list();
                }
            }
        });

        return rootView;
    }

    private void list(){
        final String[] dinner = {"輕食","小吃","鍋類","早午餐","下午茶點心","精緻","麵食","燒烤","日式","泰式","港式","海鮮","熱炒","冰/飲品","其他"};
        SharedPreferences preferences = this.getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);  ;
        account = preferences.getString("USER_NAME","");
        AlertDialog.Builder dialog_list = new AlertDialog.Builder(getContext());
        dialog_list.setTitle("請選擇分類");
        dialog_list.setItems(dinner, new DialogInterface.OnClickListener(){
            @Override

            //只要你在onClick處理事件內，使用which參數，就可以知道按下陣列裡的哪一個了
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Toast.makeText(getContext(), "你選的是" + dinner[which], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("account",account);
                intent.putExtra("class",dinner[which]);
                intent.setClass(getContext(), EditActivity.class);
                startActivity(intent);

            }
        });
        dialog_list.show();
    }
    //Light,Snack,Pot,BB,AftTee,Exquisite,Noodle,Roast,Japan,Thai,HK,SeaFood,Fried,Drink,Other;
    @Override
    public void onClick(View v) {
        Intent i = new Intent();
        i.setClass(getContext(), EssayByClass.class);
        if(v==All)i.putExtra("class","all");
        if(v==Light)i.putExtra("class","輕食");
        if(v==Snack)i.putExtra("class","小吃");
        if(v==Pot)i.putExtra("class","鍋類");
        if(v==BB)i.putExtra("class","早午餐");
        if(v==AftTee)i.putExtra("class","下午茶點心");
        if(v==Exquisite)i.putExtra("class","精緻");
        if(v==Noodle)i.putExtra("class","麵食");
        if(v==Roast)i.putExtra("class","燒烤");
        if(v==Japan)i.putExtra("class","日式");
        if(v==Thai)i.putExtra("class","泰式");
        if(v==HK)i.putExtra("class","港式");
        if(v==SeaFood)i.putExtra("class","海鮮");
        if(v==Fried)i.putExtra("class","熱炒");
        if(v==Drink)i.putExtra("class","冰/飲品");
        if(v==Other)i.putExtra("class","其他");
        startActivity(i);
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        longt = location.getLongitude();
        address = getAddressByLocation(location);
    }
    private String getAddressByLocation(Location location) {
        String returnAddress = "";
        try {
            if (location != null) {
                Double longitude = location.getLongitude();        //取得經度
                Double latitude = location.getLatitude();        //取得緯度

                Geocoder gc = new Geocoder(getContext(), Locale.TRADITIONAL_CHINESE);        //地區:台灣
                //自經緯度取得地址
                List<Address> lstAddress = gc.getFromLocation(latitude, longitude, 1);

                if (!Geocoder.isPresent()){ //Since: API Level 9
                    returnAddress = "Sorry! Geocoder service not Present.";
                }
                String admin = "";
                String subadmin = "";
                String locality = "";
                admin = lstAddress.get(0).getAdminArea();
                subadmin = lstAddress.get(0).getSubAdminArea();
                locality = lstAddress.get(0).getLocality();
                returnAddress = (admin==null?"":admin) + (subadmin==null?"":subadmin) + (locality==null?"":locality);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return returnAddress;
    }
    @Override
    public void onResume() {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 1, this);
        super.onResume();
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, this);
        } else {
            Toast.makeText(getContext(), "請開啟定位服務", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        provider = locationManager.NETWORK_PROVIDER;
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
