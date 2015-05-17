package com.cookie_computing.wastenomore.Water;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cookie_computing.wastenomore.R;


public class ActivityWaterHome extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_water_home, menu);
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

    /** Called when the user clicks the Check In button */
    public void goToCheckIn(View view) {
        Intent intent = new Intent(this, ActivityWaterCheckIn.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Past Usage button */
    public void goToTrack(View view) {
        Intent intent = new Intent(this, ActivityWaterTrack.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Stats button */
    public void goToStats(View view) {
        Intent intent = new Intent(this, ActivityWaterStats.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Tips button */
    public void goToTips(View view) {
        Intent intent = new Intent(this, ActivityWaterTips.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Facts button */
    public void goToFact(View view) {
        Intent intent = new Intent(this, ActivityWaterFact.class);
        startActivity(intent);
    }
}
