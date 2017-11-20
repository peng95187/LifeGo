package com.example.jt3282.lifego;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class MapsStore extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Double storelat;
    Double storelng;
    Double mylat;
    Double mylng;
    float zoom;
    LatLng sydney;
    LatLng mysydney;
    RatingBar rb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_store);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        String textStr = getIntent().getStringExtra("lat");
        storelat = Double.parseDouble(textStr);

        String textStr2 = getIntent().getStringExtra("lng");
        storelng = Double.parseDouble(textStr2);

        TextView tv1 = (TextView)findViewById(R.id.stname);
        String stname = getIntent().getStringExtra("stname");
        tv1.setText("店名 : "+stname);

        TextView tv2 = (TextView)findViewById(R.id.staddress);
        String staddress = getIntent().getStringExtra("address");
        tv2.setText("地址 : "+staddress);

        String rate = getIntent().getStringExtra("rating");
        rb = (RatingBar)findViewById(R.id.ratBar1);
        rb.setMax(5); //設定最大值
        rb.setNumStars(5); //設定最大星型數量
        rb.setStepSize((float) 0.5);
        rb.setRating(Float.parseFloat(rate));
        rb.setIsIndicator(true);

        String placeID = getIntent().getStringExtra("placeid");
        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid="+placeID+"&key=AIzaSyA_L_TKi8QE24mFM6OUE7Lf7w8P6czlcwI&language=zh-tw";
        new HttpAsyncTask().execute(url);

        mylat = getIntent().getDoubleExtra("myLat",0.0);
        mylng = getIntent().getDoubleExtra("myLng",0.0);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        zoom = 17;

        Button myLocation = (Button)findViewById(R.id.mylocation);
        myLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(mysydney));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(mysydney).zoom(zoom).bearing(0).tilt(0).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
        Button destiny = (Button)findViewById(R.id.destiny);
        destiny.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(zoom).bearing(0).tilt(0).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        sydney = new LatLng(storelat, storelng);
        mMap.addMarker(new MarkerOptions().position(sydney).title("目的地"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mysydney = new LatLng(mylat, mylng);
        mMap.addMarker(new MarkerOptions().position(mysydney).title("您的位置"));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(zoom).bearing(0).tilt(0).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(final String result) {
            try {
                JSONObject jObject = new JSONObject(result);
                String phone = jObject.getJSONObject("result").getString("formatted_phone_number");
                TextView t1 = (TextView)findViewById(R.id.stphone);
                t1.setText("聯絡電話 : "+phone);
                JSONArray open_hour = jObject.getJSONObject("result").getJSONObject("opening_hours").getJSONArray("weekday_text");
                TextView t2 = (TextView)findViewById(R.id.stopen);
                t2.setText(open_hour.getString(0)+"\n"+open_hour.getString(1)+"\n"+open_hour.getString(2)+"\n"+open_hour.getString(3)
                        +"\n"+open_hour.getString(4)+"\n"+open_hour.getString(5)+"\n"+open_hour.getString(6));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        private String GET(String url) {

            InputStream inputStream;
            String result = "";

            try {
                // create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                // make GET request to the given URL
                HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
                // receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();
                // convert inputstream to string

                if (inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return result;
        }

        private String convertInputStreamToString(InputStream inputStream) throws IOException {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

            String line;
            String result = "";

            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            inputStream.close();
            return result;
        }
    }

}
