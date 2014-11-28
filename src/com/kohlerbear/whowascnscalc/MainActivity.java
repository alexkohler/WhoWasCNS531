package com.kohlerbear.whowascnscalc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;
import com.kohlerbear.whowascns.hannesorfmann.swipeback.SwipeForwardTransformer;




public class MainActivity extends BaseActivity {

	//private EasyTracker easyTracker = null;
	
	//to be used in activity 3 - may not be most efficient but for testing purposes 
	public static DatePicker dp;
	static int startingDateDay;
	static int startingDateMonth;
	static int startingDateYear;
	DrawerLayout drawerLayout;  //declare this globally
	String[] defaultPattern = {"Bench", "Squat", "Rest", "OHP", "Deadlift", "Rest"};
	String[] liftPattern = new String[7];
	TextView liftTicker;
	Tracker tracker = null;
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	SwipeBack swipebackInst;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setTheme(R.style.AppBaseTheme);
		setContentView(R.layout.activity_main);
		// Init the swipe back mechanism
		swipebackInst = SwipeBack.attach(this, Position.RIGHT)
		.setContentView(R.layout.activity_main)
		.setSwipeBackView(R.layout.swipeback_forward)
		.setSwipeBackTransformer(new SwipeForwardTransformer(this));
		
		
		//Set up our navigation drawer
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load
																					// titles
																					// from
																					// strings.xml

		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);// load icons from
															// strings.xml

		set(navMenuTitles, navMenuIcons);

 
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); 
 
        // just styling option add shadow the right edge of the drawer
   //drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
		//Initialize tracker
        
		// Sending the same screen view hit using MapBuilder.createAppView()
		GoogleAnalytics.getInstance(this).getTracker("UA-55018534-1");
		tracker = GoogleAnalytics.getInstance(this).getTracker("UA-55018534-1");
		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME, "Home Screen");

		tracker.send(hitParameters);

		dp = (DatePicker) findViewById(R.id.dp);
		dp.setCalendarViewShown(false);
		final Button setBtn = (Button) findViewById(R.id.set);
		liftTicker = (TextView) findViewById(R.id.liftTicker);
		
		Intent patternAdjusterIntent = getIntent();
		String origin = patternAdjusterIntent.getStringExtra("origin");

		
		if (origin != null)//if we came from patternAdjuster
			liftPattern = patternAdjusterIntent.getStringArrayExtra("liftPattern");
		else
			liftPattern = defaultPattern;
		
		String liftTickerBuffer = "Current lift Pattern";
		for (int i=0; i < liftPattern.length; i++)
			liftTickerBuffer = liftTickerBuffer + " " + liftPattern[i].substring(0, 1) + " - ";
		liftTickerBuffer = liftTickerBuffer.substring(0, liftTickerBuffer.length() - 2); //remove last dash
		
		liftTicker.setText(liftTickerBuffer);
		

		setBtn.setOnClickListener(new OnClickListener() {//TODO move this out of inline declaration, you're cluttering up onCreate

			@Override
			public void onClick(View v) {
				goToSecond();
//				finish();
			}
		});
		
		
        // Gesture detection
	    final GestureDetector gestureDetector;
//	    View.OnTouchListener gestureListener;
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        View.OnTouchListener gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        
        //these collide with navdrawer listener
       drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
       drawerLayout.setOnTouchListener(gestureListener);
       //set up our modified swipeback implementation
       
       




	}//end method onCreate 
	
	 @Override
	  public void onStart() {
	    super.onStart();
	    EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	  }
	  @Override
	  public void onStop() {
	    super.onStop();
	    EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	  }
	  @Override
	  public void onResume(){//onresume will NEVER be called before oncreate
		  super.onResume();
		  swipebackInst.close(false);
		  swipebackInst.close();
	  }
	  
		class MyGestureDetector extends SimpleOnGestureListener {
			
		    private static final int SWIPE_MIN_DISTANCE = 170;
		    private static final int SWIPE_THRESHOLD_VELOCITY = 100;
		
	        @Override
	        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	            try {
	               // if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
	                //    return false;
	                // right to left swipe
	                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	                	goToSecond();
	                }  
	                
	                //no need to support left swipe
	                //else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//	                	Toast.makeText(MainActivity.this, "Left", Toast.LENGTH_SHORT).show();
	                //}
	                
	                
	                
	                
	                
	            } catch (Exception e) {
	                // nothing
	            }
	            return false;
	        }
		}



		private void goToSecond()
		{
			Toast.makeText(MainActivity.this, "Date Selected: " + (dp.getMonth() + 1) + "-" + dp.getDayOfMonth() + "-" + dp.getYear(),  Toast.LENGTH_SHORT).show();
			startingDateDay = dp.getDayOfMonth();
			startingDateMonth = dp.getMonth();
			startingDateYear = dp.getYear();
			
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(0);
			cal.set(startingDateYear, startingDateMonth, startingDateDay);

			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", java.util.Locale.getDefault());
			Date myDate = cal.getTime();
			String formattedDate = dateFormat.format(myDate);
			//if this db does not have a database....
			EventsDataSQLHelper eventsData = new EventsDataSQLHelper(this);
			SQLiteDatabase db = eventsData.getWritableDatabase();
			//TODO check repercussions of tis...
	//		db.execSQL("drop table Lifts");
			//db.execSQL("create table Lifts (liftDate text not null, Cycle integer, Lift text not null, Frequency text not null, First_Lift real, Second_Lift real, Third_Lift real, Training_Max integer, column_LbFlag integer, RoundFlag integer, pattern text not null)"); //TODO why is there here?
			Intent intent = new Intent(MainActivity.this, REVAMPEDSecondScreenActivity.class); //TODO just change this back to second if things go awry
			intent.putExtra("key", formattedDate );
			intent.putExtra("origin", "first");
			intent.putExtra("liftPattern", liftPattern);
			
			
			db.close();
			startActivity(intent);
//			overridePendingTransition(R.anim.exit_slide_right,R.anim.exit_slide_right);


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
