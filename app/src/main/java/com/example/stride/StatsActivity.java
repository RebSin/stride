package com.example.stride;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class StatsActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {
    //initialize navigation buttons
    public Button goHome;
    public Button goDiary;
    public Button goGraph;
    public Button goStats;

    //initialize textViews
    public TextView amountHealthy;
    public TextView amountUnhealthy;
    public TextView amountUnsure;
    public TextView goal;
    public TextView ifGoalMet;
    public TextView tv_steps;
    public TextView dailySteps;

    //check whether or not the user is running
    boolean running = false;

    //initialize sensorManager for step counter
    SensorManager sensorManager;

    //variables for counting the number of food from all categories
    int numHealthy;
    int numUnhealthy;
    int numUnsure;
    int theGoal;

    //variable to check shared preferences
    public static final int DEFAULT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        //link navigation buttons to XML
        goHome = (Button) findViewById(R.id.nav_home);
        goDiary = (Button) findViewById(R.id.nav_diary);
        goGraph = (Button) findViewById(R.id.nav_graph);
        goStats = (Button) findViewById(R.id.nav_stats);

        //link textViews to XML
        amountHealthy = (TextView) findViewById(R.id.healthy_textView);
        amountUnhealthy = (TextView) findViewById(R.id.unhealthy_textView);
        amountUnsure = (TextView) findViewById(R.id.unsure_textView);
        goal = (TextView) findViewById(R.id.goal_textView);
        ifGoalMet = (TextView) findViewById(R.id.goal_status_textView);

        //set listeners for the navgiation
        goHome.setOnClickListener(this);
        goDiary.setOnClickListener(this);
        goGraph.setOnClickListener(this);
        goStats.setOnClickListener(this);

        //for step counter
        tv_steps = (TextView) findViewById(R.id.step_number_textView);
        dailySteps = (TextView) findViewById(R.id.dailySteps);

        //get sensor manager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //get data from shared preferences regarding the category
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        //get information about how much healthy, unhealthy and unsure food the user ate
        numHealthy = sharedPrefs.getInt("Healthy", DEFAULT);
        numUnhealthy = sharedPrefs.getInt("Unhealthy", DEFAULT);
        numUnsure = sharedPrefs.getInt("Unsure", DEFAULT);
        theGoal = sharedPrefs.getInt("foodgoal", DEFAULT);


        //update the view with the amount of food eaten
        amountHealthy.setText(numHealthy + " Healthy Foods");
        amountUnhealthy.setText(numUnhealthy + " Unhealthy Foods");
        amountUnsure.setText(numUnsure + " Unsure Foods");
        goal.setText("Consming " + theGoal + " Healthy Foods");

        //check to see if the user has met the goal they entered
        if(theGoal < numHealthy){
            ifGoalMet.setText("You have already met your food goal!");
        } else{
            ifGoalMet.setText("You need to consume " + (theGoal-numHealthy) + " more healthy foods");
        }
        String theDay = sharedPrefs.getString("day", "Sunday"); //this gets the day of the week from shared profs
        if (!checkTheDay().equals(theDay)) { //if the current day doesn't match the saved day
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString("day", checkTheDay()); //put the current day into shared prefs
            aDayHasPassed = true; //a day has passed
        } else {
            aDayHasPassed = false; //else a day has not passed
        }
    }
    boolean aDayHasPassed = false; //global var checking if a day has passed
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

    @Override
    protected void onResume(){
        super.onResume();
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null){
            //check to see if the sensor is not null before registering it 
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else{
            //display toast if sensor is not found
            Toast.makeText(this, "Sensor is not found!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause(){
        //if you unregister the hardware will stop detecting steps so do not do this
        //sensorManager.unregisterListener(this);
        super.onPause();
        running = false;
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(running){
            //update the steps on the textView if the user is moving
            tv_steps.setText(String.valueOf(sensorEvent.values[0]));
            if (aDayHasPassed) {
                dailySteps.setText("0");
            } else {
                dailySteps.setText(String.valueOf(sensorEvent.values[0]));
            }
        }
    }

    public String checkTheDay() { //this checks and returns the current day
        LocalDate date = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            date = LocalDate.now(); //the if statements are just checks cause this only
            //works on a certain build version
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DayOfWeek theDay = date.getDayOfWeek(); //this is the current day of the week
            return String.valueOf(theDay); //return the value of the day of the week
        }
        return null;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
