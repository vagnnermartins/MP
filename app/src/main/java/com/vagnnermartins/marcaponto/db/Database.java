package com.vagnnermartins.marcaponto.db;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by vagnnermartins on 25/03/15.
 */
public class Database extends SQLiteAssetHelper {

    public static final String DATABASE_SPEC = "timesheet.spec";
    public static final String DATABASE_NAME = "timesheet.db";
    public static final int DATABASE_VERSION = 1;


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
