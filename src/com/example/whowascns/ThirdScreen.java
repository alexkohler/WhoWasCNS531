package com.example.whowascns;



import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
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
			LinearLayout llPrincipal = (LinearLayout)findViewById(R.id.linearLayout1);
			switch(view)
			{//		DEFAULT('D'), BENCH('B'), SQUAT('S'), OHP('O'), DEAD('D'), FIVES('5'), THREES('3'), ONES('1');
				case "DEFAULT":
					setQuery(null);
					llPrincipal.removeAllViews();
					cursor = getEvents();
					showDefaultEvents(cursor);
					break;
				case "BENCH":
					setQuery("Lift = 'Bench'");
					llPrincipal.removeAllViews();
					cursor = getEvents();
					insertStatus = false;
					showDefaultEvents(cursor);
					break;
				case "SQUAT":
					setQuery("Lift = 'Squat'");
					llPrincipal.removeAllViews();
					cursor = getEvents();
					insertStatus = false;
					showDefaultEvents(cursor);
					break;
				case "OHP":
					setQuery("Lift = 'OHP'");
					llPrincipal.removeAllViews();
					cursor = getEvents();
					insertStatus = false;
					showDefaultEvents(cursor);
					break;
				case "DEAD":
					setQuery("Lift = 'Deadlift'");
					llPrincipal.removeAllViews();
					cursor = getEvents();
					insertStatus = false;
					showDefaultEvents(cursor);
					break;	
				case "FIVES":
					setQuery("Frequency = '5-5-5'");
					llPrincipal.removeAllViews();
					cursor = getEvents();
					insertStatus = false;
					showDefaultEvents(cursor);
					break;	
				case "THREES":
					setQuery("Frequency = '3-3-3'");
					llPrincipal.removeAllViews();
					cursor = getEvents();
					insertStatus = false;
					showDefaultEvents(cursor);
					break;
				case "ONES":
					setQuery("Frequency = '5-3-1'");
					llPrincipal.removeAllViews();
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


	private OnClickListener optionsListener = new OnClickListener () {

		@Override
		public void onClick(View v) {
			//Options menu -- wrap up in a checkbox Listener 
			CharSequence colors[] = new CharSequence[] {"Adjust Lifts", "Reset", "Export...", "View By...", "Back"};

			AlertDialog.Builder builder = new AlertDialog.Builder(ThirdScreen.this);
			builder.setTitle("Options menu");
			//final LinearLayout myLL = (LinearLayout)findViewById(R.id.linearLayout1);
			builder.setItems(colors, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == 0){
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
					if (which == 1) 
					{// arrays are zero indexed
						SQLiteDatabase db = eventsData.getWritableDatabase();
						curView = CURRENT_VIEW.DEFAULT;
						db.delete("Lifts", null, null);
						backToFirst();

					}
					if (which == 3)
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
					LinearLayout llPrincipal = (LinearLayout)findViewById(R.id.linearLayout1);
					switch (which){
					case 0:
						curView = CURRENT_VIEW.DEFAULT;
						Toast.makeText(ThirdScreen.this, "View Selected: Show All", Toast.LENGTH_SHORT).show();
						setQuery(null);
						llPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;
					case 1:
						curView = CURRENT_VIEW.BENCH;
						Toast.makeText(ThirdScreen.this, "View Selected: Bench Only", Toast.LENGTH_SHORT).show();
						setQuery("Lift = 'Bench'");
						llPrincipal.removeAllViews(); //try something like this 
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;	
					case 2:
						curView = CURRENT_VIEW.SQUAT;
						Toast.makeText(ThirdScreen.this, "View Selected: Squat Only", Toast.LENGTH_SHORT).show();
						setQuery("Lift = 'Squat'");
						llPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true; 
						showDefaultEvents(cursor);
						break;	
					case 3:
						curView = CURRENT_VIEW.OHP;
						Toast.makeText(ThirdScreen.this, "View Selected: OHP Only", Toast.LENGTH_SHORT).show();
						setQuery("Lift = 'OHP'");
						llPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;		
					case 4:
						curView = CURRENT_VIEW.DEAD;
						Toast.makeText(ThirdScreen.this, "View Selected: Deadlift Only", Toast.LENGTH_SHORT).show();
						setQuery("Lift = 'Deadlift'");
						llPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;
					case 5:
						curView = CURRENT_VIEW.FIVES;
						Toast.makeText(ThirdScreen.this, "View Selected: Fives Days Only", Toast.LENGTH_SHORT).show();
						setQuery("Frequency = '5-5-5'");
						llPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;
					case 6:
						curView = CURRENT_VIEW.THREES;
						Toast.makeText(ThirdScreen.this, "View Selected: Triples Days Only", Toast.LENGTH_SHORT).show();
						setQuery("Frequency = '3-3-3'");
						llPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;
					case 7:
						curView = CURRENT_VIEW.ONES;
						Toast.makeText(ThirdScreen.this, "View Selected: 5-3-1 Days Only", Toast.LENGTH_SHORT).show();
						setQuery("Frequency = '5-3-1'");
						llPrincipal.removeAllViews();
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
			LinearLayout llPrincipal = (LinearLayout)findViewById(R.id.linearLayout1);
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
					ret.append("\n");
					if (lbMode == 1)
					ret.append("Mode: lbs");
					else if (lbMode == 0 )
					ret.append("Mode: kgs");
					else
					ret.append("Mode error");	
					cursor.moveToFirst();
					insertStatus = true;
					retStringSaver = ret.toString();
					setQuery(temp); //change query back to what it was
					TextView title = new TextView(this);
					title.setText(retStringSaver.toString());
					title.setTextSize(12);
					title.setGravity(Gravity.LEFT);
					title.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,          

							LayoutParams.WRAP_CONTENT));
					llPrincipal.addView(title);
				}
				else
					if (changedView){//if the title hasn't been inserted, has there been a change in the view?

						TextView title = new TextView(this);
						title.setText(retStringSaver.toString());
						title.setTextSize(12);
						title.setGravity(Gravity.LEFT);
						title.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,          

								LayoutParams.WRAP_CONTENT));
						llPrincipal.addView(title);
						changedView = false;
					}
				String liftDate = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.LIFTDATE));
				String cycle = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.CYCLE));
				String lift = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.LIFT));
				String freq = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.FREQUENCY));
				String first = String.valueOf(roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.FIRST))));
				Double second = roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.SECOND)));
				Double third = roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.THIRD)));
				String entryString = liftDate + "|" + cycle + "|" + lift + "|" + freq + "|" + first + "|" + second + "|" + third + "|\n";

				TextView entry = new TextView(this);
				entry.setText(entryString); 
				entry.setTextSize(12);
				entry.setGravity(Gravity.LEFT);
				entry.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,          

						LayoutParams.WRAP_CONTENT));
				entry.setOnClickListener(individualViewListener);	   
				llPrincipal.addView(entry);

				//TODO encase everything in a linear layout, then you should have individualized views by date. be sure there is not too much of a performance hit 
				//then parse by date (regex) and then either parse first second and third (along with any other additional info you may want to add for featues), and you should be good to go my nigga ;)
			}

		}


		public OnClickListener individualViewListener = new  OnClickListener(){

			@Override
			public void onClick(View v) {
				String entryText =  ((TextView) v).getText().toString(); 
				String myDate = entryText.substring(0, 10);//parse our date
				//parsing cycle: account for cycles greater than 9

				String dividerRegex = "(?<=\\|)[^|]++(?=\\|)";//regex to grab data between pipes (|)
				Pattern pattern = Pattern.compile(dividerRegex);
				Matcher matcher = pattern.matcher(entryText);

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
			}};  

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





