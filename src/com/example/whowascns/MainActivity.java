package com.example.whowascns;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Toast;




public class MainActivity extends ActionBarActivity {
	
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
		
		
		Intent intent = new Intent(MainActivity.this, SecondScreen.class);
		intent.putExtra("key", formattedDate );
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
/*	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}*/

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
