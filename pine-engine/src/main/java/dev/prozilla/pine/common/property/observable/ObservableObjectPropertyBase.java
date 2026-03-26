package dev.prozilla.pine.common.property.observable;

import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.util.checks.Checks;

import java.util.ArrayList;
import java.util.List;

public abstract class ObservableObjectPropertyBase<T> implements ObservableObjectProperty<T>, ObservablePropertyBase<T> {
	
	private final List<Observer<T>> observers;
	protected Logger logger;
	
	/**
	 * Creates an observable property with an initial value.
	 */
	public ObservableObjectPropertyBase() {
		observers = new ArrayList<>();
	}
	
	@Override
	public Observer<T> addObserver(Observer<T> observer) {
		Checks.isNotNull(observer, "observer");
		observers.add(observer);
		return observer;
	}
	
	@Override
	public void removeObserver(Observer<T> observer) {
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
	protected void onValueChange(T oldValue, T newValue) {
		for (Observer<T> observer : observers) {
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
