package com.example.pelorusbv.pelorus;

import android.app.Activity;
import android.content.Intent;
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


public class ActivityJoinEvent extends Activity {

    DataSourceBoat dataSourceBoat;
    DataSourceCrews dataSourceCrews;
    DataSourceEvents dataSourceEvents;

    SimpleCursorAdapter dataAdapterBoat;
    SimpleCursorAdapter dataAdapterEvent;

    ListView listViewEventList;
    ListView listViewBoatList;

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

        try {
            dataSourceBoat.open();
            dataSourceCrews.open();
            dataSourceEvents.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Cursor cursorEvent = dataSourceEvents.getEvents();
        Cursor cursorBoat = dataSourceBoat.getBoatUserCrews(User.getInstance().getID());
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
                new int[]{R.id.textViewEvent}
        );

        listViewBoatList.setAdapter(dataAdapterBoat);
        listViewEventList.setAdapter(dataAdapterEvent);
        listViewBoatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IDclickedBoat = id;
                view.setSelected(true);
            }
        });
        listViewEventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IDclickedEvent = id;
                view.setSelected(true);
            }
        });



    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSourceBoat.close();
        dataSourceCrews.close();
        dataSourceEvents.close();
    }

    @Override
    protected void onResume(){
        super.onResume();
        try {
            dataSourceBoat.open();
            dataSourceCrews.open();
            dataSourceEvents.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        Intent intent = new Intent(this, ActivityDashboard.class);
        startActivity(intent);
    }
}
