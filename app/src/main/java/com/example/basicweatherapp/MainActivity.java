package com.example.basicweatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    ImageView imageView;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
      imageView = findViewById(R.id.weatherPng);
      Picasso.get().load("https://i.pinimg.com/originals/77/0b/80/770b805d5c99c7931366c2e84e88f251.png").into(imageView);
      fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        queue = Volley.newRequestQueue(this);

    }

    public void onBtnClick(View vew) {
        Log.d("Clicked", "Button was Clicked");
        TextView textView = findViewById(R.id.txtMessage);


        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {

                            double longitude = location.getLongitude();
                            double latitude = location.getLatitude();

                            String url = "https://fcc-weather-api.glitch.me/api/current?lat=" + latitude + "&lon=" + longitude;
//                            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                                    new Response.Listener<String>() {
//                                        @Override
//                                        public void onResponse(String response) {
//                                            // Display the first 500 characters of the response string.
//                                            textView.setText("Response is: "+ response.coord);
//                                        }
//                                    }, new Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    textView.setText("That didn't work!");
//                                }
//                            });

                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                textView.setText("The Weather will be: " + response.getJSONArray("weather").getJSONObject(0).getString("description"));
                                                Picasso.get().load(response.getJSONArray("weather").getJSONObject(0).getString("icon")).into(imageView);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {

                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            // TODO: Handle error

                                        }
                                    });

// Access the RequestQueue through your singleton class.


                            Log.d("TAG", "Location returned.");
                            queue.add(jsonObjectRequest);
                        } else {
                            Log.d("TAG", "Null location returned.");
                        }
                    }
                });
        }


    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }
}