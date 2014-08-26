package com.kohlerbear.whowascnscalc;

public class KilogramPlateComputer {

	boolean lb_mode = true; 

	//weight entered
	double weight;

	//weight not including bar 
	double plateWeight; 


	//weight remaining (for sanity checks and more readable calculation

	int twentyFivesNeeded;
	int twentysNeeded;
	int fifteensNeeded;
	int tensNeeded;
	int fivesNeeded;
	int twopointfivesNeeded;
	int onepointtwofivesNeeded;
	int currentPlate_needed;
	String currentPlateName;


	public void computeKgPlates (double myWeight, double barbellUsed, boolean[] kgFlags)
	{
		//should unpack booleans....
		weight = myWeight - barbellUsed;
		plateWeight = round (weight, 2.5); //this will have to be dynamic depending on the plates they have 
		//plateWeight = weight;
		Boolean haveTwentyFive = kgFlags[0];
		Boolean haveTwenty = kgFlags[1];
		Boolean haveFifteen = kgFlags[2];
		Boolean haveTen = kgFlags[3];
		Boolean haveFive = kgFlags[4];
		Boolean haveTwoPointFive = kgFlags[5];
		Boolean haveOnePointTwoFive = kgFlags[6];

		double currentPlate = 0;
		//boolean thirtyfive_flag = true;
		for (int i=0; i<7; i++)
		{
			currentPlate_needed = 0;
			switch (i){ 
			case 0:
				if(!haveTwentyFive) //could add a flag for each of these to allow user to specify what plates they have 
				{
					currentPlate = 0;
					break;
				}
				if (haveTwentyFive)
				{
					currentPlate = 25;
					break;
				}
			case 1:
				if(!haveTwenty) //could add a flag for each of these to allow user to specify what plates they have 
				{
					currentPlate = 0;
					break;
				}
				if (haveTwenty)
				{
					currentPlate = 20;
					break;
				}
			case 2:
				if (!haveFifteen)
				{
					currentPlate = 0;
					break;
				}
				if (haveFifteen)
				{
					currentPlate = 15;
					break;
				}
			case 3:
				if (!haveTen)
				{
					currentPlate = 0;
					break;
				}
				if (haveTen)
				{
					currentPlate = 10;
					break;
				}
			case 4: 
				if (!haveFive)
				{
					currentPlate = 0;
					break;
				}
				if (haveFive)
				{
				currentPlate = 5;
				break;
				}
			case 5:
				if (!haveTwoPointFive)
				{
					currentPlate = 0;
					break;
				}
				if (haveTwoPointFive)
				{
					currentPlate = 2.5;
					break;					
				}
			case 6:
				if (!haveOnePointTwoFive)
				{
					currentPlate = 0;
					break;
				}
				if (haveOnePointTwoFive)
				{
					currentPlate = 1.25;
					break;
				}
			}//end switch

			if (currentPlate > 0) //weed out unneeded 35
			{
				currentPlate_needed = (int) (plateWeight / currentPlate);
				if ((currentPlate_needed % 2)!=0)//if there is an uneven number of plates
					currentPlate_needed--; //decrement and make it even
				plateWeight = plateWeight - (currentPlate_needed * currentPlate); //calculate new plateweight
				if (currentPlate_needed > 0)//weed out plates that aren't needed
				{
					currentPlateName = String.valueOf(currentPlate).intern();
					switch (currentPlateName)
					{
					case "25.0":
						setTwentyFivesNeeded(currentPlate_needed);
						break;
					case "20.0":
						setTwentysNeeded(currentPlate_needed);
						break;
					case "15.0":
						setFifteensNeeded(currentPlate_needed);
						break;
					case "10.0":
						setTensNeeded(currentPlate_needed);
						break;
					case "5.0":
						setFivesNeeded(currentPlate_needed); //what's needed per side
						break;
					case "2.5":
						setTwopointfivesNeeded(currentPlate_needed); //what's needed per side
						break;	
					case "1.25":
						setOnepointtwofivesNeeded(currentPlate_needed);
						break;

						

					}//end switch


				}
			}


		}


	}//end method calculate	





	double getWeight() {
		return weight;
	}

	

//One way is to multiply your number by a number that will allow rounding by Math.round(...) to do the correct rounding, and then
	//divide by that number before using String.format(...) or 
	//DecimalFormat to produce a nice String representation. 
	//You could multiply the number by 0.4 or divide it by 2.5,
	//then round it to the nearest int, then divide the result by 0.4 or multiply it by 2.5 to get the result,
	//but then use a String formatting tool to round this to 1 decimal place. String.format("%.1f", myNumber) could work well for this. 
	double round(double i,double v) //first argument is rounded, second argument is what to round to: (5 for lbs)... for kg just figure out a way to get close.. lol
	{ 
		return (double) (Math.round(i/v) * v);
	}


	public int getTwentyFivesNeeded() {
		return twentyFivesNeeded;
	}





	public void setTwentyFivesNeeded(int twentyFivesNeeded) {
		this.twentyFivesNeeded = twentyFivesNeeded;
	}





	public int getTwentysNeeded() {
		return twentysNeeded;
	}





	public void setTwentysNeeded(int twentysNeeded) {
		this.twentysNeeded = twentysNeeded;
	}





	public int getFifteensNeeded() {
		return fifteensNeeded;
	}





	public void setFifteensNeeded(int fifteensNeeded) {
		this.fifteensNeeded = fifteensNeeded;
	}





	public int getTensNeeded() {
		return tensNeeded;
	}





	public void setTensNeeded(int tensNeeded) {
		this.tensNeeded = tensNeeded;
	}





	public int getFivesNeeded() {
		return fivesNeeded;
	}





	public void setFivesNeeded(int fivesNeeded) {
		this.fivesNeeded = fivesNeeded;
	}





	public int getTwopointfivesNeeded() {
		return twopointfivesNeeded;
	}





	public void setTwopointfivesNeeded(int twopointfivesNeeded) {
		this.twopointfivesNeeded = twopointfivesNeeded;
	}



	public int getOnepointtwofivesNeeded() {
		return onepointtwofivesNeeded;
	}





	public void setOnepointtwofivesNeeded(int onepointtwofivesNeeded) {
		this.onepointtwofivesNeeded = onepointtwofivesNeeded;
	}

	
	



}
