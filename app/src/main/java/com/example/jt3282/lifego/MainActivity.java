package com.example.jt3282.lifego;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity implements LocationListener,AdapterView.OnItemClickListener{

    LocationManager locationManager;
    String provider;
    Double lat;
    Double longt;
    ArrayList<String> storeOpen = new ArrayList<String>();
    ArrayList<String> storeOpen2 = new ArrayList<String>();
    ArrayList<String> storeClose = new ArrayList<String>();
    ArrayList<String> storeClose2 = new ArrayList<String>();

    ArrayList<String> stname_open = new ArrayList<String>();
    ArrayList<String> stname_close = new ArrayList<String>();

    ArrayList<String> address_open = new ArrayList<String>();
    ArrayList<String> address_close = new ArrayList<String>();

    ArrayList<String> place_id_open = new ArrayList<String>();
    ArrayList<String> place_id_close = new ArrayList<String>();

    ArrayList<String> open_rating = new ArrayList<String>();
    ArrayList<String> close_rating = new ArrayList<String>();

    ListView listView1;
    ListView listView2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_google);
        // Getting LocationManager object
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Creating an empty criteria object
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, true);
    }

    @Override
    public void onLocationChanged(Location location) {

        lat = location.getLatitude();
        longt = location.getLongitude();

        String key = "AIzaSyA_L_TKi8QE24mFM6OUE7Lf7w8P6czlcwI";
        String ListApi = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+location.getLatitude()+","+location.getLongitude()+"&radius=2000&types=food&key="+key+"&language=zh-TW";
        new HttpAsyncTask().execute(ListApi);
        //https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyA_L_TKi8QE24mFM6OUE7Lf7w8P6czlcwI&radius=500&location=25.0540279,121.5199219
    }
    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onStatusChanged(String provide, int status, Bundle extras) {
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        } else {
            Toast.makeText(getBaseContext(), "請開啟定位服務", Toast.LENGTH_SHORT).show();
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, MapsStore.class);
        if(parent==listView1) {
            intent.putExtra("lat",storeOpen.get(position));
            intent.putExtra("lng",storeOpen2.get(position));
            intent.putExtra("stname",stname_open.get(position));
            intent.putExtra("address",address_open.get(position));
            intent.putExtra("placeid",place_id_open.get(position));
            intent.putExtra("rating",open_rating.get(position));
            intent.putExtra("myLat",lat);
            intent.putExtra("myLng",longt);
        }
        else
        {
            intent.putExtra("lat",storeClose.get(position));
            intent.putExtra("lng",storeClose2.get(position));
            intent.putExtra("stname",stname_close.get(position));
            intent.putExtra("address",address_close.get(position));
            intent.putExtra("placeid",place_id_close.get(position));
            intent.putExtra("rating",close_rating.get(position));
            intent.putExtra("myLat",lat);
            intent.putExtra("myLng",longt);
        }
        startActivity(intent);
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

            try{
                ArrayList<HashMap<String,Object>> list1=new ArrayList<HashMap<String,Object>>();
                ArrayList<HashMap<String,Object>> list2=new ArrayList<HashMap<String,Object>>();
                JSONObject jObject = new JSONObject(result);

                for(int i=0;i<jObject.getJSONArray("results").length();i++) {
                    HashMap<String, Object> item = new HashMap<String, Object>();
                    String placeid = jObject.getJSONArray("results").getJSONObject(i).getString("place_id");
                    String store_name = jObject.getJSONArray("results").getJSONObject(i).getString("name");
                    Boolean open_now = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("opening_hours").getBoolean("open_now");
                    Double rating = jObject.getJSONArray("results").getJSONObject(i).getDouble("rating");
                    Double location_lat = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                    Double location_lng = jObject.getJSONArray("results").getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                    String adrs = jObject.getJSONArray("results").getJSONObject(i).getString("vicinity");
                    Double distance = Distance(location_lng,location_lat);
                    String dis = setDistance(distance);
                    String store_rating =  Double.toString(rating);

                    if(store_name.contains("7-Eleven")||store_name.contains("7-ELEVEN")){
                        item.put("icon",R.drawable.seveneleven);
                    }else if(store_name.contains("Family Mart")){
                        item.put("icon",R.drawable.familymart);
                    }else if(store_name.contains("咖啡")||store_name.contains("coffee")||store_name.contains("cafe")){
                        item.put("icon",R.drawable.coffee);
                    }else if(store_name.contains("超市")||store_name.contains("全聯")
                            ||store_name.contains("愛買")||store_name.contains("家樂福")
                            ||store_name.contains("大潤發")||store_name.contains("COSCO")){
                        item.put("icon",R.drawable.supermarket);
                    }else item.put("icon",R.drawable.rest);

                    item.put("store_name", store_name);
                    item.put("distance", dis);
                    item.put("store_rating", store_rating);

                    if(open_now){
                        item.put("store_status", R.drawable.open);
                    }else{
                        item.put("store_status", R.drawable.close);
                    }
                    if(open_now){
                        storeOpen.add(location_lat.toString());
                        storeOpen2.add(location_lng.toString());
                        stname_open.add(store_name);
                        address_open.add(adrs);
                        place_id_open.add(placeid);
                        open_rating.add(store_rating);
                        list1.add(item);
                    }else{
                        storeClose.add(location_lat.toString());
                        storeClose2.add(location_lng.toString());
                        stname_close.add(store_name);
                        address_close.add(adrs);
                        place_id_close.add(placeid);
                        close_rating.add(store_rating);
                        list2.add(item);
                    }
                }
                String from[] = { "icon", "store_name", "distance", "store_rating", "store_status"};
                int to[] = { R.id.iv_image, R.id.store_name ,R.id.distance , R.id.rating, R.id.status};
                SimpleAdapter adapter1 = new SimpleAdapter(MainActivity.this, list1, R.layout.store_list, from, to);
                SimpleAdapter adapter2 = new SimpleAdapter(MainActivity.this, list2, R.layout.store_list, from, to);
                listView1 = (ListView) findViewById(R.id.lv1);
                listView2 = (ListView) findViewById(R.id.lv2);

                listView1.setAdapter(adapter1);
                listView2.setAdapter(adapter2);

                locationManager.removeUpdates(MainActivity.this);
                listView1.setOnItemClickListener(MainActivity.this);
                listView2.setOnItemClickListener(MainActivity.this);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        public double Distance(double longitude2,double latitude2)
        {
            double radLatitude1 = lat * Math.PI / 180;
            double radLatitude2 = latitude2 * Math.PI / 180;
            double l = radLatitude1 - radLatitude2;
            double p = longt * Math.PI / 180 - longitude2 * Math.PI / 180;
            double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(l / 2), 2)
                    + Math.cos(radLatitude1) * Math.cos(radLatitude2)
                    * Math.pow(Math.sin(p / 2), 2)));
            distance = distance * 6378137.0;
            distance = Math.round(distance * 10000) / 10000;

            return distance ;
        }
        public String setDistance(double distance){
            String dis;
            if(distance>=1000)dis = Double.toString(distance/1000) + " km";
            else dis = Double.toString(distance) + " m";
            return dis;
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




