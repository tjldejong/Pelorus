package com.example.pelorusbv.pelorus;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Tobias on 24-5-2015.
 */
public class TableCourses {
    public static final String TABLE_COURSES = "Courses";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BUOY1LAT = "buoy1lat";
    public static final String COLUMN_BUOY1LNG = "buoy1lng";
    public static final String COLUMN_BUOY2LAT = "buoy2lat";
    public static final String COLUMN_BUOY2LNG = "buoy2lng";
    public static final String COLUMN_BUOY3LAT = "buoy3lat";
    public static final String COLUMN_BUOY3LNG = "buoy3lng";
    public static final String COLUMN_BUOY4LAT = "buoy4lat";
    public static final String COLUMN_BUOY4LNG = "buoy4lng";



    // Database creation SQL statement
    private static final String CREATE_TABLE_COURSE = "create table "
            + TABLE_COURSES
            + " ( "
            + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_NAME + " text not null,"
            + COLUMN_BUOY1LAT + " real not null,"
            + COLUMN_BUOY1LNG + " real not null,"
            + COLUMN_BUOY2LAT + " real not null,"
            + COLUMN_BUOY2LNG + " real not null,"
            + COLUMN_BUOY3LAT + " real not null,"
            + COLUMN_BUOY3LNG + " real not null,"
            + COLUMN_BUOY4LAT + " real not null,"
            + COLUMN_BUOY4LNG + " real not null);";


    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_COURSE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(TableCourses.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        onCreate(database);
    }
}
