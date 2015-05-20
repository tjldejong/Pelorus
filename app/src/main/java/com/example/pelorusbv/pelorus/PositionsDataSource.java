package com.example.pelorusbv.pelorus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Menso on 17-5-2015.
 */
public class PositionsDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] latColumns = {MySQLiteHelper.COLUMN_POSLAT, MySQLiteHelper.COLUMN_TIME};
    private String[] lngColumns = {MySQLiteHelper.COLUMN_POSLNG, MySQLiteHelper.COLUMN_TIME};

    double newPoslat;

    public PositionsDataSource(Context context) {
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
        values.put(MySQLiteHelper.COLUMN_TIME, time);
        values.put(MySQLiteHelper.COLUMN_POSLAT, lat);
        values.put(MySQLiteHelper.COLUMN_POSLNG, lng);
        long insertId = database.insert(MySQLiteHelper.TABLE_POSITIONS, null, values);

    }

    public double getPosLat(int time) {

        Cursor cursor = database.query(MySQLiteHelper.TABLE_POSITIONS, latColumns, MySQLiteHelper.COLUMN_TIME + " = " + Integer.toString(time), null, null, null, null);
        cursor.moveToFirst();
        newPoslat = cursor.getDouble(0);
        cursor.close();
        return newPoslat;
    }

    public double getPosLng(int time) {

        Cursor cursor = database.query(MySQLiteHelper.TABLE_POSITIONS, lngColumns, MySQLiteHelper.COLUMN_TIME + " = " + Integer.toString(time), null, null, null, null);
        cursor.moveToFirst();
        newPoslat = cursor.getDouble(0);
        cursor.close();
        return newPoslat;
    }
}
