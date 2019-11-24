package com.example.stride;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class StatsActivity extends AppCompatActivity implements View.OnClickListener{
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

        if(theGoal < numHealthy){
            ifGoalMet.setText("You have already met your food goal!");
        } else{
            ifGoalMet.setText("You need to consume " + (theGoal-numHealthy) + " more healthy foods");
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
}
