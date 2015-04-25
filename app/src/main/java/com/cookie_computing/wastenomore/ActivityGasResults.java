package com.cookie_computing.wastenomore;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class ActivityGasResults extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_results);

        // Get the message from the intent
        Intent intent = getIntent();
        String mileage = intent.getStringExtra(ActivityGasCheckIn.GAS_AMOUNTS);

        String message = "You averaged " + mileage + " miles per gallon of gas.";

        // Create the text view
        final TextView textView = (TextView) findViewById(R.id.usageResults);
        textView.setTextSize(30);
        textView.setText(message);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_gas_results, menu);
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

    /** Called when the user clicks the Fact button */
    public void goToFact(View view) {
//        Intent intent = new Intent(this, ActivityGasFact.class);
//        startActivity(intent);
    }

    /** Called when the user clicks the Past Usages button */
    public void goToTrack(View view) {
        Intent intent = new Intent(this, ActivityGasTrack.class);
        startActivity(intent);
    }
}
