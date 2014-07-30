//package com.example.whowascns;
//
//import android.app.ActionBar.LayoutParams;
//import android.app.Activity;
//import android.content.Context;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.view.View.OnClickListener;
//
//public class BarbellConfig extends Activity{
//	
//	
//	static int CURRENT_LEFT_ANCHOR_ID  = 0;
//	static int CURRENT_RIGHT_ANCHOR_ID = 0;
//	static int CURRENT_LEFT_ID         = 0;
//	static int CURRENT_RIGHT_ID        = 0;
//	static int PREV_ID_HOLDER		   = 0;
//
//	static final int BARBELL_ID			   = 7000;
//	static final int RIGHT_PLATE_ID_DEPTH1 = 7011;
//	static final int LEFT_PLATE_ID_DEPTH1  = 7012;
//	static final int RIGHT_PLATE_ID_DEPTH2 = 7021;
//	static final int LEFT_PLATE_ID_DEPTH2  = 7022;
//	static final int RIGHT_PLATE_ID_DEPTH3 = 7031;
//	static final int LEFT_PLATE_ID_DEPTH3  = 7032;
//	static final int RIGHT_PLATE_ID_DEPTH4 = 7041;
//	static final int LEFT_PLATE_ID_DEPTH4  = 7042;
//	static final int RIGHT_PLATE_ID_DEPTH5 = 7051;
//	static final int LEFT_PLATE_ID_DEPTH5  = 7052;
//	static final int RIGHT_PLATE_ID_DEPTH6 = 7061;
//	static final int LEFT_PLATE_ID_DEPTH6  = 7062;
//	static final int RIGHT_PLATE_ID_DEPTH7 = 7071;
//	static final int LEFT_PLATE_ID_DEPTH7  = 7072;
//	static final int RIGHT_PLATE_ID_DEPTH8 = 7081;
//	static final int LEFT_PLATE_ID_DEPTH8  = 7082;
//	static final int RIGHT_PLATE_ID_DEPTH9 = 7091;
//	static final int LEFT_PLATE_ID_DEPTH9  = 7092;//enough for 855
//
//	//static final int 
//
//	ImageView CURRENT_LEFT_IMAGEVIEW;
//	ImageView CURRENT_RIGHT_IMAGEVIEW;
//	ImageView barbellImageView;
//	ImageView RIGHTFORTYFIVEIMAGEVIEW; 
//	ImageView LEFTFORTYFIVEIMAGEVIEW;
//	ImageView RIGHTFORTYFIVEIMAGEVIEW_DEPTH2;
//	ImageView LEFTFORTYFIVEIMAGEVIEW_DEPTH2;
//	ImageView RIGHTFORTYFIVEIMAGEVIEW_DEPTH3;
//	ImageView LEFTFORTYFIVEIMAGEVIEW_DEPTH3;
//	ImageView RIGHTFORTYFIVEIMAGEVIEW_DEPTH4;
//	ImageView LEFTFORTYFIVEIMAGEVIEW_DEPTH4;
//	ImageView RIGHTFORTYFIVEIMAGEVIEW_DEPTH5;
//	ImageView LEFTFORTYFIVEIMAGEVIEW_DEPTH5;
//	ImageView RIGHTFORTYFIVEIMAGEVIEW_DEPTH6;
//	ImageView LEFTFORTYFIVEIMAGEVIEW_DEPTH6;
//	ImageView RIGHTFORTYFIVEIMAGEVIEW_DEPTH7;
//	ImageView LEFTFORTYFIVEIMAGEVIEW_DEPTH7;
//	ImageView RIGHTFORTYFIVEIMAGEVIEW_DEPTH8;
//	ImageView LEFTFORTYFIVEIMAGEVIEW_DEPTH8;
//
////	RelativeLayout relativeLayout;
//
//
//	RelativeLayout.LayoutParams CURRENT_LEFT_LP;
//	RelativeLayout.LayoutParams CURRENT_RIGHT_LP;	
//	RelativeLayout.LayoutParams BARBELL_LP;
//	RelativeLayout.LayoutParams RIGHTPLATE_LP;
//	RelativeLayout.LayoutParams LEFTPLATE_LP;
//	RelativeLayout.LayoutParams RIGHTPLATE_LP2; //number represents depth
//	RelativeLayout.LayoutParams LEFTPLATE_LP2;
//	RelativeLayout.LayoutParams RIGHTPLATE_LP3;
//	RelativeLayout.LayoutParams LEFTPLATE_LP3;
//	RelativeLayout.LayoutParams RIGHTPLATE_LP4;
//	RelativeLayout.LayoutParams LEFTPLATE_LP4;
//	RelativeLayout.LayoutParams RIGHTPLATE_LP5;
//	RelativeLayout.LayoutParams LEFTPLATE_LP5;
//	RelativeLayout.LayoutParams RIGHTPLATE_LP6;
//	RelativeLayout.LayoutParams LEFTPLATE_LP6;
//	RelativeLayout.LayoutParams RIGHTPLATE_LP7;
//	RelativeLayout.LayoutParams LEFTPLATE_LP7;
//	RelativeLayout.LayoutParams RIGHTPLATE_LP8;
//	RelativeLayout.LayoutParams LEFTPLATE_LP8;
//	
//	int barbellWeight;
//	Double firstLift;
//	Boolean lbMode; //might need to be static 
//	
//	//lbNeeded declarations
//	int fortyfivesNeeded;
//	int thirtyfivesNeeded;
//	int twentyfivesNeeded;
//	int tensNeeded;
//	int fivesNeeded;
//	int twopointfivesNeeded;
//	
//	int twentyfivesNeeded_kg;
//	int twentysNeeded_kg;
//	int fifteensNeeded_kg;
//	int tensNeeded_kg;
//	int fivesNeeded_kg;
//	int twopointfivesNeeded_kg;
//	int twosNeeded_kg;
//	int onepointfivesNeeded_kg;
//	int onesNeeded_kg;
//	int pointfivesNeeded_kg;
//	
//	
//    TextView platesNeeded6;//this is first in list 
//    TextView platesNeeded5;
//    TextView platesNeeded4;
//    TextView platesNeeded3;
//    TextView platesNeeded2;
//    TextView platesNeeded1;
//    TextView platesNeeded_kgExtra1;
//    TextView platesNeeded_kgExtra2;
//    TextView platesNeeded_kgExtra3;
//    TextView platesNeeded_kgExtra4;
//	
//	Context mContext;
//	
//    public BarbellConfig (Context context){
//        mContext = context;
//    }
//	
//	public void configureBarbell(String myFirstLiftString, ImageView barbellImageView, RelativeLayout relativeLayout)
//	{
//		String firstLiftString = myFirstLiftString;
//		/*if (lbmodeString.contains("Lbs"))
//		lbmode = true;
//		if (lbmodeString.contains("Kgs"))
//		lbmode = false;*/ //need to figure something out to keep lbmode when viewing an existing projection
//		firstLift = Double.valueOf(firstLiftString);
//		//Toast.makeText(mContext, date + cycle + liftType + frequency + firstLiftString + lbmodeString, Toast.LENGTH_SHORT).show();
//		//mode
//		IndividualView myView = new IndividualView();
//		
//		if (getLbMode())//TODO add getlbmode to this class?
//		{
//			if (firstLift < 50)//then completely skip UI generation.. could have an empty barbell sprite but meh 	
//			{
//				barbellImageView.setVisibility(View.INVISIBLE);
//			}
//			else
//			{
//				if (firstLift > 45 && firstLift < 47)
//				{
//					barbellImageView.setImageResource(R.drawable.empty_barbell); 
//					barbellWeight = 45;
//				}
//				
//				//do something like setting plates here.. 
//				if (firstLift >= 135 ) //can we use our base barebell?	
//				{
//				barbellImageView.setImageResource(R.drawable.barbell_fortyfives_lb); 
//				barbellWeight = 135;
//				}
//
//				else if ( (firstLift <= 135) && (firstLift >= 115) /*&& thirtyFiveFlag*/)//assert: lift must be less than 135 to make it to this else 
//				{	
//					barbellImageView.setImageResource(R.drawable.barbell_thirtyfives);
//					barbellWeight = 115;
//				}
//
//				else if ((firstLift <= 115) && (firstLift >= 95)) // assert: lift must be less than 115 to make it to this else
//				{
//					barbellImageView.setImageDrawable((getResources().getDrawable(R.drawable.barbell_twentyfives)));
//					barbellWeight = 95;
//				}
//
//				else if ((firstLift <= 95) && (firstLift >= 65))
//				{
//					barbellImageView.setImageDrawable((getResources().getDrawable(R.drawable.barbell_tens)));
//					barbellWeight = 65;
//				}
//				else if ((firstLift <= 65) && (firstLift >= 50) )
//				{	
//					barbellImageView.setImageDrawable((getResources().getDrawable(R.drawable.barbell_twopointfives)));
//					barbellWeight = 50;
//				}
//				
////				RelativeLayout mrelativeLayout = mContext.getRelativeLayout();
//				((Activity) mContext).setContentView(relativeLayout);
//				relativeLayout.setOnClickListener((OnClickListener) mContext); 
//
//				PoundPlateComputer platecomputer = new PoundPlateComputer();
//				platecomputer.computeLbPlates(firstLift, barbellWeight);
//				
//				
//				fortyfivesNeeded = platecomputer.getFortyFivesNeeded();
//				thirtyfivesNeeded = platecomputer.getThirtyFivesNeeded();
//				twentyfivesNeeded = platecomputer.getTwentyFivesNeeded();
//				tensNeeded = platecomputer.getTensNeeded();
//				fivesNeeded = platecomputer.getFivesNeeded();
//				twopointfivesNeeded = platecomputer.getTwoPointFivesNeeded();
//				ConfigTool helper = new ConfigTool(mContext);
//				
//				if (fortyfivesNeeded > 0)
//				{
//				int iterations = fortyfivesNeeded / 2; //e.g. 6 plates = 3 iterations  
//				for (int i=0; i < iterations; i++)
//				{
//					moveOut();
//					dynamicAddPlate(R.drawable.plate_fourtyfive_lbs, barbellImageView, relativeLayout);
//				//moveOut();
//				//dynamicAddPlate(R.drawable.plate_fourtyfive_lbs);
//				}
//				}
//				
//				if (thirtyfivesNeeded > 0)
//				{
//				int iterations = thirtyfivesNeeded / 2; //e.g. 6 plates = 3 iterations  
//				for (int i=0; i < iterations; i++)
//				{
//					moveOut();
//					dynamicAddPlate(R.drawable.plate_thirtyfive_lbs, barbellImageView, relativeLayout);
//				}
//				}
//
//				if (twentyfivesNeeded > 0)
//				{
//				int iterations = twentyfivesNeeded / 2; //e.g. 6 plates = 3 iterations  
//				for (int i=0; i < iterations; i++)
//				{
//					moveOut();
//					dynamicAddPlate(R.drawable.plate_twentyfive_lbs, barbellImageView, relativeLayout);
//				}
//				}
//				
//				if (tensNeeded > 0)
//				{
//				int iterations = tensNeeded / 2; //e.g. 6 plates = 3 iterations  
//				for (int i=0; i < iterations; i++)
//				{
//					moveOut();
//					dynamicAddPlate(R.drawable.plate_ten_lbs, barbellImageView, relativeLayout);
//				}
//				}
//				
//				if (fivesNeeded > 0)
//				{
//				int iterations = fivesNeeded / 2; //e.g. 6 plates = 3 iterations  
//				for (int i=0; i < iterations; i++)
//				{
//					moveOut();
//					dynamicAddPlate(R.drawable.plate_five_lbs, barbellImageView, relativeLayout);
//				}
//				}
//				
//				if (twopointfivesNeeded > 0)
//				{
//				int iterations = twopointfivesNeeded / 2; //e.g. 6 plates = 3 iterations  
//				for (int i=0; i < iterations; i++)
//				{
//				moveOut();
//				dynamicAddPlate(R.drawable.plate_twopointfive_lbs, barbellImageView, relativeLayout);
//				}
//				}
//				
//				
//				
//				//now add plates that were on barbell
//				switch (barbellWeight)
//				{
//				case 135:
//					fortyfivesNeeded = fortyfivesNeeded + 2;
//					break;
//				case 115:
//					thirtyfivesNeeded = thirtyfivesNeeded + 2;
//					break;
//				case 95:
//					twentyfivesNeeded = twentyfivesNeeded + 2;
//					break;
//				case 65: 
//					tensNeeded = tensNeeded + 2;
//					break; 
//				case 55:
//					fivesNeeded = fivesNeeded + 2;
//					break;
//				case 50:
//					twopointfivesNeeded = twopointfivesNeeded + 2;
//					break;
//				}
//				relativeLayout.invalidate();
////				platesNeeded6 = (TextView) findViewById(R.id.platesNeeded6);
////				platesNeeded5 = (TextView) findViewById(R.id.platesNeeded5);
////				platesNeeded4 = (TextView) findViewById(R.id.platesNeeded4);
////				platesNeeded3 = (TextView) findViewById(R.id.platesNeeded3);
////				platesNeeded2 = (TextView) findViewById(R.id.platesNeeded2);
////				platesNeeded1 = (TextView) findViewById(R.id.platesNeeded1);
////				platesNeeded_kgExtra1 = (TextView) findViewById(R.id.platesNeeded_kgExtra1);
////				platesNeeded_kgExtra2 = (TextView) findViewById(R.id.platesNeeded_kgExtra2);
////				platesNeeded_kgExtra3 = (TextView) findViewById(R.id.platesNeeded_kgExtra3);
////				platesNeeded_kgExtra4 = (TextView) findViewById(R.id.platesNeeded_kgExtra4);//chacaron
////				
////				//add data to text views
////				platesNeeded6.setText("45s needed: " + fortyfivesNeeded);
////				platesNeeded5.setText("35s needed: " + thirtyfivesNeeded);
////				platesNeeded4.setText("25s needed: " + twentyfivesNeeded);
////				platesNeeded3.setText("10s needed: " + tensNeeded);
////				platesNeeded2.setText("5s needed: " + fivesNeeded);
////				platesNeeded1.setText("2.5s needed: " + twopointfivesNeeded);
////				platesNeeded_kgExtra1.setVisibility(View.INVISIBLE);
////				platesNeeded_kgExtra2.setVisibility(View.INVISIBLE);
////				platesNeeded_kgExtra3.setVisibility(View.INVISIBLE);
////				platesNeeded_kgExtra4.setVisibility(View.INVISIBLE);don't acces UI thread outside of jimmy 
//	
//			}//end else to checking if UI generation is even possible  
//
//
//			//generate plates needed if UI is not generated 
//
//
//		}//end taking care of lbmode condition 
////		else //otheriwse we are working in kilograms..
////		{
////			if (firstLift < 20)//then completely skip UI generation.. could have an empty barbell sprite but meh 	
////			{
////				barbellImageView.setVisibility(View.INVISIBLE);
////			}
////			else
////			{
////				if (firstLift == 20)
////				{
////					barbellImageView.setImageResource(R.drawable.empty_barbell);  
////					barbellWeight = 45;
////				}
////				
////				//do something like setting plates here.. 
////				if (firstLift >= 70 ) //can we use our base barebell?	
////				{
////				barbellImageView.setImageResource(R.drawable.barbell_twentyfive_kg); 
////				barbellWeight = 70;
////				}
////
////				else if ( (firstLift <= 70) && (firstLift >= 60))//assert: lift must be less than 135 to make it to this else 
////				{	
////					barbellImageView.setImageResource(R.drawable.barbell_twenty_kg);
////					barbellWeight = 60;
////				}
////
////				else if ((firstLift <= 60) && (firstLift >= 50)) // assert: lift must be less than 115 to make it to this else
////				{
////					barbellImageView.setImageDrawable((getResources().getDrawable(R.drawable.barbell_fifteen_kg)));
////					barbellWeight = 50;
////				}
////
////				else if ((firstLift <= 50) && (firstLift >= 40))
////				{
////					barbellImageView.setImageDrawable((getResources().getDrawable(R.drawable.barbell_ten_kg)));
////					barbellWeight = 40;
////				}
////				else if ((firstLift <= 40) && (firstLift >= 30) )
////				{	
////					barbellImageView.setImageDrawable((getResources().getDrawable(R.drawable.barbell_five_kg)));
////					barbellWeight = 30;
////				}
////				else if ((firstLift <= 30) && (firstLift >= 25) )
////				{	
////					barbellImageView.setImageDrawable((getResources().getDrawable(R.drawable.barbell_twopointfive_kg)));
////					barbellWeight = 25;
////				}
////				else if ((firstLift <= 25) && (firstLift >= 24) )
////				{	
////					barbellImageView.setImageDrawable((getResources().getDrawable(R.drawable.barbell_two_kg)));
////					barbellWeight = 24;
////				}
////				else if ((firstLift <= 24) && (firstLift >= 23) )
////				{	
////					barbellImageView.setImageDrawable((getResources().getDrawable(R.drawable.barbell_onepointfive_kg)));
////					barbellWeight = 23;
////				}
////				else if ((firstLift <= 23) && (firstLift >= 22) )
////				{	
////					barbellImageView.setImageDrawable((getResources().getDrawable(R.drawable.barbell_one_kg)));
////					barbellWeight = 22;
////				}
////				else if ((firstLift <= 22) && (firstLift >= 21) )
////				{	
////					barbellImageView.setImageDrawable((getResources().getDrawable(R.drawable.barbell_pointfive_kg)));
////					barbellWeight = 21;
////				}
////				
////				relativeLayout=  (RelativeLayout) findViewById(R.id.individualView);
////				setContentView(relativeLayout);
////				//relativeLayout.setOnClickListener(mContext); 
////
////				KilogramPlateComputer platecomputer = new KilogramPlateComputer();
////				platecomputer.computeKgPlates(firstLift, barbellWeight);
////				
////				
////				 twentyfivesNeeded_kg = platecomputer.getTwentyFivesNeeded();
////				 twentysNeeded_kg = platecomputer.getTwentysNeeded();
////				 fifteensNeeded_kg = platecomputer.getFifteensNeeded();
////				 tensNeeded_kg = platecomputer.getTensNeeded();
////				 fivesNeeded_kg = platecomputer.getFivesNeeded();
////				 twopointfivesNeeded_kg = platecomputer.getTwopointfivesNeeded();
////				 twosNeeded_kg = platecomputer.getTwosNeeded();
////				 onepointfivesNeeded_kg = platecomputer.getOnepointfivesNeeded();
////				 onesNeeded_kg = platecomputer.getOnesNeeded();
////				 pointfivesNeeded_kg = platecomputer.getPointfivesNeeded();
////				
////				
////				
////				if (twentyfivesNeeded_kg > 0)
////				{
////				int iterations = twentyfivesNeeded_kg / 2; //e.g. 6 plates = 3 iterations  
////				for (int i=0; i < iterations; i++)
////				{
////				moveOut();
////				dynamicAddPlate(R.drawable.plate_twentyfive_kg);
////				}
////				}
////				
////				if (twentysNeeded_kg > 0)
////				{
////				int iterations = twentysNeeded_kg / 2; //e.g. 6 plates = 3 iterations  
////				for (int i=0; i < iterations; i++)
////				{
////				moveOut();
////				dynamicAddPlate(R.drawable.plate_twenty_kg);
////				}
////				}
////
////				if (fifteensNeeded_kg > 0)
////				{
////				int iterations = fifteensNeeded_kg / 2; //e.g. 6 plates = 3 iterations  
////				for (int i=0; i < iterations; i++)
////				{
////				moveOut();
////				dynamicAddPlate(R.drawable.plate_fifteen_kg);
////				}
////				}
////				
////				if (tensNeeded_kg > 0)
////				{
////				int iterations = tensNeeded_kg / 2; //e.g. 6 plates = 3 iterations  
////				for (int i=0; i < iterations; i++)
////				{
////				moveOut();
////				dynamicAddPlate(R.drawable.plate_ten_kg);
////				}
////				}
////				
////				if (fivesNeeded_kg > 0)
////				{
////				int iterations = fivesNeeded_kg / 2; //e.g. 6 plates = 3 iterations  
////				for (int i=0; i < iterations; i++)
////				{
////				moveOut();
////				dynamicAddPlate(R.drawable.plate_five_kg);
////				}
////				}
////				
////				if (twopointfivesNeeded_kg > 0)
////				{
////				int iterations = twopointfivesNeeded_kg / 2; //e.g. 6 plates = 3 iterations  
////				for (int i=0; i < iterations; i++)
////				{
////				moveOut();
////				dynamicAddPlate(R.drawable.plate_twopointfive_kg);
////				}
////				}
////				
////				if (twosNeeded_kg > 0)
////				{
////				int iterations = twosNeeded_kg / 2; //e.g. 6 plates = 3 iterations  
////				for (int i=0; i < iterations; i++)
////				{
////				moveOut();
////				dynamicAddPlate(R.drawable.plate_two_kg);
////				}
////				}
////				
////				if (onepointfivesNeeded_kg > 0)
////				{
////				int iterations = onepointfivesNeeded_kg / 2; //e.g. 6 plates = 3 iterations  
////				for (int i=0; i < iterations; i++)
////				{
////				moveOut();
////				dynamicAddPlate(R.drawable.plate_onepointfive_kg);
////				}
////				}
////				
////				if (onesNeeded_kg > 0)
////				{
////				int iterations = onesNeeded_kg / 2; //e.g. 6 plates = 3 iterations  
////				for (int i=0; i < iterations; i++)
////				{
////				moveOut();
////				dynamicAddPlate(R.drawable.plate_one_kg);
////				}
////				}
////				
////				if (pointfivesNeeded_kg > 0)
////				{
////				int iterations = pointfivesNeeded_kg / 2; //e.g. 6 plates = 3 iterations  
////				for (int i=0; i < iterations; i++)
////				{
////				moveOut();
////				dynamicAddPlate(R.drawable.plate_pointfive_kg);
////				}
////				}
////				
////				
////				//now add plates that were on barbell
////				switch (barbellWeight)
////				{
////				case 70:
////					twentyfivesNeeded_kg = twentyfivesNeeded_kg + 2;
////					break;
////				case 60:
////					twentysNeeded_kg = twentysNeeded_kg + 2;
////					break;
////				case 50:
////					fifteensNeeded_kg = fifteensNeeded_kg + 2;
////					break;
////				case 40: 
////					tensNeeded_kg = tensNeeded_kg + 2;
////					break; 
////				case 30:
////					fivesNeeded_kg = fivesNeeded_kg + 2;
////					break;
////				case 25:
////					twopointfivesNeeded_kg = twopointfivesNeeded_kg + 2;
////					break;
////				case 23:
////					onepointfivesNeeded_kg = onepointfivesNeeded_kg + 2;
////					break;	
////				case 22:
////					onesNeeded_kg = onesNeeded_kg + 2;
////					break;
////				case 21:
////					pointfivesNeeded_kg = pointfivesNeeded_kg + 2;
////					break;
////				}
////				relativeLayout.invalidate();
////				platesNeeded6 = (TextView) findViewById(R.id.platesNeeded6);
////				platesNeeded5 = (TextView) findViewById(R.id.platesNeeded5);
////				platesNeeded4 = (TextView) findViewById(R.id.platesNeeded4);
////				platesNeeded3 = (TextView) findViewById(R.id.platesNeeded3);
////				platesNeeded2 = (TextView) findViewById(R.id.platesNeeded2);
////				platesNeeded1 = (TextView) findViewById(R.id.platesNeeded1);
////			    platesNeeded_kgExtra1 = (TextView) findViewById(R.id.platesNeeded_kgExtra1);
////			    platesNeeded_kgExtra2 = (TextView) findViewById(R.id.platesNeeded_kgExtra2);
////			    platesNeeded_kgExtra3 = (TextView) findViewById(R.id.platesNeeded_kgExtra3);
////			    platesNeeded_kgExtra4 = (TextView) findViewById(R.id.platesNeeded_kgExtra4);
////				
////				//add data to text views
////				platesNeeded6.setText("25s needed: " + twentyfivesNeeded_kg);
////				platesNeeded5.setText("20s needed: " + twentysNeeded_kg);
////				platesNeeded4.setText("15s needed: " + fifteensNeeded_kg);
////				platesNeeded3.setText("10s needed: " + tensNeeded_kg);
////				platesNeeded2.setText("5s needed: " + fivesNeeded_kg);
////				platesNeeded1.setText("2.5s needed: " + twopointfivesNeeded_kg);
////				platesNeeded_kgExtra1.setText("2s needed: " + twosNeeded_kg );
////				platesNeeded_kgExtra2.setText("1.5s needed: " + onepointfivesNeeded_kg );
////				platesNeeded_kgExtra3.setText("1s needed: " + onesNeeded_kg );;
////				platesNeeded_kgExtra4.setText(".5s needed: " + pointfivesNeeded_kg );
////				//TODO add more textviews for the extra plates
////				
////			}//end else to checking if UI generation is even possible  
////		}
//	}// end method barbellConfig
//	
//	public void *(int resource, ImageView barbellImageView, RelativeLayout mrelativeLayout)
//	{
////		barbellImageView = (ImageView) findViewById(R.id.barbell);
//		
//		//Right/left 45 Setup
//		CURRENT_RIGHT_IMAGEVIEW = new ImageView(mContext);
//		CURRENT_LEFT_IMAGEVIEW = new ImageView(mContext);
//		//setting image resource
//		//setting image position
//		CURRENT_RIGHT_IMAGEVIEW.setImageResource(resource);
//		CURRENT_LEFT_IMAGEVIEW.setImageResource(resource);
//
//		//right
//		
//		CURRENT_RIGHT_LP = new RelativeLayout.LayoutParams(
//				LayoutParams.WRAP_CONTENT,
//				LayoutParams.WRAP_CONTENT);
//		CURRENT_RIGHT_IMAGEVIEW.setId(CURRENT_RIGHT_ANCHOR_ID);
//		CURRENT_RIGHT_LP.addRule(RelativeLayout.RIGHT_OF, CURRENT_RIGHT_ANCHOR_ID);
//		CURRENT_RIGHT_LP.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//		//CURRENT_RIGHT_LP.setMargins(0, 17, 0, 0);
//		CURRENT_RIGHT_IMAGEVIEW.setLayoutParams(CURRENT_RIGHT_LP);
//		CURRENT_RIGHT_IMAGEVIEW.setId(CURRENT_RIGHT_ID);
//
//		//left
//		RelativeLayout.LayoutParams CURRENT_LEFT_LP = new RelativeLayout.LayoutParams(
//				LayoutParams.WRAP_CONTENT,
//				LayoutParams.WRAP_CONTENT); //THOS
//		CURRENT_LEFT_IMAGEVIEW.setId(CURRENT_LEFT_ANCHOR_ID);
//		CURRENT_LEFT_LP.addRule(RelativeLayout.LEFT_OF, CURRENT_LEFT_ANCHOR_ID);
//		CURRENT_LEFT_LP.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//		//CURRENT_LEFT_LP.setMargins(0, 17, 0, 0);
//
//
//		CURRENT_LEFT_IMAGEVIEW.setLayoutParams(CURRENT_LEFT_LP);
//		CURRENT_LEFT_IMAGEVIEW.setId(CURRENT_LEFT_ID);
//
//
//
//		//mrelativeLayout=  (RelativeLayout) findViewById(R.id.individualView);
//
//
//		//adding view to layout
//		mrelativeLayout.addView(CURRENT_RIGHT_IMAGEVIEW);
//		mrelativeLayout.addView(CURRENT_LEFT_IMAGEVIEW);
//		//make visible to program
//
//		((Activity) mContext).setContentView(mrelativeLayout);		
//
//	}
//    
//    
//	public void moveOut ()
//	{
//		IndividualView viewInstantiation = new IndividualView();
//		
//		viewInstantiation.idCounter++;
//		switch (viewInstantiation.idCounter)
//		{
//		case 1://first time this is called our anchor for the left and right plate will be the same--the barbell.
//			viewInstantiation.CURRENT_LEFT_ANCHOR_ID  = R.id.barbell;
//			viewInstantiation.CURRENT_RIGHT_ANCHOR_ID = R.id.barbell;
//			CURRENT_RIGHT_IMAGEVIEW = RIGHTFORTYFIVEIMAGEVIEW;
//			CURRENT_LEFT_IMAGEVIEW  = LEFTFORTYFIVEIMAGEVIEW;
//			CURRENT_LEFT_ID         = LEFT_PLATE_ID_DEPTH1;
//			CURRENT_RIGHT_ID        = RIGHT_PLATE_ID_DEPTH1;	
//			CURRENT_RIGHT_LP 		= RIGHTPLATE_LP;
//			CURRENT_LEFT_LP 	    = LEFTPLATE_LP;
//			break;
//		case 2:
//			CURRENT_LEFT_ANCHOR_ID  = LEFT_PLATE_ID_DEPTH1;
//			CURRENT_RIGHT_ANCHOR_ID = RIGHT_PLATE_ID_DEPTH1;
//			CURRENT_RIGHT_IMAGEVIEW = RIGHTFORTYFIVEIMAGEVIEW_DEPTH2;
//			CURRENT_LEFT_IMAGEVIEW  = LEFTFORTYFIVEIMAGEVIEW_DEPTH2;
//			CURRENT_LEFT_ID         = LEFT_PLATE_ID_DEPTH2;
//			CURRENT_RIGHT_ID        = RIGHT_PLATE_ID_DEPTH2;
//			CURRENT_RIGHT_LP 		= RIGHTPLATE_LP2;
//			CURRENT_LEFT_LP 	    = LEFTPLATE_LP2;
//			break;
//		case 3:
//			CURRENT_LEFT_ANCHOR_ID  = LEFT_PLATE_ID_DEPTH2;
//			CURRENT_RIGHT_ANCHOR_ID = RIGHT_PLATE_ID_DEPTH2;
//			CURRENT_RIGHT_IMAGEVIEW = RIGHTFORTYFIVEIMAGEVIEW_DEPTH3;
//			CURRENT_LEFT_IMAGEVIEW  = LEFTFORTYFIVEIMAGEVIEW_DEPTH3;
//			CURRENT_LEFT_ID         = LEFT_PLATE_ID_DEPTH3;
//			CURRENT_RIGHT_ID        = RIGHT_PLATE_ID_DEPTH3;		
//			CURRENT_RIGHT_LP 		= RIGHTPLATE_LP3;
//			CURRENT_LEFT_LP 	    = LEFTPLATE_LP3;
//			break;
//		case 4:	
//			CURRENT_LEFT_ANCHOR_ID  = LEFT_PLATE_ID_DEPTH3;
//			CURRENT_RIGHT_ANCHOR_ID = RIGHT_PLATE_ID_DEPTH3;
//			CURRENT_RIGHT_IMAGEVIEW = RIGHTFORTYFIVEIMAGEVIEW_DEPTH4;
//			CURRENT_LEFT_IMAGEVIEW  = LEFTFORTYFIVEIMAGEVIEW_DEPTH4;
//			CURRENT_LEFT_ID         = LEFT_PLATE_ID_DEPTH4;
//			CURRENT_RIGHT_ID        = RIGHT_PLATE_ID_DEPTH4;		
//			CURRENT_RIGHT_LP 		= RIGHTPLATE_LP4;
//			CURRENT_LEFT_LP 	    = LEFTPLATE_LP4;
//			break;
//		case 5:
//			CURRENT_LEFT_ANCHOR_ID  = LEFT_PLATE_ID_DEPTH4;
//			CURRENT_RIGHT_ANCHOR_ID = RIGHT_PLATE_ID_DEPTH4;
//			CURRENT_RIGHT_IMAGEVIEW = RIGHTFORTYFIVEIMAGEVIEW_DEPTH5;
//			CURRENT_LEFT_IMAGEVIEW  = LEFTFORTYFIVEIMAGEVIEW_DEPTH5;
//			CURRENT_LEFT_ID         = LEFT_PLATE_ID_DEPTH5;
//			CURRENT_RIGHT_ID        = RIGHT_PLATE_ID_DEPTH5;		
//			CURRENT_RIGHT_LP 		= RIGHTPLATE_LP5;
//			CURRENT_LEFT_LP 	    = LEFTPLATE_LP5;
//			break;
//		case 6:
//			CURRENT_LEFT_ANCHOR_ID  = LEFT_PLATE_ID_DEPTH5;
//			CURRENT_RIGHT_ANCHOR_ID = RIGHT_PLATE_ID_DEPTH5;
//			CURRENT_RIGHT_IMAGEVIEW = RIGHTFORTYFIVEIMAGEVIEW_DEPTH6;
//			CURRENT_LEFT_IMAGEVIEW  = LEFTFORTYFIVEIMAGEVIEW_DEPTH6;
//			CURRENT_LEFT_ID         = LEFT_PLATE_ID_DEPTH6;
//			CURRENT_RIGHT_ID        = RIGHT_PLATE_ID_DEPTH6;		
//			CURRENT_RIGHT_LP 		= RIGHTPLATE_LP6;
//			CURRENT_LEFT_LP 	    = LEFTPLATE_LP6;
//			break;
//		case 7:	
//			CURRENT_LEFT_ANCHOR_ID  = LEFT_PLATE_ID_DEPTH6;
//			CURRENT_RIGHT_ANCHOR_ID = RIGHT_PLATE_ID_DEPTH6;
//			CURRENT_RIGHT_IMAGEVIEW = RIGHTFORTYFIVEIMAGEVIEW_DEPTH7;
//			CURRENT_LEFT_IMAGEVIEW  = LEFTFORTYFIVEIMAGEVIEW_DEPTH7;
//			CURRENT_LEFT_ID         = LEFT_PLATE_ID_DEPTH7;
//			CURRENT_RIGHT_ID        = RIGHT_PLATE_ID_DEPTH7;		
//			CURRENT_RIGHT_LP 		= RIGHTPLATE_LP7;
//			CURRENT_LEFT_LP 	    = LEFTPLATE_LP7;
//			break;
//		case 8:	
//			CURRENT_LEFT_ANCHOR_ID  = LEFT_PLATE_ID_DEPTH7;
//			CURRENT_RIGHT_ANCHOR_ID = RIGHT_PLATE_ID_DEPTH7;
//			CURRENT_RIGHT_IMAGEVIEW = RIGHTFORTYFIVEIMAGEVIEW_DEPTH8;
//			CURRENT_LEFT_IMAGEVIEW  = LEFTFORTYFIVEIMAGEVIEW_DEPTH8;
//			CURRENT_LEFT_ID         = LEFT_PLATE_ID_DEPTH8;
//			CURRENT_RIGHT_ID        = RIGHT_PLATE_ID_DEPTH8;		
//			CURRENT_RIGHT_LP 		= RIGHTPLATE_LP8;
//			CURRENT_LEFT_LP 	    = LEFTPLATE_LP8;
//			break;
//		default:
//			break;
//		}
//
//	}//end method moveOut()
//	
//	
//	boolean getLbMode()
//	{
//		return lbMode;
//	}
//	
//	void setLbMode(String lbModeString, TextView modeTV) {
//		int buffer = Integer.valueOf(lbModeString);
//		if (buffer == 1)
//		{
//		lbMode = true;
//		modeTV.setText("Mode: lbs");
//		}
//		else if (buffer == 0)
//		{
//		lbMode = false;
//		modeTV.setText("Mode: kgs");
//		}
//		else
//		{
//		lbMode = null;//create an error
//		modeTV.setText("Mode error!");
//		}
//	}
//	
//}
