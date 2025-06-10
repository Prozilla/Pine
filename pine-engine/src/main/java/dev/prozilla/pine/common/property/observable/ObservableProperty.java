package dev.prozilla.pine.common.property.observable;

import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.property.MutableProperty;
import dev.prozilla.pine.common.util.checks.Checks;

import java.util.ArrayList;
import java.util.List;

/**
 * A property that triggers observers whenever its value changes.
 */
public class ObservableProperty<T> extends MutableProperty<T> {
	
	private final List<Observer<T>> observers;
	protected Logger logger;
	
	/**
	 * Creates an observable property without an initial value.
	 */
	public ObservableProperty() {
		this(null);
	}
	
	/**
	 * Creates an observable property with an initial value.
	 * @param initialValue The initial value
	 */
	public ObservableProperty(T initialValue) {
		super(initialValue);
		observers = new ArrayList<>();
	}
	
	/**
	 * Adds an observer that is immediately called with the current value.
	 *
	 * <p>This is the equivalent of calling {@link #getValue()}, then doing something with that value, and then adding an observer which does the same thing each time the value changes.</p>
	 * @param reader The observer
	 */
	public void read(Observer<T> reader) {
		addObserver(reader);
		reader.observe(getValue());
	}
	
	public void addObserver(Observer<T> observer) {
		Checks.isNotNull(observer, "observer");
		observers.add(observer);
	}
	
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
	
	@Override
	protected void onValueChange(T oldValue, T newValue) {
		for (Observer<T> observer : observers) {
			try {
				observer.observe(newValue);
			} catch (Exception e) {
				if (logger != null) {
					logger.error("Observer failed", e);
				} else {
					e.printStackTrace();
				}
			}
		}
	}
	
}
