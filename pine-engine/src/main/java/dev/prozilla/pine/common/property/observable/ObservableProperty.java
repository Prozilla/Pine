package dev.prozilla.pine.common.property.observable;

import dev.prozilla.pine.common.property.mutable.MutableProperty;

/**
 * A property that triggers observers whenever its value changes.
 */
public interface ObservableProperty<T> extends MutableProperty<T> {
	
	String OBSERVER_ERROR = "Observer failed";
	
	/**
	 * Adds an observer that is immediately called with the current value.
	 *
	 * <p>This is the equivalent of calling {@link #getValue()}, then doing something with that value, and then adding an observer which does the same thing each time the value changes.</p>
	 * @param reader The observer
	 */
	default void read(Observer<T> reader) {
		addObserver(reader);
		reader.observe(getValue());
	}
	
	Observer<T> addObserver(Observer<T> observer);
	
	void removeObserver(Observer<T> observer);
	
}
