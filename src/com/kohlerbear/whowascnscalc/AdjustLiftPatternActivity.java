package com.kohlerbear.whowascnscalc;


import java.util.HashMap;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class AdjustLiftPatternActivity extends ActionBarActivity {
	TextView option1, option2, option3, option4, option5, choice1, choice2, choice3, choice4, choice5, choice6, choice7;
	Button backButton, resetButton, saveButton;
	String [] liftPattern = new String[7]; //max size of 7 
	Spinner patternSizeSpinner;
	Tracker tracker = null;

	//http://code.tutsplus.com/tutorials/android-sdk-implementing-drag-and-drop-functionality--mobile-14402
	//NEED TO RESIZE ARRAYS
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.AppBaseThemeNoTitleBar);
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.adjustliftpattern);
	    
	    //button declarations
		backButton = (Button) findViewById(R.id.adjustActivityBackButton);
		resetButton = (Button) findViewById(R.id.adjustActivityResetButton);
		saveButton = (Button) findViewById(R.id.adjustActivitySaveButton);
	  
		
		backButton.setOnClickListener(backListener);
		resetButton.setOnClickListener(resetListener);
		saveButton.setOnClickListener(saveListener);
		
		tracker = GoogleAnalytics.getInstance(this).getTracker("UA-55018534-1");
		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME, "Pattern Screen");

		tracker.send(hitParameters);
		
		patternSizeSpinner = (Spinner) findViewById (R.id.patternSizeSpinner);
		
	    
	  //views to drag
	    option1 = (TextView) findViewById(R.id.option1);
	    option2 = (TextView)findViewById(R.id.option2);
	    option3 = (TextView)findViewById(R.id.option3);
	    option4 = (TextView)findViewById(R.id.option4);
	    option5 = (TextView)findViewById(R.id.option5);
	    //views to drop onto
	    choice1 = (TextView)findViewById(R.id.choice_1);
	    choice2 = (TextView)findViewById(R.id.choice_2);
	    choice3 = (TextView)findViewById(R.id.choice_3);
	    choice4 = (TextView)findViewById(R.id.choice_4);
	    choice5 = (TextView)findViewById(R.id.choice_5);
	    choice6 = (TextView)findViewById(R.id.choice_6);
	    choice7 = (TextView)findViewById(R.id.choice_7); 
	    TextView[] choices = {choice1, choice2, choice3, choice4, choice5, choice6, choice7};
	    
	    
	  //set touch listeners
	    option1.setOnTouchListener(new ChoiceTouchListener());
	    option2.setOnTouchListener(new ChoiceTouchListener());
	    option3.setOnTouchListener(new ChoiceTouchListener());
	    option4.setOnTouchListener(new ChoiceTouchListener());
	    option5.setOnTouchListener(new ChoiceTouchListener());
	    
	  //set drag listeners
	    choice1.setOnDragListener(new ChoiceDragListener());
	    choice2.setOnDragListener(new ChoiceDragListener());
	    choice3.setOnDragListener(new ChoiceDragListener());
	    choice4.setOnDragListener(new ChoiceDragListener());
	    choice5.setOnDragListener(new ChoiceDragListener());
	    choice6.setOnDragListener(new ChoiceDragListener());
	    choice7.setOnDragListener(new ChoiceDragListener());
	    
	    //add spinner listener
	    patternSizeSpinner.setOnItemSelectedListener(spinnerListener);
	    
	    Intent intent = getIntent();
	    String[] pattern = intent.getStringArrayExtra("pattern");
	    liftPattern = pattern;
	    int patternIndex = 0;
	    //0 - adjust pattern
	    //1 - 4 days
	    //2 - 5 days
	    //3 - 6 days
	    //4 - 7 days
	    patternSizeSpinner.setSelection(liftPattern.length - 3);
	    while (patternIndex < liftPattern.length)
	    {
	    	choices[patternIndex].setText(liftPattern[patternIndex]);
	    	patternIndex++;
	    }	    
	    if (patternIndex < choices.length)
	    {
		    while (patternIndex < choices.length)
		    {
		    	TextView choice = choices[patternIndex];
		    	choice.setVisibility(View.INVISIBLE);
		    	patternIndex++;
		    }
	    }
	    
	}

	private final class ChoiceTouchListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
			    //setup drag
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
				  //start dragging the item touched
				view.startDrag(data, shadowBuilder, view, 0);
			    return true;
			}
			else {
			    return false;
			}
		}
	}//end clas choicetouchlistener

	private class ChoiceDragListener implements OnDragListener {
		@Override
		public boolean onDrag(View v, DragEvent event) {
		    //handle drag events
			switch (event.getAction()) {
		    case DragEvent.ACTION_DRAG_STARTED:
		        //no action necessary
		        break;
		    case DragEvent.ACTION_DRAG_ENTERED:
		        //no action necessary
		        break;
		    case DragEvent.ACTION_DRAG_EXITED:       
		        //no action necessary
		        break;
		    case DragEvent.ACTION_DROP:
		        //handle the dragged view being dropped over a drop view
		    	View view = (View) event.getLocalState();
		    	//stop displaying the view where it was before it was dragged
//		    	view.setVisibility(View.INVISIBLE);
		    	//view dragged item is being dropped on
		    	TextView dropTarget = (TextView) v;
		    	//view being dragged and dropped
		    	TextView dropped = (TextView) view;
		    	String dropArea = (String) dropTarget.getText();
		    	//update the text in the target view to reflect the data being dropped
		    	dropTarget.setText(dropped.getText());
		    	updateLiftArray(dropArea.toLowerCase(), (String) dropped.getText());
		    	//make it bold to highlight the fact that an item has been dropped
		    	dropTarget.setTypeface(Typeface.DEFAULT_BOLD);
		    	//if an item has already been dropped here, there will be a tag
		    	Object tag = dropTarget.getTag();
		    	//if there is already an item here, set it back visible in its original place
		    	if(tag!=null)
		    	{
		    	    //the tag is the view id already dropped here
		    	    int existingID = (Integer)tag;
		    	    //set the original view visible again
		    	    findViewById(existingID).setVisibility(View.VISIBLE);
		    	}
		    	//set the tag in the target view to the ID of the view being dropped
		    	dropTarget.setTag(dropped.getId());
		        break;
		    case DragEvent.ACTION_DRAG_ENDED:
		        //no action necessary
		        break;
		    default:
		        break;
		}
		    return true;
		}

		private void updateLiftArray(String dropArea, String liftName) {
			
			switch (dropArea)
			{
			case "first":
				liftPattern[0] = liftName;
				break;
			case "second": 
				liftPattern[1] = liftName;
				break;
			case "third":
				liftPattern[2] = liftName;
				break;
			case "fourth":
				liftPattern[3] = liftName;
				break;
			case "fifth":
				liftPattern[4] = liftName;
				break;
			case "sixth":
				liftPattern[5] = liftName;
				break;
			case "seventh":
				liftPattern[6] = liftName;
				break;
			}
			
		}
	}//end class ChoiceDragListener


	//parse the array based on whats chosen
	void parseArray()
	{
	//error handling and other unforseeable shit here	
	}


	private OnClickListener resetListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
				Intent resetIntent = new Intent(AdjustLiftPatternActivity.this, AdjustLiftPatternActivity.class);
				//pass date or whatever here I think... (you'll be geting it from first screen (mainActivity) 
				startActivity(resetIntent);
				overridePendingTransition(0, 0); //no animation on reset
			}};

			

		
		
	private OnClickListener backListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			Intent backToFirst = new Intent (AdjustLiftPatternActivity.this, MainActivity.class);
			//TODO add back date here i think
			startActivity(backToFirst);
			
		}};	

	private OnClickListener saveListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			if (validatePattern())
			{
			Intent patternToFirst = new Intent(AdjustLiftPatternActivity.this, MainActivity.class);
			//pass useful shit here (i.e. the date and the pattern
			patternToFirst.putExtra("origin", "patternAdjust");
			patternToFirst.putExtra("liftPattern", liftPattern);
			startActivity(patternToFirst);
			}
		}};
		
	private OnItemSelectedListener spinnerListener = new OnItemSelectedListener(){

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			switch (position)
			{
			case 1: //pattern size is 4
				liftPattern = new String[4];
				choice1.setVisibility(View.VISIBLE);
				choice2.setVisibility(View.VISIBLE);
				choice3.setVisibility(View.VISIBLE);
				choice4.setVisibility(View.VISIBLE);
				choice5.setVisibility(View.INVISIBLE);
				choice6.setVisibility(View.INVISIBLE);
				choice7.setVisibility(View.INVISIBLE);
				break;
			case 2: //pattern size is 5
				liftPattern = new String[5];
				choice1.setVisibility(View.VISIBLE);
				choice2.setVisibility(View.VISIBLE);
				choice3.setVisibility(View.VISIBLE);
				choice4.setVisibility(View.VISIBLE);
				choice5.setVisibility(View.VISIBLE);
				choice6.setVisibility(View.INVISIBLE);
				choice7.setVisibility(View.INVISIBLE);
				break;
			case 3://patterm size is 6
				liftPattern = new String[6];
				choice1.setVisibility(View.VISIBLE);
				choice2.setVisibility(View.VISIBLE);
				choice3.setVisibility(View.VISIBLE);
				choice4.setVisibility(View.VISIBLE);
				choice5.setVisibility(View.VISIBLE);
				choice6.setVisibility(View.VISIBLE);
				choice7.setVisibility(View.INVISIBLE);
				break;
			case 4:// pattern size is 7
				liftPattern = new String[7];
				choice1.setVisibility(View.VISIBLE);
				choice2.setVisibility(View.VISIBLE);
				choice3.setVisibility(View.VISIBLE);
				choice4.setVisibility(View.VISIBLE);
				choice5.setVisibility(View.VISIBLE);
				choice6.setVisibility(View.VISIBLE);
				choice7.setVisibility(View.VISIBLE);
			}
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}};	
		

		
	private boolean validatePattern()
	{
		//traverse the array and make sure all four lifts are used. 
		int benchCount = 0, squatCount = 0, ohpCount = 0, deadCount = 0; 
		boolean liftValidity = false, toastThrown = false;
		for (int i=0; i < liftPattern.length; i++)
		{
			liftPattern[0] = choice1.getText().toString();
			liftPattern[1] = choice2.getText().toString();
			liftPattern[2] = choice3.getText().toString();
			liftPattern[3] = choice4.getText().toString();
			if (liftPattern.length > 4)
			liftPattern[4] = choice5.getText().toString();
			if (liftPattern.length > 5)
			liftPattern[5] = choice6.getText().toString();
			if (liftPattern.length > 6)
			liftPattern[6] = choice7.getText().toString();
			String currentLift = liftPattern[i];
			//check if null and break if so..
			if (currentLift.equals(null))
			{
				Toast.makeText(AdjustLiftPatternActivity.this, "You left spot " + (i + 1) + " open! Please use all spots or choose a smaller pattern size.", Toast.LENGTH_SHORT).show();	
				return false;
			}
			if ((currentLift.equals("First") || currentLift.equals("Second") || currentLift.equals("Third") || currentLift.equals("Fourth")
				|| currentLift.equals("Fifth") || currentLift.equals("Sixth") || currentLift.equals("Seventh")) && !toastThrown)
			{
			Toast.makeText(AdjustLiftPatternActivity.this, "You left a spot open! Please use all spots or choose a smaller pattern size.", Toast.LENGTH_LONG).show();	
			toastThrown = true;
			}
			switch (currentLift)
			{
			case "Bench":
				benchCount++;
				break;
			case "Squat":
				squatCount++;
				break;
			case "Deadlift":
				deadCount++;
				break;
			case "OHP":
				ohpCount++;
				break;
			}
		}//end loop
		
		liftValidity = (benchCount == 1) && (squatCount == 1) && (deadCount == 1) && (ohpCount  == 1) && !toastThrown;
		if (liftValidity)
			return true;
		else
			{
			boolean multipleLifts = false;
			boolean zeroLifts = false;;
			String addBuffer = "Please add the following days to your pattern: ";
			String multipleBuffer = "You have multiples of the following lifts: ";
			if ((benchCount == 0))
			{
				addBuffer = addBuffer + "Bench ";
				zeroLifts = true;
			}
			else if (benchCount > 1)
			{
				multipleBuffer = multipleBuffer + "Bench ";
				multipleLifts = true;
			}
			if ((squatCount == 0))
			{
				addBuffer = addBuffer + "Squat ";
				zeroLifts = true;
			}	
			else if (squatCount > 1)
			{
				multipleBuffer = multipleBuffer + "Squat ";
				multipleLifts = true;
			}	
			if ((deadCount == 0))
			{
				addBuffer = addBuffer + "Deadlift ";
				zeroLifts = true;
			}		
			else if (deadCount > 1)
			{
				multipleLifts = true;
				multipleBuffer = multipleBuffer + "Deadlift ";
			}	
			if ((ohpCount == 0))
			{
				addBuffer = addBuffer + "OHP ";
				zeroLifts = true;
			}
			else if (ohpCount > 1)
			{
				multipleLifts = true;
				multipleBuffer = multipleBuffer + "OHP";
			}
			if (zeroLifts)
			Toast.makeText(AdjustLiftPatternActivity.this, addBuffer, Toast.LENGTH_SHORT).show();

			if (multipleLifts)
				Toast.makeText(AdjustLiftPatternActivity.this, multipleBuffer, Toast.LENGTH_SHORT).show();
			
			return false;
		
			}
	
	}

}
