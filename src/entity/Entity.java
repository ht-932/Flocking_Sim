/*The entity package contains anything related to the creation and manipulation
 * of the entities.*/
package entity;

//Import everything needed for the list of predators.
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Import canvas to draw the entities 
import drawing.Canvas;

//Import Cartesian coordinate to give the entities locations.
import geometry.CartesianCoordinate;

public class Entity{

	//Create a local variable to store the canvas, it is protected so it can be 
	//Accessed by predatorialEntity().
	protected Canvas canvas;

	//Create Cartesian coordinates to store the old and current locations of the entity.
	CartesianCoordinate currentLocation = new CartesianCoordinate();
	CartesianCoordinate oldLocation = new CartesianCoordinate();
	
	//Store the movement angle of the entity, it is protected so it can be accessed by predator.
	protected double entityMovementAngle;
	
	//Create a flock diameter - This is used to create a box around an entity which, if any other
	//entity is inside of, it will be considered in the line of sight of the current entity and
	//be used to effect the behaviour of the current entity.
	//In essence, this is the size of the flock.
	double flockDiameter = 50;
	
	//Local variables to store the average movement angle total movement angle of birds within the "flock box".
	double flockMovementAngle, sumOfAnglesInFlock;
	
	//Local variables to store the angles needed for entity to head towards or avoid something.
	double angleRequiredForAllignment, angleRequiredForCohesion, angleRequiredForSeperation, angleRequiredForNest, angleRequiredForObstacleAvoidance;

	//Local variables to store the quantity of entities, their total x coordinates and their total y coordinates.
	double entitiesInFlock, sumOfXDistances, sumOfYDistances;
	
	//Local variables to store the calculated centre of the flock.
	protected CartesianCoordinate centreOfFlock = new CartesianCoordinate();

	//Once calculated, these store the coordinates of the corners of the "flock box" (see double flockDiameter).
	double xLowerBound;
	double xUpperBound;
	double yLowerBound;
	double yUpperBound;
	
	//Used to tell entities apart from predatorialEntities during detection.
	protected boolean isAPredator = false;
	
	//Create a list to store any predators in sight of the entity, so its behaviour can be altered accordingly. 
	private List<PredatorialEntity> predatorsInSight;
	
	/*
	 * Constructor used by the RandomEntity and PredatorialEntity class. 
	 * It will create an entity with completely default values.
	 * e.g. movementAngle = 0
	 * @param Canvas, the canvas the entities will be drawn on.
	 */
	public Entity(Canvas canvas) {
		this.canvas = canvas;
		
		//Synchronise the predatorsInSight list to avoid any issues.
		predatorsInSight = Collections.synchronizedList(new ArrayList<PredatorialEntity>());
		
	}

	/*
	 * Move the entity at the desired speed. This is done using trigonometry to find the distance and 
	 * multiplying it by the speed. If the speed is 0, the entity will not move.
	 * @param speed, the desired speed for the entity to move.
	 */
	public void move(double speed) {
		
		//Set the entities current location equal to its old location so it can be used as reference below.
		oldLocation.setX(currentLocation.getX());
		oldLocation.setY(currentLocation.getY());

		/*
		 * The problem here is the entity needs to be moved with only a known location and a desired movement angle.
		 * Using these two pieces of data we can break the problem down into a right angled triangle. As speed is
		 * in pixels a seconds the value of speed dictates how far the entity should move. This will be the hypotunuse 
		 * of the triangle. The angle, theta, of the triangle is the entities current movement angle. This produces
		 * a trigonometry question. We know by using 
		 * 
		 * 		sin(theta) = opp/hyp		and		cos(theta) = adj/hyp
		 * 
		 * we can find the sizes of the two remaining sides of the triangle, which will be our movement values 
		 * in terms of x and y coordinates. The final issue is that this only works if theta is less than 90,
		 * else it wont make part of a triangle. Therefore theta must be reduced accordingly. When the size of
		 * the x and y distances is found they can be multiplied by speed, as speed = pixels per refresh.
		 */
		
		//Check if the movementAngle is 90 or less, meaning it will not need adjustment.
		if (entityMovementAngle <= 90) {
			
			//Set the new x location to the sum of the old x location and the calculated size of the adjacent side of the triangle,
			//multiplied by the speed/distance factor to get the true size.
			currentLocation.setX(oldLocation.getX() + Math.sin(Math.toRadians(entityMovementAngle)) * speed);
			
			//Set the new y location to the sum of the old x location and the calculated size of the opposite side of the triangle,
			//multiplied by the speed/distance factor to get the true size.
			currentLocation.setY(oldLocation.getY() - Math.cos(Math.toRadians(entityMovementAngle)) * speed);
			
			//Check if the movementAngle between 90 and 180 degrees, meaning it will need to be reduced by a factor of 90 to make
			//the triangle described above.
		} else if (entityMovementAngle <= 180 && entityMovementAngle > 90) {
			
			currentLocation.setX(oldLocation.getX() + Math.cos(Math.toRadians(entityMovementAngle - 90)) * speed);
			currentLocation.setY(oldLocation.getY() + Math.sin(Math.toRadians(entityMovementAngle - 90)) * speed);
			
			//Reduce by factor of 180 if true
		} else if (entityMovementAngle <= 270 && entityMovementAngle > 180) {
			
			currentLocation.setX(oldLocation.getX() - Math.sin(Math.toRadians(entityMovementAngle- 180)) * speed);
			currentLocation.setY(oldLocation.getY() + Math.cos(Math.toRadians(entityMovementAngle- 180)) * speed);
			
			//Reduce by factor of 270 if true.
		} else if (entityMovementAngle >= 270) {
			
			currentLocation.setX(oldLocation.getX() - Math.cos(Math.toRadians(entityMovementAngle - 270)) * speed);
			currentLocation.setY(oldLocation.getY() - Math.sin(Math.toRadians(entityMovementAngle - 270)) * speed);
			
		}

	}

	/*
	 * Adjusts the entities movementAngle by the specified value, if a reduction is required, input a negative value.
	 * @param, entityMovementAngleAdjustment, the angle of adjustment to be applied.
	 */
	public void turn(double entityMovementAngleAdjustment) {
		
		//Apply the adjustment.
		entityMovementAngle = entityMovementAngleAdjustment + entityMovementAngle;
		
		//Check if the value has gone above or below the limits of degrees and adjust accordingly.
		if (entityMovementAngle > 360) {
			entityMovementAngle = entityMovementAngle - 360;
		} else if (this.entityMovementAngle < 0) {
			entityMovementAngle = entityMovementAngle + 360;
		} 

	}

	/*
	 * Checks if an entity has lefts the confines of the screen. If it has, it is placed 
	 * on the opposing side of the screen.
	 */
	public void wrapPosition() {
		
		//Check is an entity has left the left hand side of the screen
		if (currentLocation.getX() <= 0) {
			
			//Move entity to the left hand side of the screen.
			currentLocation.setX(698);
			
		}
		
		//Check is an entity has left the upper side of the screen
		if (currentLocation.getY() <= 0) {
			
			//Move entity to the right upper of the screen.
			currentLocation.setY(498);
			
		}
		
		//Check is an entity has left the right hand side of the screen
		if (currentLocation.getX() >= 699) {
			
			//Move entity to the right hand side of the screen.
			currentLocation.setX(0);
			
		}
		
		//Check is an entity has left the lower side of the screen
		if (currentLocation.getY() >= 499) {
			
			//Move entity to the lower side of the screen.
			currentLocation.setY(0);
			
		}
		
	}

	/*
	 * Draws the entity at the current location. This must be done before calling undraw()
	 * and any entity drawn must be undrawn if it is to move, else the simulatonLoop will be 
	 * out of sync.
	 */
	public void draw() {
		canvas.drawLineBetweenPoints(currentLocation, currentLocation);
	}

	/*
	 * Removes the entity from view.
	 */
	public void undraw() {
		
		//Remove the line (dot) representing the entity.
		canvas.removeMostRecentLine();
		
		//Repaint the canvas to show the changes.
		canvas.repaint();
	}

	/*
	 * This will calculate the data needed to then calculate the required Cohesion, separation etc.
	 * This is done separately to allow control over which behavioural methods to call during
	 * testing. It works by first calculating the coordinates of the box around the entity or
	 * the "flock box" (if an entity if inside the box its behaviour will affect the behaviour of the
	 * selected entity) and then by making a list of entities and predators inside of the box.
	 * Average movement angles and the centre of the flock are then calculated.
	 * @param Entity, a list of all entities in existence.
	 */
	public void calculateFlockParameters(List<Entity> entities) {
		
		//Sync the list to avoid issues.
		entities = Collections.synchronizedList(new ArrayList<Entity>());

		//Zero the total entities inside the flock box.
		entitiesInFlock = 0; 
		
		//Zero the total x/y values inside the box, which when divided by the total entities gives the average x/y position
		sumOfXDistances = 0; 
		sumOfYDistances = 0;
		
		//Zero the sum of angles
		sumOfAnglesInFlock = 0;

		/*
		 * Iterates over every entity and checks if it is inside the "flock box". If it is its values are taken and
		 * added to the variables used for calculation of the averages.
		 */
		synchronized (entities) { 
			for (Entity entity : entities) {

				//Calculates the size of the flock box.
				xLowerBound = entity.getX() - flockDiameter/2;
				xUpperBound = entity.getX() + flockDiameter/2;
				yLowerBound = entity.getY() - flockDiameter/2;
				yUpperBound = entity.getY() + flockDiameter/2;

						//Checks if an entity is within the x bounds of the flock box.
						if (entity.getX() >= xLowerBound && entity.getX() <= xUpperBound) {
							
							//Checks if an entity is within the y bounds of the flock box.
							if (entity.getY() >= yLowerBound && entity.getY() <= yUpperBound) {
						
								//Add the entities x and y values to the rest of the flocks, for later average calculation.
								sumOfXDistances = sumOfXDistances + entity.getX();
								sumOfYDistances = sumOfYDistances + entity.getY();
								
								//Add the entities movement angle to the rest of the flocks, for later average calculation.
								sumOfAnglesInFlock = sumOfAnglesInFlock + entity.getEntityMovementAngle();
								
								//Add 1 to the entities in flock counter, for later average calculation.
								entitiesInFlock++;

								//Checks if the entity is a predator.
								if (entity.isAPredator == true) {
									
									//Adds the predator to a list of visible predators, to run away from shortly.
									predatorsInSight.add((PredatorialEntity) entity);
									
								}

								
							}
						}
			}				
		}
		
		//The centre of the flock is found by dividing the sum by the quantity.
		centreOfFlock.setX(sumOfXDistances / entitiesInFlock);
		centreOfFlock.setY(sumOfYDistances / entitiesInFlock);
		
		//The same principle is applied to find the average flock movement angle.
		flockMovementAngle = sumOfAnglesInFlock / entitiesInFlock;
		
	}

	/*
	 * Use after calculateFlockParameters(). Will calculate the angle of travel required to move the entity towards 
	 * the centre of its flock then reduce it by the cohesion factor.
	 * @param cohesionFactor, the factor which the cohesion will be reduced by.
	 */
	public void applyCohesion(double cohesionFactor) {

		//Calculate the x and y distances between the centre of the flock and the entity.
		double xDist = centreOfFlock.getX() - currentLocation.getX();
		double yDist = centreOfFlock.getY() - currentLocation.getY();

		//Apply trigonometry, treating the x value as the opposite side of the triangle and 
		//the y distance as the adjacent side to find the angle required.
		angleRequiredForCohesion = Math.toDegrees(Math.atan(xDist/yDist));

		//Apply the calculated angle, but reduced by the cohesion factor.
		entityMovementAngle = entityMovementAngle + cohesionFactor * angleRequiredForCohesion;

	}

	/*
	 * Use after calculateFlockParameters(). Will calculate the angle of travel required to move the entity away from 
	 * the centre of its flock then reduce it by the separation factor.
	 * @param seperationFactor, the factor which the separation will be reduced by.
	 */
	public void applySeperation(double seperationFactor) {

		//Calculate the x and y distances between the centre of the flock and the entity.
		double xDist = centreOfFlock.getX() - currentLocation.getX();
		double yDist = centreOfFlock.getY() - currentLocation.getY();

		//Apply trigonometry, treating the x value as the opposite side of the triangle and 
		//the y distance as the adjacent side to find the angle required.
		angleRequiredForSeperation = (Math.toDegrees(Math.atan(xDist/yDist)));

		//Apply the calculated angle, but reduced by the separation factor.
		entityMovementAngle = entityMovementAngle - seperationFactor * angleRequiredForSeperation;

	}

	/*
	 * Use after calculateFlockParameters(). Will calculate the angle of travel required to make the entity travel in the 
	 * same direction as its flock then reduce it by the alignment factor.
	 * @param alignmentFactor, the factor which the alignment will be reduced by.
	 */
	public void applyAlignment(double alignmentFactor) {

		//Calculate the angle required to align the flock and the entity.
		angleRequiredForAllignment = flockMovementAngle - entityMovementAngle;

		//Apply the calculated angle, but reduced by the alignment factor.
		entityMovementAngle = entityMovementAngle + alignmentFactor * angleRequiredForAllignment;

	}
	
	/*
	 * Will calculate the angle of travel required to make the entity 
	 * travel towards the nest then reduce it by the nest attraction factor.
	 * @param nestAttractionFactor, the factor which the nest attraction will be reduced by.
	 */
	public void applyNestAttraction(CartesianCoordinate nestLocation, double nestAttractionFactor) {
		
		//Calculate the x and y distances between the nest and the entity.
		double xDist = nestLocation.getX() - currentLocation.getX();
		double yDist = nestLocation.getY() - currentLocation.getY();

		//Apply trigonometry, treating the x value as the opposite side of the triangle and 
		//the y distance as the adjacent side to find the angle required.
		angleRequiredForNest = (Math.toDegrees(Math.atan(xDist/yDist)));
		
		//Apply the calculated angle, but reduced by the nest attraction factor.
		entityMovementAngle = entityMovementAngle + nestAttractionFactor * angleRequiredForNest;
		
	}
	
	/*
	 * Calculates and applies the angle needed to avoid the obstacle, also checks for a collision
	 * between the entity and the obstacle.
	 */
	public void applyObstacleAvoidance() {

		//Calculate the x and y distances between the obstacle and the entity.
		double xDistFromObstacle = 350 - currentLocation.getX();
		double yDistFromObstacle = 250 - currentLocation.getY();
		
		//Use Pythagoras theorem to find the distance between the obstacle and the entity for obstacle detection.
		double distFromObstacle = Math.sqrt(xDistFromObstacle * xDistFromObstacle + yDistFromObstacle * yDistFromObstacle);

		//Apply trigonometry, treating the x value as the opposite side of the triangle and 
		//the y distance as the adjacent side to find the angle required.
		angleRequiredForObstacleAvoidance = (Math.toDegrees(Math.atan(xDistFromObstacle / yDistFromObstacle)));

		//Apply the calculated angle, but reduced by a set factor.
		entityMovementAngle = entityMovementAngle - 0.03 * angleRequiredForObstacleAvoidance;
		
		//Check is the distance to the centre of the obstacle is less than the radius.
		if (distFromObstacle <= 80) {
			
			//Invert the angle of travel, bouncing the entity off. 
			entityMovementAngle = entityMovementAngle - 180;
			
		}
		
	}

	/*
	 * If collisions are enabled this will run, it will check every entity to see if there has been a
	 * collision and react by separating them via their movement angles. 
	 * @param entities, a list of every entity in existence. 
	 * @param collisionDetection, a boolean which, if true, enables collision detection.
	 */
	public void applyCollisions(List<Entity> entities, boolean collisionDetection) {
		
		//Check is collision detection is enabled.
		if (collisionDetection == true) {

			//Synchronise the entities list to avoid any issues.
			entities = Collections.synchronizedList(new ArrayList<Entity>());
			
			/*
			 * This loop within a loop takes one entity out of one loop and compares it to
			 * every entity in the second list. Then the first list moves to the second 
			 * entity it then compares it to every entity in the second list again etc.
			 */
			synchronized (entities) { 
				for (Entity entity : entities) {

					//Create the entity for comparison and set it to the entity from the loop,
					//else it cannot be seen within the second loop.
					Entity entityForComparasonOne = new Entity(canvas);
					entityForComparasonOne = entity;

					//Run the second loop and the comparison.
					synchronized (entities) { 
						for (Entity entityForComparasonTwo : entities) {

							//Check if the entities current locations are the same.
							if (entityForComparasonOne.getCurrentLocation() == entityForComparasonTwo.getCurrentLocation()) {

								//If they are the same split them up.
								entityForComparasonOne.turn(90);
								entityForComparasonTwo.turn(-90);

							}

						}

					}
					
				}

			}
			
		}	
		
	}
	
	/*
	 * If a predator is nearby, an angle of avoidance will be calculated and applied.
	 */
	public void applyPredator() {
		
		//Check if any predators have been spotted
		if (predatorsInSight.size() != 0) {
			
			//Iterate over all the predators, calculating an avoidance angle and apply them all
			//as an entity would want to get away from a predator. 
			synchronized (predatorsInSight) { 
				for (PredatorialEntity predator : predatorsInSight) {

					//Calculate the x and y distances between the predator and the entity.
					double xDist = predator.getX() - currentLocation.getX();
					double yDist = predator.getY() - currentLocation.getY();

					//Apply trigonometry, treating the x value as the opposite side of the triangle and 
					//the y distance as the adjacent side to find the angle required.
					angleRequiredForSeperation = (Math.toDegrees(Math.atan(xDist/yDist)));

					//Apply the calculated angle, but reduced by a factor of 0.5 so movement is gradual.
					entityMovementAngle = entityMovementAngle - 0.5 * angleRequiredForSeperation;

				}

			}

		}
		
		//Clear the list of predators in sight to prevent any errors.
		predatorsInSight.clear();
		
	}
	
	
	/*
	 * Getters and Setters
	 */
	
	/*
	 * Gets the current location of the entity.
	 * @return currentLocation CartesianCoordinate
	 */
	public CartesianCoordinate getCurrentLocation() {
		return currentLocation;
	}
	
	/*
	 * Sets the current location of the entity.
	 * @param currentLocation CartesianCoordinate
	 */
	public void setCurrentLocation(CartesianCoordinate currentLocation) {
		this.currentLocation = currentLocation;
	}

	/*
	 * Gets the x location of the entity.
	 * @return x double
	 */
	public double getX() {

		return currentLocation.getX();

	}

	/*
	 * Gets the y location of the entity.
	 * @return y double
	 */
	public double getY() {

		return currentLocation.getY();

	}
	
	/*
	 * Gets the entities movement angle.
	 * @return entityMovementAngle double 
	 */
	public double getEntityMovementAngle() {
		
		return entityMovementAngle;
	}
	
	/*
	 * Sets the entity movement angle.
	 * @param entityMovementAngle double
	 */
	public void setEntityMovementAngle(double entityMovementAngle) {
		
		this.entityMovementAngle = entityMovementAngle;
		
	}

	/*
	 * Gets the average flock movement angle.
	 * @return flockMovementAngle
	 */
	public double getFlockAngle() {

		return flockMovementAngle;
	}
	
	/*
	 * Checks if the entity is a predator.
	 * @return isAPredator boolean
	 */
	public boolean getPredator() {
		
		return isAPredator;
		
	}

}
