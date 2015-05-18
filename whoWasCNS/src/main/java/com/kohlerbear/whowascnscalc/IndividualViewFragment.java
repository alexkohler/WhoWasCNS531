package com.kohlerbear.whowascnscalc;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

/**
 * Fragment class that populates our fragment adapter
 */
public class IndividualViewFragment extends Fragment {

	public static IndividualViewFragment newInstance() {
	
	final IndividualViewFragment mf = new IndividualViewFragment ();
	
	    return mf;
	}

	public IndividualViewFragment() {}

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	}
		static TextView firstLiftTV;
		static TextView firstLiftErrTV;
		static TextView secondLiftTV;
		static TextView secondLiftErrTV;
		static TextView thirdLiftTV;
		static TextView thirdLiftErrTV;
		
		static ImageView lift1Barbell;
		static ImageView l1r1;
		static ImageView l1r2;
		static ImageView l1r3;
		static ImageView l1r4;
		static ImageView l1r5;
		static ImageView l1r6;
		static ImageView l1r7;
		static ImageView l1r8;
		
		
		static ImageView l1l1;
		static ImageView l1l2;
		static ImageView l1l3;
		static ImageView l1l4;
		static ImageView l1l5;
		static ImageView l1l6;
		static ImageView l1l7;
		static ImageView l1l8;
		
		//lift two declarations
		static ImageView lift2Barbell;
		static ImageView l2r1;
		static ImageView l2r2;
		static ImageView l2r3;
		static ImageView l2r4;
		static ImageView l2r5;
		static ImageView l2r6;
		static ImageView l2r7;
		static ImageView l2r8;
		
		static ImageView l2l1;
		static ImageView l2l2;
		static ImageView l2l3;
		static ImageView l2l4;
		static ImageView l2l5;
		static ImageView l2l6;
		static ImageView l2l7;
		static ImageView l2l8;
		
		
		//lift three declarations
		static ImageView lift3Barbell;
		static ImageView l3r1;
		static ImageView l3r2;
		static ImageView l3r3;
		static ImageView l3r4;
		static ImageView l3r5;
		static ImageView l3r6;
		static ImageView l3r7;
		static ImageView l3r8;
		
		static ImageView l3l1;
		static ImageView l3l2;
		static ImageView l3l3;
		static ImageView l3l4;
		static ImageView l3l5;
		static ImageView l3l6;
		static ImageView l3l7;
		static ImageView l3l8;
		
		//ImageView nextButton;
		//ImageView prevButton;
		
		static Boolean usingLbs;
		static Resources resources;
		static String viewMode;
		static String[] liftPattern;

		static EditText l1EditText;
		static EditText l2EditText;
	    static EditText l3EditText;

		//Information we need to persist data in database
		static String m_liftDate;
		static String m_liftDay;//Lift type and lift name are the same in the case of our main compound for the day
		static String m_liftName;
		static String m_frequency; //frequency probably wouldn't be bad to know either
		static double m_firstLiftWeight;
		static char m_firstLiftReps;
		static double m_secondLiftWeight;
		static char m_secondLiftReps;
		static double m_thirdLiftWeight;
		static char m_thirdLiftReps;
		static int m_cycle;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View rootView = inflater.inflate(R.layout.fragment_individual_view, container, false);
	    Bundle 	args = getArguments();
	     firstLiftTV = (TextView) rootView.findViewById(R.id.indVLiftOne);
		 firstLiftErrTV = (TextView) rootView.findViewById(R.id.lift1ErrText);
		 secondLiftTV = (TextView) rootView.findViewById(R.id.indVLiftTwo);
		 secondLiftErrTV = (TextView) rootView.findViewById(R.id.lift2ErrText);
		 thirdLiftTV = (TextView) rootView.findViewById(R.id.indVLiftThree);
		 thirdLiftErrTV = (TextView) rootView.findViewById(R.id.lift3ErrText);


		//Handle some color scheme fun
		l1EditText = (EditText) rootView.findViewById(R.id.repsHit1editText);
		l2EditText = (EditText) rootView.findViewById(R.id.repsHit2editText);
		l3EditText = (EditText) rootView.findViewById(R.id.repsHit3editText);
		int primaryColor = ColorManager.getInstance(getActivity()).getPrimaryColor();
		l1EditText.getBackground().setColorFilter(primaryColor, PorterDuff.Mode.SRC_ATOP);
		l2EditText.getBackground().setColorFilter(primaryColor, PorterDuff.Mode.SRC_ATOP);
		l3EditText.getBackground().setColorFilter(primaryColor, PorterDuff.Mode.SRC_ATOP);
	 	
	   //Lift one declarationss
       lift1Barbell = (ImageView) rootView.findViewById(R.id.liftOneBarbell);
	   l1r1 = (ImageView) rootView.findViewById(R.id.lift1r1);
	   l1r2 = (ImageView) rootView.findViewById(R.id.lift1r2);
	   l1r3 = (ImageView) rootView.findViewById(R.id.lift1r3);
	   l1r4 = (ImageView) rootView.findViewById(R.id.lift1r4);
	   l1r5 = (ImageView) rootView.findViewById(R.id.lift1r5);
	   l1r6 = (ImageView) rootView.findViewById(R.id.lift1r6);
	   l1r7 = (ImageView) rootView.findViewById(R.id.lift1r7);
	   l1r8 = (ImageView) rootView.findViewById(R.id.lift1r8);
	   
	   
	   l1l1 = (ImageView) rootView.findViewById(R.id.lift1l1);
	   l1l2 = (ImageView) rootView.findViewById(R.id.lift1l2);
	   l1l3 = (ImageView) rootView.findViewById(R.id.lift1l3);
	   l1l4 = (ImageView) rootView.findViewById(R.id.lift1l4);
	   l1l5 = (ImageView) rootView.findViewById(R.id.lift1l5);
	   l1l6 = (ImageView) rootView.findViewById(R.id.lift1l6);
	   l1l7 = (ImageView) rootView.findViewById(R.id.lift1l7);
	   l1l8 = (ImageView) rootView.findViewById(R.id.lift1l8);
		
	   //lift two declarations 
	   lift2Barbell = (ImageView) rootView.findViewById(R.id.liftTwoBarbell);
	   l2r1 = (ImageView) rootView.findViewById(R.id.lift2r1);
	   l2r2 = (ImageView) rootView.findViewById(R.id.lift2r2);
	   l2r3 = (ImageView) rootView.findViewById(R.id.lift2r3);
	   l2r4 = (ImageView) rootView.findViewById(R.id.lift2r4);
	   l2r5 = (ImageView) rootView.findViewById(R.id.lift2r5);
	   l2r6 = (ImageView) rootView.findViewById(R.id.lift2r6);
	   l2r7 = (ImageView) rootView.findViewById(R.id.lift2r7);
	   l2r8 = (ImageView) rootView.findViewById(R.id.lift2r8);
	   
	   l2l1 = (ImageView) rootView.findViewById(R.id.lift2l1);
	   l2l2 = (ImageView) rootView.findViewById(R.id.lift2l2);
	   l2l3 = (ImageView) rootView.findViewById(R.id.lift2l3);
	   l2l4 = (ImageView) rootView.findViewById(R.id.lift2l4);
	   l2l5 = (ImageView) rootView.findViewById(R.id.lift2l5);
	   l2l6 = (ImageView) rootView.findViewById(R.id.lift2l6);
	   l2l7 = (ImageView) rootView.findViewById(R.id.lift2l7);
	   l2l8 = (ImageView) rootView.findViewById(R.id.lift2l8);
	   
	   
	   //lift three declarations
	   lift3Barbell = (ImageView) rootView.findViewById(R.id.liftThreeBarbell);
	   l3r1 = (ImageView) rootView.findViewById(R.id.lift3r1);
	   l3r2 = (ImageView) rootView.findViewById(R.id.lift3r2);
	   l3r3 = (ImageView) rootView.findViewById(R.id.lift3r3);
	   l3r4 = (ImageView) rootView.findViewById(R.id.lift3r4);
	   l3r5 = (ImageView) rootView.findViewById(R.id.lift3r5);
	   l3r6 = (ImageView) rootView.findViewById(R.id.lift3r6);
	   l3r7 = (ImageView) rootView.findViewById(R.id.lift3r7);
	   l3r8 = (ImageView) rootView.findViewById(R.id.lift3r8);
	   
	   l3l1 = (ImageView) rootView.findViewById(R.id.lift3l1);
	   l3l2 = (ImageView) rootView.findViewById(R.id.lift3l2);
	   l3l3 = (ImageView) rootView.findViewById(R.id.lift3l3);
	   l3l4 = (ImageView) rootView.findViewById(R.id.lift3l4);
	   l3l5 = (ImageView) rootView.findViewById(R.id.lift3l5);
	   l3l6 = (ImageView) rootView.findViewById(R.id.lift3l6);
	   l3l7 = (ImageView) rootView.findViewById(R.id.lift3l7);
	   l3l8 = (ImageView) rootView.findViewById(R.id.lift3l8);
	   
		resources = getResources();
	
	   /*int cycle*/ m_cycle = args.getInt("cycle");
	   /*String frequency*/ m_frequency = args.getString("frequency");
	   /*String liftType*/ m_liftDay = args.getString("liftType");
		m_liftName = m_liftDay; //only in the case of compounds
	   m_firstLiftWeight = args.getDouble("firstLift");
	   m_firstLiftReps = m_frequency.charAt(0);
	   m_secondLiftWeight = args.getDouble("secondLift");
       m_secondLiftReps = m_frequency.charAt(2);
	   m_thirdLiftWeight = args.getDouble("thirdLift");
       m_thirdLiftReps = m_frequency.charAt(4);
	   /*String date*/m_liftDate = args.getString("date");
	   String mode = args.getString("mode");
	   viewMode = args.getString("viewMode");
	   liftPattern = args.getStringArray("liftPattern");
	   if (mode.equals("0"))
	   	usingLbs = false;
	   else
	   	usingLbs = true;
	   
		
	   generateWeights(m_firstLiftWeight, 1);
	   generateWeights(m_secondLiftWeight, 2);
	   generateWeights(m_thirdLiftWeight, 3);
	   getActivity().setTitle(m_liftDate);
	   TextView dataMarquee = (TextView) rootView.findViewById(R.id.dataMarquee);
	   dataMarquee.setText(m_liftDay + " " + m_frequency  + " Cycle " + m_cycle);
       firstLiftTV.setText(String.valueOf("Set 1: " + m_firstLiftWeight + "x" + m_firstLiftReps));
	   secondLiftTV.setText(String.valueOf("Set 2: " + m_secondLiftWeight+ "x" + m_secondLiftReps));
	   thirdLiftTV.setText(String.valueOf("Set 3: "  + m_thirdLiftWeight)+ "x" + m_thirdLiftReps);
	    
	    return rootView;
	}



	double roundkg(double valueToBeRounded)
	{
		return (double) (Math.round(valueToBeRounded/2.5) * 2.5);
	}
	
	double round(double valueToBeRounded)
	{
		return (double) (Math.round(valueToBeRounded/5) * 5);
	}


	void generateWeights(double weight, int liftOneTwoOrThree)
	{
		try
		{
		 int barbellWeight;
		 double[] plateVals;
			if (!usingLbs)
			{
		        weight = roundkg(weight);
		        plateVals = new double[]{25, 20, 15, 10, 5, 2.5, 1.25};
		        //weight = weight(round to 2.5)
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
		        if (currentNeeded > 0 && plateValIterator < plateVals.length) 
		        {
		        	if (usingLbs)
		            plateWeight = (int) (plateWeight - (2 * currentNeeded * plateVals[plateValIterator]));
		        	else
		        	plateWeight = (double) (plateWeight - (2 * currentNeeded * plateVals[plateValIterator]));
//		            System.out.println(plateVals[plateValIterator] + " needed per side " + currentNeeded);
		            int numberOfTimesToAddPlateToEachSide = currentNeeded;
		            while (numberOfTimesToAddPlateToEachSide >= 1)
		            {
//		                System.out.println("Added " + plateVals[plateValIterator] + "s to plate position " + platePosition);
		            	setPlateImageAtRow(platePosition, plateValIterator, liftOneTwoOrThree);
		                platePosition++;
		                numberOfTimesToAddPlateToEachSide--;
		            }
//		            System.out.println(platePosition);
		        }
		        plateValIterator++;
		        
		        
		    }
		}
		catch (Exception e)
		{
				sendTrackerException("GenerateWeightsException", e.getLocalizedMessage());
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
	            for (int i = 0; i < 8; i++) 
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
	        
	            for (int i = row + 1; i < 8; i++) 
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
	            for (int i = 0; i < 8; i++) 
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
//	                Toast.makeText(IndividualViewsPrototype.this, "A plate generation error has occured", Toast.LENGTH_LONG).show();
                    sendTrackerException("getPlateImageFor(pounds) hit default case", "plateIndex:" +  plateIndex);
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
	//                Toast.makeText(IndividualViewsPrototype.this, "A plate generation error has occured", Toast.LENGTH_LONG).show();
                    sendTrackerException("getPlateImageFor(kilograms) hit default case", "plateIndex:" +  plateIndex);
	            	return resources.getDrawable(R.drawable.plate_fourtyfive_lbs);
	            	} 
	        }
		}
	      
	}

	//Once this goes out of view, the viewpager handler will call this method and the data will be created or updated in the progress database
	public void persistData() {
		//Things we need to know: Date, lift day, accessory name, weight, reps
        //Make sure textviews aren't empty, otherwise don't enter an entry. (So do an individual entry for each textview)
		//public LongTermEvent(String liftDate, String cycle, String liftType, String liftName, String frequency, double firstLift, int firstLiftReps, double theoOneRepMax, boolean lbs)
        if (l1EditText.getText().toString().trim().length() != 0) {//if the edittext is not empty
			LongTermEvent liftOneEntry = new LongTermEvent(m_liftDate, String.valueOf(m_cycle), m_liftDay, m_liftName, m_frequency, m_firstLiftWeight, m_firstLiftReps, usingLbs);
			LongTermDataSQLHelper helper = new LongTermDataSQLHelper(getActivity().getApplicationContext());
			//helper.getWritableDatabase().execSQL("create table LongTermRecords (liftDate text not null, Cycle text not null, Lift_Type text not null, Lift_name text not null, Frequency text not null, Weight real, Reps integer, Theoretical_Onerep real, Lb_Flag integer);");
			helper.addEvent(liftOneEntry);
//			Cursor test = helper.getWritableDatabase().rawQuery("SELECT * FROM LongTermRecords", null);
//			String dump = DatabaseUtils.dumpCursorToString(test);
//			System.out.println(dump);
		}
        if (l2EditText.getText().toString().trim().length() != 0) {
			LongTermEvent liftTwoEntry = new LongTermEvent(m_liftDate, String.valueOf(m_cycle), m_liftDay, m_liftName, m_frequency, m_secondLiftWeight, m_secondLiftReps, usingLbs);
			LongTermDataSQLHelper helper = new LongTermDataSQLHelper(getActivity().getApplicationContext());
			helper.addEvent(liftTwoEntry);
        }
        if (l3EditText.getText().toString().trim().length() != 0) {
			LongTermEvent liftThreeEntry = new LongTermEvent(m_liftDate, String.valueOf(m_cycle), m_liftDay, m_liftName, m_frequency, m_thirdLiftWeight, m_thirdLiftReps, usingLbs);
			LongTermDataSQLHelper helper = new LongTermDataSQLHelper(getActivity().getApplicationContext());
			helper.addEvent(liftThreeEntry);
        }

	}


    protected void sendTrackerException(String exceptionType, String value) {
        Tracker tracker = GoogleAnalytics.getInstance(getActivity()).getTracker("UA-55018534-1");
        tracker.send(MapBuilder
                .createEvent("Exception",     // Event category (required)
                        exceptionType,  // Event action (required)
                        value,   // Event label
                        null)            // Event value
                .build());

    }

}