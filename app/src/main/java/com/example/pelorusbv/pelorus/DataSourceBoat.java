package com.example.pelorusbv.pelorus;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

import java.sql.SQLException;

/**
 * Created by Tobias on 22-5-2015.
 */
public class DataSourceBoat {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] boatColumns = {TableBoat.COLUMN_ID,TableBoat.COLUMN_NAME};
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
        database.insert(TableBoat.TABLE_BOATS, null, values);
    }

    public Cursor getBoatList(){
//        new CursorLoader(
//                ActivityCreateEvent.getActivity(),   // Parent activity context
//                TableBoat.TABLE_BOATS,        // Table to query
//                boatColumns,     // Projection to return
//                null,            // No selection clause
//                null,            // No selection arguments
//                null             // Default sort order
//        );

        Cursor cursor = database.query(TableBoat.TABLE_BOATS,boatColumns,null,null,null,null,null);
        return cursor;
    }

    public Cursor getBoatUserCrews(long id){
        Cursor cursor = database.query(TableBoat.TABLE_BOATS,boatColumns,TableBoat.COLUMN_ID + " in (SELECT " + TableCrews.COLUMN_BOATID + " FROM " + TableCrews.TABLE_CREWS + " WHERE " + TableCrews.COLUMN_USERID + " = " + Long.toString(id)+");",null,null,null,null );
        return cursor;
    }
}
