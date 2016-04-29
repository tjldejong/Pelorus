package com.example.pelorusbv.pelorus;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

/**
 * Created by Menso on 16-2-2015.
 */
public class Boat extends ActivityDashboard {

    private double lat;
    private double lng;

    private LatLng boatPos;
    private LatLng oldBoatPos;
    private double speed;
    private double heading;

    private double oldLat;
    private double oldLng;



    private double a ;
    private SphericalUtil SphUt;
    private int dt;


    public Boat(double x, double y) {
        lat = x;
        lng = y;
        dt = 1;
    }

    public LatLng getPos() {
        boatPos = new LatLng(lat, lng);
        return boatPos;
    }

    public void setPos(double x, double y) {
        lat = x;
        lng = y;
    }

    public double getSpeed(int t, DataSourcePositions dataSource1) {
        if (t > 1) {
            boatPos = new LatLng(lat, lng);
            oldLat = dataSource1.getPosLat(t - dt);
            oldLng = dataSource1.getPosLng(t - dt);
            oldBoatPos = new LatLng(oldLat,oldLng);
            Log.i("oldposlat", String.format("%.6f", oldLat));
            Log.i("newposlat", String.format("%.6f", lat));
            speed = (SphericalUtil.computeDistanceBetween(oldBoatPos, boatPos) * 1.943844) / dt; //speed in knots
            //speed = Math.sqrt( Math.pow(boatPos.latitude - oldLat, 2) + Math.pow(boatPos.longitude - oldLng,2))/dt;
            return speed;
        }
        else {
            return 0;
        }
    }

    public float getHeading(int t, DataSourcePositions dataSource1) {
        if (t > 1) {
            boatPos = new LatLng(lat, lng);
            oldLat = dataSource1.getPosLat(t - dt);
            oldLng = dataSource1.getPosLng(t - dt);
            oldBoatPos = new LatLng(oldLat, oldLng);
            heading = SphericalUtil.computeHeading(oldBoatPos, boatPos); //heading in degree -180 to 180
            return (float) heading;
        } else
            return 0;
    }



}
