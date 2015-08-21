package com.example.pelorusbv.pelorus;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.sql.SQLException;

public class ActivityCreateCourse extends FragmentActivity implements FragmentInsertCourse.OnFragmentInteractionListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    Buoy Buoy1;
    Buoy Buoy2;
    Buoy Buoy3;
    Buoy Buoy4;

    SphericalUtil SpUtl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        setUpMapIfNeeded();


    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

    }


    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

    }


    public void onPreviewCourse(LatLng b1, LatLng b2, LatLng b3, LatLng b4){
//        Buoy1 = new Buoy(lat,lng);
//        Buoy2 = new Buoy(SpUtl.computeOffset(Buoy1.getPos(), 500, wind-90));
//        Buoy3 = new Buoy(SpUtl.computeOffset(Buoy1.getPos(), 2*1852, wind));
//        Buoy4 = new Buoy(SpUtl.computeOffset(Buoy1.getPos(), 2*1852, wind-180));

        mMap.addMarker(new MarkerOptions().position(b1));
        mMap.addMarker(new MarkerOptions().position(b2));
        mMap.addMarker(new MarkerOptions().position(b3));
        mMap.addMarker(new MarkerOptions().position(b4));

        mMap.animateCamera(CameraUpdateFactory.newLatLng(b1));
    }

    public  void onCreateCourse(){
        Intent intent = new Intent(this, ActivityCreateEvent.class);
        startActivity(intent);
    }



}
