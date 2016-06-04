package com.example.pelorusbv.pelorus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by Menso on 17-5-2015.
 */
public class DataSourcePositions {

    double newPoslat;
    double newPoslng;
    int runid;
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] latColumns = {TablePositions.COLUMN_POSLAT, TablePositions.COLUMN_TIME, TablePositions.COLUMN_RUNID};
    private String[] lngColumns = {TablePositions.COLUMN_POSLNG, TablePositions.COLUMN_TIME, TablePositions.COLUMN_RUNID};
    private String[] runidColumns = {TablePositions.COLUMN_RUNID};

    public DataSourcePositions(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createPosition(int time, double lat, double lng, int runid) {
        ContentValues values = new ContentValues();
        values.put(TablePositions.COLUMN_TIME, time);
        values.put(TablePositions.COLUMN_POSLAT, lat);
        values.put(TablePositions.COLUMN_POSLNG, lng);
        values.put(TablePositions.COLUMN_RUNID, runid);
        long insertId = database.insert(TablePositions.TABLE_POSITIONS, null, values);

    }

    public double getPosLat(int time, int runid) {
        Cursor cursor = database.query(TablePositions.TABLE_POSITIONS, latColumns, TablePositions.COLUMN_TIME + " = " + Integer.toString(time) + " AND " + TablePositions.COLUMN_RUNID + " = " + runid, null, null, null, null);

        if (cursor.moveToFirst()) {
            newPoslat = cursor.getDouble(0);
        } else {
            newPoslat = 0;
        }
        cursor.close();
        return newPoslat;
    }

    public double getPosLng(int time, int runid) {
        Cursor cursor = database.query(TablePositions.TABLE_POSITIONS, lngColumns, TablePositions.COLUMN_TIME + " = " + Integer.toString(time) + " AND " + TablePositions.COLUMN_RUNID + " = " + runid, null, null, null, null);

        if (cursor.moveToFirst()) {
            newPoslng = cursor.getDouble(0);
        } else {
            newPoslng = 0;
        }
        cursor.close();
        return newPoslng;
    }

    public int getMaxRunID() {
        Cursor cursor = database.query(TablePositions.TABLE_POSITIONS, runidColumns, TablePositions.COLUMN_RUNID + " =(SELECT MAX(" + TablePositions.COLUMN_RUNID + ") FROM " + TablePositions.TABLE_POSITIONS + ")", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            runid = cursor.getInt(0);
        } else {
            runid = 0;
        }
        cursor.close();
        return runid;
    }

    public void deletePositions() {
        database.delete(TablePositions.TABLE_POSITIONS, null, null);
    }
}
