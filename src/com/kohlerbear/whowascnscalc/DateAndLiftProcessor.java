package com.kohlerbear.whowascnscalc;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//DateProcessor.java- processes dates passed to it in ThirdScreen-the main algorithm of this program
public class DateAndLiftProcessor {
	//day classification definition (Proper call syntax- String myString = Lift.Bench.name())
	enum Lift {Bench, Squat, OHP, Deadlift, REST};  
	

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
	static String CURRENT_LIFT = Lift.Bench.name(); //for now this is start
	String CURRENT_FREQUENCY = freq5;
	Integer CURRENT_CYCLE = 1;
	int liftTrack = 0;//because we want to progress from bench (if we stayed at one bench would happen twice at the incrementing of the lift) 
	int freqTrack = 2;//because we want to progress from fives (if we stayed at one freq would be fives twice when incrementing frequency)
	Double CURRENT_FIRST;
	Double CURRENT_SECOND;
	Double CURRENT_THIRD;
	String CURRENT_DATE_STRING;
	Calendar CURRENT_DATE_CAL = Calendar.getInstance();; //to be parsed and worked to maintain current date (instead of modifying starting_date_string like I was before)
	SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy", java.util.Locale.getDefault()); // date format only needs to be declared once. Is not and won't be changing (Unless users really want a changable date format...)


	static int patternSize; //size of user specified pattern
	//logic is funky but will stay for now
	
	String PATTERN_ACRONYM;
	
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
		DEAD_TRAINING_MAX = Double.valueOf(startingDead);;

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
//		if (ROUND_FLAG)//if there is rounding wanted
//		{
//			if (UNIT_MODE_LBS)//lbs
//				CURRENT_FIRST =  round(CURRENT_FIRST, 5);//return first lift rounded to nearest 5lb
//			if (!UNIT_MODE_LBS)
//				CURRENT_FIRST = roundkg(CURRENT_FIRST, 2.5);//return first lift rounded to nearest 1kg
//		}	


		return CURRENT_FIRST;
	}

	double getSecondLift()
	{
//		if (ROUND_FLAG)//if there is rounding wanted
//		{
//			if (UNIT_MODE_LBS)//lbs
//				CURRENT_SECOND =  round(CURRENT_SECOND, 5);//return first lift rounded to nearest 5lb
//			if (!UNIT_MODE_LBS)
//				CURRENT_SECOND = roundkg(CURRENT_SECOND, 2.5);//return first lift rounded to nearest 1kg
//		}	


		return CURRENT_SECOND;
	}

	double getThirdLift()
	{
//		if (ROUND_FLAG)//if there is rounding wanted
//		{
//			if (UNIT_MODE_LBS)//lbs
//				CURRENT_THIRD =  round(CURRENT_THIRD, 5);//return first lift rounded to nearest 5lb
//			if (!UNIT_MODE_LBS)
//				CURRENT_THIRD = roundkg(CURRENT_THIRD, 2.5);//return first lift rounded to nearest 1kg
//		}	


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
	
	public boolean getRoundingFlag ()
	{
		return ROUND_FLAG;
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
	public void incrementLift(String[] myPattern, String currentLift)
	{
		//NAMES ASSIGNED ARE BASED ON ENUM::
		
		//#####****
		//enum Lift {Bench, Squat, OHP, Deadlift, REST};  
		//liftTrack will have to be
		//need separate variable (static? like lifttrack that holds patternSize
		if (liftTrack + 1 < patternSize)
		{
		liftTrack++;
		CURRENT_LIFT = myPattern[liftTrack];//assign currentLift to be the next one in the patten
		//if it's a rest day that is taken care of in getCurrentTM by returning 0 which will be rooted out on insertion (onlys tms > 0)
		}
		else if ( (liftTrack + 1) == patternSize)
			{
					liftTrack = 0;//reset our liftTrack
					incrementFreq();
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
		{
		BENCH_TRAINING_MAX = BENCH_TRAINING_MAX + (5 / UNIT_CONVERSION_FACTOR); 
		OHP_TRAINING_MAX = OHP_TRAINING_MAX + (5 / UNIT_CONVERSION_FACTOR);
		SQUAT_TRAINING_MAX = SQUAT_TRAINING_MAX + (10 / UNIT_CONVERSION_FACTOR);
		DEAD_TRAINING_MAX = DEAD_TRAINING_MAX + (10 / UNIT_CONVERSION_FACTOR);	
		}

	}


	//to be called after a regular increment (just go to next day)


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
	
	public void setPatternAcronym(String[] pattern)
	{
		PATTERN_ACRONYM = "";
		for (String day : pattern)
		{
			PATTERN_ACRONYM = PATTERN_ACRONYM + day.substring(0, 1).toUpperCase(Locale.US); //For internal use on a switch statement, no need to use user's locale
		}
		
	}
	
	
	public String getPatternAcronym()
	{
		return PATTERN_ACRONYM;
	}



	double roundkg(double i, double v) //first argument is rounded, 
	{
		return (double) (Math.round(i/v) * v);
	}

	double round(double i, int v) //first argument is rounded, 
	{
		return (double) (Math.round(i/v) * v);
	}
	
	public void initializePatternSize(int length) {
		patternSize = length;
		
	}

	
	
	//enum Lift {Bench, Squat, OHP, Deadlift, REST};  
	public void setCurrentLift(String lift) {
		if (lift.equals("Bench") || lift.equals("Squat") || lift.equals("OHP") || lift.equals("Deadlift") || lift.equals("Rest"))
		CURRENT_LIFT = lift;
		else
			CURRENT_LIFT = "ERROR";
		
	}

	public double getCurrentTM() {
	
		switch (CURRENT_LIFT)	
		{
		case "Bench":
			return getBenchTM();
		case "Squat":
			return getSquatTM();
		case "OHP":
			return getOHPTM();
		case "Deadlift":
			return getDeadTM();
		case "Rest":
			return 0;
		default://if something falls to here something is screwy, so let's return something screwy because I am a young coder who doesn't know how to be robust
			return 999;	
		}
		
	}

	void calculateCycle(ThirdScreenActivity thirdScreen, String[] myPattern)
	{
		//(max pattern of 7 days), 
		//String[] myPattern = {"Squat", "Rest", "Bench", "Deadlift", "Rest", "OHP"  }; //be sure to use default naming patterns (like you've used in rest of program) 
		//lets give the patern it's been dealing with since the start, however, now it's hopefully in a generalized algorithm
		//String[] myPattern = {"Bench", "Squat", "Rest", "OHP", "Deadlift", "Rest"};
		//String[] myPattern = {"Squat", "Bench", "Rest", "Deadlift", "OHP", "Rest" };
		
		initializePatternSize(myPattern.length);//separate variable from liftTrack
		setCycle(1);
		for (int i=0; i < thirdScreen.getNumberCycles(); i++) 
		{
			for (int j=0; j < myPattern.length; j++){
			//create setCurrentLift function that sets current lift based on an enum
			setCurrentLift(myPattern[j]);//fixed names so that we can use an enum based on a switch statement
			//calculate fives day will have to be revamped - 
				if (getCurrentTM() > 0 ){//set getCurrentTM will access the variable that setCurrentLift uses. (will be set to zero for rest day{
					calculateFivesDay(getCurrentTM());
					thirdScreen.eventsData.addEvent(thirdScreen);
				}
			incrementDay();//for sake of being less cryptic i am separating increment because it was too small. if only i could fix my functions that are too big.
			incrementLift(myPattern, myPattern[j]);//no matter what the day, we still need to incrementCycleAndUpdateTMs
			}
			
			
			for (int j=0; j < myPattern.length; j++){
			//create setCurrentLift function that sets current lift based on an enum
			setCurrentLift(myPattern[j]);//fixed names so that we can use an enum based on a switch statement
			//calculate fives day will have to be revamped - 
				if (getCurrentTM()> 0 ){//set getCurrentTM will access the variable that setCurrentLift uses. (will be set to zero for rest day{
					calculateTriplesDay(getCurrentTM());
					thirdScreen.eventsData.addEvent(thirdScreen);
				}
			incrementDay();//no matter what the day, we still need to incrementCycleAndUpdateTMs
			incrementLift(myPattern, myPattern[j]);
			}
			
			for (int j=0; j < myPattern.length; j++){
			//create setCurrentLift function that sets current lift based on an enum
			setCurrentLift(myPattern[j]);//fixed names so that we can use an enum based on a switch statement
			//calculate fives day will have to be revamped - 
				if (getCurrentTM()> 0 ){//set getCurrentTM will access the variable that setCurrentLift uses. (will be set to zero for rest day{
					calculateSingleDay(getCurrentTM());
					thirdScreen.eventsData.addEvent(thirdScreen);
				}
				incrementDay();//no matter what the day, we still need to incrementCycleAndUpdateTMs
				incrementLift(myPattern, myPattern[j]);
			}
			
			incrementCycleAndUpdateTMs();//still needs to be within loop
			}
	
			
		}

}

