package com.example.pelorusbv.pelorus;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

/**
 * Created by Menso on 16-2-2015.
 */
public class Boat extends ActivityDashboard {

    private LatLng boatPos;
    private LatLng oldBoatPos;
    private double speed;

    private double oldLat;
    private double oldLng;



    private double a ;
    private SphericalUtil SphUt;


    public Boat(double x, double y) {
        boatPos = new LatLng(x, y);
    }

    public LatLng getPos() {
        return boatPos;
    }

    public void setPos(double x, double y) {
        boatPos = new LatLng(x, y);
    }

    public double getSpeed(double dt, int t, DataSourcePositions dataSource1) {
        if (t > 1) {
            oldLat = dataSource1.getPosLat(t - (int)dt);
            oldLng = dataSource1.getPosLng(t - (int) dt);
            oldBoatPos = new LatLng(oldLat,oldLng);
            speed = (SphUt.computeDistanceBetween(oldBoatPos,boatPos)*1.943844)/dt; //speed in knots
            //speed = Math.sqrt( Math.pow(boatPos.latitude - oldLat, 2) + Math.pow(boatPos.longitude - oldLng,2))/dt;
            return speed;
        }
        else {
            return 7;
        }
    }


}
