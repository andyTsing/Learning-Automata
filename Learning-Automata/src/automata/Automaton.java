package automata;

import java.util.Random;

import main.Environment.Response;

public abstract class Automaton {

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
	
	public abstract void adjustAction(Response response);
}
