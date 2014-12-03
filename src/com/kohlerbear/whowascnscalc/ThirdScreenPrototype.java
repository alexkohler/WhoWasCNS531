package com.kohlerbear.whowascnscalc;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ThirdScreenPrototype extends BaseActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	
	ViewPager mViewPager;
	
	//for nav drawer
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	
	EventsDataSQLHelper eventsData;
	String MODE_FORMAT;
	Integer NUMBER_CYCLES;
	static String CURRENT_SELECT_QUERY;
	static int lbMode;
	boolean insertStatus = false;
	boolean changedView = false;
	String retStringSaver; //for sake of changing views
	Cursor cursor;
	String lbmode;
	
	TableRow titleTableRow; 
	
	boolean toggleButtonCalled = false;
	
	Tracker tracker = null;

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
	
	int currentCycleSelected;

	static CURRENT_VIEW curView = CURRENT_VIEW.DEFAULT;//start with default (show all) view (for overview)

	static String[] liftPattern;

	CURRENT_VIEW individualCV; //for individual view
	CURRENT_FREQ individualFreq; //^^

	//initialize processor to process all lifts, dates, etc...
	DateAndLiftProcessor Processor = new DateAndLiftProcessor();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_third_screen_prototype);

		//Set up our navigation drawer
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load titles from strings.xml
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);// load icons from
															// strings.xml

		set(navMenuTitles, navMenuIcons);
		navMenuIcons.recycle();
 
        getActionBar().setDisplayHomeAsUpEnabled(true); 
		
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
//						Toast.makeText(getApplicationContext(), "Nav position " + position, Toast.LENGTH_SHORT).show(); //does not get called until explicit page is selected (meaning not called upon opening)
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		
		//Take care of actual third screen festivities
		Button configureButton = (Button) findViewById(R.id.configureButtonPrototype);//TODO watchme
		
		configureButton.setBackgroundColor(Color.GRAY);
		configureButton.setTextColor(Color.WHITE);

		configureButton.setOnClickListener(optionsListener);

		Intent intent = getIntent();

		String startingDate = intent.getStringExtra("key2");
		liftPattern = intent.getStringArrayExtra("liftPattern");
		
		tracker = GoogleAnalytics.getInstance(this).getTracker("UA-55018534-1");
		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME, "Third Screen");

		tracker.send(hitParameters);
		
		eventsData = new EventsDataSQLHelper(this);
		SQLiteDatabase db = eventsData.getWritableDatabase(); 
		
		titleTableRow = (TableRow) findViewById(R.id.insertValuesPrototype);//TODO fixme
		
		
		//if we are coming from second screen
		String origin = intent.getStringExtra("origin");
		if (origin.equals("individualViews"))
		{
			Processor.setRoundingFlag(true);
			ConfigTool configtool = new ConfigTool(ThirdScreenPrototype.this);
			Processor.setUnitMode(configtool.getLbModeFromDatabase());
			eventsData.reinflateTable(this, intent);
		}
		else if (origin.equals("second"))
		{
			Processor.setRoundingFlag(true);//rounding on by default
			
			String mode = intent.getStringExtra("mode");
			Processor.setUnitMode(mode);
			eventsData.inflateTable(this, intent, startingDate, db); 
			new AsyncCaller(liftPattern).execute();
		}
		else//this is called during refreshes/view existing
		{
			Processor.setRoundingFlag(true);//rounding on by default
			ConfigTool configtool = new ConfigTool(ThirdScreenPrototype.this);
			Processor.setUnitMode(configtool.getLbModeFromDatabase());
			
			Cursor cursor = getEvents();
			showDefaultEvents(cursor);
			
			
		}
		
		if (toggleButtonCalled)
		{
			if (Processor.getRoundingFlag())
				Processor.setRoundingFlag(false);
			else
				Processor.setRoundingFlag(true);
		}
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
			CharSequence colors[] = new CharSequence[] {"Toggle rounding",  /*"Adjust Lifts"*/ "Reset", "View By...", "Cancel"};

			AlertDialog.Builder builder = new AlertDialog.Builder(ThirdScreenPrototype.this);
			builder.setTitle("Options menu");
			builder.setItems(colors, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					
					if (which == 0){//Toggle Rounding
						if (Processor.getRoundingFlag())
							Processor.setRoundingFlag(false);
						else 
							Processor.setRoundingFlag(true);
						Cursor cursor = getEvents();//TODO watch aliasing
						TableLayout tableRowPrincipal = (TableLayout)findViewById(R.id.tableLayout1Prototype); 
						tableRowPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
					}
/*					if (which == 1)//Adjust lifts 
					{
						SQLiteDatabase db = eventsData.getWritableDatabase();
						Intent myIntent = new Intent(ThirdScreenPrototype.this, REVAMPEDSecondScreenActivity.class);
						myIntent.putExtra("origin", "third");
						String[] intentDataArray = new String[6];
						intentDataArray = getTrainingMaxesInDefaultOrder();
						myIntent.putExtra("key", Processor.getStartingDate()); //key is for get starting date  //TODO change 
						myIntent.putExtra("bench", intentDataArray[0]);
						myIntent.putExtra("squat", intentDataArray[1]);
						myIntent.putExtra("ohp", intentDataArray[2]);
						myIntent.putExtra("dead", intentDataArray[3]);
						myIntent.putExtra("liftPattern", liftPattern);
						db.delete("Lifts", null, null);//TODO necessary?
						startActivity(myIntent);
						//decide if you want to support, big problem is determining where the 
					}*/
					if (which == 1) //reset
					{
						AlertDialog.Builder builder = new AlertDialog.Builder(ThirdScreenPrototype.this);
						builder.setMessage("Are you sure you want to reset?").setPositiveButton("Yes", dialogClickListener)
					    .setNegativeButton("No", dialogClickListener).show();

					}
					if (which == 2)//view by
					{
						createViewBuilder();
					}
					if (which == 3) //cancel 
					{
						dialog.cancel();
					}
				}

			});
			builder.show();

		}};

		private String[] getTrainingMaxesInDefaultOrder() {
			setQuery("Lift = 'Bench' AND Cycle = '1'"); 
			Cursor myCursor = getEvents(); 
			myCursor.moveToNext();
			String benchTM  = myCursor.getString(myCursor.getColumnIndex(EventsDataSQLHelper.TRAINING_MAX));
			setQuery("Lift = 'Squat' AND Cycle = '1'"); 
			myCursor = getEvents(); //vladdy
			myCursor.moveToNext();
			String squatTM  = myCursor.getString(myCursor.getColumnIndex(EventsDataSQLHelper.TRAINING_MAX));
			setQuery("Lift = 'OHP' AND Cycle = '1'"); 
			myCursor = getEvents(); //vladdy
			myCursor.moveToNext();
			String ohpTM  = myCursor.getString(myCursor.getColumnIndex(EventsDataSQLHelper.TRAINING_MAX));
			setQuery("Lift = 'Deadlift' AND Cycle = '1'");  
			myCursor = getEvents(); //vladdy
			myCursor.moveToNext();
			String deadTM  = myCursor.getString(myCursor.getColumnIndex(EventsDataSQLHelper.TRAINING_MAX));
			String[] trainingMaxes = new String[4];
			trainingMaxes[0] = benchTM;
			trainingMaxes[1] = squatTM;
			trainingMaxes[2] = ohpTM;
			trainingMaxes[3] = deadTM;
			
			return trainingMaxes;
		}
		
		
		//"Are you sure?" builder template (Used in reset)
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
					SQLiteDatabase db = eventsData.getWritableDatabase();
					curView = CURRENT_VIEW.DEFAULT;
					db.delete("Lifts", null, null);
					backToFirst();
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            dialog.cancel();
		            break;
		        }
		    }
		};

		public void createViewBuilder()
		{
			CharSequence colors[] = new CharSequence[] {"Show all", "Bench Only", "Squat Only", "OHP Only", "Deadlift only", "5-5-5 Days only", "3-3-3 Days only", "5-3-1 days only", "Back"};

			AlertDialog.Builder builder2 = new AlertDialog.Builder(ThirdScreenPrototype.this);
			builder2.setTitle("Show by:");

			builder2.setItems(colors, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Cursor cursor = getEvents();
					TableLayout tableRowPrincipal = (TableLayout)findViewById(R.id.tableLayout1Prototype);
					switch (which){
					case 0:
						curView = CURRENT_VIEW.DEFAULT;
						Toast.makeText(ThirdScreenPrototype.this, "View Selected: Show All", Toast.LENGTH_SHORT).show();
						setQuery("CYCLE = '" + currentCycleSelected + "'");
						tableRowPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;
					case 1:
						curView = CURRENT_VIEW.BENCH;
						Toast.makeText(ThirdScreenPrototype.this, "View Selected: Bench Only", Toast.LENGTH_SHORT).show();
						setQuery("Lift = 'Bench' AND CYCLE = '" + currentCycleSelected + "'");
						tableRowPrincipal.removeAllViews(); 
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;	
					case 2:
						curView = CURRENT_VIEW.SQUAT;
						Toast.makeText(ThirdScreenPrototype.this, "View Selected: Squat Only", Toast.LENGTH_SHORT).show();
						setQuery("Lift = 'Squat' AND CYCLE = '" + currentCycleSelected + "'");
						tableRowPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true; 
						showDefaultEvents(cursor);
						break;	
					case 3:
						curView = CURRENT_VIEW.OHP;
						Toast.makeText(ThirdScreenPrototype.this, "View Selected: OHP Only", Toast.LENGTH_SHORT).show();
						setQuery("Lift = 'OHP' AND CYCLE = '" + currentCycleSelected + "'");
						tableRowPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;		
					case 4:
						curView = CURRENT_VIEW.DEAD;
						Toast.makeText(ThirdScreenPrototype.this, "View Selected: Deadlift Only", Toast.LENGTH_SHORT).show();
						setQuery("Lift = 'Deadlift' AND CYCLE = '" + currentCycleSelected + "'");
						tableRowPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;
					case 5:
						curView = CURRENT_VIEW.FIVES;
						Toast.makeText(ThirdScreenPrototype.this, "View Selected: Fives Days Only", Toast.LENGTH_SHORT).show();
						setQuery("Frequency = '5-5-5' AND CYCLE = '" + currentCycleSelected + "'");
						tableRowPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;
					case 6:
						curView = CURRENT_VIEW.THREES;
						Toast.makeText(ThirdScreenPrototype.this, "View Selected: Triples Days Only", Toast.LENGTH_SHORT).show();
						setQuery("Frequency = '3-3-3' AND CYCLE = '" + currentCycleSelected + "'");
						tableRowPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;
					case 7:
						curView = CURRENT_VIEW.ONES;
						Toast.makeText(ThirdScreenPrototype.this, "View Selected: 5-3-1 Days Only", Toast.LENGTH_SHORT).show();
						setQuery("Frequency = '5-3-1' AND CYCLE = '" + currentCycleSelected + "'");
						tableRowPrincipal.removeAllViews();
						cursor = getEvents();
						changedView = true;
						showDefaultEvents(cursor);
						break;				
					case 8:
						CharSequence colors[] = new CharSequence[] {"Adjust Lifts", "Reset", /*"Export...",*/ "View By...", "Back"}; //TODO this is completely incorrect

						AlertDialog.Builder builder = new AlertDialog.Builder(ThirdScreenPrototype.this);
						builder.setTitle("Options menu");
						builder.setItems(colors, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (which == 0){
									onBackPressed();
								}
								if (which == 1) 
								backToFirst();

								if (which == 2)
								{
									createViewBuilder();
								}
								if (which== 3) 
									dialog.cancel();
							}//end inner onClick 
						});//end inner which listener
						builder.show();	
						break;


					}//end switch statement
				}//end outter onclick
			});//end outter which listener

			builder2.show();

		}//end createViewBuilder

		@SuppressWarnings("deprecation") Cursor getEvents() {
			//null check for eventsData, since onTabSelected gets called eventsData is initialized
			//TODO remove me if not needed
/*			
			if (eventsData == null)
			{
				eventsData = new EventsDataSQLHelper(this);
			}
			*/
			SQLiteDatabase db = eventsData.getReadableDatabase();
			Cursor cursor = db.query(EventsDataSQLHelper.TABLE, null, getQuery(), null, null,
					null, null);

			startManagingCursor(cursor);
			return cursor;
		}
		
		

		@SuppressWarnings("deprecation") void showDefaultEvents(Cursor cursor) {
			StringBuilder ret = new StringBuilder("");
			TableLayout tableLayout = (TableLayout)findViewById(R.id.tableLayout1Prototype);//TODO watchme
			while (cursor.moveToNext()) {
				if (!this.insertStatus){//has title been inserted?
					String temp = getQuery(); //temporarily hold query
					//call get second screen data?
					String[] TMS = getTrainingMaxesInDefaultOrder();
					String benchTM = TMS[0];
					String squatTM = TMS[1];
					String ohpTM = TMS[2];
					String deadTM = TMS[3];
					setQuery("CYCLE = '" + currentCycleSelected + "'");//TODO is this query stuff dead code now?
					ret = new StringBuilder("Start TMs [Bench: " + benchTM + "]");
					ret.append(" [Squat: " + squatTM + "]");
					ret.append(" [OHP: " + ohpTM + "]" );
					ret.append(" [Dead: " + deadTM + "]" );
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
					
					TextView trainingMaxesStream = (TextView) findViewById(R.id.trainingMaxesTVPrototype);//TODO watchme
					trainingMaxesStream.setText(retStringSaver.toString());
					trainingMaxesStream.setTextColor(Color.WHITE);
				}
				else
					if (changedView){//if the title hasn't been inserted, has there been a change in the view?

/*						TextView title = new TextView(this);
						title.setText(retStringSaver.toString());
						title.setTextSize(12);
						title.setGravity(Gravity.CENTER);
						title.setTextColor(Color.WHITE);
						title.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,          

								LayoutParams.WRAP_CONTENT));
						tableRowPrincipal.addView(title);
						TableRow titleRow = (TableRow) findViewById(R.id.insertValues);*/
						
						//add title row here?
						TableRow tr = new TableRow(this);
						LayoutParams trParams = tableLayout.getLayoutParams();
						tr.setLayoutParams(trParams);
						tr.setGravity(Gravity.CENTER_HORIZONTAL);
/*			            android:layout_width="match_parent"
			                    android:layout_height="wrap_content"
			                    android:gravity="center_horizontal"*/
//						eventsData.createRow(this, tr, "Date", "Cycle", "Lift", "Freq", "1st", "2nd", "3rd");
						tableLayout.addView(titleTableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
						changedView = false;
					}
				String liftDate = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.LIFTDATE));
				String cycle = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.CYCLE));
				String lift = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.LIFT));
				String freq = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.FREQUENCY));
				try
				{
				String first; 
				Double second;
				Double third;
				if (Processor.getRoundingFlag())//getUnitMode = true -> using lbs, otherwise, using kgs
				{
					if (Processor.getUnitMode())//using lbs
					{
					first = String.valueOf(round(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.FIRST)), 5));
					second = round(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.SECOND)), 5);
					third = round(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.THIRD)), 5);
					}
					else //using kgs
					{
						first = String.valueOf(roundkg(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.FIRST)), 2.5));
						second = roundkg(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.SECOND)), 2.5);
						third = roundkg(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.THIRD)), 2.5);	
					}
					
				}
				else
				{
				first = String.valueOf(roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.FIRST))));
				second = roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.SECOND)));
				third = roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.THIRD)));
				}
				
				final String entryString = liftDate + "|" + cycle + "|" + lift + "|" + freq + "|" + first + "|" + second + "|" + third + "|\n";
				TableRow tr = new TableRow(this);
				LayoutParams trParams = tableLayout.getLayoutParams();
				tr.setLayoutParams(trParams);
				tr.setGravity(Gravity.CENTER_HORIZONTAL);
				//parse date (remove 20..., I don't think any cycles will be running for a millenium)
				String insertDate = liftDate.substring(0, 6) + liftDate.substring(8, 10);
				eventsData.createRow(this, tr, insertDate, cycle, lift, freq, first, String.valueOf(second), String.valueOf(third));
				
				TextView entry = new TextView(this);
				entry.setText(entryString); 
				entry.setTextSize(12);
				entry.setGravity(Gravity.CENTER);
				LinearLayout.LayoutParams PO = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1f);
				entry.setLayoutParams(PO); 
				
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

						Toast.makeText(ThirdScreenPrototype.this, myEntries[2], Toast.LENGTH_SHORT).show();


						Intent intent = new Intent(ThirdScreenPrototype.this, IndividualViews.class);

						String myFrequency = myEntries[2];	
						String myLiftType = myEntries[1];
						String myCycle = myEntries[0];
						String myFirstLift = myEntries[3];
						String mySecondLift =  myEntries[4];
						String myThirdLift = myEntries[5];
						String viewMode = curView.name().toString(); 
						String mode = String.valueOf(lbMode);//1 or 0 
						
						

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
						


						startActivity(intent);
					}});   
				//tableRowPrincipal.addView(entry);
				tableLayout.addView(tr);
				} // end numberformatexception block
				catch (NumberFormatException e)
				{
					Toast.makeText(ThirdScreenPrototype.this, "There was an error processing your lift numbers, please double check them!", Toast.LENGTH_LONG).show();
				}
			}

		}
		
			private void backToFirst()
			{
				SQLiteDatabase db = eventsData.getWritableDatabase();
				db.delete("Lifts", null, null);
				startActivity(new Intent(this, MainActivity.class));
			}		

			//Async caller for threading
			class AsyncCaller extends AsyncTask<Void, Void, Void>
			{
				String[] LiftPattern;
				public AsyncCaller(String[] liftPattern)
				{
					LiftPattern = liftPattern;
				}
				
				ProgressDialog pdLoading = new ProgressDialog(ThirdScreenPrototype.this);
				@Override
				protected void onPreExecute() {
					super.onPreExecute();

					//this method will be running on UI thread
					pdLoading.setMessage("\tLoading your gains...");

					pdLoading.show();
				}
				@Override
				protected Void doInBackground(Void... params) {
					Processor.setPatternAcronym(LiftPattern);
					Processor.calculateCycle(ThirdScreenPrototype.this, LiftPattern);
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


			

			//setters/getters
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
			
			public void setNumberCycles(int numberCycles)
			{
				NUMBER_CYCLES = numberCycles;
			}

			public int getNumberCycles()
			{
				return NUMBER_CYCLES;
			}
			
			
			//rounding methods
			double roundkg(double valueToBeRounded, double roundVal) //first argument is rounded, 
			{
				return (double) (Math.round(valueToBeRounded/roundVal) * roundVal);
			}

			double round(double valueToBeRounded, int roundVal)  
			{
				return (double) (Math.round(valueToBeRounded/roundVal) * roundVal);
			}
			
			double roundtoTwoDecimals(double d) 
			{
				DecimalFormat twoDForm = new DecimalFormat("#.##");
				return Double.valueOf(twoDForm.format(d));
			}

	
	
			boolean init = false;
	//Fragment stuff
	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
		currentCycleSelected = tab.getPosition() + 1;//ASSERT: this will be assigned before any database method (and hopefully called whenever this activity shows up
		if (init)
		{
		TableLayout tableRowPrincipal = (TableLayout)findViewById(R.id.tableLayout1Prototype);
		curView = CURRENT_VIEW.DEFAULT;
//		Toast.makeText(ThirdScreenPrototype.this, "DEBUG: Showing cycle" + currentCycleSelected, Toast.LENGTH_SHORT).show();
		setQuery("CYCLE = '" + currentCycleSelected + "'");
		tableRowPrincipal.removeAllViews();
		Cursor cursor = getEvents();
		changedView = true;
		showDefaultEvents(cursor);
		}
		else
			init = true;
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 5 cycles
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.cycle1).toUpperCase(l);
			case 1:
				return getString(R.string.cycle2).toUpperCase(l);
			case 2:
				return getString(R.string.cycle3).toUpperCase(l);
			case 3:
				return getString(R.string.cycle4).toUpperCase(l);
			case 4:
				return getString(R.string.cycle5).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_third_screen_prototype, container, false);
			return rootView;
		}
	}

}
