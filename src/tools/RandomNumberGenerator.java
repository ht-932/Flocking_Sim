/*Tools contains generic tools for the manipulation of the program.
 * 
 */
package tools;

//Import the random number generator for use.
import java.util.Random;

public class RandomNumberGenerator {
	
	//Create a random number generation object storage space.
	private Random randomNumberObject;
	
	/*
	 * Create variables to store the required lower and upper limits of the random number and
	 * one to store the random value once it has been generated.
	 */
	double lowerLimit, upperLimit, randomValue;

	/*
	 * Constructor to generate a random (whole) number within the desired limits
	 * when get() is called, making this reusable.
	 * @param lowerLimit, the smallest possible number that could be generated.
	 * @param upperLimit, the largest possible number that could be generated.
	 */
	public RandomNumberGenerator(double lowerLimit, double upperLimit) {
		
		//Create a new random number generator.
		randomNumberObject = new Random();
		
		//Set the upper and lower limits.
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		
	}
	
	/*
	 * Call to generate a new random value within the limits specified in the constructor.
	 * @return randomValue, the new random value stored as a double.
	 */
	public double get() {
		
		//Generate the value and apply the upper and lower.
		randomValue = lowerLimit + (upperLimit - lowerLimit) * randomNumberObject.nextDouble();
		
		return randomValue;
		
	}
	
}
