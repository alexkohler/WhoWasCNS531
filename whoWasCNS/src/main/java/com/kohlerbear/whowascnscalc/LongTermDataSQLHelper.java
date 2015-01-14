package com.kohlerbear.whowascnscalc;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by alex on 1/4/15.
 */
public class LongTermDataSQLHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Lifts.db";
    private static final int DATABASE_VERSION = 1;

    // Table name
    public static final String TABLE = "LongTermRecords";

    // Columns
    public static final String LIFTDATE = "liftDate";
    public static final String CYCLE = "Cycle";
    public static final String LIFT = "Lift";
    public static final String FREQUENCY = "Frequency";
    public static final String FIRST = "First_Lift";
    public static final String FIRSTREPS = "First_Lift_Reps";
    public static final String SECOND = "Second_Lift";
    public static final String SECONDREPS = "Second_Lift_Reps";
    public static final String THIRD = "Third_Lift";
    public static final String THIRDREPS = "Third_Lift_Reps";
    public static final String THEORETICAL_ONEREP = "THEORETICAL_ONEREP";

    public LongTermDataSQLHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE + "(liftDate text not null, Cycle text not null, Lift text not null, Frequency text not null, First_Lift real, First_Lift_Reps integer, Second_Lift real, Second_Lift_Reps integer, Third_Lift real, Third_Lift_reps integer, THEORETICAL_ONEREP real);";
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
        values.put(LongTermDataSQLHelper.LIFT, event.getLiftType());
        values.put(LongTermDataSQLHelper.FIRST, event.getFirstLift());
        values.put(LongTermDataSQLHelper.FIRSTREPS, event.getFirstLiftReps());
        values.put(LongTermDataSQLHelper.SECOND, event.getSecondLift());
        values.put(LongTermDataSQLHelper.SECONDREPS, event.getSecondLiftReps());
        values.put(LongTermDataSQLHelper.THIRD, event.getThirdLift());
        values.put(LongTermDataSQLHelper.THIRDREPS, event.getThirdLiftReps());
        values.put(LongTermDataSQLHelper.THEORETICAL_ONEREP, event.getTheoreticalOneRepMax());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(LongTermDataSQLHelper.TABLE, null, values);
    }

    private class LongTermEvent {
        private final String m_liftDate;
        private final String m_cycle;
        private final String m_liftType;
        private final String m_frequency;
        private final double m_firstLift;
        private final int m_firstLiftReps;
        private final double m_secondLift;
        private final int m_secondLiftReps;
        private final double m_thirdLift;
        private final int m_thirdLiftReps;
        private final double m_theoreticalOneRepMax;

        public double getThirdLift() {
            return m_thirdLift;
        }

        public double getSecondLift() {
            return m_secondLift;
        }
        public double getFirstLift() {
            return m_firstLift;
        }

        public String getFrequency() {
            return m_frequency;
        }

        public String getCycle() {
            return m_cycle;
        }

        public String getLiftDate() {
            return m_liftDate;
        }

        public String getLiftType() {
            return m_liftType;
        }

        public int getThirdLiftReps() {
            return m_thirdLiftReps;
        }

        public int getSecondLiftReps() {
            return m_secondLiftReps;
        }

        public int getFirstLiftReps() {
            return m_firstLiftReps;
        }

        public double getTheoreticalOneRepMax() {
            return m_theoreticalOneRepMax;
        }



        public LongTermEvent(String liftDate, String cycle, String liftType, String frequency, double firstLift, int firstLiftReps, double secondLift, int secondLiftReps, double thirdLift, int thirdLiftReps, double theoOneRepMax)
        {
            m_liftDate = liftDate;
            m_cycle = cycle;
            m_liftType = liftType;
            m_frequency = frequency;
            m_firstLift = firstLift;
            m_firstLiftReps = firstLiftReps;
            m_secondLift = secondLift;
            m_secondLiftReps = secondLiftReps;
            m_thirdLift = thirdLift;
            m_thirdLiftReps = thirdLiftReps;
            m_theoreticalOneRepMax = theoOneRepMax;
        }
    }
}
