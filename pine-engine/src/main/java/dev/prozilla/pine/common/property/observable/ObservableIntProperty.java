package dev.prozilla.pine.common.property.observable;

import dev.prozilla.pine.common.property.mutable.MutableIntProperty;

public interface ObservableIntProperty extends ObservableProperty<Integer>, MutableIntProperty {
	
	@Override
	default void read(Observer<Integer> reader) {
		read((IntObserver)reader);
	}
	
	@Override
	default Observer<Integer> addObserver(Observer<Integer> observer) {
		return addObserver((IntObserver)observer);
	}
	
	@Override
	default void removeObserver(Observer<Integer> observer) {
		removeObserver((IntObserver)observer);
	}
	
	/**
	 * Adds an observer that is immediately called with the current value.
	 *
	 * <p>This is the equivalent of calling {@link #getValue()}, then doing something with that value, and then adding an observer which does the same thing each time the value changes.</p>
	 * @param reader The observer
	 */
	default void read(IntObserver reader) {
		addObserver(reader);
		reader.observe(get());
	}
	
	IntObserver addObserver(IntObserver observer);
	
	void removeObserver(IntObserver observer);

}
