package com.example.pelorusbv.pelorus;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Tobias on 22-5-2015.
 */
public class TableUsers {
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_USERTYPE = "usertype";


    // Database creation SQL statement
    private static final String CREATE_TABLE_USERS = "create table "
            + TABLE_USERS
            + " ( "
            + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_EMAIL + " text not null,"
            + COLUMN_PASSWORD + " text not null,"
            + COLUMN_USERTYPE + " integer not null);";


    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_USERS);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(TableUsers.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(database);
    }
}
