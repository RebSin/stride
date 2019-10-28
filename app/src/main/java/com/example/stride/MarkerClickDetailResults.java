package com.example.stride;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

//this displays a page with all the details and saved image
public class MarkerClickDetailResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_click_detail_results);
        Intent markerLoc = getIntent();
        String locName = markerLoc.getStringExtra("Loc_Name");
        Log.d("MarkerClickDetailResul", locName);
    }
}
