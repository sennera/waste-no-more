package com.cookie_computing.wastenomore;

import android.provider.BaseColumns;

/**
 * This lays out the column names and table name for the Usage Types table.
 * Created by April on 2/28/15.
 */
public final class UsageTypeContract {
    // Empty constructor prevents someone from accidentally instantiating the contract class
    public UsageTypeContract() {}

    /* Inner class that defines the table contents */
    public static abstract class UsageTypes implements BaseColumns {
        public static final String TABLE_NAME = "usageTypes";
        public static final String COLUMN_NAME_USAGE_TYPE = "usageType";
    }
}
