package automata;

import main.Environment.Response;

public class Krinsky extends Automaton {

	public Krinsky() {
		super();
	}
	
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
