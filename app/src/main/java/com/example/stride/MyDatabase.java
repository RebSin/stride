package com.example.stride;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.example.stride.Constants.NAME;
import static com.example.stride.Constants.TABLE_NAME;

public class MyDatabase {
    private SQLiteDatabase db;
    private Context context;
    private final MyHelper helper;
    String photo;
    public MyDatabase(Context c){
        context = c;
        helper = new MyHelper(context);
    }

    public long insertData(String name, String type, String the_status, Bitmap image){
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(Constants.TYPE, type);
        contentValues.put(Constants.THE_STATUS, the_status);

        photo = getEncodedString(image);

        contentValues.put(Constants.IMAGE, photo);

        long id = db.insert(TABLE_NAME, null, contentValues);
        return id;
    }

//    public boolean deleteTitle(String name)
//    {
//        db = helper.getWritableDatabase();
//        return db.delete(TABLE_NAME, NAME + " = " + name, null) > 0;
//    }

    public void removeAllInfo(String title) {
        //Open the database
        db = helper.getWritableDatabase();

        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + NAME + "= '" + title + "'");

        //Close the database
        db.close();
    }

    private String getEncodedString(Bitmap bitmap){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        byte[] imageArr = os.toByteArray();
        return Base64.encodeToString(imageArr, Base64.URL_SAFE);
    }

    /*public static byte[] getBitmapAsByteArray(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }*/

    public Cursor getData(){
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {Constants.UID, NAME, Constants.TYPE, Constants.THE_STATUS, Constants.IMAGE};
        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }

    public Cursor getStatusFilteredData(String the_status){
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {Constants.UID, NAME, Constants.TYPE, Constants.THE_STATUS, Constants.IMAGE};
        String selection = Constants.TYPE + "='" +the_status+ "'";
        Cursor cursor = db.query(TABLE_NAME, columns, selection, null, null, null, null);
        return cursor;
    }
}
