package com.cookie_computing.wastenomore;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import com.cookie_computing.wastenomore.CheckInContract.CheckIns;
import com.cookie_computing.wastenomore.UsageTypeContract.UsageTypes;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class TrashCalc extends ActionBarActivity {

    public final static String EXTRA_MESSAGE = "com.cookie-computing.wastenomore.TOTAL_GAL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash_calc);
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
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** Called when the user clicks the Calculate button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, TrashResults.class);

        // Get the edit text views for the bags
        EditText smallEditText = (EditText) findViewById(R.id.small_bags_number);
        EditText medEditText = (EditText) findViewById(R.id.med_bags_number);
        EditText largeEditText = (EditText) findViewById(R.id.large_bags_number);

        // Get the number of bags
        String smallBagString = smallEditText.getText().toString();
        String medBagString = medEditText.getText().toString();
        String largeBagString = largeEditText.getText().toString();

        int smallBags, medBags, largeBags;
        if (smallBagString.equals("")) {
            smallBags = 0;
        } else {
            smallBags = Integer.parseInt(smallBagString);
        }
        if (medBagString.equals("")) {
            medBags = 0;
        } else {
            medBags = Integer.parseInt(medBagString);
        }
        if (largeBagString.equals("")) {
            largeBags = 0;
        } else {
            largeBags = Integer.parseInt(largeBagString);
        }

        // Calculate the number of gallons ad weight used for that week to send with the Intent
        int smallBagWeight = smallBags * 9;
        int medBagWeight = medBags * 15;
        int largeBagWeight = largeBags * 24;
        int totalWeight = smallBagWeight + medBagWeight + largeBagWeight;


        // Send this check-in to the CheckIns DB
        CheckInDbHelper checkInDbHelper = new CheckInDbHelper(this);
        SQLiteDatabase wdb = checkInDbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(CheckIns.COLUMN_NAME_DATE, getCurrentDate());
        values.put(CheckIns.COLUMN_NAME_USAGE_TYPE_ID, getTrashID());
        values.put(CheckIns.COLUMN_NAME_AMOUNT, totalWeight);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = wdb.insert(
                CheckInContract.CheckIns.TABLE_NAME,
                CheckIns.COLUMN_NAME_AMOUNT,
                values);
        System.out.println("Stored to Database: " + totalWeight + " lbs on " + getCurrentDate() +
                " for usage type ID " + getTrashID());
        wdb.close();

        intent.putExtra(EXTRA_MESSAGE, "" + totalWeight);
        startActivity(intent);
    }

    /* Get the current date and put it in correct format so it can be put in the map */
    public String getCurrentDate() {
        final SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date(System.currentTimeMillis()); //gets the current date
        return parser.format(date);
    }

    /* Get the UsageTypeID for trash from the UsageTypes DB */
    public int getTrashID() {
        CheckInDbHelper mDbHelper = new CheckInDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                UsageTypeContract.UsageTypes._ID};

        // How the results should be sorted in the resulting Cursor
        String[] selectionArgs = {"Trash"};

        Cursor c = db.query(
                UsageTypeContract.UsageTypes.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                UsageTypeContract.UsageTypes.COLUMN_NAME_USAGE_TYPE + "=?",                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // don't sort the rows
        );

        c.moveToFirst();
        db.close();
        int id = c.getInt(
                c.getColumnIndexOrThrow(UsageTypeContract.UsageTypes._ID)
        );
        c.close();
        System.out.println("Found Trash ID: " + id);
        return id;
    }
}
