package com.lifegofood.jt3282.lifego.food.fab;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.food.GMActivity;
import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.food.userpage.EssayOwn;

import java.util.List;
import java.util.Locale;

/**
 * Created by jt3282 on 2017/12/27.
 */

public class NearlyFood extends Activity implements LocationListener {

    Button nearly,google;
    private double lat;
    private double longt;
    private String provider;
    private LocationManager locationManager;
    private String address = "NowLocattion";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearlyfood);
        nearly=(Button)findViewById(R.id.nearly_food);

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        provider = locationManager.NETWORK_PROVIDER;

        nearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(address==""||address.equals("NowLocattion")){
                    Toast.makeText(NearlyFood.this,"請開啟定位服務", Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent();
                    intent.setClass(NearlyFood.this, EssayOwn.class);
                    intent.putExtra("key",address);
                    startActivity(intent);
                }
            }
        });
        google=(Button)findViewById(R.id.google_map);
        google.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(address==""||address.equals("NowLocattion")){
                    Toast.makeText(NearlyFood.this,"請開啟定位服務", Toast.LENGTH_LONG).show();
                }else {
                    Intent intend = new Intent();
                    intend.setClass(NearlyFood.this, GMActivity.class);
                    startActivity(intend);
                }
            }
        });
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

                Geocoder gc = new Geocoder(NearlyFood.this, Locale.TRADITIONAL_CHINESE);        //地區:台灣
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
            if (ActivityCompat.checkSelfPermission(NearlyFood.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NearlyFood.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            Toast.makeText(NearlyFood.this, "請開啟定位服務", Toast.LENGTH_SHORT).show();
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵

            finish();
        }
        return true;
    }
}
