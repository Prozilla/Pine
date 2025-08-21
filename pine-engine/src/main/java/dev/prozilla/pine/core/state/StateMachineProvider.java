package dev.prozilla.pine.core.state;

@FunctionalInterface
public interface StateMachineProvider<Context, S extends State<Context>> extends MutableStateProvider<Context, S> {
	
	StateMachine<Context, S> getStateMachine();
	
	@Override
	default S getState() {
		return getStateMachine().getState();
	}
	
	default void changeState(S newState) {
		getStateMachine().changeState(newState);
	}
	
}
