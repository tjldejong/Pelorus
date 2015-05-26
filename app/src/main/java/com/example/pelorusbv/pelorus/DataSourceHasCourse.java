package com.example.pelorusbv.pelorus;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Tobias on 26-5-2015.
 */
public class DataSourceHasCourse {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    //private String[] CourseColumns = {TableCourses.COLUMN_ID,TableCourses.COLUMN_NAME};
    //private String[] lngColumns = {TablePositions.COLUMN_POSLNG, TablePositions.COLUMN_TIME};


    public DataSourceHasCourse(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void CreateHasCourse(long eventid,long courseid){
        ContentValues values = new ContentValues();
        values.put(TableHasCourses.COLUMN_EVENTID,eventid);
        values.put(TableHasCourses.COLUMN_COURSEID,courseid);
        database.insert(TableHasCourses.TABLE_HASCOURSES, null, values);
    }
}
