package dev.prozilla.pine.core.state;

public interface State<Context> {
	
	default void enter(Context context) {
	
	}
	
	default void exit(Context context) {
	
	}
	
}
