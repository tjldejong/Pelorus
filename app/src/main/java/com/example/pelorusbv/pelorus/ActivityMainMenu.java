package com.example.pelorusbv.pelorus;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.sql.SQLException;


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

        TextView TextViewWelkom = (TextView)findViewById(R.id.textViewWelkom);
        TextViewWelkom.setText("welkom bij Pelorus " + email);
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
