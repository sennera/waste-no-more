package com.cookie_computing.wastenomore;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.cookie_computing.wastenomore.UsageTypeContract.UsageTypes;

import java.util.ArrayList;

/**
 * Created by April on 2/28/15.
 */
public class UsageTypeDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "WasteNoMore.db";

    // Queries
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String TEXT_TYPE = " TEXT";
    private static final String NOT_NULL = " NOT NULL";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UsageTypes.TABLE_NAME + " (" +
                    UsageTypes._ID + INTEGER_TYPE + " PRIMARY KEY," +
                    UsageTypes.COLUMN_NAME_USAGE_TYPE + TEXT_TYPE + NOT_NULL + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UsageTypes.TABLE_NAME;


    public UsageTypeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        System.out.println("OnCreateUsageTypes");
        db.execSQL(SQL_CREATE_ENTRIES);
        seedDb(db);
    }

    // **MAY WANT TO CHANGE LATER**
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
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
        //int i = 0;
        for (String type : types) {
//            String insert = "INSERT INTO " + UsageTypes.TABLE_NAME +
//                    " (" + UsageTypes._ID + ", " + UsageTypes.COLUMN_NAME_USAGE_TYPE +
//                    ") VALUES ('" + i + "','" + type + "') ";
            String insert = "INSERT INTO " + UsageTypes.TABLE_NAME +
                    " (" + UsageTypes.COLUMN_NAME_USAGE_TYPE +
                    ") VALUES ('" + type + "') ";
            db.execSQL(insert);
            System.out.println("seeded a field in DB");

            //i++;
        }

    }
}