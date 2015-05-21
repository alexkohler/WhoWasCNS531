package com.kohlerbear.whowascnscalc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 1/4/15.
 */
public class LongTermDataSQLHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Lifts.db";
    private static final int DATABASE_VERSION = 9;

    // Table name
    public static final String TABLE = "LongTermRecords";

    // Columns
    public static final String LIFTDATE = "liftDate";
    public static final String CYCLE = "Cycle";
    public static final String LIFT_TYPE = "Lift_Type";
    public static final String LIFT_NAME = "Lift_Name";
    public static final String FREQUENCY = "Frequency";
    public static final String WEIGHT = "Weight";
    public static final String REPS = "Reps";
    public static final String THEORETICAL_ONEREP = "Theoretical_Onerep";
    public static final String LBS_FLAG= "Lb_Flag";

    public LongTermDataSQLHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE + "(liftDate text not null, Cycle text not null, Lift_Type text not null, Lift_name text not null, Frequency text not null, Weight real, Reps integer, Theoretical_Onerep real, Lb_Flag integer);";
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

    public void addEvent(LongTermEvent event)
    {
        ContentValues values = new ContentValues();

        values.put(LongTermDataSQLHelper.LIFTDATE, event.getLiftDate());
        values.put(LongTermDataSQLHelper.CYCLE, event.getCycle());
        values.put(LongTermDataSQLHelper.FREQUENCY, event.getFrequency());
        values.put(LongTermDataSQLHelper.LIFT_TYPE, event.getLiftType());
        values.put(LongTermDataSQLHelper.LIFT_NAME, event.getLiftName());
        values.put(LongTermDataSQLHelper.WEIGHT, event.getWeight());
        values.put(LongTermDataSQLHelper.REPS, event.getReps());
        values.put(LongTermDataSQLHelper.THEORETICAL_ONEREP, event.getTheoreticalOneRepMax());
        values.put(LongTermDataSQLHelper.LBS_FLAG, event.getLbsFlag());

        SQLiteDatabase db = getWritableDatabase();
        //If we have a compound
      //  if (event.getLiftName().matches("Squat|Deadlift|OHP|Bench")) {
            Cursor mCount= db.rawQuery("select count(*) from " + TABLE +  " where " + LIFTDATE + "='" + event.getLiftDate() + "' and " + LIFT_NAME + "='" + event.getLiftName() + "' and " + WEIGHT + "='" + event.getWeight() + "';", null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            if (count == 0)
                db.insert(LongTermDataSQLHelper.TABLE, null, values);
            else {
                String selection = LIFTDATE + "=? and " + LIFT_NAME + "=? and " + WEIGHT + "=?";
                db.update(TABLE, values, selection, new String[]{event.getLiftDate(), event.getLiftName(), String.valueOf(event.getWeight())});
            }
            mCount.close();
     //   }
   //     else {//otherwise we have an accessory
   //         System.out.println("accessory");
   //     }


        db.close();
    }
    /*
       public static final String LIFTDATE = "liftDate";
    public static final String CYCLE = "Cycle";
    public static final String LIFT_TYPE = "Lift_Type";
    public static final String LIFT_NAME = "Lift_Name";
    public static final String FREQUENCY = "Frequency";
    public static final String WEIGHT = "Weight";
    public static final String REPS = "Reps";
    public static final String THEORETICAL_ONEREP = "Theoretical_Onerep";
    public static final String LBS_FLAG= "Lb_Flag";
     */


    //"create table " + TABLE + "(liftDate text not null, Cycle text not null, Lift_Type text not null, Lift_name text not null, Frequency text not null, Weight real, Reps integer, Theoretical_Onerep real, Lb_Flag integer)
    public List<LongTermEvent> getProgressList(ThirdScreenFragment.CURRENT_VIEW view){ //this will give us individual entries grouped by liftDate (a single entry for each lift date)



        String whereClause = "";
        switch (view)
        {
            case DEFAULT:
                whereClause = "";
                break;
            case BENCH:
                whereClause = " WHERE " + LongTermDataSQLHelper.LIFT_TYPE + "=" +"'Bench'";
                break;
            case SQUAT:
                whereClause = " WHERE " + LongTermDataSQLHelper.LIFT_TYPE + "=" +"'Squat'";
                break;
            case OHP:
                whereClause = " WHERE " + LongTermDataSQLHelper.LIFT_TYPE + "=" +"'OHP'";
                break;
            case DEAD:
                whereClause = " WHERE " + LongTermDataSQLHelper.LIFT_TYPE + "=" +"'Deadlift'";
                break;
            case FIVES:
                whereClause = " WHERE " + LongTermDataSQLHelper.FREQUENCY + "=" + "'5-5-5'";
                break;
            case THREES:
                whereClause = " WHERE " + LongTermDataSQLHelper.FREQUENCY + "=" + "'3-3-3'";
                break;
            case ONES:
                whereClause = " WHERE " + LongTermDataSQLHelper.FREQUENCY + "=" + "'5-3-1'";
                break;


        }



        List<LongTermEvent> eventList = new ArrayList<LongTermEvent>();
        String selectQuery = "SELECT * FROM " + LongTermDataSQLHelper.TABLE + whereClause + " group by " + LongTermDataSQLHelper.LIFTDATE;//give us unique dates
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{
                LongTermEvent event = new LongTermEvent();
                event.setLiftDate(cursor.getString(0));
                event.setCycle(cursor.getString(1));
                event.setLiftType(cursor.getString(2));
                event.setLiftName(cursor.getString(3));
                event.setFrequency(cursor.getString(4));
                event.setWeight(cursor.getDouble(5));
                // event.setLbsFlag(cursor.getString(6));
                event.setReps(cursor.getInt(8));


                eventList.add(event);
            } while(cursor.moveToNext());
        }

        if (eventList.size() == 0) {
            LongTermEvent emptyEvent = new LongTermEvent();
            emptyEvent.setLiftDate("No previous workouts found!");
            emptyEvent.setCycle("");
            emptyEvent.setLiftType("");
            emptyEvent.setLiftName("");
            emptyEvent.setFrequency("");
            emptyEvent.setWeight(0);
            // event.setLbsFlag(cursor.getString(6));
            emptyEvent.setReps(0);
            eventList.add(emptyEvent);

        }

        return eventList;
    }


}

