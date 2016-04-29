package com.example.pelorusbv.pelorus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.sql.SQLException;


public class ActivityJoinBoat extends ListActivity {

    DataSourceBoat dataSourceBoat;
    DataSourceCrews dataSourceCrews;

    SimpleCursorAdapter dataAdapter;
    ListView listViewBoatList;
    boolean BoatClicked;
    Cursor cursorBoat;
    private long IDclickedBoat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_join_boat);
        listViewBoatList = (ListView)findViewById(android.R.id.list);

        dataSourceBoat = new DataSourceBoat(this);
        dataSourceCrews = new DataSourceCrews(this);
    }

    public void OnClickJoinBoat(View view) {
        if (BoatClicked) {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            long id = pref.getLong("userID", 0);
            dataSourceCrews.CreateCrews(id, IDclickedBoat);
            Intent intent = new Intent(this, ActivityMainMenu.class);
            startActivity(intent);
        } else {
            // 1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Please select a boat")
                    .setTitle("No boat selected")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                        }
                    });

            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();

            dialog.show();
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_join_boat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onPause() {
        super.onPause();
        dataSourceBoat.close();
        dataSourceCrews.close();
        stopManagingCursor(cursorBoat);
        cursorBoat.close();
    }

    @Override
    protected void onResume(){
        super.onResume();
        try {
            dataSourceBoat.open();
            dataSourceCrews.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cursorBoat = dataSourceBoat.getBoatList();
        startManagingCursor(cursorBoat);

        dataAdapter = new SimpleCursorAdapter(
                this,
                R.layout.boat_info,
                cursorBoat,
                new String[]{TableBoat.COLUMN_NAME},
                new int[]{R.id.textViewBoat}
        );

        BoatClicked = false;
        listViewBoatList.setAdapter(dataAdapter);
        listViewBoatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                IDclickedBoat = id;
                BoatClicked = true;
            }
        });
    }


}
