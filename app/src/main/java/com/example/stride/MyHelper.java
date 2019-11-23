package com.example.stride;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyHelper extends SQLiteOpenHelper {

    private Context context;

    //create a string which is for creating a db table
    private static final String CREATE_TABLE =
            "CREATE TABLE "+
                    Constants.TABLE_NAME + " (" +
                    Constants.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.NAME + " TEXT, " +
                    Constants.TYPE + " TEXT, " +
                    Constants.THE_STATUS + " TEXT, " +
                    Constants.IMAGE + " BLOB, " + //use blob to store image in database
                    Constants.DATE + " TEXT);" ;

    //create a string for the drop table command
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + Constants.TABLE_NAME;

    public MyHelper(Context context){
        super (context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            //execute the creating a table sql statements
            db.execSQL(CREATE_TABLE);
            Toast.makeText(context, "onCreate() called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            //throw exception if the db was not created successfully
            Toast.makeText(context, "exception onCreate() db", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        try {
            //execute the drop table sql statements
            db.execSQL(DROP_TABLE);
            onCreate(db);
            Toast.makeText(context, "onUpgrade called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            //throw exception if the db table was not successfully dropped
            Toast.makeText(context, "exception onUpgrade() db", Toast.LENGTH_LONG).show();
        }
    }
}
