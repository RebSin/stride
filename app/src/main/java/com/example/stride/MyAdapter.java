package com.example.stride;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import static com.example.stride.R.layout.row;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    //create an array of the diary entries from the db
    public ArrayList<String> list;
    Context context;

    public MyAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //create and return viewholder
        View v = LayoutInflater.from(parent.getContext()).inflate(row, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //parse the array of diary entried by the comma
        String[] results = (list.get(position).toString()).split(",");
        holder.titleTextView.setText(results[0]); //the first entry is for the title
        holder.descriptionTextView.setText(results[1]); //the second entry is for the description
        holder.statusTextView.setText(results[2]); //the third entry is for the stats
        holder.imageView.setImageBitmap(getBitmapFromEncodedString(results[3])); //the fourth entry is for the image
    }


    public Bitmap getBitmapFromEncodedString(String encodedString){
        //convert the string for the image into a bitmpa
        byte[] arr = Base64.decode(encodedString, Base64.URL_SAFE);
        Bitmap img = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        //return the bitmap so it can be displayed in an imageView
        return img;
    }

    @Override
    //get the size of the list of diary entries
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //initalize variables for the textViews
        public TextView titleTextView;
        public TextView descriptionTextView;
        public TextView statusTextView;
        public ImageView imageView; //initialize the variable for the image
        public LinearLayout myLayout; //create a linear layout

        Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            //make linear layout
            myLayout = (LinearLayout) itemView;

            //link the components of the diary entry to the XML layout
            titleTextView = (TextView) itemView.findViewById(R.id.titleEntry);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionEntry);
            statusTextView = (TextView) itemView.findViewById(R.id.statusEntry);
            imageView = (ImageView) itemView.findViewById(R.id.imageEntry);

            //set an onClick listener to each diary entry
            itemView.setOnClickListener(this);
            context = itemView.getContext();
        }

        @Override
        public void onClick(View view) {
            //notify the user about what entry they have clicked
            Toast.makeText(context,
                    "You have clicked " + ((TextView)view.findViewById(R.id.titleEntry)).getText().toString(),
                    Toast.LENGTH_SHORT).show();
            //first convert image into bitmap and store in temp
            ImageView bitmap = ((ImageView)view.findViewById(R.id.imageEntry));
            //set view to bitmap image
            Bitmap temp = convertImageViewToBitmap(bitmap);
            //use shared preferences to store the contents of the diary entry
            SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("title", ((TextView)view.findViewById(R.id.titleEntry)).getText().toString());
            editor.putString("description", ((TextView)view.findViewById(R.id.descriptionEntry)).getText().toString());
            editor.putString("status", ((TextView)view.findViewById(R.id.statusEntry)).getText().toString());
            //convert it into string base64 to share it in shared preferences
            editor.putString("image", encodeTobase64(temp));
            editor.commit();

            //if the entry was clicked, start the detailedView activity
            Intent intent = new Intent(context, DetailedViewActivity.class);
            context.startActivity(intent);
        }

        private Bitmap convertImageViewToBitmap(ImageView v){
            //convert the image into a bitmap so it can be shown on ImageView
            Bitmap bm=((BitmapDrawable)v.getDrawable()).getBitmap();
            return bm;
        }

        public static String encodeTobase64(Bitmap image) {
            //turn the bitmap of an image so it can be put in shared preferences
            Bitmap immage = image;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //compress the image into png format
            immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

            //return string of image
            return imageEncoded;
        }
    }
}

