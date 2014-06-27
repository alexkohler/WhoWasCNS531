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
import android.widget.Toast;


//small victories :) 


public class ThirdScreen extends Activity {

	EventsDataSQLHelper eventsData;
	TextView OUTPUT;
	String MODE_FORMAT;
    DecimalFormat twoDForm = new DecimalFormat("#.##");
    Integer NUMBER_CYCLES;
	
    public enum CURRENT_VIEW {
        DEFAULT('D'), BENCH('B'), SQUAT('S'), OHP('O'), DEAD('D'), FIVES('5'), THREES('3'), ONES('1');
        @SuppressWarnings("unused")
		private int value;

        private CURRENT_VIEW(char value) {
                this.value = value;
        }
        

    };   

    CURRENT_VIEW curView = CURRENT_VIEW.DEFAULT;//start with default (show all) view

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
	    setNumberCycles(numberCycles);
	    calculateCycle();
	    
	   
	  
	   
	   

	    Cursor cursor = getEvents();
	    showDefaultEvents(cursor);

	}//end method oncreate 

	public void setNumberCycles(int numberCycles)
	{
		NUMBER_CYCLES = numberCycles;
	}
	
	public int getNumberCycles()
	{
		return NUMBER_CYCLES;
	}

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
		    builder.setTitle("Options menu");
		    builder.setItems(colors, new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	if (which == 0){
		        		onBackPressed();
		        		}
		            if (which == 1) // arrays are zero indexed
		            	backToFirst();
		            
		            if (which == 3)
		            	{
		            	createViewBuilder();
		            	}
		           //if (which== 4) 
		            	//no need to worry about Back case, takes care of itself
		        }
		    });
		    builder.show();
			
		}};
		
		//ugly but will clean up later..
		public void createViewBuilder()
		{
			 CharSequence colors[] = new CharSequence[] {"Show all", "Bench Only", "Squat Only", "OHP Only", "Deadlift only", "5-5-5 Days only", "3-3-3 Days only", "5-3-1 days only", "Back"};
		
			 AlertDialog.Builder builder2 = new AlertDialog.Builder(ThirdScreen.this);
			    builder2.setTitle("Show by:");

			    builder2.setItems(colors, new DialogInterface.OnClickListener() {
			        @Override
			        public void onClick(DialogInterface dialog, int which) {
		        		Bundle tempBundle = new Bundle();
			        	switch (which){
			        	case 0:
			        		curView = CURRENT_VIEW.DEFAULT;
			        		Toast.makeText(ThirdScreen.this, "View Selected: Show All", Toast.LENGTH_SHORT).show();
			        		onCreate(tempBundle);
			        		break;
			        	case 1:
			        		curView = CURRENT_VIEW.BENCH;
			        		Toast.makeText(ThirdScreen.this, "View Selected: Bench Only", Toast.LENGTH_SHORT).show();
			        		onCreate(tempBundle);
			        		break;	
			        	case 2:
			        		curView = CURRENT_VIEW.SQUAT;
			        		Toast.makeText(ThirdScreen.this, "View Selected: Squat Only", Toast.LENGTH_SHORT).show();
			        		onCreate(tempBundle);
			        		break;	
			        	case 3:
			        		curView = CURRENT_VIEW.OHP;
			        		Toast.makeText(ThirdScreen.this, "View Selected: OHP Only", Toast.LENGTH_SHORT).show();
			        		onCreate(tempBundle);
			        		break;		
			        	case 4:
			        		curView = CURRENT_VIEW.DEAD;
			        		Toast.makeText(ThirdScreen.this, "View Selected: Deadlift Only", Toast.LENGTH_SHORT).show();
			        		onCreate(tempBundle);
			        		break;
			        	case 5:
			        		curView = CURRENT_VIEW.FIVES;
			        		Toast.makeText(ThirdScreen.this, "View Selected: Fives Days Only", Toast.LENGTH_SHORT).show();
			        		onCreate(tempBundle);
			        		break;
			        	case 6:
			        		curView = CURRENT_VIEW.THREES;
			        		Toast.makeText(ThirdScreen.this, "View Selected: Triples Days Only", Toast.LENGTH_SHORT).show();
			        		onCreate(tempBundle);
			        		break;
			        	case 7:
			        		curView = CURRENT_VIEW.ONES;
			        		Toast.makeText(ThirdScreen.this, "View Selected: 5-3-1 Days Only", Toast.LENGTH_SHORT).show();
			        		onCreate(tempBundle);
			        		break;				
			        	case 8:
			            	CharSequence colors[] = new CharSequence[] {"Adjust Lifts", "Reset", "Export...", "View By...", "Back"};

			    		    AlertDialog.Builder builder = new AlertDialog.Builder(ThirdScreen.this);
			    		    builder.setTitle("Options menu");
			    		    builder.setItems(colors, new DialogInterface.OnClickListener() {
			    		        @Override
			    		        public void onClick(DialogInterface dialog, int which) {
			    		        	if (which == 0){
			    		        		onBackPressed();
			    		        		}
			    		            if (which == 1) // arrays are zero indexed
			    		            	backToFirst();
			    		            
			    		            if (which == 3)
			    		            	{
			    		            	createViewBuilder();
			    		            	}
			    		           //if (which== 4) 
			    		            	//no need to worry about Back case, takes care of itself
			    		        }//end inner onClick 
			    		    });//end inner which listener
			    		    builder.show();	
			    		    break;//break 8
			    		    

			        }//end switch statement
			        }//end outter onclick
			    });//end outter which listener
			        
			        builder2.show();
			        
		}//end createViewBuilder
		
		
			
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

		  private void showDefaultEvents(Cursor cursor) {
		    StringBuilder ret = new StringBuilder("Starting TMs [Bench: " + Processor.getStartingBench() + "] [Squat: " + Processor.getStartingSquat() + "] [OHP: " + Processor.getStartingOHP() + "] [Dead: " + Processor.getStartingDead() + "] " + "\n" + getModeFormat() + " " + "View Mode: " + curView.toString() + "\n");
		    while (cursor.moveToNext()) {
		      //long id = cursor.getLong(0);
		      String liftDate = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.LIFTDATE));
		      String cycle = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.CYCLE));
		      String lift = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.LIFT));
		      String freq = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.FREQUENCY));
		      Double first = roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.FIRST)));
		      Double second = roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.SECOND)));
		      Double third = roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.THIRD)));
		      switch (curView){
		      case DEFAULT:
		    	  ret.append(liftDate + "| " + cycle + "| " + lift + "| " + freq + "| " + first + "| " + second + "| " + third + "\n");
		    	  break;
		      case BENCH: 
		    	if (lift.equals("Bench"))
			      ret.append(liftDate + "| " + cycle + "| " + lift + "| " + freq + "| " + first + "| " + second + "| " + third + "\n");	  
		    	  break;
		      case SQUAT:
			    if (lift.equals("Squat"))
				  ret.append(liftDate + "| " + cycle + "| " + lift + "| " + freq + "| " + first + "| " + second + "| " + third + "\n");	  
				  break;
		      case OHP:
				if (lift.equals("OHP"))
				  ret.append(liftDate + "| " + cycle + "| " + lift + "| " + freq + "| " + first + "| " + second + "| " + third + "\n");	  
				  break;
		      case DEAD:
				if (lift.equals("Dead"))
				  ret.append(liftDate + "| " + cycle + "| " + lift + "| " + freq + "| " + first + "| " + second + "| " + third + "\n");	  
				  break;
		      case FIVES:
				if (freq.equals("5-5-5"))
				  ret.append(liftDate + "| " + cycle + "| " + lift + "| " + freq + "| " + first + "| " + second + "| " + third + "\n");	  
				  break;
		      case THREES:
				if (freq.equals("3-3-3"))
				  ret.append(liftDate + "| " + cycle + "| " + lift + "| " + freq + "| " + first + "| " + second + "| " + third + "\n");	  
				  break;
		      case ONES:
				if (freq.equals("5-3-1"))
				  ret.append(liftDate + "| " + cycle + "| " + lift + "| " + freq + "| " + first + "| " + second + "| " + third + "\n");	  
				  break;  
		      
		      }//end curview switch
		    }
		    OUTPUT.setText(ret);
		  }
		


		private void backToFirst()
		{
			startActivity(new Intent(this, MainActivity.class));
			SQLiteDatabase db = eventsData.getWritableDatabase();
			db.delete("Lifts", null, null);
		}		
		
		
		private void calculateCycle()
		{
			
			
			Processor.setCycle(1);
		  for (int i=0; i < getNumberCycles(); i++) 
			{
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

			Processor.incrementCycleAndUpdateTMs();
			 }
			
		}
		
		double roundtoTwoDecimals(double d) 
		{
        return Double.valueOf(twoDForm.format(d));
		}
		





}//end thirdscreen activiy 





