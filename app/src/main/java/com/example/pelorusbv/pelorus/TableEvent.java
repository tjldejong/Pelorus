package com.example.pelorusbv.pelorus;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Tobias on 22-5-2015.
 */
public class TableEvent {
    public static final String TABLE_EVENTS = "events";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";



    // Database creation SQL statement
    private static final String CREATE_TABLE_BOATS = "create table "
            + TABLE_EVENTS
            + " ( "
            + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_NAME + " text not null);";


    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_BOATS);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(TableEvent.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(database);
    }
}
