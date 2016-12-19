package com.example.pelorusbv.pelorus;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Tobias on 22-5-2015.
 */
public class TableBoat {
    public static final String TABLE_BOATS = "boats";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EVENTID = "eventid";

    // Database creation SQL statement
    private static final String CREATE_TABLE_BOATS = "create table "
            + TABLE_BOATS
            + " ( "
            + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_NAME + " text not null,"
            + COLUMN_EVENTID + " integer,"
            + "foreign key(" + COLUMN_EVENTID + ") references " + TableEvent.TABLE_EVENTS + "(" + COLUMN_ID + "));";


    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_BOATS);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(TablePositions.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_BOATS);
        onCreate(database);
    }
}
