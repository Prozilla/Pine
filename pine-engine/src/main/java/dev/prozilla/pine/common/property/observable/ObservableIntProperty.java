package dev.prozilla.pine.common.property.observable;

import dev.prozilla.pine.common.property.mutable.MutableIntProperty;

/**
 * A property with an integer value that can be observed.
 */
public interface ObservableIntProperty extends MutableIntProperty, ObservableProperty<Integer> {
	
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
	 * @see #read(Observer)
	 */
	default void read(IntObserver reader) {
		addObserver(reader);
		reader.observe(get());
	}
	
	IntObserver addObserver(IntObserver observer);
	
	void removeObserver(IntObserver observer);

}
