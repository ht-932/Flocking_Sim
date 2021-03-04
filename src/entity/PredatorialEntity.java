/* The entity package contains anything related to the boids/entities/birds and their behaviour. */
package entity;

//Import "Canvas" so the entities can be drawn and "CartesianCoordinate" so they can be given locations.
import drawing.Canvas;
import geometry.CartesianCoordinate;

//A predatorial entity will appear at a random location, thus extends randomEntity.
public class PredatorialEntity extends RandomEntity {
	
	//A Predator will be drawn as a large box. The locations of its 4 corners are stored here.
	private CartesianCoordinate topLeftOfPredator;
	private CartesianCoordinate topRightOfPredator;
	private CartesianCoordinate bottomLeftOfPredator;
	private CartesianCoordinate bottomRightOfCoordinate;

	/*
	 * Constructor for a predatorial entity, no location or movement angle is required 
	 * as they are randomised.
	 */
	public PredatorialEntity(Canvas canvas) {
		super(canvas);
		
		//Set to true so other entities can identify this one as a predator.
		isAPredator = true;
		
	}
	
	/*
	 * Use after calculateFlockParameters(). Will calculate the angle of travel required to move the entity towards 
	 * the centre of its flock then reduce it by the cohesion factor. A predator will head towards the centre of a 
	 * flock aggressively, hence the overridden cohesion factor.
	 */
	@Override
	public void applyCohesion(double cohesionFactor) {

		//Calculate the x and y distances between the centre of the flock and the entity.
		double xDist = centreOfFlock.getX() - currentLocation.getX();
		double yDist = centreOfFlock.getY() - currentLocation.getY();

		//Apply trigonometry, treating the x value as the opposite side of the triangle and 
		//the y distance as the adjacent side to find the angle required.
		angleRequiredForCohesion = Math.toDegrees(Math.atan(xDist/yDist));

		//Apply the calculated angle, but reduced by the cohesion factor.
		entityMovementAngle = entityMovementAngle + 0.01 * angleRequiredForCohesion;

	}
	
	/*
	 * Separation, alignment and nest attraction are disabled as a predator would only be drawn towards 
	 * a flock and would not know where its nest is. A predator would also never flee from another predator
	 * (or itself) so this is disabled.
	 */
	
	//Not required by a predator.
	@Override
	public void applySeperation(double seperationFactor) {}
	
	//Not required by a predator.
	@Override
	public void applyAlignment(double alignmentFactor) {

	}
	
	//Not required by a predator.
	@Override
	public void applyNestAttraction(CartesianCoordinate nestLocation, double nestAttractionFactor) {}
	
	//Not required by a predator.
	@Override 
	public void applyPredator() {};
	
	/*
	 * Draw() is overridden so a predator is visually different compared to a standard entity. 4 lines are
	 * drawn in accordance with the current location, the box is 4x4 in size.
	 * This must be done before the first draw undraw() else the program will be out of sync.
	 */
	@Override
	public void draw() {
		
		//Create the coordinates a 4x4 box around the current location.
		topLeftOfPredator = new CartesianCoordinate(currentLocation.getX() - 2, currentLocation.getY() + 2);
		topRightOfPredator = new CartesianCoordinate(currentLocation.getX() + 2, currentLocation.getY() + 2);
		bottomLeftOfPredator = new CartesianCoordinate(currentLocation.getX() - 2, currentLocation.getY() - 2);
		bottomRightOfCoordinate = new CartesianCoordinate(currentLocation.getX() + 2, currentLocation.getY() - 2);
		
		//Draw the calculated box/
		canvas.drawLineBetweenPoints(topLeftOfPredator, topRightOfPredator);
		canvas.drawLineBetweenPoints(topRightOfPredator, bottomRightOfCoordinate);
		canvas.drawLineBetweenPoints(bottomRightOfCoordinate, bottomLeftOfPredator);
		canvas.drawLineBetweenPoints(bottomLeftOfPredator, topLeftOfPredator);
		
	}
	
	/*
	 * As draw() has been overridden, undraw() must also be altered accordingly. Else not all of the lines 
	 * will be deleted. The 4 lines just drawn are removed and the canvas "repainted" to register the changes.
	 * This must be run before the next draw() call else the program will be out of sync.
	 */
	@Override 
	public void undraw() {
		
		//Remove the 4 lines of the box
		canvas.removeMostRecentLine();
		canvas.removeMostRecentLine();
		canvas.removeMostRecentLine();
		canvas.removeMostRecentLine();
		
		//Repiant the canvas to show the changes.
		canvas.repaint();
		
	}

}
