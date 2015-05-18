package com.kohlerbear.whowascnscalc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

/**
 * For storing accessory templates
 */
public class AccessoryLiftSQLHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Lifts.db";
    private static final int DATABASE_VERSION = 9;

    // Table name
    public static final String TABLE = "AccessoryTemplates";

    // Columns
    public static final String ACCESSORY = "ACCESSORY";
    public static final String ACCESSORY_DAY = "ACCESSORY_TYPE";
    public static final String LIFT_ORDER = "LIFT_ORDER";


    public AccessoryLiftSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE + " (ACCESSORY text not null, ACCESSORY_TYPE text not null, LIFT_ORDER integer);";
//        Log.d("EventsData", "onCreate: " + sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion)
            return;

        String sql = null;
        if (oldVersion == 1)
            sql = "alter table " + TABLE + " add note text;";
        if (oldVersion == 2)
            sql = "";

        Log.d("EventsData", "onUpgrade	: " + sql);
        if (sql != null)
            db.execSQL(sql);
    }

    public enum ACCESSORY_TYPE {BENCH, SQUAT, OHP, DEADLIFT};

    public void addAccessoryLift(String accessoryLift, ACCESSORY_TYPE type, int order) {
        ContentValues values = new ContentValues();

        values.put(AccessoryLiftSQLHelper.ACCESSORY, accessoryLift);
        values.put(AccessoryLiftSQLHelper.ACCESSORY_DAY, type.name().toUpperCase());
        values.put(AccessoryLiftSQLHelper.LIFT_ORDER, order);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(AccessoryLiftSQLHelper.TABLE, null, values);
    }

    public void repopulateDB(ArrayList<String> values, ACCESSORY_TYPE accessoryType)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + AccessoryLiftSQLHelper.TABLE + " where " + AccessoryLiftSQLHelper.ACCESSORY_DAY + " ='" + accessoryType + "'");
//        db.execSQL("Drop table AccessoryTemplates");
//        db.execSQL("create table " + AccessoryLiftSQLHelper.TABLE + "(ACCESSORY text not null, ACCESSORY_TYPE text not null, LIFT_ORDER integer);");
        ContentValues contentValues = new ContentValues();
        int orderCounter = 0;
        for (String liftValue : values)
        {
            addAccessoryLift(liftValue, accessoryType, orderCounter);
            orderCounter++;
        }
    }

    public void updateEntry(String oldEntry, String newEntry, ACCESSORY_TYPE type)
    {

        Cursor c = getWritableDatabase().query(AccessoryLiftSQLHelper.TABLE, new String[]{"rowid _id", AccessoryLiftSQLHelper.ACCESSORY}, "ACCESSORY_TYPE = '" + type.name().toUpperCase() + "' ORDER BY LIFT_ORDER ASC", null, null,
                null, null);//TODO type query
   //     String debugString = DatabaseUtils.dumpCursorToString(c);
        String updateSQL = "UPDATE " + TABLE + " SET " +
                ACCESSORY + "= '" + newEntry + "' " +
                "WHERE " + ACCESSORY + "= '" + oldEntry + "' " +
                "AND " + ACCESSORY_DAY + "= '" + type.name().toUpperCase() + "'";

        getWritableDatabase().execSQL(updateSQL);
        c = getWritableDatabase().query(AccessoryLiftSQLHelper.TABLE, new String[]{"rowid _id", AccessoryLiftSQLHelper.ACCESSORY}, "ACCESSORY_TYPE = '" + type.name().toUpperCase() + "' ORDER BY LIFT_ORDER ASC", null, null,
                null, null);//TODO type query
   //     debugString = DatabaseUtils.dumpCursorToString(c);
        System.out.println();

    }


    public ArrayList<String> getAccessoriesFor( ACCESSORY_TYPE accessoryType)
    {
        ArrayList<String> accessoryValues = new ArrayList<String>();

        Cursor c = getWritableDatabase().query(AccessoryLiftSQLHelper.TABLE, new String[]{"rowid _id", AccessoryLiftSQLHelper.ACCESSORY}, "ACCESSORY_TYPE = '" + accessoryType.name().toUpperCase() + "' ORDER BY LIFT_ORDER ASC", null, null, null, null);
        while (c.moveToNext()) {
            accessoryValues.add(c.getString(1));
        }

        return accessoryValues;
    }

    public void makeSampleData()
    {
        addAccessoryLift("Dick pushups", ACCESSORY_TYPE.BENCH, 0);
        addAccessoryLift("Quarter squats", ACCESSORY_TYPE.BENCH, 1);
        addAccessoryLift("Hot pocket eating", ACCESSORY_TYPE.BENCH, 3);
        addAccessoryLift("Crossfit", ACCESSORY_TYPE.BENCH, 4);
        addAccessoryLift("Fike press", ACCESSORY_TYPE.BENCH, 5);
        addAccessoryLift("Donut eating", ACCESSORY_TYPE.BENCH, 6);
        addAccessoryLift("Matthew mconnahay push up", ACCESSORY_TYPE.BENCH, 7);
        addAccessoryLift("bench press with no clothes", ACCESSORY_TYPE.BENCH, 8);
    }



}
