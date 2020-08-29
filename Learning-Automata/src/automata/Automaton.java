package automata;

import java.util.Random;

import main.Environment.Response;
/**
Abstract class to encapsulate the shared behaviour of all the types of automata.
The structure of most learning automata is very similar with most differences coming from how actions
are chosen and they way in which they respond to responses from the environment.
**/
public abstract class Automaton {

	/** 
	Possible actions the automaton may take in its environment. 
	Represents the six possible floors the elevator may choose to wait on. 
	**/
	public static enum Action {
		FLOOR_1,
		FLOOR_2,
		FLOOR_3,
		FLOOR_4,
		FLOOR_5,
		FLOOR_6
	}
	
	protected static final int MAX_ACTION_STRENGTH = Action.values().length;
	
	protected Action action;
	protected int actionStrength;
	protected Random rand;
	
	public Automaton() {
		rand = new Random();
		randomAction();
		actionStrength = 1;
	}
	
	protected void randomAction() {
		action = Action.values()[rand.nextInt(Action.values().length)];
	}
	
	protected void randomActionStrength() {
		actionStrength = rand.nextInt(MAX_ACTION_STRENGTH) + 1;
	}
	
	/** Move to the next possible action one confidence in the current action has been reduced to below its minimum value **/
	protected void nextAction() {
		int actionIndex = 0;
		for(int i = 0; i < Action.values().length; i++) {
			if(Action.values()[i] == action) {
				actionIndex = i;
				break;
			}
		}
		action = Action.values()[(actionIndex + 1) % Action.values().length];
	}
	
	public Action getAction() { return action; }
	
	/** 
	Each automaton adapts uniquely to responses from its environment 
	and must therefore implement its own method.
	**/
	public abstract void adjustAction(Response response);
}
