package com.cookie_computing.wastenomore;

import android.app.Application;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * To hold global variables for the application.
 * Created by April on 4/24/15.
 */
public class Global extends Application {

    // wash face
    // other
    // water conserving toilet
    private double TYPICAL_SHOWER = 0.0;
    public double getTypicalShower() {
        return TYPICAL_SHOWER;
    }
    public void setTypicalShower(double TYPICAL_SHOWER) {
        this.TYPICAL_SHOWER = TYPICAL_SHOWER;
    }

    private double TYPICAL_HAND_DISHES = 0.0;
    public double getTypicalHandDishes() {
        return TYPICAL_HAND_DISHES;
    }
    public void setTypicalHandDishes(double TYPICAL_HAND_DISHES) {
        this.TYPICAL_HAND_DISHES = TYPICAL_HAND_DISHES;
    }

    private double TYPICAL_DISHWASHER = 0.0;
    public double getTypicalDishwasher() {
        return TYPICAL_DISHWASHER;
    }
    public void setTypicalDishwasher(double TYPICAL_DISHWASHER) {
        this.TYPICAL_DISHWASHER = TYPICAL_DISHWASHER;
    }

    private double TYPICAL_BRUSH_TEETH = 0.0;
    public double getTypicalBrushTeeth() {
        return TYPICAL_BRUSH_TEETH;
    }
    public void setTypicalBrushTeeth(double TYPICAL_BRUSH_TEETH) {
        this.TYPICAL_BRUSH_TEETH = TYPICAL_BRUSH_TEETH;
    }

    private double TYPICAL_OTHER_WATER = 0.0;
    public double getTypicalOtherWater() {
        return TYPICAL_OTHER_WATER;
    }
    public void setTypicalOtherWater(double TYPICAL_OTHER_WATER) {
        this.TYPICAL_OTHER_WATER = TYPICAL_OTHER_WATER;
    }

    private boolean TYPICAL_CONSERVING_TOILET = false;
    public boolean getTypicalConservingToilet() {
        return TYPICAL_CONSERVING_TOILET;
    }
    public void setTypicalConservingToilet(boolean TYPICAL_CONSERVING_TOILET) {
        this.TYPICAL_CONSERVING_TOILET = TYPICAL_CONSERVING_TOILET;
    }

    private double TYPICAL_GAS_GAL = 0.0;
    public double getTypicalGasGallons() {
        return TYPICAL_GAS_GAL;
    }
    public void setTypicalGasGallons(double TYPICAL_GAS_GAL) {
        this.TYPICAL_GAS_GAL = TYPICAL_GAS_GAL;
    }

    private double TYPICAL_GAS_MILES = 0.0;
    public double getTypicalGasMiles() {
        return TYPICAL_GAS_MILES;
    }
    public void setTypicalGasMiles(double TYPICAL_GAS_MILES) {
        this.TYPICAL_GAS_MILES = TYPICAL_GAS_MILES;
    }

    // It will be the mean of car mileage until they have saved a fuel up check-in
    //Value from data in MTH 366 data from .gov website
    // The total miles and total gallons will be used to find an average mpg
    private double TOTAL_MILES = 0.0;
    public double getTotalMiles() {
        return TOTAL_MILES;
    }
    public void setTotalMiles(double TOTAL_MILES) {
        this.TOTAL_MILES = TOTAL_MILES;
    }

    private double TOTAL_GALS = 0.0;
    public double getTotalGals() {
        return TOTAL_GALS;
    }
    public void setTotalGals(double TOTAL_GALS) {
        this.TOTAL_GALS = TOTAL_GALS;
    }


    /* Get the current date and put it in correct format so it can be put in the map */
    public static String getCurrentDate() {
        final SimpleDateFormat parser = new SimpleDateFormat("ww yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date(System.currentTimeMillis()); //gets the current date
        return parser.format(date);
    }

    // Helper method to get the number from a string. Always gives positive:
    // If input is negative, this returns 0.
    public static double getPositiveNumFromString(String string){
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

    public static int getMax(int[] arr) {
        int max = Integer.MIN_VALUE;
        for(int i = 0; i < arr.length; i++) {
            if(max < arr[i]) {
                max = arr[i];
            }
        }
        return max;
    }

    public static double getMax(double[] arr) {
        double max = Double.MIN_VALUE;
        for(int i = 0; i < arr.length; i++) {
            if(max < arr[i]) {
                max = arr[i];
            }
        }
        return max;
    }

    // Calculate the average for an array of doubles
    public static double getAverage(double[] numbers) {
        double sum = 0;

        for(int i=0; i < numbers.length ; i++) {
            sum += numbers[i];
        }

        //calculate average value
        return sum / numbers.length;
    }

    /* Resets the time but not the date or weeks part of the Date */
    public static java.util.Date resetTime (java.util.Date d) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
