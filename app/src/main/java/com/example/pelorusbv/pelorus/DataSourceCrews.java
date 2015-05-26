package com.example.pelorusbv.pelorus;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Tobias on 26-5-2015.
 */
public class DataSourceCrews {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    //private String[] CrewsColumns = {TableCrews.COLUMN_ID,TableCrews.COLUMN_NAME};
    //private String[] lngColumns = {TablePositions.COLUMN_POSLNG, TablePositions.COLUMN_TIME};


    public DataSourceCrews(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void CreateCrews(long userid, long boatid){
        ContentValues values = new ContentValues();
        values.put(TableCrews.COLUMN_USERID,userid);
        values.put(TableCrews.COLUMN_BOATID,boatid);
        database.insert(TableCrews.TABLE_CREWS, null, values);
    }
}
