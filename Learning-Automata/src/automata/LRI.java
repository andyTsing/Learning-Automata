package automata;

import main.Environment.Response;

public class LRI extends Automaton {
	
	private double[] actionProbabilities;
	private double lambda;

	public LRI() {
		super();
		initializeActionProbabilities();
		lambda = 0.01;
	}
	
	private void initializeActionProbabilities() {
		actionProbabilities = new double[Action.values().length];
		for(int i = 0; i < Action.values().length; i++)
			actionProbabilities[i] = 1.0 / Action.values().length;
	}
	
	@Override
	public Action getAction() {
		double u = rand.nextDouble();
		double totalProbability = 0;
		for(int i = 0; i < actionProbabilities.length; i++) {
			totalProbability += actionProbabilities[i];
			if(u < totalProbability) {
				action = Action.values()[i];
				break;
			}
		}
		return action;
	}
	
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
