package com.example.pelorusbv.pelorus;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Menso on 17-5-2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pelorus.db";

    public static final String TABLE_POSITIONS = "positions";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_POSLAT = "poslat";
    public static final String COLUMN_POSLNG = "poslng";


    private static final String DATABASE_CREATE = "create table "+ TABLE_POSITIONS + " ( " + COLUMN_ID + " integer primary key autoincrement," + COLUMN_TIME + " integer not null," + COLUMN_POSLAT + " real not null," + COLUMN_POSLNG + " real not null);";
    private static final int DATABASE_VERSION = 3;

    public MySQLiteHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database){
        database.execSQL(DATABASE_CREATE);
        System.out.println(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSITIONS);
        onCreate(db);
    }
}
