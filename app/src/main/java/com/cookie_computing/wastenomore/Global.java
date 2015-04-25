package com.cookie_computing.wastenomore;

import android.app.Application;

/**
 * To hold global variables for the application.
 * Created by April on 4/24/15.
 */
public  class  Global extends Application {

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

    private double TYPICAL_FACE_WASH = 0.0;
    public double getTypicalFaceWash() {
        return TYPICAL_FACE_WASH;
    }
    public void setTypicalFaceWash(double TYPICAL_FACE_WASH) {
        this.TYPICAL_FACE_WASH = TYPICAL_FACE_WASH;
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
}
