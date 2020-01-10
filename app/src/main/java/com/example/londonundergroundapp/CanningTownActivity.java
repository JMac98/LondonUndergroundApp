package com.example.londonundergroundapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CanningTownActivity extends AppCompatActivity{

    private TextView textViewResult1;
    private TextView textViewResult2;
    private int k;
    private int q;
    private RequestQueue mQueue;
    private String serverAddress = "http://192.168.0.23/Volley/Insert.php";
    private AlertDialog.Builder builder;
    SpannableString spannableString1;
    SpannableString spannableString2;
    private String platformName;
    private String destinationName;
    private String currentLocation;
    private int timeToStation;

    private List<PlatformInfo> canningTownArrivals1 = new ArrayList<>();
    private List<PlatformInfo> canningTownArrivals2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canning_town);

        textViewResult1 = findViewById(R.id.textView1);
        textViewResult2 = findViewById(R.id.textView2);

        builder = new AlertDialog.Builder(CanningTownActivity.this);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                jsonParse();
                handler.postDelayed(this, 50000); //50 second refresh rate
            }
        };
        handler.postDelayed(runnable, 500);

    }

    boolean mIsRequest = false;


    private void jsonParse(){
        String url = "https://api.tfl.gov.uk/Line/jubilee/Arrivals/940GZZLUCGT?direction=all&app_id=461641d9&app_key=d25484e7ca02fe0985d5ac06c2923ad5"; //API is offline at midnight; instantly closes application

        if(mIsRequest){
            return;
        }
        mIsRequest = true;

        canningTownArrivals1.clear();
        canningTownArrivals2.clear();
        textViewResult1.setText("");
        textViewResult2.setText("");

        mQueue = Volley.newRequestQueue(CanningTownActivity.this);

        JsonArrayRequest request = new JsonArrayRequest(Method.GET, url, null, new Response.Listener<JSONArray>() { //JSON Array

            @Override
            public void onResponse(JSONArray response) {
                mIsRequest = false;
                for(int i = 0; i < response.length();i++){ //parses through the JSON file (JSON array)
                    try {
                        JSONObject arrivals = response.getJSONObject(i);

                        platformName = arrivals.getString("platformName");
                        destinationName = arrivals.getString("destinationName");
                        currentLocation = arrivals.getString("currentLocation");
                        timeToStation = arrivals.getInt("timeToStation");

                        if(platformName.contains("Eastbound")){
                            canningTownArrivals1.add(new PlatformInfo(platformName,destinationName,currentLocation,timeToStation));
                        }else if(platformName.contains("Westbound")){
                            canningTownArrivals2.add(new PlatformInfo(platformName,destinationName,currentLocation,timeToStation));
                        }

                        Collections.sort(canningTownArrivals1, new Comparator<PlatformInfo>() { //sort the arrivals in order of time
                            @Override
                            public int compare(PlatformInfo o1, PlatformInfo o2) {
                                return Integer.compare(o1.timeToStation,o2.timeToStation);
                            }
                        });

                        Collections.sort(canningTownArrivals2, new Comparator<PlatformInfo>() {
                            @Override
                            public int compare(PlatformInfo o1, PlatformInfo o2) {
                                return Integer.compare(o1.timeToStation,o2.timeToStation);
                            }
                        });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Toast message for eastbound platform

            // Need to use spannable string to display PlatformInfo

                for(k = 0; k < 8;k++) {
                    spannableString1 = new SpannableString("Destination: " + canningTownArrivals1.get(k).destinationName); //spannable string is in the scope of the for loop
                    spannableString1.setSpan(createClickableSpanEastbound(canningTownArrivals1.get(k).currentLocation),0,canningTownArrivals1.get(k).length() + 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // display toast message

                    textViewResult1.append(spannableString1);

                    textViewResult1.append(
                            "\nTime: " + canningTownArrivals1.get(k).getMinutesToStation() + " mins " + canningTownArrivals1.get(k).getSecondsToStation() + " seconds\n");

                    textViewResult1.setMovementMethod(LinkMovementMethod.getInstance());
                    }

                // Toast message for westbound platform

                for(q = 0; q < canningTownArrivals2.size();q++) {
                    spannableString2 = new SpannableString("Destination: " + canningTownArrivals2.get(q).destinationName); //spannable string is in the scope of the for loop,
                    spannableString2.setSpan(createClickableSpanWestbound(canningTownArrivals2.get(q).currentLocation),0,canningTownArrivals2.get(q).length() + 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    textViewResult2.append(spannableString2);

                    textViewResult2.append(
                            "\nTime: " + canningTownArrivals2.get(q).getMinutesToStation() + " mins " + canningTownArrivals2.get(q).getSecondsToStation() + " seconds\n");

                    textViewResult2.setMovementMethod(LinkMovementMethod.getInstance());
                    }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

    private ClickableSpan createClickableSpanEastbound(final String location) {
        return new ClickableSpan() {
            @Override
            public void onClick(View view1) {
                Toast.makeText(CanningTownActivity.this, "This train is " + location, Toast.LENGTH_LONG)
                        .show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, serverAddress, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
            }

        };
    }

        private ClickableSpan createClickableSpanWestbound( final String location){
            return new ClickableSpan() {
                @Override
                public void onClick(View view1) {
                    Toast.makeText(CanningTownActivity.this, "This train is " + location, Toast.LENGTH_LONG)
                            .show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, serverAddress, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }
                            , new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                }

            };

        }
}






