package com.cookie_computing.wastenomore.Trash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cookie_computing.wastenomore.R;


public class ActivityTrashHome extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trash_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.water) {
            //openWaterHome();
            return true;
        }
        //noinspection SimplifiableIfStatement
        else if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** Called when the user clicks the Check In button */
    public void goToCheckIn(View view) {
        Intent intent = new Intent(this, ActivityTrashCheckIn.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Past Usage button */
    public void goToTrack(View view) {
        Intent intent = new Intent(this, ActivityTrashTrack.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Tips button */
    public void goToTips(View view) {
        Intent intent = new Intent(this, ActivityTrashTips.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Facts button */
    public void goToFact(View view) {
        Intent intent = new Intent(this, ActivityTrashFact.class);
        startActivity(intent);
    }
}
