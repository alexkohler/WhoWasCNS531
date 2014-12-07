package com.kohlerbear.whowascnscalc;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;



public class ConfigTool {

	
	Context mContext;
	Boolean topCornerCaseFlag = false;
	Boolean bottomCornerCaseFlag = false;
	EventsDataSQLHelper eventsData;
	
    public ConfigTool(Context context){
        mContext = context;
		eventsData = new EventsDataSQLHelper(mContext);
    }
	
    public Boolean topCase()
    {
    	return topCornerCaseFlag;
    }
    
    public Boolean bottomCase()
    {
    	return bottomCornerCaseFlag;
    }
    
    
	public Intent configurePreviousSet(String myDate, String myPrevLift,  String viewMode, String mode, String[] liftPattern)
	{
		String prevLft = myPrevLift;
		//Boolean topCornerCaseFlag = false;
		String liftDate = null;
		String cycle = null;
		String lift = null;
		String freq = null;
		String first = null;
		String second = null;
		String third = null;
		SQLiteDatabase db = eventsData.getReadableDatabase();
		Cursor cursor = db.query(EventsDataSQLHelper.TABLE, null, "liftDate = '" + myDate + "' AND Lift = '" + prevLft + "'", null, null, null, null);
		if (!cursor.moveToNext())
			{
			topCornerCaseFlag = true; 
//			Toast.makeText(mContext, "Can't go back any further. This is the beginning of your projection!", Toast.LENGTH_SHORT).show(); 
			}
		else
			cursor.moveToPrevious();
		while (cursor.moveToNext()) {
		liftDate = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.LIFTDATE));
		cycle = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.CYCLE));
		lift = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.LIFT));
		freq = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.FREQUENCY));
		first = String.valueOf(roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.FIRST))));
		second = String.valueOf(roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.SECOND))));
		third = String.valueOf(roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.THIRD))));
		}
		
		Intent intent  = new Intent(mContext, IndividualViews.class);
		intent.putExtra("cycle", cycle);
		intent.putExtra("frequency", freq);
		intent.putExtra("liftType", lift);
		intent.putExtra("firstLift", first);
		intent.putExtra("secondLift", second);
		intent.putExtra("thirdLift", third);
		intent.putExtra("date", liftDate);
		intent.putExtra("viewMode", viewMode);
		intent.putExtra("mode", mode);
		intent.putExtra("liftPattern", liftPattern);
		//forwardIntent.putExtra("mode", lbmodeString);
		//TODO take care of mode
		db.close();
		
		
		return intent;
		//startActivity(intent);
		//overridePendingTransition(0,R.anim.exit_slide_down);
		
		//other direction
		//overridePendingTransition(R.anim.push_down_out,R.anim.push_down_in);
	}
	
	
	public Intent configureNextSet(String myDate, String myNextLift, String viewMode, String lbMode, String[] pattern)
	{
		String nextLift = myNextLift;
		String liftDate = null;
		String cycle = null;
		String lift = null;
		String freq = null;
		String first = null;
		String second = null;
		String third = null;
		SQLiteDatabase db = eventsData.getReadableDatabase();
		Cursor cursor = db.query(EventsDataSQLHelper.TABLE, null, "liftDate = '" + myDate + "' AND Lift = '" + nextLift + "'", null, null,
				null, null);
		if (!cursor.moveToNext())
		{
			bottomCornerCaseFlag = true;
		}
		else
			cursor.moveToPrevious();
		while (cursor.moveToNext()) {
		liftDate = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.LIFTDATE));
		cycle = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.CYCLE));
		lift = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.LIFT));
		freq = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.FREQUENCY));
		first = String.valueOf(roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.FIRST))));
		second = String.valueOf(roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.SECOND))));
		third = String.valueOf(roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.THIRD))));
		}
		
		Intent intent  = new Intent(mContext, IndividualViews.class);
		intent.putExtra("cycle", cycle);
		intent.putExtra("frequency", freq);
		intent.putExtra("liftType", lift);
		intent.putExtra("firstLift", first);
		intent.putExtra("secondLift", second);
		intent.putExtra("thirdLift", third);
		intent.putExtra("date", liftDate);
		intent.putExtra("viewMode", viewMode);
		intent.putExtra("mode", lbMode);
		intent.putExtra("liftPattern", pattern);
		//forwardIntent.putExtra("mode", lbmodeString);
		//TODO take care of mode
		db.close();
/*		if (!bottomCornerCaseFlag)
		{
		startActivity(intent);
		overridePendingTransition(0,R.anim.exit_slide_up);
		//hide bottom arrow here maybe?
		}*/
		return intent;
		//other direction
		//overridePendingTransition(R.anim.push_down_out,R.anim.push_down_in);
	}
	
	
	String[] getPrevLift(Calendar c1, String[] myPattern, String currentLift, String viewMode) {
		//String[] myPattern = {"Squat", "Rest", "Bench", "Deadlift", "Rest", "OHP"  };
		//each case: getnextliftfunctiondefault that finds the lift we are at case of, gets the column index, increments (or decrements) that column indexes WHILE incrementing or decrementing the day until it runs into a day that isn't rest.
		//returns a 2 dimensioned array, with the first value being nextLift and the second value being incremented string
		String[] resultsBackward = new String[2];
		if (viewMode.equals("BENCH") || viewMode.equals("SQUAT") || viewMode.equals("DEAD") || viewMode.equals("OHP") )
		{
			
			String decrementedString = decrementDay(c1, myPattern.length); 	 
			resultsBackward[0] = currentLift; //current lift should not change, we are in the view of all the same lift.
			resultsBackward[1] = decrementedString;
			return resultsBackward;
		}
		
		
		
		int i = 0;
		String decrementedString = null;
		//find our column index of our current lift...
		while (!myPattern[i].equals(currentLift))
			i++;
		//we need to increment at least once, but there may be more than one rest day we we have loop implemented..
		int j = i - 1;//set a new incrementer for i
		if (j < 0){//don't allow negatives 
			j = myPattern.length - 1; // put j back at the top
			if (viewMode.equals("FIVES") || viewMode.equals("THREES") || viewMode.equals("ONES"))
				decrementedString = decrementDay(c1, 2 * myPattern.length);//so we must run through a full cycle twice. 
			}
		decrementedString = decrementDay(c1, 1);
		if (j == 0 && myPattern[j].equals("Rest")){//rest part is for bounds protection, if it is not equal to rest we don't need to worry about it going into the next while loop, we already found our prevlift
			j = myPattern.length - 1; //start at the top
			if (viewMode.equals("FIVES") || viewMode.equals("THREES") || viewMode.equals("ONES"))
				decrementedString = decrementDay(c1, 2 * myPattern.length);//so we must run through a full cycle twice. 
			}
		while (myPattern[j].equals("Rest"))
			{
			j--;
			decrementedString = decrementDay(c1, 1);
			if (j == 0  && myPattern[j].equals("Rest"))
			{
				j = myPattern.length - 1; //go back to top (based on index)
				if (viewMode.equals("FIVES") || viewMode.equals("THREES") || viewMode.equals("ONES"))
					decrementedString = decrementDay(c1, 2 * myPattern.length);//so we must run through a full cycle twice. 
			}	
			}
		//assert: if we broke out of this loop then myPattern[j] is NOT a rest day, and hence our next lift
		String prevLift = myPattern[j];
		
		resultsBackward[0] = prevLift;
		resultsBackward[1] = decrementedString;
		
		return resultsBackward;
	}
	
	String[] getNextLift(Calendar c1, String[] myPattern, String currentLift, String viewMode) {
		//String[] myPattern = {"Squat", "Rest", "Bench", "Deadlift", "Rest", "OHP"  };
		//each case: getnextliftfunctiondefault that finds the lift we are at case of, gets the column index,
		//increments (or decrements) that column indexes WHILE incrementing or decrementing the day until it runs into a day that isn't rest.
		//returns a 2 dimensioned array, with the first value being nextLift and the second value being incremented string
		String[] resultsForward = new String[2];
		if (viewMode.equals("BENCH") || viewMode.equals("SQUAT") || viewMode.equals("DEAD") || viewMode.equals("OHP") )
		{
			
			String incrementedString = incrementDay(c1, myPattern.length); 	 
			resultsForward[0] = currentLift; //current lift should not change, we are in the view of all the same lift.
			resultsForward[1] = incrementedString;
			return resultsForward;
		}
		
		
		
		int i = 0;
		//find our column index of our current lift...
		while (!myPattern[i].equals(currentLift))
			i++;
		//we need to increment at least once, but there may be more than one rest day we we have loop implemented..
		int j = i + 1;//set a new incrementer for i
		String incrementedString = incrementDay(c1, 1);
		if (j >= myPattern.length){ //may not need second declaration?
			j = 0;
			if (viewMode.equals("FIVES") || viewMode.equals("THREES") || viewMode.equals("ONES"))
			incrementedString = incrementDay(c1, 2 * myPattern.length);//we must run thorugh cycle twice if we break forward or backward through our array indexes
			}
		while (myPattern[j].equals("Rest")) //while loop is here to take into account multiple rest days
			{
			j++;
			incrementedString = incrementDay(c1, 1);
			if (j >= myPattern.length )
				{
				j = 0; //go back to beginnig
				if (viewMode.equals("FIVES") || viewMode.equals("THREES") || viewMode.equals("ONES"))
				incrementedString = incrementDay(c1, 2 * myPattern.length);//we must run thorugh cycle twice if we break forward or backward through our array indexes
				}
			}
		//assert: if we broke out of this loop then myPattern[j] is NOT a rest day, and hence our next lift
		String nextLift = myPattern[j];
		
		resultsForward[0] = nextLift;
		resultsForward[1] = incrementedString;
		
		return resultsForward;
	}
	
	//increment day and return as a string 
	public String incrementDay(Calendar myCal, int days)
	{
		myCal.add(Calendar.DAY_OF_MONTH, days);  // number of days to add
		SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy", java.util.Locale.getDefault());
		Date myDate = myCal.getTime();
		String formattedDate = df.format(myDate);

		return formattedDate;
	}
	
	
	//decrement day and return as a string 
	public String decrementDay(Calendar myCal, int days)
	{
		int decrementer = (-1 * days);
		myCal.add(Calendar.DAY_OF_MONTH, decrementer);  // number of days to add
		SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy", java.util.Locale.getDefault());
		Date myDate = myCal.getTime();
		String formattedDate = df.format(myDate);

		return formattedDate;
	}
	
	
	
	
	double roundtoTwoDecimals(double d) 
	{
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d));
	}
	
	public String[] populateArrayBasedOnDatabase() {
		ArrayList<String> myPattern = new ArrayList<String>(); //using arraylist because array size not known at runtime
		SQLiteDatabase db = eventsData.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
		Cursor cursor = db.rawQuery("SELECT pattern FROM Lifts limit 1", null);
		String liftBuffer = "";
		if (cursor.moveToNext())
			liftBuffer = cursor.getString(0);
		else
			Toast.makeText(mContext, "A database error occured", Toast.LENGTH_LONG).show();//TODO add better analytics logging here (if it's even possible to get the tracker in here) 
		for (int i=0; i < liftBuffer.length(); i++)
		{
			switch (liftBuffer.charAt(i))
			{
			case 'B':
				myPattern.add("Bench");
				break;
			case 'S':
				myPattern.add("Squat");
				break;
			case 'D':
				myPattern.add("Deadlift");
				break;
			case 'O':
				myPattern.add("OHP");
				break;
			case 'R':
				myPattern.add("Rest");
				break;
			}
		}
		
		return myPattern.toArray(new String[myPattern.size()]);
	}
	

public String getRoundingFlagFromDatabase()//TODO change me from a stub to a real boy (method) 
{
	SQLiteDatabase db = eventsData.getWritableDatabase();
	Cursor cursor = db.rawQuery("Select RoundFlag from Lifts limit 1", null);
	String roundingFlag = "1"; //true by default
	if (cursor.moveToNext())
		roundingFlag = cursor.getString(0);
	
	return roundingFlag;
}

public String getLbModeFromDatabase()//TODO change me from a stub to a real boy (method) 
{
	SQLiteDatabase db = eventsData.getWritableDatabase();
	Cursor cursor = db.rawQuery("Select column_lbFlag from Lifts limit 1", null);
	String unitMode = "error";
	if (cursor.moveToNext())
		unitMode = cursor.getString(0); 
	
	String modeString = "";
	if (unitMode.equals("1"))
		modeString = "Lbs";
	else
		modeString = "Kgs";
	
	
	if (modeString.equals(""))
	{
		//TODO send analytics tracking here
	}
	
	return modeString;
}
	
	
public String getStartingDateFromDatabase()
{
	SQLiteDatabase db = eventsData.getWritableDatabase();
	Cursor cursor = db.rawQuery("Select liftDate from Lifts limit 1", null);
	String startingDate = "11-11-11";//TODO think of better way to error handle this
	if (cursor.moveToNext())
		startingDate = cursor.getString(0);
	
	return startingDate;

}

public boolean dbEmpty()
{
	SQLiteDatabase db = eventsData.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
	Cursor mCursor = db.rawQuery("SELECT * FROM " + EventsDataSQLHelper.TABLE, null);
	Boolean rowExists;

	if (mCursor.moveToFirst())
	{
		// DO SOMETHING WITH CURSOR
		rowExists = false;

	} else
	{
		// I AM EMPTY
		rowExists = true;
	}

	db.close();
	return rowExists;
}

}
	

