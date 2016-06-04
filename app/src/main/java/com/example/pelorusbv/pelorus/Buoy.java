package com.example.pelorusbv.pelorus;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Menso on 16-2-2015.
 */
public class Buoy extends ActivityDashboard {
    //Een boei object met een LatLng
    public LatLng buoyPos;

    public Buoy() {
    }

    public Buoy(double x,double y){
        buoyPos = new LatLng(x,y);
    }

    public LatLng getPos(){
        return buoyPos;
    }

    public void setPos(LatLng latLng) {
        buoyPos = latLng;
    }

}
