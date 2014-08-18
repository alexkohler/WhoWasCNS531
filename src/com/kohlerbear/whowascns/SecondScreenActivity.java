package com.kohlerbear.whowascns;

import com.kohlerbear.whowascns.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		//declare our button, tie it to listener, code listener

		Button backButton = (Button) findViewById(R.id.secondToFirstButton);
		backButton.setOnClickListener(goToFirstListener);

		Button createScheduleButton = (Button) findViewById(R.id.secondScreenNextButton);
		createScheduleButton.setOnClickListener(goToThirdListener); //because fuck inline listeners 

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


		RadioGroup unitModeGroup = (RadioGroup) findViewById(R.id.unitModeGroup);
		unitModeGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				if (lbRadioButton.isChecked())
					lbs = true;


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
					String thousandBenchString = errorBench + " lbs? Lettuce be reality here. Enter your actual bench.";
					String thousandBenchStringKgs = errorBench + "kgs? Lettuce be reality here. Enter your actual bench.";
					String zeroBenchString = "Please enter a bench greater than 0lbs!";

					String emptySquatString = "Please enter a starting squat number!";
					String thousandSquatString = errorSquat + " lbs? Lettuce be actual reality here. Enter your actual squat.";
					String thousandSquatStringKgs = errorSquat + " kgs? Lettuce be actual reality here. Enter your actual squat.";
					String zeroSquatString = "Please enter a squat greater than 0lbs!";

					String emptyOHPString = "Please enter a starting OHP number!";
					String thousandOHPString = errorOHP  + " lbs? Lettuce be actual reality here. Enter your actual OHP.";
					String thousandOHPStringKgs = errorOHP + " kgs? Lettuce be actual reality here. Enter your actual OHP.";
					String zeroOHPString = "Please enter a OHP greater than 0lbs!";

					String emptyDeadliftString = "Please enter a starting deadlift number!";
					String thousandDeadliftString = errorDead +  " lbs? Lettuce be actual reality here. Enter your actual deadlift.";
					String thousandDeadliftStringKgs = errorDead + " kgs? Lettuce be actual reality here. Enter your actual deadlift.";
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

					//second, get our starting lifts
					String bench = benchEditText.getText().toString();

					//null error handling
					if (bench.equals("")){	
						//technically don't need endline char first time...
						errorStream = errorStream + "\n" + emptyBenchString;
						errorTextView.setText(errorStream);
						benchErrorFlag = true;
					}

					else{
						if ((Double.parseDouble(bench) >= 1000 && lbs) || (Double.parseDouble(bench) >= 500 && !lbs))
						{
							if (lbs)
							errorStream = errorStream + "\n" + thousandBenchString;
							if (!lbs)
							errorStream = errorStream + "\n" + thousandBenchStringKgs;
							errorTextView.setText(errorStream);
							benchErrorFlag = true;
						}
						if (Double.parseDouble(bench) == 0) {
							errorStream = errorStream + "\n" + zeroBenchString;
							errorTextView.setText(errorStream);
							benchErrorFlag = true;
						}			
					}

					if (benchErrorFlag.equals(false))
						intent.putExtra("bench", bench);

					//end bench error handling 



					String squat = squatEditText.getText().toString();
					//null error handling
					if (squat.equals("")){	
						//technically don't need endline char first time...
						errorStream = errorStream + "\n" + emptySquatString;
						errorTextView.setText(errorStream);
						squatErrorFlag = true;
					}

					else{
						if ((Double.parseDouble(squat) >= 1500 && lbs) || (Double.parseDouble(squat) > 600 && !lbs))
						{
							if (lbs)
							errorStream = errorStream + "\n" + thousandSquatString;
							if (!lbs)
							errorStream = errorStream + "\n" + thousandSquatStringKgs;
							errorTextView.setText(errorStream);
							squatErrorFlag = true;
						}
						if (Double.parseDouble(squat) == 0) {
							errorStream = errorStream + "\n" + zeroSquatString;
							errorTextView.setText(errorStream);
							squatErrorFlag = true;
						}			
					}

					if (squatErrorFlag.equals(false))
						intent.putExtra("squat", squat);

					//end squat error handling



					String OHP = ohpEditText.getText().toString();
					//null error handling
					if (OHP.equals("")){	
						//technically don't need endline char first time...
						errorStream = errorStream + "\n" + emptyOHPString;
						errorTextView.setText(errorStream);
						ohpErrorFlag = true;
					}

					else{
						if ((Double.parseDouble(OHP) >= 1000 && lbs) || (Double.parseDouble(OHP) >= 400 && !lbs) )
						{
							if (lbs)
							errorStream = errorStream + "\n" + thousandOHPString;
							if (!lbs)
							errorStream = errorStream + "\n" + thousandOHPStringKgs;
							errorTextView.setText(errorStream);
							ohpErrorFlag = true;
						}
						if (Double.parseDouble(OHP) == 0) {
							errorStream = errorStream + "\n" + zeroOHPString;
							errorTextView.setText(errorStream);
							ohpErrorFlag = true;
						}			
					}

					if (ohpErrorFlag.equals(false))
						intent.putExtra("OHP", OHP);
					//end OHP error handling


					String dead = deadEditText.getText().toString();
					if (dead.equals("")){	
						//technically don't need endline char first time...
						errorStream = errorStream + "\n" + emptyDeadliftString;
						errorTextView.setText(errorStream);
						deadErrorFlag = true;
					}

					else{
						if ((Double.parseDouble(dead) >= 1500 && lbs ) || (Double.parseDouble(dead) >= 700 && !lbs) )
						{
							if (lbs)
							errorStream = errorStream + "\n" + thousandDeadliftString;
							if (!lbs)
							errorStream = errorStream + "\n" + thousandDeadliftStringKgs;
							errorTextView.setText(errorStream);
							deadErrorFlag = true;
						}
						if (Double.parseDouble(dead) == 0) {
							errorStream = errorStream + "\n" + zeroDeadliftString;
							errorTextView.setText(errorStream);
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

}






