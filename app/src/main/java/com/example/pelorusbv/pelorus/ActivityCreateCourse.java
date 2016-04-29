package com.example.pelorusbv.pelorus;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActivityCreateCourse extends FragmentActivity implements FragmentInsertCourse.OnFragmentInteractionListener, OnMapReadyCallback {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapCreateCourse);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }


    public void onPreviewCourse(LatLng b1, LatLng b2, LatLng b3, LatLng b4){
//        Buoy1 = new Buoy(lat,lng);
//        Buoy2 = new Buoy(SpUtl.computeOffset(Buoy1.getPos(), 500, wind-90));
//        Buoy3 = new Buoy(SpUtl.computeOffset(Buoy1.getPos(), 2*1852, wind));
//        Buoy4 = new Buoy(SpUtl.computeOffset(Buoy1.getPos(), 2*1852, wind-180))
        mMap.addMarker(new MarkerOptions().position(b1));
        mMap.addMarker(new MarkerOptions().position(b2));
        mMap.addMarker(new MarkerOptions().position(b3));
        mMap.addMarker(new MarkerOptions().position(b4));

        mMap.animateCamera(CameraUpdateFactory.newLatLng(b1));
    }

    public void onCreateCourse() {
        Intent intent = new Intent(this, ActivityCreateEvent.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



}
