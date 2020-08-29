package automata;

import main.Environment.Response;

public class Krinsky extends Automaton {

	public Krinsky() {
		super();
	}
	
	/** 
	Krinsky implementation of response adaptation.
	On reward: increase the confidence in the action to its maximum value.
	On penalty: decrease the confidence in the action by one level and move to next action
	if confidence is below minimum threshold
	**/
	@Override
	public void adjustAction(Response response) {
		if(response == Response.REWARD) {
			actionStrength = MAX_ACTION_STRENGTH;
		}
		else {
			actionStrength--;
			if(actionStrength < 1) {
				actionStrength = 1;
				nextAction();
			}
		}
	}

}
