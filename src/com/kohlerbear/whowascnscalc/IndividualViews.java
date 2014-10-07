package com.kohlerbear.whowascnscalc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class IndividualViews extends ActionBarActivity {
	
	
    
    //mode, viewmode, and lift pattern will be for if we support swipe navigation, however, I think that making cells bigger is probably better off.
    
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_individual_views);
		
		TextView dateTV = (TextView) findViewById(R.id.indVDate);
		TextView cycleTV = (TextView) findViewById(R.id.indVCycle);
		TextView typefreqTV = (TextView) findViewById(R.id.indVTypeFreq);
		TextView firstLiftTV = (TextView) findViewById(R.id.indVLiftOne);
		TextView secondLiftTV = (TextView) findViewById(R.id.indVLiftTwo);
		TextView thirdLiftTV = (TextView) findViewById(R.id.indVLiftThree);
		
		//Lift one declarationss
		ImageView lift1Barbell = (ImageView) findViewById(R.id.liftOneBarbell);
	    ImageView l1r1 = (ImageView) findViewById(R.id.lift1r1);
	    ImageView l1r2 = (ImageView) findViewById(R.id.lift1r2);
	    ImageView l1r3 = (ImageView) findViewById(R.id.lift1r3);
	    ImageView l1r4 = (ImageView) findViewById(R.id.lift1r1);
	    ImageView l1r5 = (ImageView) findViewById(R.id.lift1r5);
	    ImageView l1r6 = (ImageView) findViewById(R.id.lift1r6);
	    ImageView l1r7 = (ImageView) findViewById(R.id.lift1r7);
	    ImageView l1r8 = (ImageView) findViewById(R.id.lift1r8);
	    
	    
	    ImageView l1l1 = (ImageView) findViewById(R.id.lift1l1);
	    ImageView l1l2 = (ImageView) findViewById(R.id.lift1l2);
	    ImageView l1l3 = (ImageView) findViewById(R.id.lift1l3);
	    ImageView l1l4 = (ImageView) findViewById(R.id.lift1l1);
	    ImageView l1l5 = (ImageView) findViewById(R.id.lift1l5);
	    ImageView l1l6 = (ImageView) findViewById(R.id.lift1l6);
	    ImageView l1l7 = (ImageView) findViewById(R.id.lift1l7);
	    ImageView l1l8 = (ImageView) findViewById(R.id.lift1l8);
		
	    //lift two declarations 
	    ImageView lift2Barbell = (ImageView) findViewById(R.id.liftTwoBarbell);
	    ImageView l2r1 = (ImageView) findViewById(R.id.lift2r1);
	    ImageView l2r2 = (ImageView) findViewById(R.id.lift2r2);
	    ImageView l2r3 = (ImageView) findViewById(R.id.lift2r3);
	    ImageView l2r4 = (ImageView) findViewById(R.id.lift2r1);
	    ImageView l2r5 = (ImageView) findViewById(R.id.lift2r5);
	    ImageView l2r6 = (ImageView) findViewById(R.id.lift2r6);
	    ImageView l2r7 = (ImageView) findViewById(R.id.lift2r7);
	    ImageView l2r8 = (ImageView) findViewById(R.id.lift2r8);
	    
	    ImageView l2l1 = (ImageView) findViewById(R.id.lift2l1);
	    ImageView l2l2 = (ImageView) findViewById(R.id.lift2l2);
	    ImageView l2l3 = (ImageView) findViewById(R.id.lift2l3);
	    ImageView l2l4 = (ImageView) findViewById(R.id.lift2l1);
	    ImageView l2l5 = (ImageView) findViewById(R.id.lift2l5);
	    ImageView l2l6 = (ImageView) findViewById(R.id.lift2l6);
	    ImageView l2l7 = (ImageView) findViewById(R.id.lift2l7);
	    ImageView l2l8 = (ImageView) findViewById(R.id.lift2l8);
	    
	    
	    //lift three declarations
	    ImageView lift3Barbell = (ImageView) findViewById(R.id.liftThreeBarbell);
	    ImageView l3r1 = (ImageView) findViewById(R.id.lift3r1);
	    ImageView l3r2 = (ImageView) findViewById(R.id.lift3r2);
	    ImageView l3r3 = (ImageView) findViewById(R.id.lift3r3);
	    ImageView l3r4 = (ImageView) findViewById(R.id.lift3r1);
	    ImageView l3r5 = (ImageView) findViewById(R.id.lift3r5);
	    ImageView l3r6 = (ImageView) findViewById(R.id.lift3r6);
	    ImageView l3r7 = (ImageView) findViewById(R.id.lift3r7);
	    ImageView l3r8 = (ImageView) findViewById(R.id.lift3r8);
	    
	    ImageView l3l1 = (ImageView) findViewById(R.id.lift3l1);
	    ImageView l3l2 = (ImageView) findViewById(R.id.lift3l2);
	    ImageView l3l3 = (ImageView) findViewById(R.id.lift3l3);
	    ImageView l3l4 = (ImageView) findViewById(R.id.lift3l1);
	    ImageView l3l5 = (ImageView) findViewById(R.id.lift3l5);
	    ImageView l3l6 = (ImageView) findViewById(R.id.lift3l6);
	    ImageView l3l7 = (ImageView) findViewById(R.id.lift3l7);
	    ImageView l3l8 = (ImageView) findViewById(R.id.lift3l8);
	    

	    
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
		
		
		
	    int cycle = Integer.valueOf(prevScreen.getStringExtra("cycle"));
	    String frequency = prevScreen.getStringExtra("frequency");
	    String liftType = prevScreen.getStringExtra("liftType");
	    double firstLift = Double.valueOf(prevScreen.getStringExtra("firstLift")); 
	    double secondLift = Double.valueOf(prevScreen.getStringExtra("secondLift")); 	
	    double thirdLift = Double.valueOf(prevScreen.getStringExtra("thirdLift")); 
	    String date = prevScreen.getStringExtra("date");
		
		dateTV.setText(date);
		cycleTV.setText(String.valueOf(cycle));
		typefreqTV.setText(liftType + " " + frequency);
		//5-5-5
		firstLiftTV.setText(String.valueOf(firstLift + "x" + frequency.charAt(0)));
		secondLiftTV.setText(String.valueOf(secondLift+ "x" + frequency.charAt(2)));
		thirdLiftTV.setText(String.valueOf(thirdLift)+ "x" + frequency.charAt(4));
	}


}
