package com.example.pelorusbv.pelorus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Menso on 17-5-2015.
 */
public class DataSourcePositions {

    double newPoslat;
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] latColumns = {TablePositions.COLUMN_POSLAT, TablePositions.COLUMN_TIME};
    private String[] lngColumns = {TablePositions.COLUMN_POSLNG, TablePositions.COLUMN_TIME};

    public DataSourcePositions(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createPosition(int time, double lat, double lng) {
        ContentValues values = new ContentValues();
        values.put(TablePositions.COLUMN_TIME, time);
        values.put(TablePositions.COLUMN_POSLAT, lat);
        values.put(TablePositions.COLUMN_POSLNG, lng);
        long insertId = database.insert(TablePositions.TABLE_POSITIONS, null, values);

    }

    public double getPosLat(int time) {

        Cursor cursor = database.query(TablePositions.TABLE_POSITIONS, latColumns, TablePositions.COLUMN_TIME + " = " + Integer.toString(time), null, null, null, null);
        cursor.moveToFirst();
        newPoslat = cursor.getDouble(0);
        cursor.close();
        return newPoslat;
    }

    public double getPosLng(int time) {

        Cursor cursor = database.query(TablePositions.TABLE_POSITIONS, lngColumns, TablePositions.COLUMN_TIME + " = " + Integer.toString(time), null, null, null, null);
        cursor.moveToFirst();
        newPoslat = cursor.getDouble(0);
        cursor.close();
        return newPoslat;
    }

    public void deletePositions() {
        database.delete(TablePositions.TABLE_POSITIONS, null, null);
    }
}
