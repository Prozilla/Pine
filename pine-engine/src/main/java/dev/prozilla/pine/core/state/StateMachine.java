package dev.prozilla.pine.core.state;

import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.util.checks.Checks;

/**
 * Utility class for managing a <a href="https://en.wikipedia.org/wiki/Finite-state_machine">finite-state machine (FSM)</a>.
 */
public class StateMachine<Context, State extends dev.prozilla.pine.core.state.State<Context>> implements MutableStateProvider<Context, State> {
	
	protected State currentState;
	protected final Context context;
	
	/**
	 * Creates a state machine for a given context and enters its initial state.
	 * @param initialState The initial state of the state machine
	 * @param context The context of the state
	 */
	public StateMachine(State initialState, Context context) {
		this.currentState = Checks.isNotNull(initialState, "initialState");
		this.context = context;
		
		try {
			initialState.onEnter(context);
		} catch (Exception e) {
			getLogger().error("Failed to enter initial state: " + initialState, e);
		}
	}
	
	@Override
	public void changeState(State newState) {
		Checks.isNotNull(newState, "newState");
		if (currentState == newState) {
			return;
		}
		
		// Exit previous state
		try {
			currentState.onExit(context);
		} catch (Exception e) {
			getLogger().error("Failed to exit state: " + currentState, e);
		}
		
		// Enter new state
		currentState = newState;
		try {
			newState.onEnter(context);
		} catch (Exception e) {
			getLogger().error("Failed to enter state: " + newState, e);
		}
	}
	
	@Override
	public State getState() {
		return currentState;
	}
	
	protected Logger getLogger() {
		return Logger.system;
	}
	
}
