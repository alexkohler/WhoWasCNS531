package com.example.whowascns;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater.Filter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class IndividualViewThree extends ActionBarActivity implements OnClickListener {

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
	Boolean lbMode = true;//for now 
	Boolean thirtyFiveFlag = true;
	Double firstLift;
	int barbellWeight;
	
	//LBS
	int fortyfivesNeeded;
	int thirtyfivesNeeded;
	int twentyfivesNeeded;
	int tensNeeded;
	int fivesNeeded;
	int twopointfivesNeeded;
	
	//KGS
	int twentyfivesNeeded_kg;
	int twentysNeeded_kg;
	int fifteensNeeded_kg;
	int tensNeeded_kg;
	int fivesNeeded_kg;
	int twopointfivesNeeded_kg;
	int onepointtwofivesNeeded_kg;
	

    private static final int SWIPE_MIN_DISTANCE = 170;
    private static final int SWIPE_MAX_OFF_PATH = 100;
    private static final int SWIPE_THRESHOLD_VELOCITY = 100;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    
    TextView dateTV;
    TextView cycleTV;
    TextView liftTV;
    TextView weightTV;
    TextView modeTV;
    TextView platesNeeded6;//this is first in list 
    TextView platesNeeded5;
    TextView platesNeeded4;
    TextView platesNeeded3;
    TextView platesNeeded2;
    TextView platesNeeded1;
    TextView platesNeeded_kgExtra1;
    int idCounter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(new Bundle());
		idCounter = 0;
		setContentView(R.layout.activity_sixth);
		dateTV = (TextView) findViewById(R.id.dateTextView_activity_three);
		cycleTV = (TextView) findViewById(R.id.cycleTextView_activity_three);
		liftTV = (TextView) findViewById(R.id.liftTypeTextView_activity_three);
		weightTV = (TextView) findViewById(R.id.weightTextView_activity_three);
		modeTV = (TextView) findViewById(R.id.modeTextView_activity_three);
		platesNeeded6 = (TextView) findViewById(R.id.platesNeeded6_activity_three);
		platesNeeded5 = (TextView) findViewById(R.id.platesNeeded5_activity_three);
		platesNeeded4 = (TextView) findViewById(R.id.platesNeeded4_activity_three);
		platesNeeded3 = (TextView) findViewById(R.id.platesNeeded3_activity_three);
		platesNeeded2 = (TextView) findViewById(R.id.platesNeeded2_activity_three);
		platesNeeded1 = (TextView) findViewById(R.id.platesNeeded1_activity_three);
		platesNeeded_kgExtra1 = (TextView) findViewById(R.id.platesNeeded_kgExtra1_activity_three);
		barbellImageView = (ImageView) findViewById(R.id.barbell_activity_three);
		
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
//		String firstLiftString = intent.getStringExtra("firstLift");
//		String secondLiftString = intent.getStringExtra("secondLift");
		String thirdLiftString = intent.getStringExtra("thirdLift");
		String lbmodeString = intent.getStringExtra("mode");
		setLbMode(lbmodeString);
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
			frequencyBuffer = "x1";
			break;
		
		}
		
		
		configureBarbell(thirdLiftString);
		dateTV.setText(date);
		cycleTV.setText("Cycle: " + cycle);
		liftTV.setText(liftType);
		weightTV.setText(thirdLiftString  + frequencyBuffer );
	//	modeTV.setText(lbmodeString);
		

		//if the weight is greater than 45 (or equal to).... second and third lift will be handled in right swipe intent

		//TODO add back button and 35 support 
		relativeLayout =  (RelativeLayout) findViewById(R.id.individualViewThree);

		relativeLayout.setOnTouchListener(gestureListener);

		//weight divisions 



		




	}//end oncreate
	
	
	public void configureBarbell(String thirdLiftString)
	{
		String liftString = thirdLiftString;
		/*if (lbmodeString.contains("Lbs"))
		lbmode = true;
		if (lbmodeString.contains("Kgs"))
		lbmode = false;*/ //need to figure something out to keep lbmode when viewing an existing projection
		firstLift = Double.valueOf(liftString);
		//Toast.makeText(IndividualViewThree.this, date + cycle + liftType + frequency + firstLiftString + lbmodeString, Toast.LENGTH_SHORT).show();
		//mode
		if (getLbMode())
		{
			if (firstLift < 45)//then completely skip UI generation 	
			{
				
			}
			
			else
			{
				//do something like setting plates here.. 
				if (firstLift >= 135 ) //can we use our base barebell?	
				{
					barbellImageView = (ImageView) findViewById(R.id.barbell_activity_three);
				barbellImageView.setImageResource(R.drawable.barbell_fortyfives_lb); 
				barbellWeight = 135;
				}

				else if ( (firstLift <= 135) &&(firstLift >= 115) && thirtyFiveFlag)//assert: lift must be less than 135 to make it to this else 
				{	
					barbellImageView.setImageResource(R.drawable.barbell_thirtyfives);
					barbellWeight = 115;
				}

				else if ((firstLift <= 115) && (firstLift >= 95)) // assert: lift must be less than 115 to make it to this else
				{
					barbellImageView.setImageDrawable((getResources().getDrawable(R.drawable.barbell_twentyfives)));
					barbellWeight = 95;
				}

				else if ((firstLift <= 95) &&(firstLift >= 65))
				{
					barbellImageView.setImageDrawable((getResources().getDrawable(R.drawable.barbell_tens)));
					barbellWeight = 65;
				}
				else if ((firstLift <= 65) &&(firstLift >= 50) )
				{	
					barbellImageView.setImageDrawable((getResources().getDrawable(R.drawable.barbell_twopointfives)));
					barbellWeight = 50;
				}
				relativeLayout =  (RelativeLayout) findViewById(R.id.individualViewThree);
				setContentView(relativeLayout);
				relativeLayout.setOnClickListener(IndividualViewThree.this); 

				PoundPlateComputer platecomputer = new PoundPlateComputer();
				Boolean[] satisifer = null;
				platecomputer.computeLbPlates(firstLift, barbellWeight, satisifer);
				
				
				fortyfivesNeeded = platecomputer.getFortyFivesNeeded();
				thirtyfivesNeeded = platecomputer.getThirtyFivesNeeded();
				twentyfivesNeeded = platecomputer.getTwentyFivesNeeded();
				tensNeeded = platecomputer.getTensNeeded();
				fivesNeeded = platecomputer.getFivesNeeded();
				twopointfivesNeeded = platecomputer.getTwoPointFivesNeeded();
				
				
				if (fortyfivesNeeded > 0)
				{
				int iterations = fortyfivesNeeded / 2; //e.g. 6 plates = 3 iterations  
				for (int i=0; i < iterations; i++)
				{
				moveOut();
				dynamicAddPlate(R.drawable.plate_fourtyfive_lbs);
				}
				}
				
				if (thirtyfivesNeeded > 0)
				{
				int iterations = thirtyfivesNeeded / 2; //e.g. 6 plates = 3 iterations  
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
				
				//add data to text views
				relativeLayout.invalidate();
				platesNeeded6.setText("45s needed: " + fortyfivesNeeded);
				platesNeeded5.setText("35s needed: " + thirtyfivesNeeded);
				platesNeeded4.setText("25s needed: " + twentyfivesNeeded);
				platesNeeded3.setText("10s needed: " + tensNeeded);
				platesNeeded2.setText("5s needed: " + fivesNeeded);
				platesNeeded1.setText("2.5s needed: " + twopointfivesNeeded);
				platesNeeded_kgExtra1.setVisibility(View.INVISIBLE);
	
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
				else if ((firstLift <= 30) && (firstLift >= 25) )
				{	
					barbellImageView.setImageDrawable((getResources().getDrawable(R.drawable.barbell_twopointfive_kg)));
					barbellWeight = 25;
				}
				else if ((firstLift <= 23) && (firstLift >= 22) )
				{	
					barbellImageView.setImageDrawable((getResources().getDrawable(R.drawable.barbell_one_kg)));
					barbellWeight = 23;
				}
				
				relativeLayout=  (RelativeLayout) findViewById(R.id.individualViewThree);
				setContentView(relativeLayout);
				relativeLayout.setOnClickListener(IndividualViewThree.this); 

				KilogramPlateComputer platecomputer = new KilogramPlateComputer();
				Boolean[] satisifer = null;
				platecomputer.computeKgPlates(firstLift, barbellWeight, satisifer);
				
				
				 twentyfivesNeeded_kg = platecomputer.getTwentyFivesNeeded();
				 twentysNeeded_kg = platecomputer.getTwentysNeeded();
				 fifteensNeeded_kg = platecomputer.getFifteensNeeded();
				 tensNeeded_kg = platecomputer.getTensNeeded();
				 fivesNeeded_kg = platecomputer.getFivesNeeded();
				 twopointfivesNeeded_kg = platecomputer.getTwopointfivesNeeded();
				 onepointtwofivesNeeded_kg = platecomputer.getOnepointtwofivesNeeded();
//				 twosNeeded_kg = platecomputer.getTwosNeeded();
//				 onepointfivesNeeded_kg = platecomputer.getOnepointfivesNeeded();
//				 onesNeeded_kg = platecomputer.getOnesNeeded();
//				 pointfivesNeeded_kg = platecomputer.getPointfivesNeeded();
				
				
				
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
					setContentView(relativeLayout);
					relativeLayout.setOnClickListener(IndividualViewThree.this); 
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
				dynamicAddPlate(R.drawable.plate_one_kg);
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
				case 23:
					onepointtwofivesNeeded_kg = onepointtwofivesNeeded_kg + 2;
					break;	
				}
				relativeLayout.invalidate();
				platesNeeded6 = (TextView) findViewById(R.id.platesNeeded6_activity_three);
				platesNeeded5 = (TextView) findViewById(R.id.platesNeeded5_activity_three);
				platesNeeded4 = (TextView) findViewById(R.id.platesNeeded4_activity_three);
				platesNeeded3 = (TextView) findViewById(R.id.platesNeeded3_activity_three);
				platesNeeded2 = (TextView) findViewById(R.id.platesNeeded2_activity_three);
				platesNeeded1 = (TextView) findViewById(R.id.platesNeeded1_activity_three);
				
				//add data to text views
				platesNeeded6.setText("25s needed: " + twentyfivesNeeded_kg);
				platesNeeded5.setText("20s needed: " + twentysNeeded_kg);
				platesNeeded4.setText("15s needed: " + fifteensNeeded_kg);
				platesNeeded3.setText("10s needed: " + tensNeeded_kg);
				platesNeeded2.setText("5s needed: " + fivesNeeded_kg);
				platesNeeded1.setText("2.5s needed: " + twopointfivesNeeded_kg);
				platesNeeded_kgExtra1.setText("1.25s needed: " + onepointtwofivesNeeded_kg );
				
			}//end else to checking if UI generation is even possible  
		}
	}
	
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
	
	
	@Override
	public void onBackPressed() {
	   IndividualViewThree.this.finish();
	}
	
    class MyGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
               // if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                //    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    //Toast.makeText(IndividualViewThree.this, "Left Swipe", Toast.LENGTH_SHORT).show();
                   
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    onBackPressed();
                }
                
                //up and down swipes 
                if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(IndividualViewThree.this, "Up? Swipe", Toast.LENGTH_SHORT).show();
                }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(IndividualViewThree.this, "Down? Swipe", Toast.LENGTH_SHORT).show();
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

	

	private boolean getLbMode()
	{
		return lbMode;
	}
	



	@SuppressLint("InlinedApi")
	public void dynamicAddPlate(int resource)
	{
		barbellImageView = (ImageView) findViewById(R.id.barbell_activity_three);
		
		//Right/left 45 Setup
		CURRENT_RIGHT_IMAGEVIEW = new ImageView(IndividualViewThree.this);
		CURRENT_LEFT_IMAGEVIEW = new ImageView(IndividualViewThree.this);
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



		relativeLayout=  (RelativeLayout) findViewById(R.id.individualViewThree);


		//adding view to layout
		relativeLayout.addView(CURRENT_RIGHT_IMAGEVIEW);
		relativeLayout.addView(CURRENT_LEFT_IMAGEVIEW);
		//make visible to program

		setContentView(relativeLayout);
		

	}



	//research reflection... I think that may be what you w

	public void moveOut ()
	{
		idCounter++;
		switch (idCounter)
		{
		case 1://first time this is called our anchor for the left and right plate will be the same--the barbell.
			CURRENT_LEFT_ANCHOR_ID  = R.id.barbell_activity_three;
			CURRENT_RIGHT_ANCHOR_ID = R.id.barbell_activity_three;
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
//		Filter f = (Filter) v.getTag();//TODO why are you here
        //FilterFullscreenActivity.show(this, input, f);
		
	}




}