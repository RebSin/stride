package com.example.stride;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {
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
        barDataSet.setValueTextSize(18f); //set text sie
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
}
