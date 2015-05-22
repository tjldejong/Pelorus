package com.example.pelorusbv.pelorus;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Tobias on 22-5-2015.
 */
public class TablePositions {
    public static final String TABLE_POSITIONS = "positions";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_POSLAT = "poslat";
    public static final String COLUMN_POSLNG = "poslng";

    // Database creation SQL statement
    private static final String CREATE_TABLE_POSITIONS = "create table "
            + TABLE_POSITIONS
            + " ( "
            + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_TIME + " integer not null,"
            + COLUMN_POSLAT + " real not null,"
            + COLUMN_POSLNG + " real not null);";


    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_POSITIONS);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(TablePositions.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_POSITIONS);
        onCreate(database);
    }
}
