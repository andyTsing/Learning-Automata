package automata;

import main.Environment.Response;

/**
The LRI Scheme differs the most from the other automata. Instead of choosing actions uniformly
and having a level of confidence in the chosen action the automaton maintains a probability
vector along with a learning rate. The probability vector is modified over time as responses
are received from the environment. The learning rate controls the speed at which the vector
is modified.
**/
public class LRI extends Automaton {
	
	/** Action probability vector**/
	private double[] actionProbabilities;
	
	/** Automata learning rate **/
	private double lambda;

	public LRI() {
		super();
		initializeActionProbabilities();
		lambda = 0.01;
	}
	
	/** 
	Set all probabilities in the vector to be uniform inititally. 
	This is simply a design choice a may vary for different problems. 
	**/
	private void initializeActionProbabilities() {
		actionProbabilities = new double[Action.values().length];
		for(int i = 0; i < Action.values().length; i++)
			actionProbabilities[i] = 1.0 / Action.values().length;
	}
	
	/** 
	Unlike the other three automata a new action is chosen each iteration and therefore the 
	LRI Scheme implements its own getAction() method instead of the one inherited from its superclass. 
	**/
	@Override
	public Action getAction() {
		double u = rand.nextDouble(); // Generate random value from ~U[0, 1]
		double totalProbability = 0; // Track the total cumulative probability from the vector
		//Return the action whose probability causes the cumulative probability to exceed the random value
		for(int i = 0; i < actionProbabilities.length; i++) {
			totalProbability += actionProbabilities[i];
			if(u < totalProbability) {
				action = Action.values()[i];
				break;
			}
		}
		return action;
	}
	
	/** 
	LRI implementation of response adaptation. LRI only adapts vector upon receiving a reward from the environment.
	On reward: Increase the probability of choosing the action that caused the reward and decrease the probability
	of choosing all other actions
	On penalty: Do nothing
	**/
	@Override
	public void adjustAction(Response response) {
		if(response == Response.REWARD) {
			for(int i = 0; i < actionProbabilities.length; i++) {
				if(action == Action.values()[i]) {
					double oldProbability = actionProbabilities[i];
					double newProbability = oldProbability + (lambda * (1 - oldProbability));
					actionProbabilities[i] = newProbability;
				}
				else {
					double oldProbability = actionProbabilities[i];
					double newProbability = (1 - lambda) * oldProbability;
					actionProbabilities[i] = newProbability;
				}
			}
		}
	}
	
	public void showActionProbabilities() {
		for(int i = 0; i < actionProbabilities.length; i++) {
			System.out.print(actionProbabilities[i] + " ");
		}
		System.out.println("");
	}

}
