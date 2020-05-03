package automata;

import main.Environment.Response;

public class Krylov extends Automaton {

	public Krylov() {
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
			int u = rand.nextInt();
			if(u < 0.5) {
				actionStrength--;
				if (actionStrength < 1) {
					actionStrength = 1;
					nextAction();
				}
			}
			else {
				actionStrength++;
				if(actionStrength > MAX_ACTION_STRENGTH)
					actionStrength = MAX_ACTION_STRENGTH;
			}
		}
	}

}
