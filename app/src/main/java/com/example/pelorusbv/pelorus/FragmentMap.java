package com.example.pelorusbv.pelorus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by tobia on 25-7-2017.
 * TODO: markers voor boeien maken
 * TODO: andere schepen laten zien
 */

public class FragmentMap extends Fragment {

    Boat myBoat;
    Marker myBoatMarker;
    MapView mMapView;
    private GoogleMap googleMap;

    public FragmentMap() {
    }

    public static FragmentMap newInstance() {
        return new FragmentMap();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myBoat = ((ActivityDashboard) getActivity()).getMyBoat();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                myBoatMarker = googleMap.addMarker(new MarkerOptions()
                        .position(myBoat.getPos())
                        .title("myBoat")
                        .flat(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.boat)));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myBoat.getPos(),15));
            }
        });

        return rootView;
    }

    public void updateBoatPosOnMap(int time, DataSourcePositions dataSourcePositions, int runID){
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myBoat.getPos(),15));
        myBoatMarker.setPosition(myBoat.getPos());
        myBoatMarker.setRotation(myBoat.getHeading(time, dataSourcePositions, runID));
    }

}
