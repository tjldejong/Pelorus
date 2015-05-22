package com.example.pelorusbv.pelorus;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class ActivityMainMenu extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
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
        Intent intent = new Intent(this, ActivityDashboard.class);
        startActivity(intent);
    }

    public void OnClickCreateEvent(View view){
        Intent intent = new Intent(this, ActivityCreateEvent.class);
        startActivity(intent);
    }

    public void onFragmentInteraction(Uri uri){

    }


    public void OnClickCreateBoat(View view) {
        Intent intent = new Intent(this, ActivityCreateBoat.class);
        startActivity(intent);
    }
}
