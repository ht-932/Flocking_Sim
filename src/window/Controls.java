/*The window package contains everything related to the controls/GUI.*/
package window;

//Import Action/Change events.
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//Import array/lists.
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Import buttons/labels/sliders/panels.
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.Color;
import drawing.Canvas;
import entity.Entity;

//Import entities.
import entity.PredatorialEntity;
import entity.RandomEntity;

//Import the CartesianCoordinate system.
import geometry.CartesianCoordinate;

//Import the random number generator.
import tools.RandomNumberGenerator;

public class Controls {
	
	//Create variables to store the buttons.
	private JButton addFlockButton;
	private JButton collisionDetectionButton;
	private JButton addRandomEntityButton;
	private JButton addRandomPredatorButton;

	//Create variables to store the sliders.
	private JSlider entitySpeedSlider;
	private JSlider sizeOfFlockToAddSlider;
	private JSlider angleOfFlockToAddSlider;
	private JSlider cohesionFactorSlider;
	private JSlider alignmentFactorSlider;
	private JSlider seperationFactorSlider;
	private JSlider nestAttractionFactorSlider;

	//Create variables to store the labels.
	private JLabel simulationControlLabel;
	private JLabel flockControlLabel;
	private JLabel entitySpeedLabel;
	private JLabel sizeOfFlockToAddLabel;
	private JLabel angleOfFlockToAddLabel;
	private JLabel cohesionFactorLabel;
	private JLabel alignmentFactorLabel;
	private JLabel seperationFactorLabel;
	private JLabel nestAttractionFactorLabel;
	private JLabel collisionsLabel;
	
	//Create variables to store the trueFactor, a double used during calculations.
	private double trueFactor;
	
	//Create and set the default values for the factors needed to control the entities behaviour.
	private double speed = 1;
	private double cohesionFactor = 0.2;
	private double alignmentFactor = 0.1;
	private double seperationFactor = 0.2;
	private double nestAttractionFactor = 0.0;
	
	private boolean collisionDetection = false;
	
	//Create a list to store every entity in existence.
	private List<Entity> entitiesToAdd;
	
	//Create variable to store the sidePanel which will be populated with controls.
	private JPanel sidePanel;

	//Create variable to store the canvas.
	private Canvas canvas;
	
	/*
	 * Constructor for the controls of the program.
	 * @param sidePanel, a panel to display the buttons and sliders on.
	 * @param canvas, a canvas to draw entities on, needed by the interrupt handlers.
	 * @param entitiesToAdd, a list of entities to add to the program again, needed by interrupt handlers.
	 */
	public Controls(JPanel sidePanel, Canvas canvas, List<Entity> entitiesToAdd) {
		
		//Sets all parameters to local equivalents.
		this.sidePanel = sidePanel;
		this.canvas = canvas;
		this.entitiesToAdd = entitiesToAdd;
		this.entitiesToAdd = Collections.synchronizedList(new ArrayList<Entity>());
		
	}
	
	/*
	 * Creates the Labels, Buttons and Sliders of the GUI. This has to be done in chronological order
	 * to prevent controls appearing in the wrong place, so it is one large method.
	 */
	public void createControls() {

		//Create buttons.
		addFlockButton = new JButton("Add Flock At Random Location");
		collisionDetectionButton = new JButton("Toggle Collisions");
		addRandomEntityButton = new JButton("Add Random Entity");
		addRandomPredatorButton = new JButton("Add Random Predator");

		//Create labels .
		entitySpeedLabel = new JLabel("  Entity Speed = 1");
		sizeOfFlockToAddLabel = new JLabel("  Size Of New Flock = 2");
		angleOfFlockToAddLabel = new JLabel("  Angle Of New Flock = 90");
		cohesionFactorLabel = new JLabel("  Cohesion Factor = 0.2");
		alignmentFactorLabel = new JLabel("  Alignment Factor = 0.1");
		seperationFactorLabel = new JLabel("  Seperation Factor = 0.2");
		nestAttractionFactorLabel = new JLabel(" Nest Attaction Factor = 0.0");
		simulationControlLabel = new JLabel("  Simulation Controls:");
		flockControlLabel = new JLabel("  Flock Controls:");
		collisionsLabel = new JLabel("  Collisions Are Disabled");

		//Create sliders with their default values which are what I found to make the simulation
		//act the most naturally. All factors must be divided by 10 before being applied as sliders 
		//can only handle ints.
		entitySpeedSlider = new JSlider(0, 5, 1);
		sizeOfFlockToAddSlider = new JSlider(1, 5, 2);
		angleOfFlockToAddSlider = new JSlider(0, 360, 90);
		cohesionFactorSlider = new JSlider(0, 10, 2);
		alignmentFactorSlider = new JSlider(0, 10, 1);
		seperationFactorSlider = new JSlider(0, 10, 2);
		nestAttractionFactorSlider = new JSlider(0, 10, 0);

		//Add all objects to the side panel, in order of appearance. 
		//Event Listeners are added below their respective buttons and sliders. 
		
		//Adds the simulation control label at the top of the panel.
		sidePanel.add(simulationControlLabel);

		sidePanel.add(collisionDetectionButton);
		sidePanel.add(collisionsLabel);
		collisionDetectionButton.addActionListener(new addCollisionsButtonListener());

		sidePanel.add(entitySpeedLabel);
		sidePanel.add(entitySpeedSlider);
		entitySpeedSlider.addChangeListener(new entitySpeedSliderListener());

		sidePanel.add(cohesionFactorLabel);
		sidePanel.add(cohesionFactorSlider);
		cohesionFactorSlider.addChangeListener(new cohesionFactorSliderListener());

		sidePanel.add(alignmentFactorLabel);
		sidePanel.add(alignmentFactorSlider);
		alignmentFactorSlider.addChangeListener(new alignmentFactorSliderListener());

		sidePanel.add(seperationFactorLabel);
		sidePanel.add(seperationFactorSlider);
		seperationFactorSlider.addChangeListener(new seperationFactorSliderListener());

		sidePanel.add(nestAttractionFactorLabel);
		sidePanel.add(nestAttractionFactorSlider);
		nestAttractionFactorSlider.addChangeListener(new nestAttractionFactorSliderListener());

		sidePanel.add(flockControlLabel);

		sidePanel.add(addRandomEntityButton);
		addRandomEntityButton.addActionListener(new addRandomEntityButtonListener());

		sidePanel.add(sizeOfFlockToAddLabel);
		sidePanel.add(sizeOfFlockToAddSlider);
		sizeOfFlockToAddSlider.addChangeListener(new sizeOfFlockToAddSliderListener());
		
		sidePanel.add(angleOfFlockToAddLabel);
		sidePanel.add(angleOfFlockToAddSlider);
		angleOfFlockToAddSlider.addChangeListener(new angleOfFlockToAddSliderListener());

		sidePanel.add(addFlockButton);
		addFlockButton.addActionListener(new addFlockButtonListener());
		
		sidePanel.add(addRandomPredatorButton);
		addRandomPredatorButton.addActionListener(new addRandomPredatorButtonListener());

		//Set the background colours of the sliders as they look more natural blended with the background.
		entitySpeedSlider.setBackground(Color.lightGray);
		sizeOfFlockToAddSlider.setBackground(Color.lightGray);
		angleOfFlockToAddSlider.setBackground(Color.lightGray);
		cohesionFactorSlider.setBackground(Color.lightGray);
		alignmentFactorSlider.setBackground(Color.lightGray);
		seperationFactorSlider.setBackground(Color.lightGray);
		nestAttractionFactorSlider.setBackground(Color.lightGray);

	}
	
	/*
	 * Event and Change Listeners...
	 */
	
	/*
	 * Will run if the collisions button is pressed. Toggles collisions.
	 */
	public class addCollisionsButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			//Check if collisions have been set to enabled, or disabled,
			//change the JLabel and set the variable appropriately. 
			if (collisionDetection == true) {
				collisionDetection = false;
				collisionsLabel.setText("  Collisions Are Disabled");
			} else {
				collisionDetection = true;
				collisionsLabel.setText("  Collisions Are Enabled");
			}

		}

	}

	/*
	 * Will run if the entity speed slider is moved. Sets the simulation speed.
	 */
	public class entitySpeedSliderListener implements  ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {

			//Set the label to the value of the slider.
			entitySpeedLabel.setText("  Entity Speed = " + entitySpeedSlider.getValue());

			//Set the variable to the value of the slider.
			speed = entitySpeedSlider.getValue();

		}

	}

	/*
	 * Will run if the Cohesion slider is moved. Sets the cohesion factor.
	 */
	public class cohesionFactorSliderListener implements  ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {

			//Get the new value of the slider.
			trueFactor = cohesionFactorSlider.getValue();

			//Reduce the factor by a factor of 10 and set the factor.
			cohesionFactor = trueFactor / 10;

			//Set the label to show the new factor.
			cohesionFactorLabel.setText(" Cohesion Factor = " + cohesionFactor);

		}

	}


	/*
	 * Will run if the Separation slider is moved. Sets the separation factor.
	 */
	public class seperationFactorSliderListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {

			//Get the new value of the slider.
			trueFactor = seperationFactorSlider.getValue();

			//Reduce the factor by a factor of 10 and set the factor.
			seperationFactor = trueFactor / 10;

			//Set the label to show the new factor.
			seperationFactorLabel.setText(" Seperation Factor = " + seperationFactor);

		}
	}

	/*
	 * Will run if the Alignment slider is moved. Sets the Alignment factor.
	 */
	public class alignmentFactorSliderListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {

			//Get the new value of the slider.
			trueFactor = alignmentFactorSlider.getValue();

			//Reduce the factor by a factor of 10 and set the factor.
			alignmentFactor = trueFactor / 10;
			
			//Set the label to show the new factor.
			alignmentFactorLabel.setText(" Alignment Factor = " + alignmentFactor);

		}


	}

	/*
	 * Will run if the Nest Attraction slider is moved. Sets the Nest Attraction factor.
	 */
	public class nestAttractionFactorSliderListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {

			//Get the new value of the slider.
			trueFactor = nestAttractionFactorSlider.getValue();

			//Reduce the factor by a factor of 10 and set the factor.
			nestAttractionFactor = trueFactor / 10;

			//Set the label to show the new factor.
			nestAttractionFactorLabel.setText(" Nest Attarction Factor = " + nestAttractionFactor);

		}
	}

	/*
	 * Will run if the add random entity button is pressed. Adds a random entity to the program.
	 */
	public class addRandomEntityButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			//Create a new entity.
			RandomEntity flockComponent = new RandomEntity(canvas);
			
			//Add the new entity to the new entities list.
			entitiesToAdd.add(flockComponent);

		}

	}

	/*
	 * Will run if the size of flock slider is moved. Sets the size of the flock added.
	 */
	public class sizeOfFlockToAddSliderListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {

			//Set the label - the value is accessed using getValue() elsewhere.
			sizeOfFlockToAddLabel.setText(" Size Of New Flock = " + sizeOfFlockToAddSlider.getValue());
			
		}

	}

	/*
	 * Will run if the size of flock slider is moved. Sets the movement angle of the flock added.
	 */
	public class angleOfFlockToAddSliderListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {

			//Set the label - the value is accessed using getValue() elsewhere.
			angleOfFlockToAddLabel.setText(" Angle Of New Flock = " + angleOfFlockToAddSlider.getValue());
			
		}

	} 
	
	/*
	 * Will run if the add flock button is pressed, adds a flock to the program.
	 * A "Flock box" is generated, in which, entities will be randomly placed with 
	 * the same movement angle.
	 */
	public class addFlockButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {	
			
			//Create limits in which the flock will be added, a "flock box".
			RandomNumberGenerator randomXForBoxGenerator = new RandomNumberGenerator(20, 680);
			RandomNumberGenerator randomYForBoxGenerator = new RandomNumberGenerator(20, 480);
			
			//Create flock box.
			double flockBoxX = randomXForBoxGenerator.get();
			double flockBoxY = randomYForBoxGenerator.get();
			
			//Assign flock box coordinates to CartesianCoordinates.
			CartesianCoordinate topLeftLimit = new CartesianCoordinate(flockBoxX, flockBoxY);
			CartesianCoordinate bottomRightLimit = new CartesianCoordinate(flockBoxX - 20, flockBoxY - 20);
			
			//Iterates for the size of flock requested, creating a new entity with each iteration.
			for (int counter = 1; counter <= sizeOfFlockToAddSlider.getValue(); counter++) {
				
				//Create a new member of the flock.
				RandomEntity flockCompoenent = new RandomEntity(canvas, topLeftLimit, bottomRightLimit, angleOfFlockToAddSlider.getValue());
				
				//Add the new member of the flock to the program.
				entitiesToAdd.add(flockCompoenent);
				
			}
			
		}
		
	}
	
	/*
	 * When the add predator button is pressed, add a predator. 
	 */
	public class addRandomPredatorButtonListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			//Create a new predator.
			PredatorialEntity flockComponent = new PredatorialEntity(canvas);
			
			//Add the predator.
			entitiesToAdd.add(flockComponent);
			
		}
		
	}
	
	/*
	 * Getters and Setters...
	 */

	/*
	 * Gets the entities speed.
	 * @return speed double
	 */
	public double getSpeed() {
		return speed;
	}

	/*
	 * Gets the current cohesion factor.
	 * @return cohesionFactor double 
	 */
	public double getCohesionFactor() {
		return cohesionFactor;
	}

	/*
	 * Gets the current alignment factor.
	 * @return alignmentFactor double 
	 */
	public double getAlignmentFactor() {
		return alignmentFactor;
	}

	/*
	 * Gets the current separation factor.
	 * @return separationFactor double 
	 */
	public double getSeperationFactor() {
		return seperationFactor;
	}

	/*
	 * Gets the current nest attraction factor.
	 * @return nestAttractionFactor double 
	 */
	public double getNestAttractionFactor() {
		return nestAttractionFactor;
	}

	/*
	 * Is collision detection enabled true/false.
	 * @return collisionDetection boolean 
	 */
	public boolean isCollisionDetection() {
		return collisionDetection;
	}
	
	/*
	 * Gets a list of all the entities that need to be added to the simulation.
	 * @return entitiesToAdd List<entities>
	 */
	public List<Entity> getEntitiesToAdd() {
		return entitiesToAdd;
	}

}
