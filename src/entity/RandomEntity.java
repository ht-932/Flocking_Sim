/* The entity package contains anything related to the boids/entities/birds and their behaviour. */
package entity;

//Import "Canvas" so the entities can be drawn, "CartesianCoordinate" so they can be given locations
//and the random number generator to provide random locations and movement angles.
import drawing.Canvas;
import geometry.CartesianCoordinate;
import tools.RandomNumberGenerator;

//A random entity is an entity thus extends it.
public class RandomEntity extends Entity {
	
	//Create storage space for the random number generators.
	private RandomNumberGenerator randomX;
	private RandomNumberGenerator randomY;
	private RandomNumberGenerator randomAngle;

	/*
	 * Constructor for a random entity with no set values whatsoever.
	 * @param canvas, a sheet to draw the entity on.
	 */
	public RandomEntity(Canvas canvas) {
		super(canvas);
		
		//Generate random coordinates and a random movement angle.
		randomX = new RandomNumberGenerator(0, 700);
		randomY = new RandomNumberGenerator(500, 0);
		randomAngle = new RandomNumberGenerator(0, 360);
		
		//Assign the randomly generated coordinates.
		currentLocation.setX(randomX.get());
		currentLocation.setY(randomY.get());
		
		//Set the entities movement angle to the randomly generated one.
		setEntityMovementAngle(randomAngle.get());
		
		//Checks if the random entity has been created inside the obstacle. 
		checkIfInsideObstacle();
		
	}
	
	/*
	 * Constructor for a random entity which will be created within a given space. The space can 
	 * be thought of as a box with a top left and bottom right coordinate. The entity will also 
	 * be given movement angle. Useful for creating entities in the same direction and proximity (flocks).
	 * @param canvas, to draw the entity on.
	 * @param topLeftLimit, the upper left corner of the limiting "box".
	 * @param bottomRightLimit, the bottom right corner of the limiting "box".
	 * @param movementAngle, the desired entity movement angle.
	 */
	public RandomEntity(Canvas canvas, CartesianCoordinate topLeftLimit, CartesianCoordinate bottomRightLimit, double movementAngle) {
		super(canvas);
		
			//Generate two random coordinates for the entity
			randomX = new RandomNumberGenerator(topLeftLimit.getX(), bottomRightLimit.getX());
			randomY = new RandomNumberGenerator(topLeftLimit.getY(), bottomRightLimit.getY());
		
		//Assign the randomly generated coordinates.
		currentLocation.setX(randomX.get());
		currentLocation.setY(randomY.get());
		
		//Set the entities movement angle to the desired one.
		setEntityMovementAngle(movementAngle);
		
		//Checks if the random entity has been created inside the obstacle. 
		checkIfInsideObstacle();
		
	}
	
	/*
	 * Checks if the random entity has been created inside the obstacle. If it has been its coordinates are
	 * altered to a fixed point. As attempts at using another random number created stack overflow errors.
	 * Possibly due to random numbers being in a similar range.
	 */
	private void checkIfInsideObstacle() {
		
		//Gets x and y distances between the new entity and the centre of the obstacle.
		double xDistFromObstacle = 350 - currentLocation.getX();
		double yDistFromObstacle = 250 - currentLocation.getY();
		
		//Use Pythagoras theorem to find the distance between the obstacle and the entity for obstacle detection.
		double distFromObstacle = Math.sqrt(xDistFromObstacle * xDistFromObstacle + yDistFromObstacle * yDistFromObstacle);
		
		//Check is the distance to the centre of the obstacle is less than the radius.
		if (distFromObstacle <= 80) {

			//Sets the current location well out of the way of the obstacle (-100).
			currentLocation.setX(currentLocation.getX() - 100);
			currentLocation.setY(currentLocation.getY() - 100);
				
		}

	}
	
}

