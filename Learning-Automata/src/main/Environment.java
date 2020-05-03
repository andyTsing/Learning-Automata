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


public class Environment {
	
	public static enum Machine {
		TSETLIN,
		KRINSKY,
		KRYLOV,
		LRI
	}
	
	public static enum Response {
		REWARD,
		PENALTY
	}

	private static final int NUM_EXPERIMENTS = 100;
	
	private HashMap<Action, Integer> G;
	private HashMap<Action, Double> averageWaitTimes;
	private HashMap<Action, Integer> actionCounts;
	private List<Experiment> experiments;
	private Machine automaton;
	private Random rand;
	private Interface view;
	
	public Environment(Interface view) {
		this.view = view;
		rand = new Random();
		createExperiments();
		setG();
	}
	
	private void createExperiments() {
		experiments = new ArrayList<Experiment>();
		for(int i = 1; i <= NUM_EXPERIMENTS; i++) {
			Experiment e = new Experiment(this, i);
			experiments.add(e);
		}
	}
	
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
	
	public void start() {
		for(Experiment experiment: experiments) {
			experiment.setAutomaton(createAutomaton());
			experiment.runExperiment();
		}
		computeAverages();
		view.displayAverages(averageWaitTimes);
	}
	
	public Map.Entry<Integer, Integer> getRequest() {
		int startFloor = rand.nextInt(Automaton.Action.values().length) + 1;
		int endFloor = rand.nextInt(Automaton.Action.values().length) + 1;
		while(startFloor == endFloor) {
			endFloor = rand.nextInt(Automaton.Action.values().length) + 1;
		}
		
		return new AbstractMap.SimpleEntry<Integer, Integer>(startFloor, endFloor);
	}
	
	public void reset() {
		createExperiments();
		setG();
	}
	
	public Response getResponse(Action action) {
		double waitTime = computeWaitTime(action);
		averageWaitTimes.put(action, averageWaitTimes.get(action) + waitTime);
		actionCounts.put(action, actionCounts.get(action) + 1);
		double e = randomExponential();
		if(waitTime < e)
			return Response.REWARD;
		return Response.PENALTY;
	}
	
	private double computeWaitTime(Action action) {
		int g = G.get(action);
		double h = rand.nextGaussian();
		double waitTime = 0.8 * g + 0.4 * Math.ceil(g / 2) + h;
		return waitTime;
	}
	
	private double randomExponential() {
		double u = rand.nextDouble();
		double e = Math.log(1 - u)/ (-0.25);
		return e;
	}
	
	private void computeAverages() {
		for(Map.Entry<Action, Double> averageWaitTime: averageWaitTimes.entrySet()) {
			double average = averageWaitTime.getValue() / actionCounts.get(averageWaitTime.getKey());
			averageWaitTime.setValue(average);
		}
	}
}
