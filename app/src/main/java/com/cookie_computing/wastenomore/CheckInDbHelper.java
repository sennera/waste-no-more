package com.cookie_computing.wastenomore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.cookie_computing.wastenomore.CheckInContract.CheckIns;

import java.util.ArrayList;

/**
 * Created by April on 2/28/15.
 */
public class CheckInDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "WasteNoMore.db";

    // Queries
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String DATETIME_TYPE = " DATETIME";
    private static final String NOT_NULL = " NOT NULL";
    private static final String DOUBLE_TYPE = " DOUBLE";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_CHECKINS =
            "CREATE TABLE " + CheckIns.TABLE_NAME + " (" +
                    CheckIns._ID + INTEGER_TYPE + " PRIMARY KEY," +
                    CheckIns.COLUMN_NAME_DATE + DATETIME_TYPE + NOT_NULL + COMMA_SEP +
                    CheckIns.COLUMN_NAME_USAGE_TYPE_ID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                    CheckIns.COLUMN_NAME_AMOUNT + DOUBLE_TYPE + " )";

    private static final String SQL_DELETE_CHECKINS =
            "DROP TABLE IF EXISTS " + CheckIns.TABLE_NAME;

    private static final String TEXT_TYPE = " TEXT";
    private static final String SQL_CREATE_USAGE_TYPES =
            "CREATE TABLE " + UsageTypeContract.UsageTypes.TABLE_NAME + " (" +
                    UsageTypeContract.UsageTypes._ID + INTEGER_TYPE + " PRIMARY KEY," +
                    UsageTypeContract.UsageTypes.COLUMN_NAME_USAGE_TYPE + TEXT_TYPE + NOT_NULL + " )";

    private static final String SQL_DELETE_USAGE_TYPES =
            "DROP TABLE IF EXISTS " + UsageTypeContract.UsageTypes.TABLE_NAME;


    //Indexes for the Usage types
    public static final int TRASH_ID = 1;
    public static final int WATER_ID = 2;
    //public static final int GAS_ID = 3;


    public CheckInDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CHECKINS);
        db.execSQL(SQL_CREATE_USAGE_TYPES);
        seedDb(db);
    }

    // **MAY WANT TO CHANGE LATER**
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_CHECKINS);
        db.execSQL(SQL_DELETE_USAGE_TYPES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void seedDb(SQLiteDatabase db) {
        ArrayList<String> types = new ArrayList<String>();
        types.add("Trash");
        types.add("Water");
        types.add("Gas");

        // This will be the primary key
        for (String type : types) {
            String insert = "INSERT INTO " + UsageTypeContract.UsageTypes.TABLE_NAME +
                    " (" + UsageTypeContract.UsageTypes.COLUMN_NAME_USAGE_TYPE +
                    ") VALUES ('" + type + "') ";
            db.execSQL(insert);
        }

    }
}
