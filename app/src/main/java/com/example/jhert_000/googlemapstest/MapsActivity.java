package com.example.jhert_000.googlemapstest;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.location.Location;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.maps.model.PolylineOptions;
import android.view.View.OnClickListener;

import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener, OnClickListener {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int INTERVAL_REFRESH = 10 * 1000;   // 10 seconds

    private GoogleMap mMap;
    private GoogleApiClient gAC;
    private Timer timer;


    private Button backButton;
    private Button favButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        gAC = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        backButton = (Button) findViewById(R.id.backButton);
        favButton = (Button) findViewById(R.id.favButton);
    }


    public void onClick(View v){
        switch(v.getId()){
            case R.id.backButton:
                //code for moving back to cache select screen
                break;
            case R.id.favButton:
                //code for favoriting and saving a cache to be continued
                break;
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        setCurrentLocationMarker();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // if GoogleMap object is not already available, get it
        if (mMap == null) {
            FragmentManager manager = getSupportFragmentManager();
            SupportMapFragment fragment =
                    (SupportMapFragment) manager.findFragmentById(R.id.map);
            mMap = fragment.getMap();
        }

        // if GoogleMap object is available, configure it
        if (mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }

        gAC.connect();
    }

    @Override
    protected void onStop() {
        gAC.disconnect();

        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if returning from connection failed resolution activity...
        if (requestCode == CONNECTION_FAILURE_RESOLUTION_REQUEST) {
            // do additional processing
        }
    }

    //**************************************************************
    // Private methods
    //****************************************************************
    private void updateMap(){
        if (gAC.isConnected()){
            setCurrentLocationMarker();
        }
        displayRun();
    }

    private void setCurrentLocationMarker(){
        if (mMap != null) {
            // get current location
            if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }


            Location location = LocationServices.FusedLocationApi
                    .getLastLocation(gAC);

            if (location != null) {
                // zoom in on current location
                mMap.animateCamera(
                        CameraUpdateFactory.newCameraPosition(
                                new CameraPosition.Builder()
                                        .target(new LatLng(location.getLatitude(),
                                                location.getLongitude()))
                                        .zoom(16.5f)
                                        .bearing(0)
                                        .tilt(25)
                                        .build()));

                // add a marker for the current location
                mMap.clear();      // clear old marker(s)
                mMap.addMarker(    // add new marker
                        new MarkerOptions()
                                .position(new LatLng(location.getLatitude(),
                                        location.getLongitude()))
                                .title("You are here"));
            }
        }
    }

    private void displayRun(){
        if (mMap != null) {
            //locationList = db.getLocations();
            PolylineOptions polyline = new PolylineOptions();
            /*if (locationList.size() > 0) {
                for (Location l : locationList) {
                    LatLng point = new LatLng(
                            l.getLatitude(), l.getLongitude());
                    polyline.add(point);
                }
            }
            map.addPolyline(polyline);*/
        }
    }

    private void setMapToRefresh(){
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                MapsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateMap();
                    }
                });
            }
        };
        timer.schedule(task, INTERVAL_REFRESH, INTERVAL_REFRESH);
    }

    //**************************************************************
    // Implement ConnectionCallbacks interface
    //****************************************************************
    @Override
    public void onConnected(Bundle dataBundle) {
        updateMap();
        setMapToRefresh();
    }

    @Override
    public void onConnectionSuspended(int i) {
        timer.cancel();
        Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
    }

    //**************************************************************
    // Implement OnConnectionFailedListener
    //****************************************************************
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // if Google Play services can resolve the error, display activity
        if (connectionResult.hasResolution()) {
            try {
                // start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            }
            catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
        else {
            new AlertDialog.Builder(this)
                    .setMessage("Connection failed. Error code: "
                            + connectionResult.getErrorCode())
                    .show();
        }
    }


}
