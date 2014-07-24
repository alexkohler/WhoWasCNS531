package com.example.whowascns;



import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


//small victories :) 


public class ThirdScreen extends Activity {

	EventsDataSQLHelper eventsData;
	TextView OUTPUT;
	String MODE_FORMAT;
	DecimalFormat twoDForm = new DecimalFormat("#.##");
	Integer NUMBER_CYCLES;
	static String CURRENT_SELECT_QUERY;
	static int lbMode;
	boolean insertStatus = false;
	boolean changedView = false;
	static boolean tableColorToggle = true;
	String retStringSaver; //for sake of changing views
	Cursor cursor;
	String lbmode;

	public enum CURRENT_VIEW {
		DEFAULT('D'), BENCH('B'), SQUAT('S'), OHP('O'), DEAD('D'), FIVES('5'), THREES('3'), ONES('1');
		@SuppressWarnings("unused")
		private int value;

		private CURRENT_VIEW(char value) {
			this.value = value;
		}


	};   


	public enum CURRENT_FREQ
	{
		FIVES ('F'), THREES ('T'), ONES ('O');

		@SuppressWarnings("unused")
		private int value;

		private CURRENT_FREQ(char value) {
			this.value = value;
		}
	}

	static CURRENT_VIEW curView = CURRENT_VIEW.DEFAULT;//start with default (show all) view (for overview)


	CURRENT_VIEW individualCV; //for individual view
	CURRENT_FREQ individualFreq; //^^

	//initialize processor to process all lifts, dates, etc...
	DateProcessor Processor = new DateProcessor();

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_third);

		Button configureButton = (Button) findViewById(R.id.configureButton);
		
		configureButton.setBackgroundColor(Color.GRAY);
		configureButton.setTextColor(Color.WHITE);

		configureButton.setOnClickListener(optionsListener);

		Intent intent = getIntent();

		String startingDate = intent.getStringExtra("key2");

		
		eventsData = new EventsDataSQLHelper(this);
		SQLiteDatabase db = eventsData.getWritableDatabase(); // helper is object extends SQLiteOpenHelper

		//if we are coming from second screen
		String origin = intent.getStringExtra("origin");
		if (origin.equals("individualView"))
		{
			String view = intent.getStringExtra("viewMode");
			TableLayout tableRowPrincipal = (TableLayout)findViewById(R.id.tableLayout1);
			switch(view)
			{//		DEFAULT('D'), BENCH('B'), SQUAT('S'), OHP('O'), DEAD('D'), FIVES('5'), THREES('3'), ONES('1');
				case "DEFAULT":
					setQuery(null);
					tableRowPrincipal.removeAllViews();
					cursor = getEvents();
					showDefaultEvents(cursor);
					break;
				case "BENCH":
					setQuery("Lift = 'Bench'");
					tableRowPrincipal.removeAllViews();
					cursor = getEvents();
					insertStatus = false;
					showDefaultEvents(cursor);
					break;
				case "SQUAT":
					setQuery("Lift = 'Squat'");
					tableRowPrincipal.removeAllViews();
					cursor = getEvents();
					insertStatus = false;
					showDefaultEvents(cursor);
					break;
				case "OHP":
					setQuery("Lift = 'OHP'");
					tableRowPrincipal.removeAllViews();
					cursor = getEvents();
					insertStatus = false;
					showDefaultEvents(cursor);
					break;
				case "DEAD":
					setQuery("Lift = 'Deadlift'");
					tableRowPrincipal.removeAllViews();
					cursor = getEvents();
					insertStatus = false;
					showDefaultEvents(cursor);
					break;	
				case "FIVES":
					setQuery("Frequency = '5-5-5'");
					tableRowPrincipal.removeAllViews();
					cursor = getEvents();
					insertStatus = false;
					showDefaultEvents(cursor);
					break;	
				case "THREES":
					setQuery("Frequency = '3-3-3'");
					tableRowPrincipal.removeAllViews();
					cursor = getEvents();
					insertStatus = false;
					showDefaultEvents(cursor);
					break;
				case "ONES":
					setQuery("Frequency = '5-3-1'");
					tableRowPrincipal.removeAllViews();
					cursor = getEvents();
					insertStatus = false;
					showDefaultEvents(cursor);
					break;	

			}
		}
		
		
		else if (origin.equals("second"))
		{

			db.delete("Lifts", null, null);
			setQuery(null);
			//determine whether to round or not
			String areWeGoingToRound = intent.getStringExtra("round");
			String roundMode = "error"; // if this is not changed from error, there was an error 
			if (areWeGoingToRound.equals("true"))	
				Processor.setRoundingFlag(true);
			
			else //revert to the default of no round
				Processor.setRoundingFlag(false);
			

			//get unit mode
			lbmode = intent.getStringExtra("mode");
			if (lbmode.length() > 1)
			{
				setMode(lbmode);

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
			new AsyncCaller().execute();
		}



		else
		{
			Cursor cursor = getEvents();
			showDefaultEvents(cursor);
		}
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	private OnClickListener optionsListener = new OnClickListener () {

		@Override
		public void onClick(View v) {
			//Options menu -- wrap up in a checkbox Listener 
			CharSequence colors[] = new CharSequence[] {"Plate Config", "Adjust Lifts", "Reset", "Export...", "View By...", "Back"};

			AlertDialog.Builder builder = new AlertDialog.Builder(ThirdScreen.this);
			builder.setTitle("Options menu");
			//final LinearLayout myLL = (LinearLayout)findViewById(R.id.linearLayout1);
			builder.setItems(colors, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which==0)
					{
						CharSequence modes[] = new CharSequence[] {"Pounds", "Kilograms"};
						AlertDialog.Builder builder = new AlertDialog.Builder(ThirdScreen.this);
						builder.setTitle("Unit menu");
						builder.setItems(modes, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (which == 0){
								    final CharSequence[] lbPlates = {" 45lb "," 35lb "," 25lb "," 10lb ", " 5lb "," 2.5lb "};
								    // arraylist to keep the selected items
								    final ArrayList<Integer> seletedItems=new ArrayList<Integer>();
								    
								    AlertDialog.Builder builder = new AlertDialog.Builder(ThirdScreen.this);
								    builder.setTitle("What plates does your gym have?");
								    builder.setMultiChoiceItems(lbPlates, null,
								            new DialogInterface.OnMultiChoiceClickListener() {
								     @Override
								     public void onClick(DialogInterface dialog, int indexSelected,
								             boolean isChecked) {
								         if (isChecked) {
								             // If the user checked the item, add it to the selected items
								             seletedItems.add(indexSelected);
								         } else if (seletedItems.contains(indexSelected)) {
								             // Else, if the item is already in the array, remove it
								             seletedItems.remove(Integer.valueOf(indexSelected));
								         }
								     }
								 })
								  // Set the action buttons
								 .setPositiveButton("OK", new DialogInterface.OnClickListener() {
								     @Override
								     public void onClick(DialogInterface dialog, int id) {
								         //  Your code when user clicked on OK
								         //  You can write the code  to save the selected item here
								    	 for (int indexSelected : seletedItems)
								    	 {
								    		 switch (indexSelected)
								    		 {
								    		 case 0:
								    		 	Boolean lbHaveFortyFive = true;//TODO fix logic 
								    			break;
								    		 case 1:	
								    		 	Boolean lbHaveThirtyFive = true;
								    			break;
								    		 case 2:	
								    			Boolean lbHaveTwentyFive = true;
								    			break;
								    		 case 3:	
								    			Boolean lbHaveTen = true;
								    			break;
								    		 case 4:	
								    			Boolean lbHaveFive = true;
								    			break;
								    		 case 5:	
								    			Boolean lbHaveTwoPointFive = true;
								    			break;
								    		 }
								    	 }

								     }
								 })
								 .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								     @Override
								     public void onClick(DialogInterface dialog, int id) {
								        //  Your code when user clicked on Cancel

								     }
								 });

								    dialog = builder.create();//AlertDialog dialog; create like this outside onClick
								    ((Dialog) dialog).show();
								}
								if (which == 1)
								{// arrays are zero indexed
								    final CharSequence[] kgPlates = {" 25kg "," 20kg "," 15kg "," 10kg "," 5kg ", " 2.5kg "," 1.25kg "};
								    // arraylist to keep the selected items
								    final ArrayList<Integer> seletedItems=new ArrayList<Integer>();

								    AlertDialog.Builder builder = new AlertDialog.Builder(ThirdScreen.this);
								    builder.setTitle("What plates does your gym have?");
								    builder.setMultiChoiceItems(kgPlates, null,
								            new DialogInterface.OnMultiChoiceClickListener() {
								     @Override
								     public void onClick(DialogInterface dialog, int indexSelected,
								             boolean isChecked) {
								         if (isChecked) {
								             // If the user checked the item, add it to the selected items
								             seletedItems.add(indexSelected);
								         } else if (seletedItems.contains(indexSelected)) {
								             // Else, if the item is already in the array, remove it
								             seletedItems.remove(Integer.valueOf(indexSelected));
								         }
								     }
								 })
								  // Set the action buttons
								 .setPositiveButton("OK", new DialogInterface.OnClickListener() {
								     @Override
								     public void onClick(DialogInterface dialog, int id) {
								         //  Your code when user clicked on OK
								         //  You can write the code  to save the selected item here
								    	 for (int indexSelected : seletedItems)
								    	 {
								    		switch (indexSelected)
								    		{
								    		case 0:
									    		Boolean kgHaveTwentyFive = true;
									    		break;
								    		case 1:
									    		Boolean kgHaveTwenty = true;
									    		break;
								    		case 2:
									    		Boolean kgHaveFifteen = true;
									    		break;
								    		case 3:
								    			Boolean kgHaveTen  = true;
								    			break;
								    		case 4:	
								    			Boolean kgHaveFive  = true;
								    			break;
								    		case 5:
								    			Boolean kgHaveTwoPointFive = true;
								    			break;
								    		case 6:
								    			Boolean kgHaveOnePointTwoFive = true;
								    			break;
								    		}
								    	 }

								     }
								 })
								 .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								     @Override
								     public void onClick(DialogInterface dialog, int id) {
								        //  Your code when user clicked on Cancel

								     }
								 });

								    dialog = builder.create();//AlertDialog dialog; create like this outside onClick
								    ((Dialog) dialog).show();	
									
								}

				
							}//end inner onClick 
						});//end inner which listener
						builder.show();	
					}
					
					
					if (which == 1){
						SQLiteDatabase db = eventsData.getWritableDatabase();
						Intent myIntent = new Intent(ThirdScreen.this, SecondScreen.class);
						myIntent.putExtra("origin", "third");
						String[] intentDataArray = new String[6];
						intentDataArray = getSecondScreenData(intentDataArray);
						myIntent.putExtra("bench", intentDataArray[0]);
						myIntent.putExtra("squat", intentDataArray[1]);
						myIntent.putExtra("ohp", intentDataArray[2]);
						myIntent.putExtra("dead", intentDataArray[3]);
						db.delete("Lifts", null, null);
						startActivity(myIntent);
					}
					if (which == 2) 
					{// arrays are zero indexed
						SQLiteDatabase db = eventsData.getWritableDatabase();
						curView = CURRENT_VIEW.DEFAULT;
						db.delete("Lifts", null, null);
						backToFirst();

					}
					//nothing for export yet
					if (which == 4)
					{
						createViewBuilder();
					}
					//if (which== 4) 
					//no need to worry about Back case, takes care of itself
				}

				private String[] getSecondScreenData(String[] intentDataArray) {
					SQLiteDatabase db = eventsData.getWritableDatabase();
					setQuery("Lift = 'Bench' AND Cycle = '1'"); //more specific query to leave room for customization down the road (order of lifts may not always be the same) 
					Cursor myCursor = getEvents(); //vladdy
					myCursor.moveToNext();
					String benchTM  = myCursor.getString(myCursor.getColumnIndex(EventsDataSQLHelper.TRAINING_MAX));
					setQuery("Lift = 'Squat' AND Cycle = '1'"); //more specific query to leave room for customization down the road (order of lifts may not always be the same) 
					myCursor = getEvents(); //vladdy
					myCursor.moveToNext();
					String squatTM  = myCursor.getString(myCursor.getColumnIndex(EventsDataSQLHelper.TRAINING_MAX));
					setQuery("Lift = 'OHP' AND Cycle = '1'"); //more specific query to leave room for customization down the road (order of lifts may not always be the same) 
					myCursor = getEvents(); //vladdy
					myCursor.moveToNext();
					String ohpTM  = myCursor.getString(myCursor.getColumnIndex(EventsDataSQLHelper.TRAINING_MAX));
					setQuery("Lift = 'Deadlift' AND Cycle = '1'"); //more specific query to leave room for customization down the road (order of lifts may not always be the same) 
					myCursor = getEvents(); //vladdy
					myCursor.moveToNext();
					String deadTM  = myCursor.getString(myCursor.getColumnIndex(EventsDataSQLHelper.TRAINING_MAX));
					
					intentDataArray[0] = benchTM;
					intentDataArray[1] = squatTM;
					intentDataArray[2] = ohpTM;
					intentDataArray[3] = deadTM;
					
					return intentDataArray;
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
					Cursor cursor = getEvents();
					TableLayout tableRowPrincipal = (TableLayout)findViewById(R.id.tableLayout1);
					switch (which){
					case 0:
						curView = CURRENT_VIEW.DEFAULT;
						Toast.makeText(ThirdScreen.this, "View Selected: Show All", Toast.LENGTH_SHORT).show();
						setQuery(null);
						tableRowPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;
					case 1:
						curView = CURRENT_VIEW.BENCH;
						Toast.makeText(ThirdScreen.this, "View Selected: Bench Only", Toast.LENGTH_SHORT).show();
						setQuery("Lift = 'Bench'");
						tableRowPrincipal.removeAllViews(); //try something like this 
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;	
					case 2:
						curView = CURRENT_VIEW.SQUAT;
						Toast.makeText(ThirdScreen.this, "View Selected: Squat Only", Toast.LENGTH_SHORT).show();
						setQuery("Lift = 'Squat'");
						tableRowPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true; 
						showDefaultEvents(cursor);
						break;	
					case 3:
						curView = CURRENT_VIEW.OHP;
						Toast.makeText(ThirdScreen.this, "View Selected: OHP Only", Toast.LENGTH_SHORT).show();
						setQuery("Lift = 'OHP'");
						tableRowPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;		
					case 4:
						curView = CURRENT_VIEW.DEAD;
						Toast.makeText(ThirdScreen.this, "View Selected: Deadlift Only", Toast.LENGTH_SHORT).show();
						setQuery("Lift = 'Deadlift'");
						tableRowPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;
					case 5:
						curView = CURRENT_VIEW.FIVES;
						Toast.makeText(ThirdScreen.this, "View Selected: Fives Days Only", Toast.LENGTH_SHORT).show();
						setQuery("Frequency = '5-5-5'");
						tableRowPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;
					case 6:
						curView = CURRENT_VIEW.THREES;
						Toast.makeText(ThirdScreen.this, "View Selected: Triples Days Only", Toast.LENGTH_SHORT).show();
						setQuery("Frequency = '3-3-3'");
						tableRowPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;
					case 7:
						curView = CURRENT_VIEW.ONES;
						Toast.makeText(ThirdScreen.this, "View Selected: 5-3-1 Days Only", Toast.LENGTH_SHORT).show();
						setQuery("Frequency = '5-3-1'");
						tableRowPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
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

		
		
		//modeBuilder
		private OnClickListener kgLbConfig = new OnClickListener () {
			CharSequence modes[] = new CharSequence[] {"Pounds", "Kilograms"};
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ThirdScreen.this);
				builder.setTitle("Unit menu");
				builder.setItems(modes, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0){
						    final CharSequence[] lbPlates = {" 45lb "," 35lb "," 25lb "," 10lb ", " 5lb "," 2.5lb "};
						    // arraylist to keep the selected items
						    final ArrayList<Integer> seletedItems=new ArrayList<Integer>();
						    
						    AlertDialog.Builder builder = new AlertDialog.Builder(ThirdScreen.this);
						    builder.setTitle("What plates does your gym have?");
						    builder.setMultiChoiceItems(lbPlates, null,
						            new DialogInterface.OnMultiChoiceClickListener() {
						     @Override
						     public void onClick(DialogInterface dialog, int indexSelected,
						             boolean isChecked) {
						         if (isChecked) {
						             // If the user checked the item, add it to the selected items
						             seletedItems.add(indexSelected);
						         } else if (seletedItems.contains(indexSelected)) {
						             // Else, if the item is already in the array, remove it
						             seletedItems.remove(Integer.valueOf(indexSelected));
						         }
						     }
						 })
						  // Set the action buttons
						 .setPositiveButton("OK", new DialogInterface.OnClickListener() {
						     @Override
						     public void onClick(DialogInterface dialog, int id) {
						         //  Your code when user clicked on OK
						         //  You can write the code  to save the selected item here
						    	 for (int indexSelected : seletedItems)
						    	 {
						    		 switch (indexSelected)
						    		 {
						    		 case 0:
						    		 	Boolean lbHaveFortyFive = true;
						    			break;
						    		 case 1:	
						    		 	Boolean lbHaveThirtyFive = true;
						    			break;
						    		 case 2:	
						    			Boolean lbHaveTwentyFive = true;
						    			break;
						    		 case 3:	
						    			Boolean lbHaveTen = true;
						    			break;
						    		 case 4:	
						    			Boolean lbHaveFive = true;
						    			break;
						    		 case 5:	
						    			Boolean lbHaveTwoPointFive = true;
						    			break;
						    		 }
						    	 }

						     }
						 })
						 .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						     @Override
						     public void onClick(DialogInterface dialog, int id) {
						        //  Your code when user clicked on Cancel

						     }
						 });

						    dialog = builder.create();//AlertDialog dialog; create like this outside onClick
						    ((Dialog) dialog).show();
						}
						if (which == 1)
						{// arrays are zero indexed
						    final CharSequence[] kgPlates = {" 25kg "," 20kg "," 15kg "," 10kg "," 5kg ", " 2.5kg "," 1.25kg "};
						    // arraylist to keep the selected items
						    final ArrayList<Integer> seletedItems=new ArrayList<Integer>();

						    AlertDialog.Builder builder = new AlertDialog.Builder(ThirdScreen.this);
						    builder.setTitle("What plates does your gym have?");
						    builder.setMultiChoiceItems(kgPlates, null,
						            new DialogInterface.OnMultiChoiceClickListener() {
						     @Override
						     public void onClick(DialogInterface dialog, int indexSelected,
						             boolean isChecked) {
						         if (isChecked) {
						             // If the user checked the item, add it to the selected items
						             seletedItems.add(indexSelected);
						         } else if (seletedItems.contains(indexSelected)) {
						             // Else, if the item is already in the array, remove it
						             seletedItems.remove(Integer.valueOf(indexSelected));
						         }
						     }
						 })
						  // Set the action buttons
						 .setPositiveButton("OK", new DialogInterface.OnClickListener() {
						     @Override
						     public void onClick(DialogInterface dialog, int id) {
						         //  Your code when user clicked on OK
						         //  You can write the code  to save the selected item here
						    	 for (int indexSelected : seletedItems)
						    	 {
						    		switch (indexSelected)
						    		{
						    		case 0:
							    		Boolean kgHaveTwentyFive = true;
							    		break;
						    		case 1:
							    		Boolean kgHaveTwenty = true;
							    		break;
						    		case 2:
							    		Boolean kgHaveFifteen = true;
							    		break;
						    		case 3:
						    			Boolean kgHaveTen  = true;
						    			break;
						    		case 4:	
						    			Boolean kgHaveFive  = true;
						    			break;
						    		case 5:
						    			Boolean kgHaveTwoPointFive = true;
						    			break;
						    		case 6:
						    			Boolean kgHaveOnePointTwoFive = true;
						    			break;
						    		}
						    	 }

						     }
						 })
						 .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						     @Override
						     public void onClick(DialogInterface dialog, int id) {
						        //  Your code when user clicked on Cancel

						     }
						 });

						    dialog = builder.create();//AlertDialog dialog; create like this outside onClick
						    ((Dialog) dialog).show();	
							
						}

		
					}//end inner onClick 
				});//end inner which listener
				builder.show();	
				
			}};


		public void setMode (String myMode )
		{
			MODE_FORMAT = myMode;
		}

		String getModeFormat ()
		{
			return MODE_FORMAT; 
		}


		public void addEvent() {
			SQLiteDatabase db = eventsData.getWritableDatabase();
			ContentValues values = new ContentValues();
			//db.execSQL("ALTER TABLE Lifts ADD COLUMN column_lbFlag integer");
			int sqlLitelbMode = 3; //booleans in sqllite are represented by 1 and 0
			if (getModeFormat().contains("Lbs"))
			sqlLitelbMode = 1;
			if (getModeFormat().contains("Kgs")) //TODO change mode format just to hold raw data we need i think..
			sqlLitelbMode = 0;
			
			
			
				
			

			values.put(EventsDataSQLHelper.LIFTDATE, Processor.getStartingDate() );
			values.put(EventsDataSQLHelper.CYCLE, Processor.getCycle());
			values.put(EventsDataSQLHelper.LIFT, Processor.getLiftType());
			values.put(EventsDataSQLHelper.FREQUENCY, Processor.getFreq());
			values.put(EventsDataSQLHelper.FIRST, Processor.getFirstLift());
			values.put(EventsDataSQLHelper.SECOND, Processor.getSecondLift());
			values.put(EventsDataSQLHelper.THIRD, Processor.getThirdLift());
			if ((Processor.getLiftType().equals("Bench")) && Processor.getCycle() == 1) //insert our initial training maxes into table instead of trying to pass them back and forth between intents 
				{
				values.put(EventsDataSQLHelper.TRAINING_MAX, Processor.getBenchTM());
				values.put(EventsDataSQLHelper.LBFLAG, sqlLitelbMode);
				}//(the first entry of each lift has a value in the "training_max" column for sake of easily generating title between changing views)
				
			if (Processor.getLiftType().equals("Squat") && Processor.getCycle() == 1 )	  		
			{
				values.put(EventsDataSQLHelper.TRAINING_MAX, Processor.getSquatTM());   
				values.put(EventsDataSQLHelper.LBFLAG, sqlLitelbMode);
			}
			if (Processor.getLiftType().equals("OHP") && Processor.getCycle() == 1 )	   
			{
				values.put(EventsDataSQLHelper.TRAINING_MAX, Processor.getOHPTM());
				values.put(EventsDataSQLHelper.LBFLAG, sqlLitelbMode);
			}
			if (Processor.getLiftType().equals("Deadlift") && Processor.getCycle() == 1 )	   
			{
				values.put(EventsDataSQLHelper.TRAINING_MAX, Processor.getDeadTM());
				values.put(EventsDataSQLHelper.LBFLAG, sqlLitelbMode);
			}
			db.insert(EventsDataSQLHelper.TABLE, null, values);
		}

		public void setQuery(String myQuery)
		{
			CURRENT_SELECT_QUERY = myQuery;
		}

		public String getQuery ()
		{
			return CURRENT_SELECT_QUERY;
		}

		@SuppressWarnings("deprecation")
		private Cursor getEvents() {
			SQLiteDatabase db = eventsData.getReadableDatabase();
			Cursor cursor = db.query(EventsDataSQLHelper.TABLE, null, getQuery(), null, null,
					null, null);

			startManagingCursor(cursor);
			return cursor;
		}
		
		static String test = null;
		
		static void setTest (String myString)
		{
			test = myString;
		}
		
		static String getTest()
		{
			return test;
		}

		@SuppressWarnings("deprecation")
		private void showDefaultEvents(Cursor cursor) {
			StringBuilder ret; 
			ret = new StringBuilder("");
			TableLayout tableRowPrincipal = (TableLayout)findViewById(R.id.tableLayout1);
			while (cursor.moveToNext()) {
				if (!this.insertStatus){//has title been inserted?
					String temp = getQuery(); //temporarily hold query
					setQuery(null);
					Cursor subcursor = getEvents();//create a subcursor to get our training maxes 
					ret = new StringBuilder("Start TMs [Bench: " + cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.TRAINING_MAX)) + "]");
					subcursor.moveToNext();
					ret.append(" [Squat: " +cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.TRAINING_MAX)) + "]");
					subcursor.moveToNext();
					ret.append(" [OHP: " + cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.TRAINING_MAX)) + "]" );
					subcursor.moveToNext();
					ret.append(" [Dead: " + cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.TRAINING_MAX)) + "]" );
					lbMode = cursor.getInt((cursor.getColumnIndex(EventsDataSQLHelper.LBFLAG)));
					if (lbMode == 1)
					ret.append("	Mode: lbs");
					else if (lbMode == 0 )
					ret.append("	Mode: kgs");
					else
					ret.append("Mode error");	
					cursor.moveToFirst();
					insertStatus = true;
					retStringSaver = ret.toString();
					setQuery(temp); //change query back to what it was
					
					TextView title = (TextView) findViewById(R.id.trainingMaxesTV);
					title.setText(retStringSaver.toString());
					//title.setTextSize(12);
					//title.setGravity(Gravity.CENTER);
					title.setTextColor(Color.WHITE);
					//title.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
					//tableRowPrincipal.addView(title);
				}
				else
					if (changedView){//if the title hasn't been inserted, has there been a change in the view?

						TextView title = new TextView(this);
						title.setText(retStringSaver.toString());
						title.setTextSize(12);
						title.setGravity(Gravity.CENTER);
						title.setTextColor(Color.WHITE);
						title.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,          

								LayoutParams.WRAP_CONTENT));
					//	tableRowPrincipal.addView(title);
					//	TableRow titleRow = (TableRow) findViewById(R.id.insertValues);
						
						//createColumns("Date", "Cycle", "Lift", "Freq", "1st", "2nd", "3rd");
						changedView = false;
					}
				String liftDate = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.LIFTDATE));
				String cycle = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.CYCLE));
				String lift = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.LIFT));
				String freq = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.FREQUENCY));
				String first = String.valueOf(roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.FIRST))));
				Double second = roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.SECOND)));
				Double third = roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.THIRD)));
				final String entryString = liftDate + "|" + cycle + "|" + lift + "|" + freq + "|" + first + "|" + second + "|" + third + "|\n";
				TableRow tr = new TableRow(this);
				LayoutParams trParams = tableRowPrincipal.getLayoutParams();
				tr.setLayoutParams(trParams);
				tr.setGravity(Gravity.CENTER_HORIZONTAL);
				//parse date (remove 20..., I don't think any cycles will be running for a millenium)
				String insertDate = liftDate.substring(0, 6) + liftDate.substring(8, 10);
				createColumns(tr, insertDate, cycle, lift, freq, first, String.valueOf(second), String.valueOf(third));
				
				TextView entry = new TextView(this);
				entry.setText(entryString); 
				entry.setTextSize(12);
				entry.setGravity(Gravity.CENTER);
				LinearLayout.LayoutParams PO = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1f);
				entry.setLayoutParams(PO); 
				if(tableColorToggle)
				{
				entry.setBackgroundColor(Color.WHITE);
				tableColorToggle = false;
				}
				else if (!tableColorToggle)
				{
				entry.setBackgroundColor(Color.LTGRAY);
				tableColorToggle = true;
				}
				tr.setOnClickListener(new TableRowClickListener(entryString){

					@Override
					public void onClick(View v) {
						String myDate = entryString.substring(0, 10);//parse our date
						//parsing cycle: account for cycles greater than 9

						String dividerRegex = "(?<=\\|)[^|]++(?=\\|)";//regex to grab data between pipes (|)
						Pattern pattern = Pattern.compile(dividerRegex);
						Matcher matcher = pattern.matcher(entryString);

						String myEntries[] = new String[6]; //for reference: 0 = date, 1 = cycle, 2 = liftType, 3 = frequency, 4 = firstLift, 5 = secondLift, 6 = secondLift
						int iterator = 0 ;
						while (matcher.find()) {
							myEntries[iterator] = matcher.group(0);
							iterator++;
						}	

						Toast.makeText(ThirdScreen.this, myEntries[2], Toast.LENGTH_SHORT).show();


						Intent intent = new Intent(ThirdScreen.this, IndividualView.class);

						String myFrequency = myEntries[2];	
						String myLiftType = myEntries[1];
						String myCycle = myEntries[0];
						String myFirstLift = myEntries[3];
						String mySecondLift =  myEntries[4];
						String myThirdLift = myEntries[5];
						String viewMode = curView.name().toString(); 
						String mode = String.valueOf(lbMode);
						
						
						

						intent.putExtra("cycle", myCycle);
						intent.putExtra("frequency", myFrequency);
						intent.putExtra("liftType", myLiftType);
						intent.putExtra("firstLift", myFirstLift);
						intent.putExtra("secondLift", mySecondLift);
						intent.putExtra("thirdLift", myThirdLift);
						intent.putExtra("thirdLift", myThirdLift);
						intent.putExtra("date", myDate);
						intent.putExtra("mode", mode);
						intent.putExtra("viewMode", viewMode);


						startActivity(intent);
					}});	   
				//tableRowPrincipal.addView(entry);
				tableRowPrincipal.addView(tr);
				//TODO encase everything in a linear layout, then you should have individualized views by date. be sure there is not too much of a performance hit 
				//then parse by date (regex) and then either parse first second and third (along with any other additional info you may want to add for featues), and you should be good to go my nigga ;)
			}

		}


/*		public TableRowClickListener individualViewListener = new  TableRowClickListener(){

			@Override
			public void onClick(View v) {
				String entryText =  ((TextView) v).getText().toString(); 
				
			}};  */

			private void createColumns(TableRow tr, String liftDate, String cycle, String lift, String freq, String first, String second, String third) {
				//date column creation 
				
				
				
				TableRow.LayoutParams tvParams = new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				tvParams.setMargins(1, 1, 1, 1);
				final int minHeight = 15;
				TextView dateColumn = new TextView(this);
				dateColumn.setText(liftDate);
				dateColumn.setLayoutParams(tvParams);
				dateColumn.setBackgroundColor(Color.WHITE);
				dateColumn.setMinHeight(minHeight);
				dateColumn.setGravity(Gravity.CENTER_HORIZONTAL);
				tr.addView(dateColumn);
				
				
				//cycle column creation 
				TextView cycleColumn = new TextView(this);
				cycleColumn.setText(cycle);
				cycleColumn.setLayoutParams(tvParams);
				cycleColumn.setBackgroundColor(Color.WHITE);
				cycleColumn.setMinHeight(minHeight);
				cycleColumn.setGravity(Gravity.CENTER_HORIZONTAL);
				tr.addView(cycleColumn);
				
				//lift column creation 
				TextView liftColumn = new TextView(this);
				liftColumn.setText(lift);
				liftColumn.setLayoutParams(tvParams);
				liftColumn.setBackgroundColor(Color.WHITE);
				liftColumn.setMinHeight(minHeight);
				liftColumn.setGravity(Gravity.CENTER_HORIZONTAL);
				tr.addView(liftColumn);

				//freq column creation 
				TextView freqColumn = new TextView(this);
				freqColumn.setText(freq);
				freqColumn.setLayoutParams(tvParams);
				freqColumn.setBackgroundColor(Color.WHITE);
				freqColumn.setMinHeight(minHeight);
				freqColumn.setGravity(Gravity.CENTER_HORIZONTAL);
				tr.addView(freqColumn);
				
				
				//first lift column creation
				TextView firstLiftColumn = new TextView(this);
				firstLiftColumn.setText(first);
				firstLiftColumn.setLayoutParams(tvParams);
				firstLiftColumn.setBackgroundColor(Color.WHITE);
				firstLiftColumn.setMinHeight(minHeight);
				firstLiftColumn.setGravity(Gravity.CENTER_HORIZONTAL);
				tr.addView(firstLiftColumn);
				
				//second lift column creation
				TextView secondLiftColumn = new TextView(this);
				secondLiftColumn.setText(second);
				secondLiftColumn.setLayoutParams(tvParams);
				secondLiftColumn.setBackgroundColor(Color.WHITE);
				secondLiftColumn.setMinHeight(minHeight);
				secondLiftColumn.setGravity(Gravity.CENTER_HORIZONTAL);
				tr.addView(secondLiftColumn);
				
				//third lift column creation
				TextView thirdLiftColumn = new TextView(this);
				thirdLiftColumn.setText(third);
				thirdLiftColumn.setLayoutParams(tvParams);
				thirdLiftColumn.setBackgroundColor(Color.WHITE);
				thirdLiftColumn.setMinHeight(minHeight);
				thirdLiftColumn.setGravity(Gravity.CENTER_HORIZONTAL);
				tr.addView(thirdLiftColumn);
		}

			private void backToFirst()
			{
				SQLiteDatabase db = eventsData.getWritableDatabase();
				db.delete("Lifts", null, null);
				startActivity(new Intent(this, MainActivity.class));
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

					//bin
					
					//Dead triples
					Processor.calculateTriplesDay(Processor.getDeadTM());
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


			//Async caller for threading
			private class AsyncCaller extends AsyncTask<Void, Void, Void>
			{
				ProgressDialog pdLoading = new ProgressDialog(ThirdScreen.this);
				@Override
				protected void onPreExecute() {
					super.onPreExecute();

					//this method will be running on UI thread
					pdLoading.setMessage("\tLoading your gains...");

					pdLoading.show();
				}
				@Override
				protected Void doInBackground(Void... params) {
					calculateCycle();
					cursor = getEvents();

					//this method will be running on background thread so don't update UI frome here
					//do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here


					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					super.onPostExecute(result);
					showDefaultEvents(cursor);

					pdLoading.dismiss();
				}

			}





}//end thirdscreen activiy 





