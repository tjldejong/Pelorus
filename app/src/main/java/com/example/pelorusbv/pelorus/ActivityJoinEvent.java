package com.example.pelorusbv.pelorus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.sql.SQLException;


public class ActivityJoinEvent extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    //Met een boot een event joinen.
    //TODO: alleen schippers kunen met hun boat een event joinen

    DataSourceBoat dataSourceBoat;
    DataSourceCrews dataSourceCrews;
    DataSourceEvents dataSourceEvents;
    DataSourcePositions dataSourcePositions;

    SimpleCursorAdapter dataAdapterBoat;
    SimpleCursorAdapter dataAdapterEvent;

    ListView listViewEventList;
    ListView listViewBoatList;
    boolean BoatClicked;
    boolean EventClicked;
    Cursor cursorEvent;
    Cursor cursorBoat;
    private long IDclickedBoat;
    private long IDclickedEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_join_event);

        listViewEventList = (ListView)findViewById(R.id.listViewEventList);
        listViewBoatList = (ListView)findViewById(R.id.listViewBoatList);

        dataSourceBoat = new DataSourceBoat(this);
        dataSourceCrews = new DataSourceCrews(this);
        dataSourceEvents = new DataSourceEvents(this);
        dataSourcePositions = new DataSourcePositions(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSourceBoat.close();
        dataSourceCrews.close();
        dataSourceEvents.close();
        dataSourcePositions.close();
        stopManagingCursor(cursorEvent);
        stopManagingCursor(cursorBoat);
        cursorEvent.close();
        cursorBoat.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            dataSourceBoat.open();
            dataSourceCrews.open();
            dataSourceEvents.open();
            dataSourcePositions.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        long id = pref.getLong("userID", 0);

        cursorEvent = dataSourceEvents.getEvents();
        cursorBoat = dataSourceBoat.getBoatUserCrews(id);
        startManagingCursor(cursorEvent);
        startManagingCursor(cursorBoat);

        dataAdapterBoat  = new SimpleCursorAdapter(
                this,
                R.layout.boat_info,
                cursorBoat,
                new String[]{TableBoat.COLUMN_NAME},
                new int[]{R.id.textViewBoat}
        );


        dataAdapterEvent = new SimpleCursorAdapter(
                this,
                R.layout.event_info,
                cursorEvent,
                new String[]{TableEvent.COLUMN_NAME},
                new int[]{R.id.textViewEventName}
        );

        BoatClicked = false;
        EventClicked = false;
        listViewBoatList.setAdapter(dataAdapterBoat);
        listViewEventList.setAdapter(dataAdapterEvent);
        listViewBoatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IDclickedBoat = id;
                view.setSelected(true);
                BoatClicked = true;
            }
        });
        listViewEventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IDclickedEvent = id;
                view.setSelected(true);
                EventClicked = true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_join_event, menu);
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

    public void OnClickJoinEvent(View view) {
        if (EventClicked && BoatClicked) {
            Intent intent = new Intent(this, ActivityDashboard.class);

            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            Log.i("eventid", String.format("%d", IDclickedEvent));
            editor.putLong("eventID", IDclickedEvent);
            int LastRunID = dataSourcePositions.getMaxRunID();
            Log.i("lastrunid", String.format("%d", LastRunID));
            editor.putInt("runID", LastRunID + 1);
            editor.putLong("boatID", IDclickedBoat );
            editor.commit();

            dataSourceBoat.setEventID(IDclickedBoat, IDclickedEvent);

            startActivity(intent);
        } else if (!EventClicked) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please select an event")
                    .setTitle("No event selected")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (!BoatClicked) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please select a boat")
                    .setTitle("No boat selected")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    // Called when a new Loader needs to be created
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        Uri uri = DataSourceEvents.CONTENT_URI;
        return new CursorLoader(this, uri, null, null, null, null);
    }

    /**
     * A callback method, invoked after the requested content provider returned all the data
     */
    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
        dataAdapterEvent.swapCursor(arg1);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        dataAdapterEvent.swapCursor(null);
    }


}
