package com.kohlerbear.whowascnscalc;



import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

/** Helper to the database, manages versions and creation */
public class EventsDataSQLHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Lifts.db";
    private static final int DATABASE_VERSION = 8;

    // Table name
    public static final String TABLE = "Lifts";

    // Columns
    public static final String LIFTDATE = "liftDate";
    public static final String CYCLE = "Cycle";
    public static final String LIFT = "Lift";
    public static final String FREQUENCY = "Frequency";
    public static final String FIRST = "First_Lift";
    public static final String SECOND = "Second_Lift";
    public static final String THIRD = "Third_Lift";
    public static final String TRAINING_MAX = "Training_Max";
    public static final String LBFLAG = "column_LbFlag";
    public static final String ROUNDFLAG = "RoundFlag";
    public static final String PATTERN = "Pattern";


    public EventsDataSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //this was originally alter table
        String sql = "create table " + TABLE + "(liftDate text not null, Cycle integer, Lift text not null, Frequency text not null, First_Lift real, Second_Lift real, Third_Lift real, Training_Max integer, column_LbFlag integer, RoundFlag integer, Pattern text);";
        Log.d("EventsData", "onCreate: " + sql);
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

    public void addEvent(ThirdScreenFragment thirdScreen) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        int sqlLitelbMode = 3; //booleans in sqllite are represented by 1 and 0
        if (thirdScreen.getModeFormat().contains("Lbs"))
            sqlLitelbMode = 1;
        if (thirdScreen.getModeFormat().contains("Kgs"))
            sqlLitelbMode = 0;


        values.put(EventsDataSQLHelper.LIFTDATE, thirdScreen.Processor.getDate());
        values.put(EventsDataSQLHelper.CYCLE, thirdScreen.Processor.getCycle());
        values.put(EventsDataSQLHelper.LIFT, thirdScreen.Processor.getLiftType());
        values.put(EventsDataSQLHelper.FREQUENCY, thirdScreen.Processor.getFreq());
        values.put(EventsDataSQLHelper.FIRST, thirdScreen.Processor.getFirstLift());
        values.put(EventsDataSQLHelper.SECOND, thirdScreen.Processor.getSecondLift());
        values.put(EventsDataSQLHelper.THIRD, thirdScreen.Processor.getThirdLift());
        values.put(EventsDataSQLHelper.PATTERN, thirdScreen.Processor.getPatternAcronym());
        values.put(EventsDataSQLHelper.ROUNDFLAG, thirdScreen.Processor.getRoundingFlag() ? 1 : 0);


        if ((thirdScreen.Processor.getLiftType().equals("Bench")) && thirdScreen.Processor.getCycle() == 1) //insert our initial training maxes into table instead of trying to pass them back and forth between intents
        {
            values.put(EventsDataSQLHelper.TRAINING_MAX, thirdScreen.Processor.getBenchTM());
            values.put(EventsDataSQLHelper.LBFLAG, sqlLitelbMode);//TODO move these outside the loop..it's okay to keep all these entries in the table
        }//(the first entry of each lift has a value in the "training_max" column for sake of easily generating title between changing views)

        if (thirdScreen.Processor.getLiftType().equals("Squat") && thirdScreen.Processor.getCycle() == 1) {
            values.put(EventsDataSQLHelper.TRAINING_MAX, thirdScreen.Processor.getSquatTM());
            values.put(EventsDataSQLHelper.LBFLAG, sqlLitelbMode);
        }
        if (thirdScreen.Processor.getLiftType().equals("OHP") && thirdScreen.Processor.getCycle() == 1) {
            values.put(EventsDataSQLHelper.TRAINING_MAX, thirdScreen.Processor.getOHPTM());
            values.put(EventsDataSQLHelper.LBFLAG, sqlLitelbMode);
        }
        if (thirdScreen.Processor.getLiftType().equals("Deadlift") && thirdScreen.Processor.getCycle() == 1) {
            values.put(EventsDataSQLHelper.TRAINING_MAX, thirdScreen.Processor.getDeadTM());
            values.put(EventsDataSQLHelper.LBFLAG, sqlLitelbMode);
        }
        db.insert(EventsDataSQLHelper.TABLE, null, values);
    }

    void createStickyRow(Context thirdScreen, TableRow tr, String cycle) {
        LayoutParams tvParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        tvParams.setMargins(1, 1, 1, 1);
        final int minHeight = 30;
        TextView stickyHeader = new TextView(thirdScreen);
        stickyHeader.setText("Cycle " + cycle);
        stickyHeader.setTextSize(17);
        stickyHeader.setLayoutParams(tvParams);
        stickyHeader.setBackgroundColor(Color.BLACK);
//        stickyHeader.setMinHeight(minHeight);//this will create space for rest of textviews in table
        stickyHeader.setMinWidth(45);
        stickyHeader.setGravity(Gravity.CENTER);

        tr.addView(stickyHeader);
    }

    void createRow(Context thirdScreen, TableRow tr, String liftDate, String cycle, String lift, String freq, String first, String second, String third) {
        LayoutParams tvParams = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        tvParams.setMargins(1, 1, 1, 1);
        final int minHeight = 30;
        TextView dateColumn = new TextView(thirdScreen);
        dateColumn.setText(liftDate.substring(0, 5));//xx-xx
        dateColumn.setTextSize(17);
        dateColumn.setLayoutParams(tvParams);
        dateColumn.setBackgroundColor(Color.BLACK);
        dateColumn.setMinHeight(minHeight);//this will create space for rest of textviews in table
        dateColumn.setGravity(Gravity.CENTER_HORIZONTAL);
        tr.addView(dateColumn);


        //cycle column creation
/*		TextView cycleColumn = new TextView(thirdScreen);
		cycleColumn.setText(cycle);
		cycleColumn.setLayoutParams(tvParams);
		cycleColumn.setBackgroundColor(Color.BLACK);
		cycleColumn.setMinHeight(minHeight);
		cycleColumn.setGravity(Gravity.CENTER_HORIZONTAL);
		tr.addView(cycleColumn);*/

        //lift column creation
        TextView liftColumn = new TextView(thirdScreen);
        liftColumn.setText(lift);
        liftColumn.setTextSize(17);
        liftColumn.setTextColor(Color.WHITE);
        liftColumn.setLayoutParams(tvParams);
        liftColumn.setBackgroundColor(Color.BLACK);
        liftColumn.setMinHeight(minHeight);
        liftColumn.setGravity(Gravity.CENTER_HORIZONTAL);
        tr.addView(liftColumn);

        //freq column creation
        TextView freqColumn = new TextView(thirdScreen);
        freqColumn.setText(freq);
        freqColumn.setTextSize(17);
        freqColumn.setLayoutParams(tvParams);
        freqColumn.setBackgroundColor(Color.BLACK);
        freqColumn.setMinHeight(minHeight);
        freqColumn.setGravity(Gravity.CENTER_HORIZONTAL);
        tr.addView(freqColumn);


        //first lift column creation
        TextView firstLiftColumn = new TextView(thirdScreen);
        firstLiftColumn.setText(first);
        firstLiftColumn.setTextSize(17);
        firstLiftColumn.setLayoutParams(tvParams);
        firstLiftColumn.setBackgroundColor(Color.BLACK);
        firstLiftColumn.setMinHeight(minHeight);
        firstLiftColumn.setGravity(Gravity.CENTER_HORIZONTAL);
        tr.addView(firstLiftColumn);

        //second lift column creation
        TextView secondLiftColumn = new TextView(thirdScreen);
        secondLiftColumn.setText(second);
        secondLiftColumn.setTextSize(17);
        secondLiftColumn.setLayoutParams(tvParams);
        secondLiftColumn.setBackgroundColor(Color.BLACK);
        secondLiftColumn.setMinHeight(minHeight);
        secondLiftColumn.setGravity(Gravity.CENTER_HORIZONTAL);
        tr.addView(secondLiftColumn);

        //third lift column creation
        TextView thirdLiftColumn = new TextView(thirdScreen);
        thirdLiftColumn.setText(third);
        thirdLiftColumn.setTextSize(17);
        thirdLiftColumn.setLayoutParams(tvParams);
        thirdLiftColumn.setBackgroundColor(Color.BLACK);
        thirdLiftColumn.setMinHeight(minHeight);
        thirdLiftColumn.setGravity(Gravity.CENTER_HORIZONTAL);
        tr.addView(thirdLiftColumn);
    }

    void inflateTable(ThirdScreenFragment thirdScreen, /*Intent intent,*/ String startingDate, SQLiteDatabase db) {
        db.beginTransaction();
        db.delete("Lifts", null, null);
        db.endTransaction();
//		Toast.makeText(thirdScreen, "DEBUG: displaying cycle 1 from inflateTable", Toast.LENGTH_SHORT).show();
        thirdScreen.setQuery(null);

//		String areWeGoingToRound = intent.getStringExtra("round");
//		if (areWeGoingToRound.equals("true"))	
//			thirdScreen.Processor.setRoundingFlag(true);

//		else //revert to the default of no round
//			thirdScreen.Processor.setRoundingFlag(false);


        //get unit mode
        thirdScreen.lbmode = TabPrototype.unitMode;//intent.getStringExtra("mode");
        if (thirdScreen.lbmode.length() > 1) {
            thirdScreen.setMode(thirdScreen.lbmode);

        }


        thirdScreen.Processor.setUnitMode(thirdScreen.lbmode);


        //set starting lifts (separate strings so title can them too)
        String startingBench = TabPrototype.benchTM;//intent.getStringExtra("bench");
        String startingSquat = TabPrototype.squatTM;//intent.getStringExtra("squat");
        String startingOHP = TabPrototype.ohpTM;//intent.getStringExtra("OHP");
        String startingDead = TabPrototype.deadTM;//intent.getStringExtra("dead");

        //also set starting lifts locally


        thirdScreen.Processor.setStartingLifts(startingBench, startingSquat, startingOHP, startingDead);
        thirdScreen.Processor.setStartingDate(startingDate);
        thirdScreen.Processor.setDate(startingDate);
        int numberCycles = Integer.parseInt(TabPrototype.numberOfCycles/*intent.getStringExtra("numberCycles")*/);
        thirdScreen.setNumberCycles(numberCycles);

    }

    void reinflateTable(ThirdScreenFragment thirdScreen/*, Intent intent*/) {
        String view = TabPrototype.viewMode;//intent.getStringExtra("viewMode");
        TableLayout tableRowPrincipal = (TableLayout) thirdScreen.drawerLayout.findViewById(R.id.tableLayout1Prototype);
        thirdScreen.setQuery("Frequency = '5-5-5'");
        switch (view) {
            case "DEFAULT":
                thirdScreen.setQuery(null);
                tableRowPrincipal.removeAllViews();
                thirdScreen.cursor = thirdScreen.getEvents();
                thirdScreen.showDefaultEvents(thirdScreen.cursor);
                break;
            case "BENCH":
                thirdScreen.setQuery("Lift = 'Bench'");
                tableRowPrincipal.removeAllViews();
                thirdScreen.cursor = thirdScreen.getEvents();
                  thirdScreen.insertStatus = false;
                thirdScreen.showDefaultEvents(thirdScreen.cursor);
                break;
            case "SQUAT":
                thirdScreen.setQuery("Lift = 'Squat'");
                tableRowPrincipal.removeAllViews();
                thirdScreen.cursor = thirdScreen.getEvents();
                thirdScreen.insertStatus = false;
                thirdScreen.showDefaultEvents(thirdScreen.cursor);
                break;
            case "OHP":
                thirdScreen.setQuery("Lift = 'OHP'");
                tableRowPrincipal.removeAllViews();
                thirdScreen.cursor = thirdScreen.getEvents();
                thirdScreen.insertStatus = false;
                thirdScreen.showDefaultEvents(thirdScreen.cursor);
                break;
            case "DEAD":
                thirdScreen.setQuery("Lift = 'Deadlift'");
                tableRowPrincipal.removeAllViews();
                thirdScreen.cursor = thirdScreen.getEvents();
                thirdScreen.insertStatus = false;
                thirdScreen.showDefaultEvents(thirdScreen.cursor);
                break;
            case "FIVES":
                thirdScreen.setQuery("Frequency = '5-5-5'");
                tableRowPrincipal.removeAllViews();
                thirdScreen.cursor = thirdScreen.getEvents();
                thirdScreen.insertStatus = false;
                thirdScreen.showDefaultEvents(thirdScreen.cursor);
                break;
            case "THREES":
                thirdScreen.setQuery("Frequency = '3-3-3'");
                tableRowPrincipal.removeAllViews();
                thirdScreen.cursor = thirdScreen.getEvents();
                thirdScreen.insertStatus = false;
                thirdScreen.showDefaultEvents(thirdScreen.cursor);
                break;
            case "ONES":
                thirdScreen.setQuery("Frequency = '5-3-1'");
                tableRowPrincipal.removeAllViews();
                thirdScreen.cursor = thirdScreen.getEvents();
                thirdScreen.insertStatus = false;
                thirdScreen.showDefaultEvents(thirdScreen.cursor);
                break;

        }
    }

}