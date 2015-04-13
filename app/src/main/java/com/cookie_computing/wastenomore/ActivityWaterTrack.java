package com.cookie_computing.wastenomore;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;


public class ActivityWaterTrack extends ActionBarActivity {

    final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_track);
        openChart();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_water_track, menu);
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

    private void openChart() {
        // Get data
        HashMap<Integer,Double> data = getUsageData();
        int count = data.size();
        int[] days = getAscendingDayNumbers(data);
        double[] amounts = getAmountsAscendingByDate(data);

        // Put data in the series
        XYSeries series = new XYSeries("Water Amount");
        for(int i = 0; i < count; i++){
            series.add(days[i], amounts[i]);
        }

        // Create a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series);

        // Creating XYSeriesRenderer to customize series
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setColor(Color.WHITE);
        renderer.setDisplayChartValues(true);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();

        multiRenderer.setChartTitle("Your Water Check-Ins");
        multiRenderer.setXTitle("Day");
        multiRenderer.setYTitle("Gallons");
        multiRenderer.setZoomButtonsVisible(true);
        double yMax = getMax(amounts);
        yMax = yMax + (0.1 * yMax);
        multiRenderer.setYAxisMax(yMax);
        multiRenderer.setYAxisMin(0);
        double xMax = getMax(days) + 0.5;
        multiRenderer.setXAxisMin(0.5);
        multiRenderer.setXAxisMax(xMax);
        multiRenderer.setXLabels((int) (xMax - 0.5));
        //Increase text size
        multiRenderer.setLabelsTextSize(22);
        multiRenderer.setAxisTitleTextSize(22);
        multiRenderer.setChartTitleTextSize(22);
        multiRenderer.setLegendTextSize(22);

        int width = 100;
        if (android.os.Build.VERSION.SDK_INT >= 13){
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            width = size.x;
        } else {
            Display display = getWindowManager().getDefaultDisplay();
            width = display.getWidth();  // deprecated
        }
        float barWidth = (float) (.8 * width) / getMax(days);
        multiRenderer.setBarWidth(barWidth);

        multiRenderer.addSeriesRenderer(renderer);

        // Getting a reference to RelativeLayout of the ActivityWaterTrack Layout
        RelativeLayout chartContainer = (RelativeLayout) findViewById(R.id.track_water_chart);

        final GraphicalView chart = ChartFactory.getBarChartView(getBaseContext(), dataset, multiRenderer, BarChart.Type.DEFAULT);
        multiRenderer.setClickEnabled(true);
        multiRenderer.setSelectableBuffer(50);

        // Setting a click event listener for the graph
        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeriesSelection seriesSelection = chart.getCurrentSeriesAndPoint();

                if (seriesSelection != null) {
                    String selectedSeries="Gallons of Water";
                    int clickedDay = (int) seriesSelection.getXValue(); // Getting the clicked Date ( x value )
                    int amount = (int) seriesSelection.getValue(); // Getting the y value

                    // Displaying Toast Message
                    Toast.makeText(
                            getBaseContext(),
                            selectedSeries + " for Day " + clickedDay + " : " + amount + " gals",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding the Line Chart to the Layout
        chartContainer.addView(chart);
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
        HashMap<Integer,Double> entriesMap = new HashMap<>();
        long minDate = getMinDate(c).getTime();
        c.moveToFirst();

        for (int i = 0; i < c.getCount(); i++) {
            double amount = c.getDouble(c.getColumnIndexOrThrow(CheckInContract.CheckIns.COLUMN_NAME_AMOUNT));
            String dateString = c.getString(c.getColumnIndexOrThrow(CheckInContract.CheckIns.COLUMN_NAME_DATE));
            final SimpleDateFormat parser = new SimpleDateFormat("ww yyyy-MM-dd HH:mm:ss.SSS");
            long date = 0;
            try {
                date = parser.parse(dateString).getTime();
                // the day number is one more than the number of days since the first check-in
                int dayNumber = getDaysBetween(minDate, date);
                entriesMap.put(dayNumber, amount);
                System.out.println("date: " + date + " day: " + dayNumber + " amount: " + amount);
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

    /*
    Gets the number of days included in the two timestamps (if there's 24 hours between,
    it spanned 2 days)
     */
    private int getDaysBetween(long minDate, long maxDate) {
        if (maxDate < minDate) {
            return -getDaysBetween(maxDate, minDate);
        }
        return (int) ((maxDate - minDate)/ DAY_IN_MILLIS) + 1;
    }

    /**
     * Gets the day numbers for the chart. The day number is the number of days since the user
     * started tracking their data in this usage type.
     *
     * @param map The map to read from, which includes the days and usage amounts
     * @return The array of day numbers
     */
    private int[] getAscendingDayNumbers(HashMap<Integer,Double> map) {
        // The entries need to be sorted by date in case they weren't in order in the cursor so they
        // will display correctly in the chart
        LinkedList<Integer> daysList = new LinkedList<>(map.keySet());
        Collections.sort(daysList);
        int[] days = new int[daysList.size()];

        for(int i = 0; i < days.length; i++){
            days[i] = daysList.get(i);
        }

        return days;
    }

    private int getMax(int[] arr) {
        int max = Integer.MIN_VALUE;
        for(int i = 0; i < arr.length; i++) {
            if(max < arr[i]) {
                max = arr[i];
            }
        }
        return max;
    }

    private double getMax(double[] arr) {
        double max = Double.MIN_VALUE;
        for(int i = 0; i < arr.length; i++) {
            if(max < arr[i]) {
                max = arr[i];
            }
        }
        return max;
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
