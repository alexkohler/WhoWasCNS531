package com.example.whowascns;

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
	int twosNeeded;
	int onepointfivesNeeded;
	int onesNeeded;
	int pointfivesNeeded;

	int currentPlate_needed;
	String currentPlateName;


	public void computeKgPlates (double myWeight, double barbellUsed)
	{
		weight = myWeight - barbellUsed;
		plateWeight = round (weight, 1); //this will have to be dynamic depending on the plates they have 
		//plateWeight = weight;


		double currentPlate = 0;
		//boolean thirtyfive_flag = true;
		for (int i=0; i<9; i++)
		{
			currentPlate_needed = 0;
			switch (i){
			case 0:
				currentPlate = 25;
				break;
			case 1:
			//	if(!thirtyfive_flag) //could add a flag for each of these to allow user to specify what plates they have 
				//{
					//currentPlate = 0;
				//	break;
				//}
				//if (thirtyfive_flag)
				//{
					currentPlate = 20;
					break;
				//}
			case 2:
				currentPlate = 15;
				break;
			case 3:
				currentPlate = 10;
				break;
			case 4: 
				currentPlate = 5;
				break;
			case 5:
				currentPlate = 2.5;
				break;
			case 6:
				currentPlate = 2;
				break;
			case 7:	
				currentPlate = 1.5;
				break;
			case 8:
				currentPlate = 1;
				break;
			case 9:
				currentPlate = .5;
				break;
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
					case "2.0":
						setTwosNeeded(currentPlate_needed);
						break;
					case "1.5":
						setOnepointfivesNeeded(currentPlate_needed);
						break;
					case "1.0":
						setOnepointfivesNeeded(currentPlate_needed);
						break;
					case "0.5":
						setPointfivesNeeded(currentPlate_needed);
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




	double round(double i, int v) //first argument is rounded, second argument is what to round to: (5 for lbs)... for kg just figure out a way to get close.. lol
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





	public int getTwosNeeded() {
		return twosNeeded;
	}





	public void setTwosNeeded(int twosNeeded) {
		this.twosNeeded = twosNeeded;
	}





	public int getOnepointfivesNeeded() {
		return onepointfivesNeeded;
	}





	public void setOnepointfivesNeeded(int onepointfivesNeeded) {
		this.onepointfivesNeeded = onepointfivesNeeded;
	}





	public int getOnesNeeded() {
		return onesNeeded;
	}





	public void setOnesNeeded(int onesNeeded) {
		this.onesNeeded = onesNeeded;
	}





	public int getPointfivesNeeded() {
		return pointfivesNeeded;
	}





	public void setPointfivesNeeded(int pointfivesNeeded) {
		this.pointfivesNeeded = pointfivesNeeded;
	}



}
