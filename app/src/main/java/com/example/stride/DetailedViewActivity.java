package com.example.stride;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        title = (TextView) findViewById(R.id.detail_title);
        description = (TextView) findViewById(R.id.detail_description);
        status = (TextView) findViewById(R.id.detail_status);
        image = (ImageView) findViewById(R.id.detail_image);

        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        String theTitle = sharedPrefs.getString("title", DEFAULT);
        String theDescription = sharedPrefs.getString("description", DEFAULT);
        String theStatus = sharedPrefs.getString("status", DEFAULT);
        String theImage = sharedPrefs.getString("image", DEFAULT);

        title.setText(theTitle.toString());
        description.setText(theDescription.toString());
        status.setText(theStatus.toString());
        //convert from base64 to bitmap to display the image
        Bitmap temp = decodeBase64(theImage);
        image.setImageBitmap(temp);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
