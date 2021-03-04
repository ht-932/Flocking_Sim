/*Part of the default package so no package identifier needed*/

//Imports for the creation of the GUI and handling inputs to buttons etc.
import drawing.Canvas;
import window.Controls;
import window.Obstacle;
import window.Window;
import javax.swing.JFrame;
import javax.swing.JPanel;

//Imports for the creation and synchronisation of lists
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Import of the entity class used to create the birds/entities.
import entity.Entity;

//Used to identify an entities location
import geometry.CartesianCoordinate;

//Used to make the program wait
import tools.Utils;

public class FlockingProgram {

	/*
	 * Create local variables to store the frame, canvas and side panel.
	 * canvas will be where the entities are drawn. sidePanel where the
	 * controls will be and frame is to display two.
	 */
	private JFrame frame;
	private Canvas canvas;
	private JPanel sidePanel;

	//Create a variable for the obstacle and nestLocation.
	private Obstacle obstacle;
	private CartesianCoordinate nestLocation;

	/*
	 * Create a list of entities to store every single entity to draw in and
	 * a list of entities to add, so they can be added at the correct time 
	 * in the simulation loop.
	 */
	private List<Entity> entities;
	private List<Entity> entitiesToAdd;
	
	//Create a variable for the window and controls.
	private Window window;
	private Controls controls;

	/*
	 * The constructor for the program overall. This will run after main().
	 * It creates the window, controls, lists of entities, synchronises them 
	 * and creates the nest location. It then automatically runs the simulation
	 * loop and the user can interact with the program.
	 */
	private FlockingProgram() {
		
		//Instantiate frame, canvas and sidePanel.
		frame = new JFrame();
		sidePanel = new JPanel();
		canvas = new Canvas();
		
		//Use the window class to create objects for the GUI..
		window = new Window(frame, sidePanel, canvas);
		controls = new Controls(sidePanel, canvas, entities);
		obstacle = new Obstacle(canvas);
		
		//Draw the created objects.
		window.createFrames();
		controls.createControls();
		obstacle.draw();

		//Synchronise the lists of entities and entities to add to prevent issues 
		//in the simulation loop.
		entities = Collections.synchronizedList(new ArrayList<Entity>());
		entitiesToAdd = Collections.synchronizedList(new ArrayList<Entity>());
		
		//Create a nest location for the birds to be drawn too
		nestLocation = new CartesianCoordinate(100, 100); 
		
		//Run simulation loop.
		simulationLoop();
		
	}

	/*
	 * This loop runs constantly throughout use of the program. It calculates,
	 * moves, draws and adds entities from the add list. Any other function 
	 * of the program is operated by the Change and Action listeners found 
	 * in window -> controls.
	 */
	private void simulationLoop() {

		//Creates an infinite loop, the program can be terminated by exiting the window.
		//At which point, this will stop running.
		while (true) {

			/*
			 * Draws all entities stored in the list entities.
			 */			
			synchronized (entities) { 
				for (Entity entity : entities) {

					//Every object in the list is iterated over and its draw() function called.
					entity.draw();
			
				}}

			//Pauses the program for 20 milliseconds to make the drawn entities more viewable.
			Utils.pause(20);

			/*
			 * Moves the entity according to its current movement values, then calculates new
			 * ones. The entity is then un-drawn. In depth explanations of each action are found
			 * within in its respective method/class.
			 */
			synchronized (entities) { 
				for (Entity entity : entities) {

					//Move the entity, this needs the current speed from the on screen slider.
					entity.move(controls.getSpeed());
					
					//Apply the cohesive factor to the entity / Move them closer.
					entity.applyCohesion(controls.getCohesionFactor());
					
					//Apply the separation factor to the entity / Move them apart.
					entity.applySeperation(controls.getSeperationFactor());
					
					//Apply the alignment factor to the entity / Move them in the same direction.
					entity.applyAlignment(controls.getAlignmentFactor());
					
					//Apply the nest attraction factor to the entity / move it towards (100,100).
					entity.applyNestAttraction(nestLocation, controls.getNestAttractionFactor());
					
					//Apply the avoidance factor and check if it has been hit.
					entity.applyObstacleAvoidance();
					
					//See if the entity has hit another entity.
					entity.applyCollisions(entities, controls.isCollisionDetection());
					
					//Move entities away from nearby predators.
					entity.applyPredator();
					
					//If the entity leaves the screen, make it reappear on the other side.
					entity.wrapPosition();
					
					//Remove the entity from view.
					entity.undraw();

				}}

			//Get the list of entities to add from the controls.
			entitiesToAdd = controls.getEntitiesToAdd();
			
			/*
			 * If there are any entities to add, add them to the main list of entities.
			 */
			if (entitiesToAdd.size() != 0) {
				
				synchronized (entitiesToAdd) { 
					for (Entity entity : entitiesToAdd) {
						
						entities.add(entity);
						
					}
				
				}
				
				//CLear the list so the entities are not added again by mistake.
				entitiesToAdd.clear();
					
			}

		}

	}

	/*
	 * The main function serves as the entry point to the program. 
	 * It simply starts a new FlockingProgram()
	 */
	public static void main(String[] args) {

		new FlockingProgram();

	}

}
