package com.example.mapandweather;

import android.nfc.cardemulation.CardEmulation;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    String lat = null;
    String lng = null;
    String URL;
    GoogleMap GMap;
    JSONObject currently = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    public void getCoordinates(View view) {
        // Rodolfo driving now
        String address = ((TextView) findViewById(R.id.addressBox)).getText().toString();
        address.replace(" ", "+");
        URL = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=AIzaSyDAl-UKkld3t7nq_YZpv1Dk3ekvORy9tDg";

        Thread t = new Thread(new Runnable(){
            public void run(){
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                InputStream stream = null;

                try{
                    URL url = new URL(URL);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    stream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuilder sb = new StringBuilder();

                    String line = "";

                    while((line = reader.readLine()) != null){
                        sb.append(line + " ");
                    }
                    String json = sb.toString();

                    // end of Rodolfo driving, Ashkan driving now
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray results = jsonObject.getJSONArray("results");
                    JSONObject geometry = results.getJSONObject(0);
                    geometry = geometry.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    lat = location.getString("lat");
                    lng = location.getString("lng");


                }
                catch (Exception e){
                    e.printStackTrace();

                }
                finally{
                    if(connection != null){
                        connection.disconnect();
                    }
                    if(reader != null){
                        try{
                            reader.close();
                        }
                        catch (Exception e){

                        }
                    }
                }


                /* Get Weather Data */


                connection = null;
                reader = null;
                stream = null;

                try{
                    // end of Ashkan driving, Rodolfo driving now
                    URL url = new URL("https://api.darksky.net/forecast/59a2dd6fad06874036c063ca748d4d80/"+lat+","+lng);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    stream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuilder sb = new StringBuilder();

                    String line = "";

                    while((line = reader.readLine()) != null){
                        sb.append(line + " ");
                    }
                    String json = sb.toString();


                    JSONObject jsonObject = new JSONObject(json);
                    currently = jsonObject.getJSONObject("currently");


                }
                catch (Exception e){


                }
                finally{
                    if(connection != null){
                        connection.disconnect();
                    }
                    if(reader != null){
                        try{
                            reader.close();
                        }
                        catch (Exception e){

                        }
                    }
                }
            }
        });
        t.start();

        try {
            t.join();
        }catch(Exception e){}

        // end of Rodolfo driving, Ashkan driving now
        GMap.clear();
        LatLng location = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
        GMap.addMarker(new MarkerOptions().position(location));
        GMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        GMap.setMinZoomPreference(10);


        try{
            if(currently != null){
                if(currently.has("temperature")){
                    ((TextView) findViewById(R.id.temperatureBox)).setText("Temperature: " + currently.getString("temperature") + " C");

                }
                if(currently.has("humidity")){
                    ((TextView) findViewById(R.id.humidityBox)).setText("Humidity: " + currently.getString("humidity") + "%");

                }
                if(currently.has("precipType") && currently.has("precipProbability")){
                    ((TextView) findViewById(R.id.precipitationBox)).setText("Precipitation: " + currently.getString("precipType") + " " + currently.getString("precipProbability") + "%");
                }
                else{
                    ((TextView) findViewById(R.id.precipitationBox)).setText("Precipitation: None");
                }

                if(currently.has("windSpeed")){
                    ((TextView) findViewById(R.id.windspeedBox)).setText("Wind Speed: " + currently.getString("windSpeed") + " m/s");

                }

            }

        }
        catch(Exception e){}

    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        GMap = googleMap;
        // end of Ashkan driving
    }

}
