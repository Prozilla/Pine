package dev.prozilla.pine.common.property.observable;

import dev.prozilla.pine.common.property.mutable.MutableBooleanProperty;

/**
 * A property with a boolean value that can be observed.
 */
public interface ObservableBooleanProperty extends ObservableProperty<Boolean>, MutableBooleanProperty {
	
	@Override
	default void read(Observer<Boolean> reader) {
		read((BooleanObserver)reader);
	}
	
	@Override
	default Observer<Boolean> addObserver(Observer<Boolean> observer) {
		return addObserver((BooleanObserver)observer);
	}
	
	@Override
	default void removeObserver(Observer<Boolean> observer) {
		removeObserver((BooleanObserver)observer);
	}
	
	/**
	 * @see #read(Observer)
	 */
	default void read(BooleanObserver reader) {
		addObserver(reader);
		reader.observe(get());
	}
	
	BooleanObserver addObserver(BooleanObserver observer);
	
	void removeObserver(BooleanObserver observer);

}
