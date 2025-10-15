package dev.prozilla.pine.core.state;

public interface MutableStateProvider<Context, S extends State<Context>> extends StateProvider<Context, S> {
	
	/**
	 * Changes the current state to {@code active}, unless {@code active} is already the current state, then the state is changed to {@code inactive}.
	 * @param active The primary state
	 * @param inactive The secondary state
	 */
	default void toggleState(S active, S inactive) {
		if (getState() != active) {
			changeState(active);
		} else {
			changeState(inactive);
		}
	}
	
	/**
	 * Changes the state of this state machine, only if the current state matches a given state.
	 * @param from Determines what the current state must be in order for this change to be performed.
	 * @param to The new state
	 */
	default void changeState(S from, S to) {
		if (getState() == from) {
			changeState(to);
		}
	}
	
	/**
	 * Exits the current state and enters a new state, unless both are equal.
	 * @param newState The new state to enter
	 */
	void changeState(S newState);

}
