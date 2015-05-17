package com.cookie_computing.wastenomore.Water;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.cookie_computing.wastenomore.R;
import com.cookie_computing.wastenomore.db.CheckInContract;
import com.cookie_computing.wastenomore.db.CheckInDbHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

// An activity that will show stats for the user including their best recorded day, worst day,
// average usage, best weekly average, current weekly average, most recent usage, American average,
// among others


public class ActivityWaterStats extends Activity {

    ArrayList<HashMap<String,String>> stats = new ArrayList<>();

    static final int BEST_DAY = 0;
    static final int WORST_DAY = 1;
    static final int MOST_RECENT_DAY = 2;
    static final int DAILY_AVG = 3;
    static final int AMERICAN_AVG = 4;
    static final int WEEKLY_AVG = 5;
    static final int CURRENT_WEEK = 6;
    static final int TOTAL_CHECKINS = 7;
    static final int START_DATE = 8;
    static final int PERCENT_DAYS_CHECKED_IN = 9;
    static final int TOTAL_STATS = 10;

    static final String STAT = "Stat";
    static final String VALUE = "Value";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_stats);

        // Fills the ArrayList with the stats
        HashMap<Date,Double> data = getWaterData();
        String[] statNames = new String[TOTAL_STATS];
        String[] statValues = new String[TOTAL_STATS];

        statNames[BEST_DAY] = "Best Recorded Day";
        statNames[WORST_DAY] = "Worst Recorded Day";
        statNames[MOST_RECENT_DAY] = "Most Recent Day";
        statNames[DAILY_AVG] = "Average Daily Usage";
        statNames[AMERICAN_AVG] = "American Average";
        statNames[WEEKLY_AVG] = "Weekly Average";
        statNames[CURRENT_WEEK] = "Current Weekly Average";
        statNames[TOTAL_CHECKINS] = "Total Days You've Checked In";
        statNames[START_DATE] = "Date of First Water Check-In";
        statNames[PERCENT_DAYS_CHECKED_IN] = "% of Days You've Checked In";

        if(data.keySet().size() == 0){
            for(int i = 0; i < TOTAL_STATS; i++) {
                statValues[i] = " - ";
            }
        }
        else {
            final SimpleDateFormat parser = new SimpleDateFormat("MM/dd/yy");

            // BEST DAY
            double[] bestDay = getBestDay(data);
            String bestDate = parser.format(new Date((long) bestDay[0]));
            statValues[BEST_DAY] = (int) bestDay[1] + " gals on " + bestDate;
            // WORST DAY
            double[] worstDay = getWorstDay(data);
            String worstDate = parser.format(new Date((long) worstDay[0]));
            statValues[WORST_DAY] = (int) worstDay[1] + " gals on " + worstDate;
            // RECENT DAY
            double[] recentDay = getRecentDay(data);
            String recentDate = parser.format(new Date((long) recentDay[0]));
            statValues[MOST_RECENT_DAY] = (int) recentDay[1] + " gals on " + recentDate;

            // DAILY AVG
            statValues[DAILY_AVG] = ((int)(getDailyAvg(data)*100)) / 100.0 + " gals";
            // AMERICAN AVG
            statValues[AMERICAN_AVG] = ActivityWaterTrack.AVG_AMERICAN_INTAKE + " gals";

            // WEEKLY AVG
            statValues[WEEKLY_AVG] = ((int)(getWeeklyAvg(data)*100)) / 100.0 + " gals";
            // CURRENT WEEK
            statValues[CURRENT_WEEK] = ((int)(getCurrentWeek(data)*100)) / 100.0  + " gals";

            // TOTAL CHECK-INS
            statValues[TOTAL_CHECKINS] = data.size() + " check-ins";
            // START DATE
            statValues[START_DATE] = parser.format(new Date(getStartDate(data)));
            // PERCENT OF DAYS CHECKED IN
            statValues[PERCENT_DAYS_CHECKED_IN] = getPercentOfDaysCheckedIn(data) + "%";

        }

        for(int i = 0; i < TOTAL_STATS; i++){
            HashMap<String,String> datum = new HashMap<String, String>();
            datum.put(STAT, statNames[i]);
            datum.put(VALUE, statValues[i]);
            stats.add(datum);
        }


        final ListView listView = (ListView) findViewById(R.id.list);
        final SimpleAdapter adapter = new SimpleAdapter(this, stats,
                android.R.layout.simple_list_item_2, new String[] {STAT, VALUE},
                new int[] {android.R.id.text1, android.R.id.text2});

        listView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_water_stats, menu);
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

    // Gets all the water data from the database
    private HashMap<Date,Double> getWaterData() {
        //Get the data from the DB
        CheckInDbHelper mDbHelper = new CheckInDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                CheckInContract.CheckIns.COLUMN_NAME_DATE,
                CheckInContract.CheckIns.COLUMN_NAME_AMOUNT};

        String[] selectionArgs = {"" + CheckInDbHelper.WATER_ID};
        String sortBy = CheckInContract.CheckIns.COLUMN_NAME_DATE + " DESC";

        Cursor c = db.query(
                CheckInContract.CheckIns.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                CheckInContract.CheckIns.COLUMN_NAME_USAGE_TYPE_ID + "=?", // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortBy                                      // don't sort the rows
        );

        // With the results from the DB, fill a map with where the key is the date and the value is
        // the usage amount
        HashMap<Date,Double> entriesMap = new HashMap<>();
        c.moveToFirst();

        for (int i = 0; i < c.getCount(); i++) {
            double amount = c.getDouble(c.getColumnIndexOrThrow(CheckInContract.CheckIns.COLUMN_NAME_AMOUNT));
            String dateString = c.getString(c.getColumnIndexOrThrow(CheckInContract.CheckIns.COLUMN_NAME_DATE));
            final SimpleDateFormat parser = new SimpleDateFormat("ww yyyy-MM-dd HH:mm:ss.SSS");
            Date date;
            try {
                date = parser.parse(dateString);
                entriesMap.put(date, amount);
            } catch (Exception e) {
                e.printStackTrace();
            }
            c.moveToNext();
        }

        c.close();
        db.close();
        mDbHelper.close();
        return entriesMap;
    }

    // Returns an array that contains the date and amount for the day with the least amount of water
    // used. The first index has the timestamp of the best day and the second index is the number of
    // lowest recorded gallons.
    // If there was an error then [-1.0,Double.MAX_VALUE] is returned.
    private double[] getBestDay(HashMap<Date,Double> data) {
        double[] bestDay = new double[]{-1.0,Double.MAX_VALUE};
        LinkedList<Date> dates = new LinkedList<>(data.keySet());
        for(Date d : dates) {
            if(data.get(d) <= bestDay[1]) {
                bestDay[0] = (double) d.getTime();
                bestDay[1] = data.get(d);
            }
        }
        return bestDay;
    }

    // Returns an array that contains the date and amount for the day with the most amount of water
    // used. The first index has the timestamp of the worst day and the second index is the number of
    // highest recorded gallons.
    // If there was an error then [-1.0,Double.MAX_VALUE] is returned.
    private double[] getWorstDay(HashMap<Date,Double> data) {
        double[] worstDay = new double[]{-1.0,Double.MIN_VALUE};
        LinkedList<Date> dates = new LinkedList<>(data.keySet());
        for(Date d : dates) {
            if(data.get(d) >= worstDay[1]) {
                worstDay[0] = (double) d.getTime();
                worstDay[1] = data.get(d);
            }
        }
        return worstDay;
    }

    // Returns an array that contains the date and amount for the most recent day. The first index
    // has the timestamp of the most recent day and the second index is the number of gallons.
    // If there was an error then [-1.0,Double.MAX_VALUE] is returned.
    private double[] getRecentDay(HashMap<Date,Double> data) {
        double[] recentDay = new double[]{-1.0,Double.MIN_VALUE};
        LinkedList<Date> dates = new LinkedList<>(data.keySet());
        long recentDate = 0;
        for(Date d : dates) {
            if(d.getTime() > recentDate) {
                recentDate = d.getTime();
            }
        }
        if(recentDate != 0){
            recentDay[0] = (double) recentDate;
            recentDay[1] = data.get(new Date(recentDate));
        }
        return recentDay;
    }

    // Returns the daily average in gallons
    private double getDailyAvg(HashMap<Date,Double> data) {
        double totalGals = 0.0;
        LinkedList<Date> dates = new LinkedList<>(data.keySet());
        double totalDays = dates.size();
        for(Date d : dates) {
            totalGals += data.get(d);
        }
        return totalGals / totalDays;
    }

    // Returns the weekly average in gallons
    private double getWeeklyAvg(HashMap<Date,Double> data) {
        ArrayList<String> weeks = new ArrayList<>();
        HashMap<String, Double> weekAmount = new HashMap<>();
        HashMap<String, Integer> entriesPerWeek = new HashMap<>();
        for(Date d : data.keySet()) {
            // See if this week is in the map
            final SimpleDateFormat parser = new SimpleDateFormat("ww");
            String week = parser.format(d);
            if(weekAmount.containsKey(week)) {
                weekAmount.put(week, weekAmount.get(week) + data.get(d));
                entriesPerWeek.put(week, entriesPerWeek.get(week)+1);
            }
            else {
                weekAmount.put(week, data.get(d));
                entriesPerWeek.put(week, 1);
                weeks.add(week);
            }
        }

        double totalOfAvgs = 0.0;
        for(String week : weeks){
            totalOfAvgs += (weekAmount.get(week) / entriesPerWeek.get(week));
        }

        return totalOfAvgs / weeks.size();
    }

    // Returns the usage for this week so far (in gallons)
    private double getCurrentWeek(HashMap<Date,Double> data) {
        long today = System.currentTimeMillis();
        final SimpleDateFormat parser = new SimpleDateFormat("ww");
        String thisWeek = parser.format(today);

        double thisWeeksTotal = 0.0;
        double checkInsThisWeek = 0;
        for(Date d : data.keySet()) {
            String week = parser.format(d);
            if(week.equals(thisWeek)) {
                thisWeeksTotal += data.get(d);
                checkInsThisWeek++;
            }
        }

        return thisWeeksTotal / checkInsThisWeek;
    }

    // Returns the timestamp of the earliest check in date
    private long getStartDate(HashMap<Date,Double> data) {
        long date = System.currentTimeMillis();
        for(Date d : data.keySet()) {
            if(d.getTime() < date) {
                date = d.getTime();
            }
        }
        return date;
    }

    // Returns the percentage of days they've checked in since starting
    public double getPercentOfDaysCheckedIn(HashMap<Date,Double> data) {
        // Find the number of days they could have potentially checked in since starting
        long startDate = getStartDate(data);
        long today = System.currentTimeMillis();
        int msInDay = 1000 * 60 * 60 * 24;
        int possibleDays = (int) ((today - startDate) / msInDay) + 1;

        double checkins = data.size();
        return ((int) (checkins / possibleDays * 1000)) / 10;
    }

}
