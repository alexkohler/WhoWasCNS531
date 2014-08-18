package com.kohlerbear.whowascns;



import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kohlerbear.whowascns.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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


public class ThirdScreenActivity extends Activity {

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
	static boolean[] kgBooleans = new boolean[]{true, true, true, true, true, true, true};
	static boolean[] lbBooleans = new boolean[]{true, true, true, true, true, true, true}; //defaults until plateconfig is changed

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

	static String[] liftPattern;

	CURRENT_VIEW individualCV; //for individual view
	CURRENT_FREQ individualFreq; //^^

	//initialize processor to process all lifts, dates, etc...
	DateAndLiftProcessor Processor = new DateAndLiftProcessor();

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
		liftPattern = intent.getStringArrayExtra("liftPattern");
		

		
		eventsData = new EventsDataSQLHelper(this);
		SQLiteDatabase db = eventsData.getWritableDatabase(); // helper is object extends SQLiteOpenHelper

		//if we are coming from second screen
		String origin = intent.getStringExtra("origin");
		if (origin.equals("individualView"))
		{
			eventsData.reinflateTable(this, intent);
		}
		
		
		else if (origin.equals("second"))
		{

			eventsData.inflateTable(this, intent, startingDate, db);
			new AsyncCaller(liftPattern).execute();
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
			CharSequence colors[] = new CharSequence[] {"Plate Config", "Adjust Lifts", /*"Export.."*/ "Reset", "View By...", "Back"};

			AlertDialog.Builder builder = new AlertDialog.Builder(ThirdScreenActivity.this);
			builder.setTitle("Options menu");
			//final LinearLayout myLL = (LinearLayout)findViewById(R.id.linearLayout1);
			builder.setItems(colors, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which==0)
					{
						CharSequence modes[] = new CharSequence[] {"Pounds", "Kilograms"};
						AlertDialog.Builder builder = new AlertDialog.Builder(ThirdScreenActivity.this);
						builder.setTitle("Unit menu");
						builder.setItems(modes, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (which == 0){
								    createLbBuilderMenu();
								}
								if (which == 1)
								{
									createKgBuilderMenu();		
								}

				
							}//end inner onClick 


						});//end inner which listener
						builder.show();	
					}
					
					
					if (which == 1){
						SQLiteDatabase db = eventsData.getWritableDatabase();
						Intent myIntent = new Intent(ThirdScreenActivity.this, SecondScreenActivity.class);
						myIntent.putExtra("origin", "third");
						String[] intentDataArray = new String[6];
						intentDataArray = getSecondScreenData(intentDataArray);
						myIntent.putExtra("key", Processor.getStartingDate()); //key is for get starting date  //TODO change 
						myIntent.putExtra("bench", intentDataArray[0]);
						myIntent.putExtra("squat", intentDataArray[1]);
						myIntent.putExtra("ohp", intentDataArray[2]);
						myIntent.putExtra("dead", intentDataArray[3]);
						myIntent.putExtra("liftPattern", liftPattern);
						db.delete("Lifts", null, null);
						startActivity(myIntent);
					}
					if (which == 2) 
					{// arrays are zero indexed
						AlertDialog.Builder builder = new AlertDialog.Builder(ThirdScreenActivity.this);
						builder.setMessage("Are you sure you want to reset?").setPositiveButton("Yes", dialogClickListener)
					    .setNegativeButton("No", dialogClickListener).show();

					}
					//nothing for export yet
					if (which == 3)
					{
						createViewBuilder();
					}
					//if (which== 4) 
					//no need to worry about Back case, takes care of itself
				}

				private String[] getSecondScreenData(String[] intentDataArray) {
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

		
		//"Are you sure?" builder template (Used in reset)
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
					//if yes 
					SQLiteDatabase db = eventsData.getWritableDatabase();
					curView = CURRENT_VIEW.DEFAULT;
					db.delete("Lifts", null, null);
					backToFirst();
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            //No button clicked
		            break;
		        }
		    }
		};

		//ugly but will clean up later..
		public void createViewBuilder()
		{
			CharSequence colors[] = new CharSequence[] {"Show all", "Bench Only", "Squat Only", "OHP Only", "Deadlift only", "5-5-5 Days only", "3-3-3 Days only", "5-3-1 days only", "Back"};

			AlertDialog.Builder builder2 = new AlertDialog.Builder(ThirdScreenActivity.this);
			builder2.setTitle("Show by:");

			builder2.setItems(colors, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Cursor cursor = getEvents();
					TableLayout tableRowPrincipal = (TableLayout)findViewById(R.id.tableLayout1);
					switch (which){
					case 0:
						curView = CURRENT_VIEW.DEFAULT;
						Toast.makeText(ThirdScreenActivity.this, "View Selected: Show All", Toast.LENGTH_SHORT).show();
						setQuery(null);
						tableRowPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;
					case 1:
						curView = CURRENT_VIEW.BENCH;
						Toast.makeText(ThirdScreenActivity.this, "View Selected: Bench Only", Toast.LENGTH_SHORT).show();
						setQuery("Lift = 'Bench'");
						tableRowPrincipal.removeAllViews(); //try something like this 
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;	
					case 2:
						curView = CURRENT_VIEW.SQUAT;
						Toast.makeText(ThirdScreenActivity.this, "View Selected: Squat Only", Toast.LENGTH_SHORT).show();
						setQuery("Lift = 'Squat'");
						tableRowPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true; 
						showDefaultEvents(cursor);
						break;	
					case 3:
						curView = CURRENT_VIEW.OHP;
						Toast.makeText(ThirdScreenActivity.this, "View Selected: OHP Only", Toast.LENGTH_SHORT).show();
						setQuery("Lift = 'OHP'");
						tableRowPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;		
					case 4:
						curView = CURRENT_VIEW.DEAD;
						Toast.makeText(ThirdScreenActivity.this, "View Selected: Deadlift Only", Toast.LENGTH_SHORT).show();
						setQuery("Lift = 'Deadlift'");
						tableRowPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;
					case 5:
						curView = CURRENT_VIEW.FIVES;
						Toast.makeText(ThirdScreenActivity.this, "View Selected: Fives Days Only", Toast.LENGTH_SHORT).show();
						setQuery("Frequency = '5-5-5'");
						tableRowPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;
					case 6:
						curView = CURRENT_VIEW.THREES;
						Toast.makeText(ThirdScreenActivity.this, "View Selected: Triples Days Only", Toast.LENGTH_SHORT).show();
						setQuery("Frequency = '3-3-3'");
						tableRowPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;
					case 7:
						curView = CURRENT_VIEW.ONES;
						Toast.makeText(ThirdScreenActivity.this, "View Selected: 5-3-1 Days Only", Toast.LENGTH_SHORT).show();
						setQuery("Frequency = '5-3-1'");
						tableRowPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;				
					case 8:
						CharSequence colors[] = new CharSequence[] {"Adjust Lifts", "Reset", /*"Export...",*/ "View By...", "Back"};

						AlertDialog.Builder builder = new AlertDialog.Builder(ThirdScreenActivity.this);
						builder.setTitle("Options menu");
						builder.setItems(colors, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (which == 0){
									onBackPressed();
								}
								if (which == 1) // arrays are zero indexed
								backToFirst();

								if (which == 2)
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

		
		private void createKgBuilderMenu() {
			DialogInterface dialog;
			// arrays are zero indexed
			    final CharSequence[] kgPlates = {" 25kg "," 20kg "," 15kg "," 10kg "," 5kg ", " 2.5kg "," 1.25kg "};
			    // arraylist to keep the selected items
			    final ArrayList<Integer> seletedItems=new ArrayList<Integer>();

			    AlertDialog.Builder builder = new AlertDialog.Builder(ThirdScreenActivity.this);
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
			    	 boolean kgHaveTwentyFive = false, kgHaveTwenty = false, kgHaveFifteen = false, kgHaveTen = false, 
			    			 kgHaveFive = false , kgHaveTwoPointFive= false , kgHaveOnePointTwoFive= false;//actually name booleans for sanity purposes
			    	 for (int indexSelected : seletedItems)
			    	 {
			    		switch (indexSelected)
			    		{
			    		case 0:
				    		 kgHaveTwentyFive = true;
				    		break;
			    		case 1:
				    		 kgHaveTwenty = true;
				    		break;
			    		case 2:
				    		 kgHaveFifteen = true;
				    		break;
			    		case 3:
			    			kgHaveTen  = true;
			    			break;
			    		case 4:	
			    			kgHaveFive  = true;
			    			break;
			    		case 5:
			    			 kgHaveTwoPointFive = true;
			    			break;
			    		case 6:
			    			 kgHaveOnePointTwoFive = true;
			    			break;
			    		}
			    	 }
			    	 //after sorting through status, pack these up into a boolean array
			    	 boolean[] localkgBooleans = new boolean[7];
			    	 localkgBooleans[0] = kgHaveTwentyFive;
			    	 localkgBooleans[1] = kgHaveTwenty;
			    	 localkgBooleans[2] = kgHaveFifteen;
			    	 localkgBooleans[3] = kgHaveTen;
			    	 localkgBooleans[4] = kgHaveFive;
			    	 localkgBooleans[5] = kgHaveTwoPointFive;
			    	 localkgBooleans[6] = kgHaveOnePointTwoFive;
			    	 setKgBooleans(localkgBooleans);
			    	 
			    	 
			    	 
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
		
	private void createLbBuilderMenu() {
		DialogInterface dialog;
		final CharSequence[] lbPlates = { " 45lb ", " 35lb ", " 25lb ",
				" 10lb ", " 5lb ", " 2.5lb " };
		// arraylist to keep the selected items
		final ArrayList<Integer> seletedItems = new ArrayList<Integer>();

		AlertDialog.Builder builder = new AlertDialog.Builder(ThirdScreenActivity.this);
		builder.setTitle("What plates does your gym have?");
		builder.setMultiChoiceItems(lbPlates, null,
				new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int indexSelected, boolean isChecked) {
						if (isChecked) {
							// If the user checked the item, add it to the
							// selected items
							seletedItems.add(indexSelected);
						} else if (seletedItems.contains(indexSelected)) {
							// Else, if the item is already in the array, remove
							// it
							seletedItems.remove(Integer.valueOf(indexSelected));
						}
					}
				})
				// Set the action buttons
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
				    	 Boolean lbHaveFortyFive = false, lbHaveThirtyFive = false, lbHaveTwentyFive = false, lbHaveTen = false, 
				    			 lbHaveFive = false , lbHaveTwoPointFive= false;
						for (int indexSelected : seletedItems) {
							switch (indexSelected) {
							case 0:
								lbHaveFortyFive = true;// TODO fix logic
								break;
							case 1:
								lbHaveThirtyFive = true;
								break;
							case 2:
								lbHaveTwentyFive = true;
								break;
							case 3:
								lbHaveTen = true;
								break;
							case 4:
								lbHaveFive = true;
								break;
							case 5:
								lbHaveTwoPointFive = true;
								break;
							}
						}
						//after running through boolean status, pack them up in an array.. is there a more elegant way? Probably, but that's why you need to hire me and teach me ;)
						boolean[] localLbStatus = new boolean[6];
						localLbStatus[0] = lbHaveFortyFive;
						localLbStatus[1] = lbHaveThirtyFive;
						localLbStatus[2] = lbHaveTwentyFive;
						localLbStatus[3] = lbHaveTen;
						localLbStatus[4] = lbHaveFive;
						localLbStatus[5] = lbHaveTwoPointFive;
						setLbBooleans(localLbStatus);
						

					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								// Your code when user clicked on Cancel

							}
						});

			dialog = builder.create();//AlertDialog dialog; create like this outside onClick
			((Dialog) dialog).show();
		}


		public void setMode (String myMode )
		{
			MODE_FORMAT = myMode;
		}

		String getModeFormat ()
		{
			return MODE_FORMAT; 
		}


		public void setQuery(String myQuery)
		{
			CURRENT_SELECT_QUERY = myQuery;
		}

		public String getQuery ()
		{
			return CURRENT_SELECT_QUERY;
		}

		@SuppressWarnings("deprecation") Cursor getEvents() {
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

		@SuppressWarnings("deprecation") void showDefaultEvents(Cursor cursor) {
			StringBuilder ret; 
			ret = new StringBuilder("");
			TableLayout tableRowPrincipal = (TableLayout)findViewById(R.id.tableLayout1);
			while (cursor.moveToNext()) {
				if (!this.insertStatus){//has title been inserted?
					String temp = getQuery(); //temporarily hold query
					setQuery(null);
					Cursor subcursor = cursor;//create a subcursor to get our training maxes 
					ret = new StringBuilder("Start TMs [Bench: " + subcursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.TRAINING_MAX)) + "]");
					subcursor.moveToNext();
					ret.append(" [Squat: " + subcursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.TRAINING_MAX)) + "]");
					subcursor.moveToNext();
					ret.append(" [OHP: " + subcursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.TRAINING_MAX)) + "]" );
					subcursor.moveToNext();
					ret.append(" [Dead: " + subcursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.TRAINING_MAX)) + "]" );
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
				eventsData.createColumns(this, tr, insertDate, cycle, lift, freq, first, String.valueOf(second), String.valueOf(third));
				
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

						Toast.makeText(ThirdScreenActivity.this, myEntries[2], Toast.LENGTH_SHORT).show();


						Intent intent = new Intent(ThirdScreenActivity.this, IndividualViewActivity.class);

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
						intent.putExtra("liftPattern", liftPattern);
						
						if (lbMode == 1) //if we are in lbs mode
						intent.putExtra("boolArray", getLbBooleans());
						if (lbMode ==0)// if we are in kgMode
						intent.putExtra("boolArray", getKgBooleans());	
						//intent.putExtra(liftPattern, "pattern"); monkey


						startActivity(intent);
					}});	   
				//tableRowPrincipal.addView(entry);
				tableRowPrincipal.addView(tr);
				//then parse by date (regex) and then either parse first second and third (along with any other additional info you may want to add for featues), and you should be good to go my nigga ;)
			}

		}


/*		public TableRowClickListener individualViewListener = new  TableRowClickListener(){

			@Override
			public void onClick(View v) {
				String entryText =  ((TextView) v).getText().toString(); 
				
			}};  */

			private void backToFirst()
			{
				SQLiteDatabase db = eventsData.getWritableDatabase();
				db.delete("Lifts", null, null);
				startActivity(new Intent(this, MainActivity.class));
			}		


			double roundtoTwoDecimals(double d) 
			{
				return Double.valueOf(twoDForm.format(d));
			}
			
			public static boolean[] getKgBooleans() {
				return kgBooleans;
			}

			public static void setKgBooleans(boolean[] kgBooleans) {
				ThirdScreenActivity.kgBooleans = kgBooleans;
			}

			public static boolean[] getLbBooleans() {
				return lbBooleans;
			}

			public static void setLbBooleans(boolean[] lbBooleans) {
				ThirdScreenActivity.lbBooleans = lbBooleans;
			}


			//Async caller for threading
			class AsyncCaller extends AsyncTask<Void, Void, Void>
			{
				String[] LiftPattern;
				public AsyncCaller(String[] liftPattern)
				{
					LiftPattern = liftPattern;
				}
				
				ProgressDialog pdLoading = new ProgressDialog(ThirdScreenActivity.this);
				@Override
				protected void onPreExecute() {
					super.onPreExecute();

					//this method will be running on UI thread
					pdLoading.setMessage("\tLoading your gains...");

					pdLoading.show();
				}
				@Override
				protected Void doInBackground(Void... params) {
					Processor.calculateCycle(ThirdScreenActivity.this, LiftPattern);
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





