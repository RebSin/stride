package com.example.stride;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

public class DetailedViewActivity extends AppCompatActivity {
    TextView title;
    TextView description;
    TextView status;
    ImageView image;
    public static final String DEFAULT = "not available";
    MyDatabase db;
    Button deleteEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        title = (TextView) findViewById(R.id.detail_title);
        description = (TextView) findViewById(R.id.detail_description);
        status = (TextView) findViewById(R.id.detail_status);
        image = (ImageView) findViewById(R.id.detail_image);
        deleteEntry = (Button) findViewById(R.id.deleteentry);

        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

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
}
