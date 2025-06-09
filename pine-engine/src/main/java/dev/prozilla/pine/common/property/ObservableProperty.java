package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.util.checks.Checks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A property that triggers observers whenever its value changes.
 */
public class ObservableProperty<T> extends MutableProperty<T> {
	
	private final List<Consumer<T>> observers;
	protected Logger logger;
	
	public ObservableProperty() {
		this(null);
	}
	
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
	public void read(Consumer<T> reader) {
		addObserver(reader);
		reader.accept(getValue());
	}
	
	public void addObserver(Consumer<T> observer) {
		Checks.isNotNull(observer, "observer");
		observers.add(observer);
	}
	
	public void removeObserver(Consumer<T> observer) {
		Checks.isNotNull(observer, "observer");
		observers.remove(observer);
	}
	
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	@Override
	protected void onValueChange(T oldValue, T newValue) {
		for (Consumer<T> observer : observers) {
			try {
				observer.accept(newValue);
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
