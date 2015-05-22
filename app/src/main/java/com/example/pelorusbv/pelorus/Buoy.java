package com.example.pelorusbv.pelorus;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Menso on 16-2-2015.
 */
public class Buoy extends ActivityDashboard {

    public LatLng buoyPos;

    public Buoy(double x,double y){
        buoyPos = new LatLng(x,y);
    }

    public Buoy(LatLng LL){
        buoyPos = LL;
    }

    public LatLng getPos(){
        return buoyPos;
    }
}
