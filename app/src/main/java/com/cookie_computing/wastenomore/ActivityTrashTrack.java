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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;

public class ActivityTrashTrack extends ActionBarActivity {

    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_trash);

        openChart();
//        //Get the data from the DB
//        CheckInDbHelper mDbHelper = new CheckInDbHelper(this);
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//
//        // Define a projection that specifies which columns from the database
//        // you will actually use after this query.
//        String[] projection = {
//                CheckInContract.CheckIns._ID,
//                CheckInContract.CheckIns.COLUMN_NAME_DATE,
//                CheckInContract.CheckIns.COLUMN_NAME_USAGE_TYPE_ID,
//                CheckInContract.CheckIns.COLUMN_NAME_AMOUNT};
//
//        // How the results should be sorted in the resulting Cursor
//        String[] selectionArgs = {"" + CheckInDbHelper.TRASH_ID};
//
//        Cursor c = db.query(
//                CheckInContract.CheckIns.TABLE_NAME,  // The table to query
//                projection,                               // The columns to return
//                CheckInContract.CheckIns.COLUMN_NAME_USAGE_TYPE_ID + "=?",                                // The columns for the WHERE clause
//                selectionArgs,                            // The values for the WHERE clause
//                null,                                     // don't group the rows
//                null,                                     // don't filter by row groups
//                null                                      // don't sort the rows
//        );
//
//        c.moveToFirst();
//        db.close();
//
//        while(!c.isAfterLast()){
//            data += c.getInt(
//                    c.getColumnIndexOrThrow(CheckInContract.CheckIns._ID)
//            ) + " ";
//            data += c.getString(
//                    c.getColumnIndexOrThrow(CheckInContract.CheckIns.COLUMN_NAME_DATE)
//            ) + " ";
//            data += c.getInt(
//                    c.getColumnIndexOrThrow(CheckInContract.CheckIns.COLUMN_NAME_USAGE_TYPE_ID)
//            ) + " ";
//            data += c.getDouble(
//                    c.getColumnIndexOrThrow(CheckInContract.CheckIns.COLUMN_NAME_AMOUNT)
//            ) + " ";
//            data += "\n";
//
//            c.moveToNext();
//        }
//
//        c.close();


//        final TextView textView = (TextView) findViewById(R.id.track_trash_text);
//        textView.setText(data);



        // BEGINNING OF COPIED CODE
//        public Intent execute(Context context) {
//            String[] titles = new String[] { "Sales growth January 1995 to December 2000" };
//            List<Date[]> dates = new ArrayList<Date[]>();
//            List<double[]> values = new ArrayList<double[]>();
//            Date[] dateValues = new Date[] { new Date(95, 0, 1), new Date(95, 3, 1), new Date(95, 6, 1),
//                    new Date(95, 9, 1), new Date(96, 0, 1), new Date(96, 3, 1), new Date(96, 6, 1),
//                    new Date(96, 9, 1), new Date(97, 0, 1), new Date(97, 3, 1), new Date(97, 6, 1),
//                    new Date(97, 9, 1), new Date(98, 0, 1), new Date(98, 3, 1), new Date(98, 6, 1),
//                    new Date(98, 9, 1), new Date(99, 0, 1), new Date(99, 3, 1), new Date(99, 6, 1),
//                    new Date(99, 9, 1), new Date(100, 0, 1), new Date(100, 3, 1), new Date(100, 6, 1),
//                    new Date(100, 9, 1), new Date(100, 11, 1) };
//            dates.add(dateValues);
//
//            values.add(new double[] { 4.9, 5.3, 3.2, 4.5, 6.5, 4.7, 5.8, 4.3, 4, 2.3, -0.5, -2.9, 3.2, 5.5,
//                    4.6, 9.4, 4.3, 1.2, 0, 0.4, 4.5, 3.4, 4.5, 4.3, 4 });
//            int[] colors = new int[] { Color.BLUE };
//            PointStyle[] styles = new PointStyle[] { PointStyle.POINT };
//            XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
//            setChartSettings(renderer, "Sales growth", "Date", "%", dateValues[0].getTime(),
//                    dateValues[dateValues.length - 1].getTime(), -4, 11, Color.GRAY, Color.LTGRAY);
//            renderer.setYLabels(10);
//            renderer.setXRoundedLabels(false);
//            XYSeriesRenderer xyRenderer = (XYSeriesRenderer) renderer.getSeriesRendererAt(0);
//            FillOutsideLine fill = new FillOutsideLine(FillOutsideLine.Type.BOUNDS_ABOVE);
//            fill.setColor(Color.GREEN);
//            xyRenderer.addFillOutsideLine(fill);
//            fill = new FillOutsideLine(FillOutsideLine.Type.BOUNDS_BELOW);
//            fill.setColor(Color.MAGENTA);
//            xyRenderer.addFillOutsideLine(fill);
//            fill = new FillOutsideLine(FillOutsideLine.Type.BOUNDS_ABOVE);
//            fill.setColor(Color.argb(255, 0, 200, 100));
//            fill.setFillRange(new int[] {10, 19});
//            xyRenderer.addFillOutsideLine(fill);
//
//            return ChartFactory.getTimeChartIntent(context, buildDateDataset(titles, dates, values),
//                    renderer, "MMM yyyy");
//        }
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


        // Put data in the series
        XYSeries series = new XYSeries("Trash Amount");
        for(int i = 0; i < count; i++){
            series.add(weeks[i], amounts[i]);
        }

        // Create a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series);

        // Creating XYSeriesRenderer to customize series
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setColor(Color.WHITE);
        //renderer.setPointStyle(PointStyle.CIRCLE);
        //renderer.setFillPoints(true);
        //renderer.setLineWidth(4);
        //renderer.setPointStrokeWidth(5);
        renderer.setDisplayChartValues(true);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();

        multiRenderer.setChartTitle("Your Trash Check-Ins");
        multiRenderer.setXTitle("Week");
        multiRenderer.setYTitle("Weight (lbs)");
        multiRenderer.setZoomButtonsVisible(true);
        double yMax = getMax(amounts);
        yMax = yMax + (0.1 * yMax);
        multiRenderer.setYAxisMax(yMax);
        multiRenderer.setYAxisMin(0);
        double xMax = getMax(weeks) + 0.5;
        multiRenderer.setXAxisMin(0.5);
        multiRenderer.setXAxisMax(xMax);
        multiRenderer.setXLabels((int) (xMax - 0.5));
        //Increase text size
        multiRenderer.setLabelsTextSize(22);
        multiRenderer.setAxisTitleTextSize(22);
        multiRenderer.setChartTitleTextSize(22);
        multiRenderer.setLegendTextSize(22);

        // Set the width of the bars
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
        float barWidth = (float) (.8 * width) / getMax(weeks);
        multiRenderer.setBarWidth(barWidth);

        multiRenderer.addSeriesRenderer(renderer);

        // Getting a reference to RelativeLayout of the ActivityTrashTrack Layout
        RelativeLayout chartContainer = (RelativeLayout) findViewById(R.id.track_trash_chart);

        final GraphicalView chart = ChartFactory.getBarChartView(getBaseContext(), dataset, multiRenderer, BarChart.Type.DEFAULT);
        multiRenderer.setClickEnabled(true);
        multiRenderer.setSelectableBuffer(50);

        // Setting a click event listener for the graph
        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeriesSelection seriesSelection = chart.getCurrentSeriesAndPoint();

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

        // With the results from the DB, fill a map with where the key is the date and the value is
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
        a = resetTime(a);
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

    /* Resets the time but not the date or weeks part of the Date */
    public static Date resetTime (Date d) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
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
