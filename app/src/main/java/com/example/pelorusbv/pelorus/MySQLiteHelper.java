package com.example.pelorusbv.pelorus;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Menso on 17-5-2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pelorus.db";
    private static final int DATABASE_VERSION = 19;

    public MySQLiteHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database){
        TablePositions.onCreate(database);
        TableUsers.onCreate(database);
        TableBoat.onCreate(database);
        TableCrews.onCreate(database);
        TableEvent.onCreate(database);
        TableCourses.onCreate(database);
        TableHasCourses.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        TablePositions.onUpgrade(database, oldVersion, newVersion);
        TableUsers.onUpgrade(database,oldVersion,newVersion);
        TableBoat.onUpgrade(database, oldVersion, newVersion);
        TableCrews.onUpgrade(database,oldVersion,newVersion);
        TableEvent.onUpgrade(database, oldVersion, newVersion);
        TableHasCourses.onUpgrade(database, oldVersion , newVersion);
        TableCourses.onUpgrade(database, oldVersion , newVersion);

    }
}
