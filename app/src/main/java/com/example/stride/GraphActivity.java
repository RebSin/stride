package com.example.stride;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity implements View.OnClickListener{
    //initialize navigation buttons
    public Button goHome;
    public Button goDiary;
    public Button goGraph;
    public Button goStats;

    //declare the bar chart variables
    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntries;

    //variable to check shared preferences
    public static final int DEFAULT = 0;

    //variables for counting the number of food from all categories
    int numHealthy;
    int numUnhealthy;
    int numUnsure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        //get data from shared preferences regarding the category
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        //get information about how much healthy, unhealthy and unsure food the user ate
        numHealthy = sharedPrefs.getInt("Healthy", DEFAULT);
        numUnhealthy = sharedPrefs.getInt("Unhealthy", DEFAULT);
        numUnsure = sharedPrefs.getInt("Unsure", DEFAULT);

        //link barchart to XML file
        barChart = findViewById(R.id.BarChart);
        //call getEntries function to get entries number
        getEntries();

        //set the label of the chart
        barDataSet = new BarDataSet(barEntries, "");
        barData = new BarData(barDataSet);
        barChart.setData(barData);
        //set colours of the char
        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK); //set text colour
        barDataSet.setValueTextSize(18f); //set text size

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
    }
    private void getEntries() {
        barEntries = new ArrayList<>();
        //healthy food based on number in shared preferences
        barEntries.add(new BarEntry(2f, numHealthy));
        //unhealthy food based on number in shared preferences
        barEntries.add(new BarEntry(4f, numUnhealthy));
        //unsure food based on number in shared preferences
        barEntries.add(new BarEntry(6f, numUnsure));
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
    }
}
