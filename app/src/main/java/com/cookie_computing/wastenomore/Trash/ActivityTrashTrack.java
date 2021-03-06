package com.cookie_computing.wastenomore.Trash;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cookie_computing.wastenomore.Global;
import com.cookie_computing.wastenomore.db.CheckInContract;
import com.cookie_computing.wastenomore.db.CheckInDbHelper;
import com.cookie_computing.wastenomore.R;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.LineChart;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;

public class ActivityTrashTrack extends ActionBarActivity {

    private SQLiteDatabase db;
    static Global global;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_trash);
        global = ((Global)getApplicationContext());

        openChart();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_track_trash, menu);
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

    private void openChart() {
        //Get the data for the dates and amounts
        HashMap<Integer,Double> data = getUsageData();
        int count = data.size();
        int[] weeks = getAscendingWeekNumbers(data);
        double[] amounts = getAmountsAscendingByDate(data);


        final double AVG_AMERICAN_INTAKE = 35; // 5 lbs a day * 7 days
        int avgUserAmount = (int) global.getAverage(amounts);

        double xMin = 0.5;
        double xMax = global.getMax(weeks) + 0.5;


        // Put data in the series
        XYSeries series = new XYSeries("Trash Amount");
        for(int i = 0; i < count; i++){
            series.add(weeks[i], amounts[i]);
        }
        // Make two points so that a line of the average shows up
        XYSeries avgAmericanSeries = new XYSeries("American Average");
        avgAmericanSeries.add(xMin, AVG_AMERICAN_INTAKE);
        avgAmericanSeries.add(xMax, AVG_AMERICAN_INTAKE);
        // Make two points so that a line of their average shows up
        XYSeries avgUserSeries = new XYSeries("Your Average");
        avgUserSeries.add(xMin, avgUserAmount);
        avgUserSeries.add(xMax, avgUserAmount);

        // Create a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series);
        dataset.addSeries(avgAmericanSeries);
        dataset.addSeries(avgUserSeries);

        // Creating XYSeriesRenderer to customize series
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setColor(Color.WHITE);
        renderer.setDisplayChartValues(true);

        XYSeriesRenderer avgAmericanRenderer = new XYSeriesRenderer();
        avgAmericanRenderer.setColor(Color.RED);
        avgAmericanRenderer.setFillPoints(true);
        avgAmericanRenderer.setLineWidth(4);
        avgAmericanRenderer.setDisplayChartValues(false);

        XYSeriesRenderer avgUserRenderer = new XYSeriesRenderer();
        avgUserRenderer.setColor(Color.GREEN);
        avgUserRenderer.setFillPoints(true);
        avgUserRenderer.setLineWidth(4);
        avgUserRenderer.setDisplayChartValues(false);


        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();

        multiRenderer.setChartTitle("");
        multiRenderer.setXTitle("Week");
        multiRenderer.setYTitle("Weight (lbs)");
        double yMax = global.getMax(amounts);
        yMax = yMax + (0.1 * yMax);
        multiRenderer.setYAxisMax(yMax);
        multiRenderer.setYAxisMin(0);
        multiRenderer.setXAxisMin(xMin);
        multiRenderer.setXAxisMax(xMax);
        multiRenderer.setXLabels((int) (xMax - 0.5));

        // setting legend to fit the screen size
        multiRenderer.setFitLegend(true);
        multiRenderer.setBackgroundColor(Color.TRANSPARENT);
        multiRenderer.setApplyBackgroundColor(true);

        // setting the margin size for the graph in the order top, left, bottom, right
        multiRenderer.setMargins(new int[] { 0, 20, 40, 0 });

        //Increase text size
        multiRenderer.setLabelsTextSize(22);
        multiRenderer.setAxisTitleTextSize(22);
        multiRenderer.setChartTitleTextSize(22);
        multiRenderer.setLegendTextSize(22);

        // Set the width of the bars
        float barSpacing = (float) 0.5;
        multiRenderer.setBarSpacing(barSpacing);

        multiRenderer.addSeriesRenderer(renderer);
        multiRenderer.addSeriesRenderer(avgAmericanRenderer);
        multiRenderer.addSeriesRenderer(avgUserRenderer);

        // Creating a combined chart with the chart types specified in types array
        String[] types = new String[] { BarChart.TYPE, LineChart.TYPE, LineChart.TYPE };
        final GraphicalView mChart;
        mChart = ChartFactory.getCombinedXYChartView(getBaseContext(), dataset, multiRenderer, types);

        // Getting a reference to RelativeLayout of the ActivityTrashTrack Layout
        RelativeLayout chartContainer = (RelativeLayout) findViewById(R.id.track_trash_chart);

        multiRenderer.setClickEnabled(true);
        multiRenderer.setSelectableBuffer(50);

        // Setting a click event listener for the graph
        mChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeriesSelection seriesSelection = mChart.getCurrentSeriesAndPoint();

                if (seriesSelection != null) {
                    String selectedSeries="Weight of Trash";
                    int clickedWeek = (int) seriesSelection.getXValue(); // Getting the clicked Date ( x value )
                    int amount = (int) seriesSelection.getValue(); // Getting the y value

                    // Displaying Toast Message
                    Toast.makeText(
                            getBaseContext(),
                            selectedSeries + " for Week " + clickedWeek + " : " + amount + " lbs",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding the Line Chart to the Layout
        chartContainer.addView(mChart);
    }

    private HashMap<Integer,Double> getUsageData() {
        //Get the data from the DB
        CheckInDbHelper mDbHelper = new CheckInDbHelper(this);
        db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                CheckInContract.CheckIns.COLUMN_NAME_DATE,
                CheckInContract.CheckIns.COLUMN_NAME_AMOUNT};

        String[] selectionArgs = {"" + CheckInDbHelper.TRASH_ID};
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

        // With the results from the DB, fill a map where the key is the date and the value is
        // the usage amount
        HashMap<Integer,Double> entriesMap = new HashMap<>();
        Date minDate = getMinDate(c);
        c.moveToFirst();

        for (int i = 0; i < c.getCount(); i++) {
            double amount = c.getDouble(c.getColumnIndexOrThrow(CheckInContract.CheckIns.COLUMN_NAME_AMOUNT));
            String dateString = c.getString(c.getColumnIndexOrThrow(CheckInContract.CheckIns.COLUMN_NAME_DATE));
            final SimpleDateFormat parser = new SimpleDateFormat("ww yyyy-MM-dd HH:mm:ss.SSS");
            Date date;
            try {
                date = parser.parse(dateString);
                // the day number is one more than the number of days since the first check-in
                int weekNumber = getWeeksBetween(minDate, date);
                entriesMap.put(weekNumber, amount);
                System.out.println("date: " + date + " week: " + weekNumber + " amount: " + amount);
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

    private Date getMinDate(Cursor c) {
        // the check in time couldn't have been later than the current time
        Date minDate = new Date(System.currentTimeMillis());
        c.moveToFirst();

        for (int i = 0; i < c.getCount(); i++) {
            String dateString = c.getString(c.getColumnIndexOrThrow(CheckInContract.CheckIns.COLUMN_NAME_DATE));
            final SimpleDateFormat parser = new SimpleDateFormat("ww yyyy-MM-dd HH:mm:ss.SSS");
            Date date = new Date();
            try {
                date = parser.parse(dateString);
                //long thisDate = date.getTime();
                if (date.before(minDate)) {
                    minDate = date;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            c.moveToNext();
        }
        return minDate;
    }

    /**
     * Gets the week numbers for the chart. The week number is the number of weeks since the user
     * started tracking their data in this usage type.
     *
     * @param map The map to read from, which includes the weeks and usage amounts
     * @return The array of week numbers
     */
    private int[] getAscendingWeekNumbers(HashMap<Integer,Double> map) {
        // The entries need to be sorted by date in case they weren't in order in the cursor so they
        // will display correctly in the chart
        LinkedList<Integer> weeksList = new LinkedList<>(map.keySet());
        Collections.sort(weeksList);
        int[] weeks = new int[weeksList.size()];

        for(int i = 0; i < weeks.length; i++){
            weeks[i] = weeksList.get(i);
        }

        return weeks;
    }

    public static int getWeeksBetween (Date a, Date b) {

        if (b.before(a)) {
            return -getWeeksBetween(b, a);
        }

        // Back up the time for a until the beginning of that week, down to the very second.
        a = global.resetTime(a);
        Calendar cal = new GregorianCalendar();
        cal.setTime(a);
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            cal.add(Calendar.DATE, -1);
        }

        int weeks = 0;
        while (cal.getTime().before(b)) {
            // add another week
            cal.add(Calendar.WEEK_OF_YEAR, 1);
            weeks++;
        }
        return weeks;
    }

    private double[] getAmountsAscendingByDate(HashMap<Integer,Double> map) {
        // Sort the dates
        LinkedList<Integer> daysList = new LinkedList<>(map.keySet());
        Collections.sort(daysList);
        double[] amounts = new double[daysList.size()];

        // Finds the amount for the dates, inserting them into the array ordered by date
        for(int i = 0; i < amounts.length; i++){
            amounts[i] = map.get(daysList.get(i));
        }

        return amounts;
    }

}
