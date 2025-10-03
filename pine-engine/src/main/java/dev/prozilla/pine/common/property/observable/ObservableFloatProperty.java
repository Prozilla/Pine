package dev.prozilla.pine.common.property.observable;

import dev.prozilla.pine.common.property.mutable.MutableFloatProperty;

/**
 * A property with a float value that can be observed.
 */
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
	 * @see #read(Observer)
	 */
	default void read(FloatObserver reader) {
		addObserver(reader);
		reader.observe(get());
	}
	
	FloatObserver addObserver(FloatObserver observer);
	
	void removeObserver(FloatObserver observer);

}
