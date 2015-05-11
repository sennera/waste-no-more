package com.cookie_computing.wastenomore.Gas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cookie_computing.wastenomore.R;


public class ActivityChooseGasCheckIn extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_gas_check_in);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_acitivity_choose_gas_check_in, menu);
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

    /** Called when the user clicks the Fuel Check In button */
    public void goToGasCheckIn(View view) {
        Intent intent = new Intent(this, ActivityGasCheckIn.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Saved Gas Check In button */
    public void goToGasSavedCheckIn(View view) {
        Intent intent = new Intent(this, ActivityGasSavedCheckIn.class);
        startActivity(intent);
    }
}
