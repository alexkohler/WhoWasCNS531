package com.kohlerbear.whowascnscalc;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kohlerbear.whowascnscalc.R;
import com.kohlerbear.whowascnscalc.ThirdScreenActivity;
public class IndividualViewActivity extends Activity implements OnClickListener {

	static int CURRENT_LEFT_ANCHOR_ID  = 0;
	static int CURRENT_RIGHT_ANCHOR_ID = 0;
	static int CURRENT_LEFT_ID         = 0;
	static int CURRENT_RIGHT_ID        = 0;
	static int PREV_ID_HOLDER		   = 0;

	static final int BARBELL_ID			   = 7000;
	static final int RIGHT_PLATE_ID_DEPTH1 = 7011;
	static final int LEFT_PLATE_ID_DEPTH1  = 7012;
	static final int RIGHT_PLATE_ID_DEPTH2 = 7021;
	static final int LEFT_PLATE_ID_DEPTH2  = 7022;
	static final int RIGHT_PLATE_ID_DEPTH3 = 7031;
	static final int LEFT_PLATE_ID_DEPTH3  = 7032;
	static final int RIGHT_PLATE_ID_DEPTH4 = 7041;
	static final int LEFT_PLATE_ID_DEPTH4  = 7042;
	static final int RIGHT_PLATE_ID_DEPTH5 = 7051;
	static final int LEFT_PLATE_ID_DEPTH5  = 7052;
	static final int RIGHT_PLATE_ID_DEPTH6 = 7061;
	static final int LEFT_PLATE_ID_DEPTH6  = 7062;
	static final int RIGHT_PLATE_ID_DEPTH7 = 7071;
	static final int LEFT_PLATE_ID_DEPTH7  = 7072;
	static final int RIGHT_PLATE_ID_DEPTH8 = 7081;
	static final int LEFT_PLATE_ID_DEPTH8  = 7082;
	static final int RIGHT_PLATE_ID_DEPTH9 = 7091;
	static final int LEFT_PLATE_ID_DEPTH9  = 7092;//enough for 855

	//static final int 

	ImageView CURRENT_LEFT_IMAGEVIEW;
	ImageView CURRENT_RIGHT_IMAGEVIEW;
	ImageView barbellImageView;
	ImageView RIGHTFORTYFIVEIMAGEVIEW; 
	ImageView LEFTFORTYFIVEIMAGEVIEW;
	ImageView RIGHTFORTYFIVEIMAGEVIEW_DEPTH2;
	ImageView LEFTFORTYFIVEIMAGEVIEW_DEPTH2;
	ImageView RIGHTFORTYFIVEIMAGEVIEW_DEPTH3;
	ImageView LEFTFORTYFIVEIMAGEVIEW_DEPTH3;
	ImageView RIGHTFORTYFIVEIMAGEVIEW_DEPTH4;
	ImageView LEFTFORTYFIVEIMAGEVIEW_DEPTH4;
	ImageView RIGHTFORTYFIVEIMAGEVIEW_DEPTH5;
	ImageView LEFTFORTYFIVEIMAGEVIEW_DEPTH5;
	ImageView RIGHTFORTYFIVEIMAGEVIEW_DEPTH6;
	ImageView LEFTFORTYFIVEIMAGEVIEW_DEPTH6;
	ImageView RIGHTFORTYFIVEIMAGEVIEW_DEPTH7;
	ImageView LEFTFORTYFIVEIMAGEVIEW_DEPTH7;
	ImageView RIGHTFORTYFIVEIMAGEVIEW_DEPTH8;
	ImageView LEFTFORTYFIVEIMAGEVIEW_DEPTH8;

	RelativeLayout relativeLayout;


	RelativeLayout.LayoutParams CURRENT_LEFT_LP;
	RelativeLayout.LayoutParams CURRENT_RIGHT_LP;	
	RelativeLayout.LayoutParams BARBELL_LP;
	RelativeLayout.LayoutParams RIGHTPLATE_LP;
	RelativeLayout.LayoutParams LEFTPLATE_LP;
	RelativeLayout.LayoutParams RIGHTPLATE_LP2; //number represents depth
	RelativeLayout.LayoutParams LEFTPLATE_LP2;
	RelativeLayout.LayoutParams RIGHTPLATE_LP3;
	RelativeLayout.LayoutParams LEFTPLATE_LP3;
	RelativeLayout.LayoutParams RIGHTPLATE_LP4;
	RelativeLayout.LayoutParams LEFTPLATE_LP4;
	RelativeLayout.LayoutParams RIGHTPLATE_LP5;
	RelativeLayout.LayoutParams LEFTPLATE_LP5;
	RelativeLayout.LayoutParams RIGHTPLATE_LP6;
	RelativeLayout.LayoutParams LEFTPLATE_LP6;
	RelativeLayout.LayoutParams RIGHTPLATE_LP7;
	RelativeLayout.LayoutParams LEFTPLATE_LP7;
	RelativeLayout.LayoutParams RIGHTPLATE_LP8;
	RelativeLayout.LayoutParams LEFTPLATE_LP8;

	//is creating separate layout parameters necessary?
	//may want to dynamically creating ids via appending and some cute algorithm..
	//Setting up depth  = 2 plates will be top of hill, and should be downhill from there once you figure something out...
	//Make it elegant! 
	//After you have everything figured, make sure all of these plates fit.
	//at the least you should be able to do this with reused code
	//dynamic shit can be done later... for now just do manual labor so you don't get lost in the code. 
	Boolean lbmode = true;//for now 
	static boolean[] boolArray = new boolean[7];
	static String[] liftPattern;
	
	
	
	
	
	Boolean lbMode; //might need to be static 
	
	static String viewMode;
	
    private static final int SWIPE_MIN_DISTANCE = 170;
//    private static final int SWIPE_MAX_OFF_PATH = 100;
    private static final int SWIPE_THRESHOLD_VELOCITY = 100;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    
    TextView dateTV;
    TextView freqTV;
    TextView liftTV;
    TextView weightTV;
    TextView cycleTV;
    TextView modeTV;
    int idCounter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		idCounter = 0;
		setContentView(R.layout.activity_fourth);
//		int plateDepth = 0; 
		dateTV = (TextView) findViewById(R.id.dateTextView);
		liftTV = (TextView) findViewById(R.id.liftTypeTextView);
		weightTV = (TextView) findViewById(R.id.weightTextView);
		cycleTV = (TextView) findViewById(R.id.cycleTextView);
		modeTV = (TextView) findViewById(R.id.modeTextView);
		barbellImageView = (ImageView) findViewById(R.id.barbell);
		
        // Gesture detection
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
		

		Intent intent = getIntent();

		String date = intent.getStringExtra("date");
		String cycle = intent.getStringExtra("cycle");
		String liftType = intent.getStringExtra("liftType");
		String frequency = intent.getStringExtra("frequency");
		String firstLiftString = intent.getStringExtra("firstLift");
//		String secondLiftString = intent.getStringExtra("secondLift");
//		String thirdLiftString = intent.getStringExtra("thirdLift");
		String lbModeString = intent.getStringExtra("mode");
		setLbMode(lbModeString);
		setViewModeString(intent.getStringExtra("viewMode"));
		setLiftPattern(intent.getStringArrayExtra("liftPattern"));
		
		String frequencyBuffer = null;
		switch (frequency)
		{
		case "5-5-5":
			frequencyBuffer = "x5";
			break;
		case "3-3-3":
			frequencyBuffer = "x3";
			break;
		case "5-3-1":
			frequencyBuffer = "x5";
			break;
		
		}
//		BarbellConfig barbellconfig = new BarbellConfig(IndividualView.this);
        boolArray = intent.getBooleanArrayExtra("boolArray");
		configureBarbell(firstLiftString, barbellImageView, relativeLayout, boolArray);
		dateTV.setText(date);
		cycleTV.setText("Cycle: " + cycle);
		liftTV.setText(liftType);
		weightTV.setText(firstLiftString + frequencyBuffer);


		//if the weight is greater than 45 (or equal to).... second and third lift will be handled in right swipe intent

		//TODO add back button and 35 support 
		relativeLayout = (RelativeLayout) findViewById(R.id.individualView);
		relativeLayout.setOnTouchListener(gestureListener);

		//weight divisions 



		




	}//end oncreate
	
	
	
	
	private void setLbMode(String lbModeString) {
		
		int buffer = Integer.valueOf(lbModeString);
		if (buffer == 1)
		{
		lbMode = true;
		modeTV.setText("Mode: lbs");
		}
		else if (buffer == 0)
		{
		lbMode = false;
		modeTV.setText("Mode: kgs");
		}
		else
		{
		lbMode = null;//create an error
		modeTV.setText("Mode error!");
		}
	}
	
	boolean getLbMode()
	{
		return lbMode;
	}


	void setViewModeString(String viewmodestring)
	{
		viewMode = viewmodestring;
	}
	
	String getViewModeString()
	{
		return viewMode;
	}
	
	public void configureBarbell(String myFirstLiftString, ImageView barbellImageView, RelativeLayout relativeLayout, boolean[] booleanArray) //pass barbellimageView, relative layout, first lift
	{
		//lbNeeded declarations
		int fortyfivesNeeded;
		int thirtyfivesNeeded;
		int twentyfivesNeeded;
		int tensNeeded;
		int fivesNeeded;
		int twopointfivesNeeded;
		
		
		int twentyfivesNeeded_kg;
		int twentysNeeded_kg;
		int fifteensNeeded_kg;
		int tensNeeded_kg;
		int fivesNeeded_kg;
		int twopointfivesNeeded_kg;
		int onepointtwofivesNeeded_kg;
		
	    TextView platesNeeded6;//this is first in list 
	    TextView platesNeeded5;
	    TextView platesNeeded4;
	    TextView platesNeeded3;
	    TextView platesNeeded2;
	    TextView platesNeeded1;
	    TextView platesNeeded_kgExtra1;
		
		int barbellWeight = 0;
		Double firstLift;
		
		String firstLiftString = myFirstLiftString;
		firstLift = Double.valueOf(firstLiftString);
		if (getLbMode())
		{
            boolean 
           lbHaveFortyFive = booleanArray[0],
           lbHaveThirtyFive = booleanArray[1],
           lbHaveTwentyFive = booleanArray[2],
           lbHaveTen = booleanArray[3],
           lbHaveFive = booleanArray[4],
           lbHaveTwoPointFive = booleanArray[5]; 	
			
			if (firstLift < 50)//then completely skip UI generation.. could have an empty barbell sprite but meh 	
			{
				barbellImageView.setVisibility(View.INVISIBLE);
			}
			else
			{
				if (firstLift > 45 && firstLift < 47)
				{
					barbellImageView.setImageResource(R.drawable.empty_barbell); 
					barbellWeight = 45;
				}
				//do something like setting plates here.. 
				if (firstLift >= 135 && lbHaveFortyFive ) //can we use our base barebell?	
				{
				barbellImageView.setImageResource(R.drawable.barbell_fortyfives_lb); 
				barbellWeight = 135;
				}

				else if ( (firstLift <= 135) && (firstLift >= 115) && lbHaveThirtyFive)//assert: lift must be less than 135 to make it to this else 
				{	
					barbellImageView.setImageResource(R.drawable.barbell_thirtyfives);
					barbellWeight = 115;
				}

				else if ((firstLift <= 115) && (firstLift >= 95) && lbHaveTwentyFive) // assert: lift must be less than 115 to make it to this else
				{
					barbellImageView.setImageDrawable((getResources().getDrawable(R.drawable.barbell_twentyfives)));
					barbellWeight = 95;
				}

				else if ((firstLift <= 95) && (firstLift >= 65) && lbHaveTen)
				{
					barbellImageView.setImageDrawable((getResources().getDrawable(R.drawable.barbell_tens)));
					barbellWeight = 65;
				}
				else if ((firstLift <= 65) && (firstLift >= 55) && lbHaveFive )
				{	
					barbellImageView.setImageDrawable((getResources().getDrawable(R.drawable.barbell_fives_lb)));
					barbellWeight = 50;
				}
				else if ((firstLift <= 55) && (firstLift >= 50) && lbHaveTwoPointFive )
				{	
					barbellImageView.setImageDrawable((getResources().getDrawable(R.drawable.barbell_twopointfives)));
					barbellWeight = 50;
				}
				relativeLayout=  (RelativeLayout) findViewById(R.id.individualView);
				setContentView(relativeLayout);
				relativeLayout.setOnClickListener(IndividualViewActivity.this); 

				PoundPlateComputer platecomputer = new PoundPlateComputer();
				platecomputer.computeLbPlates(firstLift, barbellWeight, booleanArray);
				
				
				fortyfivesNeeded = platecomputer.getFortyFivesNeeded();
				thirtyfivesNeeded = platecomputer.getThirtyFivesNeeded();
				twentyfivesNeeded = platecomputer.getTwentyFivesNeeded();
				tensNeeded = platecomputer.getTensNeeded();
				fivesNeeded = platecomputer.getFivesNeeded();
				twopointfivesNeeded = platecomputer.getTwoPointFivesNeeded();
//				ConfigTool helper = new ConfigTool(IndividualView.this);
				
				if (fortyfivesNeeded > 0)
				{
				int iterations = fortyfivesNeeded / 2; //e.g. 6 plates = 3 iterations  
				for (int i=0; i < iterations; i++)
				{
					moveOut();
					dynamicAddPlate(R.drawable.plate_fourtyfive_lbs);
				//moveOut();
				//dynamicAddPlate(R.drawable.plate_fourtyfive_lbs);
				}
				}
				
				if (thirtyfivesNeeded > 0)
				{
				int iterations = thirtyfivesNeeded / 2; //e.g. 6 platsatisiferes = 3 iterations  
				for (int i=0; i < iterations; i++)
				{
					moveOut();
					dynamicAddPlate(R.drawable.plate_thirtyfive_lbs);
				}
				}

				if (twentyfivesNeeded > 0)
				{
				int iterations = twentyfivesNeeded / 2; //e.g. 6 plates = 3 iterations  
				for (int i=0; i < iterations; i++)
				{
					moveOut();
					dynamicAddPlate(R.drawable.plate_twentyfive_lbs);
				}
				}
				
				if (tensNeeded > 0)
				{
				int iterations = tensNeeded / 2; //e.g. 6 plates = 3 iterations  
				for (int i=0; i < iterations; i++)
				{
					moveOut();
					dynamicAddPlate(R.drawable.plate_ten_lbs);
				}
				}
				
				if (fivesNeeded > 0)
				{
				int iterations = fivesNeeded / 2; //e.g. 6 plates = 3 iterations  
				for (int i=0; i < iterations; i++)
				{
					moveOut();
					dynamicAddPlate(R.drawable.plate_five_lbs);
				}
				}
				
				if (twopointfivesNeeded > 0)
				{
				int iterations = twopointfivesNeeded / 2; //e.g. 6 plates = 3 iterations  
				for (int i=0; i < iterations; i++)
				{
				moveOut();
				dynamicAddPlate(R.drawable.plate_twopointfive_lbs);
				}
				}
				
				
				
				//now add plates that were on barbell
				switch (barbellWeight)
				{
				case 135:
					fortyfivesNeeded = fortyfivesNeeded + 2;
					break;
				case 115:
					thirtyfivesNeeded = thirtyfivesNeeded + 2;
					break;
				case 95:
					twentyfivesNeeded = twentyfivesNeeded + 2;
					break;
				case 65: 
					tensNeeded = tensNeeded + 2;
					break; 
				case 55:
					fivesNeeded = fivesNeeded + 2;
					break;
				case 50:
					twopointfivesNeeded = twopointfivesNeeded + 2;
					break;
				}
				setPlatesNeededTextViews(relativeLayout, fortyfivesNeeded,
						thirtyfivesNeeded, twentyfivesNeeded, tensNeeded,
						fivesNeeded, twopointfivesNeeded);
	
			}//end else to checking if UI generation is even possible  


			//generate plates needed if UI is not generated 


		}//end taking care of lbmode condition 
		else //otheriwse we are working in kilograms..
		{
			if (firstLift < 20)//then completely skip UI generation.. could have an empty barbell sprite but meh 	
			{
				barbellImageView.setVisibility(View.INVISIBLE);
			}
			else
			{
				if (firstLift == 20)
				{
					barbellImageView.setImageResource(R.drawable.empty_barbell);  
					barbellWeight = 45;
				}
				
				//do something like setting plates here.. 
				if (firstLift >= 70 ) //can we use our base barebell?	
				{
				barbellImageView.setImageResource(R.drawable.barbell_twentyfive_kg); 
				barbellWeight = 70;
				}

				else if ( (firstLift <= 70) && (firstLift >= 60))//assert: lift must be less than 135 to make it to this else 
				{	
					barbellImageView.setImageResource(R.drawable.barbell_twenty_kg);
					barbellWeight = 60;
				}

				else if ((firstLift <= 60) && (firstLift >= 50)) // assert: lift must be less than 115 to make it to this else
				{
					barbellImageView.setImageDrawable((getResources().getDrawable(R.drawable.barbell_fifteen_kg)));
					barbellWeight = 50;
				}

				else if ((firstLift <= 50) && (firstLift >= 40))
				{
					barbellImageView.setImageDrawable((getResources().getDrawable(R.drawable.barbell_ten_kg)));
					barbellWeight = 40;
				}
				else if ((firstLift <= 40) && (firstLift >= 30) )
				{	
					barbellImageView.setImageDrawable((getResources().getDrawable(R.drawable.barbell_five_kg)));
					barbellWeight = 30;
				}
				else if ((firstLift <= 23) && (firstLift >= 22) )
				{	
					barbellImageView.setImageDrawable((getResources().getDrawable(R.drawable.barbell_twopointfive_kg)));
					barbellWeight = 23;
				}
				
				relativeLayout=  (RelativeLayout) findViewById(R.id.individualView);
				setContentView(relativeLayout);
				relativeLayout.setOnClickListener(IndividualViewActivity.this); 

				KilogramPlateComputer platecomputer = new KilogramPlateComputer();
				platecomputer.computeKgPlates(firstLift, barbellWeight, booleanArray);
				
				
				 twentyfivesNeeded_kg = platecomputer.getTwentyFivesNeeded();
				 twentysNeeded_kg = platecomputer.getTwentysNeeded();
				 fifteensNeeded_kg = platecomputer.getFifteensNeeded();
				 tensNeeded_kg = platecomputer.getTensNeeded();
				 fivesNeeded_kg = platecomputer.getFivesNeeded();
				 twopointfivesNeeded_kg = platecomputer.getTwopointfivesNeeded();
				 onepointtwofivesNeeded_kg = platecomputer.getOnepointtwofivesNeeded();
				
				
				
				if (twentyfivesNeeded_kg > 0)
				{
				int iterations = twentyfivesNeeded_kg / 2; //e.g. 6 plates = 3 iterations  
				for (int i=0; i < iterations; i++)
				{
				moveOut();
				dynamicAddPlate(R.drawable.plate_twentyfive_kg);
				}
				}
				
				if (twentysNeeded_kg > 0)
				{
				int iterations = twentysNeeded_kg / 2; //e.g. 6 plates = 3 iterations  
				for (int i=0; i < iterations; i++)
				{
				moveOut();
				dynamicAddPlate(R.drawable.plate_twenty_kg);
				}
				}

				if (fifteensNeeded_kg > 0)
				{
				int iterations = fifteensNeeded_kg / 2; //e.g. 6 plates = 3 iterations  
				for (int i=0; i < iterations; i++)
				{
				moveOut();
				dynamicAddPlate(R.drawable.plate_fifteen_kg);
				}
				}
				
				if (tensNeeded_kg > 0)
				{
				int iterations = tensNeeded_kg / 2; //e.g. 6 plates = 3 iterations  
				for (int i=0; i < iterations; i++)
				{
				moveOut();
				dynamicAddPlate(R.drawable.plate_ten_kg);
				}
				}
				
				if (fivesNeeded_kg > 0)
				{
				int iterations = fivesNeeded_kg / 2; //e.g. 6 plates = 3 iterations  
				for (int i=0; i < iterations; i++)
				{
				moveOut();
				dynamicAddPlate(R.drawable.plate_five_kg);
				}
				}
				
				if (twopointfivesNeeded_kg > 0)
				{
				int iterations = twopointfivesNeeded_kg / 2; //e.g. 6 plates = 3 iterations  
				for (int i=0; i < iterations; i++)
				{
				moveOut();
				dynamicAddPlate(R.drawable.plate_twopointfive_kg);
				}
				}
				
				
				if (onepointtwofivesNeeded_kg > 0)
				{
				int iterations = onepointtwofivesNeeded_kg / 2; //e.g. 6 plates = 3 iterations  
				for (int i=0; i < iterations; i++)
				{
				moveOut();
				dynamicAddPlate(R.drawable.plate_one_kg);//todo eventually change .pngs to support this rather than abusing the scale you already had made up. 
				}
				}
				
				
				//now add plates that were on barbell
				switch (barbellWeight)
				{
				case 70:
					twentyfivesNeeded_kg = twentyfivesNeeded_kg + 2;
					break;
				case 60:
					twentysNeeded_kg = twentysNeeded_kg + 2;
					break;
				case 50:
					fifteensNeeded_kg = fifteensNeeded_kg + 2;
					break;
				case 40: 
					tensNeeded_kg = tensNeeded_kg + 2;
					break; 
				case 30:
					fivesNeeded_kg = fivesNeeded_kg + 2;
					break;
				case 25:
					twopointfivesNeeded_kg = twopointfivesNeeded_kg + 2;
					break;
				case 23: //rounded up...
					onepointtwofivesNeeded_kg = onepointtwofivesNeeded_kg + 2;
					break;	
				}
				relativeLayout.invalidate();
				platesNeeded6 = (TextView) findViewById(R.id.platesNeeded6);
				platesNeeded5 = (TextView) findViewById(R.id.platesNeeded5);
				platesNeeded4 = (TextView) findViewById(R.id.platesNeeded4);
				platesNeeded3 = (TextView) findViewById(R.id.platesNeeded3);
				platesNeeded2 = (TextView) findViewById(R.id.platesNeeded2);
				platesNeeded1 = (TextView) findViewById(R.id.platesNeeded1);
			    platesNeeded_kgExtra1 = (TextView) findViewById(R.id.platesNeeded_kgExtra1);
				
				//add data to text views
				platesNeeded6.setText("25s needed: " + twentyfivesNeeded_kg);
				platesNeeded5.setText("20s needed: " + twentysNeeded_kg);
				platesNeeded4.setText("15s needed: " + fifteensNeeded_kg);
				platesNeeded3.setText("10s needed: " + tensNeeded_kg);
				platesNeeded2.setText("5s needed: " + fivesNeeded_kg);
				platesNeeded1.setText("2.5s needed: " + twopointfivesNeeded_kg);
				platesNeeded_kgExtra1.setText("1.25s needed: " + onepointtwofivesNeeded_kg );
				//TODO add more textviews for the extra plates
				
			}//end else to checking if UI generation is even possible  
		}
	}




	private void setPlatesNeededTextViews(RelativeLayout relativeLayout,
			int fortyfivesNeeded, int thirtyfivesNeeded, int twentyfivesNeeded,
			int tensNeeded, int fivesNeeded, int twopointfivesNeeded) {
		TextView platesNeeded6;
		TextView platesNeeded5;
		TextView platesNeeded4;
		TextView platesNeeded3;
		TextView platesNeeded2;
		TextView platesNeeded1;
		TextView platesNeeded_kgExtra1;
		relativeLayout.invalidate();
		platesNeeded6 = (TextView) findViewById(R.id.platesNeeded6);
		platesNeeded5 = (TextView) findViewById(R.id.platesNeeded5);
		platesNeeded4 = (TextView) findViewById(R.id.platesNeeded4);
		platesNeeded3 = (TextView) findViewById(R.id.platesNeeded3);
		platesNeeded2 = (TextView) findViewById(R.id.platesNeeded2);
		platesNeeded1 = (TextView) findViewById(R.id.platesNeeded1);
		platesNeeded_kgExtra1 = (TextView) findViewById(R.id.platesNeeded_kgExtra1);
		
		//add data to text views
		platesNeeded6.setText("45s needed: " + fortyfivesNeeded);
		platesNeeded5.setText("35s needed: " + thirtyfivesNeeded);
		platesNeeded4.setText("25s needed: " + twentyfivesNeeded);
		platesNeeded3.setText("10s needed: " + tensNeeded);
		platesNeeded2.setText("5s needed: " + fivesNeeded);
		platesNeeded1.setText("2.5s needed: " + twopointfivesNeeded);
		platesNeeded_kgExtra1.setVisibility(View.INVISIBLE);//hide 1.25s when dealing with pounds
	}
	

	//forward direction (lifts go normal way)
	//String[] myPattern = {"Squat", "Rest", "Bench", "Deadlift", "Rest", "OHP"  };
	public void onUpSwipe() throws ParseException
	{
		Intent myIntent = getIntent();
		String DATE_FORMAT = "MM-dd-yyyy";
	    String date_string = myIntent.getStringExtra("date"); 
	    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT, java.util.Locale.getDefault());
	    Date date = (Date)sdf.parse(date_string);
	    Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(0);
	    c1.setTime(date);

	    
	    String liftType = myIntent.getStringExtra("liftType"); 
		String viewMode = myIntent.getStringExtra("viewMode");
		String mode = myIntent.getStringExtra("mode");
		liftPattern = myIntent.getStringArrayExtra("liftPattern"); 
		setLiftPattern(liftPattern);
		String nextLift = null;
	    String incrementedString = null;
	    String[] result = new String[2];
	    ConfigTool helper = new ConfigTool(IndividualViewActivity.this);
		result = helper.getNextLift(c1, liftPattern, liftType, viewMode);//getNextLiftDefault returns a result array which has nextLift and incrementedString
		nextLift = result[0];
		incrementedString = result[1];

			    
	    Intent nextLiftIntent = helper.configureNextSet(incrementedString, nextLift, viewMode, mode, boolArray, liftPattern);
	    if (!helper.bottomCase())
	    {
	    	startActivity(nextLiftIntent);
			overridePendingTransition(0,R.anim.exit_slide_up);
	    }
	    
	   
	}
	
	public void onDownSwipe() throws ParseException
	{
		Intent myIntent = getIntent();
		
		String DATE_FORMAT = "MM-dd-yyyy";
	    String date_string = myIntent.getStringExtra("date"); 
	    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT, java.util.Locale.getDefault());
	    Date date = (Date)sdf.parse(date_string);
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
	     ConfigTool helper = new ConfigTool(IndividualViewActivity.this);
		 result = helper.getPrevLift(c1, liftPattern, liftType, viewMode);//getNextLiftDefault returns a result array which has nextLift and incrementedString
		 nextLift = result[0];
		 incrementedString = result[1];
	    
	    
	    
	    Intent prevLiftIntent =  helper.configurePreviousSet(incrementedString, nextLift, viewMode, mode, boolArray, liftPattern);
	    if (!helper.topCase())
	    {
	    	startActivity(prevLiftIntent);
	    	overridePendingTransition(0,R.anim.exit_slide_down);
	    }
	   
	}
	
	
	
	
	@Override
	public void onBackPressed() {
		Intent preIntent = getIntent();
		Intent intent = new Intent(IndividualViewActivity.this, ThirdScreenActivity.class);
		intent.putExtra("origin", "individualView");//reusing flag I use to jump to third from first. 
		intent.putExtra("liftPattern", getLiftPattern());
		intent.putExtra("viewMode", getViewModeString());
		startActivity(intent);
		overridePendingTransition(0,R.anim.exit_slide_right);
	}
	
    private String[] getLiftPattern() {
		
		return liftPattern;
	}
    
    private void setLiftPattern (String[] myPattern)
    {
    	liftPattern = myPattern;
    }

	class MyGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
               // if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                //    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    forwardSwipe(); 
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    onBackPressed();
                }
                
                //up and down swipes 
                if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    onUpSwipe();
                }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    onDownSwipe();
                }
                
                
                
                
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

            @Override
        public boolean onDown(MotionEvent e) {
              return true;
        }
    }
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
	}




	@SuppressLint("InlinedApi")
	public void dynamicAddPlate(int resource)
	{
		barbellImageView = (ImageView) findViewById(R.id.barbell);
		
		//Right/left 45 Setup
		CURRENT_RIGHT_IMAGEVIEW = new ImageView(IndividualViewActivity.this);
		CURRENT_LEFT_IMAGEVIEW = new ImageView(IndividualViewActivity.this);
		//setting image resource
		//setting image position
		CURRENT_RIGHT_IMAGEVIEW.setImageResource(resource);
		CURRENT_LEFT_IMAGEVIEW.setImageResource(resource);

		//right
		
		CURRENT_RIGHT_LP = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		CURRENT_RIGHT_IMAGEVIEW.setId(CURRENT_RIGHT_ANCHOR_ID);
		CURRENT_RIGHT_LP.addRule(RelativeLayout.RIGHT_OF, CURRENT_RIGHT_ANCHOR_ID);
		CURRENT_RIGHT_LP.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		//CURRENT_RIGHT_LP.setMargins(0, 17, 0, 0);
		CURRENT_RIGHT_IMAGEVIEW.setLayoutParams(CURRENT_RIGHT_LP);
		CURRENT_RIGHT_IMAGEVIEW.setId(CURRENT_RIGHT_ID);

		//left
		RelativeLayout.LayoutParams CURRENT_LEFT_LP = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT); //THOS
		CURRENT_LEFT_IMAGEVIEW.setId(CURRENT_LEFT_ANCHOR_ID);
		CURRENT_LEFT_LP.addRule(RelativeLayout.LEFT_OF, CURRENT_LEFT_ANCHOR_ID);
		CURRENT_LEFT_LP.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		//CURRENT_LEFT_LP.setMargins(0, 17, 0, 0);


		CURRENT_LEFT_IMAGEVIEW.setLayoutParams(CURRENT_LEFT_LP);
		CURRENT_LEFT_IMAGEVIEW.setId(CURRENT_LEFT_ID);



		relativeLayout=  (RelativeLayout) findViewById(R.id.individualView);


		//adding view to layout
		relativeLayout.addView(CURRENT_RIGHT_IMAGEVIEW);
		relativeLayout.addView(CURRENT_LEFT_IMAGEVIEW);
		//make visible to program

		setContentView(relativeLayout);
		

	}

    public void forwardSwipe()
    {
		Intent intent = getIntent();
		String date = intent.getStringExtra("date");
		String cycle = intent.getStringExtra("cycle");
		String liftType = intent.getStringExtra("liftType");
		String frequency = intent.getStringExtra("frequency");
		String firstLiftString = intent.getStringExtra("firstLift");
		String secondLiftString = intent.getStringExtra("secondLift");
		String thirdLiftString = intent.getStringExtra("thirdLift");
		String lbmodeString = intent.getStringExtra("mode");
		String[] pattern = intent.getStringArrayExtra("liftPattern");
		boolean[] plateconfig = intent.getBooleanArrayExtra("boolArray"); // monkey)
		
		Intent forwardIntent  = new Intent(IndividualViewActivity.this, IndividualViewTwoActivity.class);
		forwardIntent.putExtra("cycle", cycle);
		forwardIntent.putExtra("frequency", frequency);
		forwardIntent.putExtra("liftType", liftType);
		forwardIntent.putExtra("firstLift", firstLiftString);
		forwardIntent.putExtra("secondLift", secondLiftString);
		forwardIntent.putExtra("thirdLift", thirdLiftString);
		forwardIntent.putExtra("date", date);
		forwardIntent.putExtra("mode", lbmodeString);
		forwardIntent.putExtra("boolArray", plateconfig);
		startActivity(forwardIntent);
    }
	

	//research reflection... I think that may be what you w

	public void moveOut ()
	{
		idCounter++;
		switch (idCounter)
		{
		case 1://first time this is called our anchor for the left and right plate will be the same--the barbell.
			CURRENT_LEFT_ANCHOR_ID  = R.id.barbell;
			CURRENT_RIGHT_ANCHOR_ID = R.id.barbell;
			CURRENT_RIGHT_IMAGEVIEW = RIGHTFORTYFIVEIMAGEVIEW;
			CURRENT_LEFT_IMAGEVIEW  = LEFTFORTYFIVEIMAGEVIEW;
			CURRENT_LEFT_ID         = LEFT_PLATE_ID_DEPTH1;
			CURRENT_RIGHT_ID        = RIGHT_PLATE_ID_DEPTH1;	
			CURRENT_RIGHT_LP 		= RIGHTPLATE_LP;
			CURRENT_LEFT_LP 	    = LEFTPLATE_LP;
			break;
		case 2:
			CURRENT_LEFT_ANCHOR_ID  = LEFT_PLATE_ID_DEPTH1;
			CURRENT_RIGHT_ANCHOR_ID = RIGHT_PLATE_ID_DEPTH1;
			CURRENT_RIGHT_IMAGEVIEW = RIGHTFORTYFIVEIMAGEVIEW_DEPTH2;
			CURRENT_LEFT_IMAGEVIEW  = LEFTFORTYFIVEIMAGEVIEW_DEPTH2;
			CURRENT_LEFT_ID         = LEFT_PLATE_ID_DEPTH2;
			CURRENT_RIGHT_ID        = RIGHT_PLATE_ID_DEPTH2;
			CURRENT_RIGHT_LP 		= RIGHTPLATE_LP2;
			CURRENT_LEFT_LP 	    = LEFTPLATE_LP2;
			break;
		case 3:
			CURRENT_LEFT_ANCHOR_ID  = LEFT_PLATE_ID_DEPTH2;
			CURRENT_RIGHT_ANCHOR_ID = RIGHT_PLATE_ID_DEPTH2;
			CURRENT_RIGHT_IMAGEVIEW = RIGHTFORTYFIVEIMAGEVIEW_DEPTH3;
			CURRENT_LEFT_IMAGEVIEW  = LEFTFORTYFIVEIMAGEVIEW_DEPTH3;
			CURRENT_LEFT_ID         = LEFT_PLATE_ID_DEPTH3;
			CURRENT_RIGHT_ID        = RIGHT_PLATE_ID_DEPTH3;		
			CURRENT_RIGHT_LP 		= RIGHTPLATE_LP3;
			CURRENT_LEFT_LP 	    = LEFTPLATE_LP3;
			break;
		case 4:	
			CURRENT_LEFT_ANCHOR_ID  = LEFT_PLATE_ID_DEPTH3;
			CURRENT_RIGHT_ANCHOR_ID = RIGHT_PLATE_ID_DEPTH3;
			CURRENT_RIGHT_IMAGEVIEW = RIGHTFORTYFIVEIMAGEVIEW_DEPTH4;
			CURRENT_LEFT_IMAGEVIEW  = LEFTFORTYFIVEIMAGEVIEW_DEPTH4;
			CURRENT_LEFT_ID         = LEFT_PLATE_ID_DEPTH4;
			CURRENT_RIGHT_ID        = RIGHT_PLATE_ID_DEPTH4;		
			CURRENT_RIGHT_LP 		= RIGHTPLATE_LP4;
			CURRENT_LEFT_LP 	    = LEFTPLATE_LP4;
			break;
		case 5:
			CURRENT_LEFT_ANCHOR_ID  = LEFT_PLATE_ID_DEPTH4;
			CURRENT_RIGHT_ANCHOR_ID = RIGHT_PLATE_ID_DEPTH4;
			CURRENT_RIGHT_IMAGEVIEW = RIGHTFORTYFIVEIMAGEVIEW_DEPTH5;
			CURRENT_LEFT_IMAGEVIEW  = LEFTFORTYFIVEIMAGEVIEW_DEPTH5;
			CURRENT_LEFT_ID         = LEFT_PLATE_ID_DEPTH5;
			CURRENT_RIGHT_ID        = RIGHT_PLATE_ID_DEPTH5;		
			CURRENT_RIGHT_LP 		= RIGHTPLATE_LP5;
			CURRENT_LEFT_LP 	    = LEFTPLATE_LP5;
			break;
		case 6:
			CURRENT_LEFT_ANCHOR_ID  = LEFT_PLATE_ID_DEPTH5;
			CURRENT_RIGHT_ANCHOR_ID = RIGHT_PLATE_ID_DEPTH5;
			CURRENT_RIGHT_IMAGEVIEW = RIGHTFORTYFIVEIMAGEVIEW_DEPTH6;
			CURRENT_LEFT_IMAGEVIEW  = LEFTFORTYFIVEIMAGEVIEW_DEPTH6;
			CURRENT_LEFT_ID         = LEFT_PLATE_ID_DEPTH6;
			CURRENT_RIGHT_ID        = RIGHT_PLATE_ID_DEPTH6;		
			CURRENT_RIGHT_LP 		= RIGHTPLATE_LP6;
			CURRENT_LEFT_LP 	    = LEFTPLATE_LP6;
			break;
		case 7:	
			CURRENT_LEFT_ANCHOR_ID  = LEFT_PLATE_ID_DEPTH6;
			CURRENT_RIGHT_ANCHOR_ID = RIGHT_PLATE_ID_DEPTH6;
			CURRENT_RIGHT_IMAGEVIEW = RIGHTFORTYFIVEIMAGEVIEW_DEPTH7;
			CURRENT_LEFT_IMAGEVIEW  = LEFTFORTYFIVEIMAGEVIEW_DEPTH7;
			CURRENT_LEFT_ID         = LEFT_PLATE_ID_DEPTH7;
			CURRENT_RIGHT_ID        = RIGHT_PLATE_ID_DEPTH7;		
			CURRENT_RIGHT_LP 		= RIGHTPLATE_LP7;
			CURRENT_LEFT_LP 	    = LEFTPLATE_LP7;
			break;
		case 8:	
			CURRENT_LEFT_ANCHOR_ID  = LEFT_PLATE_ID_DEPTH7;
			CURRENT_RIGHT_ANCHOR_ID = RIGHT_PLATE_ID_DEPTH7;
			CURRENT_RIGHT_IMAGEVIEW = RIGHTFORTYFIVEIMAGEVIEW_DEPTH8;
			CURRENT_LEFT_IMAGEVIEW  = LEFTFORTYFIVEIMAGEVIEW_DEPTH8;
			CURRENT_LEFT_ID         = LEFT_PLATE_ID_DEPTH8;
			CURRENT_RIGHT_ID        = RIGHT_PLATE_ID_DEPTH8;		
			CURRENT_RIGHT_LP 		= RIGHTPLATE_LP8;
			CURRENT_LEFT_LP 	    = LEFTPLATE_LP8;
			break;
		default:
			break;
		}

	}//end method moveOut()


	public int getLeftAnchorId()
	{
		return CURRENT_LEFT_ANCHOR_ID;
	}

	public int getRightAnchorId()
	{
		return CURRENT_RIGHT_ANCHOR_ID;
	}

	public int getCurrentLeftId()
	{
		return CURRENT_LEFT_ID;
	}

	public int getCurrentRightId()
	{
		return CURRENT_RIGHT_ID;
	}


	public ImageView getCurrentLeftImageView()
	{
		return CURRENT_RIGHT_IMAGEVIEW;
	}

	public ImageView getCurrentRightImageView()
	{
		return CURRENT_LEFT_IMAGEVIEW;
	}

	public RelativeLayout.LayoutParams getCurrentLeftLp()
	{
		return CURRENT_LEFT_LP;
	}

	public RelativeLayout.LayoutParams getCurrentRightLp()
	{
		return CURRENT_RIGHT_LP;
	}

	@Override
	public void onClick(View v) {
//        @SuppressWarnings("unused")
//		Filter f = (Filter) v.getTag();
        //FilterFullscreenActivity.show(this, input, f);//TODO what to do with this?
		
	}




}