package com.example.pelorusbv.pelorus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;

import static com.android.volley.VolleyLog.TAG;


public class ActivityCreateBoat extends Activity {
    //Maakt een boot aan in de database
    //TODO: Meer gegevens dan alleen naam toevoegen
    //TODO: User die de boot creert toevoegen aan de boot met schipper-status
    DataSourceBoat dataSourceBoat;
    DataSourceCrews dataSourceCrews;
    long userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_boat);

        dataSourceBoat = new DataSourceBoat(this);
        dataSourceCrews = new DataSourceCrews(this);
        try {
            dataSourceCrews.open();
            dataSourceBoat.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        userid = pref.getLong("userID", 0);
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
        String BoatName = editTextBoatName.getText().toString();
        if (editTextBoatName.length() != 0) {
            long boatid = dataSourceBoat.CreateBoat(BoatName);
            dataSourceCrews.CreateCrews(userid, boatid);

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://192.168.1.5/addBoats.php?name=" + BoatName;

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            Log.i(TAG, "Response is: " + response);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG, "That didn't work!");
                }
            });
            queue.add(stringRequest);

            Intent intent = new Intent(this, ActivityMainMenu.class);
            startActivity(intent);
        } else
            editTextBoatName.setError("Give the boat a name");
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSourceBoat.close();
        dataSourceCrews.close();
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
    }

}
