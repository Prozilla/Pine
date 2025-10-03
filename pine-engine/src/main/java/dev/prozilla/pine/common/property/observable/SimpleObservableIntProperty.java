package dev.prozilla.pine.common.property.observable;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.property.mutable.SimpleMutableIntProperty;
import dev.prozilla.pine.common.util.checks.Checks;

import java.util.ArrayList;
import java.util.List;

public class SimpleObservableIntProperty extends SimpleMutableIntProperty implements ObservableIntProperty, Destructible {
	
	private final List<IntObserver> observers;
	protected Logger logger;
	
	/**
	 * Creates an observable property with an initial value.
	 * @param initialValue The initial value
	 */
	public SimpleObservableIntProperty(int initialValue) {
		super(initialValue);
		observers = new ArrayList<>();
	}
	
	@Override
	public IntObserver addObserver(IntObserver observer) {
		Checks.isNotNull(observer, "observer");
		observers.add(observer);
		return observer;
	}
	
	@Override
	public void removeObserver(IntObserver observer) {
		Checks.isNotNull(observer, "observer");
		observers.remove(observer);
	}
	
	/**
	 * Sets the logger of this property, which is used to log errors thrown by observers.
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	/**
	 * Triggers all observers with the newValue whenever the value changes.
	 * @param oldValue The previous value
	 * @param newValue The new value
	 */
	@Override
	protected void onValueChange(int oldValue, int newValue) {
		for (IntObserver observer : observers) {
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
