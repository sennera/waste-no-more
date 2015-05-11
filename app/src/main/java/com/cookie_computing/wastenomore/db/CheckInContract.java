package com.cookie_computing.wastenomore.db;

import android.provider.BaseColumns;

/**
 * This lays out the column names and table name for the Check Ins table.
 * Created by April on 2/28/15.
 */
public final class CheckInContract {
    // Empty constructor prevents someone from accidentally instantiating the contract class
    public CheckInContract() {}

    /* Inner class that defines the table contents */
    public static abstract class CheckIns implements BaseColumns {
        public static final String TABLE_NAME = "checkIns";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_USAGE_TYPE_ID = "usageTypeID";
        public static final String COLUMN_NAME_AMOUNT = "amount";
    }
}
