package DeterministicPushDownAutomata;

public class Transition {
	String input, pop, push;
	int destination;

	Transition() {

	}

	Transition(String input, String pop, int destination, String push) {
		this.input = input;
		this.pop = pop;
		this.destination = destination;
		this.push = push;
	}

	public String getInput() {
		return input;
	}

	public String getPop() {
		return pop;
	}

	public String getPush() {
		return push;
	}

	public int getDestination() {
		return destination;
	}

}