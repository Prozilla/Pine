package dev.prozilla.pine.core.state;

import dev.prozilla.pine.common.ProviderOf;

/**
 * Provides information about the state in a certain context.
 */
@ProviderOf(State.class)
public interface StateProvider<Context, State extends dev.prozilla.pine.core.state.State<Context>> {
	
	/**
	 * @return The current state of the state machine.
	 */
	State getState();
	
	/**
	 * Checks whether the state machine is in a given state.
	 */
	default boolean isState(State state) {
		return getState() == state;
	}
	
	/**
	 * Checks whether the state machine is in any of the given states.
	 */
	default boolean isAnyState(State... states) {
		for (State state : states) {
			if (isState(state)) {
				return true;
			}
		}
		
		return false;
	}
	
}
