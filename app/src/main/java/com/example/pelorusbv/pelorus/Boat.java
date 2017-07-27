package com.example.pelorusbv.pelorus;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import android.location.Location;
import com.google.android.gms.common.api.GoogleApiClient;

import java.sql.Timestamp;

/**
 * Created by Roelof on 27-7-2017.
 */
public class Boat extends ActivityDashboard {
    //Een boot object met een lat lng speed en heading

    private String name;
    private float msToKnots = 0.5144f;
    private Location mLocation;

    public Boat(Location location, String boatname) {
        name = boatname;
        mLocation = location;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

    public Location getLocation(){
        return mLocation;
    }

    public LatLng getLatLng(){
        return new LatLng(mLocation.getLatitude(),mLocation.getLongitude());
    }


    public float getSpeed() {
        return mLocation.getSpeed()*msToKnots;
    }

    public float getHeading(){
        return mLocation.getBearing();
    }


    public String getBoatname() {
        return name;
    }

}