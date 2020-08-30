package main;

import automata.Automaton;
import automata.Automaton.Action;
import automata.LRI;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.Environment.Response;

/**
The experiment is responsible for facilitating and tracking the interation between the automaton and the environment.
**/
public class Experiment {

	// Number of iterations to train the automaton to identify the correct action
	private static final int TRAINING_ITERATIONS = 10000;
	
	// Number iterations to test the accuracy of the automaton
	private static final int TESTING_ITERATIONS = 1000;
	
	private StringProperty experimentNum;
	public StringProperty experimentNumProperty() {
		if(experimentNum == null)
			experimentNum = new SimpleStringProperty(this, "num");
		return experimentNum;
	}
	
	private int numAction1;
	private StringProperty action1Percentage;
	public StringProperty action1PercentageProperty() {
		if(action1Percentage == null)
			action1Percentage = new SimpleStringProperty(this, "0.0");
		return action1Percentage;
	}
	
	private int numAction2;
	private StringProperty action2Percentage;
	public StringProperty action2PercentageProperty() {
		if(action2Percentage == null)
			action2Percentage = new SimpleStringProperty(this, "0.0");
		return action2Percentage;
	}
	
	private int numAction3;
	private StringProperty action3Percentage;
	public StringProperty action3PercentageProperty() {
		if(action3Percentage == null)
			action3Percentage = new SimpleStringProperty(this, "0.0");
		return action3Percentage;
	}
	
	private int numAction4;
	private StringProperty action4Percentage;
	public StringProperty action4PercentageProperty() {
		if(action4Percentage == null)
			action4Percentage = new SimpleStringProperty(this, "0.0");
		return action4Percentage;
	}
	
	private int numAction5;
	private StringProperty action5Percentage;
	public StringProperty action5PercentageProperty() {
		if(action5Percentage == null)
			action5Percentage = new SimpleStringProperty(this, "0.0");
		return action5Percentage;
	}
	
	private int numAction6;
	private StringProperty action6Percentage;
	public StringProperty action6PercentageProperty() {
		if(action6Percentage == null)
			action6Percentage = new SimpleStringProperty(this, "0.0");
		return action6Percentage;
	}
	
	private StringProperty initialAction;
	public StringProperty initialActionProperty() {
		if(initialAction == null)
			initialAction = new SimpleStringProperty(this, "No action");
		return initialAction;
	}
	
	// The environment that receives and evaluates actions from the automaton
	private Environment environment;
	
	// The automaton that interacts with the environment and adapts to the responses
	private Automaton automaton;
	
	public Experiment(Environment environment, int experimentNum) {
		this.environment = environment;
		experimentNumProperty().set(String.valueOf(experimentNum));
	}
	
	public void setAutomaton(Automaton automaton) {
		this.automaton = automaton;
	}
	
	/** Run the experiment for the specified number of training and testings iterations and document the results **/
	public void runExperiment() {
		// Run the training iterations
		for(int i = 0; i < TRAINING_ITERATIONS; i++) {
			Action action = automaton.getAction(); // Get the action from the automaton
			// Keep track of the first action taken to see where the automaton started
			if(i == 0)
				initialActionProperty().set(action.toString());
			Response response = environment.getResponse(action); // Send action to environment and receive the response
			automaton.adjustAction(response); // Send the response to the automaton
		}
		
		if(automaton instanceof LRI)
			((LRI) automaton).showActionProbabilities();
		
		numAction1 = 0;
		numAction2 = 0;
		numAction3 = 0;
		numAction4 = 0;
		numAction5 = 0;
		numAction6 = 0;
		
		for(int i = 0; i < TESTING_ITERATIONS; i++) {
			Action action = automaton.getAction();
			countActions(action);
			Response response = environment.getResponse(action);
			automaton.adjustAction(response);
		}
		setActionProperties();
	}
	
	/** Identify the action taken and keep count of the number of times is has been chosen **/
	private void countActions(Action action) {
		if(action == Action.FLOOR_1)
			numAction1++;
		else if(action == Action.FLOOR_2)
			numAction2++;
		else if(action == Action.FLOOR_3)
			numAction3++;
		else if(action == Action.FLOOR_4)
			numAction4++;
		else if(action == Action.FLOOR_5)
			numAction5++;
		else
			numAction6++;
	}
	
	private void setActionProperties() {
		double percentage = ((double) numAction1 / TESTING_ITERATIONS) * 100.0;
		action1PercentageProperty().set(String.valueOf(percentage));
		percentage = ((double) numAction2 / TESTING_ITERATIONS) * 100.0;
		action2PercentageProperty().set(String.valueOf(percentage));
		percentage = ((double) numAction3 / TESTING_ITERATIONS) * 100.0;
		action3PercentageProperty().set(String.valueOf(percentage));
		percentage = ((double) numAction4 / TESTING_ITERATIONS) * 100.0;
		action4PercentageProperty().set(String.valueOf(percentage));
		percentage = ((double) numAction5 / TESTING_ITERATIONS) * 100.0;
		action5PercentageProperty().set(String.valueOf(percentage));
		percentage = ((double) numAction6 / TESTING_ITERATIONS) * 100.0;
		action6PercentageProperty().set(String.valueOf(percentage));
	}
}
