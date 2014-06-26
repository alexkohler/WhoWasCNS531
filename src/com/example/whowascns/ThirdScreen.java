package com.example.whowascns;



import java.text.DecimalFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;


//small victories :) 


public class ThirdScreen extends Activity {

	EventsDataSQLHelper eventsData;
	TextView OUTPUT;
	String MODE_FORMAT;
    DecimalFormat twoDForm = new DecimalFormat("#.##");
	
    public enum CURRENT_VIEW {
        DEFAULT('D'), BENCH('B'), SQUAT('S'), OHP('O'), FIVE('5'), THREE('3'), ONE('1');
        private int value;

        private CURRENT_VIEW(char value) {
                this.value = value;
        }
    };   

    CURRENT_VIEW curView = CURRENT_VIEW.DEFAULT;

	//initialize processor to process all lifts, dates, etc...
	DateProcessor Processor = new DateProcessor();

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_third);
		//declare our button, tie it to listener, code listener

		

		CheckBox optionsCheckBox = (CheckBox) findViewById(R.id.optionsCheckBox);
		
		optionsCheckBox.setOnCheckedChangeListener(optionsCheckBoxListener);
		
		//Data output
		OUTPUT = (TextView) findViewById(R.id.output);
		
		
		Intent intent = getIntent();

		String startingDate = intent.getStringExtra("key2");
		

	    eventsData = new EventsDataSQLHelper(this);
	    SQLiteDatabase db = eventsData.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
	    db.delete("Lifts", null, null);
	    
	    
	  //determine whether to round or not
	  String areWeGoingToRound = intent.getStringExtra("round");
	  String roundMode = "error"; // if this is not changed from error, there was an error 
	  if (areWeGoingToRound.equals("true"))	
	  {
	  Processor.setRoundingFlag(true);
	  roundMode = "Rounded";
	  }
	  else //revert to the default of no round
	  {
      Processor.setRoundingFlag(false);
	  roundMode = "Unrounded";  
	  }
	    
	    //get unit mode
	    String lbmode = intent.getStringExtra("mode");
	    if (lbmode.length() > 1)
	    	{
	    	setModeFormat(roundMode + " " +  lbmode);
	    	
	    	}

	    

	    Processor.setUnitMode(lbmode);
	    
	    
		
	    //set starting lifts (separate strings so title can them too) 
		String startingBench = intent.getStringExtra("bench");
		String startingSquat = intent.getStringExtra("squat");
		String startingOHP   = intent.getStringExtra("OHP");
		String startingDead  = intent.getStringExtra("dead");
	    
		//also set starting lifts locally 

		
	    Processor.setStartingLifts(startingBench, startingSquat, startingOHP, startingDead);
	    Processor.setStartingDate(startingDate);
	    
	    
	    int numberCycles =  Integer.parseInt(intent.getStringExtra("numberCycles"));
	   for (int i=0; i < numberCycles; i++) 
	   {
	    calculateCycle();
	    Processor.incrementCycleAndUpdateTMs();
	   }
	  
	   
	   
	   	if (curView.equals(CURRENT_VIEW.DEFAULT))
	   	{
	    Cursor cursor = getEvents();
	    showEvents(cursor);
	   	}

	}//end method oncreate 


	 @Override
	  public void onDestroy() {
		 super.onDestroy();
		 
	    eventsData.close();
	  }
	 
	 
	 private OnCheckedChangeListener optionsCheckBoxListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
		    //Options menu -- wrap up in a checkbox Listener 
		    CharSequence colors[] = new CharSequence[] {"Adjust Lifts", "Reset", "Export...", "View By...", "Back"};

		    AlertDialog.Builder builder = new AlertDialog.Builder(ThirdScreen.this);
		    builder.setTitle("Pick a color");
		    builder.setItems(colors, new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	if (which == 0){
		        		onBackPressed();
		        		}
		            if (which == 1) // arrays are zero indexed
		            	backToFirst();
		            
		           //if (which== 4) 
		            	//no need to worry about Back case, takes care of itself
		        }
		    });
		    builder.show();
			
		}};
		
			
		public void setModeFormat (String myFormat )
		{
			MODE_FORMAT = "Mode: " + myFormat;
		}
		
		String getModeFormat ()
		{
			return MODE_FORMAT; 
		}

		//pass back title argument if things go awry 
		public void addEvent() {
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
		    StringBuilder ret = new StringBuilder("Starting TMs [Bench: " + Processor.getStartingBench() + "] [Squat: " + Processor.getStartingSquat() + "] [OHP: " + Processor.getStartingOHP() + "] [Dead: " + Processor.getStartingDead() + "] " + getModeFormat() + "\n\n");
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
		    OUTPUT.setText(ret);
		  }
		


		private void backToFirst()
		{
			startActivity(new Intent(this, MainActivity.class));
			SQLiteDatabase db = eventsData.getWritableDatabase();
			db.delete("Lifts", null, null);
		}
		
		private void backToSecond()
		{
			startActivity(new Intent(this, SecondScreen.class));
			SQLiteDatabase db = eventsData.getWritableDatabase();
			db.delete("Lifts", null, null);
		}
		
		
		
		private void calculateCycle()
		{
			
			
			//SparseArray benchFives = new SparseArray()
			//bench fives
			Processor.calculateFivesDay(Processor.getBenchTM());
	
			addEvent();
			Processor.increment();
			
			//Squat fives
			Processor.calculateFivesDay(Processor.getSquatTM());
			addEvent();
			Processor.incrementRest();
			
			//OHP Fives 
			Processor.calculateFivesDay(Processor.getOHPTM());
			addEvent();
			Processor.increment();
			
			//Dead Fives
			Processor.calculateFivesDay(Processor.getDeadTM());
			addEvent();
			Processor.incrementRest();
			
			//bench triples
			Processor.calculateTriplesDay(Processor.getBenchTM());
			addEvent();
			Processor.increment();
			
			
			//Squat triples
			Processor.calculateTriplesDay(Processor.getSquatTM());
			addEvent();
			Processor.incrementRest();
			
			//OHP triples 
			Processor.calculateTriplesDay(Processor.getOHPTM());
			addEvent();
			Processor.increment();
			
			//Dead triples
			Processor.calculateFivesDay(Processor.getDeadTM());
			addEvent();
			Processor.incrementRest();
			
			//bench single
			Processor.calculateSingleDay(Processor.getBenchTM());
			addEvent();
			Processor.increment();
			
			//Squat single
			Processor.calculateSingleDay(Processor.getSquatTM());
			addEvent();
			Processor.incrementRest();
			
			//OHP single 
			Processor.calculateSingleDay(Processor.getOHPTM());
			addEvent();
			Processor.increment();
			
			//Dead single
			Processor.calculateSingleDay(Processor.getDeadTM());
			addEvent();
			Processor.incrementRest();

			
		}
		
		double roundtoTwoDecimals(double d) 
		{
        return Double.valueOf(twoDForm.format(d));
		}
		





}//end thirdscreen activiy 





