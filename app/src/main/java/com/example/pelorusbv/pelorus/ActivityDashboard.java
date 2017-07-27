package com.example.pelorusbv.pelorus;


import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.annotation.NonNull;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.location.Location;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

import com.google.android.gms.maps.model.LatLng;

import com.google.maps.android.SphericalUtil;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * TODO: keep track of leaderboard
 * TODO: cursor error fixen  
 * TODO: Change datasource 1 to add times of pos
 * TODO: layout beter maken
 */

public class ActivityDashboard extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    protected static final String TAG = "basic-location-sample";
    private static final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 0;

    protected Location mLastLocation;
    Timer sailingTimer;
    TimerTask sailingTimerTask;
    int time;
    LatLng myPos;
    TextView speedText;
    EditText windText;
    TextView headingText;
    TextView timeText;
    TextView latText;
    TextView lngText;
    TextView DTWText;
    TextView VMGText;
    ListView listLeaderboard;

    DataSourceBoat dataSourceBoats;
    DataSourcePositions dataSourcePositions;
    DataSourceCourses dataSourceCourses;
    DataSourceEvents dataSourceEvents;
    long eventID;
    int runID;
    long boatID;

    private GoogleApiClient mGoogleApiClient;

    private Boat myBoat;

    private Buoy Start;
    private Buoy mark1;
    private Buoy mark2;
    private Buoy mark3;
    private Buoy mark4;
    private Buoy currentMark;

    private LocationRequest mLocationRequest;

    Cursor cursorBoat;
    SimpleCursorAdapter dataAdapterLeaderboard;

    MyFragmentPagerAdapter mAdapter;

    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        time = 0;

        dataSourceBoats = new DataSourceBoat(this);
        dataSourcePositions = new DataSourcePositions(this);
        dataSourceCourses = new DataSourceCourses(this);
        dataSourceEvents = new DataSourceEvents(this);

        try {
            dataSourceBoats.open();
            dataSourcePositions.open();
            dataSourceCourses.open();
            dataSourceEvents.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        eventID = pref.getLong("eventID", 0);
        runID = pref.getInt("runID", 0);
        boatID = pref.getLong("boatID",0);

        double[] buoyArray = dataSourceCourses.getBuoyPositions(eventID);

        mark1 = new Buoy(buoyArray[0], buoyArray[1]);
        mark2 = new Buoy(buoyArray[2], buoyArray[3]);
        mark3 = new Buoy(buoyArray[4], buoyArray[5]);
        mark4 = new Buoy(buoyArray[6], buoyArray[7]);
        currentMark = mark1;

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1 * 1000)        // 1 seconds, in milliseconds
                .setFastestInterval(500); // 0.5 second, in milliseconds

//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(mLocationRequest);
//
//        PendingResult<LocationSettingsResult> result =
//                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
//                        builder.build());



    }

    protected void onStart() {
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            dataSourcePositions.open();
            dataSourceCourses.open();
            dataSourceEvents.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sailingTimer = new Timer();

        sailingTimerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        updateDatabase();
                        if (mPager.getCurrentItem()==1){
                        updateLeaderboard();}
                    }
                });

            }
        };

        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
        sailingTimer.scheduleAtFixedRate(sailingTimerTask, 1000, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSourceBoats.close();
        dataSourcePositions.close();
        dataSourceCourses.close();
        dataSourceEvents.close();
        stopManagingCursor(cursorBoat);
        cursorBoat.close();
        sailingTimer.cancel();
        stopLocationUpdates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }


    private void updateBoatPos() {
        if (mLastLocation != null) {
            myBoat.setLocation(mLastLocation);
            if (mPager.getCurrentItem() == 2) {
                mAdapter.fmap.updateBoatPosOnMap(time,dataSourcePositions,runID);
//                mMap.animateCamera(CameraUpdateFactory.newLatLng(myBoat.getPos()));
//                myBoatMarker.setPosition(myBoat.getPos());
//                myBoatMarker.setRotation(myBoat.getHeading(time, dataSourcePositions, runID));
            }
        } else {
            Toast.makeText(this, "no_location_detected", Toast.LENGTH_LONG).show();
        }
    }

    private void updateDatabase(){
        dataSourcePositions.createPosition(time, myBoat.getLocation().getLatitude(),  myBoat.getLocation().getLongitude(), runID);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://download.soft.nl/pelorus/addPos.php?boatid=2&lat=" + myBoat.getLocation().getLatitude() + "&lng=" + myBoat.getLocation().getLongitude();//" + myBoat.getBoatname() + "

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.i(TAG, "Response is: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "That didn't work!");
            }
        });
        queue.add(stringRequest);
    }

    private void updateDisplay() {
        timeText = (TextView) findViewById(R.id.textViewTime);
        windText = (EditText) findViewById(R.id.editTextWind);
        speedText = (TextView) findViewById(R.id.textViewSpeed);
        headingText = (TextView) findViewById(R.id.textViewHeading);
        latText = (TextView) findViewById(R.id.textViewLat);
        lngText = (TextView) findViewById(R.id.textViewLng);
        DTWText = (TextView) findViewById(R.id.textViewDTW);
        VMGText = (TextView) findViewById(R.id.textViewVMG);

        double windAngle;
        if (TextUtils.isEmpty(windText.getText())) {
            windAngle = 0;
        } else {
            windAngle = Double.parseDouble(windText.getText().toString());
        }

        timeText.setText(Integer.toString(time));
        speedText.setText(String.format("%.1f", myBoat.getSpeed()));
        Log.i("Heading: ", Float.toString(myBoat.getHeading()));

        headingText.setText(String.format("%.0f", myBoat.getHeading()));
        latText.setText(String.format("%.4f", myBoat.getLocation().getLatitude()));
        lngText.setText(String.format("%.4f", myBoat.getLocation().getLongitude()));
        double DTW = (SphericalUtil.computeDistanceBetween(new LatLng(myBoat.getLocation().getLatitude(),myBoat.getLocation().getLongitude()), currentMark.getPos()) / 1852);
        DTWText.setText(String.format("%.1f", DTW));
        double VMG = Math.cos(((windAngle - myBoat.getHeading()) / 360) * 2 * Math.PI) * myBoat.getSpeed();
        VMGText.setText(String.format("%.1f", VMG));
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_LOCATION);
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            String boatName = dataSourceBoats.getBoat(boatID);
            myBoat = new Boat(mLastLocation,boatName);
        } else {
           Toast.makeText(this, "no_location_detected", Toast.LENGTH_LONG).show();
        }
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_LOCATION);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        updateBoatPos();
        if(mPager.getCurrentItem()==0){
            updateDisplay();}
        String mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        Log.i(TAG, mLastUpdateTime);
        Log.i(TAG, mLastLocation.toString());
    }

    public void updateLeaderboard(){

        cursorBoat = dataSourceBoats.getBoatList();
        startManagingCursor(cursorBoat);

        dataAdapterLeaderboard  = new SimpleCursorAdapter(
                this,
                R.layout.boat_info,
                cursorBoat,
                new String[]{TableBoat.COLUMN_NAME},
                new int[]{R.id.textViewBoat}
        );

        listLeaderboard = (ListView)findViewById(R.id.listLeaderboard);
        listLeaderboard.setAdapter(dataAdapterLeaderboard);

        listLeaderboard.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
    }

    private static class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        FragmentMap fmap;

        private MyFragmentPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return FragmentDisplay.newInstance();
                    case 1:
                        return FragmentLeaderBoard.newInstance();
                    case 2:
                        fmap = FragmentMap.newInstance();
                        return fmap;
                    default:
                        return null;
                }
            }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }
    }

    public Boat getMyBoat(){
        return myBoat;
    }

}
