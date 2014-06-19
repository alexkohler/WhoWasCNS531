package com.example.whowascns;



import java.text.DecimalFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


//small victories :) 


public class ThirdScreen extends Activity {

	EventsDataSQLHelper eventsData;
	TextView output;
	Calendar myCalendarInstance;
	String myMessage;
	

	//initialize processor to process all lifts, dates, etc...
	DateProcessor Processor = new DateProcessor();

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_third);
		//declare our button, tie it to listener, code listener

		Button thirdScreenBackButton = (Button) findViewById(R.id.thirdScreenBackButton);
		thirdScreenBackButton.setOnClickListener(goToSecondListener);
		
		Button clearButton = (Button) findViewById(R.id.clearButton);
		clearButton.setOnClickListener(clearListener);
		
		TextView dynamicModeTextView = (TextView) findViewById(R.id.dynamicModeTextView);
		//dynamicModeTextView.setText("Kg");
		
		//Data output
		output = (TextView) findViewById(R.id.output);
		
		
		Intent intent = getIntent();

		String startingDate = intent.getStringExtra("key2");
		

	    eventsData = new EventsDataSQLHelper(this);
	    
	    
	    //get unit mode
	    String lbmode = intent.getStringExtra("mode");
	    if (lbmode.length() > 1)
	    dynamicModeTextView.setText(lbmode);
	    else
	    	 dynamicModeTextView.setText("lbs");

	    
	    
	   
		
	    //set starting lifts
	    Processor.setStartingLifts(intent.getStringExtra("bench"), intent.getStringExtra("squat"), intent.getStringExtra("OHP"), intent.getStringExtra("dead"));
	    Processor.setStartingDate(startingDate);
	    
	    
	    int numberCycles =  Integer.parseInt(intent.getStringExtra("numberCycles"));
	   for (int i=0; i < numberCycles; i++) 
	   {
	    calculateCycle();
	    Processor.incrementCycleAndUpdateTMs();
	   }
	   
	    Cursor cursor = getEvents();
	    showEvents(cursor);

	}//end method oncreate 


	 @Override
	  public void onDestroy() {
	    eventsData.close();
	  }

	 
	 private OnClickListener clearListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
		    SQLiteDatabase db = eventsData.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
		    db.delete("Lifts", null, null);
		    
		    //this may not be pretty and may take too many resources 
		    Bundle tempBundle = new Bundle();
		    onCreate(tempBundle);
		}
		 
	 };


	private OnClickListener goToSecondListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			SQLiteDatabase db = eventsData.getWritableDatabase();
			db.close();
			backToSecond();

		}};
		
			
			

		//pass back title argument if things go awry 
		public void addEvent(String startDate, Integer currentCycle, String currentLift, String currentFreq, double first, double second, double third) {
		    SQLiteDatabase db = eventsData.getWritableDatabase();
		    ContentValues values = new ContentValues();

		    
		   
		   values.put(EventsDataSQLHelper.LIFTDATE, Processor.getStartingDate() );
		   values.put(EventsDataSQLHelper.CYCLE, Processor.getCycle());
		   values.put(EventsDataSQLHelper.LIFT, Processor.getLiftType());
		   values.put(EventsDataSQLHelper.FREQUENCY, Processor.getFreq());
		   values.put(EventsDataSQLHelper.FIRST, Processor.getFirstLift());
		   values.put(EventsDataSQLHelper.SECOND, Processor.getSecondLift());
		   values.put(EventsDataSQLHelper.THIRD, Processor.getThirdLift());
		    db.insert(EventsDataSQLHelper.TABLE, null, values);
		  }

		  @SuppressWarnings("deprecation")
		private Cursor getEvents() {
		    SQLiteDatabase db = eventsData.getReadableDatabase();
		    Cursor cursor = db.query(EventsDataSQLHelper.TABLE, null, null, null, null,
		        null, null);
		    
		    startManagingCursor(cursor);
		    return cursor;
		  }

		  private void showEvents(Cursor cursor) {
		    StringBuilder ret = new StringBuilder("5/3/1 Schema:\n\n");
		    while (cursor.moveToNext()) {
		      //long id = cursor.getLong(0);
		      String liftDate = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.LIFTDATE));
		      String cycle = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.CYCLE));
		      String lift = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.LIFT));
		      String freq = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.FREQUENCY));
		      Double first = roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.FIRST)));
		      Double second = roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.SECOND)));
		      Double third = roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.THIRD)));
		      ret.append(liftDate + "| " + cycle + "| " + lift + "| " + freq + "| " + first + "| " + second + "| " + third + "\n");
		    }
		    output.setText(ret);
		  }
		


		private void backToSecond()
		{
			//need to pass back date before going back. 
			//startActivity(new Intent(this, SecondScreen.class));
			//temp(perhaps permanent) fix 
			startActivity(new Intent(this, MainActivity.class));
			SQLiteDatabase db = eventsData.getWritableDatabase();
			db.delete("Lifts", null, null);
		}
		
		
		
		private void calculateCycle()
		{
			//bench fives
			Processor.calculateFivesDay(Processor.getBenchTM());
			addEvent(Processor.getStartingDate(), Processor.getCycle(), Processor.getLiftType(), Processor.getFreq(), Processor.getFirstLift(), Processor.getSecondLift(), Processor.getThirdLift());
		
			
			Processor.incrementDay();
			Processor.incrementLift();
			
			//Squat fives
			Processor.calculateFivesDay(Processor.getSquatTM());
			addEvent(Processor.getStartingDate(), Processor.getCycle(), Processor.getLiftType(), Processor.getFreq(), Processor.getFirstLift(), Processor.getSecondLift(), Processor.getThirdLift());
			
			Processor.incrementDay();
			Processor.incrementDay();
			Processor.incrementLift();
			Processor.incrementLift();
			
			//OHP Fives 
			Processor.calculateFivesDay(Processor.getOHPTM());
			addEvent(Processor.getStartingDate(), Processor.getCycle(), Processor.getLiftType(), Processor.getFreq(), Processor.getFirstLift(), Processor.getSecondLift(), Processor.getThirdLift());
			
			Processor.incrementDay();
			Processor.incrementLift();
			
			//Dead Fives
			Processor.calculateFivesDay(Processor.getDeadTM());
			addEvent(Processor.getStartingDate(), Processor.getCycle(), Processor.getLiftType(), Processor.getFreq(), Processor.getFirstLift(), Processor.getSecondLift(), Processor.getThirdLift());

			Processor.incrementDay();
			Processor.incrementDay();
			Processor.incrementLift();
			Processor.incrementLift();
			
			//bench triples
			Processor.calculateTriplesDay(Processor.getBenchTM());
			addEvent(Processor.getStartingDate(), Processor.getCycle(), Processor.getLiftType(), Processor.getFreq(), Processor.getFirstLift(), Processor.getSecondLift(), Processor.getThirdLift());
		
			
			Processor.incrementDay();
			Processor.incrementLift();
			
			//Squat triples
			Processor.calculateTriplesDay(Processor.getSquatTM());
			addEvent(Processor.getStartingDate(), Processor.getCycle(), Processor.getLiftType(), Processor.getFreq(), Processor.getFirstLift(), Processor.getSecondLift(), Processor.getThirdLift());
			
			Processor.incrementDay();
			Processor.incrementDay();
			Processor.incrementLift();
			Processor.incrementLift();
			
			//OHP triples 
			Processor.calculateTriplesDay(Processor.getOHPTM());
			addEvent(Processor.getStartingDate(), Processor.getCycle(), Processor.getLiftType(), Processor.getFreq(), Processor.getFirstLift(), Processor.getSecondLift(), Processor.getThirdLift());
			
			Processor.incrementDay();
			Processor.incrementLift();
			
			//Dead triples
			Processor.calculateFivesDay(Processor.getDeadTM());
			addEvent(Processor.getStartingDate(), Processor.getCycle(), Processor.getLiftType(), Processor.getFreq(), Processor.getFirstLift(), Processor.getSecondLift(), Processor.getThirdLift());

			Processor.incrementDay();
			Processor.incrementDay();
			Processor.incrementLift();
			Processor.incrementLift();
			
			//bench single
			Processor.calculateSingleDay(Processor.getBenchTM());
			addEvent(Processor.getStartingDate(), Processor.getCycle(), Processor.getLiftType(), Processor.getFreq(), Processor.getFirstLift(), Processor.getSecondLift(), Processor.getThirdLift());
		
			
			Processor.incrementDay();
			Processor.incrementLift();
			
			//Squat single
			Processor.calculateSingleDay(Processor.getSquatTM());
			addEvent(Processor.getStartingDate(), Processor.getCycle(), Processor.getLiftType(), Processor.getFreq(), Processor.getFirstLift(), Processor.getSecondLift(), Processor.getThirdLift());
			
			Processor.incrementDay();
			Processor.incrementDay();
			Processor.incrementLift();
			Processor.incrementLift();
			
			//OHP triples 
			Processor.calculateSingleDay(Processor.getOHPTM());
			addEvent(Processor.getStartingDate(), Processor.getCycle(), Processor.getLiftType(), Processor.getFreq(), Processor.getFirstLift(), Processor.getSecondLift(), Processor.getThirdLift());
			
			Processor.incrementDay();
			Processor.incrementLift();
			
			//Dead triples
			Processor.calculateSingleDay(Processor.getDeadTM());
			addEvent(Processor.getStartingDate(), Processor.getCycle(), Processor.getLiftType(), Processor.getFreq(), Processor.getFirstLift(), Processor.getSecondLift(), Processor.getThirdLift());

			Processor.incrementDay();
			Processor.incrementDay();
			Processor.incrementLift();
			Processor.incrementLift();
			

			
		}
		
		double roundtoTwoDecimals(double d) {
            DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
}

}//end thirdscreen activiy 





