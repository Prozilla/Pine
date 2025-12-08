package dev.prozilla.pine.common.property.observable;

import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.property.mutable.SimpleMutableFloatProperty;
import dev.prozilla.pine.common.util.checks.Checks;

import java.util.ArrayList;
import java.util.List;

public class SimpleObservableFloatProperty extends SimpleMutableFloatProperty implements ObservableFloatProperty, SimpleObservableProperty<Float> {
	
	private final List<FloatObserver> observers;
	protected Logger logger;
	
	/**
	 * Creates an observable property with an initial value.
	 * @param initialValue The initial value
	 */
	public SimpleObservableFloatProperty(float initialValue) {
		super(initialValue);
		observers = new ArrayList<>();
	}
	
	@Override
	public FloatObserver addObserver(FloatObserver observer) {
		Checks.isNotNull(observer, "observer");
		observers.add(observer);
		return observer;
	}
	
	@Override
	public void removeObserver(FloatObserver observer) {
		Checks.isNotNull(observer, "observer");
		observers.remove(observer);
	}
	
	@Override
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	/**
	 * Triggers all observers with the newValue whenever the value changes.
	 * @param oldValue The previous value
	 * @param newValue The new value
	 */
	@Override
	protected void onValueChange(float oldValue, float newValue) {
		for (FloatObserver observer : observers) {
			try {
				observer.observe(newValue);
			} catch (Exception e) {
				getLogger().error(OBSERVER_ERROR, e);
			}
		}
	}
	
	protected Logger getLogger() {
		if (logger == null) {
			return Logger.system;
		}
		return logger;
	}
	
	/**
	 * Removes all observers.
	 */
	@Override
	public void destroy() {
		observers.clear();
		logger = null;
	}
	
}
