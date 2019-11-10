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
    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntries;
    MyDatabase db;
    public static final int DEFAULT = 0;

    int numHealthy = 0;
    int numUnhealthy = 0;
    int numUnsure = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        numHealthy = sharedPrefs.getInt("healthy", DEFAULT);
        numUnhealthy = sharedPrefs.getInt("unhealthy", DEFAULT);
        numHealthy = sharedPrefs.getInt("unsure", DEFAULT);

        barChart = findViewById(R.id.BarChart);
        getEntries();
        barDataSet = new BarDataSet(barEntries, "");
        barData = new BarData(barDataSet);
        barChart.setData(barData);
        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(18f);
    }
    private void getEntries() {
        barEntries = new ArrayList<>();
        //healthy
        barEntries.add(new BarEntry(2f, numHealthy));
        //unhealthy
        barEntries.add(new BarEntry(4f, numUnhealthy));
        //unsure
        barEntries.add(new BarEntry(6f, numUnsure));
    }
}
