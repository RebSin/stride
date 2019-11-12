package com.example.stride;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class DetailedViewActivity extends AppCompatActivity implements View.OnClickListener{
    TextView title;
    TextView description;
    TextView status;
    ImageView image;
    public static final String DEFAULT = "not available";
    MyDatabase db;
    Button deleteEntry;
    TextView detailTime;
    TextView detailLoc;
    private MyDatabase databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        title = (TextView) findViewById(R.id.detail_title);
        description = (TextView) findViewById(R.id.detail_description);
        status = (TextView) findViewById(R.id.detail_status);
        image = (ImageView) findViewById(R.id.detail_image);
        deleteEntry = (Button) findViewById(R.id.deleteentry);
        detailTime = (TextView) findViewById(R.id.detail_timestamp);
        detailLoc = (TextView) findViewById(R.id.detail_location);

        deleteEntry.setOnClickListener(this);

        databaseHelper = new MyDatabase(this.getApplicationContext());
        databaseHelper.open();

        Long systemTime = System.currentTimeMillis()/1000;
        String timeInSeconds = systemTime.toString();
        detailTime.setText(timeInSeconds + " seconds");

        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        Float latitude = sharedPrefs.getFloat("latitude", (float) -33.8523341);
        Float longitude = sharedPrefs.getFloat("longitude", (float) 151.2106085);

        detailLoc.setText("Latitude: " + latitude + " Longitude: " + longitude);

        final String theTitle = sharedPrefs.getString("title", DEFAULT);
        String theDescription = sharedPrefs.getString("description", DEFAULT);
        String theStatus = sharedPrefs.getString("status", DEFAULT);
        String theImage = sharedPrefs.getString("image", DEFAULT);

        title.setText(theTitle.toString());
        description.setText(theDescription.toString());
        status.setText(theStatus.toString());
        //convert from base64 to bitmap to display the image
        Bitmap temp = decodeBase64(theImage);
        image.setImageBitmap(temp);
        deleteEntry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                db.removeSingleContact(theTitle);
            }
        });
        db = new MyDatabase(this);
    }


    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.deleteentry){
            final int UID = databaseHelper.GetUserID("diarydatabase", title.getText().toString());
            databaseHelper.deleteEntry(UID);
            Toast.makeText(this, "The entry has been deleted", Toast.LENGTH_SHORT).show();
        }
    }
}
