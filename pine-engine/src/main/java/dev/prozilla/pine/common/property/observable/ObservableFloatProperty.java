package dev.prozilla.pine.common.property.observable;

import dev.prozilla.pine.common.property.mutable.MutableFloatProperty;

public interface ObservableFloatProperty extends ObservableProperty<Float>, MutableFloatProperty {
	
	@Override
	default void read(Observer<Float> reader) {
		read((FloatObserver)reader);
	}
	
	@Override
	default Observer<Float> addObserver(Observer<Float> observer) {
		return addObserver((FloatObserver)observer);
	}
	
	@Override
	default void removeObserver(Observer<Float> observer) {
		removeObserver((FloatObserver)observer);
	}
	
	/**
	 * Adds an observer that is immediately called with the current value.
	 *
	 * <p>This is the equivalent of calling {@link #getValue()}, then doing something with that value, and then adding an observer which does the same thing each time the value changes.</p>
	 * @param reader The observer
	 */
	default void read(FloatObserver reader) {
		addObserver(reader);
		reader.observe(get());
	}
	
	FloatObserver addObserver(FloatObserver observer);
	
	void removeObserver(FloatObserver observer);

}
