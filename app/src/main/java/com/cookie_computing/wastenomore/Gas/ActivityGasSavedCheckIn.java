package com.cookie_computing.wastenomore.Gas;

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
import com.cookie_computing.wastenomore.db.CheckInDbHelper;


public class ActivityGasSavedCheckIn extends ActionBarActivity {

    public final static String GAS_AMOUNTS = "com.cookie-computing.wastenomore.GAS_AMOUNTS";
    SQLiteDatabase wdb;
    Global global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_saved_check_in);
        global = ((Global)getApplicationContext());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_gas_saved_check_in, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wdb != null && wdb.isOpen()) {
            wdb.close();
        }
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
        Intent intent = new Intent(this, ActivityGasSavedResults.class);

        double gals = getGallons();

        // Send this check-in to the CheckIns DB
        CheckInDbHelper checkInDbHelper = new CheckInDbHelper(this);
        wdb = checkInDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(CheckInContract.CheckIns.COLUMN_NAME_USAGE_TYPE_ID, CheckInDbHelper.GAS_SAVINGS_ID);


        //See if there have been any check-ins already today
        double[] todaysInfo = getTodaysInfo();

        // If the ID is -1 then there was no check-in found for today
        if (todaysInfo[0] == -1) {
            values.put(CheckInContract.CheckIns.COLUMN_NAME_DATE, global.getCurrentDate());
            values.put(CheckInContract.CheckIns.COLUMN_NAME_AMOUNT, gals);

            try{
                // Insert the new row, returning the primary key value of the new row
                long newRowId = wdb.insert(
                        CheckInContract.CheckIns.TABLE_NAME,
                        CheckInContract.CheckIns.COLUMN_NAME_AMOUNT,
                        values);
                wdb.close();
            } catch (Exception e) {
                System.out.println("An error occurred when trying to insert the entry in the database.");
                if(wdb.isOpen()) {
                    wdb.close();
                }
                if(checkInDbHelper != null){
                    checkInDbHelper.close();
                }
            }
        } else {
            double newGallons = todaysInfo[1] + gals;
            values.put(CheckInContract.CheckIns.COLUMN_NAME_AMOUNT, newGallons);

            // we'll say update WHERE _ID = today's ID
            String[] selectionArgs = {"" + todaysInfo[0]};

            try{
                long newRowId = wdb.update(
                        CheckInContract.CheckIns.TABLE_NAME,
                        values,
                        CheckInContract.CheckIns._ID + "=?", // The columns for the WHERE clause
                        selectionArgs);                      // The values for the WHERE clause,
                wdb.close();
            } catch (Exception e) {
                if(wdb.isOpen()) {
                    wdb.close();
                }
                if(checkInDbHelper != null){
                    checkInDbHelper.close();
                }
            }
        }

        intent.putExtra(GAS_AMOUNTS, "" + gals);
        startActivity(intent);
    }

    // Get the number of gallons that were saved for this trip, rounded to the 2nd decimal place
    public double getGallons() {
        double mpg = getAvgMileage();
        double miles = getNumFromEditText(R.id.miles_saved);
        double gals = miles / mpg;
        return ((int)(gals*100)) / 100.0;
    }

    // Gets the average mileage for this user, other wise returns the default mileage for
    // cars in 2014 (from data in MTH 366 class data set)
    public double getAvgMileage() {
        double gals = global.getTotalGals();
        if(gals == 0) {
            // the user has not stored any values for the mileage, so use default
            return 29.3;
        }
        else {
            return global.getTotalMiles() / gals;
        }
    }

    // Helper method to get the number from the EditText with the given ID. Always gives positive:
    // If input is negative, this returns 0.
    public double getNumFromEditText(int editTextID){
        EditText editText = (EditText) findViewById(editTextID);
        return global.getPositiveNumFromString(editText.getText().toString());
    }

    // Returns the row id and amount for today's check-in, otherwise returns {-1,0}
    private double[] getTodaysInfo() {
        //Get the data from the DB
        CheckInDbHelper mDbHelper = new CheckInDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {CheckInContract.CheckIns.COLUMN_NAME_DATE, CheckInContract.CheckIns._ID, CheckInContract.CheckIns.COLUMN_NAME_AMOUNT};

        String[] selectionArgs = {"" + CheckInDbHelper.GAS_SAVINGS_ID};

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
            String today = global.getCurrentDate();
            today = today.substring(0, 13);

            while(!c.isAfterLast()) {
                String dateString = c.getString(c.getColumnIndexOrThrow(CheckInContract.CheckIns.COLUMN_NAME_DATE));
                //The format for the date is "ww yyyy-MM-dd HH:mm:ss.SSS" so we'll check if the
                // first 13 characters match (ww yyyy-MM-dd)
                String day = dateString.substring(0, 13);

                if (today.equals(day)) {
                    double[] info = new double[2];
                    info[0] = c.getInt(c.getColumnIndexOrThrow(CheckInContract.CheckIns._ID));
                    info[1] = c.getDouble(c.getColumnIndexOrThrow(CheckInContract.CheckIns.COLUMN_NAME_AMOUNT));
                    c.close();
                    db.close();
                    mDbHelper.close();
                    return info;
                } else {
                    c.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        c.close();
        db.close();
        mDbHelper.close();
        return new double[] {-1,0};
    }
}
