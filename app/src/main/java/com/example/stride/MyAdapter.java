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

    public ArrayList<String> list;
    Context context;
    public int numHealthy = 0;

    public MyAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(row, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String[] results = (list.get(position).toString()).split(",");
        holder.titleTextView.setText(results[0]);
        holder.descriptionTextView.setText(results[1]);
        holder.statusTextView.setText(results[2]);
        holder.imageView.setImageBitmap(getBitmapFromEncodedString(results[3]));
    }


    public Bitmap getBitmapFromEncodedString(String encodedString){
        byte[] arr = Base64.decode(encodedString, Base64.URL_SAFE);
        Bitmap img = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        return img;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView titleTextView;
        public TextView descriptionTextView;
        public TextView statusTextView;
        public ImageView imageView;
        public LinearLayout myLayout;

        Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            myLayout = (LinearLayout) itemView;

            titleTextView = (TextView) itemView.findViewById(R.id.titleEntry);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionEntry);
            statusTextView = (TextView) itemView.findViewById(R.id.statusEntry);
            imageView = (ImageView) itemView.findViewById(R.id.imageEntry);

            itemView.setOnClickListener(this);
            context = itemView.getContext();
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context,
                    "You have clicked " + ((TextView)view.findViewById(R.id.titleEntry)).getText().toString(),
                    Toast.LENGTH_SHORT).show();
            //first convert image into bitmap and store in temp
            ImageView bitmap = ((ImageView)view.findViewById(R.id.imageEntry));
            //set view to bitmap image
            //bitmap.setImageBitmap(convertImageViewToBitmap(img));
            Bitmap temp = convertImageViewToBitmap(bitmap);
            SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("title", ((TextView)view.findViewById(R.id.titleEntry)).getText().toString());
            editor.putString("description", ((TextView)view.findViewById(R.id.descriptionEntry)).getText().toString());
            editor.putString("status", ((TextView)view.findViewById(R.id.statusEntry)).getText().toString());
            //convert it into string base64 to share it in shared preferences
            editor.putString("image", encodeTobase64(temp));
            editor.commit();

            Intent intent = new Intent(context, DetailedViewActivity.class);
            context.startActivity(intent);
        }

        private Bitmap convertImageViewToBitmap(ImageView v){
            Bitmap bm=((BitmapDrawable)v.getDrawable()).getBitmap();
            return bm;
        }

        public static String encodeTobase64(Bitmap image) {
            Bitmap immage = image;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

            return imageEncoded;
        }
    }
}

