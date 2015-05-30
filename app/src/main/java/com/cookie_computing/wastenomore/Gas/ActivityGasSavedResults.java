package com.cookie_computing.wastenomore.Gas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookie_computing.wastenomore.R;

public class ActivityGasSavedResults extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_gas_saved_results);

        // Get the message from the intent
        Intent intent = getIntent();
        String gals = intent.getStringExtra(ActivityGasCheckIn.GAS_AMOUNTS);

        String message = "Great job! You saved " + gals + " gallons of gas from that trip.";

        // Create the text view
        final TextView textView = (TextView) findViewById(R.id.usageResults);
        textView.setTextSize(30);
        textView.setText(message);

        String message2 = "It'll be added for your total for today. Look at your past saved gas to see how much you've saved!";
        final TextView textView2 = (TextView) findViewById(R.id.addedToWeekOrDayNote);
        textView2.setText(message2);

        Button button = (Button) findViewById(R.id.usageButton);
        button.setText("Past Saved Gas");

        // Remove the Fact Button
        Button button2 = (Button) findViewById(R.id.factButton);
        ((RelativeLayout) button2.getParent()).removeView(button2);

        // Since fact button is gone, set what the past usage button is below
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) button.getLayoutParams();
        params.addRule(RelativeLayout.BELOW, R.id.addedToWeekOrDayNote);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_gas_saved_results, menu);
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


    /** Called when the user clicks the Past Usages button */
    public void goToTrack(View view) {
        Intent intent = new Intent(this, ActivityGasSavedTrack.class);
        startActivity(intent);
    }
}
