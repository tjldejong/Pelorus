package com.example.pelorusbv.pelorus;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.location.Location;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;


public class MapsActivity extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener, DisplayFragment.OnFragmentInteractionListener  {

    protected static final String TAG = "basic-location-sample";
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
//    private GoogleApiClient mGoogleApiClient;
    private Boat boat1;
    private Buoy Start;
    private Buoy mark1;
    private Buoy mark2;
    private Buoy mark3;
    private Buoy pampus;

    Timer testTimer;
    TimerTask testTimerTask;

    int time;
    /**
     * Represents a geographical location.
     */
//    protected Location mLastLocation;
//
//    protected TextView mLatitudeText;
//    protected TextView mLongitudeText;

    TextView speedText;
    TextView timeText;
    TextView latText;
    TextView lngText;

    Marker Boat1Marker;

    public PositionsDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        time = 0;
        dataSource = new PositionsDataSource(this);
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        boat1 = new Boat((52.365319), 5.069827);

        mark1 = new Buoy(52.365319, (5.069827+0.01));
        mark2 = new Buoy((52.365319+0.01), 5.069827);
        mark3 = new Buoy(52.365319, (5.069827-0.01));

        pampus = new Buoy(52.365319, 5.069827);

//        buildGoogleApiClient();

        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();


    }


//    protected synchronized void buildGoogleApiClient() {
//       mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

        testTimer = new Timer();

        testTimerTask = new TimerTask() {
            @Override
            public void run(){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        updateBoatPos();
                        updateDatabase();
                        updateDisplay();

                    }
                });

            }
        };

        testTimer.scheduleAtFixedRate(testTimerTask,1000,1000);
    }

    private void updateDisplay(){
        timeText = (TextView)findViewById(R.id.textViewTime);
        speedText =(TextView)findViewById(R.id.textViewSpeed);
        latText = (TextView)findViewById(R.id.textViewLat);
        lngText = (TextView)findViewById(R.id.textViewLng);

        timeText.setText(Integer.toString(time));
        speedText.setText(Double.toString(boat1.getSpeed(1,time,dataSource)));
        latText.setText(Double.toString(boat1.getPos().latitude));
        lngText.setText(Double.toString(boat1.getPos().longitude));
    }

    private void updateBoatPos(){
        boat1.setPos(pampus.getPos().latitude - Math.cos(((double)time)/10)/100, pampus.getPos().longitude + Math.sin(((double)time)/10)/100);
        Boat1Marker.setPosition(boat1.getPos());
    }

    private void updateDatabase(){
        dataSource.createPosition(time,boat1.getPos().latitude,boat1.getPos().longitude);
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

    @Override
    protected void onStart() {
        super.onStart();
//        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
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
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (mLastLocation != null) {
//            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
//            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
//        } else {
//           Toast.makeText(this, "no_location_detected", Toast.LENGTH_LONG).show();
//        }
//
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        //Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
       // Log.i(TAG, "Connection suspended");
//        mGoogleApiClient.connect();
    }
    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        Boat1Marker = mMap.addMarker(new MarkerOptions().position(boat1.getPos()).title("boat1").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.addMarker(new MarkerOptions().position(mark1.getPos()).title("mark1"));
        mMap.addMarker(new MarkerOptions().position(mark2.getPos()).title("mark2"));
        mMap.addMarker(new MarkerOptions().position(mark3.getPos()).title("mark3"));
        mMap.addMarker(new MarkerOptions().position(pampus.getPos()).title("Pampus"));

    }

//    public void testrondje(){
//        timer = new Timer();
//        timer.schedule(new updateboatpos(),1000);
//    }
//
//    class updateboatpos extends TimerTask{
//        public void run(){
//
//        }
//    }

    public void onFragmentInteraction(Uri uri){

    }

}