package com.example.stride;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

//this displays a page with all the details and saved image
//after the user clicks on a marker in MapsActivity
public class MarkerClickDetailResults extends AppCompatActivity {
    MyDatabase db;
    TextView title, description, status, date;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_marker_click_detail_results);
        setContentView(R.layout.activity_detailed_view);
        db = new MyDatabase(this); //initalize database
        Intent markerLoc = getIntent(); //get intent
        String locName = markerLoc.getStringExtra("Loc_Name"); //get string name from intent
   //     Log.d("MarkerClickDetailResul", "name: " + locName);

        //retrieve from database
        String diaryId = markerLoc.getStringExtra("id"); //get id of this
        Long thisId = Long.valueOf(diaryId); //parse the id back into a long
        String Type = db.getSelectedType(thisId); //get info from the database
        String Status = db.getSelectedStatus(thisId); //get info from the database
        String Date = db.getSelectedDate(thisId);//get info from the database
        String Image = db.getSelectedImage(thisId);//get info from the database
        Log.d("MarkerClick", "Image: " + Image);

        //initalize and set values
        Bitmap temp = decodeBase64(Image);
        image = (ImageView) findViewById(R.id.detail_image);
        image.setImageBitmap(temp);

        title = (TextView) findViewById(R.id.detail_title);
        title.setText(locName);

        description = (TextView) findViewById(R.id.detail_description);
        description.setText(Type);

        status = (TextView) findViewById(R.id.detail_status);
        status.setText(Status);

        date = (TextView) findViewById(R.id.detail_timestamp);
        date.setText(Date);
//        deleteEntry = (Button) findViewById(R.id.deleteentry);
//        detailLoc = (TextView) findViewById(R.id.detail_location);

    }
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
