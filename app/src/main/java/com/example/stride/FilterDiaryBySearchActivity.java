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
    Button submit_button;
    String clicked_filter = "nothing";
    RadioButton healthy_button;
    RadioButton unhealthy_button;
    RadioButton unsure_button;
    RadioButton all_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_diary_by_search);

        submit_button = (Button) findViewById(R.id.submit_button);
        healthy_button = (RadioButton) findViewById(R.id.healthy_filter_button);
        unhealthy_button = (RadioButton) findViewById(R.id.unhealthy_filter_button);
        unsure_button = (RadioButton) findViewById(R.id.unsure_filter_button);
        all_button = (RadioButton) findViewById(R.id.all_filter_button);

        submit_button.setOnClickListener(this);
        healthy_button.setOnClickListener(this);
        unhealthy_button.setOnClickListener(this);
        unsure_button.setOnClickListener(this);
        all_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.healthy_filter_button){
            clicked_filter = "healthy_filter";
        }
        if(view.getId() == R.id.unhealthy_filter_button){
            clicked_filter = "unhealthy_filter";
        }
        if(view.getId() == R.id.unsure_filter_button){
            clicked_filter = "unsure_filter";
        }
        if(view.getId() == R.id.all_filter_button){
            clicked_filter = "all_filter";
        }
        if(view.getId() == R.id.submit_button){
            if(clicked_filter == "nothing"){
                Toast.makeText(this, "Please Select a Filter", Toast.LENGTH_LONG).show();
            } else{
                SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("filter", clicked_filter);
                editor.commit();

                Intent intent = new Intent(view.getContext(), DiaryActivity.class);
                startActivity(intent);
            }
        }
    }
}
