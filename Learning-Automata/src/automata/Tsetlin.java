package automata;

import main.Environment.Response;

public class Tsetlin extends Automaton {
	
	public Tsetlin() {
		super();
	}

	/** 
	Tsetlin implementation of response adaptation.
	On reward: increase the confidence in the action by one level, not exceeding the maximum threshold
	On penalty: decrease the confidence in the action by one level and move to next action
	if confidence is below minimum threshold
	**/
	@Override
	public void adjustAction(Response response) {
		if(response == Response.REWARD) {
			actionStrength++;
			if(actionStrength > MAX_ACTION_STRENGTH)
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
