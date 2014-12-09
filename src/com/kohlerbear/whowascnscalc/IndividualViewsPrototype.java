package com.kohlerbear.whowascnscalc;



import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
/**
 * Prototype class for base of individualViews 
 * 
 */
public class IndividualViewsPrototype extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments representing
     * each object in a collection. We use a {@link android.support.v4.app.FragmentStatePagerAdapter}
     * derivative, which will destroy and re-create fragments as needed, saving and restoring their
     * state in the process. This is important to conserve memory and is a best practice when
     * allowing navigation between objects in a potentially large collection.
     */

    /**
     * The {@link android.support.v4.view.ViewPager} that will display the object collection.
     */
    ViewPager mViewPager;
    
	Tracker tracker = null; 
    public enum liftRetrievalDirection{
  	  NEXT, PREV;
    }
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_view_proto);
        
        //Set up analytics tracking 
		tracker = GoogleAnalytics.getInstance(this).getTracker("UA-55018534-1");
		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME, "IndV Prototype");

		tracker.send(hitParameters);
        

        // Create an adapter that when requested, will return a fragment representing an object in
        // the collection.
        // 
        // ViewPager and its adapters use support library fragments, so we must use
        // getSupportFragmentManager.


      // Set up the ViewPager, attaching the adapter.
	  mViewPager = (ViewPager) findViewById(R.id.pager);
      List<Fragment> fragments = new Vector<Fragment>();

      //for each fragment you want to add to the pager
      Bundle initialArgs = new Bundle();
      Intent prevScreen = getIntent();
 	  int cycle = Integer.valueOf(prevScreen.getStringExtra("cycle"));
 	  initialArgs.putInt("cycle",cycle);
      String frequency = prevScreen.getStringExtra("frequency");
      initialArgs.putString("frequency", frequency);
      String liftType = prevScreen.getStringExtra("liftType");
      initialArgs.putString("liftType", liftType);
      double firstLift = Double.valueOf(prevScreen.getStringExtra("firstLift")); 
      initialArgs.putDouble("firstLift", firstLift);
      double secondLift = Double.valueOf(prevScreen.getStringExtra("secondLift"));          
      initialArgs.putDouble("secondLift", secondLift);
      double thirdLift = Double.valueOf(prevScreen.getStringExtra("thirdLift")); 
      initialArgs.putDouble("thirdLift", thirdLift);
      String date = prevScreen.getStringExtra("date");
      initialArgs.putString("date", date);
      String mode = prevScreen.getStringExtra("mode");
      initialArgs.putString("mode", mode);
      String viewMode = prevScreen.getStringExtra("viewMode");
      initialArgs.putString("viewMode", viewMode);
      String[] liftPattern = prevScreen.getStringArrayExtra("liftPattern");
      initialArgs.putStringArray("liftPattern", liftPattern);
      initialArgs.putString("status", "init");
      int selectedItem = 0;//selected item is initially one, but if we have any previous lifts we need to increment out selected item
      //Add all fragments before our clicked fragment
      Bundle prevLiftArgs = getLiftBasedOn(initialArgs, liftRetrievalDirection.PREV, cycle);
      while (!prevLiftArgs.getString("status").equals("invalid"))
      {
    	  if (prevLiftArgs.getString("date") != null)
    	  {
    		  fragments.add(Fragment.instantiate(this,IndividualViewFragment.class.getName(), prevLiftArgs));
    		  selectedItem++;
    	  }
    	  prevLiftArgs = getLiftBasedOn(prevLiftArgs, liftRetrievalDirection.PREV, cycle);
      }
      
      //our originally clicked fragment
      fragments.add(Fragment.instantiate(this,IndividualViewFragment.class.getName(),initialArgs));//add our inital fragment
      
      //Add all fragments after our clicked fragment
      Bundle nextLiftArgs = getLiftBasedOn(initialArgs, liftRetrievalDirection.NEXT, cycle);
      while (!nextLiftArgs.getString("status").equals("invalid"))
      {
    	  if (nextLiftArgs.getString("date") != null)
    		  fragments.add(Fragment.instantiate(this,IndividualViewFragment.class.getName(), nextLiftArgs));

    	  nextLiftArgs = getLiftBasedOn(nextLiftArgs, liftRetrievalDirection.NEXT, cycle);
      }
      
      PagerAdapter mPagerAdapter  = new MyFragmentAdapter(super.getSupportFragmentManager(), fragments);
      mViewPager.setAdapter(mPagerAdapter);
    
      mViewPager.setCurrentItem(selectedItem);
    
    }
    public Bundle getLiftBasedOn(Bundle prevArgs, liftRetrievalDirection direction, int currentCycleSection) throws ParseException
	{
		String DATE_FORMAT = "MM-dd-yy";
	    String date_string = prevArgs.getString("date"); 
	    Bundle nextLiftArgs = new Bundle();
	    if (date_string != null)
	    {
		    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT, java.util.Locale.getDefault());
		    Date date = null;
			try {
				date = (Date)sdf.parse(date_string);
			} catch (java.text.ParseException e) {
				sendTrackerException("DateException", date_string);
				Toast.makeText(getApplicationContext(), "Error parsing date. An error report has been sent.", Toast.LENGTH_SHORT).show();
			}
		    Calendar c1 = Calendar.getInstance();
			c1.setTimeInMillis(0);
		    c1.setTime(date);
	
		    
		    String liftType = prevArgs.getString("liftType"); 
			String viewMode = prevArgs.getString("viewMode");
			String mode = prevArgs.getString("mode");
			String [] liftPattern = prevArgs.getStringArray("liftPattern"); 
			//set lift pattern?
			String nextLift = null;
		    String incrementedString = null;
		    String[] result = new String[2];
		    FragmentConfigTool helper = new FragmentConfigTool(getApplicationContext());
		    if (direction == liftRetrievalDirection.NEXT)
		    {
		    	result = helper.getNextLift(c1, liftPattern, liftType, viewMode);//getNextLiftDefault returns a result array which has nextLift and incrementedString
				nextLift = result[0];
				incrementedString = result[1];
		    	nextLiftArgs= helper.configureNextSet(incrementedString, nextLift, viewMode, mode, liftPattern, getApplicationContext(), currentCycleSection);// will need to retrieve cycle eventually
		    }
		    else
		    {
		    	result = helper.getPrevLift(c1, liftPattern, liftType, viewMode);
				nextLift = result[0];
				incrementedString = result[1];
		    	nextLiftArgs= helper.configurePrevSet(incrementedString, nextLift, viewMode, mode, liftPattern, getApplicationContext(), currentCycleSection);// will need to retrieve cycle eventually
		    }
	
				    
		    
		    if (helper.topCase() == true || helper.bottomCase() == true)//redundant but for my sanity
		    	nextLiftArgs.putString("status", "invalid");
		    else
		    	nextLiftArgs.putString("status", "valid");
	    }
	    else
	    	nextLiftArgs.putString("status", "invalid");
	    
	    
	    return nextLiftArgs;
  	}
    
    
    public static class MyFragmentAdapter extends FragmentPagerAdapter {

    	public static int pos = 0;

    	private List<Fragment> myFragments;

    	public MyFragmentAdapter(FragmentManager fm, List<Fragment> myFrags) {
    	    super(fm);
    	    myFragments = myFrags;
    		myFragments.removeAll(Collections.singleton(null));
    	}

    	@Override
    	public Fragment getItem(int position) {

    	    return myFragments.get(position);

    	}

    	@Override
    	public int getCount() {

    	    return myFragments.size();
    	}

    	@Override
    	public CharSequence getPageTitle(int position) {

    	   Bundle args = myFragments.get(position).getArguments();
    	   String truncatedDate = args.getString("date").substring(0, 5);
    	   String lift = args.getString("liftType");
    	    String PageTitle = truncatedDate + "- " + lift;
    	    return PageTitle;
    	}

    	public static int getPos() {
    	    return pos;
    	}

    	 public static void setPos(int pos) {
    	    MyFragmentAdapter.pos = pos;
    	 }
    	}
    
    
	protected void sendTrackerException(String exceptionType, String value) {
		Tracker tracker = GoogleAnalytics.getInstance(getApplicationContext()).getTracker("UA-55018534-1");
		  tracker.send(MapBuilder
			      .createEvent("Exception",     // Event category (required)
			                   exceptionType,  // Event action (required)
			                   value,   // Event label
			                   null)            // Event value
			      .build());
		
	}
    
}

