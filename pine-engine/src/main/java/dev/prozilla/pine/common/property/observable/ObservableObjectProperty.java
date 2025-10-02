package dev.prozilla.pine.common.property.observable;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.property.mutable.MutableObjectProperty;
import dev.prozilla.pine.common.util.checks.Checks;

import java.util.ArrayList;
import java.util.List;

/**
 * A property that triggers observers whenever its value changes.
 */
public class ObservableObjectProperty<T> extends MutableObjectProperty<T> implements ObservableProperty<T>, Destructible {
	
	private final List<Observer<T>> observers;
	protected Logger logger;
	
	/**
	 * Creates an observable property without an initial value.
	 */
	public ObservableObjectProperty() {
		this(null);
	}
	
	/**
	 * Creates an observable property with an initial value.
	 * @param initialValue The initial value
	 */
	public ObservableObjectProperty(T initialValue) {
		super(initialValue);
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
	protected void onValueChange(T oldValue, T newValue) {
		for (Observer<T> observer : observers) {
			try {
				observer.observe(newValue);
			} catch (Exception e) {
				getLogger().error("Observer failed", e);
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
