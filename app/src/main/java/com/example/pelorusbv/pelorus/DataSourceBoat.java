package com.example.pelorusbv.pelorus;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Tobias on 22-5-2015.
 */
public class DataSourceBoat {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    //private String[] latColumns = {TablePositions.COLUMN_POSLAT, TablePositions.COLUMN_TIME};
    //private String[] lngColumns = {TablePositions.COLUMN_POSLNG, TablePositions.COLUMN_TIME};


    public DataSourceBoat(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void CreateBoat(String name){
        ContentValues values = new ContentValues();
        values.put(TableBoat.COLUMN_NAME,name);
        database.insert(TableBoat.TABLE_BOATS,null,values);
    }
}
