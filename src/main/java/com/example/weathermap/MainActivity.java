package com.example.weathermap;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void getCoordinates(View view){
        StringBuilder sb = new StringBuilder();
        try{
            URL oracle = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=AIzaSyDAl-UKkld3t7nq_YZpv1Dk3ekvORy9tDg");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(oracle.openStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null)
                sb.append(inputLine);
            in.close();

        }catch (Exception e){

        }

            try{

                JSONObject jsonObject = new JSONObject(sb.toString());
                JSONObject results = jsonObject.getJSONObject("results");
                JSONObject geometry = results.getJSONObject("geometry");
                JSONObject location = results.getJSONObject("location");
                String lat = location.getString("lat");
                String lng = location.getString("lng");
            } catch(Exception e){  }




        }



}
