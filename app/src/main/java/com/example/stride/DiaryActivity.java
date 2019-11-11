package com.example.stride;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DiaryActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    //declare variables to use for the recycler view
    RecyclerView myRecycler;
    MyDatabase db;
    MyHelper helper;
    MyAdapter myAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Button addEntryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        addEntryButton = (Button) findViewById(R.id.add_entry_button);
        myRecycler = (RecyclerView) findViewById(R.id.graph_button);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        myRecycler.setLayoutManager(mLayoutManager);
        addEntryButton.setOnClickListener(this);

        db = new MyDatabase(this);
        helper = new MyHelper(this);

        Cursor cursor = db.getData();
        int numHealthy = 0;
        int numUnhealthy = 0;
        int numUnsure = 0;

        int index1 = cursor.getColumnIndex(Constants.NAME);
        int index2 = cursor.getColumnIndex(Constants.TYPE);
        int index3 = cursor.getColumnIndex(Constants.THE_STATUS);
        int index4 = cursor.getColumnIndex(Constants.IMAGE);

        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        ArrayList<String> mArrayList = new ArrayList<String>();
        ArrayList<String> onlyStatus = new ArrayList<String>();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String title = cursor.getString(index1);
            String description = cursor.getString(index2);
            String status = cursor.getString(index3);
            String image = cursor.getString(index4);
            String s = title + "," + description + "," + status + "," + image;
            String temp = status.toString();
            if(temp.equals("Healthy")) {
                numHealthy = numHealthy + 1;
                editor.putInt("Healthy", numHealthy);
                editor.commit();
            } else if(temp.equals("Unhealthy")){
                numUnhealthy = numUnhealthy + 1;
                editor.putInt("Unhealthy", numUnhealthy);
                editor.commit();
            } else if(temp.equals("Unsure")){
                numUnsure = numUnsure + 1;
                editor.putInt("Unsure", numUnsure);
                editor.commit();
            }
            mArrayList.add(s);
            onlyStatus.add(image);
            cursor.moveToNext();

        }

        myAdapter = new MyAdapter(mArrayList);
        myRecycler.setAdapter(myAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        //LinearLayout clickedRow = (LinearLayout) view;
        TextView titleTextView = (TextView) view.findViewById(R.id.titleEntry);
        TextView descriptionTextView = (TextView) view.findViewById(R.id.descriptionEntry);
        TextView statusTextView = (TextView) view.findViewById(R.id.statusEntry);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageEntry);
        Toast.makeText(this, "row " + (1+position) + ": " + titleTextView.getText() + " " + descriptionTextView.getText() + " " + statusTextView.getText(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.add_entry_button){
            Intent intent = new Intent(view.getContext(), AddDiaryEntryActivity.class);
            startActivity(intent);
        }
    }
}
