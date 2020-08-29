package automata;

import main.Environment.Response;

public class Krylov extends Automaton {

	public Krylov() {
		super();
	}
	
	/** 
	Krylov implementation of response adaptation.
	On reward: increase the confidence in the action by level not exceeding the maximum threshold.
	On penalty: increase or decrease confidence in the action by one level; each with probability 1/2
	**/
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
