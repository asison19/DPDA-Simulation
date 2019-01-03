/*
 * @author Andrew Sison
 * 
 * The following is a simulation of a DPDA.
 * Lambda is represented as .
 * Start symbol of stack is $
 * Alphabet is anything that isn't the above 
 * Initial State is hard-coded as 0
 */

package DeterministicPushDownAutomata;
import java.util.Scanner;
import java.util.Stack;

public class DeterministicPushdownAutomata {

	// Global variables
	public static int currentState = 0; // current state that we are on
	public static int stateAmount; // the number of states total in our automata
	public static String alphabet; // the alphabet of our automata
	public static Stack<String> stack; // stack of the automata
	public static State[] states; // array of states in our automata
	public static Scanner reader = new Scanner(System.in); // used to read user's input
	public static boolean isStringAcceptable = false; // boolean will be true if the inputed string for the automata is acceptable
	public static boolean wasThereProperTransition = false; // boolean will be true if there was a proper transition from one state to another
	public static String lambda = "."; // we use . as a representation for lambda

	public static void main(String[] args) {

		stack = new Stack<String>();
		stack.push("$"); // start symbol for stack

		// here we get the number of states and create array of states
		System.out.print("Enter the number of states: ");
		stateAmount = reader.nextInt();
		states = new State[stateAmount];
		for (int i = 0; i < stateAmount; i++) // instantiating states
			states[i] = new State();

		//create our alphabet
		System.out.print("Enter the alphabet, not seperated by spaces: ");
		alphabet = reader.next();

		// create our transitions
		System.out.println("Enter the Transitions, or -1 if no more transitions to create: ");
		boolean isTransitionCreationDone = false;
		do {
			int stateSource = reader.nextInt();
			if (stateSource == -1)
				isTransitionCreationDone = true;
			else
				states[stateSource].addTransition(reader.next(), reader.next(), reader.nextInt(), reader.next());
		} while (!isTransitionCreationDone);

		// make final states
		System.out.print("Enter the Final States, seperated by spaces, and then enter -1 to continue: ");
		boolean isFinalStateCreationDone = false;
		do {
			int finalState = reader.nextInt();
			if (finalState == -1)
				isFinalStateCreationDone = true;
			else
				states[finalState].changeStateStatus(true);
		} while (!isFinalStateCreationDone);

		// onto checking strings
		System.out.println("Enter a String to test, and then enter -1 to end the input: ");
		boolean continueTestingStrings = true;
		while (continueTestingStrings) {
			currentState = 0;
			testStrings();
			System.out.println("Continue Testing Strings (y/n)?");
			if (!reader.next().toLowerCase().equals("y"))
				continueTestingStrings = false;
			stack.clear();
			stack.push("$");
			isStringAcceptable = false;
		}

		reader.close();
		System.out.println("Program Complete");
	}

	public static  boolean userInput(String inputedString) {
		int transitionNumber = findProperTransition(inputedString);
		if (transitionNumber >= 0) { // a proper transition was found so transition number did not return negative
			popOutOfStack(transitionNumber);
			pushOntoStack(transitionNumber);
			currentState = states[currentState].getTransitions().get(transitionNumber).getDestination(); // change
																											// currentState
			// to the next location
			wasThereProperTransition = true;
			return false;
		} else { // input has no proper transition, we are done with user input and string is not
					// accepted
			isStringAcceptable = false;
			wasThereProperTransition = false;
			return true;
		}
	}

	public static void testStrings() {
		boolean isInputtingStringDone = false;
		do {
			System.out.print("Current Status: " + currentState + ": ");
			// output what is on the stack
			for (int i = stack.size() - 1; i >= 0; i--)
				System.out.print(stack.elementAt(i));

			System.out.print(", Enter single Input: ");

			String inputedString = reader.next();
			if (!inputedString.equals("-1")) { // if it's -1 we end the string input
				if (alphabet.contains(inputedString) || inputedString.equals(lambda))
					isInputtingStringDone = userInput(inputedString);
				else {
					isInputtingStringDone = true;
					isStringAcceptable = false;
				}

			} else { // if input is -1
				isInputtingStringDone = true;
			}

		} while (!isInputtingStringDone);

		// check if string is acceptable and that user input ended because of proper
		// input, not incorrect transition
		if (states[currentState].isFinalState() && wasThereProperTransition) {
			isStringAcceptable = true;
		} else
			isStringAcceptable = false;

		if (!isStringAcceptable)
			System.out.println("String is not Accepted.");
		else
			System.out.println("String is Accepted.");
	}

	public static void pushOntoStack(int transitionNumber) {
		if (!states[currentState].getTransitions().get(transitionNumber).getPush().equals(".")) // if it does not equal
																								// . we add
		// something
		{
			for (int j = states[currentState].getTransitions().get(transitionNumber).getPush().length()
					- 1; j >= 0; j--) {

				stack.push(Character
						.toString(states[currentState].getTransitions().get(transitionNumber).getPush().charAt(j)));
			}
		}
	}

	public static void popOutOfStack(int transitionNumber) {
		for (int j = 0; j < states[currentState].getTransitions().get(transitionNumber).getPop().length(); j++) {
			if (Character.toString(states[currentState].getTransitions().get(transitionNumber).getPop().charAt(j))
					.equals(stack.peek())) {
				stack.pop();
			}
		}
	}

	public static int findProperTransition(String inputedString) {
		// traverse the transitions of our current state, and check if we've a
		// transition for our inputed string
		for (int i = 0; i < states[currentState].getTransitions().size(); i++) {
			// if we find an input that's the same as inputedString
			if (states[currentState].getTransitions().get(i).getInput().equals(inputedString)) {
				// check if the stack has what's needed
				for (int j = 0; j < states[currentState].getTransitions().get(i).getPop().length(); j++) {
					if (Character.toString(states[currentState].getTransitions().get(i).getPop().charAt(j))
							.equals(stack.elementAt(stack.size() - 1 - j))) {
						return i;
					}
				}
			}
		}
		return -1; // if we get here, there is no proper transition
	}
}
