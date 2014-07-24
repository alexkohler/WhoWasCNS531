package com.example.whowascns;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//DateProcessor.java- processes dates passed to it in ThirdScreen-the main algorithm of this program
public class DateProcessor {
	//day classification definition (Proper call syntax- String myString = Lift.Bench.name())
	enum Lift {Bench, Squat, REST1, OHP, Deadlift, REST2};  


	//Frequency definitions 
	String freq5 = "5-5-5";
	String freq3 = "3-3-3";
	String freq1 = "5-3-1";


	//Constants
	Double FIVE_1 = .65;
	Double FIVE_2 = .75;
	Double FIVE_3 = .85;

	Double TRIPLE_1 = .7;
	Double TRIPLE_2 = .8;
	Double TRIPLE_3 = .9;

	Double SINGLE_1 = .75;
	Double SINGLE_2 = .85;
	Double SINGLE_3 = .95;




	//Starting definitions
	Date STARTING_DATE;
	static String STARTING_DATE_STRING;
	Double BENCH_TRAINING_MAX;
	Double SQUAT_TRAINING_MAX;
	Double OHP_TRAINING_MAX;
	Double DEAD_TRAINING_MAX;
	Boolean UNIT_MODE_LBS; //If true, then lbs are unit, otheriwse, kgs are unit. 
	Boolean ROUND_FLAG; //determine whether or not to round
	Double UNIT_CONVERSION_FACTOR = 2.20462;


	//for sake of output on title
	String STARTINGBENCH;
	String STARTINGSQUAT;
	String STARTINGOHP;
	String STARTINGDEAD;


	//Currency definitions 
	String CURRENT_LIFT = Lift.Bench.name(); //for now this is start
	String CURRENT_FREQUENCY = freq5;
	Integer CURRENT_CYCLE = 1;
	int liftTrack = 2;//because we want to progress from bench (if we stayed at one bench would happen twice at the incrementing of the lift) 
	int freqTrack = 2;//because we want to progress from fives (if we stayed at one freq would be fives twice when incrementing frequency)
	Double CURRENT_FIRST;
	Double CURRENT_SECOND;
	Double CURRENT_THIRD;
	String CURRENT_DATE_STRING;
	Calendar CURRENT_DATE_CAL = Calendar.getInstance();; //to be parsed and worked to maintain current date (instead of modifying starting_date_string like I was before)
	SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy", java.util.Locale.getDefault()); // date format only needs to be declared once. Is not and won't be changing (Unless users really want a changable date format...)



	//logic is funky but will stay for now
	public void setStartingDate (String myDate)
	{


		STARTING_DATE_STRING = myDate;

	}
	
	public String getDate ()
	{
		return CURRENT_DATE_STRING;
	}
	
	void setDate(String formattedDate) {
		CURRENT_DATE_STRING = formattedDate;
		
	}


	public void setStartingLifts(String startingBench, String startingSquat, String startingOHP, String startingDead)
	{
		//error handling may have to be held here, but ideally before even grouping with intent.
		BENCH_TRAINING_MAX = Double.valueOf(startingBench);
		SQUAT_TRAINING_MAX = Double.valueOf(startingSquat);
		OHP_TRAINING_MAX = Double.valueOf(startingOHP);
		DEAD_TRAINING_MAX = Double.valueOf(startingDead); 

		//for sake of getStartingXXX method (title output on ThirdScreen)

		STARTINGBENCH = startingBench;
		STARTINGSQUAT = startingSquat;
		STARTINGOHP = startingOHP;
		STARTINGDEAD = startingDead;




	}




	//getter definitions 

	public String getStartingDate (){

		return STARTING_DATE_STRING;
	}

	public Integer getCycle()
	{
		return CURRENT_CYCLE;
	}

	public void setCycle(Integer myCycle)
	{
		CURRENT_CYCLE = myCycle;
	}
	//String startDate, Integer currentCycle, String currentLift, String currentFreq, double first, double second, double third


	String getLiftType()
	{
		return CURRENT_LIFT;
	}

	String getFreq()
	{
		return CURRENT_FREQUENCY;
	}

	//need to implement a calculate5 function, calculate3 function, and calculate1 function 
	double getFirstLift()
	{
		if (ROUND_FLAG)//if there is rounding wanted
		{
			if (UNIT_MODE_LBS)//lbs
				CURRENT_FIRST =  round(CURRENT_FIRST, 5);//return first lift rounded to nearest 5lb
			if (!UNIT_MODE_LBS)
				CURRENT_FIRST = round(CURRENT_FIRST, 1);//return first lift rounded to nearest 1kg
		}	


		return CURRENT_FIRST;
	}

	double getSecondLift()
	{
		if (ROUND_FLAG)//if there is rounding wanted
		{
			if (UNIT_MODE_LBS)//lbs
				CURRENT_SECOND =  round(CURRENT_SECOND, 5);//return first lift rounded to nearest 5lb
			if (!UNIT_MODE_LBS)
				CURRENT_SECOND = round(CURRENT_SECOND, 1);//return first lift rounded to nearest 1kg
		}	


		return CURRENT_SECOND;
	}

	double getThirdLift()
	{
		if (ROUND_FLAG)//if there is rounding wanted
		{
			if (UNIT_MODE_LBS)//lbs
				CURRENT_THIRD =  round(CURRENT_THIRD, 5);//return first lift rounded to nearest 5lb
			if (!UNIT_MODE_LBS)
				CURRENT_THIRD = round(CURRENT_THIRD, 1);//return first lift rounded to nearest 1kg
		}	


		return CURRENT_THIRD;
	}

	double getBenchTM()
	{
		return BENCH_TRAINING_MAX;
	}

	double getSquatTM()
	{
		return SQUAT_TRAINING_MAX;
	}

	double getOHPTM()
	{
		return OHP_TRAINING_MAX;
	}

	double getDeadTM()
	{
		return DEAD_TRAINING_MAX;
	}

	public boolean getUnitMode()
	{
		return UNIT_MODE_LBS;
	}


	String getStartingBench()
	{
		return STARTINGBENCH;
	}

	String getStartingSquat()
	{
		return STARTINGSQUAT;
	}

	String getStartingOHP()
	{
		return STARTINGOHP;
	}

	String getStartingDead()
	{
		return STARTINGDEAD;
	}


	//Setter definitions

	@SuppressLint("DefaultLocale")
	public void setUnitMode(String unitMode)
	{
		if (unitMode.equals("Lbs"))
			UNIT_MODE_LBS = true;
		if (unitMode.equals("Kgs"))
			UNIT_MODE_LBS = false;
	}

	public void setRoundingFlag (boolean roundFlag)
	{
		ROUND_FLAG = roundFlag;
	}

	//calculation/misc definitions

	//turns our STARTING_DATE_STRING into a more workable calendar object that we can do date arithmetic om
	public void parseDateString() 
	{
		try {
			CURRENT_DATE_CAL.setTime(df.parse(STARTING_DATE_STRING));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	//day incrementing function
	public void incrementDay()
	{
		CURRENT_DATE_CAL.add(Calendar.DAY_OF_MONTH, 1);  // number of days to add

		Date myDate = CURRENT_DATE_CAL.getTime();
		String formattedDate = df.format(myDate);

		setDate(formattedDate);
	}



	//Lift (and cycle if needed) incrementing function
	//PERCENTAGE definitions
	public void incrementLift()
	{
		switch (liftTrack)
		{
		case 1:
			CURRENT_LIFT = Lift.Bench.name();
			liftTrack++;
			break;

		case 2:
			CURRENT_LIFT=Lift.Squat.name();
			liftTrack++;
			break;

		case 3:
			CURRENT_LIFT=Lift.REST1.name();
			liftTrack++;
			break;

		case 4:
			CURRENT_LIFT=Lift.OHP.name();
			liftTrack++;
			break;

		case 5:
			CURRENT_LIFT=Lift.Deadlift.name();
			liftTrack++;
			break;

		case 6:
			CURRENT_LIFT=Lift.REST2.name();
			liftTrack = 1;
			incrementFreq();
			break;

		default:
			CURRENT_LIFT="incrementLift ERROR:<";
		}
	}//end method incrementLift

	//Method to increment frequency-(only called within incrementLift)
	public void incrementFreq()
	{
		switch (freqTrack)
		{
		case 1:
			CURRENT_FREQUENCY = freq5;
			freqTrack++;
			break;

		case 2:
			CURRENT_FREQUENCY = freq3;
			freqTrack++;
			break;

		case 3:
			CURRENT_FREQUENCY = freq1;
			freqTrack = 1;
			break;

		default:	
			CURRENT_FREQUENCY = "incrementFreq ERROR:<";					

		}
	}//end method incrementfreq

	public void incrementCycleAndUpdateTMs()
	{
		CURRENT_CYCLE = CURRENT_CYCLE + 1;
		if (getUnitMode())
		{
			BENCH_TRAINING_MAX = BENCH_TRAINING_MAX + 5; //this WILL HAVE TO CHANGE FOR KG MODE 
			OHP_TRAINING_MAX = OHP_TRAINING_MAX + 5;
			SQUAT_TRAINING_MAX = SQUAT_TRAINING_MAX + 10;
			DEAD_TRAINING_MAX = DEAD_TRAINING_MAX + 10;
		}
		if (!getUnitMode())
			BENCH_TRAINING_MAX = BENCH_TRAINING_MAX + (5 / UNIT_CONVERSION_FACTOR); 
		OHP_TRAINING_MAX = OHP_TRAINING_MAX + (5 / UNIT_CONVERSION_FACTOR);
		SQUAT_TRAINING_MAX = SQUAT_TRAINING_MAX + (10 / UNIT_CONVERSION_FACTOR);
		DEAD_TRAINING_MAX = DEAD_TRAINING_MAX + (10 / UNIT_CONVERSION_FACTOR);	

	}


	//to be called after a regular increment (just go to next day)
	void increment()
	{
		incrementDay();
		incrementLift();
	}


	void incrementRest()
	{
		incrementDay();
		incrementDay();
		incrementLift();
		incrementLift();
	}


	//Calculation methods

	public void calculateFivesDay(Double myLift)
	{
		CURRENT_FIRST  = myLift * FIVE_1;
		CURRENT_SECOND = myLift * FIVE_2;
		CURRENT_THIRD  = myLift * FIVE_3; 

	}


	public void calculateTriplesDay(Double myLift)
	{
		CURRENT_FIRST  = myLift * TRIPLE_1;
		CURRENT_SECOND = myLift * TRIPLE_2;
		CURRENT_THIRD  = myLift * TRIPLE_3;

	}

	public void calculateSingleDay(Double myLift)
	{
		CURRENT_FIRST  = myLift * SINGLE_1;
		CURRENT_SECOND = myLift * SINGLE_2;
		CURRENT_THIRD  = myLift * SINGLE_3;
	}



	double round(double i, int v) //first argument is rounded, 
	{
		return (double) (Math.round(i/v) * v);
	}

}
