package com.example.pelorusbv.pelorus;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.SQLException;

import static com.example.pelorusbv.pelorus.ActivityDashboard.TAG;


public class ActivityMainMenu extends Activity {
    //Menu met buttons om naar elke activiteit te gaan.
    //TODO: Schipper buttons alleen voor schippers
    //TODO: Umpire buttons alleen voor umpires

    DataSourceUsers dataSourceUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        dataSourceUsers = new DataSourceUsers(this);
        try {
            dataSourceUsers.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        long id = pref.getLong("userID", 0);

        //long id = User.getInstance().getID();


        String email = dataSourceUsers.getEmail(id);
        Integer userType = dataSourceUsers.getUserType(id);
        Log.i(TAG, Integer.toString(userType));

        TableRow trCrew = (TableRow) findViewById(R.id.rowCrew);
        TableRow trCaptain = (TableRow) findViewById(R.id.rowCaptain);
        TableRow trUmpire = (TableRow) findViewById(R.id.rowUmpire);
        TableRow trSpectate = (TableRow) findViewById(R.id.rowSpectate);
        switch (userType) {
            case 0:
                trCaptain.setVisibility(View.GONE);
                trUmpire.setVisibility(View.GONE);
                trSpectate.setVisibility(View.GONE);
                break;
            case 1:
                trCrew.setVisibility(View.GONE);
                trUmpire.setVisibility(View.GONE);
                trSpectate.setVisibility(View.GONE);
                break;
            case 2:
                trCrew.setVisibility(View.GONE);
                trCaptain.setVisibility(View.GONE);
                trSpectate.setVisibility(View.GONE);
                break;
            case 3:
                trCrew.setVisibility(View.GONE);
                trCaptain.setVisibility(View.GONE);
                trUmpire.setVisibility(View.GONE);
                break;
        }



        TextView TextViewWelkom = (TextView)findViewById(R.id.textViewWelkom);
        TextViewWelkom.setText("Welcome to Pelorus " + email);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
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

    public void OnClickJoinEventList(View view){
        Intent intent = new Intent(this, ActivityJoinEvent.class);
        startActivity(intent);
    }

    public  void OnClickJoinBoat(View view){
        Intent intent = new Intent(this, ActivityJoinBoat.class);
        startActivity(intent);
    }

    public void OnClickCreateEvent(View view){
        Intent intent = new Intent(this, ActivityCreateEvent.class);
        startActivity(intent);
    }

    public void OnClickCreateBoat(View view) {
        Intent intent = new Intent(this, ActivityCreateBoat.class);
        startActivity(intent);
    }

    public void onFragmentInteraction(Uri uri){

    }

}
