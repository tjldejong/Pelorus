package com.example.pelorusbv.pelorus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Tobias on 26-5-2015.
 */
public class DataSourceCourses {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] coursesColumns = {TableCourses.COLUMN_ID,TableCourses.COLUMN_NAME};
    private String[] buoyColumns = {TableCourses.COLUMN_ID, TableCourses.COLUMN_BUOY1LAT,TableCourses.COLUMN_BUOY1LNG,TableCourses.COLUMN_BUOY2LAT,TableCourses.COLUMN_BUOY2LNG,TableCourses.COLUMN_BUOY3LAT,TableCourses.COLUMN_BUOY3LNG,TableCourses.COLUMN_BUOY4LAT,TableCourses.COLUMN_BUOY4LNG};


    public DataSourceCourses(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void CreateCourse(String name, double b1lat, double b1lng, double b2lat, double b2lng, double b3lat, double b3lng, double b4lat, double b4lng){
        ContentValues values = new ContentValues();
        values.put(TableCourses.COLUMN_NAME,name);
        values.put(TableCourses.COLUMN_BUOY1LAT,b1lat);
        values.put(TableCourses.COLUMN_BUOY1LNG,b1lng);
        values.put(TableCourses.COLUMN_BUOY2LAT,b2lat);
        values.put(TableCourses.COLUMN_BUOY2LNG,b2lng);
        values.put(TableCourses.COLUMN_BUOY3LAT,b3lat);
        values.put(TableCourses.COLUMN_BUOY3LNG,b3lng);
        values.put(TableCourses.COLUMN_BUOY4LAT,b4lat);
        values.put(TableCourses.COLUMN_BUOY4LNG,b4lng);
        database.insert(TableCourses.TABLE_COURSES, null, values);
    }

    public Cursor getCoursesList(){
        Cursor cursor = database.query(TableCourses.TABLE_COURSES,coursesColumns,null,null,null,null,null);
        return cursor;
    }

    public double[] getBuoyPositions(long courseID){
        Cursor cursor = database.query(TableCourses.TABLE_COURSES,buoyColumns,TableCourses.COLUMN_ID + " = " + Long.toString(courseID),null,null,null,null);
        //
        cursor.moveToFirst();
        double[] buoyArray = {cursor.getDouble(1),cursor.getDouble(2),cursor.getDouble(3),cursor.getDouble(4),cursor.getDouble(5),cursor.getDouble(6),cursor.getDouble(7),cursor.getDouble(8)};
        cursor.close();
        return buoyArray;
    }


}

