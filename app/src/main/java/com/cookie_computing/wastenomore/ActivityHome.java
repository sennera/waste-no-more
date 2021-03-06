package com.cookie_computing.wastenomore;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cookie_computing.wastenomore.Gas.ActivityGasHome;
import com.cookie_computing.wastenomore.Trash.ActivityTrashHome;
import com.cookie_computing.wastenomore.Water.ActivityWaterHome;


public class ActivityHome extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the ActivityHome/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** Called when the user clicks the Trash button */
    public void goToTrash(View view) {
        openTrashHome();
    }

    private void openTrashHome() {
        Intent intent = new Intent(this, ActivityTrashHome.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Water button */
    public void goToWater(View view) {
        Intent intent = new Intent(this, ActivityWaterHome.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Gasoline button */
    public void goToGas(View view) {
        Intent intent = new Intent(this, ActivityGasHome.class);
        startActivity(intent);
    }

    /**
     * Called when the user clicks the Why button
     */
    public void goToWhy(View view) {
        Intent intent = new Intent(this, ActivityAbout.class);
        startActivity(intent);
    }

}
