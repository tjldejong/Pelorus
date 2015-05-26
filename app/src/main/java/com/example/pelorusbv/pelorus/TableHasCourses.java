package com.example.pelorusbv.pelorus;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Tobias on 24-5-2015.
 */
public class TableHasCourses {
    public static final String TABLE_HASCOURSES = "hascourses";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_EVENTID = "eventid";
    public static final String COLUMN_COURSEID = "courseid";





    // Database creation SQL statement
    private static final String CREATE_TABLE_HASCOURSES = "create table "
            + TABLE_HASCOURSES
            + " ( "
            + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_EVENTID + " integer,"
            + COLUMN_COURSEID + " integer,"
            + "foreign key(" + COLUMN_EVENTID + ") references " + TableEvent.TABLE_EVENTS + "(" + TableEvent.COLUMN_ID + "),"
            + "foreign key(" + COLUMN_COURSEID + ") references " + TableCourses.TABLE_COURSES + "(" + TableCourses.COLUMN_ID + "));";


    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_HASCOURSES);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(TableHasCourses.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_HASCOURSES);
        onCreate(database);
    }
}
