# Learning Automata

Learning automata (LA) are machine learning technique which are used primarily to solve reinforcement learning problems. This project implements four different kinds of learning automata; the Tsetlin, Krinsky, Krylov, and LRI scheme. Learning is done through experiments which are made up of a number of iterations split into training and testing iterations. In each iteration the automaton chooses an action from a set provided by the environment and the environment responds to this choice with either a reward or a penalty probabilistically. A reward strengthens the automatons confidence, up to a specified maximum value, that the chosen action is the correct one, while a penalty weakens its confidence in the action, to a minimum of zero at which point the automaton switches to a different action. An iteration can be summarized in four steps:
1. LA chooses an action
2. Action is sent to the environment for evaluation
3. Environment either responds with a reward or penalty
4. LA adapts to this response

<h2>Types of Learning Automata Used</h2>

The primary difference between different learning schemes is how to automaton chooses its action and how it adapts to the response from the environment. Learning automata fall into one of two main categories; deterministic and stochastic. With stochastic automata being broken down into two more types fixed structure stochastic automata (FSSA) and variable structure stochastic automata (VSSA). The Tsetlin and Krinsky are examples of deterministic automata while the Krylov and LRI scheme are stochastic. Furthermore, the Krylov is an example of an FSSA and the LRI a VSSA.

<h3>Tsetlin Automaton</h3>
Chooses an initial action uniformly at random with minimum confidence. If the environment responds with a reward the action confidence is increased by one level, or confidence remains the same if it is at the maximum value. If a penalty is received then the confidence is reduced by one level. If the confidence in an action falls below the minimum value the automaton switches to a new action with a minimum level of confidence.

<h3>Krinsky Automaton</h3>
Chooses an initial action uniformly at random with minimum confidence. Upon receiving a reward confidence in the action is set to the maximum confidence level. Penalty behaviour is the same as the Tsetlin.

<h3>Krylov Automaton</h3>
Chooses an initial action uniformly at random with minimum confidence. Reward behaviour is the same the Tsetlin. When a penalty is returned from the environment confidence in the action is either increased or decreased by one level with probability 1/2.

<h3>LRI (Linear Reward-Inaction) Scheme</h3>
The automaton maintains a probability vector and an action is chosen based on the distribution given by the vector every iteration. The probabilities change over time based on the responses from the environment with a user specified learning rate, λ, used to control the speed at which the vector changes. The automaton modifies its probability vector only upon receiving a reward. The action that caused the reward from the environment has its probability of being selected increased and all other actions have their probabilities decreased.

<h2>Project Specifics</h2>

<h3>Problem</h3>
The problem being dealt with in this project is a version of the elevator problem. An elevator in a building with six floors must determine the best floor to wait on between requests so as to minimize the wait time for the next passenger. The probability distribution of requests from one floor to another is unknown but the wait time for each floor can quantified by the equation<br>

**f(x)** = 0.8 * **Q(x)** + 0.4 * CEIL(**Q(x)** / 2) + h where<br>
**x** is the floor the elevator is on, **x ε {1, 2, ..., 6}**<br>
**Q(x)** maps a floor to a unique value by a one-to-one and onto mapping, **Q(x) ε {1, 2, ..., 6}**<br>
**h** is random noise, **h ~ N(0, 1)**<br>

<h3>Experiments</h3>
By default an ensemble of 100 experiments are performed with each experiment being made up of 10,000 training iterations and 1,000 testing iterations. In each iteration a reuqest is made to take a passenger from one floor to another after which the elevator chooses a floor to wait on for the next request. The environment will respond with a reward or a penalty based on this choice. The environment responds probabilistically based on the wait time for the next passenger.
