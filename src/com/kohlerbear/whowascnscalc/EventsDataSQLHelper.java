package com.kohlerbear.whowascnscalc;



import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

/** Helper to the database, manages versions and creation */
public class EventsDataSQLHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "Lifts.db";
	private static final int DATABASE_VERSION = 8;

	// Table name
	public static final String TABLE = "Lifts";

	// Columns
	public static final String LIFTDATE = "liftDate"; //PREVIOUSLY TIME
	public static final String CYCLE = "Cycle"; //PREVIOUSLY TITLE 
	public static final String LIFT = "Lift";
	public static final String FREQUENCY = "Frequency";
	public static final String FIRST = "First_Lift";
	public static final String SECOND = "Second_Lift";
	public static final String THIRD = "Third_Lift";
	public static final String TRAINING_MAX = "Training_Max";
	public static final String LBFLAG = "column_lbFlag";
	public EventsDataSQLHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//this was originally alter table
		String sql = "create table " + TABLE + "(liftDate text not null, Cycle integer, Lift text not null, Frequency text not null, First_Lift real, Second_Lift real, Third_Lift real, Training_Max integer, column_lbFlag integer);";
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

	public void addEvent(ThirdScreenActivity thirdScreen) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		//db.execSQL("ALTER TABLE Lifts ADD COLUMN column_lbFlag integer");
		int sqlLitelbMode = 3; //booleans in sqllite are represented by 1 and 0
		if (thirdScreen.getModeFormat().contains("Lbs"))
		sqlLitelbMode = 1;
		if (thirdScreen.getModeFormat().contains("Kgs")) 
		sqlLitelbMode = 0;
		
		
		
			
		
	
		values.put(EventsDataSQLHelper.LIFTDATE, thirdScreen.Processor.getDate() );
		values.put(EventsDataSQLHelper.CYCLE, thirdScreen.Processor.getCycle());
		values.put(EventsDataSQLHelper.LIFT, thirdScreen.Processor.getLiftType());
		values.put(EventsDataSQLHelper.FREQUENCY, thirdScreen.Processor.getFreq());
		values.put(EventsDataSQLHelper.FIRST, thirdScreen.Processor.getFirstLift());
		values.put(EventsDataSQLHelper.SECOND, thirdScreen.Processor.getSecondLift());
		values.put(EventsDataSQLHelper.THIRD, thirdScreen.Processor.getThirdLift());
		if ((thirdScreen.Processor.getLiftType().equals("Bench")) && thirdScreen.Processor.getCycle() == 1) //insert our initial training maxes into table instead of trying to pass them back and forth between intents 
			{
			values.put(EventsDataSQLHelper.TRAINING_MAX, thirdScreen.Processor.getBenchTM());
			values.put(EventsDataSQLHelper.LBFLAG, sqlLitelbMode);
			}//(the first entry of each lift has a value in the "training_max" column for sake of easily generating title between changing views)
			
		if (thirdScreen.Processor.getLiftType().equals("Squat") && thirdScreen.Processor.getCycle() == 1 )	  		
		{
			values.put(EventsDataSQLHelper.TRAINING_MAX, thirdScreen.Processor.getSquatTM());   
			values.put(EventsDataSQLHelper.LBFLAG, sqlLitelbMode);
		}
		if (thirdScreen.Processor.getLiftType().equals("OHP") && thirdScreen.Processor.getCycle() == 1 )	   
		{
			values.put(EventsDataSQLHelper.TRAINING_MAX, thirdScreen.Processor.getOHPTM());
			values.put(EventsDataSQLHelper.LBFLAG, sqlLitelbMode);
		}
		if (thirdScreen.Processor.getLiftType().equals("Deadlift") && thirdScreen.Processor.getCycle() == 1 )	   
		{
			values.put(EventsDataSQLHelper.TRAINING_MAX, thirdScreen.Processor.getDeadTM());
			values.put(EventsDataSQLHelper.LBFLAG, sqlLitelbMode);
		}
		db.insert(EventsDataSQLHelper.TABLE, null, values);
	}

	void createColumns(ThirdScreenActivity thirdScreen, TableRow tr, String liftDate, String cycle, String lift, String freq, String first, String second, String third) {
		//date column creation 
		
		
		
		LayoutParams tvParams = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		tvParams.setMargins(1, 1, 1, 1);
		final int minHeight = 15;
		TextView dateColumn = new TextView(thirdScreen);
		dateColumn.setText(liftDate);
		dateColumn.setLayoutParams(tvParams);
		dateColumn.setBackgroundColor(Color.BLACK);
		dateColumn.setMinHeight(minHeight);
		dateColumn.setGravity(Gravity.CENTER_HORIZONTAL);
		tr.addView(dateColumn);
		
		
		//cycle column creation 
		TextView cycleColumn = new TextView(thirdScreen);
		cycleColumn.setText(cycle);
		cycleColumn.setLayoutParams(tvParams);
		cycleColumn.setBackgroundColor(Color.BLACK);
		cycleColumn.setMinHeight(minHeight);
		cycleColumn.setGravity(Gravity.CENTER_HORIZONTAL);
		tr.addView(cycleColumn);
		
		//lift column creation 
		TextView liftColumn = new TextView(thirdScreen);
		liftColumn.setText(lift);
		liftColumn.setTextColor(Color.WHITE);
		liftColumn.setLayoutParams(tvParams);
		liftColumn.setBackgroundColor(Color.BLACK);
		liftColumn.setMinHeight(minHeight);
		liftColumn.setGravity(Gravity.CENTER_HORIZONTAL);
		tr.addView(liftColumn);
	
		//freq column creation 
		TextView freqColumn = new TextView(thirdScreen);
		freqColumn.setText(freq);
		freqColumn.setLayoutParams(tvParams);
		freqColumn.setBackgroundColor(Color.BLACK);
		freqColumn.setMinHeight(minHeight);
		freqColumn.setGravity(Gravity.CENTER_HORIZONTAL);
		tr.addView(freqColumn);
		
		
		//first lift column creation
		TextView firstLiftColumn = new TextView(thirdScreen);
		firstLiftColumn.setText(first);
		firstLiftColumn.setLayoutParams(tvParams);
		firstLiftColumn.setBackgroundColor(Color.BLACK);
		firstLiftColumn.setMinHeight(minHeight);
		firstLiftColumn.setGravity(Gravity.CENTER_HORIZONTAL);
		tr.addView(firstLiftColumn);
		
		//second lift column creation
		TextView secondLiftColumn = new TextView(thirdScreen);
		secondLiftColumn.setText(second);
		secondLiftColumn.setLayoutParams(tvParams);
		secondLiftColumn.setBackgroundColor(Color.BLACK);
		secondLiftColumn.setMinHeight(minHeight);
		secondLiftColumn.setGravity(Gravity.CENTER_HORIZONTAL);
		tr.addView(secondLiftColumn);
		
		//third lift column creation
		TextView thirdLiftColumn = new TextView(thirdScreen);
		thirdLiftColumn.setText(third);
		thirdLiftColumn.setLayoutParams(tvParams);
		thirdLiftColumn.setBackgroundColor(Color.BLACK);
		thirdLiftColumn.setMinHeight(minHeight);
		thirdLiftColumn.setGravity(Gravity.CENTER_HORIZONTAL);
		tr.addView(thirdLiftColumn);
	}

	void inflateTable(ThirdScreenActivity thirdScreen, Intent intent, String startingDate, SQLiteDatabase db) {
		db.delete("Lifts", null, null);
		thirdScreen.setQuery(null);
		//determine whether to round or not
		String areWeGoingToRound = intent.getStringExtra("round");
		if (areWeGoingToRound.equals("true"))	
			thirdScreen.Processor.setRoundingFlag(true);
		
		else //revert to the default of no round
			thirdScreen.Processor.setRoundingFlag(false);
		
	
		//get unit mode
		thirdScreen.lbmode = intent.getStringExtra("mode");
		if (thirdScreen.lbmode.length() > 1)
		{
			thirdScreen.setMode(thirdScreen.lbmode);
	
		}
	
	
	
		thirdScreen.Processor.setUnitMode(thirdScreen.lbmode);
	
	
	
		//set starting lifts (separate strings so title can them too) 
		String startingBench = intent.getStringExtra("bench");
		String startingSquat = intent.getStringExtra("squat");
		String startingOHP   = intent.getStringExtra("OHP");
		String startingDead  = intent.getStringExtra("dead");
	
		//also set starting lifts locally 
	
	
		thirdScreen.Processor.setStartingLifts(startingBench, startingSquat, startingOHP, startingDead);
		thirdScreen.Processor.setStartingDate(startingDate);
		thirdScreen.Processor.setDate(startingDate);
		int numberCycles =  Integer.parseInt(intent.getStringExtra("numberCycles"));
		thirdScreen.setNumberCycles(numberCycles);

	}

	void reinflateTable(ThirdScreenActivity thirdScreen, Intent intent) {
		String view = intent.getStringExtra("viewMode");
		TableLayout tableRowPrincipal = (TableLayout)thirdScreen.findViewById(R.id.tableLayout1);
		thirdScreen.setQuery("Frequency = '5-5-5'");
		Cursor subcursor = thirdScreen.getEvents();
		switch(view)
		{//		DEFAULT('D'), BENCH('B'), SQUAT('S'), OHP('O'), DEAD('D'), FIVES('5'), THREES('3'), ONES('1');
			case "DEFAULT":
				thirdScreen.setQuery(null);
				tableRowPrincipal.removeAllViews();
				thirdScreen.cursor = thirdScreen.getEvents();
				thirdScreen.showDefaultEvents(thirdScreen.cursor, thirdScreen.cursor);
				break;
			case "BENCH":
				thirdScreen.setQuery("Lift = 'Bench'");
				tableRowPrincipal.removeAllViews();
				thirdScreen.cursor = thirdScreen.getEvents();
				thirdScreen.insertStatus = false;
				System.out.println(DatabaseUtils.dumpCursorToString(thirdScreen.cursor));
				System.out.println( DatabaseUtils.dumpCursorToString(subcursor));
				thirdScreen.showDefaultEvents(thirdScreen.cursor, subcursor);
				break;
			case "SQUAT":
				thirdScreen.setQuery("Lift = 'Squat'");
				tableRowPrincipal.removeAllViews();
				thirdScreen.cursor = thirdScreen.getEvents();
				thirdScreen.insertStatus = false;
				thirdScreen.showDefaultEvents(thirdScreen.cursor, subcursor);
				break;
			case "OHP":
				thirdScreen.setQuery("Lift = 'OHP'");
				tableRowPrincipal.removeAllViews();
				thirdScreen.cursor = thirdScreen.getEvents();
				thirdScreen.insertStatus = false;
				thirdScreen.showDefaultEvents(thirdScreen.cursor, subcursor);
				break;
			case "DEAD":
				thirdScreen.setQuery("Lift = 'Deadlift'");
				tableRowPrincipal.removeAllViews();
				thirdScreen.cursor = thirdScreen.getEvents();
				thirdScreen.insertStatus = false;
				thirdScreen.showDefaultEvents(thirdScreen.cursor, subcursor);
				break;	
			case "FIVES":
				thirdScreen.setQuery("Frequency = '5-5-5'");
				tableRowPrincipal.removeAllViews();
				thirdScreen.cursor = thirdScreen.getEvents();
				thirdScreen.insertStatus = false;
				thirdScreen.showDefaultEvents(thirdScreen.cursor, thirdScreen.cursor);
				break;	
			case "THREES":
				thirdScreen.setQuery("Frequency = '3-3-3'");
				tableRowPrincipal.removeAllViews();
				thirdScreen.cursor = thirdScreen.getEvents();
				thirdScreen.insertStatus = false;
				thirdScreen.showDefaultEvents(thirdScreen.cursor, thirdScreen.cursor);;
				break;
			case "ONES":
				thirdScreen.setQuery("Frequency = '5-3-1'");
				tableRowPrincipal.removeAllViews();
				thirdScreen.cursor = thirdScreen.getEvents();
				thirdScreen.insertStatus = false;
				thirdScreen.showDefaultEvents(thirdScreen.cursor, thirdScreen.cursor);
				break;	
	
		}
	}

}