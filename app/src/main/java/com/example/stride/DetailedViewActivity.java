package com.example.stride;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class DetailedViewActivity extends AppCompatActivity {
    TextView title;
    TextView description;
    TextView status;
    ImageView image;
    public static final String DEFAULT = "not available";
    MyDatabase db;
    Button deleteEntry;
    TextView detailTime;
    TextView detailLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        //initalizing all textviews, buttons and imageview
        title = (TextView) findViewById(R.id.detail_title);
        description = (TextView) findViewById(R.id.detail_description);
        status = (TextView) findViewById(R.id.detail_status);
        image = (ImageView) findViewById(R.id.detail_image);
        deleteEntry = (Button) findViewById(R.id.deleteentry);
        detailTime = (TextView) findViewById(R.id.detail_timestamp);
        detailLoc = (TextView) findViewById(R.id.detail_location);

        Long systemTime = System.currentTimeMillis()/1000; //getting the system time in milli, dividing into 1000 for seconds
        String timeInSeconds = systemTime.toString(); //sets system time long to string
        detailTime.setText(timeInSeconds + " seconds"); //sets detailed time to time in seconds

        //gets shared preferences. long and lat are retrieved from preferences
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        Float latitude = sharedPrefs.getFloat("latitude", (float) -33.8523341);
        Float longitude = sharedPrefs.getFloat("longitude", (float) 151.2106085);

        detailLoc.setText("Latitude: " + latitude + " Longitude: " + longitude); //long and lat are displayed

        //retrieving more info from shared pref
        final String theTitle = sharedPrefs.getString("title", DEFAULT);
        String theDescription = sharedPrefs.getString("description", DEFAULT);
        String theStatus = sharedPrefs.getString("status", DEFAULT);
        String theImage = sharedPrefs.getString("image", DEFAULT);
        Log.d("detailedviewactivity", "theImage: " + theImage);

        //setting the info
        title.setText(theTitle.toString());
        description.setText(theDescription.toString());
        status.setText(theStatus.toString());
        //convert from base64 to bitmap to display the image
        Bitmap temp = decodeBase64(theImage);
        image.setImageBitmap(temp);

        //if delete entry button has been pressed
        deleteEntry.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                db.removeAllInfo(theTitle);
                                Intent intent = new Intent(v.getContext(), DiaryActivity.class);//open the diaryactivity
                                startActivity(intent);
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                //db.removeSingleContact(theTitle);
            }
        });
        db = new MyDatabase(this);
    }


    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
