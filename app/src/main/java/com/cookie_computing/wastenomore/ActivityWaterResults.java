package com.cookie_computing.wastenomore;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class ActivityWaterResults extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_results);

        // Get the message from the intent
        Intent intent = getIntent();
        String totalGallons = intent.getStringExtra(ActivityWaterCheckIn.TOTAL_GAL);

        String message = "That adds up to about " + totalGallons + " gallons of water.";

        // Edit the text in the text view
        final TextView textView = (TextView) findViewById(R.id.usageResults);
        textView.setTextSize(30);
        textView.setText(message);

        final TextView textView2 = (TextView) findViewById(R.id.addedToWeekOrDayNote);
        textView2.setText(R.string.day_results_small_text);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_water_results, menu);
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
//        Intent intent = new Intent(this, ActivityWaterFact.class);
//        startActivity(intent);
    }

    /** Called when the user clicks the Past Usages button */
    public void goToTrackTrash(View view) {
//        Intent intent = new Intent(this, ActivityWaterTrack.class);
//        startActivity(intent);
    }
}
