package com.example.pelorusbv.pelorus;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Tobias on 22-5-2015.
 */
public class DataSourceUsers {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    //private String[] latColumns = {TablePositions.COLUMN_POSLAT, TablePositions.COLUMN_TIME};
    //private String[] lngColumns = {TablePositions.COLUMN_POSLNG, TablePositions.COLUMN_TIME};


    public DataSourceUsers(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void CreateUser(String name, String password){
        ContentValues values = new ContentValues();
        values.put(TableUsers.COLUMN_EMAIL, name);
        values.put(TableUsers.COLUMN_PASSWORD, password);
        database.insert(TableUsers.TABLE_USERS, null, values);
    }
}
