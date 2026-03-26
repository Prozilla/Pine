package dev.prozilla.pine.common.property.observable;

import dev.prozilla.pine.common.property.Property;

/**
 * A property that triggers observers whenever its value changes.
 */
public interface ObservableProperty<T> extends Property<T> {
	
	String OBSERVER_ERROR = "Observer failed";
	
	/**
	 * Adds an observer that is immediately called with the current value.
	 *
	 * <p>This is the equivalent of calling {@link #getValue()}, then doing something with that value, and then adding an observer which does the same thing each time the value changes.</p>
	 * @param reader The observer
	 * @see #addObserver(Observer)
	 */
	default void read(Observer<T> reader) {
		addObserver(reader);
		reader.observe(getValue());
	}
	
	/**
	 * Adds an observer that observes changes to the value of this property.
	 * @param observer The observer to add.
	 * @return The observer.
	 */
	Observer<T> addObserver(Observer<T> observer);
	
	/**
	 * Removes an observer.
	 * @param observer The observer to remove.
	 */
	void removeObserver(Observer<T> observer);
	
}
