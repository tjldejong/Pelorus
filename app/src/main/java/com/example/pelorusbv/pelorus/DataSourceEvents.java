package com.example.pelorusbv.pelorus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Tobias on 24-5-2015.
 */
public class DataSourceEvents {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] eventColumns = {TableEvent.COLUMN_ID, TableEvent.COLUMN_NAME};
    //private String[] lngColumns = {TablePositions.COLUMN_POSLNG, TablePositions.COLUMN_TIME};


    public DataSourceEvents(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void CreateEvent(String name, long courseid){
        ContentValues values = new ContentValues();
        values.put(TableEvent.COLUMN_NAME,name);
        values.put(TableEvent.COLUMN_COURSEID,courseid);
        database.insert(TableEvent.TABLE_EVENTS, null, values);
    }

    public Cursor getEvents(){
        Cursor cursor = database.query(TableEvent.TABLE_EVENTS,eventColumns,null,null,null,null,null);
        return cursor;
    }
}
