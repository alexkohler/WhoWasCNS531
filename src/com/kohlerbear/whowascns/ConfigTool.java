package com.kohlerbear.whowascns;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
	
    public ConfigTool(Context context){
        mContext = context;
    }
	
    public Boolean topCase()
    {
    	return topCornerCaseFlag;
    }
    
    public Boolean bottomCase()
    {
    	return bottomCornerCaseFlag;
    }
    
    
	public Intent configurePreviousSet(String myDate, String myPrevLift,  String viewMode, String mode, boolean[] boolArray, String[] liftPattern)
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
		EventsDataSQLHelper eventsData = new EventsDataSQLHelper(mContext);
		SQLiteDatabase db = eventsData.getReadableDatabase();
		Cursor cursor = db.query(EventsDataSQLHelper.TABLE, null, "liftDate = '" + myDate + "' AND Lift = '" + prevLft + "'", null, null, null, null);
		if (!cursor.moveToNext())
			{
			topCornerCaseFlag = true; 
			Toast.makeText(mContext, "Can't go back any further. This is the beginning of your projection!", Toast.LENGTH_SHORT).show(); 
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
		
		Intent intent  = new Intent(mContext, IndividualViewActivity.class);
		intent.putExtra("cycle", cycle);
		intent.putExtra("frequency", freq);
		intent.putExtra("liftType", lift);
		intent.putExtra("firstLift", first);
		intent.putExtra("secondLift", second);
		intent.putExtra("thirdLift", third);
		intent.putExtra("date", liftDate);
		intent.putExtra("viewMode", viewMode);
		intent.putExtra("mode", mode);
		intent.putExtra("boolArray", boolArray);
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
	
	
	public Intent configureNextSet(String myDate, String myNextLift, String viewMode, String lbMode, boolean[] boolArray, String[] pattern)
	{
		String nextLift = myNextLift;
		String liftDate = null;
		String cycle = null;
		String lift = null;
		String freq = null;
		String first = null;
		String second = null;
		String third = null;
		EventsDataSQLHelper eventsData = new EventsDataSQLHelper(mContext);
		SQLiteDatabase db = eventsData.getReadableDatabase();
		Cursor cursor = db.query(EventsDataSQLHelper.TABLE, null, "liftDate = '" + myDate + "' AND Lift = '" + nextLift + "'", null, null,
				null, null);
		if (!cursor.moveToNext())
		{
			bottomCornerCaseFlag = true;
			Toast.makeText(mContext, "Can't continue any further. This is the end of your projection!", Toast.LENGTH_SHORT).show();
			//hide bottom arrow here 
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
		
		Intent intent  = new Intent(mContext, IndividualViewActivity.class);
		intent.putExtra("cycle", cycle);
		intent.putExtra("frequency", freq);
		intent.putExtra("liftType", lift);
		intent.putExtra("firstLift", first);
		intent.putExtra("secondLift", second);
		intent.putExtra("thirdLift", third);
		intent.putExtra("date", liftDate);
		intent.putExtra("viewMode", viewMode);
		intent.putExtra("mode", lbMode);
		intent.putExtra("boolArray", boolArray);
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
}
