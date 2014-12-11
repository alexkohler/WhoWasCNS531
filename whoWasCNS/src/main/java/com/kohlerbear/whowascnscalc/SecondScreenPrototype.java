package com.kohlerbear.whowascnscalc;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class SecondScreenPrototype extends BaseActivity {


	TextView option1, option2, option3, option4, option5;
	
	TextView choice1, choice2, choice3, choice4, choice5, choice6, choice7;
    TextView[] choices = {choice1, choice2, choice3, choice4, choice5, choice6, choice7};
    
    TextView patternSizeTV;
	EditText choice1F, choice2F, choice3F, choice4F, choice5F, choice6F, choice7F;
	EditText[] choiceFields = {choice1F, choice2F, choice3F, choice4F, choice5F, choice6F, choice7F};
	
    Button customButton, saveButton;
	String [] liftPattern = new String[7]; //max size of 7 
	String [] defaultPattern = {"Bench", "Squat", "Rest", "OHP", "Deadlift", "Rest"};
	String[] emptyPattern = { "First", "Second", "Third", "Fourth", "Fifth", "Sixth"};
	
	EditText benchEditText;
	EditText squatEditText;
	EditText ohpEditText;
	EditText deadEditText;
	
	//TM widgets
	RadioGroup patternSegmentGroup; 
	RadioButton patternFourDaysRadioButton;
	RadioButton patternFiveDaysRadioButton;
	RadioButton patternSixDaysRadioButton;
	RadioButton patternSevenDaysRadioButton;
	
	RadioButton lbRadioButton;
	RadioButton kgRadioButton;
	
	
	//TM vars
	Boolean lbs = true;
	String unit_mode;
	String startingDate; 
	
	
	Tracker tracker = null;

	//nav drawer vars
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.AppBaseLight);
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_second_screen_prototype);
	    
		//Set up our navigation drawer
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load titles from strings.xml
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);// load icons from strings.xml

		set(navMenuTitles, navMenuIcons);
		navMenuIcons.recycle();
 
        getActionBar().setDisplayHomeAsUpEnabled(true); 
	    
	    
	    
	    //button declarations
		customButton = (Button) findViewById(R.id.adjustActivityResetButton);
		saveButton = (Button) findViewById(R.id.adjustActivitySaveButton);
		
		
		customButton.setOnClickListener(customListener);
		saveButton.setOnClickListener(saveListener);
		
		RadioGroup unitModeGroup = (RadioGroup) findViewById(R.id.poundKilogramSegmentedButtonGroup);
		lbRadioButton = (RadioButton) findViewById(R.id.lbSegmentedButton);
		kgRadioButton = (RadioButton) findViewById(R.id.kgSegmentedButton);
		unitModeGroup.setOnCheckedChangeListener(poundKilogramSegmentListener);
		
		
		patternSegmentGroup = (RadioGroup) findViewById(R.id.patternSegmentGroup);
		
		patternFourDaysRadioButton = (RadioButton) findViewById(R.id.patternButtonFourDays);
		patternFiveDaysRadioButton = (RadioButton) findViewById(R.id.patternButtonFiveDays);
		patternSixDaysRadioButton  = (RadioButton) findViewById(R.id.patternButtonSixDays);
		patternSevenDaysRadioButton = (RadioButton) findViewById(R.id.patternButtonSevenDays);
		patternSegmentGroup.setOnCheckedChangeListener(patternSizeSegmentListener);

        patternSizeTV  = (TextView) findViewById(R.id.PatternSizeTV);
        patternSizeTV.setVisibility(View.INVISIBLE);
		
		//set up intent stuff
		Intent previousIntent = getIntent();
		String origin = previousIntent.getStringExtra("origin");
		
		if (origin.equals("first"))
		{
			startingDate = previousIntent.getStringExtra("key");
		}
		
		
		
		
		//set up analytics tracking 
		tracker = GoogleAnalytics.getInstance(this).getTracker("UA-55018534-1");
		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME, "Pattern Screen");

		tracker.send(hitParameters);

		
	    
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
	    
	    choices = new TextView[]{choice1, choice2, choice3, choice4, choice5, choice6, choice7};

        //If there is an error on the choice, make sure the user is able to touch the choice and show the error.
        for (TextView choice : choices)
        {
            choice.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView choiceClicked = (TextView) findViewById(view.getId());
                    if (choiceClicked.getError() != null)
                        choiceClicked.requestFocus();
                }
            });

        }
	    
	    //fields views are attributed with 
	    choice1F = (EditText)findViewById(R.id.choice1Field);
	    choice2F = (EditText)findViewById(R.id.choice2Field);
	    choice3F = (EditText)findViewById(R.id.choice3Field);
	    choice4F = (EditText)findViewById(R.id.choice4Field);
	    choice5F = (EditText)findViewById(R.id.choice5Field);
	    choice6F = (EditText)findViewById(R.id.choice6Field);
	    choice7F = (EditText)findViewById(R.id.choice7Field);
	    
	    choiceFields = new EditText[]{choice1F, choice2F, choice3F, choice4F, choice5F, choice6F, choice7F};
	    

	    
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
	    

//	    inflatePatternButtons(origin);
		inflatePatternButtons(defaultPattern, false);//not custom, in oncreate the choices wil be 	    
		
		
		//touch interceptor to clear errors if user presses outside the error bubble
		FrameLayout touchInterceptor = (FrameLayout)findViewById(R.id.content_frame);
		touchInterceptor.setOnTouchListener(new OnTouchListener() {
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		    	v.performClick();//to stop warnings
		        if (event.getAction() == MotionEvent.ACTION_DOWN) {
		        	for (TextView choice : choices)
		        	{
		            if (choice.isFocused()) {
		                Rect outRect = new Rect();
		                choice.getGlobalVisibleRect(outRect);
		                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
		                    choice.clearFocus();
		                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE); 
		                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		                }
		            }
		        	}
		        }
		        return false;
		    }
		});
	}
	
	
	private void inflatePatternButtons(String[] pattern, boolean custom)
	{
	    liftPattern = pattern;
	    if (custom) {
            liftPattern = new String[]{"First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh"};
            patternSizeTV.setVisibility(View.VISIBLE);
        }
	    int patternIndex = 0;
	    while (patternIndex < liftPattern.length)
	    {
	    	choices[patternIndex].setText(liftPattern[patternIndex]);
	    	if (!custom) //if we aren't coming from a custom, bold the pattern and hide the fields for rest
	    	{
                patternSizeTV.setVisibility(View.INVISIBLE);//hide pattern size text view
	    		choices[patternIndex].setTypeface(Typeface.DEFAULT_BOLD);
	    		if (choices[patternIndex].getText().toString().intern().equals("Rest"))
	    			choiceFields[patternIndex].setVisibility(View.INVISIBLE);
	    	}
	    	else //otherwise if we are, hide all the fields and make sure we have no bold! (Because they are numerics)
	    	{
	    		choices[patternIndex].setTypeface(Typeface.DEFAULT);
	    		choices[patternIndex].setError(null);
	    		choiceFields[patternIndex].setVisibility(View.INVISIBLE);
	    	}
	    		
	    		
	    		
	    	patternIndex++;
	    }	    
	    if (patternIndex < choices.length)
	    {
		    while (patternIndex < choices.length)
		    {
		    	TextView choice = choices[patternIndex];
		    	choice.setVisibility(View.INVISIBLE);
		    	choiceFields[patternIndex].setVisibility(View.INVISIBLE);
		    	patternIndex++;
		    }
		    	
	    }
	}
	
		private OnCheckedChangeListener poundKilogramSegmentListener = new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				if (lbRadioButton.isChecked())
				{
					lbs = true;
				}


				if (kgRadioButton.isChecked())
					lbs = false;


			}};
			
		private OnCheckedChangeListener patternSizeSegmentListener = new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId)
				{
				//TODO do this with loops good christ are you stupid?
				case R.id.patternButtonFourDays: //pattern size is 4
					//choices
					liftPattern = new String[4];
					choice1.setVisibility(View.VISIBLE);
					choice2.setVisibility(View.VISIBLE);
					choice3.setVisibility(View.VISIBLE);
					choice4.setVisibility(View.VISIBLE);
					choice5.setVisibility(View.INVISIBLE);
					choice6.setVisibility(View.INVISIBLE);
					choice7.setVisibility(View.INVISIBLE);
					//fields
					//choice 1 field 
					if (!choice1.getText().toString().equals("Rest") && !choice1.getText().toString().equals("First"))
						choice1F.setVisibility(View.VISIBLE);
					else
						choice1F.setVisibility(View.INVISIBLE);
					
					//choice 2 field 
					if (!choice2.getText().toString().equals("Rest") && !choice2.getText().toString().equals("Second"))
						choice2F.setVisibility(View.VISIBLE);
					else
						choice2F.setVisibility(View.INVISIBLE);
					
					//choice 3 field 
					if (!choice3.getText().toString().equals("Rest") && !choice3.getText().toString().equals("Third"))
						choice3F.setVisibility(View.VISIBLE);
					else
						choice3F.setVisibility(View.INVISIBLE);
					
					//choice 4 field
					if (!choice4.getText().toString().equals("Rest") && !choice4.getText().toString().equals("Fourth"))
						choice4F.setVisibility(View.VISIBLE);
					else
						choice4F.setVisibility(View.INVISIBLE);
					
					choice5F.setVisibility(View.INVISIBLE);
					choice6F.setVisibility(View.INVISIBLE);
					choice7F.setVisibility(View.INVISIBLE);
					break;
				case R.id.patternButtonFiveDays: //pattern size is 5
					liftPattern = new String[5];
					//choices
					choice1.setVisibility(View.VISIBLE);
					choice2.setVisibility(View.VISIBLE);
					choice3.setVisibility(View.VISIBLE);
					choice4.setVisibility(View.VISIBLE);
					choice5.setVisibility(View.VISIBLE);
					choice6.setVisibility(View.INVISIBLE);
					choice7.setVisibility(View.INVISIBLE);
					//fields
					
					//choice 1 field 
					if (!choice1.getText().toString().equals("Rest") && !choice1.getText().toString().equals("First"))
						choice1F.setVisibility(View.VISIBLE);
					else
						choice1F.setVisibility(View.INVISIBLE);
					
					//choice 2 field 
					if (!choice2.getText().toString().equals("Rest") && !choice2.getText().toString().equals("Second"))
						choice2F.setVisibility(View.VISIBLE);
					else
						choice2F.setVisibility(View.INVISIBLE);
					
					//choice 3 field 
					if (!choice3.getText().toString().equals("Rest") && !choice3.getText().toString().equals("Third"))
						choice3F.setVisibility(View.VISIBLE);
					else
						choice3F.setVisibility(View.INVISIBLE);
					
					//choice 4 field
					if (!choice4.getText().toString().equals("Rest") && !choice4.getText().toString().equals("Fourth"))
						choice4F.setVisibility(View.VISIBLE);
					else
						choice4F.setVisibility(View.INVISIBLE);
					
					//choice 5 field 
					if (!choice5.getText().toString().equals("Rest") && !choice5.getText().toString().equals("Fifth"))
						choice5F.setVisibility(View.VISIBLE);
					else
						choice5F.setVisibility(View.INVISIBLE);
					
					
					choice6F.setVisibility(View.INVISIBLE);
					choice7F.setVisibility(View.INVISIBLE);
					break;
				case R.id.patternButtonSixDays://patterm size is 6
					liftPattern = new String[6];
					//choices
					choice1.setVisibility(View.VISIBLE);
					choice2.setVisibility(View.VISIBLE);
					choice3.setVisibility(View.VISIBLE);
					choice4.setVisibility(View.VISIBLE);
					choice5.setVisibility(View.VISIBLE);
					choice6.setVisibility(View.VISIBLE);
					choice7.setVisibility(View.INVISIBLE);
					//fields
					//choice 1 field 
					if (!choice1.getText().toString().equals("Rest") && !choice1.getText().toString().equals("First"))
						choice1F.setVisibility(View.VISIBLE);
					else
						choice1F.setVisibility(View.INVISIBLE);
					
					//choice 2 field 
					if (!choice2.getText().toString().equals("Rest") && !choice2.getText().toString().equals("Second"))
						choice2F.setVisibility(View.VISIBLE);
					else
						choice2F.setVisibility(View.INVISIBLE);
					
					//choice 3 field 
					if (!choice3.getText().toString().equals("Rest") && !choice3.getText().toString().equals("Third"))
						choice3F.setVisibility(View.VISIBLE);
					else
						choice3F.setVisibility(View.INVISIBLE);
					
					//choice 4 field
					if (!choice4.getText().toString().equals("Rest") && !choice4.getText().toString().equals("Fourth"))
						choice4F.setVisibility(View.VISIBLE);
					else
						choice4F.setVisibility(View.INVISIBLE);
					
					//choice 5 field 
					if (!choice5.getText().toString().equals("Rest") && !choice5.getText().toString().equals("Fifth"))
						choice5F.setVisibility(View.VISIBLE);
					else
						choice5F.setVisibility(View.INVISIBLE);
					
					//choice 6 field 
					if (!choice6.getText().toString().equals("Rest") && !choice6.getText().toString().equals("Sixth"))
						choice6F.setVisibility(View.VISIBLE);
					else
						choice6F.setVisibility(View.INVISIBLE);
					
					choice7F.setVisibility(View.INVISIBLE);
					break;
				case R.id.patternButtonSevenDays:// pattern size is 7
					liftPattern = new String[7];
					//choices
					choice1.setVisibility(View.VISIBLE);
					choice2.setVisibility(View.VISIBLE);
					choice3.setVisibility(View.VISIBLE);
					choice4.setVisibility(View.VISIBLE);
					choice5.setVisibility(View.VISIBLE);
					choice6.setVisibility(View.VISIBLE);
					choice7.setVisibility(View.VISIBLE);
					//fields
					//choice 1 field 
					if (!choice1.getText().toString().equals("Rest") && !choice1.getText().toString().equals("First"))
						choice1F.setVisibility(View.VISIBLE);
					else
						choice1F.setVisibility(View.INVISIBLE);
					
					//choice 2 field 
					if (!choice2.getText().toString().equals("Rest") && !choice2.getText().toString().equals("Second"))
						choice2F.setVisibility(View.VISIBLE);
					else
						choice2F.setVisibility(View.INVISIBLE);
					
					//choice 3 field 
					if (!choice3.getText().toString().equals("Rest") && !choice3.getText().toString().equals("Third"))
						choice3F.setVisibility(View.VISIBLE);
					else
						choice3F.setVisibility(View.INVISIBLE);
					
					//choice 4 field
					if (!choice4.getText().toString().equals("Rest") && !choice4.getText().toString().equals("Fourth"))
						choice4F.setVisibility(View.VISIBLE);
					else
						choice4F.setVisibility(View.INVISIBLE);
					
					//choice 5 field 
					if (!choice5.getText().toString().equals("Rest") && !choice5.getText().toString().equals("Fifth"))
						choice5F.setVisibility(View.VISIBLE);
					else
						choice5F.setVisibility(View.INVISIBLE);
					
					//choice 6 field 
					if (!choice6.getText().toString().equals("Rest") && !choice6.getText().toString().equals("Sixth"))
						choice6F.setVisibility(View.VISIBLE);
					else
						choice6F.setVisibility(View.INVISIBLE);
					
					//choice 7 field 
					if (!choice7.getText().toString().equals("Rest") && !choice7.getText().toString().equals("Seventh"))
						choice7F.setVisibility(View.VISIBLE);
					else
						choice7F.setVisibility(View.INVISIBLE);
				}
				
			}};	
			
				

	private final class ChoiceTouchListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent motionEvent) {
			view.performClick();
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
	}

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
		    	String droppedLiftName = dropped.getText().toString();
		    	String viewName = getResources().getResourceEntryName(dropTarget.getId());
		    	Integer viewSuffix = Integer.valueOf(viewName.substring(viewName.length() - 1));//eg _1, _2, etc..
		    	if (droppedLiftName.equals("Rest"))
		    		choiceFields[viewSuffix - 1].setVisibility(View.INVISIBLE);//arays are zero indexed
		    	else //make sure field is visible 
		    		choiceFields[viewSuffix - 1].setVisibility(View.VISIBLE);
		    	//regardless, clear the error on the field.
		    		choiceFields[viewSuffix - 1].setError(null);
		    	String dropArea = (String) dropTarget.getText();
		    	//update the text in the target view to reflect the data being dropped
		    	dropTarget.setText(dropped.getText());
		    	updateLiftArray(dropArea.toLowerCase(Locale.getDefault()), (String) dropped.getText());
		    	//make it bold to highlight the fact that an item has been dropped
		    	dropTarget.setTypeface(Typeface.DEFAULT_BOLD);
		    	dropTarget.setError(null);
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



	private OnClickListener customListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			inflatePatternButtons(emptyPattern, true);
			}};


	private OnClickListener saveListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			if (validatePattern())
			{
			forwardToThird();	
			}
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
				Toast.makeText(SecondScreenPrototype.this, "You left spot " + (i + 1) + " open! Please use all spots or choose a smaller pattern size.", Toast.LENGTH_SHORT).show();	
				return false;
			}
			if ((currentLift.equals("First") || currentLift.equals("Second") || currentLift.equals("Third") || currentLift.equals("Fourth")
				|| currentLift.equals("Fifth") || currentLift.equals("Sixth") || currentLift.equals("Seventh"))/* && !toastThrown*/)
			{
				//TODO there is a bug somewhere around here... showing an error on a hidden textview
//			Toast.makeText(REVAMPEDSecondScreenActivity.this, "You left a spot open! Please use all spots or choose a smaller pattern size.", Toast.LENGTH_LONG).show();	
			//experimental error handling
			choices[i].requestFocus();
			choices[i].setError("You left this spot empty!");
			toastThrown = true;
			}
			else
			{
				choices[i].setError(null);
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
		}
		
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
			Toast.makeText(SecondScreenPrototype.this, addBuffer, Toast.LENGTH_SHORT).show();

			if (multipleLifts)
				Toast.makeText(SecondScreenPrototype.this, multipleBuffer, Toast.LENGTH_SHORT).show();
			
			return false;
		
			}
	
	}
	
	private void forwardToThird()//back end to SAVE button, where ever that may end up laying. Still need to bring in 'lbs' boolean, 'unit_mode' boolean, make sure you grab all depenendencies
	{
		int buttonID = patternSegmentGroup.getCheckedRadioButtonId();
		View selectedRadioButton = patternSegmentGroup.findViewById(buttonID);
		int visibleFieldsNew = patternSegmentGroup.indexOfChild(selectedRadioButton) + 4; //add 4?
		
		for (int i = 0; i < visibleFieldsNew; i++)
		{
			String liftName = choices[i].getText().toString();
			switch (liftName)
			{
			case "Bench":
				benchEditText = choiceFields[i];
//				Toast.makeText(getApplicationContext(), "benchEditText associated with choiceField[" + i + "]", Toast.LENGTH_SHORT).show();
				break;
			case "Squat":
				squatEditText = choiceFields[i];
//				Toast.makeText(getApplicationContext(), "squatEditText associated with choiceField[" + i + "]", Toast.LENGTH_SHORT).show();
				break;
			case "OHP":
				ohpEditText = choiceFields[i];
//				Toast.makeText(getApplicationContext(), "ohpEditText associated with choiceField[" + i + "]", Toast.LENGTH_SHORT).show();
				break;
			case "Deadlift":
				deadEditText = choiceFields[i];
//				Toast.makeText(getApplicationContext(), "deadEditText associated with choiceField[" + i + "]", Toast.LENGTH_LONG).show();
				break;
			default://don't need to identify any fields for rest or numerical values 
				break;
			}
			
		}
		
		//plug our associated EditTexts back into old back end code from original second screen activity
		
		//if we throw errors, we will need the erraneous column value
		String errorBench = benchEditText.getText().toString();
		String errorSquat = squatEditText.getText().toString();
		String errorOHP = ohpEditText.getText().toString();
		String errorDead = deadEditText.getText().toString();
		String errorStream = "";
		//error name definitions (to make appends cleaner looking)
		handleErrors(errorBench, errorSquat, errorOHP, errorDead,
				errorStream);


	}

	
	private void handleErrors(String errorBench, String errorSquat,
			String errorOHP, String errorDead, String errorStream) {
		String emptyBenchString = "Please enter a starting bench number!";
		String thousandBenchString = errorBench + " lbs? Let's be real here. Enter your actual bench.";
		String thousandBenchStringKgs = errorBench + "kgs? Let's be real here. Enter your actual bench.";
		String zeroBenchString = "Please enter a bench greater than 0lbs!";

		String emptySquatString = "Please enter a starting squat number!";
		String thousandSquatString = errorSquat + " lbs? Let's be real here. Enter your actual squat.";
		String thousandSquatStringKgs = errorSquat + " kgs? Let's be real here. Enter your actual squat.";
		String zeroSquatString = "Please enter a squat greater than 0lbs!";

		String emptyOHPString = "Please enter a starting OHP number!";
		String thousandOHPString = errorOHP  + " lbs? Let's be real here. Enter your actual OHP.";
		String thousandOHPStringKgs = errorOHP + " kgs? Let's be real here. Enter your actual OHP.";
		String zeroOHPString = "Please enter a OHP greater than 0lbs!";

		String emptyDeadliftString = "Please enter a starting deadlift number!";
		String thousandDeadliftString = errorDead +  " lbs? Let's be real here. Enter your actual deadlift.";
		String thousandDeadliftStringKgs = errorDead + " kgs? Let's be real here. Enter your actual deadlift.";
		String zeroDeadliftString = "Please enter a deadlift greater than 0lbs!";

		//all error flags initially false...
		Boolean benchErrorFlag = false;
		Boolean squatErrorFlag = false;
		Boolean ohpErrorFlag   = false;
		Boolean  deadErrorFlag = false;
//		Boolean spinnerErrorFlag = false; //possibly deprecated if number of cycles gets removed from projection
		//first, get data from previous screen (starting date passed to second from first)
		Intent intent = getIntent();

		ConfigTool ct = new ConfigTool(SecondScreenPrototype.this);
		if (startingDate == null && !ct.dbEmpty())
			startingDate = ct.getStartingDateFromDatabase();

		intent = new Intent(SecondScreenPrototype.this, ThirdScreenPrototype.class);
		intent.putExtra("key2", startingDate);
		intent.putExtra("liftPattern", liftPattern);

		NumberFormat nf = NumberFormat.getInstance(); //get user's locale to make sure we parse correctly
		
		//second, get our starting lifts
		String bench = benchEditText.getText().toString();

		double benchDouble = 0;
		    try {
				benchDouble = nf.parse(bench).doubleValue();
			} catch (ParseException e) 
			{
				if (bench.matches("^[0-9]+(.[0-9]{1,3})?$"))
					sendTrackerException("ParseException", bench);
				
				else
					benchEditText.setError("Please check the format of your training max");
			}
		
		//Some android phones are capable of accessing their full keyboard, add error checking to ensure that no commas,

		//null error handling
		if (bench.equals("")){	
			benchEditText.setError(emptyBenchString);
			benchErrorFlag = true;
		}

		else{
			if ((benchDouble >= 1000 && lbs) || (benchDouble >= 500 && !lbs))
			{
				if (lbs)
					benchEditText.setError(thousandBenchString);
				if (!lbs)
					benchEditText.setError(thousandBenchStringKgs);
				benchErrorFlag = true;
				
			}
			if (benchDouble == 0) {
				benchEditText.setError(zeroBenchString);
				benchErrorFlag = true;
			}			
		}

		if (benchErrorFlag.equals(false))
			intent.putExtra("bench", bench);

		//end bench error handling 



		String squat = squatEditText.getText().toString();
		
		double squatDouble = 0;
	    try {
	    	squatDouble = nf.parse(squat).doubleValue();
		} catch (ParseException e) 
		{

			if (squat.matches("^[0-9]+(.[0-9]{1,3})?$")) //If a legitimate number was entered (regex for decimal, regardless of comma/decimal
				sendTrackerException("ParseException", squat);
			
			else
			squatEditText.setError("Please check the format of your training max.");
		}
		
		//null error handling
		if (squat.equals("")){	
			squatEditText.setError(emptySquatString);
			squatErrorFlag = true;
		}
		
		else{
			if ((squatDouble >= 1500 && lbs) || (squatDouble > 600 && !lbs))
			{
				if (lbs)
					squatEditText.setError(thousandSquatString);
				if (!lbs)
					squatEditText.setError(thousandSquatStringKgs);
				squatErrorFlag = true;
			}
			if (squatDouble == 0) {
				squatEditText.setError(zeroSquatString);
				squatErrorFlag = true;
			}			
		}

		if (squatErrorFlag.equals(false))
			intent.putExtra("squat", squat);

		//end squat error handling



		String OHP = ohpEditText.getText().toString();
		
		
		double ohpDouble = 0;
	    try {
	    	ohpDouble = nf.parse(OHP).doubleValue();
		} catch (ParseException e) 
		{
			if (OHP.matches("^[0-9]+(.[0-9]{1,3})?$"))
				sendTrackerException("ParseException", OHP);
			
			else
				ohpEditText.setError("Please check the format of your training max.");
		}
		
		
		
		//null error handling
		if (OHP.equals("")){	
			//technically don't need endline char first time...
			ohpEditText.setError(emptyOHPString);
			ohpErrorFlag = true;
		}

		else{
			if ((ohpDouble >= 1000 && lbs) || (ohpDouble >= 400 && !lbs) )
			{
				if (lbs)
					ohpEditText.setError(thousandOHPString);
				if (!lbs)
					ohpEditText.setError(thousandOHPStringKgs);
				ohpErrorFlag = true;
			}
			if (ohpDouble == 0) {
				ohpEditText.setError(zeroOHPString);
				ohpErrorFlag = true;
			}			
		}

		if (ohpErrorFlag.equals(false))
			intent.putExtra("OHP", OHP);
		//end OHP error handling


		String dead = deadEditText.getText().toString();
		
		double deadDouble = 0;
	    try {
	    	deadDouble = nf.parse(dead).doubleValue();
		} catch (ParseException e) 
		{
			if (dead.matches("^[0-9]+(.[0-9]{1,3})?$"))
			sendTrackerException("ParseException", dead);
			
			else
				deadEditText.setError("Please check the format of your training max.");
		}
		
		
		
		if (dead.equals("")){	
			//technically don't need endline char first time...
			deadEditText.setError(emptyDeadliftString);
			deadErrorFlag = true;
		}

		else{
			if ((deadDouble >= 1500 && lbs ) || (deadDouble >= 700 && !lbs) )
			{
				if (lbs)
				deadEditText.setError(thousandDeadliftString);
				if (!lbs)
				deadEditText.setError(thousandDeadliftStringKgs);
				deadErrorFlag = true;
			}
			if (deadDouble == 0) {
				deadEditText.setError(zeroDeadliftString);
				deadErrorFlag = true;
			}			
		}
		if (deadErrorFlag.equals(false))
			intent.putExtra("dead", dead);

		//to accomodate new 5 cycle projection
		intent.putExtra("numberCycles", "5"); 

		if (!benchErrorFlag && !squatErrorFlag && !ohpErrorFlag && !deadErrorFlag )
		{


			if (lbs.equals(true)){
				unit_mode = "Lbs";	  
				Toast.makeText(SecondScreenPrototype.this, "Displaying in lbs", Toast.LENGTH_SHORT).show();
				intent.putExtra("mode", unit_mode);
			}
			if (lbs.equals(false))
			{
				unit_mode = "Kgs";
				Toast.makeText(SecondScreenPrototype.this, "Displaying in kgs", Toast.LENGTH_SHORT).show();
				intent.putExtra("mode", unit_mode);
			}

			
			intent.putExtra("origin", "second");

			startActivity(intent);

		}
	}

}
