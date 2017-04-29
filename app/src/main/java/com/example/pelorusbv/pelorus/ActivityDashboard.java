package com.example.pelorusbv.pelorus;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.location.Location;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class ActivityDashboard extends FragmentActivity implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener, View.OnClickListener, LocationListener {
    //Kaartje met huidige locatie, locatie van vorige run en de boeien. Laat ook alle meters zien. LocationListener, ConnectionCallbacks, OnConnectionFailedListener, FragmentDisplay.OnFragmentInteractionListener,
    protected static final String TAG = "basic-location-sample";
    private static final Boolean REQUESTING_LOCATION_UPDATES_KEY = true;
    private static final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 0;
    /**
     * Represents a geographical location.
     */
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
    Marker boat1Marker;
    Marker myBoatMarker;
    DataSourceBoat dataSourceBoats;
    DataSourcePositions dataSourcePositions;
    DataSourceCourses dataSourceCourses;
    DataSourceEvents dataSourceEvents;
    long eventID;
    int runID;
    Button buttonHS;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    //
//    protected TextView mLatitudeText;
//    protected TextView mLongitudeText;
    private GoogleApiClient mGoogleApiClient;
    private Boat boat1;
    private Boat myBoat;
    private Buoy Start;
    private Buoy mark1;
    private Buoy mark2;
    private Buoy mark3;
    private Buoy mark4;
    private Buoy pampus;
    private Buoy currentMark;
    private LocationRequest mLocationRequest;

    ListView listLeaderboard;

    SimpleCursorAdapter dataAdapterLeaderboard;

    Cursor cursorBoat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Log.i("runID", String.format("%d", runID));
        Log.i("eventID", String.format("%d", eventID));

        double[] buoyArray = dataSourceCourses.getBuoyPositions(eventID);

        boat1 = new Boat((52.365319), 5.069827);
        myBoat = new Boat(52.365319, 5.069827);

        mark1 = new Buoy(buoyArray[0], buoyArray[1]);
        mark2 = new Buoy(buoyArray[2], buoyArray[3]);
        mark3 = new Buoy(buoyArray[4], buoyArray[5]);
        mark4 = new Buoy(buoyArray[6], buoyArray[7]);
        currentMark = mark1;
        pampus = new Buoy(52.365319, 5.069827);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        setContentView(R.layout.activity_maps);
        buttonHS = (Button) findViewById(R.id.buttonhs);
        buttonHS.setOnClickListener(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1 * 1000)        // 1 seconds, in milliseconds
                .setFastestInterval(500); // 0.5 second, in milliseconds

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        listLeaderboard = (ListView)findViewById(R.id.listLeaderboard);

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
                        updateLeaderboard();
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
            Log.i(TAG, "updateBoatPos: ja lastlocation");
            myBoat.setPos(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLng(myBoat.getPos()));
            myBoatMarker.setPosition(myBoat.getPos());
            myBoatMarker.setRotation(myBoat.getHeading(time, dataSourcePositions, runID));
        } else {
            Toast.makeText(this, "no_location_detected", Toast.LENGTH_LONG).show();
        }
        if ((runID > 1) && (dataSourcePositions.getPosLat(time, runID - 1) != 0) && (dataSourcePositions.getPosLng(time, runID - 1) != 0)) {
            boat1.setPos(dataSourcePositions.getPosLat(time, runID - 1), dataSourcePositions.getPosLng(time, runID - 1));
            boat1Marker.setPosition(boat1.getPos());
            boat1Marker.setRotation(boat1.getHeading(time, dataSourcePositions, runID - 1));
        }
    }

    private void updateDatabase(){
        dataSourcePositions.createPosition(time, myBoat.getPos().latitude, myBoat.getPos().longitude, runID);
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
        speedText.setText(String.format("%.1f", myBoat.getSpeed(time, dataSourcePositions, runID)));
        double heading = myBoat.getHeading(time, dataSourcePositions, runID);
        if (heading < 0) {
            heading = 360 + heading;
        }
        headingText.setText(String.format("%.0f", heading));
        latText.setText(String.format("%.4f", myBoat.getPos().latitude));
        lngText.setText(String.format("%.4f", myBoat.getPos().longitude));
        double DTW = (SphericalUtil.computeDistanceBetween(myBoat.getPos(), currentMark.getPos()) / 1852);
        Log.i(TAG, Double.toString(DTW));
        DTWText.setText(String.format("%.1f", DTW));
        double VMG = Math.cos(((windAngle - heading) / 360) * 2 * Math.PI) * myBoat.getSpeed(time, dataSourcePositions, runID);
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
            myPos = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            myBoat = new Boat(myPos.latitude, myPos.longitude);
            mMap.animateCamera(CameraUpdateFactory.newLatLng(myPos));
            myBoatMarker = mMap.addMarker(new MarkerOptions()
                    .position(myBoat.getPos())
                    .title("myBoat")
                    .flat(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.boat)));
        } else {
           Toast.makeText(this, "no_location_detected", Toast.LENGTH_LONG).show();
        }

        if (runID > 1) {
            boat1 = new Boat(dataSourcePositions.getPosLat(2, runID - 1), dataSourcePositions.getPosLng(2, runID - 1));
            boat1Marker = mMap.addMarker(new MarkerOptions()
                    .position(boat1.getPos())
                    .title("LastRun")
                    .flat(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.boat_red)));
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

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        boat1Marker = mMap.addMarker(new MarkerOptions()
                .position(boat1.getPos())
                .title("boat1")
                .flat(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.boat)));
        mMap.addMarker(new MarkerOptions().position(mark1.getPos()).title("mark1"));
        mMap.addMarker(new MarkerOptions().position(mark2.getPos()).title("mark2"));
        mMap.addMarker(new MarkerOptions().position(mark3.getPos()).title("mark3"));
        mMap.addMarker(new MarkerOptions().position(mark4.getPos()).title("mark4"));
        mMap.addMarker(new MarkerOptions().position(pampus.getPos()).title("Pampus"));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        if (v == buttonHS) {
            TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
            LinearLayout leaderbordLayout = (LinearLayout) findViewById(R.id.LinearLayoutLeaderbord);
            if (tableLayout.getLayoutParams().height != 0) {
                tableLayout.getLayoutParams().height = 0;
                leaderbordLayout.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
                buttonHS.setText("Show Dashboard");
            } else {
                leaderbordLayout.getLayoutParams().height = 0;
                tableLayout.getLayoutParams().height = TableLayout.LayoutParams.WRAP_CONTENT;
                buttonHS.setText("Show Leaderboard");
            }
            tableLayout.requestLayout();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        updateBoatPos();
        updateDisplay();
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

        listLeaderboard.setAdapter(dataAdapterLeaderboard);

        listLeaderboard.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;




    }
}
