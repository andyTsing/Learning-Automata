package main;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import automata.Automaton;
import automata.Automaton.Action;
import automata.Krinsky;
import automata.Krylov;
import automata.LRI;
import automata.Tsetlin;
import view.Interface;

/**
Environment with which the automata interact to facilitate their learning. For the purpose of this project the environment can
be considered the building in which the elevator resides. The environments main purpose is to respond to
actions from the automata in one of two ways; a reward or a penalty. Rewards incentivize th automata to coninue choosing
the actions which received a reward and to not continue to pick actions that receive a penalty.
**/
public class Environment {
	
	/** Four types of automata that are implemented in this project **/
	public static enum Machine {
		TSETLIN,
		KRINSKY,
		KRYLOV,
		LRI
	}
	
	/** Kinds of responses the environment can return **/
	public static enum Response {
		REWARD,
		PENALTY
	}

	// Number of experiments to be performed
	private static final int NUM_EXPERIMENTS = 100;
	
	// Mapping of floors to their unique value
	private HashMap<Action, Integer> G;
	
	// Average wait times for each floor to identify the one which minimizes passenger wait time
	private HashMap<Action, Double> averageWaitTimes;
	
	// Counters for the number of times the automata chose an action
	private HashMap<Action, Integer> actionCounts;
	
	// List of the experiments
	private List<Experiment> experiments;
	
	// The currently selected type of automaton
	private Machine automaton;
	
	private Random rand;
	private Interface view;
	
	public Environment(Interface view) {
		this.view = view;
		rand = new Random();
		createExperiments();
		setG();
	}
	
	/** Initalize each of the experiements **/
	private void createExperiments() {
		experiments = new ArrayList<Experiment>();
		for(int i = 1; i <= NUM_EXPERIMENTS; i++) {
			Experiment e = new Experiment(this, i);
			experiments.add(e);
		}
	}
	
	/** Set the mapping of floors to values **/
	private void setG() {
		G = new HashMap<Action, Integer>();
		averageWaitTimes = new HashMap<Action, Double>();
		actionCounts = new HashMap<Action, Integer>();
		int maxValue = Automaton.Action.values().length;
		for(Action action: Automaton.Action.values()) {
			int value = rand.nextInt(maxValue) + 1;
			while(G.values().contains(value)) {
				value = rand.nextInt(maxValue) + 1;
			}
			G.put(action, value);
			averageWaitTimes.put(action, 0.0);
			actionCounts.put(action, 0);
		}
	}
	
	public List<Experiment> getExperiments() {
		return experiments;
	}
	
	public void setAutomaton(Machine automaton) {
		this.automaton = automaton;
	}
	
	/** Create the automaton based on the type selected **/
	private Automaton createAutomaton() {
		switch(automaton) {
		case TSETLIN:
			return new Tsetlin();
		case KRINSKY:
			return new Krinsky();
		case KRYLOV:
			return new Krylov();
		case LRI:
			return new LRI();
		default:
			return new Tsetlin();
	}
	}
	
	/** Run the experiments and compute the averages **/
	public void start() {
		for(Experiment experiment: experiments) {
			experiment.setAutomaton(createAutomaton());
			experiment.runExperiment();
		}
		computeAverages();
		view.displayAverages(averageWaitTimes);
	}
	
	/** The request to take a passenger from one floor to another **/
	public Map.Entry<Integer, Integer> getRequest() {
		int startFloor = rand.nextInt(Automaton.Action.values().length) + 1;
		int endFloor = rand.nextInt(Automaton.Action.values().length) + 1;
		while(startFloor == endFloor) {
			endFloor = rand.nextInt(Automaton.Action.values().length) + 1;
		}
		
		return new AbstractMap.SimpleEntry<Integer, Integer>(startFloor, endFloor);
	}
	
	/** Reset the experiments and the mapping **/
	public void reset() {
		createExperiments();
		setG();
	}
	
	/** Return a response to the automaton based on the action received **/
	public Response getResponse(Action action) {
		double waitTime = computeWaitTime(action); // Compute wait time for the next passenger
		averageWaitTimes.put(action, averageWaitTimes.get(action) + waitTime); // Update the wait time averages
		actionCounts.put(action, actionCounts.get(action) + 1); // Increment the counter for the action
		double e = randomExponential(); // Generate a random value from an exponential distribution
		// Return a reward if the wait time is less than the random value otherwise return a penalty
		if(waitTime < e)
			return Response.REWARD;
		return Response.PENALTY;
	}
	
	/** Compute the wait time for the next passenger based on the floor the elevator is waiting on **/
	private double computeWaitTime(Action action) {
		int g = G.get(action); // Get the unique value for the action
		double h = rand.nextGaussian(); // Generate random noise value from standard normal distribution
		double waitTime = 0.8 * g + 0.4 * Math.ceil(g / 2) + h; // Use function to compute wait time
		return waitTime;
	}
	
	/** Generate a random value from an exponential distribution, specifically ~Exp(4) **/
	private double randomExponential() {
		double u = rand.nextDouble(); // Generate random value from uniform distribution
		double e = Math.log(1 - u)/ (-0.25); // Input uniform value into the inverse of the exponential CDF to get exponential value
		return e;
	}
	
	private void computeAverages() {
		for(Map.Entry<Action, Double> averageWaitTime: averageWaitTimes.entrySet()) {
			double average = averageWaitTime.getValue() / actionCounts.get(averageWaitTime.getKey());
			averageWaitTime.setValue(average);
		}
	}
}
