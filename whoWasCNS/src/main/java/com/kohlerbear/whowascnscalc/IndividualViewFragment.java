package com.kohlerbear.whowascnscalc;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View rootView = inflater.inflate(R.layout.fragment_individual_view, container, false);
	    Bundle args = getArguments();
	     firstLiftTV = (TextView) rootView.findViewById(R.id.indVLiftOne);
		 firstLiftErrTV = (TextView) rootView.findViewById(R.id.lift1ErrText);
		 secondLiftTV = (TextView) rootView.findViewById(R.id.indVLiftTwo);
		 secondLiftErrTV = (TextView) rootView.findViewById(R.id.lift2ErrText);
		 thirdLiftTV = (TextView) rootView.findViewById(R.id.indVLiftThree);
		 thirdLiftErrTV = (TextView) rootView.findViewById(R.id.lift3ErrText);
		 
		
	 	
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
	
	   int cycle = args.getInt("cycle");
	   String frequency = args.getString("frequency");
	   String liftType = args.getString("liftType");
	   double firstLift = args.getDouble("firstLift"); 
	   double secondLift = args.getDouble("secondLift"); 	
	   double thirdLift = args.getDouble("thirdLift"); 
	   String date = args.getString("date");
	   String mode = args.getString("mode");
	   viewMode = args.getString("viewMode");
	   liftPattern = args.getStringArray("liftPattern");
	   if (mode.equals("0"))
	   	usingLbs = false;
	   else
	   	usingLbs = true;
	   
		
	   generateWeights(firstLift, 1);
	   generateWeights(secondLift, 2);
	   generateWeights(thirdLift, 3);
	   getActivity().setTitle(date);
	   TextView dataMarquee = (TextView) rootView.findViewById(R.id.dataMarquee);
	   dataMarquee.setText(liftType + " " + frequency  + " Cycle " + cycle);
		firstLiftTV.setText(String.valueOf("Set 1: " + firstLift + "x" + frequency.charAt(0)));
		secondLiftTV.setText(String.valueOf("Set 2: " + secondLift+ "x" + frequency.charAt(2)));
		thirdLiftTV.setText(String.valueOf("Set 3: "  + thirdLift)+ "x" + frequency.charAt(4));
	    
	    return rootView;
	}

	static double roundkg(double valueToBeRounded) 
	{
		return (double) (Math.round(valueToBeRounded/2.5) * 2.5);
	}
	
	static double round(double valueToBeRounded) 
	{
		return (double) (Math.round(valueToBeRounded/5) * 5);
	}


	static void generateWeights(double weight, int liftOneTwoOrThree)
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
		catch (Exception e)
		{
			//TODO fix analytics
		//		sendTrackerException("GenerateWeightsException", e.getLocalizedMessage());
		}
	}

	static void setPlateImageAtRow(int row, int plateIndex, int liftNumber)
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
    
    
	static Drawable getPlateImageFor(int plateIndex)
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
//	            	TODO analytics
//	                Toast.makeText(IndividualViewsPrototype.this, "A plate generation error has occured", Toast.LENGTH_LONG).show();
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
//		            	TODO analytics
	//                Toast.makeText(IndividualViewsPrototype.this, "A plate generation error has occured", Toast.LENGTH_LONG).show();
	            	return resources.getDrawable(R.drawable.plate_fourtyfive_lbs);
	            	} 
	        }
		}
	      
	}        

}