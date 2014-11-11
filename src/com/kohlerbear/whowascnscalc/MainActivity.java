package com.kohlerbear.whowascnscalc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;




public class MainActivity extends Activity {

	//private EasyTracker easyTracker = null;
	
	//to be used in activity 3 - may not be most efficient but for testing purposes 
	static int startingDateDay;
	static int startingDateMonth;
	static int startingDateYear;
	RelativeLayout relativeLayout;  //declare this globally
	String[] defaultPattern = {"Bench", "Squat", "Rest", "OHP", "Deadlift", "Rest"  };
	String[] liftPattern = new String[7];
	TextView liftTicker;
	Tracker tracker = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.AppBaseThemeNoTitleBar);
		setContentView(R.layout.activity_main);
		//Initialize tracker
		// Sending the same screen view hit using MapBuilder.createAppView()
		GoogleAnalytics.getInstance(this).getTracker("UA-55018534-1");
		tracker = GoogleAnalytics.getInstance(this).getTracker("UA-55018534-1");
		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME, "Home Screen");

		tracker.send(hitParameters);

		final DatePicker dp = (DatePicker) findViewById(R.id.dp);
		final Button setBtn = (Button) findViewById(R.id.set);
		final Button adjustLiftPatternButton = (Button) findViewById(R.id.adjustPatternButton);
		liftTicker = (TextView) findViewById(R.id.liftTicker);
		
		Intent patternAdjusterIntent = getIntent();
		String origin = patternAdjusterIntent.getStringExtra("origin");

		
		if (origin != null)//if we came from patternAdjuster
			liftPattern = patternAdjusterIntent.getStringArrayExtra("liftPattern");
		else
			liftPattern = defaultPattern;
		
		String liftTickerBuffer = "Current lift Pattern";
		for (int i=0; i < liftPattern.length; i++)
			liftTickerBuffer = liftTickerBuffer + " " + liftPattern[i].substring(0, 1) + " - ";
		liftTickerBuffer = liftTickerBuffer.substring(0, liftTickerBuffer.length() - 2); //remove last dash
		
		liftTicker.setText(liftTickerBuffer);

		
		adjustLiftPatternButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent firstActivityToAdjustPattern = new Intent(MainActivity.this, AdjustLiftPatternActivity.class);
				firstActivityToAdjustPattern.putExtra("pattern", liftPattern); //ASSERT: lift pattern won't be null because of the case statement in onCreate
				startActivity(firstActivityToAdjustPattern);
				
			}});

		setBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "Date Selected: " + (dp.getMonth() + 1) + "-" + dp.getDayOfMonth() + "-" + dp.getYear(),  Toast.LENGTH_SHORT).show();
				startingDateDay = dp.getDayOfMonth();
				startingDateMonth = dp.getMonth();
				startingDateYear = dp.getYear();



				goToSecond();
			}
		});


		Button existingProjectionButton = (Button) findViewById(R.id.existingProjectionButton);
		existingProjectionButton.setOnClickListener(goToThirdListener);


	}//end method onCreate 
	
	 @Override
	  public void onStart() {
	    super.onStart();
	    EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	  }
	  @Override
	  public void onStop() {
	    super.onStop();
	    EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	  }


	private OnClickListener goToThirdListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			goToThird();

		}};


		private void goToSecond()
		{

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(0);
			cal.set(startingDateYear, startingDateMonth, startingDateDay);

			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", java.util.Locale.getDefault());
			Date myDate = cal.getTime();
			String formattedDate = dateFormat.format(myDate);
			//if this db does not have a database....
			EventsDataSQLHelper eventsData = new EventsDataSQLHelper(this);
			SQLiteDatabase db = eventsData.getWritableDatabase();
			db.execSQL("drop table Lifts");
			db.execSQL("create table Lifts (liftDate text not null, Cycle integer, Lift text not null, Frequency text not null, First_Lift real, Second_Lift real, Third_Lift real, Training_Max integer, column_lbFlag integer)");
			Intent intent = new Intent(MainActivity.this, SecondScreenActivity.class);
			intent.putExtra("key", formattedDate );
			intent.putExtra("origin", "first");
			intent.putExtra("liftPattern", liftPattern);
			
			
			db.close();
			startActivity(intent);

		}

		private void goToThird()
		{
			if (!dbEmpty())
			{
				Intent intent = new Intent(MainActivity.this, ThirdScreenActivity.class);
				intent.putExtra("origin", "first");
				//to read a previous lift pattern, we can break down the textview back into an array
				//liftPattern = populateArrayBasedOnTextView();//horrible, use the database instead
				liftPattern = populateArrayBasedOnDatabase();
				intent.putExtra("liftPattern", liftPattern);
				
				startActivity(intent);
			}
			else
				Toast.makeText(MainActivity.this, "No previous projection exists!", Toast.LENGTH_SHORT).show();
		}
		
		private String[] populateArrayBasedOnDatabase() {
			ArrayList<String> myPattern = new ArrayList<String>(); //using arraylist because array size not known at runtime
/*			String liftBuffer = liftTicker.getText().toString().substring(19);
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
			}*/
			EventsDataSQLHelper eventsData = new EventsDataSQLHelper(this);
			SQLiteDatabase db = eventsData.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
			Cursor mCursor = db.rawQuery("SELECT Lift, liftDate FROM Lifts where cycle = ?", new String[]{"1"});
			boolean first = true;
			Calendar cal = Calendar.getInstance();
			String firstLift = "";
			while (mCursor.moveToNext())
			{
				String currentLiftDate = mCursor.getString(1);
				if (first)
				{
					firstLift = mCursor.getString(0);
					myPattern.add(firstLift);
					first = false;
				}
				else //a non first empty
				{
					mCursor.moveToPrevious();
					String prevDate = mCursor.getString(1);
					mCursor.moveToNext(); //assert -we can maintain loop integrity here because we moved back before moving forward
					String currentLift = mCursor.getString(0);
					
					//xx - xx - xxxx
					//01 2 34 5 6789
					int year = Integer.valueOf(prevDate.substring(6, 10));
					int day = Integer.valueOf(prevDate.substring(3, 5)); 
					int month = Integer.valueOf(prevDate.substring(0, 2)) - 1;
					cal.set(year, month, day);

					SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", java.util.Locale.getDefault());
					Date previousDate = cal.getTime();
					cal.add(Calendar.DAY_OF_MONTH, 1);
					Date incrementedPrevDate = cal.getTime();
					String incrementedPrevDateString = dateFormat.format(incrementedPrevDate);
					 if (currentLiftDate.intern().equals(incrementedPrevDateString.intern()))
						myPattern.add(currentLift);
					else
						{
						myPattern.add("Rest");
						//take double rest days into account
						cal.add(Calendar.DAY_OF_MONTH, 1);
						String doublyIncrementedPrevDateString = dateFormat.format(cal.getTime());
						if (doublyIncrementedPrevDateString.intern().equals(currentLiftDate.intern()))
							myPattern.add(currentLift);
						else
							myPattern.add("Rest");
						}	
					 if (currentLift.intern().equals(firstLift.intern()))
							break;
				}
				
			}
			System.out.println("Then pattern will be " + myPattern.toString());
			return myPattern.toArray(new String[myPattern.size()]);
		}



		private String[] populateArrayBasedOnTextView() {
			ArrayList<String> myPattern = new ArrayList<String>(); //using arraylist because array size not known at runtime
			String liftBuffer = liftTicker.getText().toString().substring(19);
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

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {

			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}



		public boolean dbEmpty()
		{
			EventsDataSQLHelper eventsData = new EventsDataSQLHelper(this);
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
