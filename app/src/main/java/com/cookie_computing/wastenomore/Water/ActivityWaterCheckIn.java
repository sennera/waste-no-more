package com.cookie_computing.wastenomore.Water;

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

import com.cookie_computing.wastenomore.Global;
import com.cookie_computing.wastenomore.R;
import com.cookie_computing.wastenomore.db.CheckInContract;
import com.cookie_computing.wastenomore.db.CheckInDbHelper;


public class ActivityWaterCheckIn extends ActionBarActivity {

    public final static String TOTAL_GAL = "com.cookie-computing.wastenomore.TOTAL_GAL";
    SQLiteDatabase wdb;
    Global global;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_check_in);

        // Fill in default values based on what they've entered before
        global = ((Global)getApplicationContext());
        final EditText editShower = (EditText) findViewById(R.id.shower_min_number);
        editShower.setText("" + (int) global.getTypicalShower());
        final EditText editHandDish = (EditText) findViewById(R.id.hand_dish_min);
        editHandDish.setText("" + (int) global.getTypicalHandDishes());
        final EditText editDishwasher = (EditText) findViewById(R.id.dishwasher_number);
        editDishwasher.setText("" + (int) global.getTypicalDishwasher());
        final EditText editTeeth = (EditText) findViewById(R.id.teeth_brushing_min);
        editTeeth.setText("" + (int) global.getTypicalBrushTeeth());
        final EditText editOther = (EditText) findViewById(R.id.other_running_min);
        editOther.setText("" + (int) global.getTypicalBrushTeeth());
        final ToggleButton toggleToilet = (ToggleButton) findViewById(R.id.toggleButton);
        toggleToilet.setChecked(global.getTypicalConservingToilet());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wdb != null && wdb.isOpen()) {
            wdb.close();
        }
    }

    /** Called when the user clicks the Check In button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, ActivityWaterResults.class);

        double showerBathGals = getShowerGals() + getBathGals();
        double laundryGals = getLaundryGals();
        double dishWashingGals = getHandDishWashingGals() + getDishwasherGals();
        double personalCare = getTeethBrushingGals() + getOtherRunningWaterGals();

        // Calculate the number of gallons used for that day to send with the Intent
        // Don't add in toilet gallons until we know if there's been a check-in today so it doesn't
        // get added in twice.
        double totalGallons = showerBathGals + laundryGals + dishWashingGals + personalCare;


        // Send this check-in to the CheckIns DB
        CheckInDbHelper checkInDbHelper = new CheckInDbHelper(this);
        wdb = checkInDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(CheckInContract.CheckIns.COLUMN_NAME_USAGE_TYPE_ID, CheckInDbHelper.WATER_ID);

        //See if there have been any check-ins already today
        double[] todaysInfo = getTodaysInfo();
        // If the ID is -1 then there was no check-in found for today
        if (todaysInfo[0] == -1) {
            System.out.println("first check in of the day");
            // This is the first check-in of the day, so we'll include the toilet gallons
            totalGallons += getToiletGals();
            values.put(CheckInContract.CheckIns.COLUMN_NAME_DATE, global.getCurrentDate());
            values.put(CheckInContract.CheckIns.COLUMN_NAME_AMOUNT, totalGallons);

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
            double newGallons = todaysInfo[1] + totalGallons;
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

        // What we send to with the Intent only has to be an int
        int finalGallons = (int) totalGallons;
        intent.putExtra(TOTAL_GAL, "" + finalGallons);
        startActivity(intent);
    }

    // Get the number of gallons that were used from the shower today
    public double getShowerGals() {
        // 2.5 gal/min: http://www.epa.gov/WaterSense/pubs/indoor.html
        double showerMin = getNumFromEditText(R.id.shower_min_number);
        global.setTypicalShower(showerMin);
        return showerMin * 2.5;
    }

    // Get the number of gallons that were used from baths today
    public double getBathGals() {
        // 36 gal/bath: http://www.epa.gov/WaterSense/pubs/indoor.html
        double baths = getNumFromEditText(R.id.baths_number);
        return baths * 36;
    }

    // Get the number of gallons that were used from loads of laundry today
    public double getLaundryGals(){
        // 25 gal/load: http://water.usgs.gov/edu/qa-home-percapita.html
        double loads = getNumFromEditText(R.id.laundry_number);
        return loads * 25;
    }

    // Get the number of gallons that were used from hand dish washing today
    public double getHandDishWashingGals(){
        // 3 gal/min: http://water.usgs.gov/edu/qa-home-percapita.html
        double min = getNumFromEditText(R.id.hand_dish_min);
        global.setTypicalHandDishes(min);
        return min * 3;
    }

    // Get the number of gallons that were used from using the dishwasher today
    public double getDishwasherGals(){
        // 12.5 gal/load: http://www.home-water-works.org/indoor-use/dishwasher
        double runs = getNumFromEditText(R.id.dishwasher_number);
        global.setTypicalDishwasher(runs);
        return runs * 12.5;
    }

    // Get the number of gallons that were used from using teeth-brushing today
    public double getTeethBrushingGals(){
        // 2 gal/min: http://www.epa.gov/WaterSense/pubs/indoor.html
        double min = getNumFromEditText(R.id.teeth_brushing_min);
        global.setTypicalBrushTeeth(min);
        return min * 2;
    }

    // Get the number of gallons that were used from other ways of using water today
    public double getOtherRunningWaterGals(){
        // 2 gal/min: http://www.epa.gov/WaterSense/pubs/indoor.html
        double min = getNumFromEditText(R.id.other_running_min);
        global.setTypicalOtherWater(min);
        return min * 2;
    }

    // Helper method to get the number from the EditText with the given ID. Always gives positive:
    // If input is negative, this returns 0.
    public double getNumFromEditText(int editTextID){
        EditText editText = (EditText) findViewById(editTextID);
        return global.getPositiveNumFromString(editText.getText().toString());
    }

    // Get the number of gallons that were used from using the restroom today (toilet-flushing and
    // hand washing.
    public double getToiletGals() {
        boolean hasConservingToilet = ((ToggleButton) findViewById(R.id.toggleButton)).isChecked();
        global.setTypicalConservingToilet(hasConservingToilet);
        if(hasConservingToilet) {
            // 1.3 gal/flush + 1 gal/30 sec of hand washing after = 2.3 gal, 5 times a day = 11.5 gal/day
            return 11.5;
        }
        else {
            // 2 gal/flush + 1 gal/30 sec of hand washing after = 3 gal, 5 times a day = 15 gal/day
            return 15;
        }
    }


    // Returns the row id and amount for today's check-in, otherwise returns {-1,0}
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
                String day = dateString.substring(0, 13);
                System.out.println("-"+day+"-");

                String today = global.getCurrentDate();
                today = today.substring(0, 13);
                System.out.println("-"+today+"-");

                if (today.equals(day)) {
                    System.out.println("Found that this day was in db");

                    double[] info = new double[2];
                    info[0] = c.getInt(c.getColumnIndexOrThrow(CheckInContract.CheckIns._ID));
                    info[1] = c.getInt(c.getColumnIndexOrThrow(CheckInContract.CheckIns.COLUMN_NAME_AMOUNT));
                    System.out.println("Sending info back to method");

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




//    public void fillDB() {
//        final SimpleDateFormat parser = new SimpleDateFormat("ww yyyy-MM-dd HH:mm:ss.SSS");
//
//        storeInDb(parser.format(1429156800000L), 130, CheckInDbHelper.WATER_ID);
//        storeInDb(parser.format(1429243200000L), 95, CheckInDbHelper.WATER_ID);
//        storeInDb(parser.format(1429502400000L), 115, CheckInDbHelper.WATER_ID);
//        storeInDb(parser.format(1429588800000L), 89, CheckInDbHelper.WATER_ID);
//        storeInDb(parser.format(1429675200000L), 110, CheckInDbHelper.WATER_ID);
//        storeInDb(parser.format(1430020800000L), 87, CheckInDbHelper.WATER_ID);
//        storeInDb(parser.format(1430020800000L), 90, CheckInDbHelper.WATER_ID);
//        storeInDb(parser.format(1430193600000L), 95, CheckInDbHelper.WATER_ID);
//        storeInDb(parser.format(1430366400000L), 87, CheckInDbHelper.WATER_ID);
//        storeInDb(parser.format(1430452800000L), 77, CheckInDbHelper.WATER_ID);
//        storeInDb(parser.format(1430539200000L), 74, CheckInDbHelper.WATER_ID);
//        storeInDb(parser.format(1430625600000L), 82, CheckInDbHelper.WATER_ID);
//        storeInDb(parser.format(1430798400000L), 85, CheckInDbHelper.WATER_ID);
//        storeInDb(parser.format(1430884800000L), 84, CheckInDbHelper.WATER_ID);
//        storeInDb(parser.format(1431057600000L), 92, CheckInDbHelper.WATER_ID);
//        storeInDb(parser.format(1431230400000L), 85, CheckInDbHelper.WATER_ID);
//        storeInDb(parser.format(1431403200000L), 82, CheckInDbHelper.WATER_ID);
//        storeInDb(parser.format(1431489600000L), 89, CheckInDbHelper.WATER_ID);
//
//        storeInDb(parser.format(1428465600000L), 37, CheckInDbHelper.TRASH_ID);
//        storeInDb(parser.format(1429070400000L), 30, CheckInDbHelper.TRASH_ID);
//        storeInDb(parser.format(1429675200000L), 40, CheckInDbHelper.TRASH_ID);
//        storeInDb(parser.format(1430452800000L), 28, CheckInDbHelper.TRASH_ID);
//        storeInDb(parser.format(1430884800000L), 22, CheckInDbHelper.TRASH_ID);
//
//        storeInDb(parser.format(1426824000000L), 30.7, CheckInDbHelper.GAS_MILEAGE_ID);
//        storeInDb(parser.format(1428120000000L), 25, CheckInDbHelper.GAS_MILEAGE_ID);
//        storeInDb(parser.format(1428897600000L), 34, CheckInDbHelper.GAS_MILEAGE_ID);
//        storeInDb(parser.format(1430884800000L), 35, CheckInDbHelper.GAS_MILEAGE_ID);
//
//        storeInDb(parser.format(1429156800000L), .53, CheckInDbHelper.GAS_SAVINGS_ID);
//        storeInDb(parser.format(1429243200000L), .67, CheckInDbHelper.GAS_SAVINGS_ID);
//        storeInDb(parser.format(1429588800000L), .76, CheckInDbHelper.GAS_SAVINGS_ID);
//        storeInDb(parser.format(1429675200000L), .34, CheckInDbHelper.GAS_SAVINGS_ID);
//        storeInDb(parser.format(1430020800000L), .17, CheckInDbHelper.GAS_SAVINGS_ID);
//        storeInDb(parser.format(1430193600000L), .2, CheckInDbHelper.GAS_SAVINGS_ID);
//        storeInDb(parser.format(1430452800000L), .2, CheckInDbHelper.GAS_SAVINGS_ID);
//        storeInDb(parser.format(1430539200000L), .24, CheckInDbHelper.GAS_SAVINGS_ID);
//        storeInDb(parser.format(1430798400000L), .2, CheckInDbHelper.GAS_SAVINGS_ID);
//        storeInDb(parser.format(1430884800000L), .12, CheckInDbHelper.GAS_SAVINGS_ID);
//        storeInDb(parser.format(1431403200000L), .23, CheckInDbHelper.GAS_SAVINGS_ID);
//        storeInDb(parser.format(1431489600000L), .3, CheckInDbHelper.GAS_SAVINGS_ID);
//    }
//
//    private void storeInDb (String dateString, double gals, int usageType){
//        // Send this check-in to the CheckIns DB
//        CheckInDbHelper checkInDbHelper = new CheckInDbHelper(this);
//        wdb = checkInDbHelper.getWritableDatabase();
//
//        // Create a new map of values, where column names are the keys
//        ContentValues values = new ContentValues();
//        values.put(CheckInContract.CheckIns.COLUMN_NAME_USAGE_TYPE_ID, usageType);
//        values.put(CheckInContract.CheckIns.COLUMN_NAME_DATE, dateString);
//        values.put(CheckInContract.CheckIns.COLUMN_NAME_AMOUNT, gals);
//
//        try{
//            // Insert the new row, returning the primary key value of the new row
//            long newRowId = wdb.insert(
//                    CheckInContract.CheckIns.TABLE_NAME,
//                    CheckInContract.CheckIns.COLUMN_NAME_AMOUNT,
//                    values);
//            System.out.println("Saved usage type " + usageType + " for " + gals + " on " + dateString);
//            wdb.close();
//        } catch (Exception e) {
//            System.out.println("An error occurred when trying to insert the entry in the database.");
//            if(wdb.isOpen()) {
//                wdb.close();
//            }
//            if(checkInDbHelper != null){
//                checkInDbHelper.close();
//            }
//        }
//    }


}
