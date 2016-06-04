package com.example.pelorusbv.pelorus;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.sql.SQLException;


public class ActivityCreateBoat extends Activity {
    //Maakt een boot aan in de database
    //TODO: Meer gegevens dan alleen naam toevoegen
    //TODO: User die de boot creert toevoegen aan de boot met schipper-status
    DataSourceBoat dataSourceBoat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_boat);

        dataSourceBoat = new DataSourceBoat(this);
        try {
            dataSourceBoat.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_boat, menu);
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

    public void OnClickCreateBoat(View view) {
        EditText editTextBoatName = (EditText) findViewById(R.id.editTextBoatName);
        if (editTextBoatName.length() != 0) {
            dataSourceBoat.CreateBoat(editTextBoatName.getText().toString());
            Intent intent = new Intent(this, ActivityMainMenu.class);
            startActivity(intent);
        } else
            editTextBoatName.setError("Give the boat a name");
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSourceBoat.close();
    }

    @Override
    protected void onResume(){
        super.onResume();
        try {
            dataSourceBoat.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
