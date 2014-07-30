package com.example.whowascns;

import java.text.SimpleDateFormat;
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
import android.widget.Toast;




public class MainActivity extends Activity {

	//to be used in activity 3 - may not be most efficient but for testing purposes 
	static int startingDateDay;
	static int startingDateMonth;
	static int startingDateYear;
	RelativeLayout relativeLayout;  //declare this globally

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		final DatePicker dp = (DatePicker) findViewById(R.id.dp);
		final Button setBtn = (Button) findViewById(R.id.set);

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
			Intent intent = new Intent(MainActivity.this, SecondScreen.class);
			intent.putExtra("key", formattedDate );
			intent.putExtra("origin", "first");
			db.close();
			startActivity(intent);

		}

		private void goToThird()
		{
			if (!dbEmpty())
			{
				Intent intent = new Intent(MainActivity.this, ThirdScreen.class);
				intent.putExtra("origin", "first");
				startActivity(intent);
			}
			else
				Toast.makeText(MainActivity.this, "Database currently empty!", Toast.LENGTH_SHORT).show();
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
