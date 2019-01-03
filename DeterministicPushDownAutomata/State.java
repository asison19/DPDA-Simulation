package DeterministicPushDownAutomata;

import java.util.ArrayList;

public class State {
	ArrayList<Transition> transitions = new ArrayList<Transition>();
	boolean isFinalState = false;

	State() {

	}

	public void addTransition(String input, String pop, int destination, String push) {
		transitions.add(new Transition(input, pop, destination, push));

	}

	public ArrayList<Transition> getTransitions() {
		return transitions;
	}
	
	public boolean isFinalState() {
		return isFinalState;
	}
	
	public void changeStateStatus(boolean status) {
		isFinalState = status;
	}
}