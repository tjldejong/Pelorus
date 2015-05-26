package com.example.pelorusbv.pelorus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Tobias on 22-5-2015.
 */
public class DataSourceUsers {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] emailColumn = {TableUsers.COLUMN_ID,TableUsers.COLUMN_EMAIL};
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

    public long CreateUser(String name, String password){
        ContentValues values = new ContentValues();
        values.put(TableUsers.COLUMN_EMAIL, name);
        values.put(TableUsers.COLUMN_PASSWORD, password);
        long userID = database.insert(TableUsers.TABLE_USERS, null, values);
        return userID;
    }

    public String getEmail(long ID){
        String id = Long.toString(ID);
        Cursor cursor = database.query(TableUsers.TABLE_USERS,emailColumn,TableUsers.COLUMN_ID + " = " + id,null,null,null,null);
        cursor.moveToFirst();
        String email = cursor.getString(1);
        cursor.close();
        return email;
    }
}
