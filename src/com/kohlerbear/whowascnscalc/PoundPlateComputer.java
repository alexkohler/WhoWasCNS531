package com.kohlerbear.whowascnscalc;

public class PoundPlateComputer {

	boolean lb_mode = true; 

	//weight entered
	double weight;

	//weight not including bar 
	double plateWeight; 


	//weight remaining (for sanity checks and more readable calculation

	int fortyfivesNeeded;
	int thirtyfivesNeeded;
	int twentyfivesNeeded;
	int tensNeeded;
	int fivesNeeded;
	int twopointfivesNeeded;

	int currentPlate_needed;
	String currentPlateName;


	public void computeLbPlates (double myWeight, double barbellUsed, boolean lbFlags[])
	{
		weight = myWeight - barbellUsed;
		plateWeight = round (weight, 5);
        Boolean lbHaveFortyFive = null, lbHaveThirtyFive = null, lbHaveTwentyFive = null, lbHaveTen = null, lbHaveFive = null , lbHaveTwoPointFive= null;
        lbHaveFortyFive = lbFlags[0];
        lbHaveThirtyFive = lbFlags[1] ;
        lbHaveTwentyFive = lbFlags[2];
        lbHaveTen = lbFlags[3];
        lbHaveFive = lbFlags[4];
        lbHaveTwoPointFive = lbFlags[5];


		double currentPlate = 0;
		boolean thirtyfive_flag = true;
		for (int i=0; i<6; i++)
		{
			currentPlate_needed = 0;
			switch (i){
			case 0:
				if (!lbHaveFortyFive)
				{	
					currentPlate=0;
					break;
				}
				if (lbHaveFortyFive)
				{
					currentPlate = 45;
					break;
				}
			case 1:
				if(!lbHaveThirtyFive)
				{
					currentPlate = 0;
					break;
				}
				if (lbHaveThirtyFive)
				{
					currentPlate = 35;
					break;
				}
			case 2:
				if (!lbHaveTwentyFive)
				{
					currentPlate = 0;
					break;
				}
				if (lbHaveTwentyFive)
				{
					currentPlate = 25;
					break;
				}
			case 3:
				if (!lbHaveTen)
				{
					currentPlate = 0;
					break;
				}
				if (lbHaveTen)
				{
					currentPlate = 10;
					break;
				}
			case 4: 
				if (!lbHaveFive)
				{
				currentPlate = 0;
				break;
				}				
				if (lbHaveFive)
				{
				currentPlate = 5;
				break;
				}
			case 5:
				if (!lbHaveTwoPointFive)
				{
				currentPlate = 0;
				break;
				}
				if (lbHaveTwoPointFive)
				{
				currentPlate = 2.5;
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
					case "45.0":
						setFortyfivesNeeded(currentPlate_needed);
						break;
					case "35.0":
						setThirtyfivesNeeded(currentPlate_needed);
						break;
					case "25.0":
						setTwentyfivesNeeded(currentPlate_needed);
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
					default:
						break;

					}//end switch


				}
			}


		}


	}//end method calculate	





	double getWeight() {
		return weight;
	}

	public int getFortyFivesNeeded()
	{
		return fortyfivesNeeded;
	}

	public int getThirtyFivesNeeded()
	{
		return thirtyfivesNeeded;
	}

	public int getTwentyFivesNeeded()
	{
		return twentyfivesNeeded;
	}

	public int getTensNeeded()
	{
		return tensNeeded;
	}

	public int getFivesNeeded()
	{
		return fivesNeeded;
	}

	public void setFortyfivesNeeded (int needed)
	{
		fortyfivesNeeded = needed;
	}


	public void setThirtyfivesNeeded (int needed)
	{
		thirtyfivesNeeded = needed;
	}

	public void setTwentyfivesNeeded (int needed)
	{
		twentyfivesNeeded = needed;
	}

	public void setTensNeeded (int needed)
	{
		tensNeeded = needed;
	}

	public void setFivesNeeded(int needed)
	{
		fivesNeeded = needed;
	}

	public void setTwopointfivesNeeded(int needed)
	{
		twopointfivesNeeded = needed;
	}

	public int getTwoPointFivesNeeded()
	{
		return twopointfivesNeeded;
	}


	double round(double i, int v) //first argument is rounded, second argument is what to round to: (5 for lbs)... for kg just figure out a way to get close.. lol
	{
		return (double) (Math.round(i/v) * v);
	}






}
