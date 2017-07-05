package com.example.pelorusbv.pelorus;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

/**
 * Created by Menso on 16-2-2015.
 */
public class Boat extends ActivityDashboard {
    //Een boot object met een lat lng speed en heading
    private double lat;
    private double lng;
    private String name;

    private LatLng boatPos;
    private LatLng oldBoatPos;
    private double oldSpeed;
    private double speed;
    private double heading;

    private double oldLat;
    private double oldLng;



    private double a ;
    private SphericalUtil SphUt;
    private int dt;


    public Boat(double x, double y, String boatname) {
        name = boatname;
        lat = x;
        lng = y;
        dt = 1;
        oldSpeed =0;
    }

    public LatLng getPos() {
        boatPos = new LatLng(lat, lng);
        return boatPos;
    }

    public void setPos(double x, double y) {
        lat = x;
        lng = y;
    }

    public double getSpeed(int t, DataSourcePositions dataSource1, int runID) {
        if (t > 1) {
            boatPos = new LatLng(lat, lng);
            oldLat = dataSource1.getPosLat(t - dt, runID);
            oldLng = dataSource1.getPosLng(t - dt, runID);
            oldBoatPos = new LatLng(oldLat,oldLng);
            speed = (oldSpeed +((SphericalUtil.computeDistanceBetween(oldBoatPos, boatPos)/dt)* 1.943846))/2; //speed in knots
            oldSpeed = speed;
            return speed;
        }
        else {
            return 0;
        }
    }

    public float getHeading(int t, DataSourcePositions dataSource1, int runID) {
        if (t > 1) {
            boatPos = new LatLng(lat, lng);
            oldLat = dataSource1.getPosLat(t - dt, runID);
            oldLng = dataSource1.getPosLng(t - dt, runID);
            oldBoatPos = new LatLng(oldLat, oldLng);
            heading = SphericalUtil.computeHeading(oldBoatPos, boatPos); //heading in degree -180 to 180
            return (float) heading;
        } else
            return 0;
    }

    public String getBoatname(){
        return name;
    }



}
