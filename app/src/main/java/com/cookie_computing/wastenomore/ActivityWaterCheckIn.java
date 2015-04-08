package com.cookie_computing.wastenomore;

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
import android.widget.ToggleButton;

import java.sql.Date;
import java.text.SimpleDateFormat;


public class ActivityWaterCheckIn extends ActionBarActivity {

    public final static String TOTAL_GAL = "com.cookie-computing.wastenomore.TOTAL_GAL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_check_in);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_water_shower, menu);
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
    public void sendMessage(View view) {
        Intent intent = new Intent(this, ActivityWaterResults.class);

        double showerGals = getShowerGals();
        double bathGals = getBathGals();
        double laundryGals = getLaundryGals();

        // Calculate the number of gallons used for that day to send with the Intent
        // Don't add in toilet gallons until we know if there's been a check-in today so it doesn't
        // get added in twice.
        double totalGallons = showerGals + bathGals + laundryGals;


        // Send this check-in to the CheckIns DB
        CheckInDbHelper checkInDbHelper = new CheckInDbHelper(this);
        SQLiteDatabase wdb = checkInDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(CheckInContract.CheckIns.COLUMN_NAME_USAGE_TYPE_ID, CheckInDbHelper.WATER_ID);

        //See if there have been any check-ins already today
        double[] todaysInfo = getTodaysInfo();
        // If the ID is -1 then there was no check-in found for today
        if (todaysInfo[0] == -1) {
            // This is the first check-in of the day, so we'll include the toilet gallons
            totalGallons += getToiletGals();
            values.put(CheckInContract.CheckIns.COLUMN_NAME_DATE, getCurrentDate());
            values.put(CheckInContract.CheckIns.COLUMN_NAME_AMOUNT, totalGallons);

            // Insert the new row, returning the primary key value of the new row
            long newRowId = wdb.insert(
                    CheckInContract.CheckIns.TABLE_NAME,
                    CheckInContract.CheckIns.COLUMN_NAME_AMOUNT,
                    values);
            wdb.close();

        } else {
            double newGallons = todaysInfo[1] + totalGallons;
            values.put(CheckInContract.CheckIns.COLUMN_NAME_AMOUNT, newGallons);

            // we'll say update WHERE _ID = today's ID
            String[] selectionArgs = {"" + todaysInfo[0]};

            long newRowId = wdb.update(
                    CheckInContract.CheckIns.TABLE_NAME,
                    values,
                    CheckInContract.CheckIns._ID + "=?", // The columns for the WHERE clause
                    selectionArgs);                      // The values for the WHERE clause,
            wdb.close();
        }

        // What we send to with the Intent only has to be an int
        int finalGallons = (int) totalGallons;
        intent.putExtra(TOTAL_GAL, "" + finalGallons);
        startActivity(intent);
    }

    // Get the number of gallons that were used from the shower today
    public double getShowerGals() {
        // Get the number of minutes in the shower from the edit text view
        EditText showersEditText = (EditText) findViewById(R.id.shower_min_number);
        String showersString = showersEditText.getText().toString();

        double showerMin;
        if (showersString.equals("")) {
            showerMin = 0;
        } else {
            showerMin = Double.parseDouble(showersString);
            // Check that they've entered a non-negative number
            if(showerMin < 0) {
                showerMin = 0;
            }
        }
        // Calculate the number of gallons for showers
        // Conversions from http://www.epa.gov/WaterSense/pubs/indoor.html
        return showerMin * 2.5;
    }

    // Get the number of gallons that were used from baths today
    public double getBathGals() {
        // Get the number of minutes in the bath from the edit text view
        EditText bathsEditText = (EditText) findViewById(R.id.baths_number);
        String bathsString = bathsEditText.getText().toString();

        double baths;
        if (bathsString.equals("")) {
            baths = 0;
        } else {
            baths = Double.parseDouble(bathsString);
            // Check that they've entered a non-negative number
            if(baths < 0) {
                baths = 0;
            }
        }
        // Calculate the number of gallons for baths
        // Conversions from http://www.epa.gov/WaterSense/pubs/indoor.html
        return baths * 36;
    }

    // Get the number of gallons that were used from loads of laundry today
    public double getLaundryGals(){
        //Calculate the gallons used for laundry
        EditText laundryEditText = (EditText) findViewById(R.id.laundry_number);
        String laundryString = laundryEditText.getText().toString();
        double loads;
        if (laundryString.equals("")) {
            loads = 0;
        } else {
            loads = Double.parseDouble(laundryString);
            // Check that they've entered a non-negative number
            if(loads < 0) {
                loads = 0;
            }
        }
        // 25 gal/load: http://water.usgs.gov/edu/qa-home-percapita.html
        return loads * 25;
    }

    // Get the number of gallons that were used from using the restroom today (toilet-flushing and
    // hand washing.
    public double getToiletGals() {
        boolean hasConservingToilet = ((ToggleButton) findViewById(R.id.toggleButton)).isChecked();
        if(hasConservingToilet) {
            // 1.3 gal/flush + 1 gal/30 sec of hand washing after = 2.3 gal, 5 times a day = 11.5 gal/day
            return 11.5;
        }
        else {
            // 2 gal/flush + 1 gal/30 sec of hand washing after = 3 gal, 5 times a day = 15 gal/day
            return 15;
        }
    }


    /* Get the current date and put it in correct format so it can be put in the map */
    public String getCurrentDate() {
        final SimpleDateFormat parser = new SimpleDateFormat("ww yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date(System.currentTimeMillis()); //gets the current date
        return parser.format(date);
    }

    // Returns the row id and amount for this week's check-in, otherwise returns {-1,0}
    private double[] getTodaysInfo() {
        //Get the data from the DB
        CheckInDbHelper mDbHelper = new CheckInDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {CheckInContract.CheckIns.COLUMN_NAME_DATE, CheckInContract.CheckIns._ID, CheckInContract.CheckIns.COLUMN_NAME_AMOUNT};

        String[] selectionArgs = {"" + CheckInDbHelper.WATER_ID};

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

        // If there has been a check-in today, return the id of the check-in and the amount
        try {
            while(!c.isAfterLast()) {
                String dateString = c.getString(c.getColumnIndexOrThrow(CheckInContract.CheckIns.COLUMN_NAME_DATE));
                //The format for the date is "ww yyyy-MM-dd HH:mm:ss.SSS" so we'll check if the
                // first 13 characters match (ww yyyy-MM-dd)
                String week = dateString.substring(0, 13);

                String thisWeek = getCurrentDate();
                thisWeek = thisWeek.substring(0, 13);

                if (thisWeek.equals(week)) {
                    double[] info = new double[2];
                    info[0] = c.getInt(c.getColumnIndexOrThrow(CheckInContract.CheckIns._ID));
                    info[1] = c.getInt(c.getColumnIndexOrThrow(CheckInContract.CheckIns.COLUMN_NAME_AMOUNT));
                    return info;
                } else {
                    c.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new double[] {-1,0};
    }

}
