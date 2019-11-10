package com.example.stride;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

//this displays our main homepage with the map
public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener, View.OnClickListener,
        OnMapReadyCallback, SensorEventListener {
    private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);
    private Marker mBrisbane;
    private GoogleMap mMap;
    public Button searchButton;
    public Button diaryButton;
    public Button graphButton;
    public EditText searchMe;
    private SensorManager sensorManager;
    public int healthyCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        context = this;
        searchButton = (Button) findViewById(R.id.search_button);
        diaryButton = (Button) findViewById(R.id.diary_button);
        graphButton = (Button) findViewById(R.id.graph_button);
        searchMe = (EditText) findViewById(R.id.searchEditText);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        searchButton.setOnClickListener(this); //setting onclick listeners
        diaryButton.setOnClickListener(this);
        graphButton.setOnClickListener(this);
        mapFragment.getMapAsync(this);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); //accessing sensors for accelerometer
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
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
        // Add a marker in Sydney and move the camera
        mBrisbane = mMap.addMarker(new MarkerOptions()
                .position(BRISBANE)
                .title("Brisbane"));
        mBrisbane.setTag(0);
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);
    }

///////////////////////////////////////////////////////

    public double lastLatitude;
    public double lastLongitude;
    public LatLng thisLocHere;
    public int tagNumber;

    public void cameraActivatedSaveMarker(boolean photoTaken, String title) { //this is called
        //if the camera has taken a photo and it takes the photo title
        //it then reads the location of the device, which should be where the photo was taken
        //then it sets a new marker to the map
        if (photoTaken) {
            thisLocHere = new LatLng(lastLatitude, lastLongitude);
            Marker thisMarkerHere;

            thisMarkerHere = mMap.addMarker(new MarkerOptions()
                    .position(thisLocHere)
                    .title(title));
            thisMarkerHere.setTag(tagNumber);
        }
        tagNumber++;
    }
///////////////////////////////////////////////////////////////
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085); //default loc if permission not granted
    private static final int DEFAULT_ZOOM = 15; //default zoom
    private Location mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private void getDeviceLocation() {
        /*
         * Get the location of the device if permission has been granted.
         */
    /*    try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                lastLatitude = mLastKnownLocation.getLatitude();
                                lastLongitude = mLastKnownLocation.getLongitude();
                            } else {
                                Log.d("MapsActivity", "Current location is null.");
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                            }
//                            cameraActivatedSaveMarker(true, "IM TESTING IT");
                        } else {
                            Log.d("MapsActivity", "Current location is null. Using defaults.");
                            Log.e("MapsActivity", "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }*/
    }


    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() { //this updates the location
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();

            Intent markerLoc = new Intent(this, MarkerClickDetailResults.class); //declares the
            markerLoc.putExtra("Loc_Name", marker.getTitle());
            startActivity(markerLoc);
        }
        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.search_button) { //this checks if search button pressed
            if (searchMe.getText().toString().length() > 0){ //this checks if there was something typed
                String word_entered = searchMe.getText().toString(); //this gets what was typed
                Uri webpage = Uri.parse("https://en.wikipedia.org/wiki/" + word_entered);
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(webIntent); //this starts the intent
            }

        }
        if(view.getId() == R.id.diary_button){
            Intent intent = new Intent(view.getContext(), DiaryActivity.class);
            startActivity(intent);
        }
        if(view.getId() == R.id.graph_button){
            Intent intent = new Intent(view.getContext(), GraphActivity.class);
            startActivity(intent);
        }
    }
    float motionValue; //holds current motion value
    float prevMotion; //holds previous motion value
    private float motionVal; //holds the motion value
    boolean moving;
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            int xValue = (int) event.values[0]; //this retrieves the first value
            int yValue = (int) event.values[1]; //this retrieves the second value
            int zValue = (int) event.values[2]; //this retrieves the third value
            prevMotion = motionValue; //previous motion is set to current motion
            motionValue = (float) Math.sqrt(xValue * xValue + yValue * yValue + zValue * zValue);
            //does math to get current motion value
            float calc = motionValue - prevMotion; //gets the motion difference from original
            motionVal = motionVal * 0.2f + calc; //multiplies prev motion and adds in the difference
                if (motionVal > 0.1) { //if the device motion is higher
                    //the user is moving.
                    moving = true;
                } else {
                    moving = false;
                    oneHourCountdown();
                }
        }
    }

    ////////////////////////////////////////////////////////
    boolean oneHourHasPassed;
    long originalTime = System.currentTimeMillis();
    Context context;
    public void oneHourCountdown() {
        long nowTime = System.currentTimeMillis();
        if(nowTime >= (originalTime + 3600*1000) && !moving) { //multiply by one hour in seconds by 1000 to get milliseconds
            oneHourHasPassed = true;
            originalTime = System.currentTimeMillis(); //update originalTime to new time
            Toast toast = Toast.makeText(context, "You haven't moved in one hour!", Toast.LENGTH_SHORT);
            toast.show(); //displays the toast
        } else {
            oneHourHasPassed = false;
//            Log.d("MapsActivity", "Time has NOT passed");
        }
    }

    ////////////////////////////////////////////////////
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
