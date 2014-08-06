package com.example.whowascns;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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




public class MainActivity extends Activity {

	//to be used in activity 3 - may not be most efficient but for testing purposes 
	static int startingDateDay;
	static int startingDateMonth;
	static int startingDateYear;
	RelativeLayout relativeLayout;  //declare this globally
	String[] defaultPattern = {"Bench", "Squat", "Rest", "OHP", "Deadlift", "Rest"  };
	String[] liftPattern = new String[7];
	TextView liftTicker;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		final DatePicker dp = (DatePicker) findViewById(R.id.dp);
		final Button setBtn = (Button) findViewById(R.id.set);
		final Button adjustLiftPatternButton = (Button) findViewById(R.id.adjustPatternButton);
		liftTicker = (TextView) findViewById(R.id.liftTicker);
		
		Intent patternAdjusterIntent = getIntent();
		String origin = patternAdjusterIntent.getStringExtra("origin");

		
		if (origin != null)//if we came from patternAdjuster
		{
		liftPattern = patternAdjusterIntent.getStringArrayExtra("liftPattern");
		//lift ticker needs to be populated with the first letter of every lift in our pattern 
		String liftTickerBuffer = "Current lift Pattern";
		for (int i=0; i < liftPattern.length; i++)
			liftTickerBuffer = liftTickerBuffer + " " + liftPattern[i].substring(0, 1) + " - ";
		liftTickerBuffer = liftTickerBuffer.substring(0, liftTickerBuffer.length() -2); //remove last dash
		
		liftTicker.setText(liftTickerBuffer);
		}
		
		else
		{
		liftPattern = defaultPattern;
		
		}
	

		
		adjustLiftPatternButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent firstActivityToAdjustPattern = new Intent(MainActivity.this, AdjustLiftPatternActivity.class);
				startActivity(firstActivityToAdjustPattern);
				
			}});

		setBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "Date Selected: " + dp.getMonth() + " " + dp.getDayOfMonth() + " " + dp.getYear(),  Toast.LENGTH_SHORT).show();
				startingDateDay = dp.getDayOfMonth();
				startingDateMonth = dp.getMonth();
				startingDateYear = dp.getYear();



				goToSecond();
			}
		});


		Button existingProjectionButton = (Button) findViewById(R.id.existingProjectionButton);
		existingProjectionButton.setOnClickListener(goToThirdListener);


	}//end method onCreate 


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
				liftPattern = populateArrayBasedOnTextView();
				intent.putExtra("liftPattern", liftPattern);
				
				startActivity(intent);
			}
			else
				Toast.makeText(MainActivity.this, "Database currently empty!", Toast.LENGTH_SHORT).show();
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
