package com.example.stride;

import java.sql.Blob;

public class Constants {
    //a list of constant values to use in the database files
    public static final String DATABASE_NAME = "diarydatabase";
    public static final String TABLE_NAME = "DIARYTABLE";
    public static final String UID = "_id";
    public static final String NAME = "Title";
    public static final String TYPE = "Description";
    public static final String THE_STATUS = "theStatus";
    public static final String IMAGE = "Image_URL";

    //version of the db which needs to incremented each time the db is altered
    public static final int DATABASE_VERSION = 4;
}
