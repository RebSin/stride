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
import java.util.Calendar;

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

    //varibale for storing the day of the week
    int day;

    boolean aDayHasPassed; //global var checking if a day has passed

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
        SharedPreferences.Editor editor = sharedPrefs.edit();

        //get information about how much healthy, unhealthy and unsure food the user ate
        numHealthy = sharedPrefs.getInt("Healthy", DEFAULT);
        numUnhealthy = sharedPrefs.getInt("Unhealthy", DEFAULT);
        numUnsure = sharedPrefs.getInt("Unsure", DEFAULT);
        theGoal = sharedPrefs.getInt("foodgoal", DEFAULT);


        //update the view with the amount of food eaten
        amountHealthy.setText(numHealthy + " Healthy Foods");
        amountUnhealthy.setText(numUnhealthy + " Unhealthy Foods");
        amountUnsure.setText(numUnsure + " Unsure Foods");
        goal.setText("Consuming " + theGoal + " Healthy Foods");

        //check to see if the user has met the goal they entered
        if(theGoal < numHealthy){
            ifGoalMet.setText("You have already met your food goal!");
        } else{
            ifGoalMet.setText("You need to consume " + (theGoal-numHealthy) + " more healthy foods");
        }

        //get day from calandar
        Calendar rightNow = Calendar.getInstance();
        //get day from shared preferences
        int theDay = sharedPrefs.getInt("day", -1);
        //check to see if it is sunday
        if (rightNow.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            //if it is not sunday, the day has changed
            if(theDay != 1){
                //put 1 for sunday in shared preferences
                editor.putInt("day", 1);
                editor.commit();
                //states that a day has passed
                aDayHasPassed = true;
                day = 1;
                //if it is, then the day has not changed
            } else{
                aDayHasPassed = false;
            }
        }
        //check to see if it is monday
        if (rightNow.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
            //if it is not monday, a day has passed
            if(theDay != 2){
                //put 2 for monday in shared preferences
                editor.putInt("day", 2);
                editor.commit();
                //states that a day has passed
                aDayHasPassed = true;
                day = 2;
                //if it is, then the day has not chnaged
            } else{
                aDayHasPassed = false;
            }
        }
        //check to see if it is tuesday
        if (rightNow.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY){
            //if it is not tuesday, a day has passed
            if(day != 3){
                //put 3 for tuesday in shared preferences
                editor.putInt("day", 3);
                editor.commit();
                //states that a day has passed
                aDayHasPassed = true;
                day = 3;
                //if it is, then the day has not changed
            } else{
                aDayHasPassed = false;
            }
        }
        //check to see if it is wednesday
        if (rightNow.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY){
            //if it is not wednesday, a day has passed
            if(day != 4){
                //put 4 for wednesday in shared preferences
                editor.putInt("day", 4);
                editor.commit();
                //states that a day has passed
                aDayHasPassed = true;
                day = 4;
                //if it is, then the day has not changed
            } else{
                aDayHasPassed = false;
            }
        }
        //check to see if it is thursday
        if (rightNow.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY){
            //if it is not thursday, a day has passed
            if(day != 5){
                //put 5 for thursday in shared preferences
                editor.putInt("day", 5);
                editor.commit();
                //states that a day has passed
                aDayHasPassed = true;
                day = 5;
                //if it is, then the day has not changed
            } else{
                aDayHasPassed = false;
            }
        }
        //check to see if it is friday
        if (rightNow.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
            //if it is not friday, a day has passed
            if(day != 6){
                //put 6 for friday in shared preferences
                editor.putInt("day", 6);
                editor.commit();
                //states that a day has passed
                aDayHasPassed = true;
                day = 6;
                //if it is, then the day has not changed
            } else{
                aDayHasPassed = false;
            }
        }
        //check to see if it is saturday
        if (rightNow.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            //if it is not saturday, a day has passed
            if(day != 7){
                //put 7 for saturday in shared preferences
                editor.putInt("day", 7);
                editor.commit();
                //states that a day has passed
                aDayHasPassed = true;
                day = 7;
                //if it is, then the day has not changed
            } else{
                aDayHasPassed = false;
            }
        }
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
            //get shared preferences to get count
            SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putInt("total_steps", (int) sensorEvent.values[0]);
            editor.commit();
            //update the steps on the textView if the user is moving
            tv_steps.setText(String.valueOf((int) sensorEvent.values[0]));
            if (aDayHasPassed == true) {
                //have to minus value from itself to get to zero, else it will not reset
                int val = sharedPrefs.getInt("total_steps", 0);
                //get the steps to minus from the current steps
                editor.putInt("minus_steps", val);
                //set steps to 0
                dailySteps.setText("0");
                //Toast.makeText(this, "" + day, Toast.LENGTH_LONG).show();
            } else {
                //get value to minus by from shared preferences
                int minus = sharedPrefs.getInt("minus_steps", 0);
                //set daily steps to total steps minus the total steps from yesterday 
                int daily_step = (((int) sensorEvent.values[0]) - minus);
                dailySteps.setText(toString().valueOf(daily_step));
                //put the number of steps in shared preferences
                editor.putInt("total_steps", (int) sensorEvent.values[0]);
                editor.commit();
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
