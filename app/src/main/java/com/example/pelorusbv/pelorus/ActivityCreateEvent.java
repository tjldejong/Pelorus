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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.gms.fitness.data.DataSource;

import java.sql.SQLException;


public class ActivityCreateEvent extends Activity {
    //Laat een lijst zien met mogelijke courses. Deze kunnen gebruikt worden om een event aan te maken.
    DataSourceEvents dataSourceEvents;
    DataSourceCourses dataSourceCourses;
    DataSourceHasCourse dataSourceHasCourse;

    SimpleCursorAdapter dataAdapter;

    ListView listViewCourseList;

    long IDclickedCourse;
    boolean CourseClicked;

    Cursor cursorCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_create_event);

        listViewCourseList = (ListView)findViewById(android.R.id.list);

        dataSourceEvents = new DataSourceEvents(this);
        dataSourceCourses = new DataSourceCourses(this);
        dataSourceHasCourse = new DataSourceHasCourse(this);
        //dataSourceHasCourse = new DataSourceHasCourse(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_create_event, menu);
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

    public void OnClickCreateEvent(View view) {
        EditText editTextEvent = (EditText)findViewById(R.id.editTextName);
        if ((editTextEvent.length() != 0) && CourseClicked) {
            dataSourceEvents.CreateEvent(editTextEvent.getText().toString(), IDclickedCourse);
            Intent intent = new Intent(this, ActivityMainMenu.class);
            startActivity(intent);
        } else if (editTextEvent.length() == 0)
            editTextEvent.setError("Give the event a name");
        else if (!CourseClicked)
            editTextEvent.setError("Select a course for this event");
    }


    public void OnClickCreateCourse(View view) {
        Intent intent = new Intent(this, ActivityCreateCourse.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopManagingCursor(cursorCourses);
        cursorCourses.close();
        dataSourceEvents.close();
    }

    @Override
    protected void onResume(){
        super.onResume();
        try {
            dataSourceEvents.open();
            dataSourceCourses.open();
            dataSourceHasCourse.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        cursorCourses = dataSourceCourses.getCoursesList();
        startManagingCursor(cursorCourses);

        dataAdapter = new SimpleCursorAdapter(
                this,
                R.layout.course_info,
                cursorCourses,
                new String[]{TableCourses.COLUMN_NAME},
                new int[]{R.id.textViewCourse}
        );

        CourseClicked = false;
        listViewCourseList.setAdapter(dataAdapter);
        listViewCourseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                IDclickedCourse = id;
                CourseClicked = true;
            }
        });
    }
}
