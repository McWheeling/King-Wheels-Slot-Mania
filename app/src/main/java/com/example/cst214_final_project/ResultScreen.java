package com.example.cst214_final_project;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.security.Security;

/**
 * Jared Striemer CST214
 * COSC195 Final Project - King Wheels Mania
 * Result Screen Activity
 */
public class ResultScreen extends AppCompatActivity
{
    //Message for the Win Message - EX: Doubles! grabs whatever the winning message is from GameScreen.java
    private TextView tvResultMsg;

    private TextView tvLat;
    private TextView tvLong;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);

        // ---- Win Message Code ---
        tvResultMsg = findViewById(R.id.tvResultMsg);

        //This gets the string from the intent
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String nMsg = b.getString("message");
        tvResultMsg.setText(nMsg);

        // ---- Location Services onCreate Code ----
        tvLat = findViewById(R.id.tvLat);
        tvLong = findViewById(R.id.tvLong);

        //Gets the LocationManager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Defines a listener to response when the user moves the device around
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {makeUseOfNewLocation(location);}
        };

        //Register Location Listener with the Manager to receive any updates
        try
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        catch (SecurityException e)
        {
            tvLat.setText(e.getMessage());
        }
    }// End of onCreate

    //Helper Method - makeUseOfNewLocation - Sets the text of the Location TextViews in the Results Screen
    private void makeUseOfNewLocation(Location location)
    {
        //Set the Text in the TextViews
        tvLat.setText("Latitude: " + location.getLatitude() + "");
        tvLong.setText("Longitude: " + location.getLongitude() + "");
    }

    protected void onStop()
    {
        locationManager.removeUpdates(locationListener);
        super.onStop();
    }

    protected void onResume()
    {
        //Registers the Location Listener with the Location Manager to receive any updates
        try
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        catch(SecurityException e)
        {
            tvLat.setText(e.getMessage());
        }
        super.onResume();
    }

    // ---- Click Methods - Code to switch screens based on which button is pressed ----
    //Switches screen from Results to Home Screen
    public void onHomeClick(View view) {
        Intent intent = new Intent(this, MainActivity.class); //Opens the Main (Home) Java File
        this.startActivity(intent);
    }

    //Switches screen from Results to Game Screen
    public void onPlayAgainClick(View view) {
        Intent intent = new Intent(this, GameScreen.class); //Opens the Games Java File
        this.startActivity(intent);
    }
} //End of File
