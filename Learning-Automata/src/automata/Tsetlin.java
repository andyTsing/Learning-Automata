package automata;

import main.Environment.Response;

public class Tsetlin extends Automaton {
	
	public Tsetlin() {
		super();
	}

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
