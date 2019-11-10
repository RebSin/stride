package com.example.stride;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class AddDiaryEntryActivity extends AppCompatActivity implements View.OnClickListener{
    EditText title;
    EditText description;
    RadioButton healthy;
    RadioButton unhealthy;
    RadioButton unsure;
    MyDatabase db;
    public String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary_entry);

        title = (EditText) findViewById(R.id.title_editText);
        description = (EditText) findViewById(R.id.description_editText);
        healthy = (RadioButton) findViewById(R.id.healthy_button);
        unhealthy = (RadioButton) findViewById(R.id.unhealthy_button);
        unsure = (RadioButton) findViewById(R.id.unsure_button);

        healthy.setOnClickListener(this);
        unhealthy.setOnClickListener(this);
        unsure.setOnClickListener(this);

        db = new MyDatabase(this);
    }

    public void addDiaryEntry(View view){
        String name = title.getText().toString();
        String type = description.getText().toString();
        Toast.makeText(this, name + type, Toast.LENGTH_SHORT).show();
        long id = db.insertData(name, type, status);
        if(id < 0){
            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this, "success" + " " + name + " " + type, Toast.LENGTH_SHORT).show();
        }
        //clear the input boxes after the button is pressed
        title.setText("");
        description.setText("");
        status = "";
    }

    //when button is clicked, go to the diaryactivity to see the results in recyclerview
    public void viewResults (View view){
        Intent intent = new Intent(this, DiaryActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.healthy_button){
            status = "Healthy";
        } else if(view.getId() == R.id.unhealthy_button){
            status = "Unhealthy";
        } else if(view.getId() == R.id.unsure_button){
            status = "Unsure";
        }
    }
}
