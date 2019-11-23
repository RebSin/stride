package com.example.stride;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.stride.GraphActivity.DEFAULT;

public class DiaryActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    //declare variables to use for the recycler view
    RecyclerView myRecycler;
    MyDatabase db;
    MyHelper helper;
    MyAdapter myAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //set a default to check for the shared preferences
    public static final String DEFAULT_ALT = "not available";

    //declare button for adding entry
    Button addEntryButton;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        //link button and recycler view to XML layout
        addEntryButton = (Button) findViewById(R.id.add_entry_button);
        myRecycler = (RecyclerView) findViewById(R.id.graph_button);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        myRecycler.setLayoutManager(mLayoutManager);
        addEntryButton.setOnClickListener(this);
        context = this;

        //declare new db, helper and cursor to get results from the db
        db = new MyDatabase(this);
        helper = new MyHelper(this);
        Cursor cursor = db.getData();

        //use shared preferences to see which filter was applied in the previous screen
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String the_filter = sharedPrefs.getString("filter", DEFAULT_ALT);

        //depending on the filter chosen, a different db function will be called to get filtered results
        if(the_filter == "all_filter"){
            //get all entries if they choose the all filter
            cursor = db.getData();
            //else get results based on the filter they chose
        } else if(the_filter == "healthy_filter"){
            cursor = db.getStatusFilteredData("Healthy");
        } else if(the_filter == "unhealthy_filter"){
            cursor = db.getStatusFilteredData("Unhealthy");
        } else if(the_filter == "unsure_filter"){
            cursor = db.getStatusFilteredData("Unsure");
        }

        //declare variables for the status to keep track of the number of food they ate for the graph
        int numHealthy = 0;
        int numUnhealthy = 0;
        int numUnsure = 0;

        //setting the cursor to get info
        int index1 = cursor.getColumnIndex(Constants.NAME);
        int index2 = cursor.getColumnIndex(Constants.TYPE);
        int index3 = cursor.getColumnIndex(Constants.THE_STATUS);
        int index4 = cursor.getColumnIndex(Constants.IMAGE);
        int index5 = cursor.getColumnIndex(Constants.DATE);

        //getting sharedpreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //create arrays to store the results of the db in
        ArrayList<String> mArrayList = new ArrayList<String>();
        ArrayList<String> onlyStatus = new ArrayList<String>();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){ //iterates thro the db to get the info
            //get the title, description, status, image of the diary entries
            String title = cursor.getString(index1);
            String description = cursor.getString(index2);
            String status = cursor.getString(index3);
            String image = cursor.getString(index4);
            String date = cursor.getString(index5);
//            Log.d("diaryactivity", "IAMTHEDATE: " + date);
            String s = title + "," + description + "," + status + "," + date + "," + image;
       //     Log.d("diaryactivity", "arraylistadd: " + s);
            String temp = status.toString();
            //update the number for how much of a certain food was eaten in shared preferences
            if(temp.equals("Healthy")) { //this is to check how many healthy/unhealthy/unsure items there are
                //if it is healthy, increase the healthy count
                numHealthy = numHealthy + 1;
                editor.putInt("Healthy", numHealthy);
                editor.commit();
            } else if(temp.equals("Unhealthy")){
                //if it is unhealthy, increase the unhealthy count
                numUnhealthy = numUnhealthy + 1;
                editor.putInt("Unhealthy", numUnhealthy);
                editor.commit();
            } else if(temp.equals("Unsure")){
                //if it is unsure, increase the unsure count
                numUnsure = numUnsure + 1;
                editor.putInt("Unsure", numUnsure);
                editor.commit();
            }
            //add the entry to the array
            mArrayList.add(s);
            onlyStatus.add(image);
            //move to the next entry in the db
            cursor.moveToNext();
        }

        //after the db entries are in the array, create the adaptor
        myAdapter = new MyAdapter(mArrayList);
        myRecycler.setAdapter(myAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        //link the elements of the diary entry to the XML layouts
        TextView titleTextView = (TextView) view.findViewById(R.id.titleEntry);
        TextView descriptionTextView = (TextView) view.findViewById(R.id.descriptionEntry);
        TextView statusTextView = (TextView) view.findViewById(R.id.statusEntry);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageEntry);
        Toast.makeText(this, "row " + (1+position) + ": " + titleTextView.getText() + " " + descriptionTextView.getText() + " " + statusTextView.getText(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        //if the user clicks the add entry button, then start the addDiaryEntryActivity where they can create a diary entry
        if(view.getId() == R.id.add_entry_button){
            oneWeekCountdown();
            Intent intent = new Intent(view.getContext(), AddDiaryEntryActivity.class);
            startActivity(intent);
        }
    }
    long oneWeekAgo = System.currentTimeMillis();
    public void oneWeekCountdown() {
      //  Log.d("HEALTH run", "I RAN");
        long nowTime = System.currentTimeMillis();
        //Log.d("time", "nowtime: " + nowTime + "one week ago" + oneWeekAgo);
        //Log.d("goalmet", "usermetgoal" + userMetGoal(true));
        if(nowTime >= (oneWeekAgo
        //        + 604800*1000
        )) {
          //  Log.d("HEALTH FOOD GOAL MET", "TIMEPASSED");
            if (userMetGoal(true)) {
                Toast toast = Toast.makeText(context, "You met your weekly food goal", Toast.LENGTH_SHORT);
                toast.show(); //displays the toast
            //    Log.d("HEALTH FOOD GOAL MET", "MET");
                userMetGoal(false);
            }
            oneWeekAgo = System.currentTimeMillis(); //update originalTime to new time
        }
    }
    public boolean userMetGoal(boolean weekPassed) {
        //check to see if the user has met their specified goal
        if (weekPassed) {
            //get data for their food goal from shared preferences
            SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
            int usersWeeklyFoodGoal = sharedPrefs.getInt("foodgoal", 0);
            //get data for their food which was consumed from shared preferences
            int numHealthy = sharedPrefs.getInt("Healthy", DEFAULT);
            //check to see if the goal was met
            if (usersWeeklyFoodGoal >= numHealthy) {
                return true;
            }
        }
        return false;
    }
}
