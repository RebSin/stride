package com.example.stride;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class FilterDiaryBySearchActivity extends AppCompatActivity implements View.OnClickListener {
    //declare vairables for the button/ radio button
    Button submit_button;
    RadioButton healthy_button;
    RadioButton unhealthy_button;
    RadioButton unsure_button;
    RadioButton all_button;

    //used to make sure the user selects a filter
    String clicked_filter = "nothing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_diary_by_search);

        //link the submit button and status radio buttons to the XML layout
        submit_button = (Button) findViewById(R.id.submit_button);
        healthy_button = (RadioButton) findViewById(R.id.healthy_filter_button);
        unhealthy_button = (RadioButton) findViewById(R.id.unhealthy_filter_button);
        unsure_button = (RadioButton) findViewById(R.id.unsure_filter_button);
        all_button = (RadioButton) findViewById(R.id.all_filter_button);

        //set listeners on all buttons to see when they are clicked
        submit_button.setOnClickListener(this);
        healthy_button.setOnClickListener(this);
        unhealthy_button.setOnClickListener(this);
        unsure_button.setOnClickListener(this);
        all_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //if the id matches the healthy filter, then the user wants to filter by healthy food
        if(view.getId() == R.id.healthy_filter_button){
            clicked_filter = "healthy_filter";
        }
        //if the id matches the unhealthy filter, then the user wants to filter by unhealthy food
        if(view.getId() == R.id.unhealthy_filter_button){
            clicked_filter = "unhealthy_filter";
        }
        //if the id matches the unsure filter, then the user wants to filter by unsure food
        if(view.getId() == R.id.unsure_filter_button){
            clicked_filter = "unsure_filter";
        }
        //if the id matches the all filter, then the user wants to see all diary entries
        if(view.getId() == R.id.all_filter_button){
            clicked_filter = "all_filter";
        }
        if(view.getId() == R.id.submit_button){
            //if the user has not chosen a filter, display a toast message
            if(clicked_filter == "nothing"){
                Toast.makeText(this, "Please Select a Filter", Toast.LENGTH_LONG).show();
            } else{
                //if a filter has been chosen, put this information in shared preferences
                SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("filter", clicked_filter);
                editor.commit();

                //use an activity to proceed to the diaryActivity
                Intent intent = new Intent(view.getContext(), DiaryActivity.class);
                startActivity(intent);
            }
        }
    }
}
