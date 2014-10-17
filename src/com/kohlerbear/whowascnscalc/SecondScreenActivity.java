package com.kohlerbear.whowascnscalc;


import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

public class SecondScreenActivity extends Activity {

	EditText benchEditText;
	EditText squatEditText;
	EditText ohpEditText;
	EditText deadEditText;
	Spinner numberCyclesSpinner;
	RadioButton lbRadioButton;
	RadioButton kgRadioButton;
	CheckBox roundingCheckBox;

	//to display errors 
	TextView errorTextView;
	Boolean lbs = true;
	String unit_mode;

	String restoredBench;
	String restoredSquat;
	String restoredOHP;
	String restoredDead;
	
	String[] liftPattern = new String[7];

	Tracker tracker = null;
	@Override
	public void onCreate(Bundle savedInstanceState){
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		//declare our button, tie it to listener, code listener

		Button backButton = (Button) findViewById(R.id.secondToFirstButton);
		backButton.setOnClickListener(goToFirstListener);

		Button createScheduleButton = (Button) findViewById(R.id.secondScreenNextButton);
		createScheduleButton.setOnClickListener(goToThirdListener);

		errorTextView = (TextView) findViewById(R.id.dynamicTextView);
		errorTextView.setText("");

		benchEditText = (EditText) findViewById(R.id.benchEditText);
		squatEditText = (EditText) findViewById(R.id.squatEditText);
		ohpEditText   = (EditText) findViewById(R.id.ohpEditText);;
		deadEditText  = (EditText) findViewById(R.id.deadEditText);

		numberCyclesSpinner = (Spinner) findViewById(R.id.numberCyclesSpinner);
		numberCyclesSpinner.setOnItemSelectedListener(numberCyclesSpinnerListener);

		lbRadioButton = (RadioButton) findViewById(R.id.lbRadioButton);
		kgRadioButton = (RadioButton) findViewById(R.id.kgRadioButton);
		
		tracker = GoogleAnalytics.getInstance(this).getTracker("UA-55018534-1");
		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME, "Second Screen");

		tracker.send(hitParameters);


		RadioGroup unitModeGroup = (RadioGroup) findViewById(R.id.unitModeGroup);
		unitModeGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				if (lbRadioButton.isChecked())
				{
					lbs = true;
				}


				if (kgRadioButton.isChecked())
					lbs = false;


			}}
				);


		roundingCheckBox = (CheckBox) findViewById(R.id.roundingCheckBox);	

		
		
		Intent thirdToSecondIntent = getIntent();
		String origin = thirdToSecondIntent.getStringExtra("origin");
		if (origin.equals("third"))
		{
			benchEditText.setText(thirdToSecondIntent.getStringExtra("bench"));
			squatEditText.setText(thirdToSecondIntent.getStringExtra("squat"));
			ohpEditText.setText(thirdToSecondIntent.getStringExtra("ohp"));
			deadEditText.setText(thirdToSecondIntent.getStringExtra("dead"));
			liftPattern = thirdToSecondIntent.getStringArrayExtra("liftPattern");
			
		}
		
		if (origin.equals("first"))
		{
			liftPattern = thirdToSecondIntent.getStringArrayExtra("liftPattern");
		}



	}




	private OnClickListener goToFirstListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			backToFirst();

		}};

		private void backToFirst()
		{
			startActivity(new Intent(this, MainActivity.class));
		}

		private OnClickListener goToThirdListener = new OnClickListener(){

			@Override
			public void onClick(View v) {

				forwardToThird();

			}};

			private OnItemSelectedListener numberCyclesSpinnerListener = new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					return;

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					return;

				}};	




				//error handling: eventually want to put this output on screen.. not in toast.		
				private void forwardToThird()
				{
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

					String zeroCycleString = "Please choose how many cycles you wish to project!";



					//all error flags initially false...
					Boolean benchErrorFlag = false;
					Boolean squatErrorFlag = false;
					Boolean ohpErrorFlag   = false;
					Boolean  deadErrorFlag = false;
					Boolean spinnerErrorFlag = false;
					//first, get data from previous screen (starting date passed to second from first)
					Intent intent = getIntent();

					String message = intent.getStringExtra("key");

					intent = new Intent(SecondScreenActivity.this, ThirdScreenActivity.class);
					intent.putExtra("key2", message);
					intent.putExtra("liftPattern", liftPattern);

					NumberFormat nf = NumberFormat.getInstance(); //get user's locale to make sure we parse correctly
					
					//second, get our starting lifts
					String bench = benchEditText.getText().toString();

				//	Number myNumber = nf.parse(myString);

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
						//technically don't need endline char first time...
						benchEditText.setError(emptyBenchString);
						errorTextView.setText(errorStream);
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

					//Spinner error handling
					if (numberCyclesSpinner.getSelectedItem().toString().equals("0")) 
					{
						errorStream = errorStream + "\n" + zeroCycleString;
						errorTextView.setText(errorStream);
						spinnerErrorFlag = true;
					}

					else
					{
						intent.putExtra("numberCycles", numberCyclesSpinner.getSelectedItem().toString());
					}

					if (!benchErrorFlag && !squatErrorFlag && !ohpErrorFlag && !deadErrorFlag && !spinnerErrorFlag)
					{


						if (lbs.equals(true)){
							unit_mode = "Lbs";	  
							Toast.makeText(SecondScreenActivity.this, "Displaying in lbs", Toast.LENGTH_SHORT).show();
							intent.putExtra("mode", unit_mode);
						}
						if (lbs.equals(false))
						{
							unit_mode = "Kgs";
							Toast.makeText(SecondScreenActivity.this, "Displaying in kgs", Toast.LENGTH_SHORT).show();
							intent.putExtra("mode", unit_mode);
						}

						if (roundingCheckBox.isChecked())
							intent.putExtra("round", "true");
						else
							intent.putExtra("round", "false");

						//tell third activity we are coming from second screen (creating a new query)
						intent.putExtra("origin", "second");

						startActivity(intent);

					}
				}




				private void sendTrackerException(String exceptionType, String value) {
					Toast.makeText(SecondScreenActivity.this, "Sorry! :( Something went wrong, crash report sent.", Toast.LENGTH_LONG).show();
					  tracker.send(MapBuilder
						      .createEvent("Exception",     // Event category (required)
						                   exceptionType,  // Event action (required)
						                   value,   // Event label
						                   null)            // Event value
						      .build());
					
				}

}






