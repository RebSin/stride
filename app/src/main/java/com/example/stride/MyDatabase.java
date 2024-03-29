package com.example.stride;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

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
        //set context and helper of the db
        context = c;
        helper = new MyHelper(context);
    }

    public long insertData(String name, String type, String the_status, Bitmap image, String date){
        //get writable database so it will be created
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //put the name, type, status, image into the db
        contentValues.put(NAME, name);
        contentValues.put(Constants.TYPE, type);
        contentValues.put(Constants.THE_STATUS, the_status);
        contentValues.put(Constants.DATE, date);
       // Log.d("date", "date: " + date);
        photo = getEncodedString(image); //convert image to string before storing it
        contentValues.put(Constants.IMAGE, photo);

        long id = db.insert(TABLE_NAME, null, contentValues);
        return id;
    }

//    public boolean deleteTitle(String name)
//    {
//        db = helper.getWritableDatabase();
//        return db.delete(TABLE_NAME, NAME + " = " + name, null) > 0;
//    }

    Cursor getStatusFilteredData (String the_status){
    SQLiteDatabase db = helper.getWritableDatabase();

    String[] columns = {Constants.UID, NAME, Constants.TYPE, Constants.THE_STATUS, Constants.IMAGE, Constants.DATE};
    //Constants.DATE
    String selection = Constants.THE_STATUS + "='" +the_status+ "'";
    Cursor cursor = db.query(TABLE_NAME, columns, selection, null, null, null, null);
        return cursor;
}

    public void removeAllInfo(String title) {
        //Open the database
        db = helper.getWritableDatabase();

        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + NAME + "= '" + title + "'");

        //Close the database
        db.close();
    }

    public String getSelectedType(Long id)
    {
        //get wrtitable database
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.UID, Constants.NAME, Constants.TYPE, Constants.THE_STATUS, Constants.IMAGE, Constants.DATE};
        //Constants.DATE
        String selection = Constants.UID + "='" +id+ "'";
        Cursor cursor = db.query(Constants.TABLE_NAME, columns, selection, null, null, null, null);

        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {

            //get cursor pointers to the name, type, status and image
            int index1 = cursor.getColumnIndex(Constants.NAME);
            int index2 = cursor.getColumnIndex(Constants.TYPE);
            int index3 = cursor.getColumnIndex(Constants.THE_STATUS);
            int index4 = cursor.getColumnIndex(Constants.IMAGE);

            //store the type in the buffer
            String Type = cursor.getString(index2);
            buffer.append(Type);
        }
        return buffer.toString();
    }

    public String getSelectedStatus(Long id) {
        //get writable database
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.UID, Constants.NAME, Constants.TYPE, Constants.THE_STATUS, Constants.IMAGE, Constants.DATE};
        //create array of paramaters
        //select the entry where the UID matches id
        String selection = Constants.UID + "='" +id+ "'";
        Cursor cursor = db.query(Constants.TABLE_NAME, columns, selection, null, null, null, null);

        StringBuffer buffer = new StringBuffer();
        //get pointer to the entries
        while (cursor.moveToNext()) {
            //place all entries into a buffer
            int index1 = cursor.getColumnIndex(Constants.NAME);
            int index2 = cursor.getColumnIndex(Constants.TYPE);
            int index3 = cursor.getColumnIndex(Constants.THE_STATUS);
            int index4 = cursor.getColumnIndex(Constants.IMAGE);
            String Status = cursor.getString(index3);
            buffer.append(Status);
        }
        //return the buffer
        return buffer.toString();
    }

    public String getSelectedDate(Long id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.UID, Constants.NAME, Constants.TYPE, Constants.THE_STATUS, Constants.IMAGE, Constants.DATE};
        //Constants.DATE
        String selection = Constants.UID + "='" + id + "'";
        Cursor cursor = db.query(Constants.TABLE_NAME, columns, selection, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex(Constants.NAME);
            int index2 = cursor.getColumnIndex(Constants.TYPE);
            int index3 = cursor.getColumnIndex(Constants.THE_STATUS);
            int index4 = cursor.getColumnIndex(Constants.IMAGE);
            int index5 = cursor.getColumnIndex(Constants.DATE);
            String Status = cursor.getString(index5);
            buffer.append(Status);
        }
        return buffer.toString();
    }
    public String getSelectedID(String name)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.UID, Constants.NAME, Constants.TYPE, Constants.THE_STATUS, Constants.IMAGE, Constants.DATE};
        //Constants.DATE
        String selection = Constants.NAME + "='" +name+ "'";
        Cursor cursor = db.query(Constants.TABLE_NAME, columns, selection, null, null, null, null);

        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            int index0 = cursor.getColumnIndex(Constants.UID);

            int index1 = cursor.getColumnIndex(Constants.NAME);
            int index2 = cursor.getColumnIndex(Constants.TYPE);
            int index3 = cursor.getColumnIndex(Constants.THE_STATUS);
            int index4 = cursor.getColumnIndex(Constants.IMAGE);
            int index5 = cursor.getColumnIndex(Constants.DATE);
            String Status = cursor.getString(index0);
            buffer.append(Status);
        }
        return buffer.toString();
    }

    public String getSelectedImage(Long id)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.UID, Constants.NAME, Constants.TYPE, Constants.THE_STATUS, Constants.IMAGE, Constants.DATE};

        String selection = Constants.UID + "='" +id+ "'";
        Cursor cursor = db.query(Constants.TABLE_NAME, columns, selection, null, null, null, null);

        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {

            int index1 = cursor.getColumnIndex(Constants.NAME);
            int index2 = cursor.getColumnIndex(Constants.TYPE);
            int index3 = cursor.getColumnIndex(Constants.THE_STATUS);
            int index4 = cursor.getColumnIndex(Constants.IMAGE);
            //int index5 = cursor.getColumnIndex(Constants.DATE);
            String Image = cursor.getString(index4);
            buffer.append(Image);
        }
        return buffer.toString();
    }

    private String getEncodedString(Bitmap bitmap){
        //convert a bitmap of an image into a string so it can be entered into the db
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        byte[] imageArr = os.toByteArray();
        //return the string
        return Base64.encodeToString(imageArr, Base64.URL_SAFE);
    }

    /*public static byte[] getBitmapAsByteArray(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }*/

    public Cursor getData(){
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {Constants.UID, NAME, Constants.TYPE, Constants.THE_STATUS, Constants.IMAGE, Constants.DATE};
        //Constants.DATE
        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }
}
