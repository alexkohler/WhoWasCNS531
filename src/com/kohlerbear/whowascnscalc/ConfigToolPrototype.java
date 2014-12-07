package com.kohlerbear.whowascnscalc;

import android.app.Application;
import android.content.Context;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;



public class ConfigToolPrototype extends ConfigTool{

	
	public ConfigToolPrototype(Context context) {
		super(context);
	}


	
    public Boolean topCase()
    {
    	return topCornerCaseFlag;
    }
    
    public Boolean bottomCase()
    {
    	return bottomCornerCaseFlag;
    }
    
    
	public Bundle configurePrevSet(String myDate, String myPrevLift,  String viewMode, String lbMode, String[] pattern, Context context, int cycleArgForQuery)
	{
		String prevLft = myPrevLift;
		//Boolean topCornerCaseFlag = false;
		String liftDate = null;
		int cycle = 0;
		String lift = null;
		String freq = null;
		double first = 0;
		double second = 0;
		double third = 0;
		SQLiteDatabase db = eventsData.getReadableDatabase();
		Cursor cursor = db.query(EventsDataSQLHelper.TABLE, null, "liftDate = '" + myDate + "' AND Lift = '" + prevLft + "' AND Cycle = '" + cycleArgForQuery + "'", null, null, null, null);
		if (!cursor.moveToNext())
			{
			topCornerCaseFlag = true; 
			}
		else
			cursor.moveToPrevious();
		while (cursor.moveToNext()) {
			liftDate = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.LIFTDATE));
			cycle = cursor.getInt(cursor.getColumnIndex(EventsDataSQLHelper.CYCLE));
			lift = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.LIFT));
			freq = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.FREQUENCY));
			first = roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.FIRST)));
			second = roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.SECOND)));
			third = roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.THIRD)));
		}
		
		Bundle prevLiftArgs = new Bundle();
		prevLiftArgs.putInt("cycle", cycle);
		prevLiftArgs.putString("frequency", freq);
		prevLiftArgs.putString("liftType", lift);
		prevLiftArgs.putDouble("firstLift", first);
		prevLiftArgs.putDouble("secondLift", second);
		prevLiftArgs.putDouble("thirdLift", third);
		prevLiftArgs.putString("date", liftDate);
		prevLiftArgs.putString("viewMode", viewMode);
		prevLiftArgs.putString("mode", lbMode);
		prevLiftArgs.putStringArray("liftPattern", pattern);
		//forwardIntent.putExtra("mode", lbmodeString);
		//TODO take care of mode
		db.close();
		
		
		return prevLiftArgs;
		//startActivity(intent);
		//overridePendingTransition(0,R.anim.exit_slide_down);
		
		//other direction
		//overridePendingTransition(R.anim.push_down_out,R.anim.push_down_in);
	}
	
	
	public Bundle configureNextSet(String myDate, String myNextLift, String viewMode, String lbMode, String[] pattern, Context context, int cycleArgForQuery)// A CYCLE DEFINITELY NEEDS PASSED HERE
	{
		String nextLift = myNextLift;
		String liftDate = null;
		int cycle = 0;
		String lift = null;
		String freq = null;
		double first = 0;
		double second = 0;
		double third = 0;
		SQLiteDatabase db = eventsData.getReadableDatabase();
		Cursor cursor = db.query(EventsDataSQLHelper.TABLE, null, "liftDate = '" + myDate + "' AND Lift = '" + nextLift + "' AND Cycle = '" + cycleArgForQuery + "'", null, null,
				null, null);
		if (!cursor.moveToNext())
		{
			bottomCornerCaseFlag = true;
		}
		else
			cursor.moveToPrevious();
		while (cursor.moveToNext()) {
		liftDate = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.LIFTDATE));
		cycle = cursor.getInt(cursor.getColumnIndex(EventsDataSQLHelper.CYCLE));
		lift = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.LIFT));
		freq = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.FREQUENCY));
		first = roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.FIRST)));
		second = roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.SECOND)));
		third = roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.THIRD)));
		}
		
		Bundle nextLiftArgs = new Bundle();
		nextLiftArgs.putInt("cycle", cycle);
		nextLiftArgs.putString("frequency", freq);
		nextLiftArgs.putString("liftType", lift);
		nextLiftArgs.putDouble("firstLift", first);
		nextLiftArgs.putDouble("secondLift", second);
		nextLiftArgs.putDouble("thirdLift", third);
		nextLiftArgs.putString("date", liftDate);
		nextLiftArgs.putString("viewMode", viewMode);
		nextLiftArgs.putString("mode", lbMode);
		nextLiftArgs.putStringArray("liftPattern", pattern);
		db.close();
		return nextLiftArgs;
		//other direction
		//overridePendingTransition(R.anim.push_down_out,R.anim.push_down_in);
	}
	
	
}


	


