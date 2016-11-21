package com.example.pelorusbv.pelorus;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

public class ActivityCreateCourse extends FragmentActivity implements FragmentInsertCourse.OnFragmentInteractionListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener {
    private static final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 0;
    protected Location mLastLocation;
    EditText latText;
    EditText lngText;
    Marker mark1Marker;
    Marker mark2Marker;
    Marker mark3Marker;
    Marker mark4Marker;
    //Laat een kaartje zien met boeien er op. Gebruikt een fragment om de boeien toe te voegen.
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private Buoy mark1;
    private Buoy mark2;
    private Buoy mark3;
    private Buoy mark4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapCreateCourse);
        mapFragment.getMapAsync(this);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        latText = (EditText) findViewById(R.id.editTextLat);
        lngText = (EditText) findViewById(R.id.editTextLng);

        mark1 = new Buoy();
        mark2 = new Buoy();
        mark3 = new Buoy();
        mark4 = new Buoy();
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
        mark1.setPos(b1);
        mark2.setPos(b2);
        mark3.setPos(b3);
        mark4.setPos(b4);

        if (mark1Marker == null) {
            mark1Marker = mMap.addMarker(new MarkerOptions().position(mark1.getPos()));
            mark2Marker = mMap.addMarker(new MarkerOptions().position(mark2.getPos()));
            mark3Marker = mMap.addMarker(new MarkerOptions().position(mark3.getPos()));
            mark4Marker = mMap.addMarker(new MarkerOptions().position(mark4.getPos()));
        } else {
            mark1Marker.setPosition(mark1.getPos());
            mark2Marker.setPosition(mark2.getPos());
            mark3Marker.setPosition(mark3.getPos());
            mark4Marker.setPosition(mark4.getPos());
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLng(b1));
    }

    public void onCreateCourse() {
        Intent intent = new Intent(this, ActivityCreateEvent.class);
        startActivity(intent);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_LOCATION);
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())));
            latText.setText(String.format(Locale.US, "%.4f", mLastLocation.getLatitude()));
            lngText.setText(String.format(Locale.US, "%.4f", mLastLocation.getLongitude()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
