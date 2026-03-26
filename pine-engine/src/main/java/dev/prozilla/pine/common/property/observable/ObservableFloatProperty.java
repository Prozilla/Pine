package dev.prozilla.pine.common.property.observable;

import dev.prozilla.pine.common.property.FloatProperty;

/**
 * A property with a float value that can be observed.
 */
public interface ObservableFloatProperty extends FloatProperty, ObservableProperty<Float> {
	
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
	 * @see #read(Observer)
	 */
	default void read(FloatObserver reader) {
		addObserver(reader);
		reader.observe(get());
	}
	
	/**
	 * @see #addObserver(Observer)
	 */
	FloatObserver addObserver(FloatObserver observer);
	
	/**
	 * @see #removeObserver(Observer)
	 */
	void removeObserver(FloatObserver observer);

}
