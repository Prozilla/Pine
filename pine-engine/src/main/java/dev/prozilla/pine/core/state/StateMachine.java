package dev.prozilla.pine.core.state;

import dev.prozilla.pine.common.logging.Logger;

public class StateMachine<Context, State extends dev.prozilla.pine.core.state.State<Context>> {
	
	protected State currentState;
	protected final Context context;
	
	public StateMachine(State initialState, Context context) {
		this.currentState = initialState;
		this.context = context;
		
		try {
			initialState.enter(context);
		} catch (Exception e) {
			getLogger().error("Failed to enter initial state: " + initialState, e);
		}
	}
	
	public void changeState(State from, State to) {
		if (currentState != from) {
			return;
		}
		
		changeState(to);
	}
	
	public void changeState(State newState) {
		if (currentState == newState) {
			return;
		}
		
		// Exit previous state
		try {
			currentState.exit(context);
		} catch (Exception e) {
			getLogger().error("Failed to exit state: " + currentState, e);
		}
		
		// Enter new state
		currentState = newState;
		try {
			newState.enter(context);
		} catch (Exception e) {
			getLogger().error("Failed to enter state: " + newState, e);
		}
	}
	
	public State getState() {
		return currentState;
	}
	
	public boolean isState(State state) {
		return currentState == state;
	}
	
	protected Logger getLogger() {
		return Logger.system;
	}
	
}
