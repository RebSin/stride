package com.example.stride;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddDiaryEntryActivity extends AppCompatActivity implements View.OnClickListener{
    EditText title;
    EditText description;
    RadioButton healthy;
    RadioButton unhealthy;
    RadioButton unsure;
    Button take_photo_button;
    ImageView image;
    static final int Image_Capture_Code = 1;

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
        take_photo_button = (Button) findViewById(R.id.picture_button);
        image = (ImageView) findViewById(R.id.imgCapture);

        take_photo_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cInt, Image_Capture_Code);
            }
        });

        healthy.setOnClickListener(this);
        unhealthy.setOnClickListener(this);
        unsure.setOnClickListener(this);

        db = new MyDatabase(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == Image_Capture_Code){
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(bp);
        } else if(resultCode == RESULT_CANCELED){
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
        }
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
