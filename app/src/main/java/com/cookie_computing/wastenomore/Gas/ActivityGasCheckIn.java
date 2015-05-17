package com.cookie_computing.wastenomore.Gas;

import android.content.ContentValues;
import android.content.Intent;
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


public class ActivityGasCheckIn extends ActionBarActivity {

    public final static String GAS_AMOUNTS = "com.cookie-computing.wastenomore.GAS_AMOUNTS";
    SQLiteDatabase wdb;
    Global global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_check_in);

        // Fill in default values based on what they've entered before
        global = ((Global)getApplicationContext());
        final EditText editGallons = (EditText) findViewById(R.id.gallons_bought);
        editGallons.setText("" + (int) global.getTypicalGasGallons());
        final EditText editMiles = (EditText) findViewById(R.id.miles_driven);
        editMiles.setText("" + (int) global.getTypicalGasMiles());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_gas_check_in, menu);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wdb != null && wdb.isOpen()) {
            wdb.close();
        }
    }

    /** Called when the user clicks the Check In button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, ActivityGasResults.class);

        double avgMileage = getGasMileage();

        // Send this check-in to the CheckIns DB
        CheckInDbHelper checkInDbHelper = new CheckInDbHelper(this);
        wdb = checkInDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(CheckInContract.CheckIns.COLUMN_NAME_USAGE_TYPE_ID, CheckInDbHelper.GAS_MILEAGE_ID);
        values.put(CheckInContract.CheckIns.COLUMN_NAME_DATE, global.getCurrentDate());
        values.put(CheckInContract.CheckIns.COLUMN_NAME_AMOUNT, avgMileage);

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


        // What we send to with the Intent only has to be an int
        int mileage = (int) avgMileage;
        intent.putExtra(GAS_AMOUNTS, "" + mileage);
        startActivity(intent);
    }

    // Get the average miles per gallon for this tank
    public double getGasMileage() {
        double miles = getNumFromEditText(R.id.miles_driven);
        double gals = getNumFromEditText(R.id.gallons_bought);
        global.setTypicalGasGallons(gals);
        global.setTypicalGasMiles(miles);
        global.setTotalMiles(global.getTotalMiles() + miles);
        global.setTotalGals(global.getTotalGals() + gals);
        return (int) (miles / gals);
    }

    // Helper method to get the number from the EditText with the given ID. Always gives positive:
    // If input is negative, this returns 0.
    public double getNumFromEditText(int editTextID){
        EditText editText = (EditText) findViewById(editTextID);
        String string = editText.getText().toString();
        if (string.equals("")) {
            return 0;
        } else {
            double num = Double.parseDouble(string);
            // Check that they've entered a non-negative number
            if(num < 0) {
                return 0;
            }
            return num;
        }
    }

}
