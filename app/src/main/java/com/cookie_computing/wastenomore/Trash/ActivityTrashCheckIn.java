package com.cookie_computing.wastenomore.Trash;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.cookie_computing.wastenomore.Global;
import com.cookie_computing.wastenomore.R;
import com.cookie_computing.wastenomore.db.CheckInContract;
import com.cookie_computing.wastenomore.db.CheckInContract.CheckIns;
import com.cookie_computing.wastenomore.db.CheckInDbHelper;


public class ActivityTrashCheckIn extends ActionBarActivity {

    public final static String TOTAL_GAL = "com.cookie-computing.wastenomore.TOTAL_GAL";
    Global global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash_check_in);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        global = ((Global)getApplicationContext());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trash_calc, menu);
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

    /** Called when the user clicks the Check In button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, ActivityTrashResults.class);

        // Get the edit text views for the bags
        EditText smallEditText = (EditText) findViewById(R.id.small_bags_number);
        EditText medEditText = (EditText) findViewById(R.id.med_bags_number);
        EditText largeEditText = (EditText) findViewById(R.id.large_bags_number);

        // Get the number of bags
        String smallBagString = smallEditText.getText().toString();
        String medBagString = medEditText.getText().toString();
        String largeBagString = largeEditText.getText().toString();

        double smallBags, medBags, largeBags;
        if (smallBagString.equals("")) {
            smallBags = 0;
        } else {
            smallBags = Double.parseDouble(smallBagString);
            // Check that they've entered a non-negative number
            if(smallBags < 0) {
                smallBags = 0;
            }
        }
        if (medBagString.equals("")) {
            medBags = 0;
        } else {
            medBags = Double.parseDouble(medBagString);
            // Check that they've entered a non-negative number
            if(medBags < 0) {
                medBags = 0;
            }
        }
        if (largeBagString.equals("")) {
            largeBags = 0;
        } else {
            largeBags = Double.parseDouble(largeBagString);
            // Check that they've entered a non-negative number
            if(largeBags < 0) {
                largeBags = 0;
            }
        }

        // Calculate the number of gallons and weight used for that week to send with the Intent
        double smallBagWeight = smallBags * 9;
        double medBagWeight = medBags * 15;
        double largeBagWeight = largeBags * 24;
        double totalWeight = smallBagWeight + medBagWeight + largeBagWeight;


        // Send this check-in to the CheckIns DB
        CheckInDbHelper checkInDbHelper = new CheckInDbHelper(this);
        SQLiteDatabase wdb = checkInDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(CheckIns.COLUMN_NAME_USAGE_TYPE_ID, CheckInDbHelper.TRASH_ID);

        //See if there have been any check-ins already this week
        double[] thisWeeksInfo = getThisWeeksDate();
        // If the ID is -1 then there was no check-in found for this week
        if (thisWeeksInfo[0] == -1) {
            values.put(CheckIns.COLUMN_NAME_DATE, global.getCurrentDate());
            values.put(CheckIns.COLUMN_NAME_AMOUNT, totalWeight);

            // Insert the new row, returning the primary key value of the new row
            long newRowId = wdb.insert(
                    CheckInContract.CheckIns.TABLE_NAME,
                    CheckIns.COLUMN_NAME_AMOUNT,
                    values);
            wdb.close();
            checkInDbHelper.close();

        } else {
            double newWeight = thisWeeksInfo[1] + totalWeight;
            values.put(CheckIns.COLUMN_NAME_AMOUNT, newWeight);

            // we'll say update WHERE _ID = this week's ID
            String[] selectionArgs = {"" + thisWeeksInfo[0]};

            long newRowId = wdb.update(
                    CheckIns.TABLE_NAME,
                    values,
                    CheckIns._ID + "=?", // The columns for the WHERE clause
                    selectionArgs);      // The values for the WHERE clause,
            wdb.close();
            checkInDbHelper.close();
        }

        int finalWeight = (int) totalWeight;
        intent.putExtra(TOTAL_GAL, "" + finalWeight);
        startActivity(intent);
    }


    // Returns the row id and amount for this week's check-in, otherwise returns {-1,0}
    private double[] getThisWeeksDate() {
        //Get the data from the DB
        CheckInDbHelper mDbHelper = new CheckInDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {CheckIns.COLUMN_NAME_DATE, CheckIns._ID, CheckIns.COLUMN_NAME_AMOUNT};

        String[] selectionArgs = {"" + CheckInDbHelper.TRASH_ID};

        Cursor c = db.query(
                CheckInContract.CheckIns.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                CheckInContract.CheckIns.COLUMN_NAME_USAGE_TYPE_ID + "=?", // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // don't sort the rows
        );

        c.moveToFirst();

        // If there has been a check-in this week, return the id of the check in and the amount
        try {
            while(!c.isAfterLast()) {
                String dateString = c.getString(c.getColumnIndexOrThrow(CheckInContract.CheckIns.COLUMN_NAME_DATE));
                //The format for the date is "ww yyyy-MM-dd HH:mm:ss.SSS" so we'll check if the
                // first 7 characters match (ww yyyy)
                String week = dateString.substring(0, 7);

                String thisWeek = global.getCurrentDate();
                thisWeek = thisWeek.substring(0, 7);

                if (thisWeek.equals(week)) {
                    double[] info = new double[2];
                    info[0] = c.getInt(c.getColumnIndexOrThrow(CheckIns._ID));
                    info[1] = c.getInt(c.getColumnIndexOrThrow(CheckIns.COLUMN_NAME_AMOUNT));
                    return info;
                }
                // TO DO: Check if this was the end of the year. Weeks 53 and 1 are the same.
                else {
                    c.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new double[] {-1,0};
    }
}
