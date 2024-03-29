package com.example.stride;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddDiaryEntryActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {
    //declare variables for edit text
    EditText title;
    EditText description;

    //declare variables for radio buttons and button
    RadioButton healthy;
    RadioButton unhealthy;
    RadioButton unsure;
    Button take_photo_button;

    //initialize navigation buttons
    public Button goHome;
    public Button goDiary;
    public Button goGraph;
    public Button goStats;

    //declare variable for the image
    ImageView image;

    //temp will store the bitmap value of the image
    Bitmap temp;

    //this is the request code sent in the intent
    static final int Image_Capture_Code = 1;

    MyDatabase db;
    public String status = "nothing"; //used to check if the user has entered a status
    protected LocationManager locationManager;
    Location location;
    double longitude, latitude;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary_entry);
        //initalizing all textviews//
        title = (EditText) findViewById(R.id.title_editText);
        description = (EditText) findViewById(R.id.description_editText);
        healthy = (RadioButton) findViewById(R.id.healthy_button);
        unhealthy = (RadioButton) findViewById(R.id.unhealthy_button);
        unsure = (RadioButton) findViewById(R.id.unsure_button);
        take_photo_button = (Button) findViewById(R.id.picture_button);
        image = (ImageView) findViewById(R.id.imgCapture);
        //create_entry = (Button) findViewById(R.id.createEntry_button);

        //link navigation buttons to XML
        goHome = (Button) findViewById(R.id.nav_home);
        goDiary = (Button) findViewById(R.id.nav_diary);
        goGraph = (Button) findViewById(R.id.nav_graph);
        goStats = (Button) findViewById(R.id.nav_stats);

        //set listeners for the navgiation
        goHome.setOnClickListener(this);
        goDiary.setOnClickListener(this);
        goGraph.setOnClickListener(this);
        goStats.setOnClickListener(this);

        //getting location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        //getting location

        //checks if recieved permission to use camera. If yes, opens camera
        take_photo_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getCameraPermission();
                if (mCameraPermissionGranted) {
                    Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cInt, Image_Capture_Code);
                }
            }
        });

        //sets onclicklister for the three options
        healthy.setOnClickListener(this);
        unhealthy.setOnClickListener(this);
        unsure.setOnClickListener(this);
        //create_entry.setOnClickListener(this);

        //gets database
        db = new MyDatabase(this);
    }


    @Override
    public void onLocationChanged(Location location) { //if the location has changed, get the new loc
       longitude = location.getLongitude();
       latitude = location.getLatitude();
       if (newDiaryEntryAdded) { //if a new diary entry has been added, then add a new marker
           MapsActivity.cameraActivatedSaveMarker(true, mapsTitle, latitude, longitude, thisId);
           newDiaryEntryAdded = false; //set boolean to false so that it only adds the marker once
       }

   // Log.d("addnewdiaryentry", "long: " + longitude + " lat: " +latitude);
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }


    private static final int PERMISSIONS_REQUEST_CAMERA = 1;
    private boolean mCameraPermissionGranted;
    private void getCameraPermission() {
        /*
         * Requests camera permission
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), //checks if permission already there
                android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            mCameraPermissionGranted = true;
        } else { //if not permision granted, request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA},
                    PERMISSIONS_REQUEST_CAMERA);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //check to see if the correct request code was passed
        if(requestCode == Image_Capture_Code){
            //get bitmap image and set it as the image for the imageView
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(bp);
            temp = bp;
        } else if(resultCode == RESULT_CANCELED){
            //do not set image if the incorrect request code was passed
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
        }
    }
    String mapsTitle;
    boolean newDiaryEntryAdded;
    Long thisId; //this holds the entry id
    public void addDiaryEntry(View view) {
        String name = title.getText().toString();
        mapsTitle = name; //gets the title of the name to pass to the marker
        String type = description.getText().toString();
        Toast.makeText(this, name + type, Toast.LENGTH_SHORT).show();
        //check to see if any of the fields of the diary entry were not entered
        if (status == "nothing" || temp == null || name.length() == 0 || type.length() == 0) {
            //if any of the fields were not entered, display a toast message to notify the user
            Toast.makeText(this, "Please enter all fields to make an entry", Toast.LENGTH_LONG).show();
        } else {
            Long systemTime = System.currentTimeMillis();//gets system milliseconds
            Date currentDate = new Date(systemTime); //turns milliseconds into the date
            String theDate = currentDate.toString(); //sets the date to a string
            //  Log.d("addentry", "the date: " + theDate );

            long id = db.insertData(name, type, status, temp, theDate); //inserts data to database and retrieves the id
            thisId = id; //getting the entry id
            newDiaryEntryAdded = true; //boolean that allows the marker to be created

                if (id < 0) { //if the id is smaller than zero, the data was not stored
                    Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
                } else { //else the data was stored
                    Toast.makeText(this, "success" + " " + name + " " + type, Toast.LENGTH_SHORT).show();
                    //set the boxes back to false so they can be cleared after an entry was made
                    healthy.setChecked(false);
                    unhealthy.setChecked(false);
                    unsure.setChecked(false);
                    image.setImageResource(android.R.color.transparent); //clear the image
                }
                //clear the input boxes after the button is pressed
                title.setText("");
                description.setText("");
                status = "";
            }
    }

    //when button is clicked, go to the diaryactivity to see the results in recyclerview
    public void viewResults (View view){
        Intent intent = new Intent(this, DiaryActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        //go to maps activity if home button is clicked
        if(view.getId() == R.id.nav_home){
            Intent intent = new Intent(view.getContext(), MapsActivity.class);
            startActivity(intent);
        }
        //go to filter diary activity if diary button is clicked
        if(view.getId() == R.id.nav_diary){
            Intent intent = new Intent(view.getContext(), FilterDiaryBySearchActivity.class);
            startActivity(intent);
        }
        //go to graph activity if graph button is clicked
        if(view.getId() == R.id.nav_graph){
            Intent intent = new Intent(view.getContext(), GraphActivity.class);
            startActivity(intent);
        }
        //go to stats activity if stats button is clicked
        if(view.getId() == R.id.nav_stats){
            Intent intent = new Intent(view.getContext(), StatsActivity.class);
            startActivity(intent);
        }
        //checks which button was pressed and sets status to that button
        if(view.getId() == R.id.healthy_button){
            status = "Healthy";
        } else if(view.getId() == R.id.unhealthy_button){
            status = "Unhealthy";
        } else if(view.getId() == R.id.unsure_button){
            status = "Unsure";
        }
    }
}
