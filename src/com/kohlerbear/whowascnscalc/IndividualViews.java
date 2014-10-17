package com.kohlerbear.whowascnscalc;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class IndividualViews extends ActionBarActivity {
	
	TextView firstLiftTV;
	TextView firstLiftErrTV;
	TextView secondLiftTV;
	TextView secondLiftErrTV;
	TextView thirdLiftTV;
	TextView thirdLiftErrTV;
	
    ImageView lift1Barbell;
    ImageView l1r1;
    ImageView l1r2;
    ImageView l1r3;
    ImageView l1r4;
    ImageView l1r5;
    ImageView l1r6;
    ImageView l1r7;
    ImageView l1r8;
   
   
    ImageView l1l1;
    ImageView l1l2;
    ImageView l1l3;
    ImageView l1l4;
    ImageView l1l5;
    ImageView l1l6;
    ImageView l1l7;
    ImageView l1l8;

    //lift two declarations
    ImageView lift2Barbell;
    ImageView l2r1;
    ImageView l2r2;
    ImageView l2r3;
    ImageView l2r4;
    ImageView l2r5;
    ImageView l2r6;
    ImageView l2r7;
    ImageView l2r8;
   
    ImageView l2l1;
    ImageView l2l2;
    ImageView l2l3;
    ImageView l2l4;
    ImageView l2l5;
    ImageView l2l6;
    ImageView l2l7;
    ImageView l2l8;
   
   
    //lift three declarations
    ImageView lift3Barbell;
    ImageView l3r1;
    ImageView l3r2;
    ImageView l3r3;
    ImageView l3r4;
    ImageView l3r5;
    ImageView l3r6;
    ImageView l3r7;
    ImageView l3r8;
   
    ImageView l3l1;
    ImageView l3l2;
    ImageView l3l3;
    ImageView l3l4;
    ImageView l3l5;
    ImageView l3l6;
    ImageView l3l7;
    ImageView l3l8;
    
    ImageView nextButton;
    ImageView prevButton;
    
    Boolean usingLbs;
	Resources resources;
	static String viewMode;
	static String[] liftPattern;
	
	Tracker tracker = null;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_individual_views);
		
		tracker = GoogleAnalytics.getInstance(this).getTracker("UA-55018534-1");
		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME, "Second Screen");

		tracker.send(hitParameters);
	    //mode, viewmode, and lift pattern will be for if we support swipe navigation, however, I think that making cells bigger is probably better off.
		
		 firstLiftTV = (TextView) findViewById(R.id.indVLiftOne);
		 firstLiftErrTV = (TextView) findViewById(R.id.lift1ErrText);
		 secondLiftTV = (TextView) findViewById(R.id.indVLiftTwo);
		 secondLiftErrTV = (TextView) findViewById(R.id.lift2ErrText);
		 thirdLiftTV = (TextView) findViewById(R.id.indVLiftThree);
		 thirdLiftErrTV = (TextView) findViewById(R.id.lift3ErrText);
		 
		 nextButton = (ImageView) findViewById(R.id.nextButton);
		 nextButton.setOnClickListener( new OnClickListener (){

				@Override
				public void onClick(View v) {
					moveToNextLift();
				}});
		 
		 prevButton = (ImageView) findViewById(R.id.prevButton);
		 prevButton.setOnClickListener( new OnClickListener (){

				@Override
				public void onClick(View v) {
					moveToPrevLift();
				}});
		
		 //Lift one declarationss
		lift1Barbell = (ImageView) findViewById(R.id.liftOneBarbell);
	    l1r1 = (ImageView) findViewById(R.id.lift1r1);
	    l1r2 = (ImageView) findViewById(R.id.lift1r2);
	    l1r3 = (ImageView) findViewById(R.id.lift1r3);
	    l1r4 = (ImageView) findViewById(R.id.lift1r4);
	    l1r5 = (ImageView) findViewById(R.id.lift1r5);
	    l1r6 = (ImageView) findViewById(R.id.lift1r6);
	    l1r7 = (ImageView) findViewById(R.id.lift1r7);
	    l1r8 = (ImageView) findViewById(R.id.lift1r8);
	    
	    
	    l1l1 = (ImageView) findViewById(R.id.lift1l1);
	    l1l2 = (ImageView) findViewById(R.id.lift1l2);
	    l1l3 = (ImageView) findViewById(R.id.lift1l3);
	    l1l4 = (ImageView) findViewById(R.id.lift1l4);
	    l1l5 = (ImageView) findViewById(R.id.lift1l5);
	    l1l6 = (ImageView) findViewById(R.id.lift1l6);
	    l1l7 = (ImageView) findViewById(R.id.lift1l7);
	    l1l8 = (ImageView) findViewById(R.id.lift1l8);
		
	    //lift two declarations 
	    lift2Barbell = (ImageView) findViewById(R.id.liftTwoBarbell);
	    l2r1 = (ImageView) findViewById(R.id.lift2r1);
	    l2r2 = (ImageView) findViewById(R.id.lift2r2);
	    l2r3 = (ImageView) findViewById(R.id.lift2r3);
	    l2r4 = (ImageView) findViewById(R.id.lift2r4);
	    l2r5 = (ImageView) findViewById(R.id.lift2r5);
	    l2r6 = (ImageView) findViewById(R.id.lift2r6);
	    l2r7 = (ImageView) findViewById(R.id.lift2r7);
	    l2r8 = (ImageView) findViewById(R.id.lift2r8);
	    
	    l2l1 = (ImageView) findViewById(R.id.lift2l1);
	    l2l2 = (ImageView) findViewById(R.id.lift2l2);
	    l2l3 = (ImageView) findViewById(R.id.lift2l3);
	    l2l4 = (ImageView) findViewById(R.id.lift2l4);
	    l2l5 = (ImageView) findViewById(R.id.lift2l5);
	    l2l6 = (ImageView) findViewById(R.id.lift2l6);
	    l2l7 = (ImageView) findViewById(R.id.lift2l7);
	    l2l8 = (ImageView) findViewById(R.id.lift2l8);
	    
	    
	    //lift three declarations
	    lift3Barbell = (ImageView) findViewById(R.id.liftThreeBarbell);
	    l3r1 = (ImageView) findViewById(R.id.lift3r1);
	    l3r2 = (ImageView) findViewById(R.id.lift3r2);
	    l3r3 = (ImageView) findViewById(R.id.lift3r3);
	    l3r4 = (ImageView) findViewById(R.id.lift3r4);
	    l3r5 = (ImageView) findViewById(R.id.lift3r5);
	    l3r6 = (ImageView) findViewById(R.id.lift3r6);
	    l3r7 = (ImageView) findViewById(R.id.lift3r7);
	    l3r8 = (ImageView) findViewById(R.id.lift3r8);
	    
	    l3l1 = (ImageView) findViewById(R.id.lift3l1);
	    l3l2 = (ImageView) findViewById(R.id.lift3l2);
	    l3l3 = (ImageView) findViewById(R.id.lift3l3);
	    l3l4 = (ImageView) findViewById(R.id.lift3l4);
	    l3l5 = (ImageView) findViewById(R.id.lift3l5);
	    l3l6 = (ImageView) findViewById(R.id.lift3l6);
	    l3l7 = (ImageView) findViewById(R.id.lift3l7);
	    l3l8 = (ImageView) findViewById(R.id.lift3l8);
	    
		resources = getResources();

	    
		Intent prevScreen = getIntent();
	/*	intent.putExtra("cycle", myCycle);
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
		
	 */
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		
	    int cycle = Integer.valueOf(prevScreen.getStringExtra("cycle"));
	    String frequency = prevScreen.getStringExtra("frequency");
	    String liftType = prevScreen.getStringExtra("liftType");
	    double firstLift = Double.valueOf(prevScreen.getStringExtra("firstLift")); 
	    double secondLift = Double.valueOf(prevScreen.getStringExtra("secondLift")); 	
	    double thirdLift = Double.valueOf(prevScreen.getStringExtra("thirdLift")); 
	    String date = prevScreen.getStringExtra("date");
	    String mode = prevScreen.getStringExtra("mode");
	    viewMode = prevScreen.getStringExtra("viewMode");
	    liftPattern = prevScreen.getStringArrayExtra("liftPattern");
	    if (mode.equals("0"))
	    	usingLbs = false;
	    else
	    	usingLbs = true;
	    
	    generateWeights(firstLift, 1);
	    generateWeights(secondLift, 2);
	    generateWeights(thirdLift, 3);
//		cycleTV.setText("Cycle: " + String.valueOf(cycle));
	    setTitle(date);
	    TextView dataMarquee = (TextView) findViewById(R.id.dataMarquee);
	    dataMarquee.setText(liftType + " " + frequency  + "Cycle " + cycle);
		firstLiftTV.setText(String.valueOf("Lift 1: " + firstLift + "x" + frequency.charAt(0)));
		secondLiftTV.setText(String.valueOf("Lift 2: " + secondLift+ "x" + frequency.charAt(2)));
		thirdLiftTV.setText(String.valueOf("Lift 3: "  + thirdLift)+ "x" + frequency.charAt(4));
		
	}
    
    

    
	static double roundkg(double i) //first argument is rounded, 
	{
		return (double) (Math.round(i/2.5) * 2.5);
	}

	static double round(double i) //first argument is rounded, 
	{
		return (double) (Math.round(i/5) * 5);
	}
    
    
void generateWeights(double weight, int liftOneTwoOrThree)
    {

	 int barbellWeight;
	 double[] plateVals;
        //NEED CONDITIONAL HERE 
		if (!usingLbs)
		{
	        weight = roundkg(weight);
	        plateVals = new double[]{25, 20, 15, 10, 5, 2.5, 1.25};
	        //weight = weight(round to five)
	        barbellWeight = 20;
		}
			
		else
		{
        weight = round(weight);
        plateVals = new double[]{45, 25, 10, 5, 2.5};
        //weight = weight(round to five)
        barbellWeight = 45;
		}
        
        
        
        double plateWeight = weight - barbellWeight;
        int currentNeeded = 0;
        int plateValIterator = 0;
        int platePosition = 0;
        
        
        
        while (plateWeight != 0)
        {
            currentNeeded = (int) (plateWeight / (plateVals[plateValIterator] * 2));
            if (currentNeeded > 0)
            {
                plateWeight = (int) (plateWeight - (2 * currentNeeded * plateVals[plateValIterator]));
                System.out.println(plateVals[plateValIterator] + " needed per side " + currentNeeded);
                int numberOfTimesToAddPlateToEachSide = currentNeeded;
                while (numberOfTimesToAddPlateToEachSide >= 1)
                {
                    System.out.println("Added " + plateVals[plateValIterator] + "s to plate position " + platePosition);
                	setPlateImageAtRow(platePosition, plateValIterator, liftOneTwoOrThree);
                    platePosition++;
                    numberOfTimesToAddPlateToEachSide--;
                }
                System.out.println(platePosition);
            }
            plateValIterator++;
            
            
        }
    }

void setPlateImageAtRow(int row, int plateIndex, int liftNumber)
{
    

        if (liftNumber == 1)
        {
        	ImageView liftOneRightUIViews[] = {l1r1, l1r2, l1r3, l1r4, l1r5, l1r6, l1r7, l1r8};
        	ImageView liftOneLeftUIViews[] = {l1l1, l1l2, l1l3, l1l4, l1l5, l1l6, l1l7, l1l8};
        	if (row > 7)
        	{
        		firstLiftErrTV.setText("Not enough plates :(");
        		firstLiftErrTV.setVisibility(View.VISIBLE);
                for (int i = 0; i < 8; i++) //there has to be a better way to do this..
                {
                	liftOneRightUIViews[i].setVisibility(View.INVISIBLE);
                	liftOneLeftUIViews[i].setVisibility(View.INVISIBLE);
                	lift1Barbell.setVisibility(View.INVISIBLE);
                }
        	}
        		
        	else
        	{
            Drawable plateImage =  getPlateImageFor(plateIndex);
            liftOneRightUIViews[row].setImageDrawable(plateImage);
            liftOneLeftUIViews[row].setImageDrawable(plateImage);
            liftOneRightUIViews[row].setVisibility(View.VISIBLE);
            liftOneLeftUIViews[row].setVisibility(View.VISIBLE);
            
	            for (int i = row + 1; i < 8; i++) //there has to be a better way to do this..m
	            {
	            	liftOneRightUIViews[i].setVisibility(View.INVISIBLE);
	            	liftOneLeftUIViews[i].setVisibility(View.INVISIBLE);
	            }
        	}
        }
        
      if (liftNumber == 2)
        {
        	ImageView liftTwoRightUIViews[] = {l2r1, l2r2, l2r3, l2r4, l2r5, l2r6, l2r7, l2r8};
        	ImageView liftTwoLeftUIViews[] = {l2l1, l2l2, l2l3, l2l4, l2l5, l2l6, l2l7, l2l8};
        	if (row > 7)
        	{
        		secondLiftErrTV.setText("Not enough plates :(");
        		secondLiftErrTV.setVisibility(View.VISIBLE);
                for (int i = 0; i < 8; i++) //there has to be a better way to do this..
                {
                	liftTwoRightUIViews[i].setVisibility(View.INVISIBLE);
                	liftTwoLeftUIViews[i].setVisibility(View.INVISIBLE);
                	lift2Barbell.setVisibility(View.INVISIBLE);
                }
        	}
        		
        	else
        	{
            Drawable plateImage =  getPlateImageFor(plateIndex);
            liftTwoRightUIViews[row].setImageDrawable(plateImage);
            liftTwoLeftUIViews[row].setImageDrawable(plateImage);
            liftTwoRightUIViews[row].setVisibility(View.VISIBLE);
            liftTwoLeftUIViews[row].setVisibility(View.VISIBLE);
            
	            for (int i = row + 1; i < 8; i++) //clear extra plates
	            {
	            	liftTwoRightUIViews[i].setVisibility(View.INVISIBLE);
	            	liftTwoLeftUIViews[i].setVisibility(View.INVISIBLE);
	            }
        	}
        }
        
        if (liftNumber == 3)
        {
        	ImageView liftThreeRightUIViews[] = {l3r1, l3r2, l3r3, l3r4, l3r5, l3r6, l3r7, l3r8};
        	ImageView liftThreeLeftUIViews[] = {l3l1, l3l2, l3l3, l3l4, l3l5, l3l6, l3l7, l3l8};
        	if (row > 7)
        	{
        		thirdLiftErrTV.setText("Not enough plates :(");
        		thirdLiftErrTV.setVisibility(View.VISIBLE);
                for (int i = 0; i < 8; i++) //clear everything
                {
                	liftThreeRightUIViews[i].setVisibility(View.INVISIBLE);
                	liftThreeLeftUIViews[i].setVisibility(View.INVISIBLE);
                	lift3Barbell.setVisibility(View.INVISIBLE);
                }
        	}
        		
        	else
        	{
            Drawable plateImage =  getPlateImageFor(plateIndex);
            liftThreeRightUIViews[row].setImageDrawable(plateImage);
            liftThreeLeftUIViews[row].setImageDrawable(plateImage);
            liftThreeRightUIViews[row].setVisibility(View.VISIBLE);
            liftThreeLeftUIViews[row].setVisibility(View.VISIBLE);
        	
	            for (int i = row + 1; i < 8; i++) //clear extra plates
	            {
	            	liftThreeRightUIViews[i].setVisibility(View.INVISIBLE);
	            	liftThreeLeftUIViews[i].setVisibility(View.INVISIBLE);
	            }
        	}
        }
    
}
        
        
Drawable getPlateImageFor(int plateIndex)
        {
	
		if (usingLbs)
		{
            switch (plateIndex)
            {
                case 0:
                    return resources.getDrawable(R.drawable.plate_fourtyfive_lbs);
                case 1:
                    return resources.getDrawable(R.drawable.plate_twentyfive_lbs);
                case 2:
                    return resources.getDrawable(R.drawable.plate_ten_lbs);
                case 3:
                    return resources.getDrawable(R.drawable.plate_five_lbs);
                case 4:
                    return resources.getDrawable(R.drawable.plate_twopointfive_lbs);
                default:
                	{
                    Toast.makeText(IndividualViews.this, "A plate generation error has occured", Toast.LENGTH_LONG).show();
                	return resources.getDrawable(R.drawable.plate_fourtyfive_lbs);
                	} 
            }//end switch
			
		}
		else
		{
            switch (plateIndex)
            {
                case 0:
                    return resources.getDrawable(R.drawable.plate_twentyfive_kg);
                case 1:
                    return resources.getDrawable(R.drawable.plate_twenty_kg);
                case 2:
                    return resources.getDrawable(R.drawable.plate_fifteen_kg);
                case 3:
                    return resources.getDrawable(R.drawable.plate_ten_kg);
                case 4:
                    return resources.getDrawable(R.drawable.plate_five_kg);
                case 5:
                	return resources.getDrawable(R.drawable.plate_twopointfive_kg);
                case 6:
                	return resources.getDrawable(R.drawable.plate_onepointfive_kg);
                default:
                	{
                    Toast.makeText(IndividualViews.this, "A plate generation error has occured", Toast.LENGTH_LONG).show();
                	return resources.getDrawable(R.drawable.plate_fourtyfive_lbs);
                	} 
            }//end switch
		}//end else
          
}        
        

public boolean onOptionsItemSelected(MenuItem item){
    Intent myIntent = new Intent(getApplicationContext(), ThirdScreenActivity.class);
	myIntent.putExtra("origin", "individualViews");
	myIntent.putExtra("viewMode", viewMode);	
	myIntent.putExtra("liftPattern", liftPattern);	
	startActivity(myIntent);
    return true;

}


//swipe stuff




  	public void moveToNextLift() throws ParseException
	{
		Intent myIntent = getIntent();
		String DATE_FORMAT = "MM-dd-yyyy";
	    String date_string = myIntent.getStringExtra("date"); 
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

	    
	    String liftType = myIntent.getStringExtra("liftType"); 
		String viewMode = myIntent.getStringExtra("viewMode");
		String mode = myIntent.getStringExtra("mode");
		liftPattern = myIntent.getStringArrayExtra("liftPattern"); 
		//set lift pattern?
		String nextLift = null;
	    String incrementedString = null;
	    String[] result = new String[2];
	    ConfigTool helper = new ConfigTool(IndividualViews.this);
		result = helper.getNextLift(c1, liftPattern, liftType, viewMode);//getNextLiftDefault returns a result array which has nextLift and incrementedString
		nextLift = result[0];
		incrementedString = result[1];

			    
	    Intent nextLiftIntent = helper.configureNextSet(incrementedString, nextLift, viewMode, mode, liftPattern);
	    if (!helper.bottomCase())
	    {
	    	startActivity(nextLiftIntent);
			overridePendingTransition(0,R.anim.exit_slide_up);
	    }
	    
	   
	}
 

  	public void moveToPrevLift()
	{
		Intent myIntent = getIntent();
		
		String DATE_FORMAT = "MM-dd-yyyy";
	    String date_string = myIntent.getStringExtra("date"); 
	    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT, java.util.Locale.getDefault());
	    Date date = null;
		try {
	    date = (Date)sdf.parse(date_string);
		}
	    catch (java.text.ParseException e) {
			sendTrackerException("DateException", date_string);
			Toast.makeText(getApplicationContext(), "Error parsing date. An error report has been sent.", Toast.LENGTH_SHORT).show();
		}
	    Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(0);
	    c1.setTime(date);

	    
	    String liftType = myIntent.getStringExtra("liftType"); 
	    String nextLift = null;
	    String incrementedString = null;
		String viewMode = myIntent.getStringExtra("viewMode");
		String mode = myIntent.getStringExtra("mode");
	     String[] result = new String[2];
	     liftPattern = myIntent.getStringArrayExtra("liftPattern"); 
	     ConfigTool helper = new ConfigTool(IndividualViews.this);
		 result = helper.getPrevLift(c1, liftPattern, liftType, viewMode);//getNextLiftDefault returns a result array which has nextLift and incrementedString
		 nextLift = result[0];
		 incrementedString = result[1];
	    
	    
	    
	    Intent prevLiftIntent =  helper.configurePreviousSet(incrementedString, nextLift, viewMode, mode, liftPattern);
	    if (!helper.topCase())
	    {
	    	startActivity(prevLiftIntent);
	    	overridePendingTransition(0,R.anim.exit_slide_down);
	    }
	   
	}
  	
	private void sendTrackerException(String exceptionType, String value) {
		  tracker.send(MapBuilder
			      .createEvent("Exception",     // Event category (required)
			                   exceptionType,  // Event action (required)
			                   value,   // Event label
			                   null)            // Event value
			      .build());
		
	}
    


}


