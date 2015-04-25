package com.cookie_computing.wastenomore;

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

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.LineChart;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;


public class ActivityGasTrack extends ActionBarActivity {

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_track);
        openChart();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_gas_track, menu);
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
        //Get the data for the dates and amounts
        HashMap<Date,Double> data = getUsageData();
        int count = data.size();
        Date[] days = getAscendingDates(data);
        double[] amounts = getAmountsAscendingByDate(data);

        Date xMin = getMin(days);
        Date xMax = getMax(days);

        int avgUserAmount = (int) getAverage(amounts);


        // Put data in the series
        TimeSeries series = new TimeSeries("Gas Mileage");
        for(int i = 0; i < count; i++){
            series.add(days[i], amounts[i]);
        }

        // Make two points so that a line of their average shows up
        TimeSeries avgUserSeries = new TimeSeries("Your Average");
        avgUserSeries.add(xMin, avgUserAmount);
        avgUserSeries.add(xMax, avgUserAmount);

        // Create a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series);
        dataset.addSeries(avgUserSeries);

        // Creating XYSeriesRenderer to customize series
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setColor(Color.WHITE);
        renderer.setDisplayChartValues(true);

        XYSeriesRenderer avgUserRenderer = new XYSeriesRenderer();
        avgUserRenderer.setColor(Color.GREEN);
        avgUserRenderer.setFillPoints(true);
        avgUserRenderer.setLineWidth(4);
        avgUserRenderer.setDisplayChartValues(false);


        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();

        multiRenderer.setChartTitle("Your Gasoline Check-Ins");
        multiRenderer.setXTitle("Date");
        multiRenderer.setYTitle("Mileage (miles per gallon)");
        double yMax = getMax(amounts);
        yMax = yMax + (0.1 * yMax);
        multiRenderer.setYAxisMax(yMax);
        multiRenderer.setYAxisMin(0);
//        multiRenderer.setXAxisMin(xMin);
//        multiRenderer.setXAxisMax(xMax);
//        multiRenderer.setXLabels((int) (xMax - 0.5));

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
        multiRenderer.addSeriesRenderer(avgUserRenderer);

        // Creating a combined chart with the chart types specified in types array
        String[] types = new String[] { BarChart.TYPE, LineChart.TYPE };
        final GraphicalView mChart;
        mChart = ChartFactory.getCombinedXYChartView(getBaseContext(), dataset, multiRenderer, types);

        // Getting a reference to RelativeLayout of the ActivityTrashTrack Layout
        RelativeLayout chartContainer = (RelativeLayout) findViewById(R.id.track_gas_chart);

        multiRenderer.setClickEnabled(true);
        multiRenderer.setSelectableBuffer(50);

        // Setting a click event listener for the graph
        mChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeriesSelection seriesSelection = mChart.getCurrentSeriesAndPoint();

                if (seriesSelection != null) {
                    String selectedSeries="Mileage";
                    int clickedDate = (int) seriesSelection.getXValue(); // Getting the clicked Date ( x value )
                    int amount = (int) seriesSelection.getValue(); // Getting the y value

                    // Displaying Toast Message
                    Toast.makeText(
                            getBaseContext(),
                            selectedSeries + " for " + clickedDate + " : " + amount + " miles per gallon",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding the Line Chart to the Layout
        chartContainer.addView(mChart);
    }

    private HashMap<Date,Double> getUsageData() {
        //Get the data from the DB
        CheckInDbHelper mDbHelper = new CheckInDbHelper(this);
        db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                CheckInContract.CheckIns.COLUMN_NAME_DATE,
                CheckInContract.CheckIns.COLUMN_NAME_AMOUNT};

        String[] selectionArgs = {"" + CheckInDbHelper.GAS_MILEAGE_ID};
        String sortBy = CheckInContract.CheckIns.COLUMN_NAME_DATE + " ASC";

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
        Date minDate = getMinDate(c);
        c.moveToFirst();

        for (int i = 0; i < c.getCount(); i++) {
            double amount = c.getDouble(c.getColumnIndexOrThrow(CheckInContract.CheckIns.COLUMN_NAME_AMOUNT));
            String dateString = c.getString(c.getColumnIndexOrThrow(CheckInContract.CheckIns.COLUMN_NAME_DATE));
            final SimpleDateFormat parser = new SimpleDateFormat("ww yyyy-MM-dd HH:mm:ss.SSS");
            Date date;
            try {
                date = parser.parse(dateString);
                entriesMap.put(date, amount);
                System.out.println("date: " + date + " amount: " + amount);
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
    private Date[] getAscendingDates(HashMap<Date,Double> map) {
        // The entries need to be sorted by date in case they weren't in order in the cursor so they
        // will display correctly in the chart
        LinkedList<Date> weeksList = new LinkedList<>(map.keySet());
        Collections.sort(weeksList);
        Date[] weeks = new Date[weeksList.size()];

        for(int i = 0; i < weeks.length; i++){
            weeks[i] = weeksList.get(i);
        }

        return weeks;
    }

    private Date getMax(Date[] arr) {
        Date max = new Date(0);
        for(int i = 0; i < arr.length; i++) {
            if(max.before(arr[i])) {
                max = arr[i];
            }
        }
        return max;
    }

    private Date getMin(Date[] arr) {
        Date min = new Date(System.currentTimeMillis());
        for(int i = 0; i < arr.length; i++) {
            if(min.after(arr[i])) {
                min = arr[i];
            }
        }
        return min;
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

    // Calculate the average for an array of doubles
    private double getAverage(double[] numbers) {
        double sum = 0;

        for(int i=0; i < numbers.length ; i++) {
            sum += numbers[i];
        }

        //calculate average value
        return sum / numbers.length;
    }

    private double[] getAmountsAscendingByDate(HashMap<Date,Double> map) {
        // Sort the dates
        LinkedList<Date> daysList = new LinkedList<>(map.keySet());
        Collections.sort(daysList);
        double[] amounts = new double[daysList.size()];

        // Finds the amount for the dates, inserting them into the array ordered by date
        for(int i = 0; i < amounts.length; i++){
            amounts[i] = map.get(daysList.get(i));
        }

        return amounts;
    }

}


