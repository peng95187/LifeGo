package com.lifegofood.jt3282.lifego.traffic.routedirect;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;

public class RouteDirection extends Activity implements LocationListener {

    private CheckBox locCheck;
    private EditText startLoc, endLoc;
    private Button searchBtn;
    private LinearLayout ll_start;

    //locate
    private double lat;
    private double longt;
    private String provider;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_direction);

        final Intent intent = new Intent(this,RouteDirection_result.class);

        //init
        locCheck = (CheckBox) findViewById(R.id.location_now);
        startLoc = (EditText) findViewById(R.id.start);
        endLoc = (EditText) findViewById(R.id.end);
        searchBtn = (Button) findViewById(R.id.search);
        ll_start = (LinearLayout) findViewById(R.id.ll_start) ;

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        provider = locationManager.NETWORK_PROVIDER;

        locCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                    ll_start.setVisibility(View.VISIBLE);
                }else{
                    ll_start.setVisibility(View.GONE);
                }
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!locCheck.isChecked()) {
                    if (startLoc.getText().toString() != null && endLoc.getText().toString() != null
                            &&!startLoc.getText().toString().equals("") && !endLoc.getText().toString().equals("")) {
                        intent.putExtra("startLoc",startLoc.getText().toString());
                        intent.putExtra("endLoc",endLoc.getText().toString());
                        startActivity(intent);
                    } else {
                        Toast.makeText(RouteDirection.this, "起(終)點不得為空", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(longt==0.0&&lat==0.0){
                        Toast.makeText(RouteDirection.this, "正嘗試取得位置", Toast.LENGTH_SHORT).show();
                    }else{
                        if (endLoc.getText().toString() != null && !endLoc.getText().toString().equals("")) {
                            intent.putExtra("startLoc", lat + "," + longt);
                            intent.putExtra("endLoc", endLoc.getText().toString());
                            startActivity(intent);
                        }else {
                            Toast.makeText(RouteDirection.this, "起(終)點不得為空", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        longt = location.getLongitude();

    }

    @Override
    public void onResume() {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 1, this);
        super.onResume();
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(RouteDirection.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RouteDirection.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            Toast.makeText(RouteDirection.this, "請開啟定位服務", Toast.LENGTH_SHORT).show();
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
