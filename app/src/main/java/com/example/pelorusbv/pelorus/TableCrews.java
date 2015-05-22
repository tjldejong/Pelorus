package com.example.pelorusbv.pelorus;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Tobias on 22-5-2015.
 */
public class TableCrews {
    public static final String TABLE_CREWS = "crews";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERID = "userid";
    public static final String COLUMN_BOATID = "boatid";





    // Database creation SQL statement
    private static final String CREATE_TABLE_BOATS = "create table "
            + TABLE_CREWS
            + " ( "
            + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_USERID + " integer,"
            + COLUMN_BOATID + " integer,"
            + "foreign key(" + COLUMN_USERID + ") references " + TableUsers.TABLE_USERS + "(" + COLUMN_ID + "),"
            + "foreign key(" + COLUMN_BOATID + ") references " + TableBoat.TABLE_BOATS + "(" + COLUMN_ID + "));";


    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_BOATS);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(TableCrews.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_CREWS);
        onCreate(database);
    }
}
