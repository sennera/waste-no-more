package com.cookie_computing.wastenomore.Gas;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cookie_computing.wastenomore.R;

public class ActivityChooseGasTrack extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_gas_track);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_acitivity_choose_gas_track, menu);
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

    /** Called when the user clicks the Fuel Track button */
    public void goToGasTrack(View view) {
        Intent intent = new Intent(this, ActivityGasTrack.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Saved Gas Track button */
    public void goToGasSavedTrack(View view) {
        Intent intent = new Intent(this, ActivityGasSavedTrack.class);
        startActivity(intent);
    }
}
