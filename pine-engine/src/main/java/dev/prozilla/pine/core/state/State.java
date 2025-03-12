package dev.prozilla.pine.core.state;

public interface State<Context> {
	
	default void onEnter(Context context) {
 
	}
	
	default void onExit(Context context) {
	
	}
	
}
